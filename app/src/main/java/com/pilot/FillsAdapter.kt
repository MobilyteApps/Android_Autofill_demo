package com.pilot

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.pilot.database.FillsDTO
import com.pilot.databinding.FillsListItemBinding

class FillsAdapter(
    val clickListener: MyFillsListener
) : ListAdapter<FillsDTO, FillsAdapter.FillsViewHolder>(FillsDiffCallback()) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): FillsViewHolder {
        return FillsViewHolder.from(parent)
    }

    override fun onBindViewHolder(holder: FillsViewHolder, position: Int) {
        val item = getItem(position)
        holder.bind(item, clickListener)
    }


    class FillsViewHolder private constructor(val binding: FillsListItemBinding) : RecyclerView.ViewHolder(binding.root) {

        companion object {
            fun from(parent: ViewGroup): FillsViewHolder {
                val layoutInflater = LayoutInflater.from(parent.context)
                val binding = FillsListItemBinding.inflate(layoutInflater, parent, false)

                return FillsViewHolder(binding)
            }
        }

        fun bind(
            item: FillsDTO,
            clickListener: MyFillsListener
        ) {
            binding.fill = item
            binding.clickListener = clickListener
            binding.executePendingBindings()
        }
    }


}

class FillsDiffCallback : DiffUtil.ItemCallback<FillsDTO>(){
    override fun areItemsTheSame(oldItem: FillsDTO, newItem: FillsDTO): Boolean {
        return oldItem.fillID == oldItem.fillID
    }

    override fun areContentsTheSame(oldItem: FillsDTO, newItem: FillsDTO): Boolean {
        return oldItem == newItem
    }

}

class MyFillsListener(
    var clickListener: (fillId : Long) -> Unit
){
    fun onClick(fill : FillsDTO){
        clickListener(fill.fillID)
    }
}