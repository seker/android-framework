package seker.framework.service.countdown

import seker.framework.logger.Log
import seker.asynctask.AsyncTaskExecutor
import java.util.concurrent.ScheduledFuture
import java.util.concurrent.atomic.AtomicBoolean

/**
 * @author xinwen
 * @since 2021.04.21
 */
class CountdownFeature internal constructor(
    /**
     * 总共计数的次数
     */
    private val totalCount: Int,
    periodMills: Long,
    private val callback: CountdownCallback,
    private val service: CountdownServiceImpl
) : Runnable {
    var future: ScheduledFuture<*>? = null

    /**
     * 时间间隔（单位毫秒）
     */
    private val approximate: Long

    /**
     * 当前倒计时的次数
     */
    private var currentCount: Int

    /**
     * last time
     */
    private var lastTime: Long = 0

    /**
     * 是否暂停倒计时
     */
    private val pause = AtomicBoolean(false)

    init {
        approximate = (periodMills * APPROXIMATE).toLong()
        currentCount = totalCount
    }

    fun recount() {
        currentCount = totalCount
    }

    fun pause() {
        pause.compareAndSet(false, true)
    }

    fun resume() {
        pause.compareAndSet(true, false)
    }

    fun cancel(): Boolean {
        return service.unregister(callback)
    }

    override fun run() {
        val now = System.currentTimeMillis()
        if (pause.get()) {
            lastTime = now
            return
        }
        val passedTime = now - lastTime
        if (passedTime < approximate) {
            // 曾经在mtk机具上遇到过不靠谱的情况，1毫秒内，瞬间连续回调6~7次
            Log.w(TAG, "passedTime < $approximate, return.")
            return
        }
        val count = currentCount--
        AsyncTaskExecutor.getInstance().execute({ callback.onCountdown(totalCount, count) }, "cd_cb_$totalCount")
        if (0 > currentCount) {
            service.unregister(callback)
        } else {
            lastTime = now
        }
    }

    companion object {
        /**
         * log tag
         */
        private const val TAG = "Countdown"
        private const val APPROXIMATE = 0.9f
    }
}