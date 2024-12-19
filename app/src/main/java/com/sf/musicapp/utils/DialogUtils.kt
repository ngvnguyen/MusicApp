package com.sf.musicapp.utils

import android.content.Context
import android.content.DialogInterface
import android.content.Intent
import android.net.Uri
import android.provider.Settings
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import kotlin.system.exitProcess

object DialogUtils {
    fun showDialog(
        context: Context,
        title:String,
        message:String,
        negativeButtonTitle:String?=null,
        positiveButtonTitle:String?=null,
        negativeButtonAction:()->Unit={},
        positiveButtonAction:()->Unit={}
    ) {
        val dialog = MaterialAlertDialogBuilder(context)
        dialog.setTitle(title)
        dialog.setMessage(message)
        negativeButtonTitle?.let{
            dialog.setNegativeButton(it) { _: DialogInterface?, _: Int ->
                negativeButtonAction()
            }
        }
        positiveButtonTitle?.let{
            dialog.setPositiveButton(it) { _: DialogInterface?, _: Int ->
                positiveButtonAction()
            }
        }
        dialog.show()
    }
}