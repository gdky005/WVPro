package com.gdky005.webview.pro

import android.content.Intent
import android.os.Bundle
import android.view.View
import com.zkteam.sdk.base.ZKBaseActivity
import com.zkteam.ui.components.webview.ZKWebViewActivity

class MainActivity : ZKBaseActivity() {

    private var debugUrl = "http://debugtbs.qq.com/"
    private var url = "http://www.beijiaofuxue.com/static/pad1/index.html"


    override fun getLayoutId(): Int {
        return R.layout.activity_main
    }

    override fun initData(bundle: Bundle?) {
        if (WVManager.instance.isAddDebug()) {
            url = debugUrl
        }
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

    override fun onDestroy() {
        super.onDestroy()
        WVManager.instance.clearCount()
    }
}