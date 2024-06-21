package sk.upjs.druhypokus.moments.ui

import android.os.Bundle
import android.widget.Button
import android.widget.ImageButton
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import sk.upjs.druhypokus.R

class AddMomentActivity : AppCompatActivity() {

    lateinit var buttonBack : ImageButton
    lateinit var bTitle : Button
    lateinit var bText : Button
    lateinit var bPhoto : Button
    lateinit var bDate : Button
    lateinit var bMap : Button
    lateinit var bTags : Button
    lateinit var btSave : Button


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_add_moment)






    }
}