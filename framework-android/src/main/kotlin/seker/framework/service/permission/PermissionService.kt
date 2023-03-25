package seker.framework.service.permission

import android.content.pm.PackageManager
import seker.framework.android.MicroService

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
    abstract fun requestPermissions(permissions: Array<String>, callback: PermissionsCallback): Boolean

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
    abstract fun requestOverlayPermission(callback: PermissionCallback)

    companion object {
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