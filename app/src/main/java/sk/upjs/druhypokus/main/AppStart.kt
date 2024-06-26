package sk.upjs.druhypokus.main

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import sk.upjs.druhypokus.MainActivity
import sk.upjs.druhypokus.intro.IntroSliderActivity


class AppStart : AppCompatActivity(){

    override fun onCreate(savedInstanceState: Bundle?) {
        PrefSingleton.getInstance().Initialize(applicationContext);
        super.onCreate(savedInstanceState)

        var intent :Intent

        if(PrefSingleton.getInstance().getPreferenceBoolean("hudba")) {
            intent = Intent(this, BackgroundSoundService::class.java)
            startService(intent)
            PrefSingleton.getInstance().writePreference("hudba", true);
        }

        val jePrveZapnutie = PrefSingleton.getInstance().getPreferenceBoolean("jePrveZapnutie")

        if (jePrveZapnutie) {
            PrefSingleton.getInstance().writePreference("jePrveZapnutie", false);
            // Spustenie IntroSliderActivity
            intent = Intent(this, IntroSliderActivity::class.java)
            startActivity(intent)
        } else {
            // Spustenie MainActivity
            intent = Intent(this, MainActivity::class.java)
            startActivity(intent)
        }

        finish()
    }
}