package com.gdky005.webview.pro

import android.content.Intent
import android.os.Bundle
import android.os.Handler
import android.view.View
import com.zkteam.sdk.base.ZKBaseActivity

class SplashActivity : ZKBaseActivity() {

    override fun getLayoutId(): Int {
        return R.layout.activity_splash
    }

    override fun initData(bundle: Bundle?) {

        Handler().postDelayed({
            startActivity(Intent(mContext, MainActivity::class.java))
            finish()
        }, 2000)
    }

    override fun initViews(contentView: View) {
        //function
    }
}