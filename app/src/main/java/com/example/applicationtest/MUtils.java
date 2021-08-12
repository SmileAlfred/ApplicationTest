package com.example.applicationtest;

import java.util.UUID;

public class MUtils {
    public static String generateNewUUId() {
        return UUID.randomUUID().toString();
    }

    public static String method() {
        voidArgMethod("Er",2);
        voidMethod();
        return anotherMethod();
    }

    private static void voidArgMethod(String name,int age) {
        throw new RuntimeException();
    }

    private static void voidMethod() {
        throw new RuntimeException(); // logic was replaced with exception.
    }

    private static String anotherMethod() {
        throw new RuntimeException(); // logic was replaced with exception.
    }

}
