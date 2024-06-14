package sk.upjs.druhypokus.intro

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.viewpager.widget.ViewPager
import sk.upjs.druhypokus.MainActivity
import sk.upjs.druhypokus.R


class IntroSliderActivity : AppCompatActivity() {

    // on below line we are creating a
    // variable for our view pager
    lateinit var viewPager: ViewPager

    // on below line we are creating a variable
    // for our slider adapter and slider list
    lateinit var sliderAdapter: SliderAdapter
    lateinit var sliderList: ArrayList<SliderData>

    // on below line we are creating a variable for our
    // skip button, slider indicator text view for 3 dots
    lateinit var skipBtn: Button
    lateinit var indicatorSlideOneTV: TextView
    lateinit var indicatorSlideTwoTV: TextView
    lateinit var indicatorSlideThreeTV: TextView

    override fun onCreate(savedInstanceState: Bundle?) {

        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_intro_slider)

        // on below line we are initializing all
        // our variables with their ids.
        viewPager = findViewById(R.id.idViewPager)
        skipBtn = findViewById(R.id.idBtnSkip)
        indicatorSlideOneTV = findViewById(R.id.idTVSlideOne)
        indicatorSlideTwoTV = findViewById(R.id.idTVSlideTwo)
        indicatorSlideThreeTV = findViewById(R.id.idTVSlideThree)

        // on below line we are adding click listener for our skip button
        skipBtn.setOnClickListener {
            // on below line we are opening a new activity
            val i = Intent(this@IntroSliderActivity, MainActivity::class.java)
            startActivity(i)
            finish()
        }

        // on below line we are initializing our slider list.
        sliderList = ArrayList()

        // on below line we are adding data to our list
        sliderList.add(
            SliderData(
                resources.getString(R.string.slide1_nadpis),
                resources.getString(R.string.slide1_caption),
                R.drawable.welcome
            )
        )

        sliderList.add(
            SliderData(
                resources.getString(R.string.slide2_nadpis),
                resources.getString(R.string.slide2_caption),
                R.drawable.name
            )
        )

        sliderList.add(
            SliderData(
                resources.getString(R.string.slide3_nadpis),
                resources.getString(R.string.slide3_caption),
                R.drawable.notify
            )
        )

        // on below line we are adding slider list
        // to our adapter class.
        sliderAdapter = SliderAdapter(this, sliderList)

        // on below line we are setting adapter
        // for our view pager on below line.
        viewPager.adapter = sliderAdapter

        // on below line we are adding page change
        // listener for our view pager.
        viewPager.addOnPageChangeListener(viewListener)
    }

    // creating a method for view pager for on page change listener.
    var viewListener: ViewPager.OnPageChangeListener = object : ViewPager.OnPageChangeListener {
        override fun onPageScrolled(
            position: Int,
            positionOffset: Float,
            positionOffsetPixels: Int
        ) {
        }

        override fun onPageSelected(position: Int) {

            // we are calling our dots method to
            // change the position of selected dots.

            // on below line we are checking position and updating text view text color.
            when (position){
                0 -> {
                    indicatorSlideTwoTV.setTextColor(resources.getColor(R.color.cervena_tmava_zosvetlena))
                    indicatorSlideThreeTV.setTextColor(resources.getColor(R.color.cervena_tmava_zosvetlena))
                    indicatorSlideOneTV.setTextColor(resources.getColor(R.color.cervena_tmava))
                }
                1 -> {
                    indicatorSlideTwoTV.setTextColor(resources.getColor(R.color.cervena_tmava))
                    indicatorSlideThreeTV.setTextColor(resources.getColor(R.color.cervena_tmava_zosvetlena))
                    indicatorSlideOneTV.setTextColor(resources.getColor(R.color.cervena_tmava_zosvetlena))
                }
                2 -> {
                    indicatorSlideTwoTV.setTextColor(resources.getColor(R.color.cervena_tmava_zosvetlena))
                    indicatorSlideThreeTV.setTextColor(resources.getColor(R.color.cervena_tmava))
                    indicatorSlideOneTV.setTextColor(resources.getColor(R.color.cervena_tmava_zosvetlena))
                }
            }
        }

        // below method is use to check scroll state.
        override fun onPageScrollStateChanged(state: Int) {
        }
    }
}
