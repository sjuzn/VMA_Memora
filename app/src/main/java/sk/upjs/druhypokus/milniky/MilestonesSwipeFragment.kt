package sk.upjs.druhypokus.milniky


import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.ViewModelProvider
import androidx.viewpager2.widget.ViewPager2
import sk.upjs.druhypokus.R
import sk.upjs.druhypokus.viewModel.MilestonesViewModel

class MilestonesSwipeFragment : Fragment() {

    private lateinit var viewPager: ViewPager2
    private lateinit var milestoneViewModel: MilestonesViewModel
    private lateinit var adapter: MilestonesSwipeAdapter

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_milestones_swipe, container, false)
        viewPager = view.findViewById(R.id.viewPager)

        adapter = MilestonesSwipeAdapter()
        viewPager.adapter = adapter

        milestoneViewModel = ViewModelProvider(this).get(MilestonesViewModel::class.java)
        /*    milestoneViewModel.allMilestones.observe(viewLifecycleOwner, Observer { milestones : Milestone ->
                milestones?.let { adapter.submitList(it) }
            })*/

        return view
    }

    companion object {
        @JvmStatic
        fun newInstance() = MilestonesSwipeFragment()
    }
}