package sk.upjs.druhypokus

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import sk.upjs.druhypokus.intro.IntroSliderActivity
import sk.upjs.druhypokus.welcome.WelcomeActivity

class AppStart : AppCompatActivity(){

    override fun onCreate(savedInstanceState: Bundle?) {
        PrefSingleton.getInstance().Initialize(getApplicationContext());
        super.onCreate(savedInstanceState)

        val intent = Intent(this, BackgroundSoundService::class.java)
        startService(intent)

        val jePrveZapnutie = PrefSingleton.getInstance().getPreferenceBoolean("jePrveZapnutie")

        if (jePrveZapnutie) {
            PrefSingleton.getInstance().writePreference("jePrveZapnutie", false);

            // Spustenie IntroSliderActivity
            val intent = Intent(this, IntroSliderActivity::class.java)
            startActivity(intent)
        } else {
            // Spustenie MainActivity
            val intent = Intent(this, WelcomeActivity::class.java)
            startActivity(intent)
        }

        finish()
    }
}