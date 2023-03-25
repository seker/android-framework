package seker.framework.service.permission

/**
 * 这个接口的回调函数比较固定，应该不会变化了，所以设计成interface而不是abstract class
 *
 * @author xinwen
 */
interface PermissionsCallback {
    /**
     * Callback for the result from requesting permissions. This method
     * is invoked for every call on [PermissionService.requestPermissions].
     *
     *
     * **Note:** It is possible that the permissions request interaction
     * with the user is interrupted. In this case you will receive empty permissions
     * and results arrays which should be treated as a cancellation.
     *
     *
     * @param permissions The requested permissions. Never null.
     * @param grantResults The grant results for the corresponding permissions
     * which is either [android.content.pm.PackageManager.PERMISSION_GRANTED]
     * or [android.content.pm.PackageManager.PERMISSION_DENIED]. Never null.
     *
     * @see PermissionService.requestPermissions
     */
    fun onRequestPermissionsResult(permissions: Array<String>, grantResults: IntArray)
}