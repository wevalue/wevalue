/*******************************************************************************
 * Copyright (C) 2009-2010 eoeMobile. 
 * All rights reserved.
 * http://www.eoeMobile.com/
 * 
 * CHANGE LOG:
 *  DATE			AUTHOR			COMMENTS
 * =============================================================================
 *  2010MAY11		Waznheng Ma		Refine for Constructor and error handler.
 *
 *******************************************************************************/

package com.wevalue.utils;

import java.io.FileInputStream;
import java.security.DigestInputStream;
import java.security.MessageDigest;
public final class MD5 {

	/**
     * 通过MD5加密字符串 
     * @param HASH_ALGORITHM 
     *                  加密算法，可以是MD5，SHA1 
     * @param input 
     *                  字符串 
     * @return 已经通过MD5算法加密的32位字符串 
     */  
    public final static String stringToMD5(String input) {  
        try {  
            // 拿到一个MD5转换器，如果想要SHA1参数换成”SHA1”）  
            MessageDigest messageDigest = MessageDigest.getInstance("MD5");  
            // 输入的字符串转换成字节数组  
            byte[] inputByteArray = input.getBytes();  
            // inputByteArray是输入字符串转换得到的字节数组  
            messageDigest.update(inputByteArray);  
            // 转换并返回结果，也是字节数组，包含16个元素  
            byte[] resultByteArray = messageDigest.digest();// MD5 的计算结果是一个 128 位的长度整数，   
            // 字符数组转换成字符串返回  
            return byteArrayToHex(resultByteArray).toLowerCase();  
        } catch (Exception e) {  
            e.printStackTrace();  
        }  
  
        return null;  
    }  
      
    /** 
     * 通过MD5加密文件 
     * @param inputFile 文件路径 
     * @return 已经通过MD5算法加密的32位字符串 
     */  
    public final static String fileToMD5(String inputFile) {  
        int bufferSize = 256 * 1024;// 定义缓冲区大小  
        FileInputStream fileInputStream = null;  
        DigestInputStream digestInputStream = null;  
        try {  
            MessageDigest messageDigest = MessageDigest.getInstance("MD5");  
            fileInputStream = new FileInputStream(inputFile);  
            digestInputStream = new DigestInputStream(fileInputStream, messageDigest);  
            byte[] buffer = new byte[bufferSize];  
            while (digestInputStream.read(buffer) > 0)  
                ;  
            messageDigest = digestInputStream.getMessageDigest();  
            byte[] resultByteArray = messageDigest.digest();  
            return byteArrayToHex(resultByteArray);  
        } catch (Exception e) {  
        } finally {  
            try {  
                digestInputStream.close();  
            } catch (Exception e2) {  
            }  
        }  
        return null;  
    }  
      
    /** 
     * @param byteArray 
     *                  byte数组 
     * @return 转后后的32位的字符串 
     */  
    private static String byteArrayToHex(byte[] byteArray) {  
        // 首先初始化一个字符数组，用来存放每个16进制字符  
        char[] hexDigits = { '0', '1', '2', '3', '4', '5', '6', '7', '8', '9', 'A', 'B', 'C', 'D', 'E', 'F' };  
  
        // new一个字符数组，这个就是用来组成结果字符串的（解释一下：一个byte是八位二进制，也就是2位十六进制字符（2的8次方等于16的2次方））  
        char[] resultCharArray = new char[byteArray.length * 2];// 每个字节用 16 进制表示的话，使用两个字符  
  
        // 遍历字节数组，通过位运算（位运算效率高），转换成字符放到字符数组中去  
        int index = 0;  
        // 从第一个字节开始，对MD5的每一个字节转换成16进制字符的转换  
        for (byte b : byteArray) {  
            // 取字节中高 4 位的数字转换  
            //>>> 为逻辑右移（即无符号右移），将符号位一起右移  
            resultCharArray[index++] = hexDigits[b >>> 4 & 0xf];  
            // 取字节中低4位的数字转换   
            resultCharArray[index++] = hexDigits[b & 0xf];  
        }  
        // 字符数组组合成字符串返回  
        return new String(resultCharArray);  
    } 
    
    
    public final static String setMD5(String str) {
		char hexDigits[] = { // 用来将字节转换成 16 进制表示的字符
		'0', '1', '2', '3', '4', '5', '6', '7', '8', '9', 'a', 'b', 'c', 'd',
				'e', 'f' };
		try {
			byte[] strTemp = str.getBytes();
			MessageDigest mdTemp = MessageDigest.getInstance("MD5");
			mdTemp.update(strTemp);
			byte tmp[] = mdTemp.digest(); // MD5 的计算结果是一个 128 位的长整数，
			// 用字节表示就是 16 个字节
			char strs[] = new char[16 * 2]; // 每个字节用 16 进制表示的话，使用两个字符，
			// 所以表示成 16 进制需要 32 个字符
			int k = 0; // 表示转换结果中对应的字符位置
			for (int i = 0; i < 16; i++) { // 从第一个字节开始，对 MD5 的每一个字节
				// 转换成 16 进制字符的转换
				byte byte0 = tmp[i]; // 取第 i 个字节
				strs[k++] = hexDigits[byte0 >>> 4 & 0xf]; // 取字节中高 4 位的数字转换,
				// >>> 为逻辑右移，将符号位一起右移
				strs[k++] = hexDigits[byte0 & 0xf]; // 取字节中低 4 位的数字转换
			}
			return new String(strs).toUpperCase(); // 换后的结果转换为字符串
		} catch (Exception e) {
			return null;
		}
	}
}
