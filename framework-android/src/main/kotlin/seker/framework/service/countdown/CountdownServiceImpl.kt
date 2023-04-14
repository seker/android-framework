package seker.framework.service.countdown

import seker.framework.logger.Log
import android.util.SparseArray
import seker.asynctask.AsyncTaskExecutor
import java.util.concurrent.TimeUnit

/**
 * @author xinwen
 * @since 2021.04.21
 */
class CountdownServiceImpl : CountdownService() {
    private val tasks = SparseArray<CountdownFeature>()
    override fun onCreate() {}
    override fun register(total: Int, period: Long, unit: TimeUnit?, callback: CountdownCallback): CountdownFeature {
        val key = callback.hashCode()
        return if (tasks.indexOfKey(key) >= 0) {
            Log.w(
                TAG,
                "register() already exist : total = $total, callback=$callback, should cancel it firstly!"
            )
            tasks[key]
        } else {
            Log.d(TAG, "register() : total = $total, callback=$callback")
            val task = CountdownFeature(total, unit!!.toMillis(period), callback, this)
            task.future = AsyncTaskExecutor.getInstance()
                .scheduleAtFixedRate(task, period, period, unit)
            tasks.append(key, task)
            task
        }
    }

    fun unregister(callback: CountdownCallback): Boolean {
        val key = callback.hashCode()
        return if (tasks.indexOfKey(key) < 0) {
            Log.v(
                TAG,
                "unregister callback=$callback, it's already unregister by itself or it's not registered."
            )
            false
        } else {
            val cancel = tasks[key].future!!.cancel(true)
            Log.w(TAG, "unregister callback=$callback, cancel=$cancel")
            tasks.remove(key)
            true
        }
    }
}