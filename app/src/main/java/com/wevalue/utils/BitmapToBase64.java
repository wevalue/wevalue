package com.wevalue.utils;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.util.Base64;

import java.io.ByteArrayOutputStream;
import java.io.IOException;

public class BitmapToBase64 {
	/**
	 *bitmap转base64
	 */
	public static String bitmapToBase64(Bitmap bitmap){
	 String result="";
	 ByteArrayOutputStream bos=null;
	 try {
	  if(null!=bitmap){
	   bos=new ByteArrayOutputStream();
	   bitmap.compress(Bitmap.CompressFormat.JPEG, 90, bos);//将bitmap放入字节数组流中
	    
	   bos.flush();//将bos流缓存在内存中的数据全部输出，清空缓存
	   bos.close();
	    
	   byte []bitmapByte=bos.toByteArray();
	   result= Base64.encodeToString(bitmapByte, Base64.DEFAULT);
	  }
	 } catch (Exception e) {
	  e.printStackTrace();
	 }finally{
	  if(null!=null){
	   try {
	    bos.close();
	   } catch (IOException e) {
	    e.printStackTrace();
	   }
	  }
	 }
	 return result;
	}
	
	/**
	 *base64转bitmap
	 */
	public static Bitmap base64ToBitmap(String base64String){
	 byte[] bytes = Base64.decode(base64String, Base64.DEFAULT);
	 Bitmap bitmap= BitmapFactory.decodeByteArray(bytes, 0, bytes.length);
	 return bitmap;
	}
}
