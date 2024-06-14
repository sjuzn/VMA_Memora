package sk.upjs.druhypokus.milniky

import android.app.DatePickerDialog
import android.content.DialogInterface
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.EditText
import androidx.appcompat.app.AlertDialog
import androidx.fragment.app.Fragment
import sk.upjs.druhypokus.R
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Locale


private const val ARG_PARAM1 = "param1"

class MilestoneEditFragment : Fragment() {
    private var milestone: Milestone? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            milestone = it.getSerializable(ARG_PARAM1) as Milestone
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_milestone_edit, container, false)

        val bType = view.findViewById<Button>(R.id.bType)
        val bDate = view.findViewById<Button>(R.id.bDate)
        val bPhoto = view.findViewById<Button>(R.id.bPhoto)
        val bWho = view.findViewById<Button>(R.id.bWho)

        bType.text = milestone?.typ
        bDate.text = milestone?.datum
        bWho.text = milestone?.zucastneni

        // https://stackoverflow.com/questions/10903754/input-text-dialog-android
        bType.setOnClickListener{
            val txt = EditText(this.context)
            txt.hint = milestone?.typ

            AlertDialog.Builder(requireContext())
                .setTitle(R.string.edit)
                .setView(txt)
                .setPositiveButton(R.string.confirm,
                    DialogInterface.OnClickListener { dialog, whichButton ->
                        val t = txt.text.toString()
                        bType.text = t
                        // TODO zapisat do databazy
                    })
                .setNegativeButton(R.string.no,
                    DialogInterface.OnClickListener { dialog, whichButton -> })
                .show()
        }

        bDate.setOnClickListener{
            // Získanie aktuálneho dátumu
            val calendar = Calendar.getInstance()
            val year = calendar.get(Calendar.YEAR)
            val month = calendar.get(Calendar.MONTH)
            val day = calendar.get(Calendar.DAY_OF_MONTH)

            // Vytvorenie a zobrazenie DatePickerDialog
            val datePickerDialog = DatePickerDialog(requireContext(), { _, selectedYear, selectedMonth, selectedDay ->
                // Tu môžete spracovať vybraný dátum, napríklad uložiť ho do premennej alebo aktualizovať text tlačidla
                val selectedDate = Calendar.getInstance()
                selectedDate.set(selectedYear, selectedMonth, selectedDay)

                // Tu môžete aktualizovať text na tlačidle alebo robiť iné operácie s vybraným dátumom
                val formattedDate = SimpleDateFormat("yyyy-MM-dd", Locale.getDefault()).format(selectedDate.time)
                bDate.text = formattedDate

                // TODO zapisat do databazy
/*
                val milestonesViewModel : MilestonesViewModel by activity.viewModels {
                    MilestonesViewModel.MilestoneViewModelFactory((activity.application as MemoraApplication).milestonesRepository)
                }
*/
            }, year, month, day)

            // Zobrazenie DatePickerDialog
            datePickerDialog.show()
        }

        return view
    }

    companion object {
        @JvmStatic
        fun newInstance(milestone: Milestone) =
            MilestoneEditFragment().apply {
                arguments = Bundle().apply {
                    putSerializable(ARG_PARAM1, milestone)
                }
            }
    }
}