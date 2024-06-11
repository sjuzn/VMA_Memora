package sk.upjs.druhypokus.intro

import android.app.Activity
import android.content.Context
import android.content.SharedPreferences
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.EditText
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.core.app.ActivityCompat
import androidx.viewpager.widget.PagerAdapter
import sk.upjs.druhypokus.R

class SliderAdapter(
    val context: Context,
    val sliderList: ArrayList<SliderData>
) : PagerAdapter() {

    override fun getCount(): Int {
        // on below line we are returning
        // the size of slider list
        return sliderList.size
    }

    override fun isViewFromObject(view: View, `object`: Any): Boolean {
        // inside isViewFromobject method we are
        // returning our Constraint layout object.
        return view === `object` as ConstraintLayout
    }

    override fun instantiateItem(container: ViewGroup, position: Int): Any {
        // in this method we will initialize all our layout
        // items and inflate our layout file as well.
        val layoutInflater: LayoutInflater =
            context.getSystemService(Context.LAYOUT_INFLATER_SERVICE) as LayoutInflater

        // below line is use to inflate the
        // layout file which we created.
        val view: View = layoutInflater.inflate(R.layout.slider_item, container, false)

        // on below line we are initializing our image view,
        // heading text view and description text view with their ids.
        val imageView: ImageView = view.findViewById(R.id.idIVSlider)
        val sliderHeadingTV: TextView = view.findViewById(R.id.idTVSliderTitle)
        val sliderDescTV: TextView = view.findViewById(R.id.idTVSliderDescription)
        val menoInput : EditText = view.findViewById(R.id.input_meno)
        val notifyOptions : LinearLayout = view.findViewById(R.id.notificationOptions)

        // on below line we are setting data to our text view
        // and image view on below line.
        val sliderData: SliderData = sliderList.get(position)
        sliderHeadingTV.text = sliderData.slideTitle
        sliderDescTV.text = sliderData.slideDescription
        imageView.setImageResource(sliderData.slideImage)

        if(position != 1){
            menoInput.visibility = View.GONE
        }
        if(position != 2){
            notifyOptions.visibility = View.GONE
        }
        if(position == 2) {
            var allowNotifyBtn: Button = view.findViewById(R.id.idBtnTurnOn)

            allowNotifyBtn.setOnClickListener{
                ActivityCompat.requestPermissions(context as Activity, arrayOf(android.Manifest.permission.POST_NOTIFICATIONS), 1)
            }

            val sharedPref: SharedPreferences = (context as Activity).getPreferences(AppCompatActivity.MODE_PRIVATE)
            val editor = sharedPref.edit()
            editor.putString(context.resources.getString(R.string.name), menoInput.text.toString())
            editor.apply()
        }

        // on below line we are adding our view to container.
        container.addView(view)

        // on below line we are returning our view.
        return view
    }

    override fun destroyItem(container: ViewGroup, position: Int, `object`: Any) {
        // this is a destroy view method
        // which is use to remove a view.
        container.removeView(`object` as ConstraintLayout)
    }

}
