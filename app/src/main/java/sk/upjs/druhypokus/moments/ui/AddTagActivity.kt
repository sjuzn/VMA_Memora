package sk.upjs.druhypokus.moments.ui

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.text.Editable
import android.util.Log
import android.view.GestureDetector
import android.view.MotionEvent
import android.view.inputmethod.EditorInfo
import android.widget.ImageButton
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.chip.Chip
import com.google.android.material.textfield.TextInputEditText
import sk.upjs.druhypokus.R
import sk.upjs.druhypokus.main.MemoraApplication
import sk.upjs.druhypokus.moments.MomentTagViewModel
import sk.upjs.druhypokus.moments.Tag
import java.io.Serializable


class AddTagActivity : AppCompatActivity() {

    private lateinit var inputTag: TextInputEditText
    private lateinit var chipGroup: RecyclerView

    private val momentTagViewModel: MomentTagViewModel by viewModels {
        MomentTagViewModel.MomentTagViewModelFactory((application as MemoraApplication).momentTagRepository)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_add_tag)

        chipGroup = findViewById(R.id.chipGroup)
        val chipGroupAdapter = ChipGroupAdapter(emptyList(), this, momentTagViewModel)
        chipGroup.layoutManager = GridLayoutManager(this, 3)
        chipGroup.adapter = chipGroupAdapter

        momentTagViewModel.allTags.observe(this) { list ->
            chipGroupAdapter.setTags(list)
        }

        findViewById<ImageButton>(R.id.buttonBack).setOnClickListener {
            val previousScreen = Intent(
                applicationContext,
                AddMomentActivity::class.java
            )
            previousScreen.putExtra("selectedTags", ArrayList(chipGroupAdapter.getSelectedTags()))
            setResult(1, previousScreen)
            finish()
        }

        inputTag = findViewById(R.id.inputTag)
        inputTag.setOnEditorActionListener { _, actionId, _ ->
            if (actionId == EditorInfo.IME_ACTION_GO) {

                val userInput = inputTag.text.toString()
                inputTag.text = Editable.Factory.getInstance().newEditable("")
                momentTagViewModel.insertTag(Tag(userInput))
                true
            } else {
                false
            }
        }
    }

    companion object {
        @JvmStatic
        fun newInstance() =
            MomentFragment().apply {
                arguments = Bundle().apply {
                }
            }
    }
}