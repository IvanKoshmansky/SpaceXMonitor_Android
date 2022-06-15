package com.example.android.spacexmonitor.main.viewpaging

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.paging.PagingDataAdapter
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.example.android.spacexmonitor.databinding.LaunchItemBinding
import com.example.android.spacexmonitor.models.OneLaunchModel

class LaunchesPagingDataAdapter (private val clickListener: LaunchesClickListener) :
    PagingDataAdapter<OneLaunchModel, LaunchesPagingDataAdapter.LaunchesViewHolder>(DiffCallback()) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): LaunchesViewHolder {
        return LaunchesViewHolder.from(parent)
    }

    override fun onBindViewHolder(holder: LaunchesViewHolder, position: Int) {
        val item = getItem(position)
        // Note that item may be null. ViewHolder must support binding a
        // null item as a placeholder.
        holder.bind(item, clickListener)
    }

    class LaunchesViewHolder(val binding: LaunchItemBinding): RecyclerView.ViewHolder(binding.root) {

        // TODO: по хорошему для поддержки ситуации когда вместо item приходит null надо иметь два типа ViewHolder
        fun bind(item: OneLaunchModel?, clickListener: LaunchesClickListener) {
            binding.launch = item ?: OneLaunchModel("", "", 0, false, "", "")
            binding.clickListener = if (item != null) clickListener else LaunchesClickListener {}
            binding.executePendingBindings()
        }

        companion object {
            fun from(parent: ViewGroup): LaunchesViewHolder {
                val layoutInflater = LayoutInflater.from(parent.context)
                val binding = LaunchItemBinding.inflate(layoutInflater, parent, false)
                return LaunchesViewHolder(binding)
            }
        }
    }

    class DiffCallback: DiffUtil.ItemCallback<OneLaunchModel>() {
        override fun areItemsTheSame(oldItem: OneLaunchModel, newItem: OneLaunchModel): Boolean {
            return oldItem.id == newItem.id
        }
        override fun areContentsTheSame(oldItem: OneLaunchModel, newItem: OneLaunchModel): Boolean {
            return oldItem == newItem
        }
    }
}

class LaunchesClickListener(val clickListener: (launchId: String) -> Unit) {
    fun onClick(launch: OneLaunchModel) = clickListener(launch.id)
}
