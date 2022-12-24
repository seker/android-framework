package seker.framework.android.app.demo

import seker.framework.android.MetaInfo
import seker.framework.android.desc.MicroServiceDesc
import seker.framework.android.desc.MicroValveDesc

/**
 * @author xinjian
 */
class KotlinMetaInfo : MetaInfo() {

    init {
        addMicroServiceDescription(MicroServiceDesc(
            MyService::class.java.name
            , MyServiceImpl::class.java
            , isLazyInit = false
            , isAsyncInit = true
        ))

        addMicroValveDescription(MicroValveDesc(
            MyValve::class.java
        ))
    }
}