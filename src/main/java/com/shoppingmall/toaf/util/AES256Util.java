package com.shoppingmall.toaf.util;

import java.security.InvalidAlgorithmParameterException;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.security.spec.AlgorithmParameterSpec;
import javax.crypto.BadPaddingException;
import javax.crypto.Cipher;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.SecretKeySpec;

import org.apache.commons.codec.binary.Base64;
/**
 * @AES256 : 대칭 키 암호화 알고리즘, 키의 길이가 256비트
 *  - 과정 : 초기 백터 XOR 평문 --> 그 전 평문 XOR 평문 
 *  - 특징 : 동일한 평문에 대해 서로 다른 암호문이 생성
 *    [고정 벡터 ] : 동일한 데이터를 암호화할 때 동일한 암호문이 생성될 가능성이 높아짐
 *  	[무작위 초기 벡터]		 	SecureRandom random = new SecureRandom();
													byte[] iv = new byte[16]; // 16바이트 크기의 IV
													random.nextBytes(iv);
 * */

public class AES256Util {
  // 16 BYTE 크기의 배열(0이 128개)를 정의 -> 초기화 고정 백터로 사용 
	public static byte[] ivBytes = { 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00,
			0x00, 0x00 }; 
	
	public static String key = "12345678901234567890123456789012"; //12345678901234567890123456789012

	/**
	 * 일반 문자열을 지정된 키를 이용하여 AES256 으로 암호화 @param String - 암호화 대상 문자열 @param String -
	 * 문자열 암호화에 사용될 키 @return String - key 로 암호화된 문자열 @exception
	 * @Cipher 객체 : 주어진 데이터를 암호화 및 복호화 기능 
	 * @AES: 고급 암호화 표준(Advanced Encryption Standard), 대칭 키 알고리즘.
	 * @CBC(Cipher Block Chain) 모드: 각 블록 암호화 시 '이전 블록의 암호화된 결과'를 사용하는 암호화 모드.
	 * @PKCS5Padding: 데이터 블록의 크기를 맞추기 위한 패딩 방식.
	 */
	public static String strEncode(String str)
			throws java.io.UnsupportedEncodingException, NoSuchAlgorithmException, NoSuchPaddingException,
			InvalidKeyException, InvalidAlgorithmParameterException, IllegalBlockSizeException, BadPaddingException {
		Cipher cipher = null;
		byte[] textBytes = str.getBytes("UTF-8");
		try {
			
		AlgorithmParameterSpec ivSpec = new IvParameterSpec(ivBytes);  //Initialiization Vector 
		SecretKeySpec newKey = new SecretKeySpec(key.getBytes("UTF-8"), "AES"); // 이 비밀 키가 AES 알고리즘에서 사용될 것임을 지정
		//AES 알고리즘을 사용하여 CBC 모드에서 PKCS5 패딩을 적용하는 Cipher 객체를 생성
		cipher = Cipher.getInstance("AES/CBC/PKCS5Padding");
		cipher.init(Cipher.ENCRYPT_MODE, newKey, ivSpec);
		}catch(Exception e) {	
			e.printStackTrace();
		}
		//Base64는 바이너리데이터를 텍스트로 인코딩  
		return Base64.encodeBase64String(cipher.doFinal(textBytes));
	}

	/**
	 * 암호화된 문자열을 지정된 키를 이용하여 AES256 으로 복호화 @param String - 복호화 대상 문자열 @param String
	 * - 문자열 복호화에 사용될 키 @return String - key 로 복호화된 문자열 @exception
	 */
	public static String strDecode(String str)
			throws java.io.UnsupportedEncodingException, NoSuchAlgorithmException, NoSuchPaddingException,
			InvalidKeyException, InvalidAlgorithmParameterException, IllegalBlockSizeException, BadPaddingException {

		byte[] textBytes = Base64.decodeBase64(str);
		// byte[] textBytes = str.getBytes("UTF-8");
		AlgorithmParameterSpec ivSpec = new IvParameterSpec(ivBytes);
		SecretKeySpec newKey = new SecretKeySpec(key.getBytes("UTF-8"), "AES");
		Cipher cipher = Cipher.getInstance("AES/CBC/PKCS5Padding");
		cipher.init(Cipher.DECRYPT_MODE, newKey, ivSpec);
		return new String(cipher.doFinal(textBytes), "UTF-8");
	}
}