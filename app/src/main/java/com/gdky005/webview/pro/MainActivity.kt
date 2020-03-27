package com.gdky005.webview.pro

import android.content.Intent
import android.os.Bundle
import android.view.View
import com.zkteam.sdk.base.ZKBaseActivity
import com.zkteam.ui.components.webview.ZKWebViewActivity

class MainActivity : ZKBaseActivity() {
    override fun getLayoutId(): Int {
        return R.layout.activity_main
    }

    override fun initData(bundle: Bundle?) {
        val url = "http://47.74.247.185/pad/index.html"
        startWebViewActivity(url)
        finish()
    }

    override fun initViews(contentView: View) {
        //function
    }

    private fun startWebViewActivity(url: String) {
        startActivity(
            Intent(this, WVWebViewActivity::class.java)
            .putExtra(ZKWebViewActivity.FLAG_ZK_UI_WEBVIEW_URL, url))
    }
}