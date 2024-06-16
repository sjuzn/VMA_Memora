package sk.upjs.druhypokus.milniky.crud

import android.app.Activity
import android.app.DatePickerDialog
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.provider.OpenableColumns
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AlertDialog
import androidx.fragment.app.viewModels
import sk.upjs.druhypokus.MemoraApplication
import sk.upjs.druhypokus.R
import sk.upjs.druhypokus.milniky.Milestone
import sk.upjs.druhypokus.milniky.MilestonesViewModel
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Locale

class MilestoneAddFragment : Fragment() {

    private val milestonesViewModel: MilestonesViewModel by viewModels {
        MilestonesViewModel.MilestoneViewModelFactory((requireActivity().application as MemoraApplication).milestonesRepository)
    }
    private lateinit var selectImageLauncher: ActivityResultLauncher<Intent>
    private lateinit var bPhoto :Button
    private var typ : String = ""
    private var datum : String = ""
    private var fotka : String = ""
    private var kto : String = ""

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        // Initialize the ActivityResultLauncher
        selectImageLauncher = registerForActivityResult(
            ActivityResultContracts.StartActivityForResult()
        ) { result ->
            if (result.resultCode == Activity.RESULT_OK) {
                val fullPhotoUri: Uri? = result.data?.data
                bPhoto.text = fullPhotoUri?.let { getFileName(it) }
                fotka = fullPhotoUri.toString()
            }
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_milestones_create, container, false)

        val bType = view.findViewById<Button>(R.id.bType)
        val bDate = view.findViewById<Button>(R.id.bDate)
        bPhoto = view.findViewById(R.id.bPhoto)
        val bWho = view.findViewById<Button>(R.id.bWho)
        val btSave = view.findViewById<Button>(R.id.btSave)

        btSave.setOnClickListener{
            if((typ == "") or (datum == "") or (fotka == "") or (kto == "")){
                val txt = TextView(this.context)
                txt.text = getString(R.string.unfilled)

                AlertDialog.Builder(requireContext())
                    .setTitle(R.string.warning)
                    .setView(txt)
                    .setPositiveButton(R.string.confirm) { _, _ -> }
                    .show()
            }else{
                milestonesViewModel.insert(Milestone(typ, datum, fotka, kto))
                requireActivity().finish()
            }
        }

        bType.setOnClickListener{
            val txt = EditText(this.context)
            txt.hint = bType.hint

            AlertDialog.Builder(requireContext())
                .setTitle(R.string.edit)
                .setView(txt)
                .setPositiveButton(R.string.confirm
                ) { _, _ ->
                    typ = "" + txt.text.toString()
                    bType.text = typ
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
                datum = formattedDate
                bDate.text = datum

            }, year, month, day)

            // Zobrazenie DatePickerDialog
            datePickerDialog.show()
        }

        bWho.setOnClickListener{
            val txt = EditText(this.context)
            txt.hint = bWho.hint

            AlertDialog.Builder(requireContext())
                .setTitle(R.string.edit)
                .setView(txt)
                .setPositiveButton(R.string.confirm
                ) { _, _ ->
                    kto = "" + txt.text.toString()
                    bWho.text = kto
                }
                .setNegativeButton(R.string.no
                ) { _, _ -> }
                .show()
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
        fun newInstance() =
            MilestoneAddFragment().apply {
                arguments = Bundle().apply {
                }
            }
    }
}