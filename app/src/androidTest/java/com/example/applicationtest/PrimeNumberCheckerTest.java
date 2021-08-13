package com.example.applicationtest;

import android.support.v4.app.INotificationSideChannel;
import android.util.ArraySet;
import android.util.Log;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;

import java.util.Arrays;
import java.util.Collection;

/**
 * 2021-08-06 Unit tests for {@link PrimeNumberChecker}.
 * Junit 参数化测试:
 * https://www.jianshu.com/p/a3fa5d208c93
 */
@RunWith(Parameterized.class)
public class PrimeNumberCheckerTest {

    private int inputNumber;
    private boolean expectedRes;
    private PrimeNumberChecker mPrimeNumberChecker;

    @Before
    public void setup() {
        mPrimeNumberChecker = new PrimeNumberChecker();
    }

    public PrimeNumberCheckerTest(int inputNumber, boolean expectedRes) {
        this.inputNumber = inputNumber;
        this.expectedRes = expectedRes;
    }

    @Parameterized.Parameters
    public static Collection primeNumbers() {
        return Arrays.asList(new Object[][]{
                {2, true}, {6, false}, {19, true}, {22, false}});
    }


    @Test
    public void validate() {
        Log.e("\tTAG\t", "inputNumber = "+inputNumber);
        Assert.assertEquals(expectedRes,mPrimeNumberChecker.validate(inputNumber));
    }
}