package com.dadalong.autotest.testng.suite;

import org.testng.annotations.AfterSuite;
import org.testng.annotations.AfterTest;
import org.testng.annotations.BeforeSuite;
import org.testng.annotations.BeforeTest;

//写运行测试套件前需要运行共有方法
public class SuiteConfig {

    @BeforeSuite
    public void beforeSuite() {
        System.out.println("这是BeforeSuite");
    }

    @AfterSuite
    public void afterSuite(){
        System.out.println("这是AfterSuite");
    }

    @BeforeTest
    public void beforeTest() {
        System.out.println("这是BeforeTest");
    }

    @AfterTest
    public void afterTest(){
        System.out.println("这是AfterTest");
    }

}
