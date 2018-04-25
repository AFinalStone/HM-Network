package com.hm.iou.network.utils;

/**
 * Created by hjy on 18/4/25.<br>
 */

public class StringUtil {

    public static String getUnnullString(String str) {
        if (str == null) {
            return "";
        }
        return str;
    }

}
