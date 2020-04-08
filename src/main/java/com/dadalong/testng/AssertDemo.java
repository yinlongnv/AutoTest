package com.dadalong.testng;

import org.testng.Assert;
import org.testng.Reporter;
import org.testng.annotations.Test;

public class AssertDemo {

    @Test
    public void assertTest1() {
        Assert.assertEquals(1, 2);
    }

    @Test
    public void assertTest2() {
        Assert.assertEquals(1, 1);
    }

    @Test
    public void assertTest3() {
        Assert.assertEquals("aaa", "aaa");
    }

    @Test
    public void logTest1() {
        Reporter.log("这是我们自己写的日志");
        throw new RuntimeException("自己运行时的异常");
    }

}
