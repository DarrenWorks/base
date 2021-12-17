package com.base.baselibrary.utilsKt

import android.content.Context
import android.content.Intent
import android.graphics.Bitmap
import android.media.MediaScannerConnection
import android.provider.MediaStore
import com.blankj.utilcode.constant.PermissionConstants
import com.blankj.utilcode.util.PathUtils
import com.blankj.utilcode.util.PermissionUtils
import com.bumptech.glide.Glide
import com.bumptech.glide.request.target.SimpleTarget
import com.bumptech.glide.request.transition.Transition
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.runBlocking
import kotlinx.coroutines.withContext
import java.io.File
import java.io.FileNotFoundException
import java.io.FileOutputStream
import kotlin.Unit


/**
 * Create by Darren
 * On 2020/3/30 20:27
 * 分享页图片下载管理器
 */
class DownloadImgManager(
    var context: Context,
    var downloadDone: (path: String) -> Unit = {},
    var imgName: String = "share"
) {
    var url: String? = null
        set(value) {
            field = value
            if (state == State.WaitUrl) {
                download()
            }
        }


    var state: State = State.Idle
        set(value) {
            field = value
            toastState()
        }


    /**
     * @return no need downloading if true
     */
    private fun updateDownloadState(): Boolean {
        return if (File(getFullPath()).exists()) {
            state = State.Downloaded
            true
        } else false
    }

    private fun download() {
        PermissionUtils.permission(PermissionConstants.STORAGE)
            .callback(object : PermissionUtils.SimpleCallback {
                override fun onGranted() {
                    when {
                        updateDownloadState() -> {
                        }
                        url == null -> {
                            state = State.WaitUrl
                        }
                        else -> {

                            showToastShort("开始下载")
                            state = State.Downloading
                            Glide.with(context)
                                .asBitmap()
                                .load(url)
                                .into(object : SimpleTarget<Bitmap>() {
                                    override fun onResourceReady(
                                        resource: Bitmap,
                                        transition: Transition<in Bitmap>?
                                    ) {
                                        runBlocking {
                                            withContext(Dispatchers.IO) {
                                                resource.compress(
                                                    Bitmap.CompressFormat.PNG,
                                                    100,
                                                    FileOutputStream(getFullPath())
                                                )
                                            }
                                            state = State.Downloaded
                                            downloadDone(getFullPath())
                                            updateMedia()
                                        }
                                    }
                                })
                        }
                    }
                }

                override fun onDenied() {
                    showToastShort("请开启权限后重试")
                }
            })
            .request()
    }

    fun updateMedia() {
        MediaScannerConnection.scanFile(context, arrayOf(getFullPath()), null) { _, uri ->
            try {
                MediaStore.Images.Media.insertImage(
                    context.contentResolver,
                    getFullPath(),
                    getName(),
                    null
                )
            } catch (e: FileNotFoundException) {
                e.printStackTrace()
            }
            val mediaScanIntent = Intent(Intent.ACTION_MEDIA_SCANNER_SCAN_FILE)
            mediaScanIntent.data = uri
            context.sendBroadcast(mediaScanIntent)
        }
    }

    private fun toastState() {
        showToastShort(
            when (state) {
                State.WaitUrl -> "开始下载"
                State.Downloading -> "开始下载"
                State.Downloaded -> "已下载"
                else -> ""
            }
        )
    }

    private fun getPath(): String {
        return """${PathUtils.getInternalAppCachePath()}/$prefix/"""
    }

    private fun getName(): String {
        return "${imgName}.png"
    }

    private fun getFullPath(): String {
        return getPath() + getName()
    }


    enum class State {
        Idle,//未下载
        WaitUrl,//等待下载地址
        Downloading,//下载中
        Downloaded//已下载
    }

    companion object {
        const val prefix = "base"
    }
}