package sk.upjs.druhypokus.milniky.crud

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.RecyclerView
import sk.upjs.druhypokus.main.MemoraApplication
import sk.upjs.druhypokus.R
import sk.upjs.druhypokus.milniky.Milestone
import sk.upjs.druhypokus.milniky.MilestonesViewModel
import java.util.Collections

class MilestoneReorderFragment : Fragment() {

    private val milestonesViewModel: MilestonesViewModel by viewModels {
        MilestonesViewModel.MilestoneViewModelFactory((requireActivity().application as MemoraApplication).milestonesRepository)
    }
    lateinit var mutableMilestones: MutableList<Milestone>

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_milestone_reorder, container, false)
        val milestonesLiveData = milestonesViewModel.milestones
        val recyclerView = view.findViewById<RecyclerView>(R.id.recyclerView)

        milestonesLiveData.observe(viewLifecycleOwner) { milestones ->
            mutableMilestones = milestones.toMutableList()
            val reorderMRecyclerAdapter = ReorderMRecyclerAdapter(mutableMilestones)
            recyclerView.adapter = reorderMRecyclerAdapter
        }

        val itemTouchHelper = ItemTouchHelper(simpleCallback)
        itemTouchHelper.attachToRecyclerView(recyclerView)

        val btOK = view.findViewById<Button>(R.id.btOK)
        btOK.setOnClickListener {
            milestonesViewModel.replaceAll(mutableMilestones)
            requireActivity().finish()
        }

        return view
    }

    private val simpleCallback: ItemTouchHelper.SimpleCallback = object : ItemTouchHelper.SimpleCallback(
        ItemTouchHelper.UP or ItemTouchHelper.DOWN or ItemTouchHelper.START or ItemTouchHelper.END,
        0
    ) {
        override fun onMove(
            recyclerView: RecyclerView,
            viewHolder: RecyclerView.ViewHolder,
            target: RecyclerView.ViewHolder
        ): Boolean {
            val fromPosition = viewHolder.adapterPosition
            val toPosition = target.adapterPosition

            // Perform the move operation
            try {
                Collections.swap(mutableMilestones, fromPosition, toPosition)
                recyclerView.adapter!!.notifyItemMoved(fromPosition, toPosition)
            } catch (e: IndexOutOfBoundsException) {
                // Handle potential out of bounds exception
            }

            return true
        }

        override fun onSwiped(viewHolder: RecyclerView.ViewHolder, direction: Int) {
            // No swipe action needed
        }
    }

    companion object {
        @JvmStatic
        fun newInstance() =
            MilestoneReorderFragment().apply {
                arguments = Bundle().apply {}
            }
    }
}
