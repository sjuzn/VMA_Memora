package sk.upjs.druhypokus.milniky

import android.app.AlertDialog.Builder
import android.app.AlertDialog
import android.content.Context
import android.content.DialogInterface
import android.graphics.Bitmap.CompressFormat
import android.graphics.drawable.BitmapDrawable
import android.util.Base64
import android.view.View
import android.widget.ImageButton
import android.widget.TextView
import android.widget.Toast
import androidx.activity.viewModels
import androidx.appcompat.widget.PopupMenu
import androidx.fragment.app.FragmentActivity
import androidx.viewpager.widget.ViewPager
import sk.upjs.druhypokus.MemoraApplication
import sk.upjs.druhypokus.R
import java.io.ByteArrayOutputStream


class MilestonesObsluha(
    val currView : View,
    //val viewPager: ViewPager,
    val cntx : Context,
    val milestoneList: List<Milestone>
) {
    var pozicia = 0
    val activity = cntx as FragmentActivity
    fun obsluhaMilestone(){

        var  viewPager: ViewPager = currView.findViewById(R.id.container_pages)
        var milestonesAdapter = MilestonesSwipeAdapter(cntx,milestoneList)
        viewPager.adapter = milestonesAdapter
        viewPager.addOnPageChangeListener(viewListener)

        var btEdit = currView.findViewById<ImageButton>(R.id.imageButtonEdit)
        var btShare = currView.findViewById<ImageButton>(R.id.imageButtonShare)
        currView.findViewById<TextView>(R.id.poziciaText).text = ("1").plus("/").plus(milestoneList.size)

        //https://www.geeksforgeeks.org/popup-menu-in-android-with-example/
        btEdit.setOnClickListener{

            // Initializing the popup menu and giving the reference as current context
            val popupMenu = PopupMenu(cntx, btEdit)

            // Inflating popup menu from popup_menu.xml file
            popupMenu.menuInflater.inflate(R.menu.popup_milestones, popupMenu.menu)
            popupMenu.setOnMenuItemClickListener { menuItem ->
                when(menuItem.itemId){
                    R.id.add -> {
                        Toast.makeText(cntx, "You Clicked " + menuItem.title, Toast.LENGTH_SHORT).show()
                        val milestonesViewModel : MilestonesViewModel by activity.viewModels {
                            MilestonesViewModel.MilestoneViewModelFactory((activity.application as MemoraApplication).milestonesRepository)
                        }

                        val stream = ByteArrayOutputStream()
                        val drawable = cntx.resources.getDrawable(R.drawable.test)
                        (drawable as BitmapDrawable).bitmap.compress(CompressFormat.JPEG, 70, stream)
                        val byteFormat = stream.toByteArray()
                        val imgString: String = Base64.encodeToString(byteFormat, Base64.NO_WRAP)


                        milestonesViewModel.insert(Milestone("Narodenie", "2024-06-01", imgString, "Ja a Sophia"))
                        true
                    }

                    R.id.edit -> {
                        if(milestoneList.isEmpty()){
                            Toast.makeText(cntx, R.string.edit_error, Toast.LENGTH_LONG).show()
                        }
                        true
                    }

                    R.id.delete -> {
                        if(milestoneList.isEmpty()){
                            Toast.makeText(cntx, R.string.delete_error, Toast.LENGTH_LONG).show()
                        }else{
                            opytajSA()
                        }
                        true
                    }

                    R.id.order -> {
                        Toast.makeText(cntx, "You Clicked " + menuItem.title, Toast.LENGTH_SHORT).show()
                        true
                    }
                else -> throw IllegalArgumentException("menu option not implemented!!")
                }

            }
            // Showing the popup menu
            popupMenu.show()
        }

        btShare.setOnClickListener{
            Toast.makeText(cntx, "Share klik", Toast.LENGTH_SHORT).show()
        }
    }

    // creating a method for view pager for on page change listener.
    var viewListener: ViewPager.OnPageChangeListener = object : ViewPager.OnPageChangeListener {
        override fun onPageScrolled(
            position: Int,
            positionOffset: Float,
            positionOffsetPixels: Int
        ) {
            pozicia = position
        }

        override fun onPageSelected(position: Int) {
            pozicia = position
            val p : Int = position + 1
            currView.findViewById<TextView>(R.id.poziciaText).text = (p.toString()).plus("/").plus(milestoneList.size)
        }

        // below method is use to check scroll state.
        override fun onPageScrollStateChanged(state: Int) {
        }
    }

    fun opytajSA(){
        val builder: AlertDialog.Builder = Builder(cntx)

        builder.setTitle(R.string.confirm)
        builder.setMessage(R.string.sure)

        builder.setPositiveButton(
            R.string.yes,
            DialogInterface.OnClickListener { dialog, which -> // Do nothing, but close the dialog
                val milestonesViewModel : MilestonesViewModel by activity.viewModels {
                    MilestonesViewModel.MilestoneViewModelFactory((activity.application as MemoraApplication).milestonesRepository)
                }
                milestonesViewModel.delete(milestoneList.get(pozicia))
            })

        builder.setNegativeButton(
            R.string.no,
            DialogInterface.OnClickListener { dialog, which -> // Do nothing
                dialog.dismiss()
            })

        val alert: AlertDialog = builder.create()
        alert.show()
    }

}