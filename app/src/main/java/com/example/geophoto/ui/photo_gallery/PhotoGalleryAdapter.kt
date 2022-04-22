package com.example.geophoto.ui.photo_gallery

import android.content.res.Resources
import android.net.Uri
import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.ImageView
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.example.domain.model.PhotoLocation
import com.example.geophoto.databinding.PhotoItemBinding

class PhotoGalleryAdapter(
    private val listener: OnClick,
) : ListAdapter<PhotoLocation, PhotoGalleryAdapter.MyViewHolder>(PHOTO_COMPARATOR) {

    lateinit var res: Resources

    inner class MyViewHolder(private var binding: PhotoItemBinding) :
        RecyclerView.ViewHolder(binding.root) {

        init {
            res = binding.root.resources

            binding.root.setOnClickListener {
                val position = bindingAdapterPosition
                if (position != RecyclerView.NO_POSITION) {
                    val item = getItem(position)
                    if (item != null) {
                        listener.onItemClick(item)
                    }
                }
            }

            binding.root.setOnLongClickListener {
                val position = bindingAdapterPosition
                if (position != RecyclerView.NO_POSITION) {
                    val item = getItem(position)
                    if (item != null) {
                        listener.onItemLongClick(item)
                    }
                }
                true
            }
        }

        fun bind(photoLocation: PhotoLocation) {
            binding.apply {
                listener.setPhoto(photoLocation.photo, photoItemImg)
            }

        }

    }

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): MyViewHolder {
        val binding =
            PhotoItemBinding.inflate(
                LayoutInflater.from(parent.context),
                parent,
                false
            )
        return MyViewHolder(binding)
    }

    override fun onBindViewHolder(
        holder: MyViewHolder,
        position: Int
    ) {
        val currentItem = getItem(position)

        if (currentItem != null) {
            holder.bind(currentItem)
        }
    }

    interface OnClick {
        fun onItemClick(photoLocation: PhotoLocation)
        fun onItemLongClick(photoLocation: PhotoLocation)
        fun setPhoto(photo: Uri, view: ImageView)
    }

    companion object {
        private val PHOTO_COMPARATOR = object : DiffUtil.ItemCallback<PhotoLocation>() {
            override fun areItemsTheSame(
                oldItem: PhotoLocation,
                newItem: PhotoLocation
            ) = oldItem.photo == newItem.photo

            override fun areContentsTheSame(
                oldItem: PhotoLocation,
                newItem: PhotoLocation
            ) = oldItem == newItem
        }
    }

}