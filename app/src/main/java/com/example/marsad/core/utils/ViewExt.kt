package com.example.marsad.core.utils

import android.view.View
import android.widget.ImageView
import androidx.annotation.StringRes
import androidx.fragment.app.Fragment
import com.google.android.material.snackbar.Snackbar
import com.squareup.picasso.Picasso

fun ImageView.setImageUrl(imageUrl: String) =
    Picasso.get().load(imageUrl).into(this)

fun View.hide() = run { visibility = View.GONE }
fun View.show() = run { visibility = View.VISIBLE }
fun View.invisible() = run { visibility = View.INVISIBLE }
infix fun View.visibleIf(condition: Boolean) =
    run { if (condition) show() else hide() }

infix fun View.invisibleIf(condition: Boolean) =
    run { if (condition) invisible() else show() }

infix fun View.goneIf(condition: Boolean) =
    run { if (condition) hide() else show() }

fun Fragment.showSnackbar(
    @StringRes msg: Int = 0,
    @StringRes actionLabel: Int = 0,
    action: () -> Unit = {},
    length: Int = Snackbar.LENGTH_SHORT,
) {
    Snackbar.make(requireView(), msg, length).setAction(actionLabel) { action() }.show()
}

