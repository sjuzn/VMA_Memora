package sk.upjs.druhypokus.milniky

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import sk.upjs.druhypokus.R
import sk.upjs.druhypokus.entity.Milestone

class MilestonesSwipeAdapter :
    ListAdapter<Milestone, MilestonesSwipeAdapter.PhotoViewHolder>(PhotoDiffCallback()) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): PhotoViewHolder {
        val view =
            LayoutInflater.from(parent.context).inflate(R.layout.item_photo_swipe, parent, false)
        return PhotoViewHolder(view)
    }

    override fun onBindViewHolder(holder: PhotoViewHolder, position: Int) {
        val photo = getItem(position)
        holder.bind(photo)
    }

    inner class PhotoViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        private val imageView: ImageView = itemView.findViewById(R.id.imageView)

        fun bind(milestone: Milestone) {
            // Načítanie a zobrazenie fotky pomocou knižnice Glide alebo iného nástroja
        }
    }

    class PhotoDiffCallback : DiffUtil.ItemCallback<Milestone>() {
        override fun areItemsTheSame(oldItem: Milestone, newItem: Milestone): Boolean {
            return oldItem.uuid == newItem.uuid
        }

        @SuppressLint("DiffUtilEquals")
        override fun areContentsTheSame(oldItem: Milestone, newItem: Milestone): Boolean {
            return oldItem == newItem
        }
    }
}