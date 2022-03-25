package com.ewa.mikulska.listoffriends

import android.graphics.Bitmap
import android.graphics.Canvas
import android.graphics.drawable.BitmapDrawable
import android.graphics.drawable.Drawable
import androidx.room.TypeConverter
import org.joda.time.LocalDate
import java.io.ByteArrayOutputStream

object BitmapConverter {

    @TypeConverter
    fun fromBitmaptoByteArray(bitmap: Bitmap): ByteArray {
        val outputStream = ByteArrayOutputStream()
        bitmap.compress(Bitmap.CompressFormat.PNG, 100, outputStream)
        return outputStream.toByteArray()
    }

    @TypeConverter
    fun drawableToBitmap(drawable: Drawable): Bitmap? {
        if (drawable is BitmapDrawable) {
            return drawable.bitmap
        }
        val width = if (!drawable.bounds.isEmpty) drawable
            .bounds.width() else drawable.intrinsicWidth
        val height = if (!drawable.bounds.isEmpty) drawable
            .bounds.height() else drawable.intrinsicHeight
        val bitmap = Bitmap.createBitmap(
            if (width <= 0) 1 else width,
            if (height <= 0) 1 else height, Bitmap.Config.ARGB_8888
        )
        val canvas = Canvas(bitmap)
        drawable.setBounds(0, 0, canvas.getWidth(), canvas.getHeight())
        drawable.draw(canvas)
        return bitmap
    }
}

object DateTimeConverter {

    @TypeConverter
    fun toDateTime(string: String?): LocalDate? {
        return try {
            LocalDate.parse(string)
        } catch (
            e: Exception
        ) {
            null
        }
    }

    @TypeConverter
    fun toMillis(dateTime: LocalDate?): String? {
        return try {
            dateTime?.toString()
        } catch (
            e: Exception
        ) {
            null
        }
    }
}