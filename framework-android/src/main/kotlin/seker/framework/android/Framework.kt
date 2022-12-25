package seker.framework.android

import android.content.Context
import android.content.pm.PackageManager
import android.os.Build
import android.text.TextUtils
import seker.framework.logger.Log
import seker.framework.android.desc.MicroServiceDesc
import seker.framework.android.desc.MicroValveDesc
import seker.asynctask.TimeoutTaskQueue
import java.util.ArrayList
import java.util.HashMap
import java.util.concurrent.atomic.AtomicBoolean

/**
 *
 * 负责MetaInfo的解析，以及管理下列的：
 *
 * 1、MicroServiceManager
 * 2、冷启动的TaskQueue(valve)
 * 3、AspectManager
 *
 * @author xinjian
 */
object Framework {

    const val COOL_START_TASK_QUEUE = "cool_start"

    private val init = AtomicBoolean(false)

    fun init(context: Context) {
        val start = System.currentTimeMillis()
        if (init.get()) {
            Log.w(java.lang.RuntimeException("Framework re-init."))
            return
        }

        synchronized(Framework::class.java) {
            if (init.get()) {
                Log.w(java.lang.RuntimeException("Framework already init."))
                return
            }

            Log.d("Framework.init() begin.")
            val serviceDescriptions = HashMap<String, MicroServiceDesc>(6)
            val valveDescriptions: MutableList<MicroValveDesc> = ArrayList()
            parseMetaInfo(context, serviceDescriptions, valveDescriptions)

            // 创建MicroServiceManager实例
            MicroServiceManager.createInstance(context, serviceDescriptions)

            // start冷启动的TaskQueue
            val frameworkTaskQueue = TimeoutTaskQueue(COOL_START_TASK_QUEUE, true)
            for (description in valveDescriptions) {
                val valve = MicroValve.instance(context, description)
                if (null == valve) {
                    Log.w("Failed to instance MicroValve : $description")
                } else {
                    frameworkTaskQueue.addTask(valve, valve.javaClass.simpleName, description.priority)
                }
            }
            frameworkTaskQueue.start()
            init.set(true)
            Log.d("Framework.init() end. Cost ${System.currentTimeMillis() - start} ms.")
        }
    }


    private fun parseMetaInfo(
        context: Context,
        serviceDescriptions: HashMap<String, MicroServiceDesc>,
        valveDescriptions: MutableList<MicroValveDesc>
    ) {
        val packageManager = context.packageManager
        val packageName = context.packageName

        val metaData = try {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
                packageManager.getApplicationInfo(packageName,
                    PackageManager.ApplicationInfoFlags.of(PackageManager.GET_META_DATA.toLong()))
            } else {
                packageManager.getApplicationInfo(packageName, PackageManager.GET_META_DATA)
            }.metaData
        } catch (e: Throwable) {
            Log.w(e)
            null
        }

        if (null == metaData) {
            Log.w("null == appInfo.metaData")
        } else {
            val keys = metaData.keySet()
            if (null == keys) {
                Log.w("appInfo.metaData.keys == null")
            } else if (keys.isEmpty()) {
                Log.w("appInfo.metaData.keys is empty.")
            } else {
                keys.forEach { key->
                    if (TextUtils.isEmpty(key)) {
                        Log.i("invalid meta-data : key=$key")
                    } else if (key.startsWith("metaInfo:", false)) {
                        val className = metaData.getString(key)
                        if (TextUtils.isEmpty(className)) {
                            Log.w("invalid meta-data: key=$key, value=[$className]")
                        } else {
                            val metaInfo = MetaInfo.instance(className!!)
                            if (null == metaInfo) {
                                Log.e("failed to instance MetaInfo: class name = $className")
                            } else {
                                if (null == metaInfo.serviceDescriptions) {
                                    Log.v("$className : [$className].serviceDescriptions == null")
                                } else {
                                    serviceDescriptions.putAll(metaInfo.serviceDescriptions!!)
                                }

                                if (null == metaInfo.valveDescriptions) {
                                    Log.v("$className : [$className].valveDescriptions == null")
                                } else {
                                    valveDescriptions.addAll(metaInfo.valveDescriptions!!)
                                }
                            }
                        }
                    } else {
                        Log.v("ignore meta-data: key=[$key], not for Framework.")
                    }
                }
            }
        }
    }
}