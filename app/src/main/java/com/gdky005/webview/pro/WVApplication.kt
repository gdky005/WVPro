package com.gdky005.webview.pro

import com.zkteam.sdk.base.ZKBaseApplication
import com.zkteam.ui.components.ZKUIManager

class WVApplication : ZKBaseApplication() {

    override fun onCreate() {
        super.onCreate()
        val zkUiManager = ZKUIManager.instance
        zkUiManager.setShowGuide(false) // 设置不启用引导页面
        zkUiManager.setMainActivity(SplashActivity::class.java) // 设置进入的主页面

    }
}