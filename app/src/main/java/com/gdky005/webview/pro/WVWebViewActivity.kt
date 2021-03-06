package com.gdky005.webview.pro

import android.Manifest
import android.app.Activity
import android.content.Context
import android.content.Intent
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.view.View
import androidx.annotation.ColorRes
import androidx.annotation.RequiresApi
import com.blankj.utilcode.util.BarUtils
import com.blankj.utilcode.util.ToastUtils
import com.tencent.smtt.export.external.interfaces.SslErrorHandler
import com.tencent.smtt.export.external.interfaces.WebResourceError
import com.tencent.smtt.export.external.interfaces.WebResourceRequest
import com.tencent.smtt.sdk.WebChromeClient
import com.tencent.smtt.sdk.WebSettings
import com.tencent.smtt.sdk.WebView
import com.tencent.smtt.sdk.WebViewClient
import com.zkteam.sdk.ZKBase
import com.zkteam.sdk.base.ZKBaseActivity
import kotlinx.android.synthetic.main.activity_webview.*

class WVWebViewActivity : ZKBaseActivity() {
    private var webView: WebView? = null
    private lateinit var mWebSettings: WebSettings

    private var url: String? = "http://www.gdky005.com"

    companion object {
        const val FLAG_ZK_UI_WEBVIEW_URL = "flag_zk_ui_webview_url"
    }

    override fun getLayoutId(): Int {
        return R.layout.activity_webview
    }

    override fun initData(bundle: Bundle?) {
        var newUrl = intent.getStringExtra(FLAG_ZK_UI_WEBVIEW_URL)
        initWebSettings(webView!!)

        if (newUrl.isNotEmpty()) {
            url = newUrl
        } else {
            ToastUtils.showShort("传入的 url 为空，默认展示 gdky005 网站")
        }

        webView!!.loadUrl(url)
    }

    private fun initWebSettings(webView: WebView) {
        mWebSettings = webView.settings

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            WebView.setWebContentsDebuggingEnabled(true)
        }

        webView.webViewClient = object : ZKWebViewClient() {}
        webView.webChromeClient = object : ZKWebChromeClient() {}

        mWebSettings.layoutAlgorithm = WebSettings.LayoutAlgorithm.SINGLE_COLUMN
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
//            mWebSettings.mixedContentMode = WebSettings.MIXED_CONTENT_ALWAYS_ALLOW
            mWebSettings.mixedContentMode = WebSettings.LOAD_NORMAL
            WebView.setWebContentsDebuggingEnabled(true)
        }

        //设置自适应屏幕，两者合用
        mWebSettings.useWideViewPort = true //将图片调整到适合webview的大小
        mWebSettings.loadWithOverviewMode = true // 缩放至屏幕的大小

        //缩放操作
        mWebSettings.setSupportZoom(true) //支持缩放，默认为true。是下面那个的前提。
        mWebSettings.builtInZoomControls = true //设置内置的缩放控件。若为false，则该WebView不可缩放
        mWebSettings.displayZoomControls = false //隐藏原生的缩放控件
        //设置字体大小
        val res = resources
        val fontSize = res.getDimension(R.dimen.sp_14).toInt()
        mWebSettings.defaultFontSize = fontSize
        //其他细节操作
        mWebSettings.cacheMode = WebSettings.LOAD_DEFAULT //默认
        mWebSettings.allowFileAccess = true //设置可以访问文件
        mWebSettings.javaScriptCanOpenWindowsAutomatically = true //支持通过JS打开新窗口
        mWebSettings.loadsImagesAutomatically = true //支持自动加载图片
        mWebSettings.defaultTextEncodingName = "utf-8"//设置编码格式

        mWebSettings.javaScriptEnabled = true
        mWebSettings.domStorageEnabled = true
        mWebSettings.setAppCacheMaxSize((1024 * 1024 * 8).toLong())
        val appCachePath = webView.context.cacheDir.absolutePath
        mWebSettings.setAppCachePath(appCachePath)
        mWebSettings.allowFileAccess = true
        mWebSettings.setAppCacheEnabled(true)
    }

    override fun initLifecycleObserve() {
        //function
    }

    override fun initListener() {
        //function
    }

    override fun initViews(contentView: View) {
//        showBarState(contentView, R.color.white, true)
//        BarUtils.setStatusBarLightMode(this, false)
        webView = wv_ui_webview
    }

    override fun onDebouncingClick(view: View) {
        //function
    }


    protected fun showBarState(
        rootView: View?, @ColorRes color: Int,
        isShow: Boolean
    ) {
        var color = color
        if (isShow) {
            BarUtils.subtractMarginTopEqualStatusBarHeight(rootView!!)
            if (color == 0) {
                color = R.color.transparent
            }
            val activity: Activity = this
            if (activity != null) {
                BarUtils.setStatusBarLightMode(activity, true)
                BarUtils.setStatusBarColor(activity, activity.resources.getColor(color), true)
            }
        }
    }


    private open inner class ZKWebViewClient : WebViewClient() {

        private var mIsError: Boolean = false

        @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
        override fun shouldOverrideUrlLoading(view: WebView, request: WebResourceRequest): Boolean {
            return filterUri(ZKBase.context(), request.url)
        }

        override fun shouldOverrideUrlLoading(view: WebView, url: String): Boolean {
            val uri = Uri.parse(url)
            return filterUri(ZKBase.context(), uri)
        }

        override fun onReceivedSslError(
            view: WebView?,
            handler: SslErrorHandler?,
            p2: com.tencent.smtt.export.external.interfaces.SslError?
        ) {
//            handler.cancel() //super中默认的处理方式，WebView变成空白页
            handler?.proceed()
        }

        override fun onReceivedError(view: WebView, request: WebResourceRequest, error: WebResourceError) {
            mIsError = true
        }

        override fun onReceivedError(view: WebView, errorCode: Int, description: String, failingUrl: String) {
            mIsError = true
        }
    }

    fun filterUri(context: Context, uri: Uri?): Boolean {
        if (uri == null) return false
        val url = uri.toString()
        this.url = url
        // 打电话
        if (url.contains("tel:")) {
            checkPermission()
            return true
        }
        return false
    }


    private fun checkPermission() {
        val permissions = arrayOf(Manifest.permission.CALL_PHONE)
//        todo 添加相关权限库
//        if (AndPermission.hasPermissions(this, permissions)) {
//            callPhone()
//        } else {
//            AndPermission.with(this)
//                .runtime()
//                .permission(permissions)
//                .onGranted {
//                    // Storage permission are allowed.
//                    callPhone()
//                }
//                .onDenied {
//                    ToastUtils.showShort("获取权限失败")
//                }
//                .start()
//
//        }
    }

    private fun callPhone() {
        val intent = Intent(Intent.ACTION_CALL, Uri.parse(url))
        startActivity(intent)
    }

    private open inner class ZKWebChromeClient : WebChromeClient() {

        override fun onReceivedTitle(view: WebView, title: String) {
            super.onReceivedTitle(view, title)
        }
    }

    override fun onBackPressed() {
        if (webView != null) {
            if (webView!!.canGoBack()) {
                webView!!.goBack()
            } else {
                super.onBackPressed()
            }
        }
    }

    override fun isSwipeBack(): Boolean {
        return false
    }
}