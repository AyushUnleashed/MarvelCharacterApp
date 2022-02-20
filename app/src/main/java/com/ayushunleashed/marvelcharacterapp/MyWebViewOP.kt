package com.ayushunleashed.marvelcharacterapp

import android.os.Bundle
import android.webkit.WebView
import android.webkit.WebViewClient
import androidx.appcompat.app.AppCompatActivity


class MyWebViewOP : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_my_web_view_op)

        var comicsUrl = intent.getStringExtra("comicsUrl")
        var detailsUrl = intent.getStringExtra("detailsUrl")

        val myWebView: WebView = findViewById(R.id.webview)
        //code to display it on the app itself
        myWebView.setWebViewClient(WebViewClient())
        if (detailsUrl != null) {
            myWebView.loadUrl(detailsUrl)
        }
        if (comicsUrl != null) {
            myWebView.loadUrl(comicsUrl)
        }
    }
}