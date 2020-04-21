package com.dadalong.autotest.utils;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * Created by 78089 on 2020/4/19.
 */
public class DateFormatUtils {

    public static final String DATEFORMAT = "yyyy-MM-dd HH:mm:ss";

    public static String parseDateToYMDHMS(Date date) {

        DateFormat dateFormat = new SimpleDateFormat(DATEFORMAT);
        return dateFormat.format(date);

    }

}
