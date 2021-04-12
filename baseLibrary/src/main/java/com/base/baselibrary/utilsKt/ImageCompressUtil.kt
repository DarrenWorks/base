package com.base.baselibrary.utilsKt

import android.app.Dialog
import android.content.Context
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import com.blankj.utilcode.constant.PermissionConstants
import com.blankj.utilcode.util.LogUtils
import com.blankj.utilcode.util.PathUtils
import com.blankj.utilcode.util.PermissionUtils
import io.reactivex.Observable
import io.reactivex.Observer
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.Disposable
import io.reactivex.schedulers.Schedulers
import java.io.File
import java.io.FileOutputStream

@JvmOverloads
fun compress(path: String, context: Context, loadingDialog: Dialog, handleResult: (path :String) -> kotlin.Unit, limit: Int = 3, fileSizeUnit: FileSizeUnit = FileSizeUnit.mb) {
    PermissionUtils.permission(PermissionConstants.STORAGE)
            .callback(object : PermissionUtils.SimpleCallback {
                override fun onGranted() {
                    Observable.create<String> { emitter ->
                        emitter.onNext(
                                compressInternal(
                                        path,
                                        limit,
                                        fileSizeUnit
                                )
                        )
                    }
                            .subscribeOn(Schedulers.io())
                            .observeOn(AndroidSchedulers.mainThread())
                            .subscribe(object : Observer<String> {
                                override fun onComplete() {
                                    if (loadingDialog.isShowing) {
                                        try {
                                            loadingDialog.dismiss()
                                        } catch (e: Exception) {
                                            e.printStackTrace()
                                        }
                                    }
                                }

                                override fun onSubscribe(d: Disposable) {
                                    if (!loadingDialog.isShowing) {
                                        try {
                                            loadingDialog.show()
                                        } catch (e: Exception) {
                                            e.printStackTrace()
                                        }

                                    }
                                }

                                override fun onNext(t: String) {
                                    if (loadingDialog.isShowing) {
                                        try {
                                            loadingDialog.dismiss()
                                        } catch (e: Exception) {
                                            e.printStackTrace()
                                        }
                                        handleResult(t)
                                    }
                                }

                                override fun onError(e: Throwable) {
                                    if (loadingDialog.isShowing) {
                                        try {
                                            loadingDialog.dismiss()
                                        } catch (e: Exception) {
                                            e.printStackTrace()
                                        }

                                    }
                                }
                            })

                }

                override fun onDenied() {
                    showToastShort("请允许权限后再试")
                }
            })
            .request()
}

@JvmOverloads
fun compress(path : List<String>, context: Context, loadingDialog: Dialog, handleResult : (List<String>) -> kotlin.Unit, limit : Int = 3, fileSizeUnit: FileSizeUnit = FileSizeUnit.mb) {
    PermissionUtils.permission(PermissionConstants.STORAGE)
        .callback(object : PermissionUtils.SimpleCallback {
            override fun onGranted() {
                Observable.create<List<String>> { emitter ->
                    run {
                        var compressPath = mutableListOf<String>()
                        for (s in path) {
                        compressPath.add(
                            compressInternal(
                                s,
                                limit,
                                fileSizeUnit
                            )
                        )
                        }
                        emitter.onNext(compressPath)
                    }
                }
                    .subscribeOn(Schedulers.io())
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribe(object : Observer<List<String>> {
                        override fun onComplete() {
                            if (loadingDialog.isShowing) {
                                try {
                                    loadingDialog.dismiss()
                                } catch (e: Exception) {
                                    e.printStackTrace()
                                }
                            }
                        }

                        override fun onSubscribe(d: Disposable) {
                            if (!loadingDialog.isShowing) {
                                try {
                                    loadingDialog.show()
                                } catch (e: Exception) {
                                    e.printStackTrace()
                                }

                            }
                        }

                        override fun onNext(t: List<String>) {
                            if (loadingDialog.isShowing) {
                                try {
                                    loadingDialog.dismiss()
                                } catch (e: Exception) {
                                    e.printStackTrace()
                                }
                                handleResult(t)
                            }
                        }

                        override fun onError(e: Throwable) {
                            if (loadingDialog.isShowing) {
                                try {
                                    loadingDialog.dismiss()
                                } catch (e: Exception) {
                                    e.printStackTrace()
                                }

                            }
                        }
                    })

            }

            override fun onDenied() {
                showToastShort("请允许权限后再试")
            }
        })
        .request()
}

private fun compressInternal(path : String, limit : Int, fileSizeUnit: FileSizeUnit) : String {
    LogUtils.i(File(path).length())
    val limitByte : Long = limit * fileSizeUnit.multiple
    val options = BitmapFactory.Options()

    if (File(path).length() > limitByte) {
        options.inSampleSize = (File(path).length() / limitByte * 2).toInt()
        val bitmap = BitmapFactory.decodeFile(path, options)
        val format: Bitmap.CompressFormat
        val formatStr: String
        when {
            options.outMimeType.equals("png", true) -> {
                format = Bitmap.CompressFormat.PNG
                formatStr = "png"
            }
            options.outMimeType.equals("webp", true) -> {
                format = Bitmap.CompressFormat.WEBP
                formatStr = "webp"
            }
            else -> {
                format = Bitmap.CompressFormat.JPEG
                formatStr = "jpg"
            }

        }
        val tempPath =
            PathUtils.getExternalAppCachePath() + "/temp" + System.currentTimeMillis() + "." + formatStr
        if (File(tempPath).exists()) {
            File(tempPath).delete()
        }
        bitmap.compress(format, 100, FileOutputStream(tempPath))
        PathUtils.getDataPath()
        LogUtils.i(File(tempPath).length())
        return tempPath
    } else {
        return path
    }
}


enum class FileSizeUnit(val multiple: Long) {
    b(1),
    kb(1024),
    mb(1024 * 1024),
    gb(1024 * 1024 * 1024)
}