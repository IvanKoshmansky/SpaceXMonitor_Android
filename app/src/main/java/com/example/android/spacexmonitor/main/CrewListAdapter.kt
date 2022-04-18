package com.example.android.spacexmonitor.main

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.example.android.spacexmonitor.databinding.CrewItemBinding
import com.example.android.spacexmonitor.models.CrewMember

class CrewListAdapter () : ListAdapter<CrewMember, CrewListAdapter.CrewHolder> (DiffCallback()) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CrewHolder {
        return CrewHolder.from(parent)
    }

    override fun onBindViewHolder(holder: CrewHolder, position: Int) {
        val item = getItem(position)
        holder.bind(item)
    }

    class CrewHolder (val binding: CrewItemBinding) : RecyclerView.ViewHolder(binding.root) {

        fun bind(item: CrewMember) {
            binding.crewMember = item
            binding.executePendingBindings()
        }

        companion object {
            fun from(parent: ViewGroup): CrewHolder {
                val layoutInflater = LayoutInflater.from(parent.context)
                val binding = CrewItemBinding.inflate(layoutInflater, parent, false)
                return CrewHolder(binding)
            }
        }
    }

    class DiffCallback: DiffUtil.ItemCallback<CrewMember>() {
        override fun areItemsTheSame(oldItem: CrewMember, newItem: CrewMember): Boolean {
            return oldItem.name == newItem.name
        }
        override fun areContentsTheSame(oldItem: CrewMember, newItem: CrewMember): Boolean {
            return oldItem == newItem
        }
    }
}
