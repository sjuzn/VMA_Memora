package sk.upjs.druhypokus.intro

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.text.Editable
import android.text.TextWatcher
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.EditText
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.TextView
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.core.app.ActivityCompat
import androidx.viewpager.widget.PagerAdapter
import sk.upjs.druhypokus.R
import sk.upjs.druhypokus.main.PrefSingleton
import java.security.AccessController.getContext


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
        if((position != 2) and (position != 3)){
            notifyOptions.visibility = View.GONE
        }

        if(position == 2) {
            //var allowNotifyBtn: Button = view.findViewById(R.id.idBtnTurnOn)
            (view.findViewById<Button>(R.id.idBtnTurnOn)!!).setOnClickListener{
                ActivityCompat.requestPermissions(context as Activity, arrayOf(android.Manifest.permission.POST_NOTIFICATIONS), 1)
            }
        }

        if(position == 3) {
            //var allowNotifyBtn: Button = view.findViewById(R.id.idBtnTurnOn)
            (view.findViewById<Button>(R.id.idBtnTurnOn)!!).setOnClickListener{
                ActivityCompat.requestPermissions(context as Activity, arrayOf(android.Manifest.permission.READ_MEDIA_IMAGES), 1)
            }
        }

        if(position == 1){
            var menoInput : EditText = view.findViewById(R.id.input_meno)
            menoInput.addTextChangedListener(object : TextWatcher {
                override fun afterTextChanged(s: Editable?) {
                    PrefSingleton.getInstance().writePreference("meno", menoInput.text.toString());
                    Log.d("SHARED PREF FUNGUJE", PrefSingleton.getInstance().getPreferenceString("meno"))
                }

                override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}
                override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {}
            })
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