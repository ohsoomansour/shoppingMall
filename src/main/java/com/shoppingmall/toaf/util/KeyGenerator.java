package com.shoppingmall.toaf.util;

import java.util.UUID;

public class KeyGenerator {
	public static String generateKey() {
		return UUID.randomUUID().toString();
	}
}
