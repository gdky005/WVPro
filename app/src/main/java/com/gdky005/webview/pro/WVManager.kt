package com.gdky005.webview.pro

public class WVManager {

    public var count = 0;

    companion object {
        // 双重校验锁式（Double Check)
        val instance: WVManager by lazy(mode = LazyThreadSafetyMode.SYNCHRONIZED) {
            WVManager()
        }
    }

    public fun addCount() {
        count++
    }

    public fun isAddDebug(): Boolean {
        if (count > 5) {
            return true
        }
        return false
    }

}