package com.xmn.saas.utils;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.security.InvalidAlgorithmParameterException;
import java.security.InvalidKeyException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.security.spec.InvalidKeySpecException;

import javax.crypto.BadPaddingException;
import javax.crypto.Cipher;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;
import javax.crypto.SecretKey;
import javax.crypto.SecretKeyFactory;
import javax.crypto.spec.PBEKeySpec;
import javax.crypto.spec.PBEParameterSpec;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import sun.misc.BASE64Decoder;
import sun.misc.BASE64Encoder;
/**
 * 
 * @ClassName:     CryptoUtil
 * @Description:TODO
 * @author:    DQ
 * @date:        2016-9-20 下午3:56:48
 *
 */
@SuppressWarnings("all")
public class CryptoUtil {
	
	private static final Logger logger = LoggerFactory.getLogger(CryptoUtil.class.getName());

	public static final String KEY_SHA = "SHA";

	public static final String KEY_MD5 = "MD5";

	public static final String AES_KEY_DEFAULT = "AES";

	public static final String AES_CBC_PKCS7_PADDING = "AES/CBC/PKCS7Padding";

	public static final String PBE_KEY = "PBEWithSHA1AndDESede";

	public static final byte[] SECRET_KEY = new byte[] { 1, 2, 3, 4, 5, 6, 7, 8, 9, 10, 11, 12, 13, 14, 15, 16 };

	public static final String KEY_DEFAULT = "88pingtai.com";

	private static BASE64Encoder encoder = new BASE64Encoder();

	private static BASE64Decoder decoder = new BASE64Decoder();

	/**
	 * 加密
	 * @Author:DQ
	 * @Date:Dec 21, 2016
	 * @Title: pbeEncrypt
	 * @Description: TODO
	 * @param: @param plaintext
	 * @param: @param key
	 * @param: @return
	 * @return: String
	 * @throws
	 */
	public static String pbeEncrypt(String plaintext, String key)throws Exception {
		try {
			Cipher c = pbeCipher(Cipher.ENCRYPT_MODE, key);
			byte[] cipherText = c.doFinal(plaintext.getBytes("UTF-8"));
			logger.info(encoder.encode(cipherText));
			return encoder.encode(cipherText);
		} catch (IllegalBlockSizeException e) {
			e.printStackTrace();
		} catch (BadPaddingException e) {
			e.printStackTrace();
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
		}
		return null;
	}

	/**
	 * 解密
	 * @Author:DQ
	 * @Date:Dec 21, 2016
	 * @Title: pbeDecrypt
	 * @Description: TODO
	 * @param: @param cipherText
	 * @param: @param key
	 * @param: @return
	 * @return: String
	 * @throws
	 */
	public static String pbeDecrypt(String cipherText, String key)throws Exception {

		try {
			Cipher c = pbeCipher(Cipher.DECRYPT_MODE, key);

			byte[] cipher = decoder.decodeBuffer(cipherText);

			byte[] plainText = c.doFinal(cipher);
			logger.info(new String(plainText, "UTF-8"));
			return new String(plainText, "UTF-8");
		} catch (IllegalBlockSizeException e) {
			e.printStackTrace();
		} catch (BadPaddingException e) {
			e.printStackTrace();
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}

		return null;
	}

	public static Cipher pbeCipher(Integer cipherType, String key)throws Exception {

		Cipher cipher = null;

		if (key==null || null=="") {
			key = KEY_DEFAULT;
		}

		try {
			int iterCount = 1000;
			char[] pwd = key.toCharArray();
			byte[] salt = new byte[8];

			PBEKeySpec pks = new PBEKeySpec(pwd);
			SecretKeyFactory skf = SecretKeyFactory.getInstance(PBE_KEY);
			SecretKey pbeKey = skf.generateSecret(pks);

			// byte[] iv = encryptMd5(key, 16).getBytes();
			// IvParameterSpec ivP = new IvParameterSpec(iv);
			PBEParameterSpec pbeParams = new PBEParameterSpec(salt, iterCount);
			cipher = Cipher.getInstance(PBE_KEY);
			cipher.init(cipherType, pbeKey, pbeParams);

		} catch (NoSuchAlgorithmException e) {
			e.printStackTrace();
		} catch (InvalidKeySpecException e) {
			e.printStackTrace();
		} catch (NoSuchPaddingException e) {
			e.printStackTrace();
		} catch (InvalidKeyException e) {
			e.printStackTrace();
		} catch (InvalidAlgorithmParameterException e) {
			e.printStackTrace();
		}
		return cipher;
	}

	public static String encryptMd5(String plaintext, Integer numBit)throws Exception {
		String result = plaintext;
		try {

			MessageDigest md5 = MessageDigest.getInstance(KEY_MD5);

			byte[] info = plaintext.getBytes("UTF-8");

			md5.update(info);

			byte[] data = md5.digest();

			int d;

			String sb = "";

			for (int i = 0; i < data.length; i++) {
				d = data[i];
				if (d < 0) {
					d += 256;
				}
				if (d < 16) {
					sb += "0";
				}
				sb += Integer.toHexString(d);
			}

			if (null != numBit && 16 == numBit.intValue()) {
				result = sb.substring(8, 24);
			} else {
				result = sb;
			}

		} catch (NoSuchAlgorithmException e) {
			e.printStackTrace();
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
		}
		logger.info(result);
		return result;
	}

	public static void main(String[] args) {
		try {
			String Bcode = "24B3D5E4A810A4EC97B12F8C04EA3E42";
			String str = "大中华人们世界English0102_212";
			String key =  Bcode;
			String cipherText = CryptoUtil.pbeEncrypt(str, key);
			System.out.println("CipherText: " + cipherText);
			System.out.println("PlainText: " + CryptoUtil.pbeDecrypt(cipherText, key));
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

}