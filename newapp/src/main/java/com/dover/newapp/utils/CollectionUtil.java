package com.dover.newapp.utils;

import java.util.Collection;

/**
 * Created by d on 2017/7/24.
 */
public class CollectionUtil {

    /**
     * 判断集合是否为null或者0个元素
     *
     * @param c
     * @return
     */
    public static boolean isNullOrEmpty(Collection c) {
        if (null == c || c.isEmpty()) {
            return true;
        }
        return false;
    }


}
