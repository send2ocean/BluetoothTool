package com.fyts.bluetoothtool;

import android.app.Activity;
import android.app.Dialog;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.text.TextUtils;
import android.util.Log;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.TextView;

import com.fyts.bluetoothtool.intef.OnSelectListenerImpl;
import com.fyts.bluetoothtool.other.ToastUtils;
import com.fyts.bluetoothtool.ui.BaseActivity;
import com.fyts.bluetoothtool.view.qrcode.CaptureManager;
import com.fyts.bluetoothtool.view.qrcode.DecoratedBarcodeView;
import com.google.zxing.integration.android.IntentIntegrator;
import com.google.zxing.integration.android.IntentResult;

/**
 * Message: 二维码扫描页面
 * Author: 吕帅义
 * Date: 2021/4/15 18:58
 */
public class ScanActivity extends BaseActivity {

    private CaptureManager capture;
    private DecoratedBarcodeView barcodeScannerView;

    @Override
    protected int getLayoutId() {
        return R.layout.activity_scan;
    }

    @Override
    protected void initView() {
        barcodeScannerView = findViewById(R.id.zxing_barcode_scanner);
        capture = new CaptureManager(this, barcodeScannerView);
        capture.initializeFromIntent(getIntent(), savedInstanceState);
        capture.setResultCallBack(new CaptureManager.ResultCallBack() {
            @Override
            public void callBack(int requestCode, int resultCode, Intent intent) {
                //二维码扫描结构
                IntentResult scanResult = IntentIntegrator.parseActivityResult(requestCode, resultCode, intent);
                if (scanResult != null) {
                    String result = scanResult.getContents();
                    goToActivtity(result);
                } else {
                    capture.onResume();
                    capture.decode();
                }
            }
        });
        capture.decode();

        findViewById(R.id.iv_scan).setOnClickListener(v -> finish());
    }

    //扫码后跳转
    public void goToActivtity(String text) {
        Log.d("scanZxing", "扫描后的数据：" + text);
        if (!TextUtils.isEmpty(text)) {
            showNormalDialog(text);
        }
    }


    @Override
    protected void getData() {

    }

    @Override
    protected void onResume() {
        super.onResume();
        capture.onResume();
        capture.decode();
    }

    @Override
    protected void onPause() {
        super.onPause();
        capture.onPause();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        capture.onDestroy();
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        capture.onSaveInstanceState(outState);
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String permissions[], int[] grantResults) {
        capture.onRequestPermissionsResult(requestCode, permissions, grantResults);
    }


    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        return barcodeScannerView.onKeyDown(keyCode, event) || super.onKeyDown(keyCode, event);
    }


    /**
     * dialog提示框
     *
     * @param text
     */
    public void showNormalDialog(String text) {
        final Dialog normalDialog = new Dialog(activity, R.style.dialog);
        View inflate = LayoutInflater.from(activity).inflate(R.layout.mess_view, null);
        TextView dia_mes = inflate.findViewById(R.id.dia_mes);
        dia_mes.setText(text);

        inflate.findViewById(R.id.dia_con).setOnClickListener(v -> {
            normalDialog.dismiss();
            capture.onResume();
            capture.decode();
        });
        normalDialog.setCanceledOnTouchOutside(false);
        normalDialog.setContentView(inflate);
        normalDialog.show();
    }

}
