package sk.upjs.druhypokus.milniky

import android.annotation.SuppressLint
import android.content.Context
import android.graphics.BitmapFactory
import android.graphics.drawable.BitmapDrawable
import android.util.Base64
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.viewpager.widget.PagerAdapter
import sk.upjs.druhypokus.R
import java.time.LocalDate
import java.time.format.DateTimeFormatter
import java.time.temporal.ChronoUnit


class MilestonesSwipeAdapter (
    val context: Context,
    val milestoneList: List<Milestone>
) : PagerAdapter() {

    override fun getCount(): Int {
        return milestoneList.size
    }

    override fun isViewFromObject(view: View, `object`: Any): Boolean {
        return view === `object` as ConstraintLayout
    }

    @SuppressLint("SetTextI18n")
    override fun instantiateItem(container: ViewGroup, position: Int): Any {

        val layoutInflater: LayoutInflater =
            context.getSystemService(Context.LAYOUT_INFLATER_SERVICE) as LayoutInflater

        val view: View = layoutInflater.inflate(R.layout.milestone_item, container, false)

        //treba mi potom premenne na tie elementy co tam budu
        val milestoneParent: ConstraintLayout = view.findViewById(R.id.milestoneParent)
        val ktoText: TextView = view.findViewById(R.id.ktoText)
        val coAkedyText: TextView = view.findViewById(R.id.coAkedyText)
        val kolkoUbehlo: TextView = view.findViewById(R.id.kolkoUbehlo)


        //a potom naplnim data
        val milestoneData: Milestone = milestoneList.get(position)

        //obrazok
        val imageBytes = Base64.decode(milestoneData.fotka, Base64.NO_WRAP)
        val decodedImage = BitmapFactory.decodeByteArray(imageBytes, 0, imageBytes.size)
        milestoneParent.setBackgroundDrawable(BitmapDrawable(decodedImage))

        ktoText.text = milestoneData.zucastneni
        coAkedyText.text = milestoneData.typ + " | " + milestoneData.datum

        val formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd")
        val eventDate = LocalDate.parse(milestoneData.datum, formatter)
        val currentDate = LocalDate.now()
        kolkoUbehlo.text = ChronoUnit.DAYS.between(eventDate, currentDate).toString()

        container.addView(view)
        return view
    }

    override fun destroyItem(container: ViewGroup, position: Int, `object`: Any) {
        container.removeView(`object` as ConstraintLayout)
    }
}