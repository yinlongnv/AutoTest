package com.dadalong.testng;

import org.testng.annotations.Test;

public class TimeOutTest {

    @Test(timeOut = 3000)//单位为毫秒
    public void timeOutSuccess() throws InterruptedException{
        Thread.sleep(2000);
    }

    @Test(timeOut = 2000)//单位为毫秒
    public void timeOutFailed() throws InterruptedException{
        Thread.sleep(3000);
    }

}
