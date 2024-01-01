package com.eccit.template

import android.content.Intent
import android.os.Binder
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.MenuItem
import android.widget.GridLayout
import android.widget.Toast
import androidx.appcompat.app.ActionBar
import androidx.recyclerview.widget.GridLayoutManager
import com.eccit.template.adapter.MenuListAdapter
import com.eccit.template.databinding.ActivityMainBinding
import com.eccit.template.databinding.ActivityRestaurantMenuBinding
import com.eccit.template.models.Menus
import com.eccit.template.models.RestaurantModel


@Suppress("DEPRECATION")
class RestaurantMenuActivity : AppCompatActivity(), MenuListAdapter.MenuListClickListener {

    private var itemsInTheCartList: MutableList<Menus?>? = null
    private var totalItemInCartCount = 0
    private var menuList: List<Menus?>? =null
    private var menuListAdapter: MenuListAdapter? = null
    private lateinit var binding: ActivityRestaurantMenuBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityRestaurantMenuBinding.inflate(layoutInflater)
        val view = binding.root
        setContentView(view)

        val restaurantModel = intent?.getParcelableExtra<RestaurantModel>("Restaurant Model")

        val actionBar: ActionBar? = supportActionBar
        actionBar?.setTitle(restaurantModel?.name)
        actionBar?.setSubtitle(restaurantModel?.address)
        actionBar?.setDisplayHomeAsUpEnabled(true)

        menuList = restaurantModel?.menus

        initRecyclerView(menuList)
        binding.checkoutButton.setOnClickListener {
            if (itemsInTheCartList != null && itemsInTheCartList!!.size <= 0) {
                Toast.makeText(this@RestaurantMenuActivity, "Please add some items in the cart", Toast.LENGTH_LONG).show()
            }
            else {
                restaurantModel?.menus = itemsInTheCartList
                val intent = Intent(this@RestaurantMenuActivity, PlaceYourOrderActivity::class.java)
                intent.putExtra("RestaurantModel", restaurantModel)
                startActivityForResult(intent, 1000)
            }
        }
    }
    private fun initRecyclerView(menus: List<Menus?>?) {
        binding.menuRecyclerView.layoutManager = GridLayoutManager(this, 2)
        menuListAdapter = MenuListAdapter(menus, this)
        binding.menuRecyclerView.adapter = menuListAdapter
    }

    override fun addtoCartClickListener(menu: Menus) {
        if (itemsInTheCartList == null) {
            itemsInTheCartList = ArrayList()
        }
        itemsInTheCartList?.add(menu)
        totalItemInCartCount = 0
        for (menu in itemsInTheCartList!!) {
            totalItemInCartCount = totalItemInCartCount + menu?.totalinCart!!
        }
        binding.checkoutButton.text = "Checkout (" + totalItemInCartCount +") Items"

    }

    override fun updateCartClickListener(menu: Menus) {
        val index = itemsInTheCartList!!.indexOf(menu)
        itemsInTheCartList?.removeAt(index)
        itemsInTheCartList?.add(menu)
        totalItemInCartCount = 0
        for (menu in itemsInTheCartList!!) {
            totalItemInCartCount = totalItemInCartCount + menu?.totalinCart!!
        }
        binding.checkoutButton.text = "Checkout (" + totalItemInCartCount +") Items"
    }

    override fun removeFromCartClickListener(menu: Menus) {
        if (itemsInTheCartList!!.contains(menu)) {
            itemsInTheCartList?.remove(menu)
            totalItemInCartCount = 0
            for (menu in itemsInTheCartList!!) {
                totalItemInCartCount = totalItemInCartCount + menu?.totalinCart!!
            }
            binding.checkoutButton.text = "Checkout (" + totalItemInCartCount +") Items"
        }
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when(item.itemId) {
            android.R.id.home -> finish()
            else -> {}
        }
        return super.onOptionsItemSelected(item)
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == 1000 && resultCode == RESULT_OK) {
            finish()
        }
    }

}