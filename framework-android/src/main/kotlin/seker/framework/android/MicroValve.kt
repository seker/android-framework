package seker.framework.android

import android.content.Context
import seker.framework.logger.Log
import seker.framework.android.desc.MicroValveDesc

/**
 * @author xinjian
 */
abstract class MicroValve : Runnable {

    protected lateinit var context: Context

    companion object Factory {
        fun instance(context: Context, description: MicroValveDesc): MicroValve? {
            var valve: MicroValve? = null
            try {
                valve = description.clazz.newInstance()
                valve.context = context
            } catch (e: Throwable) {
                Log.w(e)
            }
            return valve
        }
    }
}