package seker.framework.android.app

import android.app.Application
import android.content.Context
import seker.framework.android.Framework
import seker.framework.logger.Log

/**
 * @author xinjian
 */
class MyApplication : Application() {

    override fun attachBaseContext(base: Context?) {
        super.attachBaseContext(base)

        Log.setLogger(AndroidLogger())

        Framework.init(this)
    }
}