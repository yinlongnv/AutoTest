package com.dadalong.testng.suite;

import org.testng.annotations.Test;

public class SearchTest {

    @Test
    public void searchSuccess() {
        System.out.println("搜索成功");
    }
    @Test
    public void searchFail() {
        System.out.println("搜索失败");
    }

}
