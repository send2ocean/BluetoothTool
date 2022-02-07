package com.fyts.bluetoothtool.other;

import com.fyts.bluetoothtool.App;
import com.inuker.bluetooth.library.BluetoothClient;

/**
 * 初始化蓝牙管理.
 */
public class ClientManager {

    private static BluetoothClient mClient;

    public static BluetoothClient getClient() {
        if (mClient == null) {
            synchronized (ClientManager.class) {
                if (mClient == null) {
                    mClient = new BluetoothClient(App.mContext);
                }
            }
        }
        return mClient;
    }
}
