package sk.upjs.druhypokus.moments.ui

import android.app.Activity
import android.app.DatePickerDialog
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.provider.OpenableColumns
import android.text.Editable
import android.util.Log
import android.widget.Button
import android.widget.EditText
import android.widget.ImageButton
import android.widget.TextView
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import androidx.activity.viewModels
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import kotlinx.coroutines.launch
import sk.upjs.druhypokus.R
import sk.upjs.druhypokus.main.MemoraApplication
import sk.upjs.druhypokus.moments.Entity.Moment
import sk.upjs.druhypokus.moments.Entity.MomentTagCrossRef
import sk.upjs.druhypokus.moments.MomentTagViewModel
import sk.upjs.druhypokus.moments.Entity.Tag
import java.text.SimpleDateFormat
import java.util.ArrayList
import java.util.Calendar
import java.util.Locale
import kotlin.math.abs

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
    private var latitude: Float = 0.0f
    private var longitude: Float = 0.0f
    private var selectedTags: MutableList<String> = mutableListOf()
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

        bMap.setOnClickListener {
            val intent = Intent(this, MapaActivity::class.java)
            startActivityForResult(intent, 2)
        }

        btSave.setOnClickListener {
            if ((title == "") or (datum == "") or (fotka == "") or (poznamka == "") or (latitude == 0.0f) or (latitude == 0.0f)) {
                val txt = TextView(this)
                txt.text = getString(R.string.unfilled)

                AlertDialog.Builder(this)
                    .setTitle(R.string.warning)
                    .setView(txt)
                    .setPositiveButton(R.string.confirm) { _, _ -> }
                    .show()

            } else {

                lifecycleScope.launch {
                    val moment = Moment(
                        title,
                        poznamka,
                        fotka,
                        datum,
                        latitude,
                        longitude,
                        0 // Initially 0, as it will be set by the database
                    )

                    val momentId = momentTagViewModel.insertMoment(moment)
                    if (momentId != -1L) {
                        for (t in selectedTags) {
                            momentTagViewModel.insertTag(Tag(t))
                            momentTagViewModel.insertMomentTagCrossRef(MomentTagCrossRef(momentId.toInt(), t))
                        }
                    } else {
                        Log.e("Chyba", "Daco sa zase fest pokazilo a nezapisali sa data")
                    }
                    finish()
                }
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
            if (bText.text != "") {
                txt.text = Editable.Factory.getInstance().newEditable(bText.text)
            } else {
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

        bTags.setOnClickListener {
            val intent = Intent(this, AddTagActivity::class.java)
            startActivityForResult(intent, 1)
        }

        bPhoto.setOnClickListener {
            selectImage()
        }
    }

    @Deprecated("")
    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        //toto su tagy
        if (requestCode == 1) {
            val tags: ArrayList<String>? = data?.getStringArrayListExtra("selectedTags")

            tags?.let {
                selectedTags.clear()
                selectedTags.addAll(it)

                // Tu môžete aktualizovať zobrazenie tlačidla alebo robiť iné operácie s vybranými tagmi
                bTags.text = selectedTags.joinToString(", ")
            }
        }

        //toto je mapa
        if (requestCode == 2) {
            latitude = data?.getFloatExtra("latitude", 0.0f)!!
            longitude = data.getFloatExtra("longitude", 0.0f)
            bMap.text = formatCoordinates(latitude, longitude)
        }
    }

    private fun formatCoordinates(latitude: Float, longitude: Float): String {
        val latDirection = if (latitude >= 0) "N" else "S"
        val lonDirection = if (longitude >= 0) "E" else "W"

        val formattedLatitude = String.format("%.5f° %s", abs(latitude), latDirection)
        val formattedLongitude = String.format("%.5f° %s", abs(longitude), lonDirection)

        return "$formattedLatitude\n$formattedLongitude"
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