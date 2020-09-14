package com.example.baseandroid.util

import android.annotation.SuppressLint
import android.widget.ImageView
import android.widget.TextView
import androidx.core.net.toUri
import androidx.databinding.BindingAdapter
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions
import com.example.baseandroid.R
import java.math.RoundingMode
import java.text.DecimalFormat
import java.util.*

@SuppressLint("DefaultLocale")
@BindingAdapter("textUpper")
fun TextView.toUpperCase(text: String?) {
    text?.let {
        this.text = it.toUpperCase()
    }
}


@BindingAdapter("imageUrl")
fun ImageView.imageUrl(imgUrl: String?) {
    imgUrl?.let {
        val imgUri = it.toUri().buildUpon().scheme("https").build()
        Glide.with(this.context)
            .load(imgUri)
            .apply(
                RequestOptions()
                    .placeholder(R.drawable.ic_image_search)
                    .error(R.drawable.ic_broken_image_black_24dp)
            )
            .into(this)
    }
}

@BindingAdapter("formatMoney")
fun TextView.formatMoney(money: Double) {
    val format = DecimalFormat.getCurrencyInstance(Locale.forLanguageTag("es-MX"))
    format.minimumFractionDigits = 2
    format.roundingMode = RoundingMode.DOWN
    this.text = format.format(money).trim()
}