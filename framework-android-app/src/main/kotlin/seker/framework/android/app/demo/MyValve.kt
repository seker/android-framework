package seker.framework.android.app.demo

import seker.framework.android.MicroValve
import seker.framework.logger.Log

/**
 * @author xinjian
 */
class MyValve : MicroValve() {
    override fun run() {
        Log.d("MyValve.run()")
    }
}