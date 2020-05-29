package com.dadalong.autotest.utils;

import org.springframework.context.annotation.Configuration;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.List;

/**
 * 处理接口参数数据
 */
@Configuration
public class PutListToTextUtils {

    /**
     * 将接口参数值放入txt
     * @param params
     * @throws IOException
     */
    public void putParamsToText(List<List<String>> params) throws IOException{
        File file = new File("D:\\Workspace\\IDEA\\AutoTest\\src\\main\\resources\\static\\pyToSql\\params.txt");
        if (file.exists()) {
            file.delete();
        }
        for (List<String> stringList : params) {
            for (String string : stringList) {
                int index = stringList.indexOf(string);
                stringList.set(index, "'" + string + "'");
            }
        }
        file.createNewFile();
        BufferedWriter out;
        out = new BufferedWriter(new FileWriter(file));
        for (List<String> stringList : params) {
                out.write(stringList.toString() + "\r\n");
                out.flush();
        }
            out.close();
    }

    /**
     * 将接口参数放入txt
     * @param keys
     * @throws IOException
     */
    public void putKeysToText(List<String> keys) throws IOException{
        File file = new File("D:\\Workspace\\IDEA\\AutoTest\\src\\main\\resources\\static\\pyToSql\\keys.txt");
        if(file.exists()) {
            file.delete();
        }
        file.createNewFile();
        BufferedWriter out = new BufferedWriter(new FileWriter(file));
        for (String key : keys) {
            out.write(key+"\r\n");
            out.flush();
        }
        out.close();
    }

}
