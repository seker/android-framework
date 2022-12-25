package seker.framework.android.app

import android.util.Log
import seker.framework.logger.Logger

/**
 * @author xinjian
 */
class AndroidLogger : Logger() {

    override fun println(priority: Int, tag: String, msg: String): Int {
        val currentThread = Thread.currentThread()
        return Log.println(priority, tag, "[${currentThread.id}|${currentThread.name}] $msg")
    }
}