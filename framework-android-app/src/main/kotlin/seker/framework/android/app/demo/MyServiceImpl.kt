package seker.framework.android.app.demo

import seker.framework.logger.Log

/**
 * @author xinjian
 */
class MyServiceImpl : MyService() {
    override fun onCreate() {
        Log.d("MyServiceImpl.onCreate().")
    }

    override fun sayHello() {
        Log.d("Hello, Framework!")
    }
}