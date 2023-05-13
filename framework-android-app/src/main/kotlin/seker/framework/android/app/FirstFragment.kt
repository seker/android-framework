package seker.framework.android.app

import android.Manifest
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import seker.framework.android.MicroServiceManager
import seker.framework.android.app.databinding.FragmentFirstBinding
import seker.framework.service.permission.PermissionService
import seker.framework.service.permission.PermissionsCallback

/**
 * A simple [Fragment] subclass as the default destination in the navigation.
 */
class FirstFragment : Fragment() {

    companion object {
        val PERMISSIONS = arrayOf(
            Manifest.permission.INTERNET,
            Manifest.permission.READ_EXTERNAL_STORAGE,
            Manifest.permission.WRITE_EXTERNAL_STORAGE
        )
    }

    private var _binding: FragmentFirstBinding? = null

    // This property is only valid between onCreateView and
    // onDestroyView.
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        _binding = FragmentFirstBinding.inflate(inflater, container, false)
        return binding.root

    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.buttonFirst.setOnClickListener {
//            findNavController().navigate(R.id.action_FirstFragment_to_SecondFragment)
            val permissionService = MicroServiceManager.getService<PermissionService>(PermissionService::class.java.name)
            permissionService!!.requestPermissions(PERMISSIONS, object : PermissionsCallback {
                override fun onRequestPermissionsResult(permissions: Array<String>, grantResults: IntArray) {
                    if (PermissionService.Companion.granted(grantResults)) {
                        Toast.makeText(requireContext(), "获取权限成功", Toast.LENGTH_SHORT).show();
                    } else {
                        Toast.makeText(requireContext(), "获取权限失败", Toast.LENGTH_SHORT).show();
                    }
                }
            }, requireActivity())
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}