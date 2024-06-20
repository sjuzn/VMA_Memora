package sk.upjs.druhypokus.bucketList

import android.annotation.SuppressLint
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.View.GONE
import android.view.View.OnClickListener
import android.view.View.VISIBLE
import android.view.ViewGroup
import android.widget.EditText
import android.widget.ImageButton
import android.widget.ProgressBar
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.fragment.app.FragmentTransaction
import androidx.fragment.app.viewModels
import androidx.lifecycle.LiveData
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import sk.upjs.druhypokus.MainActivity
import sk.upjs.druhypokus.R
import sk.upjs.druhypokus.main.MemoraApplication
import sk.upjs.druhypokus.milniky.crud.ReorderMRecyclerAdapter
import java.util.Collections

class BucketListFragment : Fragment() {

    private val bListViewModel: BListViewModel by viewModels {
        BListViewModel.BListViewModelFactory((requireActivity().application as MemoraApplication).bListRepository)
    }

    var active = true;


    private lateinit var btActive: TextView
    private lateinit var btDone: TextView
    private lateinit var progressBar: ProgressBar
    private lateinit var progressTextView: TextView
    lateinit var recyclerView: RecyclerView
    private lateinit var imageButtonEdit: ImageButton

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
        }
    }

    @SuppressLint("UseCompatLoadingForDrawables")
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_bucket_list, container, false)
        btActive = view.findViewById(R.id.text_active)
        btDone = view.findViewById(R.id.text_done)
        progressBar = view.findViewById(R.id.progressBar)
        progressTextView = view.findViewById(R.id.progressTextView)
        recyclerView = view.findViewById(R.id.recyclerView)
        imageButtonEdit = view.findViewById(R.id.imageButtonEdit)

        init(false)

        btActive.setOnClickListener {
            btActive.background = requireContext().getDrawable(R.drawable.underline)
            btDone.setBackgroundResource(0);
            active = true;
            init(false)
        }

        btDone.setOnClickListener {
            btDone.background = requireContext().getDrawable(R.drawable.underline)
            btActive.setBackgroundResource(0);
            active = false;
            init(true)
        }

        imageButtonEdit.setOnClickListener(addNewTask)

        val itemTouchHelper = ItemTouchHelper(
            MySimpleCallback(
                this,
                ItemTouchHelper.UP or ItemTouchHelper.DOWN or ItemTouchHelper.START or ItemTouchHelper.END,
                ItemTouchHelper.END
            )
        )
        itemTouchHelper.attachToRecyclerView(recyclerView)

        return view
    }

    private fun filtrujList(stav: Boolean): List<BList> {

        val blist = bListViewModel.bList
        var novyList = mutableListOf<BList>()

        blist.observe(viewLifecycleOwner) { bList ->

            for (a in bList) {
                if (a.hotovo == stav) {
                    novyList.add(a)
                }
            }
        }

        return novyList
    }

    @SuppressLint("SetTextI18n")
    private fun init(stav: Boolean) {
        val blist = bListViewModel.bList
        blist.observe(viewLifecycleOwner) { bList ->
            if (bList.isEmpty()) {
                progressBar.progress = 100
                progressTextView.text =
                    (progressTextView.text.toString().split(" ").get(0)) + (" (0/0)")

            } else {
                var pocetHotovo = 0
                var celkovyPocet = 0
                for (task in bList) {
                    celkovyPocet++
                    if (task.hotovo) pocetHotovo++
                }

                val progressPercentage = (pocetHotovo.toDouble() / celkovyPocet) * 100
                progressBar.progress = progressPercentage.toInt()

                progressTextView.text =
                    (progressTextView.text.toString().split(" ")
                        .get(0)) + (" (" + pocetHotovo + "/" + celkovyPocet + ")")

                val blistRecyclerAdapter = BListRecyclerAdapter(filtrujList(stav), bListViewModel)
                recyclerView.layoutManager = LinearLayoutManager(requireContext())
                recyclerView.adapter = blistRecyclerAdapter
            }
        }
    }

    private val addNewTask = View.OnClickListener {
        val txt = EditText(requireContext())

        AlertDialog.Builder(requireContext())
            .setTitle(R.string.new_task)
            .setView(txt)
            .setPositiveButton(R.string.confirm) { _, _ ->
                val t = "" + txt.text.toString()
                bListViewModel.insert(BList(t, false))
            }
            .setNegativeButton(R.string.no) { _, _ -> }
            .show()
    }

    /*
        private val simpleCallback: ItemTouchHelper.SimpleCallback =
            object : ItemTouchHelper.SimpleCallback(
                ItemTouchHelper.UP or ItemTouchHelper.DOWN or ItemTouchHelper.START or ItemTouchHelper.END,
                ItemTouchHelper.END
            ) {
                override fun onMove(
                    recyclerView: RecyclerView,
                    viewHolder: RecyclerView.ViewHolder,
                    target: RecyclerView.ViewHolder
                ): Boolean {
                    return false
                }

                override fun onSwiped(viewHolder: RecyclerView.ViewHolder, direction: Int) {
                    val position = viewHolder.adapterPosition
                    var relevantnyZoznam = filtrujList(!active)


                    val chcemVymazat = relevantnyZoznam[position]
                    bListViewModel.delete(chcemVymazat)
                    recyclerView.adapter!!.notifyItemRemoved(position)
                    (relevantnyZoznam as ArrayList<BList>).remove(chcemVymazat)

                    if (relevantnyZoznam.isEmpty())
                        recyclerView.visibility = GONE

                    Toast.makeText(requireContext(), R.string.success_delete, Toast.LENGTH_LONG).show()
                }
            }
    */

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
            var relevantnyZoznam =
                (fragment as BucketListFragment).filtrujList(!fragment.active)

            val chcemVymazat = relevantnyZoznam[position]
            (fragment).bListViewModel.delete(chcemVymazat)
            (fragment as BucketListFragment).recyclerView.adapter!!.notifyItemRemoved(position)
            (relevantnyZoznam as ArrayList<BList>).remove(chcemVymazat)

            // TOTO NECHYTAT INAK TO NEPOJDE
            if (relevantnyZoznam.size == 0) {
                Toast.makeText(fragment.requireContext(),R.string.success_delete, Toast.LENGTH_SHORT)
                     .show()
                (fragment.requireActivity() as MainActivity).restartFragment(fragment.id)
            }


            Toast.makeText(fragment.requireContext(), R.string.success_delete, Toast.LENGTH_SHORT).show()
        }
    }

    companion object {
        @JvmStatic
        fun newInstance() =
            BucketListFragment().apply {
                arguments = Bundle().apply {
                }
            }
    }
}