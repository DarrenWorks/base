package com.base.baselibrary.utils

import android.content.Context
import android.content.Intent
import android.graphics.Bitmap
import android.media.MediaScannerConnection
import android.provider.MediaStore
import com.base.baselibrary.utilsKt.showToastShort
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
        var mContext: Context,
        var mDownloadDone: (path: String) -> Unit = {},
        var imgName: String = "share"
) {
    var mUrl: String? = null
        set

    private fun setUrl(url: String) {
        mUrl = url
        if (mState == State.WaitUrl) {
            download()
        }
    }

    var mState: State = State.Idle
        set(value) {
            field = value
            toastState()
        }


    /**
     * @return no need downloading if true
     */
    private fun updateDownloadState(): Boolean {
        return if (File(getPath()).exists()) {
            mState = State.Downloaded
            true
        } else false
    }

    fun download() {
        PermissionUtils.permission(PermissionConstants.STORAGE)
                .callback(object :PermissionUtils.SimpleCallback{
                    override fun onGranted() {
                        when {
                            updateDownloadState() -> {
                            }
                            mUrl == null -> {
                                mState = State.WaitUrl
                            }
                            else -> {
                                val file = File(PathUtils.getExternalDownloadsPath() + "/gz")
                                if (!file.exists()) {
                                    if (!file.mkdir()) {
                                        showToastShort("访问文件失败")
                                        return
                                    }
                                }
                                showToastShort("开始下载")
                                mState = State.Downloading
                                Glide.with(mContext)
                                        .asBitmap()
                                        .load(mUrl)
                                        .into(object : SimpleTarget<Bitmap>() {
                                            override fun onResourceReady(resource: Bitmap, transition: Transition<in Bitmap>?) {
                                                runBlocking {
                                                    withContext(Dispatchers.IO) {
                                                        resource.compress(Bitmap.CompressFormat.PNG, 100, FileOutputStream(getPath()))
                                                    }
                                                    mState = State.Downloaded
                                                    mDownloadDone(getPath())
                                                    updateMedia(mContext, PathUtils.getExternalDownloadsPath() + "/gz/", "$imgName.png")
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

    fun updateMedia(context: Context, path: String, name: String) {
        MediaScannerConnection.scanFile(context, arrayOf(path + name), null) { _, uri ->
            try {
                MediaStore.Images.Media.insertImage(context.contentResolver, "$path$name", name, null)
            } catch (e: FileNotFoundException) {
                e.printStackTrace()
            }
            val mediaScanIntent = Intent(Intent.ACTION_MEDIA_SCANNER_SCAN_FILE)
            mediaScanIntent.data = uri
            context.sendBroadcast(mediaScanIntent)
        }
    }

    private fun toastState() {
        showToastShort(when (mState) {
            State.WaitUrl -> "开始下载"
            State.Downloading -> "开始下载"
            State.Downloaded -> "已下载"
            else -> ""
        })
    }

    private fun getPath(): String {
        return PathUtils.getExternalDownloadsPath() + "/gz/" + imgName + ".png"
    }


    enum class State {
        Idle,//未下载
        WaitUrl,//等待下载地址
        Downloading,//下载中
        Downloaded//已下载
    }
}