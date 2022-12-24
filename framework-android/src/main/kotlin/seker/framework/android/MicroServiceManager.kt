package seker.framework.android

import android.content.Context
import seker.framework.logger.Log
import seker.framework.android.desc.MicroServiceDesc
import seker.asynctask.AsyncTaskExecutor
import java.lang.ref.WeakReference

/**
 * @author xinjian
 */
object MicroServiceManager {

    private lateinit var contextRef: WeakReference<Context>

    private val microServiceDescriptions: MutableMap<String, MicroServiceDesc> = HashMap()

    private val microServices: MutableMap<String, MicroService> = HashMap()

    internal fun createInstance(ctx: Context, descriptions: Map<String, MicroServiceDesc>) {
        contextRef = WeakReference(ctx)
        microServiceDescriptions.putAll(descriptions)
        if (microServiceDescriptions.isEmpty()) {
            Log.w("microServiceDescriptions is empty.")
        } else {
            synchronized(MicroServiceManager::class.java) {
                val iterator = microServiceDescriptions.entries.iterator()
                while (iterator.hasNext()) {
                    val entry = iterator.next()
                    val key = entry.key
                    val description = entry.value
                    if (description.isLazyInit) {
                        Log.d(description.serviceName + " is lazy service, ignore.")
                    } else {
                        iterator.remove()
                        val microService = MicroService.instance(ctx, description, this)
                        if (null == microService) {
                            Log.w("Failed to instance MicroService : $description")
                        } else {
                            if (description.isAsyncInit) {
                                AsyncTaskExecutor.getInstance().execute({
                                    microService.create()
                                }, microService.javaClass.simpleName)
                            } else {
                                microService.create()
                            }
                            microServices[key] = microService
                        }
                    }
                }
            }
        }
    }

    fun <T : MicroService?> getService(serName: String): T? {
        var microService = microServices[serName]
        if (null == microService) {
            // 这里的锁是为了确保sMicroServiceManager.createInstance()已经执行完毕了。
            synchronized(MicroServiceManager::class.java) {
                microService = microServices[serName]       // double lock
                if (null == microService) {
                    Log.v("getMicroService() : null == microService: serName=$serName, try to create it.")
                    val description = microServiceDescriptions.remove(serName)
                    if (null == description) {
                        Log.e("failed to get MicroServiceDescription by service name: $serName")
                    } else {
                        microService = MicroService.instance(contextRef.get()!!, description, this)
                        if (null == microService) {
                            Log.w("Failed to instance MicroService : $description")
                        } else {
                            microService!!.apply {
                                create()
                                microServices[serName] = this
                            }
                        }
                    }
                }
            }
        }

        var t: T? = null
        if (null != microService) {
            try {
                t = microService as T
            } catch (e: Throwable) {
                Log.w(e)
            }
        }
        return t
    }
}