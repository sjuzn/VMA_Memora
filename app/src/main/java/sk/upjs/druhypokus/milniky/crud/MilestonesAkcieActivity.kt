package sk.upjs.druhypokus.milniky.crud

import android.content.Intent
import android.os.Bundle
import android.widget.ImageButton
import android.widget.TextView
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.fragment.app.Fragment
import sk.upjs.druhypokus.MainActivity
import sk.upjs.druhypokus.R
import sk.upjs.druhypokus.milniky.Milestone

class MilestonesAkcieActivity : AppCompatActivity() {

    lateinit var milestone : Milestone
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_milestones_akcie)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        val fragmentType = intent.getStringExtra("TLACIDLO")
        milestone = intent.getSerializableExtra("MILESTONE") as Milestone

        val back : ImageButton = this.findViewById(R.id.buttonBack)
        back.setOnClickListener{

                val intent = Intent(
                    applicationContext,
                    MainActivity::class.java
                )
                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP)
                startActivity(intent)

        }

        loadFragment(fragmentType)
    }

    private fun loadFragment(fragmentType: String?) {
        val nadpis : TextView = this.findViewById(R.id.nazov)

        val fragment: Fragment = when (fragmentType) {
            "edit" -> {
                nadpis.text = getString(R.string.edit)
                MilestoneEditFragment.newInstance(milestone)
            }
            "add" -> {
                nadpis.text = getString(R.string.add)
                MilestoneAddFragment.newInstance()
            }
            "order" ->{
                nadpis.text = getString(R.string.order)
                MilestoneReorderFragment.newInstance()
            }
            else -> throw IllegalArgumentException("menu option not implemented!!") // Default fragment
        }

        supportFragmentManager.beginTransaction()
            .replace(R.id.fragmentContainer, fragment)
            .addToBackStack(null) // Pridajte tento riadok
            .commit()
    }
    @Deprecated("Deprecated in Java")
    override fun onBackPressed() {
        super.onBackPressed()
        val intent = Intent(
            this,
            MainActivity::class.java
        )
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP)
        startActivity(intent)
    }
}