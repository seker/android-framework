package seker.framework.service.permission

import android.content.Intent
import android.os.Build
import android.content.pm.PackageManager
import android.app.AppOpsManager
import android.content.Context
import android.view.WindowManager
import android.graphics.PixelFormat
import android.os.Binder
import android.provider.Settings
import seker.framework.logger.Log
import android.view.View
import androidx.core.content.ContextCompat
import java.util.ArrayList

/**
 * @author xinwen
 */
class PermissionServiceImpl : PermissionService() {
    /**
     * permissions 权限回调
     */
    private var permissionsCallback: PermissionsCallback? = null

    /**
     * overlay callbacks
     */
    private val overlayCallbacks: ArrayList<PermissionCallback> = ArrayList()

    override fun onCreate() {}

    override fun checkPermissionsGranted(permissions: Array<String>): IntArray {
        val grantResults = IntArray(permissions.size) {PackageManager.PERMISSION_DENIED}
        for (i in permissions.indices) {
            grantResults[i] = ContextCompat.checkSelfPermission(context, permissions[i])
        }
        val string = permissions.joinToString(",", "[", "]")
        val string2 = grantResults.joinToString(",", "[", "]")
        Log.i(TAG, "checkAllPermissionsGranted() : permissions=$string, granted=$string2")
        return grantResults
    }

    override fun requestPermissions(permissions: Array<String>, callback: PermissionsCallback): Boolean {
        synchronized(this) {
            return if (null == permissionsCallback) {
                permissionsCallback = callback
                val intent = Intent(context, PermissionActivity::class.java)
                intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK
                intent.putExtra(PermissionActivity.KEY_PERMISSION_TYPE, PermissionActivity.PERMISSION_TYPE_PERMISSIONS)
                intent.putExtra(PermissionActivity.KEY_PERMISSIONS, permissions)
                context.startActivity(intent)
                true
            } else {
                Log.w(TAG, "permissionsCallback=$permissionsCallback, not null: return false")
                false
            }
        }
    }

    internal fun permissionsResult(permissions: Array<String>, grantResults: IntArray) {
        synchronized(this) {
            try {
                permissionsCallback!!.onRequestPermissionsResult(permissions, grantResults)
            } catch (e: Throwable) {
                Log.w(TAG, e)
            } finally {
                permissionsCallback = null
            }
        }
    }

    override fun checkOverlayPermissionGranted(): Int {
        // https://stackoverflow.com/questions/46173460/why-in-android-8-method-settings-candrawoverlays-returns-false-when-user-has
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            if (Settings.canDrawOverlays(context.applicationContext)) {
                return PackageManager.PERMISSION_GRANTED
            }
            val manager: AppOpsManager? = context.getSystemService(Context.APP_OPS_SERVICE) as AppOpsManager?
            if (manager == null) {
                Log.w(TAG, "AppOpsManager manager == null.")
            } else {
                try {
                    val result: Int = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
                        manager.unsafeCheckOp(
                            AppOpsManager.OPSTR_SYSTEM_ALERT_WINDOW,
                            Binder.getCallingUid(),
                            context.packageName
                        )
                    } else {
                        manager.checkOp(
                            AppOpsManager.OPSTR_SYSTEM_ALERT_WINDOW,
                            Binder.getCallingUid(),
                            context.packageName
                        )
                    }
                    return if (result == AppOpsManager.MODE_ALLOWED) PackageManager.PERMISSION_GRANTED else PackageManager.PERMISSION_DENIED
                } catch (e: Throwable) {
                    Log.w(TAG, "AppOpsManager.checkOp() failed : " + e.message)
                }
            }
        }

        //IF This Fails, we definitely can't do it
        val mgr: WindowManager = context.getSystemService(Context.WINDOW_SERVICE) as WindowManager
        try {
            val viewToAdd = View(context)
            val params: WindowManager.LayoutParams = WindowManager.LayoutParams(
                0,
                0,
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O)
                    WindowManager.LayoutParams.TYPE_APPLICATION_OVERLAY
                else WindowManager.LayoutParams.TYPE_SYSTEM_ALERT,
                WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE or WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE,
                PixelFormat.TRANSPARENT
            )
            viewToAdd.layoutParams = params
            mgr.addView(viewToAdd, params)
            mgr.removeView(viewToAdd)
            return PackageManager.PERMISSION_GRANTED
        } catch (e: Throwable) {
            Log.w(TAG, "WindowManager.addView() failed : " + e.message)
            return PackageManager.PERMISSION_DENIED
        }
    }

    override fun requestOverlayPermission(callback: PermissionCallback) {
        synchronized(overlayCallbacks) {
            overlayCallbacks.add(callback)
            if (overlayCallbacks.size == 1) {
                val intent = Intent(context, PermissionActivity::class.java)
                intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK
                intent.putExtra(
                    PermissionActivity.KEY_PERMISSION_TYPE,
                    PermissionActivity.PERMISSION_TYPE_OVERLAY
                )
                context.startActivity(intent)
            } else {
                Log.i(TAG, "overlay permission is requesting...")
            }
        }
    }

    internal fun overlayPermissionResult() {
        val grantResult: Int = checkOverlayPermissionGranted()
        synchronized(overlayCallbacks) {
            overlayCallbacks.forEach {overlayCallback->
                try {
                    overlayCallback.onRequestPermissionsResult(grantResult)
                } catch (e: Throwable) {
                    Log.w(TAG, e)
                }
            }
            overlayCallbacks.clear()
        }
    }
}