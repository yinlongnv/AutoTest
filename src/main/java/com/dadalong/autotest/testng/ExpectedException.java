package com.dadalong.autotest.testng;

import org.testng.annotations.Test;

//在期望结果为某个异常的时候
public class ExpectedException {

    //运行一个预期失败的异常测试
    @Test(expectedExceptions = RuntimeException.class)
    public void runExpectionFailed() {
        System.out.println("这是一个运行失败的例子");
    }

    //运行一个预期成功的异常测试
    @Test(expectedExceptions = RuntimeException.class)
    public void runExpectionSuccess() {
        System.out.println("这是我的异常测试");
        throw new RuntimeException();
    }
}
