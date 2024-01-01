package com.eccit.template.adapter

import android.media.Image
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.eccit.template.R
import com.eccit.template.databinding.ActivityRestaurantMenuBinding
import com.eccit.template.databinding.MenuListRowBinding
import com.eccit.template.models.Menus
import com.google.android.material.timepicker.TimeFormat
import org.w3c.dom.Text

class MenuListAdapter(val menuList: List<Menus?>?, val clickListener: MenuListClickListener): RecyclerView.Adapter<MenuListAdapter.MyViewHolder>() {

    private lateinit var binding: MenuListRowBinding

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): MenuListAdapter.MyViewHolder {
        val binding = MenuListRowBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return MyViewHolder(binding)
    }

    override fun onBindViewHolder(holder: MenuListAdapter.MyViewHolder, position: Int) {
        holder.bind(menuList?.get(position)!!)
    }

    override fun getItemCount(): Int {
        return if (menuList == null)return 0 else menuList.size
    }

    inner class MyViewHolder(val binding: MenuListRowBinding): RecyclerView.ViewHolder(binding.root) {
        var thumbImage: ImageView = binding.thumbImage
        val menuName: TextView = binding.menuName
        val menuPrice: TextView = binding.menuPrice
        val addtoCartButton: TextView = binding.addToCartButton
        val addMoreLayout: LinearLayout = binding.addMoreLayout
        val imageMinus: ImageView = binding.imageMinus
        val imageAddOne: ImageView = binding.imageAddOne
        val tvCount: TextView = binding.tvCount

        fun bind(menus: Menus) {
            menuName.text = menus?.name
            menuPrice.text = "Rp" + String.format("%.3f", menus?.price)
            addtoCartButton.setOnClickListener {
                menus?.totalinCart = 1
                clickListener.addtoCartClickListener(menus)
                addMoreLayout?.visibility = View.VISIBLE
                addtoCartButton.visibility = View.GONE
                tvCount.text = menus?.totalinCart.toString()
            }
            imageMinus.setOnClickListener {
                var total: Int = menus?.totalinCart!!
                total--
                if (total > 0) {
                    menus?.totalinCart = total
                    clickListener.updateCartClickListener(menus)
                    tvCount.text = menus?.totalinCart.toString()
                } else {
                    menus.totalinCart = total
                    clickListener.removeFromCartClickListener(menus)
                    addMoreLayout.visibility = View.GONE
                    addtoCartButton.visibility = View.VISIBLE
                }
            }
            imageAddOne.setOnClickListener {
                var total: Int = menus?.totalinCart!!
                total++
                if (total <= 10) {
                    menus.totalinCart = total
                    clickListener.updateCartClickListener(menus)
                    tvCount.text = total.toString()
                }
            }

            Glide.with(thumbImage)
                .load(menus?.url)
                .into(thumbImage)

        }
    }

    interface MenuListClickListener {
        fun addtoCartClickListener(menu: Menus)
        fun updateCartClickListener(menu: Menus)
        fun removeFromCartClickListener(menu: Menus)
    }
}