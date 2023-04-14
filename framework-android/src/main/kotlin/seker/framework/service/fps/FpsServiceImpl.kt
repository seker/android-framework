package seker.framework.service.fps

import seker.framework.logger.Log
import android.util.SparseArray
import seker.asynctask.AsyncTaskExecutor
import java.util.concurrent.TimeUnit

/**
 * @author xinwen
 * @since 2021.04.21
 */
class FpsServiceImpl : FpsService() {
    private val tasks = SparseArray<FpsFeature>()
    override fun onCreate() {}
    override fun register(name: String, callback: FpsCallback): FpsFeature {
        val key = callback.hashCode()
        return if (tasks.indexOfKey(key) >= 0) {
            Log.w(TAG, "register() already exist : name = $name, callback=$callback")
            tasks[key]
        } else {
            Log.d(TAG, "register() : name = $name, callback=$callback")
            val task = FpsFeature(name, callback)
            task.reset()
            task.future = AsyncTaskExecutor.getInstance()
                .scheduleAtFixedRate(task, 1, 1, TimeUnit.SECONDS)
            tasks.append(key, task)
            task
        }
    }

    override fun unregister(callback: FpsCallback): Boolean {
        val key = callback.hashCode()
        return if (tasks.indexOfKey(key) < 0) {
            false
        } else {
            val cancel = tasks[key].future!!.cancel(true)
            Log.w(TAG, "unregister callback=$callback, cancel=$cancel")
            tasks.remove(key)
            true
        }
    }
}