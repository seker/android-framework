package seker.framework.android

import seker.framework.logger.Log
import seker.framework.android.desc.MicroServiceDesc
import seker.framework.android.desc.MicroValveDesc
import java.util.ArrayList
import java.util.HashMap

/**
 * @author xinjian
 */
open class MetaInfo {

    var serviceDescriptions: MutableMap<String, MicroServiceDesc>? = null

    var valveDescriptions: MutableList<MicroValveDesc>? = null

    protected fun addMicroServiceDescription(description: MicroServiceDesc) {
        if (null == serviceDescriptions) {
            serviceDescriptions = HashMap(6)
        }
        serviceDescriptions!![description.serviceName] = description
    }

    protected fun addMicroValveDescription(description: MicroValveDesc) {
        if (null == valveDescriptions) {
            valveDescriptions = ArrayList()
        }
        valveDescriptions!!.add(description)
    }

    companion object Factory {
        fun instance(className: String): MetaInfo? {
            var metaInfo: MetaInfo? = null
            try {
                val clazz = Class.forName(className)
                metaInfo = clazz.newInstance() as MetaInfo
            } catch (e: Throwable) {
                Log.w(e)
            }
            return metaInfo
        }
    }
}