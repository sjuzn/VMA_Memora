package sk.upjs.druhypokus.milniky

import android.content.Context
import android.content.Intent
import android.graphics.BitmapFactory
import android.net.Uri
import android.provider.MediaStore
import android.view.View
import android.widget.ImageButton
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.widget.PopupMenu
import androidx.fragment.app.FragmentActivity
import androidx.viewpager.widget.ViewPager
import sk.upjs.druhypokus.R
import sk.upjs.druhypokus.milniky.crud.MilestonesAkcieActivity
import java.io.Serializable


class MilestonesObsluha(
    val currView : View,
    //val viewPager: ViewPager,
    val cntx : Context,
    val milestoneList: List<Milestone>
) {
    var pozicia = 0
    val activity = cntx as FragmentActivity
    fun obsluhaMilestone(){

        val viewPager: ViewPager = currView.findViewById(R.id.container_pages)
        val milestonesAdapter = MilestonesSwipeAdapter(cntx,milestoneList)
        viewPager.adapter = milestonesAdapter
        viewPager.addOnPageChangeListener(viewListener)

        val btEdit = currView.findViewById<ImageButton>(R.id.imageButtonEdit)
        val btShare = currView.findViewById<ImageButton>(R.id.imageButtonShare)
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
                        val intent = Intent(cntx, MilestonesAkcieActivity::class.java)
                        intent.putExtra("TLACIDLO", "add")
                        intent.putExtra("MILESTONE", Milestone("","","",""))
                        cntx.startActivity(intent)
                        true
                    }

                    R.id.edit -> {
                        if(milestoneList.isEmpty()){
                            Toast.makeText(cntx, R.string.edit_error, Toast.LENGTH_LONG).show()
                        }else{
                            val intent = Intent(cntx, MilestonesAkcieActivity::class.java)
                            intent.putExtra("TLACIDLO", "edit")
                            intent.putExtra("MILESTONE", (milestoneList[pozicia] as Serializable?))
                            cntx.startActivity(intent)
                        }
                        true
                    }

                    R.id.order -> {
                        if(milestoneList.isEmpty()){
                            Toast.makeText(cntx, R.string.reorder_error, Toast.LENGTH_LONG).show()
                        }else{
                            val intent = Intent(cntx, MilestonesAkcieActivity::class.java)
                            intent.putExtra("TLACIDLO", "order")
                            intent.putExtra("MILESTONE", Milestone("","","",""))
                            cntx.startActivity(intent)
                        }
                        true
                    }
                else -> throw IllegalArgumentException("menu option not implemented!!!")
                }

            }
            // Showing the popup menu
            popupMenu.show()
        }

        btShare.setOnClickListener{
            if(milestoneList.isEmpty())
                Toast.makeText(cntx, R.string.share_error, Toast.LENGTH_LONG).show()
            else shareStory()
        }
    }

    //https://stackoverflow.com/questions/62715886/how-to-add-instagram-share-to-story-button-in-my-android-app
    private fun shareStory(){
        val sticker = BitmapFactory.decodeResource(cntx.resources,
            R.drawable.test)

        val savedImageURL: String = MediaStore.Images.Media.insertImage(
            cntx.contentResolver,
            sticker,
            "test_image",
            "image_description"
        )


        val savedImageURI = Uri.parse(savedImageURL)

        val storiesIntent = Intent("com.instagram.share.ADD_TO_STORY").apply {
            type = "image/jpeg"
            addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION)
            setPackage("com.instagram.android")

            putExtra("interactive_asset_uri", savedImageURI)
            putExtra("content_url", "something");
            putExtra("top_background_color", "#33FF33");
            putExtra("bottom_background_color", "#FF00FF")
        }

        cntx.grantUriPermission("com.instagram.android", savedImageURI, Intent.FLAG_GRANT_READ_URI_PERMISSION)
        cntx.startActivity(storiesIntent)
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

}