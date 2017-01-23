package com.wevalue.utils;

import android.content.Context;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.RandomAccessFile;
import java.util.UUID;

/**
 * 作者：邹永奎
 * 创建时间：2016/10/13
 * 类说明：用于获取设备唯一安装码的工具类
 */

public class InstallIdUtil {
    private static String sID = null;
    private static final String INSTALLATION = "INSTALLATION";
    Context mContext;

    public InstallIdUtil(Context context) {
        mContext=context;
        getId(mContext);
    }

    public static String getsID() {
        return sID;
    }
    public synchronized static void getId(Context context) {
        if (sID == null) {
            File installation = new File(context.getFilesDir(), INSTALLATION);
            try {
                if (!installation.exists())
                    writeInstallationFile(installation);
                sID = readInstallationFile(installation);
            } catch (Exception e) {
                throw new RuntimeException(e);
            }
        }
    }

    private static String readInstallationFile(File installation) throws IOException {
        RandomAccessFile f = new RandomAccessFile(installation, "r");
        byte[] bytes = new byte[(int) f.length()];
        f.readFully(bytes);
        f.close();
        return new String(bytes);
    }

    private static void writeInstallationFile(File installation) throws IOException {
        FileOutputStream out = new FileOutputStream(installation);
        String id = UUID.randomUUID().toString();
        out.write(id.getBytes());
        out.close();
    }
}

