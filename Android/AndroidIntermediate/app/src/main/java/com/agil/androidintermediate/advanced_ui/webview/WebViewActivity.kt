package com.agil.androidintermediate.advanced_ui.webview

import android.annotation.SuppressLint
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.webkit.JsResult
import android.webkit.WebChromeClient
import android.webkit.WebView
import android.webkit.WebViewClient
import android.widget.Toast
import com.agil.androidintermediate.databinding.ActivityWebViewBinding

class WebViewActivity : AppCompatActivity() {
    private lateinit var binding: ActivityWebViewBinding
    private val myUrl = "https://www.dicoding.com"

    @SuppressLint("SetJavaScriptEnabled")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityWebViewBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val webView = binding.webView

        webView.settings.javaScriptEnabled = true
        webView.webViewClient = object : WebViewClient() {
            override fun onPageFinished(view: WebView, url: String) {
                view.loadUrl("javascript:alert('Web $myUrl berhasil dimuat')")
                //Toast.makeText(this@WebViewActivity, "Web $myUrl berhasil dimuat", Toast.LENGTH_LONG).show()
            }
        }

        webView.webChromeClient = object : WebChromeClient(){
            override fun onJsAlert(view: WebView?, url: String?, message: String?, result: JsResult?
            ): Boolean {
                Toast.makeText(this@WebViewActivity, message, Toast.LENGTH_LONG).show()
                result?.confirm()
                return true
            }
        }
        webView.loadUrl(myUrl)
    }
}