package com.primefaces.pusher.auth;

import java.io.UnsupportedEncodingException;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;

import javax.crypto.Mac;
import javax.crypto.spec.SecretKeySpec;

public class HMACDigest {
	
	public static String HMACDigestSHA256(String msg, String keyString) {
		return HMACDigest(msg, keyString, "HmacSHA256");
	}
	
	public static String HMACDigest(String msg, String keyString, String algo) {
		String digest = null;
		
		try {
			
			SecretKeySpec key = new SecretKeySpec((keyString).getBytes("UTF-8"), algo);
			Mac mac = Mac.getInstance(algo);
			mac.init(key);
			
			byte[] bytes = mac.doFinal(msg.getBytes("ASCII"));
			
			StringBuffer hash = new StringBuffer();
			for (int i = 0; i < bytes.length; i++) {
				String hex = Integer.toHexString(0xFF & bytes[i]);
				if (hex.length() == 1) {
					hash.append('0');
				}
				hash.append(hex);
			}
			digest = hash.toString();
		} catch (UnsupportedEncodingException e) {
		} catch (InvalidKeyException e) {
		} catch (NoSuchAlgorithmException e) {
		}
		
		return digest;
	}
}
