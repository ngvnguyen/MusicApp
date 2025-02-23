package com.sf.musicapp.utils

import android.app.Service
import android.content.ClipData
import android.content.ClipboardManager
import android.content.Context
import android.content.Intent
import android.net.ConnectivityManager
import android.net.Uri
import android.widget.Toast

fun Context.copyLinkToClipBoard(url: String) {
    val clipboard = getSystemService(Context.CLIPBOARD_SERVICE) as ClipboardManager
    val clip = ClipData.newPlainText("URL", url)
    clipboard.setPrimaryClip(clip)
    Toast.makeText(this, "Link copied to clipboard", Toast.LENGTH_SHORT)
        .show()
}

fun Context.openLinkIntent(url: String) {
    val i = Intent(Intent.ACTION_VIEW)
    i.data = Uri.parse(url)
    startActivity(i)
}

fun Context.shareText(text:String){
    val i = Intent(Intent.ACTION_SEND).apply {
        type = "text/plain"
        putExtra(Intent.EXTRA_TEXT,text)
    }
    startActivity(Intent.createChooser(i,"Share on"))
}
fun Context.isNetworkAvailable():Boolean{
    val connectivityManager = getSystemService(Service.CONNECTIVITY_SERVICE) as ConnectivityManager
    return connectivityManager.activeNetwork!= null
}