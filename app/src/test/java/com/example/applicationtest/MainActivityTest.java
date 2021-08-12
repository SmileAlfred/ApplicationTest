package com.example.applicationtest;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.powermock.api.mockito.PowerMockito;
import org.powermock.core.classloader.annotations.PrepareForTest;
import org.powermock.modules.junit4.PowerMockRunner;

import static org.junit.Assert.*;

@RunWith(PowerMockRunner.class)
//语句告诉PowerMock准备Employee类进行测试。适用于模拟final类或有final, private, static, native方法的类。
@PrepareForTest({MUtils.class, MainActivity.class})
public class MainActivityTest {

    @Test
    public void printUUID() {
        MainActivity mainActivity = new MainActivity();
        PowerMockito.mockStatic(MUtils.class);
        //PowerMockito.when(MUtils.generateNewUUId()).thenReturn("Return UUID");
        System.out.println(mainActivity.printUUID());
        assert("Return UUID".equals( mainActivity.printUUID()));
    }
}