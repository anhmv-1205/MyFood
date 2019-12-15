package com.sun_asterisk.myfood.utils.extension

import android.Manifest
import android.app.Activity
import android.app.AlertDialog
import android.content.Intent
import android.net.Uri
import android.os.Handler
import android.provider.MediaStore
import android.provider.Settings
import android.view.View
import android.view.ViewGroup
import android.view.inputmethod.InputMethodManager
import android.widget.EditText
import androidx.annotation.IdRes
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.core.content.FileProvider
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentTransaction
import com.karumi.dexter.Dexter
import com.karumi.dexter.MultiplePermissionsReport
import com.karumi.dexter.PermissionToken
import com.karumi.dexter.listener.PermissionRequest
import com.karumi.dexter.listener.multi.MultiplePermissionsListener
import com.sun_asterisk.myfood.R
import com.sun_asterisk.myfood.data.model.Food
import com.sun_asterisk.myfood.utils.AnimateType
import com.sun_asterisk.myfood.utils.AnimateType.FADE
import com.sun_asterisk.myfood.utils.Constant
import com.sun_asterisk.myfood.utils.Constant.REQUEST_GALLERY_IMAGE
import com.sun_asterisk.myfood.utils.Constant.REQUEST_IMAGE_CAPTURE
import com.yalantis.ucrop.UCrop
import java.io.File

fun Fragment.replaceFragment(
    @IdRes containerId: Int, fragment: Fragment,
    addToBackStack: Boolean = false,
    tag: String = fragment::class.java.simpleName,
    animateType: AnimateType = FADE
) {
    fragmentManager?.transact({
        if (addToBackStack) {
            addToBackStack(tag)
        }
        replace(containerId, fragment, tag)
    }, animateType)
}

fun Fragment.addFragment(
    @IdRes containerId: Int,
    fragment: Fragment,
    addToBackStack: Boolean = false,
    tag: String = fragment::class.java.simpleName,
    animateType: AnimateType = FADE
) {
    fragmentManager?.transact({
        if (addToBackStack) {
            addToBackStack(tag)
        }
        add(containerId, fragment, tag)
    }, animateType)
}

fun Fragment.addChildFragment(
    @IdRes containerId: Int,
    fragment: Fragment,
    addToBackStack: Boolean = false,
    tag: String = fragment::class.java.simpleName,
    animateType: AnimateType? = FADE
) {
    childFragmentManager.transact({
        if (addToBackStack) {
            addToBackStack(tag)
        }
        add(containerId, fragment, tag)
    }, animateType)
}

fun Fragment.goBackFragment(): Boolean {
    fragmentManager.notNull {
        val isShowPreviousPage = it.backStackEntryCount > 0
        if (isShowPreviousPage) {
            it.popBackStackImmediate()
        }
        return isShowPreviousPage
    }
    return false
}

fun Fragment.comebackHomeFragment() {
    fragmentManager?.notNull { fm ->
        repeat((1..fm.backStackEntryCount).count()) { fm.popBackStack() }
    }
}

fun Fragment.showChildFragment(@IdRes containerId: Int, vararg hideFragments: Fragment, showFragment: Fragment) {
    val existFragment = childFragmentManager.findFragmentByTag(showFragment.tag)
    childFragmentManager.beginTransaction().apply {
        hideFragments.forEach { hide(it) }
        if (existFragment != null) show(existFragment)
        else addChildFragment(containerId, showFragment)
        commit()
    }
}

fun Fragment.addFragmentToActivity(
    @IdRes containerId: Int, fragment: Fragment,
    addToBackStack: Boolean = true,
    tag: String = fragment::class.java.simpleName
    , animateType: AnimateType? = FADE
) {
    (this.activity as AppCompatActivity).addFragmentToActivity(
        containerId,
        fragment,
        addToBackStack,
        tag,
        animateType
    )
}

inline fun FragmentManager.transact(
    action: FragmentTransaction.() -> Unit,
    animateType: AnimateType? = FADE
) {
    beginTransaction().apply {
        setCustomAnimations(this, animateType)
        action()
    }.commitAllowingStateLoss()
}

fun setCustomAnimations(
    transaction: FragmentTransaction,
    animateType: AnimateType? = FADE
) {
    when (animateType) {
        FADE -> transaction.setCustomAnimations(
            R.anim.fade_in, R.anim.fade_out,
            R.anim.fade_in,
            R.anim.fade_out
        )
        AnimateType.SLIDE_TO_RIGHT -> transaction.setCustomAnimations(
            R.anim.slide_in_right,
            R.anim.slide_out_left, R.anim.slide_in_left,
            R.anim.slide_out_right
        )
        AnimateType.SLIDE_TO_LEFT -> transaction.setCustomAnimations(
            R.anim.slide_in_left,
            R.anim.slide_out_right, R.anim.slide_in_right,
            R.anim.slide_out_left
        )
    }
}

fun setupDismissKeyBoard(context: Activity?, view: View?) {
    // Set up touch listener for non-text box views to hide keyboard.
    if (view !is EditText) {
        view?.setOnTouchListener { _, _ ->
            val inputMethodManager =
                context?.getSystemService(Activity.INPUT_METHOD_SERVICE) as InputMethodManager
            inputMethodManager.hideSoftInputFromWindow(context.currentFocus?.windowToken, 0)

            false
        }
    }

    //If a layout container, iterate over children and seed recursion.
    if (view is ViewGroup) {
        for (i in 0 until view.childCount) {
            val innerView = view.getChildAt(i)
            setupDismissKeyBoard(context, innerView)
        }
    }
}

fun delayTask(func: () -> Unit, duration: Long = 1000) {
    Handler().postDelayed(func, duration)
}

fun Fragment.cropImage(sourceUri: Uri, isBitmapMaxSize: Boolean) {
    val destinationUri = Uri.fromFile(File(activity?.cacheDir, queryName(context!!.contentResolver, sourceUri)))
    val options = UCrop.Options().apply {
        setCompressionQuality(Constant.IMAGE_COMPRESSION_QUALITY)
        setToolbarWidgetColor(ContextCompat.getColor(context!!, R.color.colorWhite))
        setToolbarColor(ContextCompat.getColor(context!!, R.color.colorAccent))
        setStatusBarColor(ContextCompat.getColor(context!!, R.color.colorAccent))
        setActiveWidgetColor(ContextCompat.getColor(context!!, R.color.colorAccent))
        withAspectRatio(Constant.ASPECT_RATIO_X, Constant.ASPECT_RATIO_Y)
        if (isBitmapMaxSize) withMaxResultSize(Constant.BITMAP_MAX_WIDTH, Constant.BITMAP_MAX_HEIGHT)
    }
    UCrop.of(sourceUri, destinationUri).withOptions(options)
        .start(activity as AppCompatActivity, this, UCrop.REQUEST_CROP)
}

fun Fragment.clearCache() {
    val path = File(context!!.externalCacheDir, Constant.CHILD_CAMERA)
    if (path.exists() && path.isDirectory) for (file in path.listFiles()) file.delete()
}

fun Fragment.getCacheImagePath(fileName: String): Uri {
    val path = File(activity?.externalCacheDir, Constant.CHILD_CAMERA)
    if (!path.exists()) path.mkdir()
    return FileProvider.getUriForFile(context!!, "${activity?.packageName}.provider", File(path, fileName))
}

fun Fragment.takeCameraImage(fileName: String) {
    clearCache()
    Dexter.withActivity(activity)
        .withPermissions(Manifest.permission.CAMERA, Manifest.permission.WRITE_EXTERNAL_STORAGE)
        .withListener(object : MultiplePermissionsListener {
            override fun onPermissionsChecked(report: MultiplePermissionsReport?) {
                report?.let {
                    if (it.areAllPermissionsGranted()) {
                        val takePictureIntent = Intent(MediaStore.ACTION_IMAGE_CAPTURE).apply {
                            putExtra(MediaStore.EXTRA_OUTPUT, getCacheImagePath(fileName))
                        }
                        if (takePictureIntent.resolveActivity(activity!!.packageManager) != null)
                            startActivityForResult(takePictureIntent, REQUEST_IMAGE_CAPTURE)
                    }
                }
            }

            override fun onPermissionRationaleShouldBeShown(
                permissions: MutableList<PermissionRequest>?,
                token: PermissionToken?
            ) {
                token?.continuePermissionRequest()
            }
        }).check()
}

fun Fragment.chooseImageFromGallery() {
    Dexter.withActivity(activity)
        .withPermissions(Manifest.permission.READ_EXTERNAL_STORAGE)
        .withListener(object : MultiplePermissionsListener {
            override fun onPermissionsChecked(report: MultiplePermissionsReport?) {
                report?.let {
                    if (it.areAllPermissionsGranted()) {
                        val pickPhoto = Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI)
                        startActivityForResult(pickPhoto, Constant.REQUEST_GALLERY_IMAGE)
                    }
                }
            }

            override fun onPermissionRationaleShouldBeShown(
                permissions: MutableList<PermissionRequest>?,
                token: PermissionToken?
            ) {
                token?.continuePermissionRequest()
            }
        }).check()
}

fun Fragment.openSettings() {
    val intent = Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS).apply {
        data = Uri.fromParts(Constant.SCHEME_PACKAGE, context?.packageName, null)
    }
    startActivityForResult(intent, Constant.REQUEST_SETTING)
}

fun Fragment.showSettingsDialog() {
    AlertDialog.Builder(context).apply {
        setTitle(getString(R.string.dialog_permission_title))
        setMessage(getString(R.string.dialog_permission_message))
        setPositiveButton(getString(R.string.go_to_settings)) { dialog, _ ->
            dialog.cancel()
            openSettings()
        }
        setNegativeButton(getString(R.string.text_cancel)) { dialog, _ -> dialog.cancel() }
    }.show()
}

fun Fragment.showImagePickerOptions(fileName: String) {
    AlertDialog.Builder(context).apply {
        setTitle(context.getString(R.string.text_image_options))
        val options = arrayOf(
            context.getString(R.string.text_take_camera_picture),
            context.getString(R.string.text_choose_from_gallery)
        )
        setItems(options) { _, which ->
            when (which) {
                REQUEST_IMAGE_CAPTURE -> takeCameraImage(fileName)
                REQUEST_GALLERY_IMAGE -> chooseImageFromGallery()
            }
        }
    }.create().show()
}

fun Fragment.checkCameraAndGalleryPermission(fileName: String) {
    Dexter.withActivity(activity)
        .withPermissions(Manifest.permission.CAMERA, Manifest.permission.WRITE_EXTERNAL_STORAGE)
        .withListener(object : MultiplePermissionsListener {
            override fun onPermissionsChecked(report: MultiplePermissionsReport?) {
                report?.let {
                    if (it.areAllPermissionsGranted()) showImagePickerOptions(fileName)
                    if (it.isAnyPermissionPermanentlyDenied) showSettingsDialog()
                }
            }

            override fun onPermissionRationaleShouldBeShown(
                permissions: MutableList<PermissionRequest>?,
                token: PermissionToken?
            ) {
                token?.continuePermissionRequest()
            }
        }).check()
}
