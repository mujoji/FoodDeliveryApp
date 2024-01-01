package com.eccit.template

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.TextView
import androidx.appcompat.app.ActionBar
import com.eccit.template.databinding.ActivityPlaceYourOrderBinding
import com.eccit.template.databinding.ActivitySuccessOrderBinding
import com.eccit.template.models.RestaurantModel

@Suppress("DEPRECATION")
class SuccessOrderActivity : AppCompatActivity() {

    private lateinit var binding: ActivitySuccessOrderBinding


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivitySuccessOrderBinding.inflate(layoutInflater)
        val view = binding.root
        setContentView(view)


        val restaurantModel: RestaurantModel? = intent.getParcelableExtra("RestaurantModel")
        val actionBar: ActionBar? = supportActionBar
        actionBar?.setTitle(restaurantModel?.name)
        actionBar?.setSubtitle(restaurantModel?.address)
        actionBar?.setDisplayHomeAsUpEnabled(false)


        binding.buttonDone.setOnClickListener {
            setResult(RESULT_OK)
            finish()
        }
    }
}