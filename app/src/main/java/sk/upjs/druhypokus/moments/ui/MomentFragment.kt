package sk.upjs.druhypokus.moments.ui

import android.content.Context
import android.content.Intent
import android.os.Bundle
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
import com.google.android.material.chip.ChipGroup
import sk.upjs.druhypokus.R
import sk.upjs.druhypokus.main.MemoraApplication
import sk.upjs.druhypokus.moments.MomentTagViewModel

class MomentFragment : Fragment() {

    private val momentTagViewModel: MomentTagViewModel by viewModels {
        MomentTagViewModel.MomentTagViewModelFactory((requireActivity().application as MemoraApplication).momentTagRepository)
    }

    lateinit var v : View

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

        v.findViewById<ImageButton>(R.id.btAdd).setOnClickListener{
            val intent = Intent(requireContext(), AddMomentActivity::class.java)
            startActivity(intent)
        }

        inicializujChipsy()

        val recyclerView = v.findViewById<RecyclerView>(R.id.recyclerView)
        momentTagViewModel.allMoments.observe(viewLifecycleOwner) { list ->
            val momentRecyclerAdapter = MomentRecyclerAdapter( list, requireContext())
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

    private fun inicializujChipsy(){

        val chipGroup = v.findViewById<ChipGroup>(R.id.chipGroup)

        momentTagViewModel.allTags.observe(viewLifecycleOwner){
            for (tag in it){
                chipGroup?.addView(createTagChip(requireContext(), tag.nazovTag))
            }
        }
    }

    private fun createTagChip(context: Context, chipName: String): Chip {
        return Chip(context).apply {
            text = chipName
            setChipBackgroundColorResource(R.color.cervena_tmava_zosvetlena)
            isCloseIconVisible = false
            setTextColor(ContextCompat.getColor(context, R.color.white))
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