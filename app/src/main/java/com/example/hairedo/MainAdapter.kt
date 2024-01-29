package com.example.hairedo

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.PagerSnapHelper
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.RecyclerView.ViewHolder
import com.example.hairedo.databinding.EachItemBinding
import com.example.hairedo.databinding.RecommendedLayoutBinding

class MainAdapter(private val dataItemList: List<DataItem>):RecyclerView.Adapter<ViewHolder>() {

    inner class RecyclerItemViewHolder(private val binding: EachItemBinding): RecyclerView.ViewHolder(binding.root){
        init{
            binding.childRecyclerView.setHasFixedSize(true)
            binding.childRecyclerView.layoutManager = LinearLayoutManager(binding.root.context, RecyclerView.HORIZONTAL, false)
        }
        fun bindRecommendedRecyclerView(recyclerItemList: List<RecyclerItem>){
            val snapHelper = PagerSnapHelper()
            snapHelper.attachToRecyclerView(binding.childRecyclerView)
            val adapter = ChildAdapter(DataItemType.RECOMMENDED, recyclerItemList)
            binding.childRecyclerView.adapter = adapter
        }
    }

    override fun getItemViewType(position: Int): Int {
        return R.layout.each_item
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val binding = EachItemBinding.inflate(LayoutInflater.from(parent.context), parent,false)
        return RecyclerItemViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        dataItemList[position].recyclerItemList?.let {
            (holder as RecyclerItemViewHolder).bindRecommendedRecyclerView(
                it
            )
        }
    }

    override fun getItemCount(): Int {
        return dataItemList.size
    }
}