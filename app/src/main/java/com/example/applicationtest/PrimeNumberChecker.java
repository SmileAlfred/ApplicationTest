package com.example.applicationtest;

/**
 * Junit 参数化测试:
 * https://www.jianshu.com/p/a3fa5d208c93
 */
public class PrimeNumberChecker {
    public boolean validate(final int primeNumber) {
        for (int i = 2; i < (primeNumber / 2); i++) {
            if (primeNumber % i == 0) {
                return false;
            }
        }
        return true;
    }
}
