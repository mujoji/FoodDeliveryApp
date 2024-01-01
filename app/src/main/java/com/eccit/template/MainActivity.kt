package com.eccit.template

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.annotation.RequiresPermission.Read
import androidx.appcompat.app.ActionBar
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.eccit.template.adapter.RestaurantListAdapter
import com.eccit.template.databinding.ActivityMainBinding
import com.eccit.template.databinding.ActivitySuccessOrderBinding
import com.eccit.template.models.RestaurantModel
import com.google.firebase.Firebase
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.auth
import com.google.gson.Gson
import java.io.BufferedReader
import java.io.InputStream
import java.io.InputStreamReader
import java.io.Reader
import java.io.StringWriter
import java.io.Writer

class MainActivity : AppCompatActivity(), RestaurantListAdapter.RestaurantListClickListener {


    private lateinit var mainBinding: ActivityMainBinding
    lateinit var auth: FirebaseAuth


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        mainBinding =ActivityMainBinding.inflate(layoutInflater)
        setContentView(mainBinding.root)

        val restaurantModel = getRestaurantData()
        initRecyclerView(restaurantModel)

        auth = Firebase.auth
        mainBinding.checkoutButton.setOnClickListener {
            auth.signOut()
            startActivity(Intent(this, LoginActivity::class.java))
            finish()
        }

    }

    private fun initRecyclerView(restaurantList: List<RestaurantModel?>?) {
        val recyclerViewRestaurant = findViewById<RecyclerView>(R.id.recyclerViewRestaurant)
        recyclerViewRestaurant.layoutManager = LinearLayoutManager(this)
        val adapter = RestaurantListAdapter(restaurantList, this)
        recyclerViewRestaurant.adapter = adapter
    }

    private fun getRestaurantData(): List<RestaurantModel?>? {
        val inputStream: InputStream = resources.openRawResource(R.raw.restaurant)
        val writer: Writer = StringWriter()
        val buffer = CharArray(1024)
        try {
            val reader: Reader = BufferedReader(InputStreamReader(inputStream, "UTF-8"))
            var n : Int
            while (reader.read(buffer).also { n = it } != -1) {
                writer.write(buffer, 0, n)
            }
        }catch (e: Exception){}
        val jsonStr: String =writer.toString()
        val gson = Gson()
        val restaurantModel = gson.fromJson<Array<RestaurantModel>>(jsonStr, Array<RestaurantModel>::class.java).toList()
        return restaurantModel
    }

    override fun onItemClick(restaurantModel: RestaurantModel) {
        val intent = Intent(this@MainActivity, RestaurantMenuActivity::class.java)
        intent.putExtra("Restaurant Model", restaurantModel)
        startActivity(intent)
    }
}