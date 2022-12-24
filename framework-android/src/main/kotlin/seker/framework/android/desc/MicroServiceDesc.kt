package seker.framework.android.desc

import seker.framework.android.MicroService

/**
 * @author xinjian
 */
data class MicroServiceDesc(
    /**
     * service name
     */
    val serviceName: String,
    /**
     * class
     */
    val clazz: Class<out MicroService>,
    /**
     * lazy init
     */
    val isLazyInit: Boolean = true,
    /**
     * async init
     */
    val isAsyncInit: Boolean = true
) {

    override fun toString(): String {
        return "MicroServiceDescription{" +
                "serviceName='" + serviceName + '\'' +
                ", clazz=" + clazz +
                ", lazyInit=" + isLazyInit +
                ", asyncInit=" + isAsyncInit +
                '}'
    }
}