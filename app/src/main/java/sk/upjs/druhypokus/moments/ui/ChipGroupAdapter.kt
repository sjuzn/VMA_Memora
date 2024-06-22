package sk.upjs.druhypokus.moments.ui

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.chip.Chip
import sk.upjs.druhypokus.R
import sk.upjs.druhypokus.moments.Tag

class ChipGroupAdapter (private var chips: List<Tag>) : RecyclerView.Adapter<ChipGroupAdapter.ChipViewHolder>() {

    private val selectedTags = mutableSetOf<Tag>()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ChipViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.chips, parent, false)
        return ChipViewHolder(view)
    }

    override fun onBindViewHolder(holder: ChipViewHolder, position: Int) {
        val tag = chips[position]
        holder.bind(tag)
    }

    override fun getItemCount(): Int {
        return chips.size
    }

    fun setTags(newChips: List<Tag>) {
        chips = newChips
        notifyDataSetChanged()
    }


    fun getSelectedTags(): Set<Tag> {
        return selectedTags
    }

    inner class ChipViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        private val chip: Chip = itemView.findViewById(R.id.chip)

        fun bind(tag: Tag) {
            chip.text = tag.nazovTag
            chip.setChipBackgroundColorResource(R.color.cervena_tmava_zosvetlena)
            chip.setTextColor(itemView.context.resources.getColor(R.color.white))
            chip.isCheckable = true

            chip.isChecked = selectedTags.contains(tag)

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