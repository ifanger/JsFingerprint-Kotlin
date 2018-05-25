package com.example.ifanger.fpmanager

import android.annotation.SuppressLint
import android.content.Context
import android.webkit.JsResult
import android.webkit.WebChromeClient
import android.webkit.WebView
import com.example.ifanger.fpmanager.model.response.JavascriptResultResponse
import com.google.gson.Gson
import java.io.InputStream

/**
 * Master class
 */
class JsFingerprint(private val context: Context) {
    /**
     * WebView reefer url (not useful).
     */
    private val PARAM_BASE_URL = "blarg://ignored"

    /**
     * WebView content type.
     */
    private val PARAM_MIME_TYPE = "text/html"

    /**
     * WebView text encoding.
     */
    private val PARAM_ENCODING = "utf-8"

    /**
     * WebView parameter.
     */
    private val PARAM_HISTORY_URL = ""

    /**
     * Listener declaration.
     */
    private var listener: WebViewListener? = null

    /**
     * Initializes the WebView and loads the Javascript.
     */
    @SuppressLint("SetJavaScriptEnabled")
    fun get() {
        val webView = WebView(context)
        val htmlResource: InputStream? = context.resources?.
                openRawResource(R.raw.fingerprint)
        val htmlPage = InputStreamManager().inputStreamToString(htmlResource)

        webView.apply {
            settings.javaScriptEnabled = true
            webChromeClient = object : WebChromeClient() {
                override fun onJsAlert(view: WebView?, url: String?, message: String?, result: JsResult?): Boolean {
                    val jsResultResponse: JavascriptResultResponse = Gson().fromJson(
                            message,
                            JavascriptResultResponse::class.java
                    )

                    listener?.onResponseReceived(jsResultResponse)
                    result?.confirm()
                    webView.destroy()
                    return true
                }
            }

            loadDataWithBaseURL(PARAM_BASE_URL, htmlPage, PARAM_MIME_TYPE, PARAM_ENCODING, PARAM_HISTORY_URL)
        }
    }

    /**
     * Attach the listener.
     */
    fun setWebViewListener(listener: WebViewListener) {
        this.listener = listener
    }

    /**
     * WebView interface responsible for the listeners.
     */
    interface WebViewListener {
        /**
         * Listener called when the Javascript reaches an result.
         */
        fun onResponseReceived(jsResultResponse: JavascriptResultResponse)
    }
}