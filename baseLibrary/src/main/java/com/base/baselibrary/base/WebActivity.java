package com.base.baselibrary.base;

import android.content.Context;
import android.content.Intent;
import android.webkit.WebChromeClient;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;

import com.base.baselibrary.databinding.BaseActivityWebBinding;

public class WebActivity extends BaseActivity<BaseActivityWebBinding> {
    private String mUrl;
    private String mTitle;
    private boolean mShowProtocol;

    public static void start(Context context, String url, String title) {
        Intent starter = new Intent(context, WebActivity.class);
        starter.putExtra("url", url);
        starter.putExtra("title", title);
        context.startActivity(starter);
    }

    public static void start(Context context, String url, String title, boolean showProtocol) {
        Intent starter = new Intent(context, WebActivity.class);
        starter.putExtra("url", url);
        starter.putExtra("title", title);
        starter.putExtra("showProtocol", showProtocol);
        context.startActivity(starter);
    }

    @Override
    protected boolean initExtra(Intent intent) {
        mUrl = intent.getStringExtra("url");
        mTitle = intent.getStringExtra("title");
        mShowProtocol = intent.getBooleanExtra("showProtocol", false);
        if (mUrl == null || mTitle == null) {
            return false;
        }

        return super.initExtra(intent);
    }

    @Override
    protected void init() {
        super.init();

        setTopByMargin(mBinding.back);
        mBinding.title.setText(mTitle);
        initWebView();
    }

    @Override
    protected void initClicks() {
        super.initClicks();

        antiShakeClick(mBinding.back, this::onBackPressed);
    }

    private void initWebView() {
        WebSettings settings = mBinding.web.getSettings();
        settings.setJavaScriptEnabled(true);
//        settings.setUseWideViewPort(true);
        settings.setLayoutAlgorithm(WebSettings.LayoutAlgorithm.NARROW_COLUMNS);
        settings.setLoadWithOverviewMode(true);
        mBinding.web.setWebViewClient(new WebViewClient() {
            @Override
            public boolean shouldOverrideUrlLoading(WebView view, String url) {
                mBinding.web.loadUrl(url);
                return true;
            }
        });
        mBinding.web.setWebChromeClient(new WebChromeClient() {
            @Override
            public void onProgressChanged(WebView view, int newProgress) {
               /* if (newProgress == 100) {
                    progressBar.setVisibility(View.GONE);
                } else {
                    progressBar.setVisibility(View.VISIBLE);
                    progressBar.setProgress(newProgress);
                }*/
                super.onProgressChanged(view, newProgress);
            }
        });
        mBinding.web.loadUrl(mUrl);
    }
}
