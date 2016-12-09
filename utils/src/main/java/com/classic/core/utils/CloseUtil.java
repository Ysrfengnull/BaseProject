package com.classic.core.utils;

import java.io.Closeable;
import java.io.IOException;

/**
 * 应用名称: RxBasicProject
 * 包 名 称: com.classic.core.utils
 *
 * 文件描述: Closeable工具类
 * 创 建 人: 续写经典
 * 创建时间: 2015/11/4 17:26
 */
@SuppressWarnings("WeakerAccess") public final class CloseUtil {

    private CloseUtil() { }

    public static void close(Closeable closeable) {
        if (null != closeable) {
            try {
                closeable.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    public static void close(Closeable... params) {
        if (null != params) {
            try {
                for (Closeable closeable : params) {
                    closeable.close();
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
}
