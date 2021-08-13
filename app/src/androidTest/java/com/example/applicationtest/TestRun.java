package com.example.applicationtest;


import android.util.Log;

import org.junit.Test;
import org.junit.runner.JUnitCore;
import org.junit.runner.Result;
import org.junit.runner.notification.Failure;

/**
 * 2021-08-06 Unit tests for {@link PrimeNumberChecker}.
 * Junit 参数化测试:
 * https://www.jianshu.com/p/a3fa5d208c93
 */
public class TestRun {
    @Test
    public void test(){
        Result result = JUnitCore.runClasses(PrimeNumberCheckerTest.class);
        for(Failure failure:result.getFailures()){
            Log.e("\tTAG\t", failure.toString() );
        }
        Log.e("\tTAG\t", "参数化测试结果："+result.wasSuccessful() );
    }
}
