package com.example.geophoto.presentation.adapters

import android.content.res.Resources
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.AdapterView
import androidx.paging.PagingDataAdapter
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.example.geophoto.databinding.FragmentPhotoGalleryBinding
import com.example.geophoto.databinding.PhotoItemBinding
import com.example.geophoto.models.PhotoLocation


class PhotoAdapter(
	private var listener: OnItemClick,
	private var longClick: OnItemLongClick
) : ListAdapter<PhotoLocation, PhotoAdapter.MyViewHolder>(PHOTO_COMPARATOR) {
	
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
						longClick.onItemLongClick(item)
					}
				}
				true
			}
		}
		
		fun bind(photoLocation: PhotoLocation) {
			binding.apply {
				
				photoItemImg.setImageURI(photoLocation.photo)
			
			}
			
		}
		
	}
	
	override fun onCreateViewHolder(
		parent: ViewGroup,
		viewType: Int
	): PhotoAdapter.MyViewHolder {
		val binding =
			PhotoItemBinding.inflate(
				LayoutInflater.from(parent.context),
				parent,
				false
			)
		return MyViewHolder(binding)
	}
	
	override fun onBindViewHolder(
		holder: PhotoAdapter.MyViewHolder,
		position: Int
	) {
		val currentItem = getItem(position)
		
		if (currentItem != null) {
			holder.bind(currentItem)
		}
	}
	
	interface OnItemClick {
		fun onItemClick(photoLocation: PhotoLocation)
	}
	interface OnItemLongClick {
		fun onItemLongClick(photoLocation: PhotoLocation)
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