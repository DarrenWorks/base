package com.base.baselibrary.utilsKt

import android.app.Activity
import android.text.TextUtils
import com.base.baselibrary.utils.GlideEngine
import com.blankj.utilcode.constant.PermissionConstants
import com.blankj.utilcode.util.PermissionUtils
import com.luck.picture.lib.PictureSelector
import com.luck.picture.lib.config.PictureMimeType
import com.luck.picture.lib.entity.LocalMedia
import com.luck.picture.lib.listener.OnResultCallbackListener

/**
Create by Darren
On 2020/5/22 17:13
图片选择工具类
 **/

fun selectPic(activity: Activity, maxCount: Int = 1, onSelected : (MutableList<LocalMedia>) -> kotlin.Unit
) {
    PictureSelector.create(activity)
        .openGallery(PictureMimeType.ofImage())
        .loadImageEngine(GlideEngine.createGlideEngine())
        .maxSelectNum(maxCount)
        .forResult(object : OnResultCallbackListener<LocalMedia> {
            override fun onResult(result: MutableList<LocalMedia>) {
                onSelected(result)
            }

            override fun onCancel() {
            }
        })


}
fun selectCutPic(activity: Activity, maxCount: Int = 1, onSelected : (MutableList<LocalMedia>) -> kotlin.Unit) {
    PermissionUtils.permission(PermissionConstants.STORAGE, PermissionConstants.CAMERA)
        .callback(object : PermissionUtils.SimpleCallback{
            override fun onGranted() {
                PictureSelector.create(activity)
                    .openGallery(PictureMimeType.ofImage())
                    .loadImageEngine(GlideEngine.createGlideEngine())
                    .maxSelectNum(maxCount)
                    .enableCrop(true)
                    .withAspectRatio(1,1)
                    .forResult(object : OnResultCallbackListener<LocalMedia> {
                        override fun onResult(result: MutableList<LocalMedia>) {
                            onSelected(result)
                        }

                        override fun onCancel() {
                        }
                    })
            }

            override fun onDenied() {
            }
        })
        .request()


}

fun LocalMedia.availablePath()  =
    if (TextUtils.isEmpty(androidQToPath)) {
        androidQToPath
        path
    } else {
        androidQToPath
    }