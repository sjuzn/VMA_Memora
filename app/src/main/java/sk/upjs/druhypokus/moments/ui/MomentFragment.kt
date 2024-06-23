package sk.upjs.druhypokus.moments.ui

import android.annotation.SuppressLint
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageButton
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.chip.Chip
import com.google.android.material.chip.ChipDrawable
import com.google.android.material.chip.ChipGroup
import sk.upjs.druhypokus.R
import sk.upjs.druhypokus.main.MemoraApplication
import sk.upjs.druhypokus.moments.Entity.Moment
import sk.upjs.druhypokus.moments.MomentTagViewModel
import sk.upjs.druhypokus.moments.Entity.MomentWithTags
import sk.upjs.druhypokus.moments.Entity.Tag
import java.time.LocalDate
import java.time.format.DateTimeFormatter

class MomentFragment : Fragment() {

    val momentTagViewModel: MomentTagViewModel by viewModels {
        MomentTagViewModel.MomentTagViewModelFactory((requireActivity().application as MemoraApplication).momentTagRepository)
    }

    private lateinit var v: View
    private lateinit var recyclerView: RecyclerView
    private var activeFilters: MutableList<String> = mutableListOf()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {

        v = inflater.inflate(R.layout.fragment_moment, container, false)

        v.findViewById<ImageButton>(R.id.btAdd).setOnClickListener {
            val intent = Intent(requireContext(), AddMomentActivity::class.java)
            startActivity(intent)
        }

        inicializujChipsy()

        recyclerView = v.findViewById(R.id.recyclerView)

        momentTagViewModel.allMoments.observe(viewLifecycleOwner) { list ->
            val momentRecyclerAdapter = MomentRecyclerAdapter(list, requireContext())
            recyclerView.layoutManager = LinearLayoutManager(requireContext())
            recyclerView.adapter = momentRecyclerAdapter
        }

        val itemTouchHelper = ItemTouchHelper(
            MySimpleCallback(
                this,
                ItemTouchHelper.UP or ItemTouchHelper.DOWN or ItemTouchHelper.START or ItemTouchHelper.END,
                ItemTouchHelper.END
            )
        )
        itemTouchHelper.attachToRecyclerView(recyclerView)

        return v
    }

    private fun inicializujChipsy() {

        val chipGroup = v.findViewById<ChipGroup>(R.id.chipGroup)
        chipGroup.removeAllViews()

        momentTagViewModel.allTags.observe(viewLifecycleOwner) {
            for (tag in it) {
                chipGroup?.addView(createTagChip(requireContext(), tag.nazovTag))
            }
        }
    }

    @SuppressLint("NotifyDataSetChanged")
    private fun createTagChip(context: Context, chipName: String): Chip {
        return Chip(context).apply {
            text = chipName
            setChipBackgroundColorResource(R.color.cervena_tmava_zosvetlena)
            isCloseIconVisible = false
            setTextColor(ContextCompat.getColor(context, R.color.white))
            val drawable = ChipDrawable.createFromAttributes(
                context,
                null,
                0,
                com.google.android.material.R.style.Widget_MaterialComponents_Chip_Choice
            )
            setChipDrawable(drawable)
            setChipBackgroundColorResource(R.color.cervena_tmava_zosvetlena)

            setOnCheckedChangeListener { _, isChecked ->

                if (isChecked) {
                    setChipBackgroundColorResource(R.color.cervena_tmava)
                    activeFilters.add(this.text.toString())

                    momentTagViewModel.allMoments.observe(viewLifecycleOwner) { list ->
                        (recyclerView.adapter as MomentRecyclerAdapter).updateMoments(list)
                        // zobrazujem = list.toMutableList()
                    }

                } else {
                    setChipBackgroundColorResource(R.color.cervena_tmava_zosvetlena)
                    activeFilters.remove(this.text.toString())

                    if (activeFilters.isEmpty()) {
                        momentTagViewModel.allMoments.observe(viewLifecycleOwner) { list ->
                            (recyclerView.adapter as MomentRecyclerAdapter).updateMoments(list)

                        }
                    }
                }

                if (activeFilters.isNotEmpty()) {
                    val zobrazujem: MutableSet<Moment> = mutableSetOf()

                    activeFilters.forEach { filter ->
                        momentTagViewModel.getTagSMomentami(Tag(filter))
                            .observe(viewLifecycleOwner) { tagWithMomentsList ->
                                if (tagWithMomentsList.isNotEmpty()) {
                                    tagWithMomentsList.first().moments.forEach { m ->
                                        zobrazujem.add(m)
                                    }
                                }
                                (recyclerView.adapter as MomentRecyclerAdapter).updateMoments(zobrazujem.toMutableList())
                            }
                    }
                }
            }
        }
    }

    companion object {
        @JvmStatic
        fun newInstance() =
            MomentFragment().apply {
                arguments = Bundle().apply {}
            }
    }
}

class MySimpleCallback(
    private val fragment: Fragment,
    dragDirs: Int,
    swipeDirs: Int
) : ItemTouchHelper.SimpleCallback(dragDirs, swipeDirs) {

    override fun onMove(
        recyclerView: RecyclerView,
        viewHolder: RecyclerView.ViewHolder,
        target: RecyclerView.ViewHolder
    ): Boolean {
        return false
    }

    override fun onSwiped(viewHolder: RecyclerView.ViewHolder, direction: Int) {

        val position = viewHolder.adapterPosition
        val zoznam: MutableList<Moment> = mutableListOf()

        (fragment as MomentFragment).momentTagViewModel.allMoments.observe(fragment.viewLifecycleOwner) { moments ->
            zoznam.clear()
            zoznam.addAll(moments)
        }

        sortMomentsByDateDescending(zoznam)
        val chcemZmazat = zoznam[position]

        fragment.momentTagViewModel.getMomentSTagmi(chcemZmazat)
            .observe(fragment.viewLifecycleOwner) { momentWithTagsList ->
                val tagyKuMomentu: MutableList<MomentWithTags> = momentWithTagsList.toMutableList()

                for (t in tagyKuMomentu) {
                    for (konkretny in t.tags) {
                        fragment.momentTagViewModel.deleteMomentTagCrossRefs(konkretny, chcemZmazat)
                    }
                }
                fragment.momentTagViewModel.deleteMoment(chcemZmazat)
            }
    }

    private fun sortMomentsByDateDescending(momentList: MutableList<Moment>) {
        val comparator = Comparator<Moment> { moment1, moment2 ->
            val date1 = LocalDate.parse(moment1.datum, DateTimeFormatter.ISO_DATE)
            val date2 = LocalDate.parse(moment2.datum, DateTimeFormatter.ISO_DATE)
            date2.compareTo(date1)
        }
        momentList.sortWith(comparator)
    }
}