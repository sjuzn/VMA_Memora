package sk.upjs.druhypokus.welcome

import android.annotation.SuppressLint
import android.content.Intent
import android.content.pm.PackageManager
import android.net.Uri
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import sk.upjs.druhypokus.R
import sk.upjs.druhypokus.main.PrefSingleton
import sk.upjs.druhypokus.milniky.Milestone
import java.time.LocalDate
import java.time.format.DateTimeFormatter
import java.time.temporal.ChronoUnit

private const val ARG_PARAM1 = "param1"

class WelcomeFragment : Fragment() {

    lateinit var currView : View
    private lateinit var milestoneList: java.util.ArrayList<Milestone>

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            milestoneList = it.getParcelableArrayList(ARG_PARAM1)!!
        }
    }

    @SuppressLint("SetTextI18n")
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        currView = inflater.inflate(R.layout.fragment_welcome, container, false)

        val meno = PrefSingleton.getInstance().getPreferenceString("meno")
        if (meno != "") currView.findViewById<TextView>(R.id.helloTextView).text = getString(R.string.hi) + ", " + meno

        nastavNadchadzajuce(najdiNajblizsiDatum(milestoneList))

        val recyclerView = currView.findViewById<RecyclerView>(R.id.todayRecyclerView)
        recyclerView.layoutManager = LinearLayoutManager(requireContext())
        val welcomeRecyclerAdapter = WelcomeRecyclerAdapter(milestoneList)
        recyclerView.adapter = welcomeRecyclerAdapter

        return currView
    }

    private fun najdiNajblizsiDatum(milestoneList : List<Milestone>?): Milestone? {
        val formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd")
        val currentDate = LocalDate.now()

        var minDays : Int = Int.MAX_VALUE
        var savedMilestone : Milestone? = null
        if (milestoneList != null) {
            for(m in milestoneList){
                //Toast.makeText(this, m.typ.toString(), Toast.LENGTH_SHORT).show()
                val objDate = LocalDate.parse(m.datum, formatter)
                if(ChronoUnit.DAYS.between(currentDate, objDate).toInt() < minDays){
                    minDays = ChronoUnit.DAYS.between(currentDate, objDate).toInt()
                    savedMilestone = m
                }
            }
        }

        return savedMilestone
    }

    private fun nastavNadchadzajuce(milestone: Milestone?){
        if(milestone != null){
          // currView.findViewById<ImageView>(R.id.imageView).setImageURI(Uri.parse(milestone.fotka))
            currView.findViewById<TextView>(R.id.textView).text = milestone.typ
            currView.findViewById<TextView>(R.id.zaKolkoDni).text = milestone.zucastneni + " | " + milestone.datum

            when {
                ContextCompat.checkSelfPermission(
                    requireContext(), android.Manifest.permission.READ_EXTERNAL_STORAGE
                ) == PackageManager.PERMISSION_GRANTED -> {
                    // mam povolenie
                    currView.findViewById<ImageView>(R.id.imageView).setImageURI(Uri.parse(milestone.fotka))
                }

                shouldShowRequestPermissionRationale(android.Manifest.permission.READ_EXTERNAL_STORAGE) -> {
                    // tu bude alert dialog
                    Toast.makeText(requireContext(), "NO PERMISSION", Toast.LENGTH_SHORT).show()
                }

                else -> {
                    requestPermissionLauncher.launch(android.Manifest.permission.READ_EXTERNAL_STORAGE)
                }
            }

        }
    }

    private val requestPermissionLauncher =
        registerForActivityResult(
            ActivityResultContracts.RequestPermission()
        ) { isGranted: Boolean ->
            if (isGranted) {
                nastavNadchadzajuce(najdiNajblizsiDatum(milestoneList))
            } else {
                Toast.makeText(requireContext(), "PERMISSION MISSING", Toast.LENGTH_SHORT).show()
            }
        }

    companion object {
        @JvmStatic
        fun newInstance(milestone: java.util.ArrayList<Milestone>) =
            WelcomeFragment().apply {
                arguments = Bundle().apply {
                    putParcelableArrayList(ARG_PARAM1, milestone)
                }
            }
    }
}