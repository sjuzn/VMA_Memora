package sk.upjs.druhypokus.milniky

import android.content.Intent
import android.graphics.BitmapFactory
import android.net.Uri
import android.os.Bundle
import android.provider.MediaStore
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.View.OnClickListener
import android.view.ViewGroup
import android.widget.ImageButton
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.widget.PopupMenu
import androidx.viewpager.widget.ViewPager
import sk.upjs.druhypokus.R
import sk.upjs.druhypokus.milniky.crud.MilestonesAkcieActivity
import java.io.Serializable


private const val ARG_PARAM1 = "param1"
class MilestonesFragment : Fragment() {

    var pozicia = 0
    lateinit var currView : View
    private lateinit var milestoneList: java.util.ArrayList<Milestone>

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            milestoneList = it.getParcelableArrayList(ARG_PARAM1)!!
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {

        currView = inflater.inflate(R.layout.fragment_milestones, container, false)


        Toast.makeText(requireContext(), milestoneList.size.toString(), Toast.LENGTH_LONG).show()

        val viewPager: ViewPager = currView.findViewById(R.id.container_pages)
        val milestonesAdapter = MilestonesSwipeAdapter(requireContext(),milestoneList)
        viewPager.adapter = milestonesAdapter
        viewPager.addOnPageChangeListener(viewListener)

        btEdit = currView.findViewById(R.id.imageButtonEdit)
        val btShare = currView.findViewById<ImageButton>(R.id.imageButtonShare)
        if(milestoneList.isEmpty()){
            currView.findViewById<TextView>(R.id.poziciaText).text = ("0").plus("/").plus(milestoneList.size)
        }else{
            currView.findViewById<TextView>(R.id.poziciaText).text = ("1").plus("/").plus(milestoneList.size)
        }

        btEdit.setOnClickListener(btEditListener)

        btShare.setOnClickListener{
            if(milestoneList.isEmpty())
                Toast.makeText(requireContext(), R.string.share_error, Toast.LENGTH_LONG).show()
            else shareStory()
        }

        return currView
    }

    lateinit var btEdit : ImageButton
    val btEditListener = OnClickListener {
        // Initializing the popup menu and giving the reference as current context
        val popupMenu = PopupMenu(requireContext(), btEdit)

        // Inflating popup menu from popup_menu.xml file
        popupMenu.menuInflater.inflate(R.menu.popup_milestones, popupMenu.menu)
        popupMenu.setOnMenuItemClickListener { menuItem ->
            when(menuItem.itemId){
                R.id.add -> {
                    val intent = Intent(requireContext(), MilestonesAkcieActivity::class.java)
                    intent.putExtra("TLACIDLO", "add")
                    intent.putExtra("MILESTONE", Milestone("","","","")as Serializable?)
                    requireContext().startActivity(intent)
                    true
                }

                R.id.edit -> {
                    if(milestoneList.isEmpty()){
                        Toast.makeText(requireContext(), R.string.edit_error, Toast.LENGTH_LONG).show()
                    }else{
                        val intent = Intent(requireContext(), MilestonesAkcieActivity::class.java)
                        intent.putExtra("TLACIDLO", "edit")
                        intent.putExtra("MILESTONE", (milestoneList[pozicia] as Serializable?))
                        requireContext().startActivity(intent)
                    }
                    true
                }

                R.id.order -> {
                    if(milestoneList.isEmpty()){
                        Toast.makeText(requireContext(), R.string.reorder_error, Toast.LENGTH_LONG).show()
                    }else{
                        val intent = Intent(requireContext(), MilestonesAkcieActivity::class.java)
                        intent.putExtra("TLACIDLO", "order")
                        intent.putExtra("MILESTONE", Milestone("","","","")as Serializable?)
                        requireContext().startActivity(intent)
                    }
                    true
                }

                else -> throw IllegalArgumentException("menu option not implemented!!!")
            }

        }
        // Showing the popup menu
        popupMenu.show()
    }

    //https://stackoverflow.com/questions/62715886/how-to-add-instagram-share-to-story-button-in-my-android-app
    private fun shareStory(){
        val sticker = BitmapFactory.decodeResource(requireContext().resources, R.drawable.test)

        val savedImageURL: String = MediaStore.Images.Media.insertImage(
            requireContext().contentResolver,
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

        requireContext().grantUriPermission("com.instagram.android", savedImageURI, Intent.FLAG_GRANT_READ_URI_PERMISSION)
        requireContext().startActivity(storiesIntent)
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

    companion object {
        @JvmStatic
        fun newInstance(milestone: java.util.ArrayList<Milestone>) =
            MilestonesFragment().apply {
                arguments = Bundle().apply {
                    putParcelableArrayList(ARG_PARAM1, milestone)
                }
            }
    }
}