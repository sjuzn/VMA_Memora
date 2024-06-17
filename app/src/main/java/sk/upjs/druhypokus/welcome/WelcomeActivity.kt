package sk.upjs.druhypokus.welcome

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.view.MenuItem
import android.view.View
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.activity.viewModels
import androidx.appcompat.app.ActionBarDrawerToggle
import androidx.appcompat.app.AppCompatActivity
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.core.view.GravityCompat
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.drawerlayout.widget.DrawerLayout
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.navigation.NavigationView
import sk.upjs.druhypokus.MainActivity
import sk.upjs.druhypokus.MemoraApplication
import sk.upjs.druhypokus.PrefSingleton
import sk.upjs.druhypokus.R
import sk.upjs.druhypokus.milniky.Milestone
import sk.upjs.druhypokus.milniky.MilestonesObsluha
import sk.upjs.druhypokus.milniky.MilestonesViewModel
import sk.upjs.druhypokus.milniky.crud.MilestonesAkcieActivity
import sk.upjs.druhypokus.milniky.crud.ReorderMRecyclerAdapter
import java.time.LocalDate
import java.time.format.DateTimeFormatter
import java.time.temporal.ChronoUnit

class WelcomeActivity : AppCompatActivity(),  NavigationView.OnNavigationItemSelectedListener {

    private lateinit var drawerLayout: DrawerLayout
    private var milestoneList: List<Milestone> = emptyList()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val milestonesViewModel : MilestonesViewModel by viewModels {
            MilestonesViewModel.MilestoneViewModelFactory((application as MemoraApplication).milestonesRepository)
        }

        enableEdgeToEdge()
        setContentView(R.layout.activity_welcome)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        val meno = PrefSingleton.getInstance().getPreferenceString("meno")
        if (meno != "") findViewById<TextView>(R.id.helloTextView).setText(getString(R.string.hi) + ", " + meno)

        milestonesViewModel.milestones.observe(this){
            milestoneList = it
        }

        nastavNadchadzajuce(najdiNajblizsiDatum(milestoneList))

        val recyclerView = findViewById<RecyclerView>(R.id.todayRecyclerView)
        val welcomeRecyclerAdapter = WelcomeRecyclerAdapter(milestoneList)
        recyclerView.adapter = welcomeRecyclerAdapter

        chcemToggle()
        inicializujMenu()
        window.decorView.apply {
            systemUiVisibility = View.SYSTEM_UI_FLAG_HIDE_NAVIGATION or View.SYSTEM_UI_FLAG_FULLSCREEN
        }
    }

    private fun chcemToggle(){
        drawerLayout = findViewById(R.id.main)
        val toggle = ActionBarDrawerToggle(
            this, drawerLayout, R.string.navigation_drawer_open, R.string.navigation_drawer_close
        )
        drawerLayout.addDrawerListener(toggle)

        toggle.syncState()
    }

    private fun inicializujMenu(){
        val navigationView: NavigationView = findViewById(R.id.navigation_view)
        navigationView.setNavigationItemSelectedListener(this)
    }

    override fun onNavigationItemSelected(menuItem: MenuItem): Boolean {
        vyberUdalost(menuItem)
        drawerLayout.closeDrawer(GravityCompat.START)
        return true
    }

    private fun vyberUdalost(menuItem: MenuItem) {

        when (menuItem.itemId) {
            R.id.nav_milestones -> {
                val intent = Intent(this, MainActivity::class.java)
                startActivity(intent)
                finish()
            }
            /*R.id.nav_moments -> MilestonesSwipeFragment(this, milestoneList)
            R.id.nav_capsules -> MilestonesSwipeFragment(this, milestoneList)
            R.id.nav_list -> MilestonesSwipeFragment(this, milestoneList)
            R.id.nav_memo -> MilestonesSwipeFragment(this, milestoneList)
            R.id.nav_calendar -> MilestonesSwipeFragment(this, milestoneList)
            R.id.nav_invite -> MilestonesSwipeFragment(this, milestoneList)
            R.id.nav_settings -> MilestonesSwipeFragment(this, milestoneList)
*/
            else -> throw IllegalArgumentException("menu option not implemented!!")
        }
    }

    @Deprecated("Deprecated in Java")
    override fun onBackPressed() {
        if (drawerLayout.isDrawerOpen(GravityCompat.START)) {
            drawerLayout.closeDrawer(GravityCompat.START)
        } else {
            super.onBackPressed()
        }
    }

    private fun najdiNajblizsiDatum(milestoneList : List<Milestone>?): Milestone? {
        val formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd")
        val currentDate = LocalDate.now()

        var minDays : Int = Int.MAX_VALUE
        var savedMilestone : Milestone? = null
        if (milestoneList != null) {
            for(m in milestoneList){
                Toast.makeText(this, m.typ, Toast.LENGTH_SHORT).show()
                val objDate = LocalDate.parse(m.datum, formatter)
                if(ChronoUnit.DAYS.between(currentDate, objDate).toInt()<minDays){
                    minDays = ChronoUnit.DAYS.between(currentDate, objDate).toInt()
                    savedMilestone = m
                }
            }
        }

        return savedMilestone
    }

    private fun nastavNadchadzajuce(milestone: Milestone?){
        if(milestone != null){
            findViewById<ImageView>(R.id.imageView).setImageURI(Uri.parse(milestone.fotka))
            findViewById<TextView>(R.id.textView).text = milestone.typ
            findViewById<TextView>(R.id.zaKolkoDni).text = milestone.datum
        }
    }
}