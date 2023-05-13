package seker.framework.service.permission

import android.app.Activity
import android.os.Bundle
import android.content.Intent
import android.content.pm.ActivityInfo
import android.content.pm.PackageManager
import android.content.res.Configuration
import android.os.Build
import seker.framework.android.MicroServiceManager
import seker.framework.android.R
import android.os.Looper
import android.graphics.Color
import android.net.Uri
import android.os.Handler
import android.provider.Settings
import seker.framework.logger.Log
import android.view.View
import androidx.core.app.ActivityCompat

/**
 * @author xinwen
 */
class PermissionActivity : Activity() {
    private var permissionType = 0
    private lateinit var permissions: Array<String>

    override fun onCreate(savedInstanceState: Bundle?) {

        intent.getBooleanExtra(KEY_WINDOW_FULLSCREEN, false).also { fullScreen ->
            Log.d(TAG, "fullScreen=$fullScreen")
            val theme = if (fullScreen) R.style.TransparentFullscreenTheme else R.style.TransparentTheme
            setTheme(theme)
        }

        super.onCreate(savedInstanceState)
        intent.getIntExtra(KEY_ORIENTATION, Configuration.ORIENTATION_PORTRAIT).also { orientation ->
            requestedOrientation = when(orientation) {
                Configuration.ORIENTATION_UNDEFINED -> ActivityInfo.SCREEN_ORIENTATION_UNSPECIFIED
                Configuration.ORIENTATION_PORTRAIT -> ActivityInfo.SCREEN_ORIENTATION_PORTRAIT
                Configuration.ORIENTATION_LANDSCAPE -> ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE
                else -> ActivityInfo.SCREEN_ORIENTATION_PORTRAIT
            }
        }

        permissionType = intent.getIntExtra(KEY_PERMISSION_TYPE, PERMISSION_TYPE_PERMISSIONS)
        if (PERMISSION_TYPE_PERMISSIONS == permissionType) {
            permissions = intent.getStringArrayExtra(KEY_PERMISSIONS)!!
        }
        val view = View(this)
        view.setBackgroundColor(Color.RED)
        setContentView(view)
        view.layoutParams.width = 3
        view.layoutParams.height = 3

        when (permissionType) {
            PERMISSION_TYPE_PERMISSIONS -> {
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                    ActivityCompat.requestPermissions(this, permissions, PERMISSION_TYPE_PERMISSIONS)
                } else {
                    val grantResults = IntArray(permissions.size) { PackageManager.PERMISSION_GRANTED }
                    onRequestPermissionsResult(PERMISSION_TYPE_PERMISSIONS, permissions, grantResults)
                }
            }
            PERMISSION_TYPE_OVERLAY -> {
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                    val intentOverlay = Intent()
                    intentOverlay.action = Settings.ACTION_MANAGE_OVERLAY_PERMISSION
                    intentOverlay.data = Uri.parse("package:$packageName")
                    startActivityForResult(intentOverlay, PERMISSION_TYPE_OVERLAY)
                } else {
                    onActivityResult(PERMISSION_TYPE_OVERLAY, RESULT_OK, null)
                }
            }
            else -> {}
        }
    }

    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<String>, grantResults: IntArray) {
        val string = permissions.joinToString(",", "[", "]")
        val string2 = grantResults.joinToString(",", "[", "]")
        Log.i(TAG, "PermissionActivity.onRequestPermissionsResult($requestCode) : permissions=$string, granted=$string2")
        if (PERMISSION_TYPE_PERMISSIONS == requestCode) {
            val serName = PermissionService::class.java.name
            val permissionService = MicroServiceManager.getService<PermissionServiceImpl>(serName)
            permissionService?.permissionsResult(permissions, grantResults)
        } else {
            super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        }
        finish()
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        if (PERMISSION_TYPE_OVERLAY == requestCode) {
            Handler(Looper.getMainLooper()).postDelayed({
                val serName = PermissionService::class.java.name
                val permissionService = MicroServiceManager.getService<PermissionServiceImpl>(serName)
                permissionService?.overlayPermissionResult()
            }, 500L)
        } else {
            super.onActivityResult(requestCode, resultCode, data)
        }
        finish()
    }

    override fun finish() {
        super.finish()
        overridePendingTransition(0, 0)
    }

    companion object {
        private const val TAG = "Permission"

        const val PERMISSION_TYPE_PERMISSIONS = 1024
        const val PERMISSION_TYPE_OVERLAY = 1025

        const val KEY_PERMISSION_TYPE = "permission_type"
        const val KEY_PERMISSIONS = "permissions"

        const val KEY_ORIENTATION = "orientation"

        const val KEY_WINDOW_FULLSCREEN = "windowFullscreen"
//        const val KEY_WINDOW_NO_TITLE = "windowNoTitle"
//        const val KEY_WINDOW_ACTION_BAR = "windowActionBar"
    }
}