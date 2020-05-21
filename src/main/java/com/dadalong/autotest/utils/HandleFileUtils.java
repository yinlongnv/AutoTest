package com.dadalong.autotest.utils;

import org.springframework.context.annotation.Configuration;

import java.io.File;
import java.util.Arrays;
import java.util.Comparator;

/**
 * 处理文件工具类
 */
@Configuration
public class HandleFileUtils {

    /**
     * 提供绝对路径删除文件夹下所有文件和文件夹
     * @param path
     * @return
     */
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

    /**
     * 获取指定文件夹下最新创建的文件名
     * @param pathName
     * @return
     */
    public String findNewFile(String pathName) {
        File path=new File(pathName);
        //列出该目录下所有文件和文件夹
        File[] files = path.listFiles();
        //按照文件最后修改日期倒序排序
        Arrays.sort(files, new Comparator<File>() {
            @Override
            public int compare(File file1, File file2) {
                return (int)(file2.lastModified()-file1.lastModified());
            }
        });
        //取出第一个(即最新修改的)文件，打印文件名
        System.out.println(files[0].getName());
        return files[0].getName();
    }

}
