package sk.upjs.druhypokus.milniky.crud

import android.app.Activity
import android.app.DatePickerDialog
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.provider.OpenableColumns
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.EditText
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AlertDialog
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import sk.upjs.druhypokus.MemoraApplication
import sk.upjs.druhypokus.R
import sk.upjs.druhypokus.milniky.Milestone
import sk.upjs.druhypokus.milniky.MilestonesViewModel
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Locale


private const val ARG_PARAM1 = "param1"

class MilestoneEditFragment : Fragment() {
    private lateinit var milestone: Milestone

    private val milestonesViewModel: MilestonesViewModel by viewModels {
        MilestonesViewModel.MilestoneViewModelFactory((requireActivity().application as MemoraApplication).milestonesRepository)
    }
    private lateinit var selectImageLauncher: ActivityResultLauncher<Intent>
    private lateinit var bPhoto :Button

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            milestone = it.getSerializable(ARG_PARAM1) as Milestone
        }

        // Initialize the ActivityResultLauncher
        selectImageLauncher = registerForActivityResult(
            ActivityResultContracts.StartActivityForResult()
        ) { result ->
            if (result.resultCode == Activity.RESULT_OK) {
                val fullPhotoUri: Uri? = result.data?.data
                bPhoto.text = fullPhotoUri?.let { getFileName(it) }
                milestone.fotka = fullPhotoUri.toString()
                milestonesViewModel.updateMilestone(milestone)
            }
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_milestone_edit, container, false)

        val bType = view.findViewById<Button>(R.id.bType)
        val bDate = view.findViewById<Button>(R.id.bDate)
        bPhoto = view.findViewById(R.id.bPhoto)
        val bWho = view.findViewById<Button>(R.id.bWho)
        val btDel = view.findViewById<Button>(R.id.btDel)

        bType.text = milestone.typ
        bDate.text = milestone.datum
        bWho.text = milestone.zucastneni

        //https://stackoverflow.com/questions/59340099/how-to-set-confirm-delete-alertdialogue-box-in-kotlin
        btDel.setOnClickListener{
            val builder = AlertDialog.Builder(requireContext())
            builder.setMessage(getString(R.string.sure))
                .setCancelable(false)
                .setPositiveButton("Yes") { _, _ ->
                    milestonesViewModel.delete(milestone)
                    requireActivity().finish()
                }
                .setNegativeButton("No") { dialog, _ ->
                    dialog.dismiss()
                }
            val alert = builder.create()
            alert.show()
        }

        // https://stackoverflow.com/questions/10903754/input-text-dialog-android
        bType.setOnClickListener{
            val txt = EditText(this.context)
            txt.hint = milestone.typ

            AlertDialog.Builder(requireContext())
                .setTitle(R.string.edit)
                .setView(txt)
                .setPositiveButton(R.string.confirm
                ) { _, _ ->
                    val t = "" + txt.text.toString()
                    bType.text = t
                    milestone.typ = t
                    milestonesViewModel.updateMilestone(milestone)
                }
                .setNegativeButton(R.string.no
                ) { _, _ -> }
                .show()
        }

        bWho.setOnClickListener{
            val txt = EditText(this.context)
            txt.hint = milestone.zucastneni

            AlertDialog.Builder(requireContext())
                .setTitle(R.string.edit)
                .setView(txt)
                .setPositiveButton(R.string.confirm
                ) { _, _ ->
                    val t = "" + txt.text.toString()
                    bWho.text = t
                    milestone.zucastneni = t
                    milestonesViewModel.updateMilestone(milestone)
                }
                .setNegativeButton(R.string.no
                ) { _, _ -> }
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
                milestone.datum = formattedDate
                milestonesViewModel.updateMilestone(milestone)
            }, year, month, day)

            // Zobrazenie DatePickerDialog
            datePickerDialog.show()
        }

        bPhoto.setOnClickListener{
            selectImage()
        }

        return view
    }

    private fun selectImage() {
        val intent = Intent(Intent.ACTION_OPEN_DOCUMENT).apply {
            type = "image/*"
            addCategory(Intent.CATEGORY_OPENABLE)
        }
        // opens the standard document browser, filtered to images
        selectImageLauncher.launch(intent)
    }

    private fun getFileName(uri: Uri): String? {
        var fileName: String? = null
        val cursor = requireContext().contentResolver.query(uri, null, null, null, null)
        cursor?.use {
            if (it.moveToFirst()) {
                val columnIndex = it.getColumnIndex(OpenableColumns.DISPLAY_NAME)
                if (columnIndex != -1) {
                    fileName = it.getString(columnIndex)
                }
            }
        }
        return fileName
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