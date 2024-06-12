package sk.upjs.druhypokus

import android.os.Bundle
import android.preference.PreferenceManager
import android.util.Log
import android.view.MenuItem
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.ActionBarDrawerToggle
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.GravityCompat
import androidx.drawerlayout.widget.DrawerLayout
import androidx.fragment.app.Fragment
import androidx.navigation.ui.AppBarConfiguration
import com.google.android.material.navigation.NavigationView
import sk.upjs.druhypokus.milniky.MilestonesSwipeFragment


class MainActivity : AppCompatActivity(),  NavigationView.OnNavigationItemSelectedListener  {

    private lateinit var drawerLayout: DrawerLayout
    private lateinit var navigationView: NavigationView
    private lateinit var appBarConfiguration: AppBarConfiguration

    override fun onCreate(savedInstanceState: Bundle?) {
        enableEdgeToEdge()
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_home)

        chcemToggle()
        inicializujMenu()

    }

    private fun chcemToggle(){
        drawerLayout = findViewById(R.id.drawer_layout)
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
        val fragment = vyberFragment(menuItem)
        ukazFragment(fragment)
        drawerLayout.closeDrawer(GravityCompat.START)
        return true
    }

    private fun vyberFragment(menuItem: MenuItem): Fragment {
        return when (menuItem.itemId) {
            R.id.nav_milestones -> MilestonesSwipeFragment()
            R.id.nav_moments -> MilestonesSwipeFragment()
            R.id.nav_capsules -> MilestonesSwipeFragment()
            R.id.nav_list -> MilestonesSwipeFragment()
            R.id.nav_memo -> MilestonesSwipeFragment()
            R.id.nav_calendar -> MilestonesSwipeFragment()
            R.id.nav_invite -> MilestonesSwipeFragment()
            R.id.nav_settings -> MilestonesSwipeFragment()

            else -> throw IllegalArgumentException("menu option not implemented!!")
        }
    }

    private fun ukazFragment(fragment : Fragment) {
        supportFragmentManager
            .beginTransaction()
            .setCustomAnimations(R.anim.nav_enter, R.anim.nav_exit)
            .replace(R.id.container_fragment, fragment)
            .commit()
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