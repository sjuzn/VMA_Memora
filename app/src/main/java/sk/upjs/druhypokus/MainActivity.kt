package sk.upjs.druhypokus

import android.os.Bundle
import android.preference.PreferenceManager
import android.util.Log
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity


class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_main2)


        val settings = PreferenceManager.getDefaultSharedPreferences(this)
        val language = settings.getString(resources.getString(R.string.name), "")

        if (language != null) {
            Log.d("LOOOOOOG", language)
        }else{
            Log.d("LOOOOOOG", "zle je")
        }

    }
}