package com.my.utils;

import java.util.UUID;

public class UUIDUtil {

    public static String getUUIDStr() {
        return UUID.randomUUID().toString().replace("-", "");
    }
    public static void main(String[] args) {
        for (int i = 0; i < 6; i++) {
            System.out.println(UUIDUtil.getUUIDStr());

        }
    }
}


