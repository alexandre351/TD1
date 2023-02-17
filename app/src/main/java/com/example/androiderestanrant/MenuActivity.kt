package com.example.androiderestanrant


import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import androidx.recyclerview.widget.LinearLayoutManager
import com.android.volley.Request.Method
import com.android.volley.toolbox.JsonObjectRequest
import com.android.volley.toolbox.Volley
import com.example.androiderestanrant.databinding.ActivityMenuBinding
import com.example.androiderestanrant.network.MenuResult
import com.example.androiderestanrant.network.NetworkConstants
import com.google.gson.GsonBuilder
import org.json.JSONObject

enum class Category { STARTER, MAIN, DESSERT }

class MenuActivity : AppCompatActivity() {

    companion object {
        val extraKey = "extraKey"
    }

    lateinit var binding: ActivityMenuBinding
    lateinit var currentCategory: Category

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMenuBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val category = intent.getSerializableExtra(extraKey) as? Category
        currentCategory = category ?: Category.STARTER
        supportActionBar?.title = categoryName()
        // if category == null { category = STARTER }
        makeRequest()
    }

    private fun makeRequest() {
        val queue = Volley.newRequestQueue(this)
        val params = JSONObject()
        params.put(NetworkConstants.idShopKey, 1)
        val request = JsonObjectRequest(
            Method.POST,
            NetworkConstants.url,
            params,
            { result ->
                // Success of request
                Log.d("request", result.toString(2))
                parseData(result.toString())
            },
            { error ->
                // Error when request
                Log.e("request", error.toString())
            }
        )
        queue.add(request)
        //showDatas()
    }

    private fun parseData(data: String) {
        val result = GsonBuilder().create().fromJson(data, MenuResult::class.java)
        val category = result.data.first { it.name == categoryFilterKey() }
        showDatas(category)
    }

    private fun showDatas(category: com.example.androiderestanrant.network.Category) {
        binding.recyclerView.layoutManager = LinearLayoutManager(this)
        binding.recyclerView.adapter = CustomAdapter(category.items) {
            val intent = Intent(this, DetailActivity::class.java)
            intent.putExtra(DetailActivity.PLATE_EXTRA, it)
            startActivity(intent)
        }
    }

    private fun categoryName(): String {
        return when(currentCategory) {
            Category.STARTER -> getString(R.string.starter)
            Category.MAIN -> getString(R.string.main)
            Category.DESSERT -> getString(R.string.finish)
        }
    }

    private fun categoryFilterKey(): String {
        return when(currentCategory) {
            Category.STARTER -> "Entrées"
            Category.MAIN -> "Plats"
            Category.DESSERT -> "Desserts"
        }
    }
}