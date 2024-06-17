package sk.upjs.druhypokus.milniky

import android.annotation.SuppressLint
import android.content.Context
import android.graphics.drawable.Drawable
import android.net.Uri
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.core.content.ContentProviderCompat.requireContext
import androidx.core.content.ContextCompat
import androidx.viewpager.widget.PagerAdapter
import sk.upjs.druhypokus.R
import java.io.FileNotFoundException
import java.io.InputStream
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
        val milestoneData: Milestone = milestoneList[position]
        ktoText.text = milestoneData.zucastneni
        coAkedyText.text = milestoneData.typ + " | " + milestoneData.datum
        kolkoUbehlo.text = ChronoUnit.DAYS.between(LocalDate.parse(milestoneData.datum, DateTimeFormatter.ofPattern("yyyy-MM-dd")), LocalDate.now()).toString() + context.getString(R.string.days)

        //obrazok
        /*
        val milestoneUri = Uri.parse(milestoneData.fotka)
        val yourDrawable : Drawable? = try {
            val inputStream = context.contentResolver.openInputStream(milestoneUri)
            Drawable.createFromStream(inputStream, milestoneUri.toString())
            // Tu by ste mali mať načítaný obrázok z URI
        } catch (e: FileNotFoundException) {
            // V prípade, že súbor na zadanom URI neexistuje, použite fallback na predvolený obrázok
            ContextCompat.getDrawable(context, R.drawable.poadie_abstract)
        }

        milestoneParent.setBackgroundDrawable(yourDrawable)
*/

        container.addView(view)
        return view
    }

    override fun destroyItem(container: ViewGroup, position: Int, `object`: Any) {
        container.removeView(`object` as ConstraintLayout)
    }
}