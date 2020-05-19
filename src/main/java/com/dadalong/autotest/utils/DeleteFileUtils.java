package com.dadalong.autotest.utils;

import org.springframework.context.annotation.Configuration;

import java.io.File;

/**
 * 提供绝对路径删除文件夹下所有文件和文件夹
 */
@Configuration
public class DeleteFileUtils {
    public boolean delAllFile(String path) {
        boolean flag = false;
        File file = new File(path);
        if (!file.exists()) {
            return flag;
        }
        if (!file.isDirectory()) {
            return flag;
        }
        String[] tempList = file.list();
        File temp = null;
        for (int i = 0; i < tempList.length; i++) {
            if (path.endsWith(File.separator)) {
                temp = new File(path + tempList[i]);
            } else {
                temp = new File(path + File.separator + tempList[i]);
            }
            if (temp.isFile()) {
                temp.delete();
            }
            if (temp.isDirectory()) {
                delAllFile(path + "/" + tempList[i]);//删除文件夹里面的文件
                flag = true;
            }
        }
        return flag;
    }

}
