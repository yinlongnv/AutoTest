package com.dadalong.testng.groups;

import org.testng.annotations.Test;

@Test(groups = "class1")
public class GroupsOnClass1 {

    public void stu1() {
        System.out.println("GroupsOnClass1的stu1");
    }

    public void stu2(){
        System.out.println("GroupsOnClass1的stu2");
    }

}
