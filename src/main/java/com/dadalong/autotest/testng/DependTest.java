package com.dadalong.autotest.testng;

import org.testng.annotations.Test;

public class DependTest {

    @Test
    public void DependTest1() {
        System.out.println("DependTest1执行");
        //throw new RuntimeException();
    }

    @Test(dependsOnMethods = {"DependTest1"})
    public void DependTest2() {
        System.out.println("DependTest2依赖DependTest1执行");
    }

}
