package com.dadalong.autotest.testng.groups;

import org.testng.annotations.Test;

@Test(groups = "class2")
public class GroupsOnClass2 {

    public void stu1() {
        System.out.println("GroupsOnClass2的stu1");
    }

    public void stu2(){
        System.out.println("GroupsOnClass2的stu2");
    }

}