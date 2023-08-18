package com.example.htseafood.activity

import android.graphics.Typeface
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import com.example.htseafood.R
import kotlinx.android.synthetic.main.activity_home.*

class HomeActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_home)

        llInvoice.setOnClickListener {
            ivInvoice.setColorFilter(
                ContextCompat.getColor(this, R.color.blue),
                android.graphics.PorterDuff.Mode.SRC_IN
            )
            ivOrder.setColorFilter(
                ContextCompat.getColor(this, R.color.dark_text),
                android.graphics.PorterDuff.Mode.SRC_IN
            )
            ivShipment.setColorFilter(
                ContextCompat.getColor(this, R.color.dark_text),
                android.graphics.PorterDuff.Mode.SRC_IN
            )

            tvInvoice.setTextColor(ContextCompat.getColor(this, R.color.blue))
            tvOrder.setTextColor(ContextCompat.getColor(this, R.color.dark_text))
            tvShipment.setTextColor(ContextCompat.getColor(this, R.color.dark_text))

            tvInvoice.typeface = Typeface.createFromAsset(this.assets, "fonts/sora_bold.ttf")
            tvOrder.typeface = Typeface.createFromAsset(this.assets, "fonts/sora_regular.ttf")
            tvShipment.typeface = Typeface.createFromAsset(this.assets, "fonts/sora_regular.ttf")

            llInvoice.background = ContextCompat.getDrawable(this, R.drawable.gredient_select)
            llOrder.background = null
            llShipment.background = null

            tvTitle.text = getString(R.string.invoice)
        }

        llOrder.setOnClickListener {
            ivInvoice.setColorFilter(
                ContextCompat.getColor(this, R.color.dark_text),
                android.graphics.PorterDuff.Mode.SRC_IN
            )
            ivOrder.setColorFilter(
                ContextCompat.getColor(this, R.color.blue),
                android.graphics.PorterDuff.Mode.SRC_IN
            )
            ivShipment.setColorFilter(
                ContextCompat.getColor(this, R.color.dark_text),
                android.graphics.PorterDuff.Mode.SRC_IN
            )

            tvInvoice.setTextColor(ContextCompat.getColor(this, R.color.dark_text))
            tvOrder.setTextColor(ContextCompat.getColor(this, R.color.blue))
            tvShipment.setTextColor(ContextCompat.getColor(this, R.color.dark_text))

            tvInvoice.typeface = Typeface.createFromAsset(this.assets, "fonts/sora_regular.ttf")
            tvOrder.typeface = Typeface.createFromAsset(this.assets, "fonts/sora_bold.ttf")
            tvShipment.typeface = Typeface.createFromAsset(this.assets, "fonts/sora_regular.ttf")

            llInvoice.background = null
            llOrder.background = ContextCompat.getDrawable(this, R.drawable.gredient_select)
            llShipment.background = null

            tvTitle.text = getString(R.string.order)

        }

        llShipment.setOnClickListener {
            ivInvoice.setColorFilter(
                ContextCompat.getColor(this, R.color.dark_text),
                android.graphics.PorterDuff.Mode.SRC_IN
            )
            ivOrder.setColorFilter(
                ContextCompat.getColor(this, R.color.dark_text),
                android.graphics.PorterDuff.Mode.SRC_IN
            )
            ivShipment.setColorFilter(
                ContextCompat.getColor(this, R.color.blue),
                android.graphics.PorterDuff.Mode.SRC_IN
            )

            tvInvoice.setTextColor(ContextCompat.getColor(this, R.color.dark_text))
            tvOrder.setTextColor(ContextCompat.getColor(this, R.color.dark_text))
            tvShipment.setTextColor(ContextCompat.getColor(this, R.color.blue))

            tvInvoice.typeface = Typeface.createFromAsset(this.assets, "fonts/sora_regular.ttf")
            tvOrder.typeface = Typeface.createFromAsset(this.assets, "fonts/sora_regular.ttf")
            tvShipment.typeface = Typeface.createFromAsset(this.assets, "fonts/sora_bold.ttf")

            llInvoice.background = null
            llOrder.background = null
            llShipment.background = ContextCompat.getDrawable(this, R.drawable.gredient_select)

            tvTitle.text = getString(R.string.shipment)


        }
    }
}