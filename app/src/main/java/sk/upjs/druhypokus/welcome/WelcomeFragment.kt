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
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import sk.upjs.druhypokus.R
import sk.upjs.druhypokus.main.MemoraApplication
import sk.upjs.druhypokus.main.PrefSingleton
import sk.upjs.druhypokus.milniky.Milestone
import sk.upjs.druhypokus.milniky.MilestonesViewModel
import java.time.LocalDate
import java.time.format.DateTimeFormatter
import java.time.temporal.ChronoUnit

class WelcomeFragment : Fragment() {

    private lateinit var currView : View
    private val milestonesViewModel: MilestonesViewModel by viewModels {
        MilestonesViewModel.MilestoneViewModelFactory((requireActivity().application as MemoraApplication).milestonesRepository)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {}
    }

    @SuppressLint("SetTextI18n")
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        currView = inflater.inflate(R.layout.fragment_welcome, container, false)

        val meno = PrefSingleton.getInstance().getPreferenceString("meno")
        if (meno != "") currView.findViewById<TextView>(R.id.helloTextView).text = getString(R.string.hi) + ", " + meno

        val recyclerView = currView.findViewById<RecyclerView>(R.id.todayRecyclerView)
        recyclerView.layoutManager = LinearLayoutManager(requireContext())

        milestonesViewModel.milestones.observe(viewLifecycleOwner) { milestones ->
            milestones?.let {
                val welcomeRecyclerAdapter = WelcomeRecyclerAdapter(listIbaDnesnych(it))
                recyclerView.adapter = welcomeRecyclerAdapter
                nastavNadchadzajuce(najdiNajblizsiDatum(it))
            }
        }

        return currView
    }

    private fun najdiNajblizsiDatum(milestoneList: List<Milestone>?): Milestone? {
        val formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd")
        val currentDate = LocalDate.now()

        var minDays: Int = Int.MAX_VALUE
        var savedMilestone: Milestone? = null

        if (milestoneList != null) {
            for (m in milestoneList) {
                val objDate = LocalDate.parse(m.datum, formatter)

                // Zmeníme rok na aktuálny rok, aby sme porovnávali len deň a mesiac
                val thisYearObjDate = objDate.withYear(currentDate.year)

                // Ak je dátum už prešiel tento rok, porovnávame s budúcim rokom
                val nextDate = if (thisYearObjDate.isBefore(currentDate) || thisYearObjDate.isEqual(currentDate)) {
                    thisYearObjDate.plusYears(1)
                } else {
                    thisYearObjDate
                }

                val daysBetween = ChronoUnit.DAYS.between(currentDate, nextDate).toInt()

                if (daysBetween < minDays) {
                    minDays = daysBetween
                    savedMilestone = m
                }
            }
        }
        return savedMilestone
    }

    private fun listIbaDnesnych(milestoneList: List<Milestone>?): List<Milestone> {
        val formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd")
        val currentDate = LocalDate.now()

        val dnesne = mutableListOf<Milestone>()

        if (milestoneList != null) {
            for (m in milestoneList) {
                val objDate = LocalDate.parse(m.datum, formatter)
                // Skontrolujeme, či majú rovnaký deň a mesiac
                if (objDate.dayOfMonth == currentDate.dayOfMonth && objDate.month == currentDate.month) {
                    dnesne.add(m)
                }
            }
        }

        if(dnesne.isEmpty()){
           // dnesne.add()
        }
        return dnesne
    }



    private fun nastavNadchadzajuce(milestone: Milestone?){
        if(milestone != null){
            currView.findViewById<TextView>(R.id.textView).text = milestone.typ
            currView.findViewById<TextView>(R.id.zaKolkoDni).text = milestone.zucastneni + " | " + milestone.datum

            when {
                ContextCompat.checkSelfPermission(
                    requireContext(), android.Manifest.permission.READ_MEDIA_IMAGES
                ) == PackageManager.PERMISSION_GRANTED -> {
                    // mam povolenie
                    currView.findViewById<ImageView>(R.id.imageView).setImageURI(Uri.parse(milestone.fotka))
                }

                shouldShowRequestPermissionRationale(android.Manifest.permission.READ_MEDIA_IMAGES) -> {
                    // tu bude alert dialog
                    Toast.makeText(requireContext(), "NO PERMISSION", Toast.LENGTH_SHORT).show()
                }

                else -> {
                    requestPermissionLauncher.launch(android.Manifest.permission.READ_MEDIA_IMAGES)
                }
            }
        }
    }

    private val requestPermissionLauncher =
        registerForActivityResult(
            ActivityResultContracts.RequestPermission()
        ) { isGranted: Boolean ->
            if (isGranted) {
                milestonesViewModel.milestones.observe(viewLifecycleOwner) { milestones ->
                    milestones?.let {
                        nastavNadchadzajuce(najdiNajblizsiDatum(it))
                    }
                }
            } else {
                Toast.makeText(requireContext(), "PERMISSION MISSING", Toast.LENGTH_SHORT).show()
            }
        }

    companion object {
        @JvmStatic
        fun newInstance() =
            WelcomeFragment().apply {
                arguments = Bundle().apply {}
            }
    }
}