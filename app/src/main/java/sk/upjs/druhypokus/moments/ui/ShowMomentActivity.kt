package sk.upjs.druhypokus.moments.ui

import android.R.attr.path
import android.annotation.SuppressLint
import android.content.Intent
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.net.Uri
import android.os.Bundle
import android.util.Log
import android.widget.ImageButton
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import sk.upjs.druhypokus.R
import sk.upjs.druhypokus.main.MemoraApplication
import sk.upjs.druhypokus.moments.Entity.Moment
import sk.upjs.druhypokus.moments.Entity.MomentWithTags
import sk.upjs.druhypokus.moments.MomentTagViewModel
import java.io.File


class ShowMomentActivity : AppCompatActivity() {
    @SuppressLint("SetTextI18n")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_show_moment)

        findViewById<ImageButton>(R.id.buttonBack).setOnClickListener {
            finish()
        }

        val intent = intent
        val moment = intent.getSerializableExtra("moment") as Moment

        findViewById<TextView>(R.id.nazov).text = moment.nazov
        findViewById<TextView>(R.id.popis).text = moment.text
        findViewById<TextView>(R.id.datum).text = moment.datum

         val momentTagViewModel: MomentTagViewModel by viewModels {
            MomentTagViewModel.MomentTagViewModelFactory((application as MemoraApplication).momentTagRepository)
        }

        val tagyTw = findViewById<TextView>(R.id.tagy)
        tagyTw.text = ""

        momentTagViewModel.getMomentSTagmi(moment).observe(this) { momentWithTagsList ->
            val tagyKuMomentu: MutableList<MomentWithTags> = momentWithTagsList.toMutableList()

            Log.i("TAGY", tagyKuMomentu.toString())

            for(t in tagyKuMomentu.first().tags){
               //for (konkretny in t.tags){
                    tagyTw.text = tagyTw.text.toString() + " " + t.nazovTag

                //}
            }
        }

        findViewById<TextView>(R.id.map).setOnClickListener{

            val uri = "http://maps.google.com/maps?z=10&t=m&q=loc:"+ moment.lat + "+" + moment.lng

            val i = Intent(
                Intent.ACTION_VIEW,
                Uri.parse(uri)
            )
            startActivity(i)
        }


        val obrazok = findViewById<ImageView>(R.id.fotka)
        val fotkaHeader = findViewById<ImageView>(R.id.fotkaHeader)
/*
        contentResolver.openInputStream(Uri.parse(moment.fotka))?.bufferedReader()?.forEachLine {
            val toast = Toast.makeText(applicationContext, it, Toast.LENGTH_SHORT)
            toast.show()
        }

*/

    }
}