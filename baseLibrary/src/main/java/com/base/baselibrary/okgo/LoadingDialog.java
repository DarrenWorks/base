package com.base.baselibrary.okgo;

import android.app.Dialog;
import android.content.Context;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.view.LayoutInflater;

import androidx.annotation.ColorRes;
import androidx.annotation.NonNull;
import androidx.core.app.NotificationCompat;

import com.base.baselibrary.R;
import com.wang.avi.AVLoadingIndicatorView;

public class LoadingDialog extends Dialog {
    AVLoadingIndicatorView avi;



    public LoadingDialog(@NonNull Context context) {
        super(context);
        setContentView(LayoutInflater.from(context).inflate(R.layout.fragment_loading_dialog, null));

        avi = findViewById(R.id.avi);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setCanceledOnTouchOutside(false);
        getWindow().setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));
    }

    @Override
    public void show() {
        super.show();

        avi.show();
    }

    @Override
    protected void onStop() {
        super.onStop();

        avi.hide();
    }
}
