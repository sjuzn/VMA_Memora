package sk.upjs.druhypokus

import android.os.Bundle
import android.view.MenuItem
import android.view.View
import androidx.activity.enableEdgeToEdge
import androidx.activity.viewModels
import androidx.appcompat.app.ActionBarDrawerToggle
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.GravityCompat
import androidx.drawerlayout.widget.DrawerLayout
import com.google.android.material.navigation.NavigationView
import sk.upjs.druhypokus.main.MemoraApplication
import sk.upjs.druhypokus.milniky.Milestone
import sk.upjs.druhypokus.milniky.MilestonesFragment
import sk.upjs.druhypokus.milniky.MilestonesViewModel
import sk.upjs.druhypokus.settings.SettingsFragment
import sk.upjs.druhypokus.welcome.WelcomeFragment


class MainActivity : AppCompatActivity(),  NavigationView.OnNavigationItemSelectedListener  {

    private lateinit var drawerLayout: DrawerLayout
    private var milestoneList: List<Milestone> = emptyList()

    override fun onCreate(savedInstanceState: Bundle?) {

        val milestonesViewModel : MilestonesViewModel by viewModels {
            MilestonesViewModel.MilestoneViewModelFactory((application as MemoraApplication).milestonesRepository)
        }
        milestonesViewModel.milestones.observe(this){
            milestoneList = it
        }

        enableEdgeToEdge()
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_home)

        chcemToggle()
        inicializujMenu()

        //https://developer.android.com/training/system-ui/navigation#kotlin
        window.decorView.apply {
            // Hide both the navigation bar and the status bar.
            // SYSTEM_UI_FLAG_FULLSCREEN is only available on Android 4.1 and higher, but as
            // a general rule, you should design your app to hide the status bar whenever you
            // hide the navigation bar.
            systemUiVisibility = View.SYSTEM_UI_FLAG_HIDE_NAVIGATION or View.SYSTEM_UI_FLAG_FULLSCREEN
        }
    }

    private fun chcemToggle(){
        drawerLayout = findViewById(R.id.drawer_layout5)
        val toggle = ActionBarDrawerToggle(
            this, drawerLayout, R.string.navigation_drawer_open, R.string.navigation_drawer_close
        )
        drawerLayout.addDrawerListener(toggle)

        toggle.syncState()
    }

    private fun inicializujMenu(){
        val navigationView: NavigationView = findViewById(R.id.navigation_view)
        navigationView.setNavigationItemSelectedListener(this)

        val menuItem = navigationView.menu.getItem(0)
        onNavigationItemSelected(menuItem)
        menuItem.isChecked = true
    }

    override fun onNavigationItemSelected(menuItem: MenuItem): Boolean {
        vyberUdalost(menuItem)
        drawerLayout.closeDrawer(GravityCompat.START)
        return true
    }

    private fun vyberUdalost(menuItem: MenuItem) {

        when (menuItem.itemId) {
            R.id.nav_milestones ->{
                val fragment = MilestonesFragment.newInstance()
                supportFragmentManager.beginTransaction()
                    .replace(R.id.fragmentContainer, fragment)
                    .commit()
            }

            R.id.nav_home -> {
                val fragment : WelcomeFragment = WelcomeFragment.newInstance()
                supportFragmentManager.beginTransaction()
                    .replace(R.id.fragmentContainer, fragment)
                    .commit()
            }

            R.id.nav_settings -> {
                val fragment : SettingsFragment = SettingsFragment.newInstance()
                supportFragmentManager.beginTransaction()
                    .replace(R.id.fragmentContainer, fragment)
                    .commit()
            }
            /*R.id.nav_moments ->
            R.id.nav_capsules ->
            R.id.nav_list ->
            R.id.nav_memo ->
            R.id.nav_calendar ->
            R.id.nav_invite ->

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
}