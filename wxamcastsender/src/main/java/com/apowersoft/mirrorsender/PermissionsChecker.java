package com.apowersoft.mirrorsender;

import android.content.Context;
import android.content.pm.PackageManager;
import androidx.annotation.NonNull;
import androidx.core.content.ContextCompat;

import java.util.ArrayList;
import java.util.List;

/**
 * 检查权限的工具类
 */
public class PermissionsChecker {
    /**
     * 判读权限集合是否缺少权限。
     *
     * @param context
     * @param permissions
     * @return 缺少权限返回true, 否则返回false。
     */
    public static boolean lacksPermissions(Context context, String... permissions) {
        for (String permission : permissions) {

            if (lacksPermission(context, permission)) {
                return true;
            }
        }
        return false;
    }

    /**
     * 判断是否缺少权限
     *
     * @param context
     * @param permission
     * @return 缺少权限返回true, 否则返回false。
     */
    public static boolean lacksPermission(Context context, String permission) {
        return ContextCompat.checkSelfPermission(context, permission) ==
                PackageManager.PERMISSION_DENIED;
    }

    /**
     * 过滤出未授权的权限集。
     *
     * @param context
     * @param permissions
     * @return 缺少权限返回所缺少权限的集合String[], 否则返回null。
     */
    public static String[] getLackPermission(Context context, String... permissions) {
        List<String> lackPermissions = new ArrayList<String>();
        for (String permission : permissions) {
            if (lacksPermission(context, permission)) {
                lackPermissions.add(permission);
            }
        }
        if (lackPermissions.size() == 0) {
            return null;
        }
        return lackPermissions.toArray(new String[lackPermissions.size()]);
    }

    /**
     * 判读权限结果是否都已授权
     *
     * @param grantResults
     * @return
     */
    public static boolean hasAllPermissionsGranted(@NonNull int[] grantResults) {
        for (int grantResult : grantResults) {
            if (grantResult == PackageManager.PERMISSION_DENIED) {
                return false;
            }
        }
        return true;
    }

    /**
     * sdk是否6.0（>=23）或以上
     *
     * @return
     */
    public static boolean isOverMarshmallow() {
        return android.os.Build.VERSION.SDK_INT >= 23;
    }

}
