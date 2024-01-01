package com.eccit.template.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.eccit.template.databinding.PlaceyourorderListRowBinding
import com.eccit.template.models.Menus

class PlaceYourOrderAdapter(val menuList: List<Menus?>?): RecyclerView.Adapter<PlaceYourOrderAdapter.MyViewHolder>() {

    private lateinit var binding: PlaceyourorderListRowBinding
    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): PlaceYourOrderAdapter.MyViewHolder {
        val binding = PlaceyourorderListRowBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return MyViewHolder(binding)
    }

    override fun onBindViewHolder(holder: PlaceYourOrderAdapter.MyViewHolder, position: Int) {
        holder.bind(menuList?.get(position)!!)
    }

    override fun getItemCount(): Int {
        return if (menuList == null) 0 else menuList.size
    }

    inner class MyViewHolder(val binding: PlaceyourorderListRowBinding): RecyclerView.ViewHolder(binding.root) {
        val thumbImage: ImageView = binding.thumbImage
        val menuName: TextView = binding.menuName
        val menuPrice: TextView = binding.menuPrice
        val menuQty: TextView = binding.menuQty

        fun bind(menu: Menus) {
            menuName.text = menu?.name!!
            menuPrice.text = "Rp" + String.format("%.3f", menu?.price!! * menu.totalinCart)
            menuQty.text = "Qty: " + menu?.totalinCart

            Glide.with(thumbImage)
                .load(menu?.url)
                .into(thumbImage)
        }
    }
}