package com.dadalong.testng.paramter;

import org.testng.annotations.DataProvider;
import org.testng.annotations.Test;

import java.lang.reflect.Method;

public class DataProviderTest {

    @Test(dataProvider = "persons1")
    public void dataProviderTest1(String name, int age) {
        System.out.println("name = " + name + " and age = " + age);
    }

    @DataProvider(name = "persons1")
    public Object[][] dataProvider1() {
        Object[][] objects = new Object[][] {
                {"dadalong1",22},
                {"zhangsan1",18},
                {"lisi1",10}
        };
        return objects;
    }

    @Test(dataProvider = "persons2")
    public void dataProviderTest2(String name, int age) {
        System.out.println("name = " + name + " and age = " + age);
    }

    @Test(dataProvider = "persons2")
    public void dataProviderTest3(String name, int age) {
        System.out.println("name = " + name + " and age = " + age);
    }

    @DataProvider(name = "persons2")
    public Object[][] dataProvider(Method method) {
        Object[][] objects = null;
        if (method.getName().equals("dataProviderTest2")) {
            objects = new Object[][] {
                    {"dadalong2",22},
                    {"zhangsan2",18},
                    {"lisi2",10}
            };
        } else if (method.getName().equals("dataProviderTest3")) {
            objects = new Object[][] {
                    {"dadalong3",22},
                    {"zhangsan3",18},
                    {"lisi3",10}
            };
        }
        return objects;
    }

}
