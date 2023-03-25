package seker.framework.service.permission

/**
 * 这个接口的回调函数比较固定，应该不会变化了，所以设计成interface而不是abstract class
 *
 * @author xinwen
 */
interface PermissionCallback {
    /**
     * 请求权限后，结果的回调
     *
     * @param grantResult               either [android.content.pm.PackageManager.PERMISSION_GRANTED] or [android.content.pm.PackageManager.PERMISSION_DENIED].
     */
    fun onRequestPermissionsResult(grantResult: Int)
}