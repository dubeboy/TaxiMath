package za.co.dubedivine.taximath.util

import android.support.design.widget.FloatingActionButton
import android.support.v4.content.ContextCompat
import android.support.annotation.ColorRes


fun FloatingActionButton.setBackgroundTint(@ColorRes value: Int) {
    val fabBackgroundTint = ContextCompat.getColorStateList(this.context, value)
    this.backgroundTintList = fabBackgroundTint
}