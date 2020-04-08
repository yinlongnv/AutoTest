package com.dadalong.autotest.testng;

import org.testng.annotations.*;

public class BasicAnnotation {

    //最基本的注解，用来把方法标记为测试的一部分
    @Test
    public void testCase1() {
        System.out.printf("Thread Id : %s%n",Thread.currentThread().getId());
        System.out.println("这是测试用例一");
    }

    @Test
    public void testCase2() {
        System.out.printf("Thread Id : %s%n",Thread.currentThread().getId());
        System.out.println("这是测试用例二");
    }

    @BeforeMethod
    public void beforeMethod() {
        System.out.println("这是在测试方法执行前运行的方法");
    }

    @AfterMethod
    public void afterMethod(){
        System.out.println("这是在测试方法执行后运行的方法");
    }

    @BeforeClass
    public void beforeClass() {
        System.out.println("这是在类运行前运行的方法");
    }

    @AfterClass
    public void afterClass(){
        System.out.println("这是在类运行后运行的方法");
    }

    @BeforeSuite
    public void beforeSuite() {
        System.out.println("这是BeforeSuite");
    }

    @AfterSuite
    public void afterSuite(){
        System.out.println("这是AfterSuite");
    }

}
