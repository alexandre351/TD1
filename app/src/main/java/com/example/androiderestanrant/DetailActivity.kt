package com.example.androiderestanrant

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.example.androiderestanrant.databinding.ActivityDetailBinding
import com.example.androiderestanrant.network.Plate

class DetailActivity : AppCompatActivity() {

    companion object {
        val PLATE_EXTRA = "PLATE_EXTRA"
    }

    lateinit var binding: ActivityDetailBinding
    var plate: Plate? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityDetailBinding.inflate(layoutInflater)
        setContentView(binding.root)

        plate = intent.getSerializableExtra(PLATE_EXTRA) as? Plate

        val ingredients = plate?.ingredients?.map { it.name }?.joinToString(", ") ?: ""
        binding.textView.text = ingredients
        val prix = plate?.prices?.map {it.price }?.joinToString(", " ) ?: ""
        var quantity = 0
        val totalPrice = TotalPrice(tot_price = quantity * prix.toFloat())
        binding.BoutonPlus.setOnClickListener{
            quantity ++
            binding.QuantityDish.text = "$quantity"

            totalPrice.tot_price = totalPrice.tot_price + prix.toFloat()
            binding.PriceButton.text = "Total : " + totalPrice.tot_price + " €"
        }
        binding.BoutonMoins.setOnClickListener{
            if(quantity > 0){
                quantity --
                binding.QuantityDish.text = "$quantity"

                totalPrice.tot_price = totalPrice.tot_price - prix.toFloat()
                binding.PriceButton.text = "Total : " + totalPrice.tot_price + " €"
            }
        }


        plate?.let {
            binding.viewPager2.adapter = PhotoAdapter(it.images, this)
        }
    }
    data class TotalPrice (
        var tot_price : Float = 0.0F
    )
}
