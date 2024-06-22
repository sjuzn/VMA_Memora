package sk.upjs.druhypokus.moments.ui

import android.annotation.SuppressLint
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageButton
import androidx.core.content.ContextCompat
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.chip.Chip
import com.google.android.material.chip.ChipDrawable
import com.google.android.material.chip.ChipGroup
import sk.upjs.druhypokus.R
import sk.upjs.druhypokus.main.MemoraApplication
import sk.upjs.druhypokus.moments.MomentTagViewModel
import sk.upjs.druhypokus.moments.Tag

class MomentFragment : Fragment() {

    private val momentTagViewModel: MomentTagViewModel by viewModels {
        MomentTagViewModel.MomentTagViewModelFactory((requireActivity().application as MemoraApplication).momentTagRepository)
    }

    private lateinit var v: View
    private lateinit var recyclerView : RecyclerView
    private var activeFilters : MutableList<String> = mutableListOf()

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
            val momentRecyclerAdapter = MomentRecyclerAdapter(list,requireContext())
            recyclerView.layoutManager = LinearLayoutManager(requireContext())
            recyclerView.adapter = momentRecyclerAdapter
        }

        val itemTouchHelper = ItemTouchHelper(
            MomentFragment.MySimpleCallback(
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

                } else {
                    setChipBackgroundColorResource(R.color.cervena_tmava_zosvetlena)
                    activeFilters.remove(this.text.toString())

                    //recyclerView.adapter?.notifyDataSetChanged()
                }

                activeFilters.forEach { filter ->
                    momentTagViewModel.getTagSMomentami(Tag(filter)).observe(viewLifecycleOwner) { tagWithMomentsList ->

                        Log.i("MOMENTY", tagWithMomentsList.toString())

                        tagWithMomentsList.forEach { tagWithMoment ->
                            val moments = tagWithMoment.moments
                            //Log.i("MOMENTY", moments.toString())
                        }
                    }
                }


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
        }
    }

    companion object {
        @JvmStatic
        fun newInstance() =
            MomentFragment().apply {
                arguments = Bundle().apply {
                }
            }
    }
}