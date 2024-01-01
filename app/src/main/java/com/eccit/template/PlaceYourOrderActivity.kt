package com.eccit.template

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.TextUtils
import android.view.MenuItem
import android.view.View
import android.widget.EditText
import androidx.appcompat.app.ActionBar
import androidx.recyclerview.widget.LinearLayoutManager
import com.eccit.template.adapter.PlaceYourOrderAdapter
import com.eccit.template.databinding.ActivityMainBinding
import com.eccit.template.databinding.ActivityPlaceYourOrderBinding
import com.eccit.template.models.RestaurantModel
import java.security.interfaces.EdECKey

@Suppress("DEPRECATION")
class PlaceYourOrderActivity : AppCompatActivity() {

    var placeYourOrderAdapter: PlaceYourOrderAdapter? = null
    var isDeliveryOn: Boolean = false

    private lateinit var binding: ActivityPlaceYourOrderBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityPlaceYourOrderBinding.inflate(layoutInflater)
        val view = binding.root
        setContentView(view)


        val restaurantModel: RestaurantModel? = intent.getParcelableExtra("RestaurantModel")
        val actionBar: ActionBar? = supportActionBar
        actionBar?.setTitle(restaurantModel?.name)
        actionBar?.setSubtitle(restaurantModel?.address)
        actionBar?.setDisplayHomeAsUpEnabled(true)

        binding.buttonPlaceYourOrder.setOnClickListener {
            onPlaceOrderButtonClick(restaurantModel)
        }

        binding.switchDelivery?.setOnCheckedChangeListener { buttonView, isChecked ->

            if (isChecked) {
                binding.inputAddress.visibility = View.VISIBLE
                binding.inputCity.visibility = View.GONE
                binding.inputState.visibility = View.GONE
                binding.inputZip.visibility = View.GONE
                binding.tvDeliveryCharge.visibility = View.VISIBLE
                binding.tvDeliveryChargeAmount.visibility = View.VISIBLE
                isDeliveryOn = true
                calculateTotalAmount(restaurantModel)
            } else {
                binding.inputAddress.visibility = View.GONE
                binding.inputCity.visibility = View.GONE
                binding.inputState.visibility = View.GONE
                binding.inputZip.visibility = View.GONE
                binding.tvDeliveryCharge.visibility = View.GONE
                binding.tvDeliveryChargeAmount.visibility = View.GONE
                isDeliveryOn = false
                calculateTotalAmount(restaurantModel)
            }
        }

        initRecyclerView(restaurantModel)
        calculateTotalAmount(restaurantModel)
    }

    private fun initRecyclerView(restaurantModel: RestaurantModel?) {
        binding.cartItemsRecyclerView.layoutManager = LinearLayoutManager(this)
        placeYourOrderAdapter = PlaceYourOrderAdapter(restaurantModel?.menus)
        binding.cartItemsRecyclerView.adapter = placeYourOrderAdapter
    }

    private fun calculateTotalAmount(restaurantModel: RestaurantModel?) {
        var subtotalAmount = 0f
        for (menu in restaurantModel?.menus!!) {
            subtotalAmount += menu?.price!! * menu?.totalinCart!!

        }
        binding.tvSubtotalAmount.text = "Rp" + String.format("%.3f", subtotalAmount)
        if (isDeliveryOn) {
            binding.tvDeliveryChargeAmount.text = "Rp" + String.format("%.3f", restaurantModel.delivery_charge?.toFloat())
            subtotalAmount += restaurantModel?.delivery_charge?.toFloat()!!
        }

        binding.tvTotalAmount.text = "Rp" + String.format("%.3f", subtotalAmount)
    }

    private fun onPlaceOrderButtonClick(restaurantModel: RestaurantModel?) {
        if (TextUtils.isEmpty(binding.inputName.text.toString())) {
            binding.inputName.error = "Enter your name"
            return
        } else if (isDeliveryOn && TextUtils.isEmpty(binding.inputAddress.text.toString())) {
            binding.inputAddress.error = "Enter your address"
            return
        } else if (isDeliveryOn && TextUtils.isEmpty(binding.inputCity.text.toString())) {
            binding.inputCity.error = "Enter your City Name"
            return
        } else if (isDeliveryOn && TextUtils.isEmpty(binding.inputZip.text.toString())) {
            binding.inputZip.error = "Enter your Zip code"
            return
        } else if (TextUtils.isEmpty(binding.inputCardNumber.text.toString())) {
            binding.inputCardNumber.error = "Enter your credit card number"
            return
        } else if (TextUtils.isEmpty(binding.inputCardExpiry.text.toString())) {
            binding.inputCardExpiry.error = "Enter your credit card expiry"
            return
        } else if (TextUtils.isEmpty(binding.inputCardPin.text.toString())) {
            binding.inputCardPin.error = "Enter your credit card PIN / CVV"
            return
        }
        val intent = Intent(this@PlaceYourOrderActivity, SuccessOrderActivity::class.java)
        intent.putExtra("Restaurant Model", restaurantModel)
        startActivityForResult(intent, 1000)
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        if (requestCode == 1000) {
            setResult(RESULT_OK)
            finish()
        }
        super.onActivityResult(requestCode, resultCode, data)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when(item.itemId) {
            android.R.id.home -> finish()
            else -> {}
        }
        return super.onOptionsItemSelected(item)
    }

    override fun onBackPressed() {
        super.onBackPressed()
        setResult(RESULT_CANCELED)
        finish()
    }
}