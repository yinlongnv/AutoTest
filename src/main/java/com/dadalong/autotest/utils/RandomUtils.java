package com.dadalong.autotest.utils;

import org.apache.commons.lang3.RandomStringUtils;
import org.springframework.context.annotation.Configuration;

import java.text.SimpleDateFormat;
import java.util.*;
import java.util.stream.Collectors;

/**
 * 随机生成数据的工具类
 */
@Configuration
public class RandomUtils {

    private int getNum(int start, int end) {
        return (int) (Math.random() * (end - start + 1) + start);
    }

    /**
     * 随机生成手机号
     */
    public String getPhoneRandom() {
        String[] telFirst = "134,135,136,137,138,139,150,151,152,157,158,159,130,131,132,155,156,133,153".split(",");
        int index = getNum(0, telFirst.length - 1);
        String first = telFirst[index];
        String second = String.valueOf(getNum(1, 888) + 10000).substring(1);
        String third = String.valueOf(getNum(1, 9100) + 10000).substring(1);
        String phone = first + second + third;
        return phone;
    }

    /**
     * 随机生成邮箱地址
     * @return
     */
    public String getEmailRandom() {
        String[] email_suffix = "@gmail.com,@yahoo.com,@msn.com,@hotmail.com,@aol.com,@ask.com,@live.com,@qq.com,@0355.net,@163.com,@163.net,@263.net,@3721.net,@yeah.net,@googlemail.com,@126.com,@sina.com,@sohu.com,@yahoo.com.cn".split(",");
        String base = "abcdefghijklmnopqrstuvwxyz0123456789";
        StringBuffer sb = new StringBuffer();
        for (int i = 0; i < 10; i++) {
            int number = (int) (Math.random() * base.length());
            sb.append(base.charAt(number));
        }
        sb.append(email_suffix[(int) (Math.random() * email_suffix.length)]);
        return sb.toString();
    }

    /**
     * 随机生成身份证号
     * @return
     */
    public String getIdNumberRandom() {
        String id = "";
        // 随机生成省、自治区、直辖市代码 1-2
        String provinces[] = { "11", "12", "13", "14", "15", "21", "22", "23",
                "31", "32", "33", "34", "35", "36", "37", "41", "42", "43",
                "44", "45", "46", "50", "51", "52", "53", "54", "61", "62",
                "63", "64", "65", "71", "81", "82" };
        String province = provinces[new Random().nextInt(provinces.length - 1)];
        // 随机生成地级市、盟、自治州代码 3-4
        String citys[] = { "01", "02", "03", "04", "05", "06", "07", "08",
                "09", "10", "21", "22", "23", "24", "25", "26", "27", "28" };
        String city = citys[new Random().nextInt(citys.length - 1)];
        // 随机生成县、县级市、区代码 5-6
        String countys[] = { "01", "02", "03", "04", "05", "06", "07", "08",
                "09", "10", "21", "22", "23", "24", "25", "26", "27", "28",
                "29", "30", "31", "32", "33", "34", "35", "36", "37", "38" };
        String county = countys[new Random().nextInt(countys.length - 1)];
        // 随机生成出生年月 7-14
        SimpleDateFormat dft = new SimpleDateFormat("yyyyMMdd");
        Date beginDate = new Date();
        Calendar date = Calendar.getInstance();
        date.setTime(beginDate);
        date.set(Calendar.DATE,
                date.get(Calendar.DATE) - new Random().nextInt(365 * 100));
        String birth = dft.format(date.getTime());
        // 随机生成顺序号 15-17
        String no = new Random().nextInt(999) + "";
        // 随机生成校验码 18
        String checks[] = { "0", "1", "2", "3", "4", "5", "6", "7", "8", "9",
                "X" };
        String check = checks[new Random().nextInt(checks.length - 1)];
        // 拼接身份证号码
        id = province + city + county + birth + no + check;
        return id;
    }

    /**
     * 获取随机字符串
     * @param min
     * @param max
     * @return
     */
    public List<String> getStringOrOtherRandom(int min, int max) {
        List<Integer> integers = makeRangeList(min, max);
        List<String> stringList = new ArrayList<>();
        for (Integer i : integers) {
            String result;
            if (i<=0) {
                result = "a";
            } else {
                result = RandomStringUtils.randomAlphanumeric(i);
            }
            stringList.add(result);
        }
        return stringList;
    }

    /**
     * 获取随机整数
     * @param min
     * @param max
     * @return
     */
    public List<String> getIntegerRandom(int min, int max) {
        List<String> integerList = new ArrayList<>();
        Random random = new Random();
        String m = String.valueOf((random.nextInt(max)%(max-min+1) + min));
        integerList.add(m);
        String a = String.valueOf(min - 1);
        String b = String.valueOf(max + 1);
        integerList.add(a);
        integerList.add(b);
        return integerList;
    }

    /**
     * 处理最大值最小值
     * @param min
     * @param max
     * @return
     */
    public List<Integer> makeRangeList(int min, int max) {
        int a = min - 1;
        int b = max + 1;
        List<Integer> integers = new ArrayList<>();
        integers.add(min);
        integers.add(max);
        integers.add(a);
        integers.add(b);
        return integers;
    }

    /**
     * 获取随机密码
     * @param min
     * @param max
     * @return
     */
    public List<String> getPasswordRandom(int min, int max) {
        List<Integer> integers = makeRangeList(min, max);
        List<String> stringList = new ArrayList<>();
        for (Integer i : integers) {
            String result = makeRandomPassword(i);
            stringList.add(result);
        }
        return stringList;
    }

    //随机密码生成
    public String makeRandomPassword(int len){
        char charr[] = "abcdefghijklmnopqrstuvwxyzABCDEFGHIJKLMNOPQRSTUVWXYZ1234567890~!@#$%^&*.?".toCharArray();
        StringBuilder sb = new StringBuilder();
        Random r = new Random();
        for (int x = 0; x < len; ++x) {
            sb.append(charr[r.nextInt(charr.length)]);
        }
        return sb.toString();
    }

    /**
     * 给数组内每个元素加上单引号
     * @param strings
     * @return
     */
    public List<String> toStringList(List<String> strings) {
        String ids = strings.stream().map(s -> "\'" + s + "\'").collect(Collectors.joining(", "));
        String[] strings1 = ids.split(",");
        List<String> stringList = new ArrayList<>();
        stringList.addAll(Arrays.asList(strings1));
        return stringList;
    }
}
