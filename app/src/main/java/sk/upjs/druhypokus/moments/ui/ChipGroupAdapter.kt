package sk.upjs.druhypokus.moments.ui

import android.annotation.SuppressLint
import android.content.Context
import android.content.Intent
import android.os.VibrationEffect
import android.os.Vibrator
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.activity.viewModels
import androidx.appcompat.app.AlertDialog
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.chip.Chip
import sk.upjs.druhypokus.MainActivity
import sk.upjs.druhypokus.R
import sk.upjs.druhypokus.main.MemoraApplication
import sk.upjs.druhypokus.moments.MomentTagViewModel
import sk.upjs.druhypokus.moments.Tag


class ChipGroupAdapter(
    private var chips: List<Tag>,
    private val context: Context,
    private val momentTagViewModel: MomentTagViewModel
) : RecyclerView.Adapter<ChipGroupAdapter.ChipViewHolder>() {

    private val selectedTags = mutableSetOf<Tag>()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ChipViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.chips, parent, false)
        return ChipViewHolder(view)
    }

    @SuppressLint("NotifyDataSetChanged")
    override fun onBindViewHolder(holder: ChipViewHolder, position: Int) {
        val tag = chips[position]
        holder.bind(tag)

        // Handle long press listener
        holder.chip.setOnLongClickListener {
            if (!selectedTags.contains(tag)) {
                val vibe: Vibrator = context.getSystemService(Context.VIBRATOR_SERVICE) as Vibrator
                val effect: VibrationEffect =
                    VibrationEffect.createOneShot(300, VibrationEffect.EFFECT_HEAVY_CLICK);
                vibe.vibrate(effect)

                val builder = AlertDialog.Builder(context)
                builder.setMessage(context.getString(R.string.sure))
                    .setCancelable(false)
                    .setPositiveButton("Yes") { _, _ ->
                        chips = chips.filterNot { it == tag }
                        momentTagViewModel.deleteTag(tag)
                        notifyDataSetChanged()
                    }
                    .setNegativeButton("No") { dialog, _ ->
                        dialog.dismiss()
                    }
                val alert = builder.create()
                alert.show()
            }
            true
        }
    }

    override fun getItemCount(): Int {
        return chips.size
    }

    fun setTags(newChips: List<Tag>) {
        chips = newChips
        notifyDataSetChanged()
    }


    fun getSelectedTags(): Set<String> {

        val s: MutableSet<String> = mutableSetOf()

        for (q in selectedTags) {
            s.add(q.nazovTag)
        }

        return s
    }

    inner class ChipViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val chip: Chip = itemView.findViewById(R.id.chip)

        fun bind(tag: Tag) {
            chip.text = tag.nazovTag
            chip.setChipBackgroundColorResource(R.color.cervena_tmava_zosvetlena)
            chip.setTextColor(itemView.context.resources.getColor(R.color.white))
            chip.isCheckable = true
            chip.isChecked = selectedTags.contains(tag)

            if(selectedTags.contains(tag)){
                chip.setChipBackgroundColorResource(R.color.cervena_tmava)
            }else{
                chip.setChipBackgroundColorResource(R.color.cervena_tmava_zosvetlena)
            }

            chip.setOnCheckedChangeListener { _, isChecked ->
                if (isChecked) {
                    selectedTags.add(tag)
                    chip.setChipBackgroundColorResource(R.color.cervena_tmava)
                } else {
                    selectedTags.remove(tag)
                    chip.setChipBackgroundColorResource(R.color.cervena_tmava_zosvetlena)
                }
            }
        }
    }
}