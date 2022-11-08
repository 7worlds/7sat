package fhnw.ws6c.sevensat.ext

import android.graphics.Bitmap
import android.graphics.Canvas
import android.graphics.drawable.BitmapDrawable
import android.graphics.drawable.Drawable



fun Drawable.toBitMap(): Bitmap? {

return if (this is BitmapDrawable) {
    this.bitmap
  } else {
// copying drawable object to not manipulate on the same reference
    val constantState = this.constantState ?: return null
    val drawable = constantState.newDrawable().mutate()
    val bitmap: Bitmap = Bitmap.createBitmap(
      drawable.intrinsicWidth, drawable.intrinsicHeight,
      Bitmap.Config.ARGB_8888
    )
    val canvas = Canvas(bitmap)
    drawable.setBounds(0, 0, canvas.width, canvas.height)
    drawable.draw(canvas)
    bitmap
  }
}


