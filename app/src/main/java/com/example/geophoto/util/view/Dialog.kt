package com.example.geophoto.util.view

import android.content.Context
import androidx.lifecycle.LifecycleOwner
import com.afollestad.materialdialogs.MaterialDialog
import com.afollestad.materialdialogs.lifecycle.lifecycleOwner
import com.example.geophoto.R


fun Context.dialogBuilder(owner: LifecycleOwner, title: Int): MaterialDialog {
    return MaterialDialog(this)
        .lifecycleOwner(owner)
        .title(title)
        .cornerRadius(res = R.dimen.common_radius)
        .cancelOnTouchOutside(cancelable = true)
}