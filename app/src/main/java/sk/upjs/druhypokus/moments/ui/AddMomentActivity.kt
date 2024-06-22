package sk.upjs.druhypokus.moments.ui

import android.app.Activity
import android.app.DatePickerDialog
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.provider.OpenableColumns
import android.text.Editable
import android.widget.Button
import android.widget.EditText
import android.widget.ImageButton
import android.widget.TextView
import androidx.activity.enableEdgeToEdge
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import androidx.activity.viewModels
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.viewModels
import sk.upjs.druhypokus.R
import sk.upjs.druhypokus.main.MemoraApplication
import sk.upjs.druhypokus.milniky.Milestone
import sk.upjs.druhypokus.moments.MomentTagViewModel
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Locale

class AddMomentActivity : AppCompatActivity() {

    private val momentTagViewModel: MomentTagViewModel by viewModels {
        MomentTagViewModel.MomentTagViewModelFactory((application as MemoraApplication).momentTagRepository)
    }

    private lateinit var selectImageLauncher: ActivityResultLauncher<Intent>

    private lateinit var buttonBack: ImageButton
    private lateinit var bTitle: Button
    private lateinit var bText: Button
    private lateinit var bPhoto: Button
    private lateinit var bDate: Button
    private lateinit var bMap: Button
    private lateinit var bTags: Button
    private lateinit var btSave: Button

    private var title: String = ""
    private var poznamka: String = ""
    private var datum: String = ""
    private var fotka: String = ""
   //tags
    //map

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_add_moment)

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

        buttonBack = findViewById(R.id.buttonBack)
        bTitle = findViewById(R.id.bTitle)
        bText = findViewById(R.id.bText)
        bDate = findViewById(R.id.bDate)
        bPhoto = findViewById(R.id.bPhoto)
        bMap = findViewById(R.id.bMap)
        bTags = findViewById(R.id.bTags)
        btSave = findViewById(R.id.btSave)


        buttonBack.setOnClickListener {
            finish()
        }

//TODO dokoncit na konci
        btSave.setOnClickListener {
            if ((title == "") or (datum == "") or (fotka == "") or (poznamka == "")) {
                val txt = TextView(this)
                txt.text = getString(R.string.unfilled)

                AlertDialog.Builder(this)
                    .setTitle(R.string.warning)
                    .setView(txt)
                    .setPositiveButton(R.string.confirm) { _, _ -> }
                    .show()

            } else {
              //  milestonesViewModel.insert(Milestone(typ, datum, fotka, kto))
              //  requireActivity().finish()
            }
        }

        bTitle.setOnClickListener {
            val txt = EditText(this)
            txt.hint = bTitle.hint

            AlertDialog.Builder(this)
                .setTitle(R.string.edit)
                .setView(txt)
                .setPositiveButton(
                    R.string.confirm
                ) { _, _ ->
                    title = "" + txt.text.toString()
                    bTitle.text = title
                }
                .setNegativeButton(
                    R.string.no
                ) { _, _ -> }
                .show()
        }

        bText.setOnClickListener {
            val txt = EditText(this)
            txt.height = 1000
            if(bText.text != ""){
                txt.text = Editable.Factory.getInstance().newEditable(bText.text)
            }else{
                txt.hint = bText.hint
            }


            AlertDialog.Builder(this)
                .setTitle(R.string.edit)
                .setView(txt)
                .setPositiveButton(
                    R.string.confirm
                ) { _, _ ->
                    poznamka = "" + txt.text.toString()
                    bText.text = poznamka
                }
                .setNegativeButton(
                    R.string.no
                ) { _, _ -> }
                .show()
        }

        bDate.setOnClickListener {
            // Získanie aktuálneho dátumu
            val calendar = Calendar.getInstance()
            val year = calendar.get(Calendar.YEAR)
            val month = calendar.get(Calendar.MONTH)
            val day = calendar.get(Calendar.DAY_OF_MONTH)

            // Vytvorenie a zobrazenie DatePickerDialog
            val datePickerDialog =
                DatePickerDialog(this, { _, selectedYear, selectedMonth, selectedDay ->
                    // Tu môžete spracovať vybraný dátum, napríklad uložiť ho do premennej alebo aktualizovať text tlačidla
                    val selectedDate = Calendar.getInstance()
                    selectedDate.set(selectedYear, selectedMonth, selectedDay)

                    // Tu môžete aktualizovať text na tlačidle alebo robiť iné operácie s vybraným dátumom
                    val formattedDate = SimpleDateFormat(
                        "yyyy-MM-dd",
                        Locale.getDefault()
                    ).format(selectedDate.time)
                    datum = formattedDate
                    bDate.text = datum

                }, year, month, day)

            // Zobrazenie DatePickerDialog
            datePickerDialog.show()
        }

        bPhoto.setOnClickListener {
            selectImage()
        }
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
        val cursor = this.contentResolver.query(uri, null, null, null, null)
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
}