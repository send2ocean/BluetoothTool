package com.fyts.bluetoothtool;

import android.Manifest;
import android.content.Intent;
import android.text.TextUtils;
import android.view.View;
import android.widget.ProgressBar;

import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.fyts.bluetoothtool.adapter.BluetoothListAdapter;
import com.fyts.bluetoothtool.intef.OnAdapterClickListenerImpl;
import com.fyts.bluetoothtool.other.PermissionUtils;
import com.fyts.bluetoothtool.other.ToastUtils;
import com.fyts.bluetoothtool.other.ClientManager;
import com.fyts.bluetoothtool.ui.BaseSmartListActivity;
import com.google.zxing.integration.android.IntentIntegrator;
import com.inuker.bluetooth.library.connect.response.BleConnectResponse;
import com.inuker.bluetooth.library.model.BleGattProfile;
import com.inuker.bluetooth.library.search.SearchRequest;
import com.inuker.bluetooth.library.search.SearchResult;
import com.inuker.bluetooth.library.search.response.SearchResponse;

import java.util.ArrayList;
import java.util.List;

import static com.inuker.bluetooth.library.Constants.REQUEST_SUCCESS;

/**
 * 首页
 */
public class MainActivity extends BaseSmartListActivity {

    //蓝牙权限-定位权限
    public static String[] PERSSION_LOCATION = {Manifest.permission.ACCESS_FINE_LOCATION, Manifest.permission.ACCESS_COARSE_LOCATION};
    //蓝牙搜索列表
    private BluetoothListAdapter adapter;
    private List<SearchResult> devicesList;
    //搜索进度条
    private ProgressBar progressBar;

    @Override
    protected int getLayoutId() {
        return R.layout.activity_main;
    }

    @Override
    protected void initView() {
        findRefresh();
        devicesList = new ArrayList<>();
        progressBar = findViewById(R.id.progress);

        //申请权限
        PermissionUtils.newIntence().requestPerssion(activity, PERSSION_LOCATION, 1, code -> {
            getBluetooth();
        });
    }

    //扫描蓝牙
    private void getBluetooth() {
        SearchRequest request = new SearchRequest.Builder()
                .searchBluetoothLeDevice(3000, 3)   // 先扫BLE设备3次，每次3s
                .searchBluetoothClassicDevice(2000) // 再扫经典蓝牙5s
                .searchBluetoothLeDevice(2000)      // 再扫BLE设备2s
                .build();
        ClientManager.getClient().search(request, new SearchResponse() {
            @Override
            public void onSearchStarted() {
                //显示进度条
                progressBar.setVisibility(View.VISIBLE);
            }

            @Override
            public void onDeviceFounded(SearchResult device) {
                //过滤掉重复和名称为null的蓝牙设备
                if (TextUtils.isEmpty(device.getName()) || device.getName().equals("NULL")) return;
                if (!devicesList.contains(device)) {
                    devicesList.add(device);
                    adapter.setData(devicesList);
                }
            }

            @Override
            public void onSearchStopped() {
                //扫描暂停
                closeRefresh();
                progressBar.setVisibility(View.GONE);
            }

            @Override
            public void onSearchCanceled() {
                //扫描结束
                closeRefresh();
                progressBar.setVisibility(View.GONE);
            }
        });

        //二维码点击事件
        findViewById(R.id.iv_scan).setOnClickListener(v -> {
            //这里的上下文是activity  所以回调的应该在 fragment所在的activity的onActivityResult
            IntentIntegrator integrator = new IntentIntegrator(activity);
            // 设置要扫描的条码类型，ONE_D_CODE_TYPES：一维码，QR_CODE_TYPES-二维码
            integrator.setDesiredBarcodeFormats(IntentIntegrator.QR_CODE_TYPES);
            integrator.setCaptureActivity(ScanActivity.class); //设置打开摄像头的Activity
            integrator.setCameraId(0); //前置或者后置摄像头
            integrator.setBeepEnabled(true); //扫描成功的「哔哔」声，默认开启
            integrator.setBarcodeImageEnabled(true);
            integrator.initiateScan();
        });
    }

    @Override
    protected void getData() {

    }

    @Override
    protected void getList() {
        //刷新扫描
        getBluetooth();
    }

    @Override
    protected void onPause() {
        super.onPause();
        //离开页面停止扫描
        ClientManager.getClient().stopSearch();
    }

    /**
     * 列表的点击事件
     */
    @Override
    protected RecyclerView.Adapter getAdapter() {
        adapter = new BluetoothListAdapter(activity, new OnAdapterClickListenerImpl() {
            @Override
            public void onItemClickListener(View v, int pos) {
                showProgress(true);
                //停止扫描
                ClientManager.getClient().stopSearch();
                //获取点击的蓝牙数据
                SearchResult choseData = adapter.getChoseData(pos);
                //连接蓝牙
                ClientManager.getClient().connect(choseData.getAddress(), new BleConnectResponse() {
                    @Override
                    public void onResponse(int code, BleGattProfile profile) {
                        showProgress(false);
                        //连接成功跳转页面
                        if (code == REQUEST_SUCCESS) {
                            startActivity(new Intent(activity, ServiceListActivity.class)
                                    .putExtra("data", profile)
                                    .putExtra("address", choseData.getAddress()));
                        } else {
                            //连接失败
                            ToastUtils.showLong(activity, code + "");
                        }
                    }
                });
            }
        });
        return adapter;
    }

    @Override
    protected RecyclerView.LayoutManager getLayoutManager() {
        //线形布局排列
        return new LinearLayoutManager(activity);
    }
}