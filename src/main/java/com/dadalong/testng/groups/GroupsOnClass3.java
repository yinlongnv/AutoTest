package com.dadalong.testng.groups;

import org.testng.annotations.Test;

@Test(groups = "teacher")
public class GroupsOnClass3 {

    public void teacher1() {
        System.out.println("GroupsOnClass3的teacher1");
    }

    public void stu2(){
        System.out.println("GroupsOnClass3的teacher1");
    }

}
