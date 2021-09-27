package com.example.geophoto.presentation.util

import android.app.AlertDialog
import android.content.Context


fun Context.alertDialog(
	title: String,
	mes: String,
	positiveBtn: String,
	neutralBtn: String = "",
	negativeBtn: String = "",
	countBtn: Int = 1,
	context: Context
) {
	
	val builder: AlertDialog.Builder = AlertDialog.Builder(context)
	builder.setTitle(title)
	builder.setMessage(mes)
	
	when (countBtn) {
		1 -> builder.setPositiveButton(positiveBtn, null)
		2 -> {
			builder.setPositiveButton(positiveBtn, null)
			builder.setNeutralButton(neutralBtn, null);
		}
		3 -> {
			builder.setPositiveButton(positiveBtn, null);
			builder.setNeutralButton(neutralBtn, null);
			builder.setNegativeButton(negativeBtn, null);
		}
	}
	
	val dialog: AlertDialog = builder.create()
	dialog.show()
}