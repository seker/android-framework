package seker.framework.service.fps

import seker.framework.logger.Log
import seker.asynctask.AsyncTaskExecutor
import java.util.concurrent.ScheduledFuture

/**
 * @author xinwen
 * @since 2019.10.18
 */
class FpsFeature internal constructor(private val name: String, private val callback: FpsCallback) : Runnable {
    var future: ScheduledFuture<*>? = null

    /**
     * callback times
     */
    private var index = 0

    /**
     * update times. Used to calculate fps
     */
    private var count = 0

    /**
     * last time
     */
    private var lastTime: Long = 0

    /**
     * update
     */
    fun update() {
        count++
    }

    fun reset() {
        lastTime = System.currentTimeMillis()
        count = 0
        index = 0
    }

    override fun run() {
        val now = System.currentTimeMillis()
        val passedTime = now - lastTime
        if (passedTime < APPROXIMATE) {
            // 曾经在mtk机具上遇到过不靠谱的情况，1毫秒内，瞬间连续回调6~7次
            Log.w(TAG, "passedTime < 900, return.")
            return
        }
        val fps = (count * INT_1000 / passedTime).toInt()
        val finalIndex = ++index
        AsyncTaskExecutor.getInstance().execute { callback.onFps(name, finalIndex, fps) }
        lastTime = now
        count = 0
    }

    companion object {
        /**
         * log tag
         */
        private const val TAG = "FpsTask"
        const val APPROXIMATE = 900
        const val INT_1000 = 1000
    }
}