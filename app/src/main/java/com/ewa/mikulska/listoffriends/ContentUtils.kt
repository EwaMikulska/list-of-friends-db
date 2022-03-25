package com.ewa.mikulska.listoffriends

import androidx.core.graphics.drawable.toDrawable
import com.ewa.mikulska.listoffriends.BitmapConverter.drawableToBitmap
import com.ewa.mikulska.listoffriends.BitmapConverter.fromBitmaptoByteArray

val image = drawableToBitmap(R.drawable.blank_profile_photo.toDrawable()) //do that better
val imageEmptyPhoto = image?.let { fromBitmaptoByteArray(it) }

