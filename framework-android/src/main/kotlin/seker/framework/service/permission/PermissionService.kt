package seker.framework.service.permission

import android.app.Activity
import android.content.pm.PackageManager
import android.content.res.Configuration
import seker.framework.android.MicroService
import seker.framework.android.MicroServiceManager

/**
 * @author xinwen
 */
abstract class PermissionService : MicroService() {
    /**
     * 检测是否已经取得了所有的权限
     *
     * @param permissions               权限名
     * @return                          true/false
     */
    abstract fun checkPermissionsGranted(permissions: Array<String>): IntArray

    /**
     * 动态的请求权限
     *
     * @param permissions               权限名
     * @param callback                  PermissionCallback
     */
    @JvmOverloads
    open fun requestPermissions(permissions: Array<String>, callback: PermissionsCallback, activity: Activity? = null): Boolean {return false}

    /**
     * 检测overlay权限检查
     *
     * @return                          true/false
     */
    abstract fun checkOverlayPermissionGranted(): Int

    /**
     * 请求overlay权限
     *
     * @param callback                  PermissionCallback
     */
    @JvmOverloads
    open fun requestOverlayPermission(callback: PermissionCallback, activity: Activity? = null) {}

    companion object {

        fun checkSelfPermissions(permissions: Array<String>): IntArray {
            val serName: String = PermissionService::class.java.name
            val permissionService = MicroServiceManager.getService<PermissionService>(serName)
            return permissionService?.checkPermissionsGranted(permissions)
                ?: IntArray(permissions.size) {PackageManager.PERMISSION_DENIED}
        }

        fun granted(grantResults: IntArray) : Boolean {
            var granted = true
            for (i in grantResults.indices) {
                if (PackageManager.PERMISSION_DENIED == grantResults[i]) {
                    granted = false
                    break
                }
            }
            return granted
        }
    }
}