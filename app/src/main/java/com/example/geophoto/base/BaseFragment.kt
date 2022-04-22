@file:Suppress("MemberVisibilityCanBePrivate")

package com.example.geophoto.base

import android.app.Activity
import android.app.Dialog
import android.content.Context
import android.view.View
import android.widget.Toast
import androidx.activity.addCallback
import androidx.annotation.IdRes
import androidx.annotation.LayoutRes
import androidx.annotation.StringRes
import androidx.fragment.app.Fragment
import androidx.navigation.NavDirections
import androidx.navigation.NavOptions
import androidx.navigation.fragment.findNavController
import com.afollestad.materialdialogs.MaterialDialog
import com.example.geophoto.util.view.dialogBuilder
import com.example.geophoto.util.view.snackbar
import com.example.geophotoapp.util.permission.checkPermission
import com.example.geophotoapp.util.view.*

abstract class BaseFragment(@LayoutRes layoutId: Int) : Fragment(layoutId) {

    private var loadingDialog: Dialog? = null

    val appTag = "geo_photo"

    override fun onPause() {
        super.onPause()
        hideKeyboard()
    }

    /* Messages */
    fun toast(@StringRes messageStringRes: Int) {
        requireContext().toast(messageStringRes)
    }

    fun simpleToast(context: Context, str: String) {
        Toast.makeText(context, str, Toast.LENGTH_SHORT).show()
    }

    fun snackbar(view: View, @StringRes messageStringRes: Int) {
        requireContext().snackbar(view, messageStringRes)
    }

    fun dialog(@StringRes titleStringRes: Int, @StringRes messageStringRes: Int) {
        dialogBuilder(titleStringRes)
            .message(messageStringRes)
            .show()
    }

    /* Dialogs */
    fun dialogBuilder(title: Int): MaterialDialog {
        return requireContext().dialogBuilder(viewLifecycleOwner, title)
    }

    /* Navigation */
    fun navigate(@IdRes resId: Int) {
        findNavController().navigate(resId)
    }

    fun navigate(direction: NavDirections) {
        findNavController().navigate(direction)
    }

    fun navigate(direction: NavDirections, navOptions: NavOptions) {
        findNavController().navigate(direction, navOptions)
    }

    fun navigateBack() {
        findNavController().popBackStack()
    }

    /* Permission */
    fun checkPermission(permission: String) = requireContext().checkPermission(permission)

    /* Other */
    fun hideKeyboard() {
        view?.let { activity?.hideKeyboard(it) }
    }

    fun backPressedDisable(str: String) {
        requireActivity().onBackPressedDispatcher.addCallback(viewLifecycleOwner) {
            Toast.makeText(activity, str, Toast.LENGTH_SHORT).show()
        }
    }

    fun backPressedCloseApp(activity: Activity) {
        requireActivity().onBackPressedDispatcher.addCallback(viewLifecycleOwner) {
            activity.finish()
        }
    }

    fun backPressed(str: String, resId: Int) {
        var backPressedTime: Long = 0
        requireActivity().onBackPressedDispatcher.addCallback(viewLifecycleOwner) {

            val backToast: Toast = Toast.makeText(activity, str, Toast.LENGTH_SHORT)
            if (backPressedTime + 2000 > System.currentTimeMillis()) {
                backToast.cancel()
                navigate(resId)
            } else {
                backToast.show()
            }
            backPressedTime = System.currentTimeMillis()
        }
    }

    fun backPressedWithArgsNavigate(direction: NavDirections){
        requireActivity().onBackPressedDispatcher.addCallback(viewLifecycleOwner) {
            navigate(direction)
        }
    }

    fun backPressedNavigate(@IdRes resId: Int){
        requireActivity().onBackPressedDispatcher.addCallback(viewLifecycleOwner) {
            navigate(resId)
        }
    }

}
