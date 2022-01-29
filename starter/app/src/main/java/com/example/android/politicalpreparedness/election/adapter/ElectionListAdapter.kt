package com.example.android.politicalpreparedness.election.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.example.android.politicalpreparedness.databinding.ListItemElectionsBinding
import com.example.android.politicalpreparedness.network.models.Election

class ElectionListAdapter(private val clickListener: ElectionListener): ListAdapter<Election, ElectionViewHolder>(ElectionDiffCallback()) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ElectionViewHolder {
        return ElectionViewHolder.from(parent)
    }

    override fun onBindViewHolder(
        holder: ElectionViewHolder,
        position: Int
    ) {
        val item = getItem(position)
        holder.bind(getItem(position)!!, clickListener)
    }

}

//Create ElectionViewHolder
class ElectionViewHolder private constructor(val binding: ListItemElectionsBinding): RecyclerView.ViewHolder(binding.root){

    fun bind(
        item: Election,
        clickListener: ElectionListener
    ) {
        //update to use BindingAdapter
        //update to use databinding
        binding.election = item
        binding.clickListener = clickListener
        binding.executePendingBindings()
    }

    //Add companion object to inflate ViewHolder (from)
    companion object {
        fun from(parent: ViewGroup): ElectionViewHolder {
            val layoutInflater = LayoutInflater.from(parent.context)
            val binding = ListItemElectionsBinding.inflate(layoutInflater,parent,false)
            return ElectionViewHolder(binding)
        }
    }

}

//Create ElectionDiffCallback
class ElectionDiffCallback: DiffUtil.ItemCallback<Election>() {
    override fun areItemsTheSame(oldItem: Election, newItem: Election): Boolean {
        return oldItem.id == newItem.id
    }

    override fun areContentsTheSame(oldItem: Election, newItem: Election): Boolean {
        return oldItem == newItem
    }

}

//Create ElectionListener
class ElectionListener(val clickListener: (elecId: Int) -> Unit){
    fun onClick(elec: Election) = clickListener(elec.id)
}