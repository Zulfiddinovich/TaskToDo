package uz.gita.tasktodo.util

import android.app.Activity
import android.content.Context
import android.view.View
import android.widget.Toast
import androidx.fragment.app.Fragment
import com.google.android.material.snackbar.Snackbar

fun Context.showToast(message: String, duration: Int = Toast.LENGTH_LONG) {
    Toast.makeText(this, message, duration).show()
}

fun Fragment.showToast(message: String) {
    requireContext().showToast(message)
}

fun Activity.showSnackbar(view: View, msg: String, action: (() -> Unit)? = null) {
    Snackbar.make(
        view,
        msg,
        Snackbar.LENGTH_LONG).also {
            it.setAction("Ok"){
//                action?.invoke()
            }.show()
    }
}
fun Fragment.showSnackbar(view: View, msg: String, action: (() -> Unit)? = null) {
    Snackbar.make(
        view,
        msg,
        Snackbar.LENGTH_LONG).also {
        it.setAction("Ok"){
            action?.invoke()
        }.show()
    }
}

/*
* adapter.itemClickListener = {
* snackbar(it (this), item.title + " is clicked")
* }
* */

/*
* binding.item.setOnClickListener {
* snackbar(it, "I Clicked")
* }
* */
