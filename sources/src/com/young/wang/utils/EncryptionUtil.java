package com.young.wang.utils;

import java.security.MessageDigest;

import javax.crypto.Cipher;
import javax.crypto.spec.SecretKeySpec;

import org.apache.commons.codec.binary.Base64;

/** 
 * |
 * |                       _oo0oo_
 * |                      o8888888o
 * |                      88" . "88
 * |                      (| -_- |)
 * |                      0\  =  /0
 * |                    ___/`---'\___
 * |                  .' \\|  南  |// '.
 * |                 / \\|||  无  |||// \
 * |                / _||||| -阿- |||||- \
 * |               |   | \\\  弥  /// |   |
 * |               | \_|  ''\-陀-/''  |_/ |
 * |               \  .-\__  '佛'  ___/-. /
 * |             ___'. .'  /--.--\  `. .'___
 * |          ."" '<  `.___\_<|>_/___.' >' "".
 * |         | | :  `- \`.;`\ _ /`;.`/ - ` : | |
 * |         \  \ `_.   \_ __\ /__ _/   .-` /  /
 * |     =====`-.____`.___ \_____/___.-`___.-'=====
 * |                       `=---='
 * |
 * |     ~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
 * |
 * |			佛祖保佑	 永无BUG	 永不修改
 * |
 *
 * Created by Young Wang on 2015年11月5日 上午10:56:59.
 *
 */
public final class EncryptionUtil {
	private EncryptionUtil(){}
	
	/**
	 * MD5加密
	 */
	public static String md5Encript(String text){
        if(text==null) text = "";
        try {
            MessageDigest md = MessageDigest.getInstance("MD5");
            byte[] enc = md.digest(text.getBytes("UTF-8"));
            return new String(Base64.encodeBase64(enc), "UTF-8");
        } catch (Exception e) {
            throw new RuntimeException(e.getMessage());
        }
    }

    /**
     * DES3加密
     */
    public static String DES3Encript(String pText,String key){
        final String Algorithm = "DESede";
        if(pText==null) pText="";
        try{
            byte[] keyByte = get3DESKey(key);
            SecretKeySpec k = new SecretKeySpec(keyByte,Algorithm);
            Cipher cp = Cipher.getInstance(Algorithm);
            cp.init(Cipher.ENCRYPT_MODE, k);
            byte[] cByte = cp.doFinal(pText.getBytes("UTF-8"));
            return new String(Base64.encodeBase64(cByte), "UTF-8");
        }catch (Exception e){
            throw new RuntimeException(e.getMessage());
        }
    }
    
    /**
     * 注意，解密只适用本类DES3Encript加密的密文
     */
    public static String DES3Decrypt(String cText,String key){
        final String Algorithm = "DESede";
        if(cText==null) return "";
        try{
            byte[] keyByte = get3DESKey(key);
            SecretKeySpec k = new SecretKeySpec(keyByte,Algorithm);
            Cipher cp = Cipher.getInstance(Algorithm);
            cp.init(Cipher.DECRYPT_MODE, k);
            byte[] pByte = cp.doFinal(Base64.decodeBase64(cText.getBytes("UTF-8")));
            return new String(pByte, "UTF-8");
        }catch (Exception e){
            throw new RuntimeException(e.getMessage());
        }
    }

    /**
     * 将传进去的key计算为长度为24的byte数组，适用DES3加密的key
     */
    public static byte[] get3DESKey(String key){
        try {
            if(key==null) key="";
            byte[] keyBytes = key.getBytes("UTF-8");
            int keyBytesLength = keyBytes.length;
            if(keyBytesLength<24){
                byte[] result = new byte[24];
                System.arraycopy(keyBytes,0,result,0,keyBytesLength);
                for (int i = keyBytesLength; i < result.length; i++) {
                    result[i]=99;
                }
                return result;
            }else if(keyBytesLength==24){
                return keyBytes;
            }else{
                byte[] result = new byte[24];
                int i = (int)Math.ceil(keyBytesLength/24.0);
                System.arraycopy(keyBytes,0,result,0,24);
                for (int j = 1; j < i; j++) {
                    int start = j*24;
                    for (int k = 0; k < result.length; k++) {
                        int index = start+k;
                        if(index<keyBytesLength){
                            result[k] = (byte)(result[k] + keyBytes[index]);
                        }else{
                            break;
                        }
                    }
                }
                return result;
            }
        } catch (Exception e) {
            throw new RuntimeException(e.getMessage());
        }
    }
}
