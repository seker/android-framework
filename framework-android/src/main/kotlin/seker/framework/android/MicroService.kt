package seker.framework.android

import android.content.Context
import seker.framework.logger.Log
import seker.framework.android.desc.MicroServiceDesc

/**
 * @author xinjian
 */
abstract class MicroService {

    @JvmField
    protected val TAG: String

    init {
        val index = javaClass.simpleName.indexOf("Service")
        TAG = if (-1 == index)
            javaClass.simpleName
        else
            javaClass.simpleName.substring(0, index)
    }

    protected lateinit var context: Context

    protected lateinit var microServiceManager: MicroServiceManager

    fun create() {
        val start = System.currentTimeMillis()
        try {
            onCreate()
        } catch (e: Throwable) {
            Log.w(e)
        } finally {
            Log.d(javaClass.simpleName + ".onCreate() cost " + (System.currentTimeMillis() - start) + " ms.")
        }
    }

    /**
     * 初始化
     */
    protected abstract fun onCreate()

    companion object Factory {
        fun instance(
            context: Context,
            description: MicroServiceDesc,
            microServiceManager: MicroServiceManager
        ): MicroService? {
            var service: MicroService? = null
            try {
                service = description.clazz.newInstance()
                service.context = context
                service.microServiceManager = microServiceManager
            } catch (e: Throwable) {
                Log.w(e)
            }
            return service
        }
    }
}