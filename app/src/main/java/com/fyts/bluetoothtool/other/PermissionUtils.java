package com.fyts.bluetoothtool.other;

import android.app.Activity;
import android.content.Intent;
import android.os.Build;
import android.util.Log;
import android.widget.Toast;

import androidx.annotation.NonNull;

import com.yanzhenjie.permission.AndPermission;
import com.yanzhenjie.permission.PermissionListener;
import com.yanzhenjie.permission.Rationale;
import com.yanzhenjie.permission.RationaleListener;

import java.util.List;

/**
 * Message:  权限判断
 * Created by  ChenLong.
 * Created by Time on 2018/1/29.
 */

public class PermissionUtils {
    public static PermissionUtils permissionUtils;

    public static PermissionUtils newIntence() {
        if (permissionUtils == null) {
            permissionUtils = new PermissionUtils();
        }
        return permissionUtils;
    }

    /**
     * 请求权限
     *
     * @param perssion    权限名称
     * @param requestCode 请求码
     * @param permission
     */
    public void requestPerssion(final Activity activity, String[] perssion, int requestCode, IPermission permission) {
        AndPermission.with(activity)
                .requestCode(requestCode)//请求码
                .permission(perssion)//权限名称
                .callback(new MyListener(activity, permission))//回调
                // rationale作用是：用户拒绝一次权限，再次申请时先征求用户同意，再打开授权对话框；
                // 这样避免用户勾选不再提示，导致以后无法申请权限。
                // 你也可以不设置。
                .rationale(new RationaleListener() {
                    @Override
                    public void showRequestPermissionRationale(int requestCode, Rationale rationale) {
                        // 这里的对话框可以自定义，只要调用rationale.resume()就可以继续申请。
                        AndPermission.rationaleDialog(activity, rationale).setMessage("需要您的位置权限").show();
                    }
                })
                .start();
    }

    public interface IPermission {
        void success(int code);
    }

    //请求回调
    private class MyListener implements PermissionListener {
        private Activity activity;
        private IPermission permission;

        public MyListener(Activity activity, IPermission permission) {
            this.activity = activity;
            this.permission = permission;
        }

        @Override
        public void onSucceed(final int requestCode, @NonNull List<String> grantPermissions) {
            if (permission != null) {
                permission.success(requestCode);
            }
        }

        @Override
        public void onFailed(int requestCode, @NonNull List<String> deniedPermissions) {
            Toast.makeText(activity, "请求权限失败", Toast.LENGTH_SHORT).show();
            //小米的手机直接跳转权限管理
            if (Build.MANUFACTURER.equalsIgnoreCase("xiaomi")) {
                openXiaomiSetting(activity);
            } else {
                // 用户否勾选了不再提示并且拒绝了权限，那么提示用户到设置中授权。
                if (AndPermission.hasAlwaysDeniedPermission(activity, deniedPermissions)) {
                    // 第一种：用默认的提示语。
                    AndPermission.defaultSettingDialog(activity, 103).show();
                }
            }
        }
    }

    /**
     * 打开权限设置界面(小米手机)
     *
     * @param activity
     */
    public void openXiaomiSetting(Activity activity) {
        try { // MIUI 8
            Intent localIntent = new Intent("miui.intent.action.APP_PERM_EDITOR");
            localIntent.setClassName("com.miui.securitycenter", "com.miui.permcenter.permissions.PermissionsEditorActivity");
            localIntent.putExtra("extra_pkgname", activity.getPackageName());
            activity.startActivity(localIntent);
        } catch (Exception e) {
            try { // MIUI 5/6/7
                Intent localIntent = new Intent("miui.intent.action.APP_PERM_EDITOR");
                localIntent.setClassName("com.miui.securitycenter", "com.miui.permcenter.permissions.AppPermissionsEditorActivity");
                localIntent.putExtra("extra_pkgname", activity.getPackageName());
                activity.startActivity(localIntent);
            } catch (Exception e1) { // 否则跳转到应用详情
                Log.d("11111111111", "否则跳转到应用详情");
            }
        }
    }
}
