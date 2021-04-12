/*
 * Copyright 2016 jeasonlzy(廖子尧)
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.base.baselibrary.okgo;

import android.app.Dialog;
import android.text.TextUtils;
import android.util.Log;

import com.base.baselibrary.utilsKt.GeneralUtilsKt;
import com.lzy.okgo.OkGo;
import com.lzy.okgo.model.Response;
import com.lzy.okgo.request.base.Request;

import org.jetbrains.annotations.NotNull;

import java.net.SocketException;
import java.net.SocketTimeoutException;

/**
 * ================================================
 * 作    者：jeasonlzy（廖子尧）Github地址：https://github.com/jeasonlzy
 * 版    本：1.0
 * 创建日期：2016/1/14
 * 描    述：对于网络请求是否需要弹出进度对话框
 * 修订历史：
 * ================================================
 */
public abstract class DialogCallback<T> extends JsonCallback<T> {

    private Dialog mLoadingDialog;
    private String mTag;

    public DialogCallback(@NotNull Dialog loadingDialog) {
        super();
        initDialog(loadingDialog);
    }

    public DialogCallback(@NotNull Dialog loadingDialog, String tag) {
        super();
        initDialog(loadingDialog);
        mTag = tag;
    }

    private void initDialog(Dialog loadingDialog) {
        mLoadingDialog = loadingDialog;
        mLoadingDialog.setOnCancelListener(dialog -> cancelByDialog());
    }

    @Override
    public void onStart(Request<T, ? extends Request> request) {
        if (mLoadingDialog != null && !mLoadingDialog.isShowing()) {
            try {
                mLoadingDialog.show();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    @Override
    public void onError(Response<T> response) {
        super.onError(response);
        if (response.getException() instanceof SocketTimeoutException) {
            Log.d("JsonCallback", "请求超时");
            GeneralUtilsKt.showToastShort("请求超时");
        } else if (response.getException() instanceof SocketException) {
            Log.d("JsonCallback", "网络异常");
            GeneralUtilsKt.showToastShort("网络异常");
        }
    }

    @Override
    public void onFinish() {
        if (mLoadingDialog != null && mLoadingDialog.isShowing()) {
            try {
                mLoadingDialog.dismiss();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    public void cancelByDialog() {
        if (!TextUtils.isEmpty(mTag)) {
            OkGo.getInstance().cancelTag(mTag);
        }
    }
}
