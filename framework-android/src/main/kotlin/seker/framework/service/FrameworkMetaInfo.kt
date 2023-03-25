package seker.framework.service

import seker.framework.android.MetaInfo
import seker.framework.android.desc.MicroServiceDesc
import seker.framework.service.countdown.CountdownService
import seker.framework.service.countdown.CountdownServiceImpl
import seker.framework.service.fps.FpsService
import seker.framework.service.fps.FpsServiceImpl
import seker.framework.service.permission.PermissionService
import seker.framework.service.permission.PermissionServiceImpl

/**
 * @author xinjian
 */
class FrameworkMetaInfo : MetaInfo() {

    init {
        // fps service
        addMicroServiceDescription(
            MicroServiceDesc(
                FpsService::class.java.name,
                FpsServiceImpl::class.java
            )
        )

        // countdown service
        addMicroServiceDescription(
            MicroServiceDesc(
                CountdownService::class.java.name,
                CountdownServiceImpl::class.java
            )
        )

        // permission service
        addMicroServiceDescription(
            MicroServiceDesc(
                PermissionService::class.java.name,
                PermissionServiceImpl::class.java
            )
        )
    }
}