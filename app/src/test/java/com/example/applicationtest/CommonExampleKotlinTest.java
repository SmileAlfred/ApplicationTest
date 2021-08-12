package com.example.applicationtest;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.powermock.api.mockito.PowerMockito;
import org.powermock.core.classloader.annotations.PrepareForTest;
import org.powermock.modules.junit4.PowerMockRunner;

@RunWith(PowerMockRunner.class)
//语句告诉PowerMock准备Employee类进行测试。适用于模拟final类或有final, private, static, native方法的类。
@PrepareForTest({MUtils.class, MainActivity.class})
public class CommonExampleKotlinTest {

    @Test
    public void printUUID() {
        CommonExampleKotlin commonExampleKotlin = new CommonExampleKotlin();
        PowerMockito.mockStatic(MUtils.class);
        PowerMockito.when(MUtils.generateNewUUId()).thenReturn("Return UUID");
        System.out.println(commonExampleKotlin.printUUID());
        assert ("Return UUID".equals(commonExampleKotlin.printUUID()));
    }
}