package com.example.hairedo

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.hairedo.databinding.RecommendedLayoutBinding

class ChildAdapter(private val viewType: Int, private val recyclerItemList: List<RecyclerItem>):
    RecyclerView.Adapter<RecyclerView.ViewHolder>(){

    inner class RecommendedViewHolder(private val binding: RecommendedLayoutBinding): RecyclerView.ViewHolder(binding.root){
        fun bindRecommendedView(recyclerItem: RecyclerItem){
            binding.shopSection1Image.setImageResource(recyclerItem.image)
            binding.shopName.text = recyclerItem.shopName
            binding.salonType.text = recyclerItem.salonType
            binding.recentVisited.text = recyclerItem.visit
            binding.timeTv.text = recyclerItem.time
        }
    }

    override fun getItemViewType(position: Int): Int {
        return viewType
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        val binding = RecommendedLayoutBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return RecommendedViewHolder(binding)
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        when(holder){
            is RecommendedViewHolder -> {
                holder.bindRecommendedView(recyclerItemList[position])
            }
        }
    }

    override fun getItemCount(): Int {
        return recyclerItemList.size
    }
}