package com.eccit.template.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.SimpleAdapter
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.eccit.template.R
import com.eccit.template.models.Hours
import com.eccit.template.models.RestaurantModel
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Date
import java.util.Locale

class RestaurantListAdapter(val restaurantList: List<RestaurantModel?>?, val clickListener: RestaurantListClickListener): RecyclerView.Adapter<RestaurantListAdapter.MyViewHolder>() {
    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): RestaurantListAdapter.MyViewHolder {
        val view: View = LayoutInflater.from(parent.context).inflate(R.layout.recycler_restaurant_list_row, parent, false)

        return MyViewHolder(view)
    }

    override fun onBindViewHolder(holder: RestaurantListAdapter.MyViewHolder, position: Int) {
        holder.bind(restaurantList?.get(position))
        holder.itemView.setOnClickListener {
            clickListener.onItemClick(restaurantList?.get(position)!!)
        }
    }

    override fun getItemCount(): Int {
        return restaurantList?.size!!
    }

    inner class MyViewHolder(view: View): RecyclerView.ViewHolder(view)
    {
        val thumbImage: ImageView = view.findViewById(R.id.thumbImage)
        val tvRestaurantName: TextView = view.findViewById(R.id.tvRestaurantName)
        val tvRestaurantAddress: TextView = view.findViewById(R.id.tvRestaurantAddress)
        val tvRestaurantHours: TextView = view.findViewById(R.id.tvRestaurantHours)
        fun bind(restaurantModel: RestaurantModel?) {
            tvRestaurantName.text = restaurantModel?.name
            tvRestaurantAddress.text = "Adress : "+restaurantModel?.address
            tvRestaurantHours.text = "Hours: " + getTodaysHours(restaurantModel?.hours!!)

            Glide.with(thumbImage)
                .load(restaurantModel?.image)
                .into(thumbImage)

        }
    }

    private fun getTodaysHours(hours: Hours): String? {
        val calendar: Calendar = Calendar.getInstance()
        val date: Date = calendar.time
        val day: String = SimpleDateFormat("EEEE", Locale.ENGLISH).format(date.time)
        return when(day) {
            "Sunday" -> hours.Sunday
            "Monday" -> hours.Monday
            "Tuesday" -> hours.Tuesday
            "Wednesday" -> hours.Wednesday
            "Thursday" -> hours.Thursday
            "Friday" -> hours.Friday
            "Saturday" -> hours.Saturday
            else -> hours.Sunday
        }
    }

    interface RestaurantListClickListener {
        fun onItemClick(restaurantModel: RestaurantModel)
    }
}