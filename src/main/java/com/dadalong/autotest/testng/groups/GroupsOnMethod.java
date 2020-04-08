package com.dadalong.autotest.testng.groups;

import org.testng.annotations.AfterGroups;
import org.testng.annotations.BeforeGroups;
import org.testng.annotations.Test;

public class GroupsOnMethod {

    @Test(groups = "zu1")
    public void group1Test1() {
        System.out.println("组1的测试方法1");
    }

    @Test(groups = "zu1")
    public void group1Test2() {
        System.out.println("组1的测试方法2");
    }

    @Test(groups = "zu2")
    public void group2Test1() {
        System.out.println("组2的测试方法1");
    }

    @Test(groups = "zu2")
    public void group2Test2() {
        System.out.println("组2的测试方法2");
    }

    @BeforeGroups("zu1")
    public void beforeGroupsZu1() {
        System.out.println("zu1的BeforeGroups");
    }

    @AfterGroups("zu1")
    public void afterGroupsZu1() {
        System.out.println("zu1的AfterGroups");
    }

    @BeforeGroups("zu2")
    public void beforeGroupsZu2() {
        System.out.println("zu2的BeforeGroups");
    }

    @AfterGroups("zu2")
    public void afterGroupsZu2() {
        System.out.println("zu2的AfterGroups");
    }

}
