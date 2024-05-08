package com.example.contacthelper.adapters

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.contacthelper.databinding.ItemBinding
import com.example.contacthelper.models.Contacts

class RvAdapter(var list: List<Contacts>)
    :RecyclerView.Adapter<RvAdapter.Vh>(){

    inner class Vh(var itemBinding: ItemBinding):RecyclerView.ViewHolder(itemBinding.root){
        fun onBind(contact: Contacts){
            itemBinding.txtName.text = contact.name
            itemBinding.txtNumber.text = contact.number
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): Vh {
        return Vh(ItemBinding.inflate(LayoutInflater.from(parent.context),parent,false))
    }

    override fun onBindViewHolder(holder: Vh, position: Int) {
        holder.onBind(list[position])
    }

    override fun getItemCount(): Int = list.size
}