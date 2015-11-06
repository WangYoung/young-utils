package com.young.wang.utils;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.lang.reflect.Array;
import java.lang.reflect.Type;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;
import java.util.HashMap;
import java.util.Map;

import org.apache.commons.io.IOUtils;
import org.apache.commons.lang.StringUtils;

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
 * Created by Young Wang on 2015年11月5日 上午11:21:48.
 *
 */
public class HttpUtil {
	private static final String _BOUNDARY = "----------_BOUNDARY.Young.Wang";
	
	private Map<String,String> headerMap = new HashMap<String,String>();
	
	/**
	 * 下载网页到String
	 * @param url url
	 * @param charset 编码，默认UTF-8
	 * @throws java.io.IOException
	 */
	public String getUrlDataToString(String url,String charset) throws IOException{
		BufferedInputStream cin = null;
		String result = "";
		try{
			cin = getUrlDataInputStream(url);
			result = IOUtils.toString(cin,charset==null || charset.isEmpty()?"UTF-8":charset);
		}finally{
			if(cin!=null){
				cin.close();
			}
		}
		return result;
	}
	
	/**
	 * 下载网页到byte[]
	 * @param url url
	 * @throws java.io.IOException
	 */
	public byte[] getUrlDataToByte(String url) throws IOException{
		BufferedInputStream cin = null;
		byte[] result = null;
		try{
			cin = getUrlDataInputStream(url);
			result = IOUtils.toByteArray(cin);
		}finally{
			if(cin!=null){
				cin.close();
			}
		}
		return result;
	}
	
	/**
	 * 下载网页到文件
	 * @param url url
	 * @param file 文件全路径
	 * @throws java.io.IOException
	 */
	public void getUrlDataToFile(String url,String file) throws IOException{
		BufferedInputStream cin = null;
        BufferedOutputStream bos = null;
		try{
			cin = getUrlDataInputStream(url);
            bos = new BufferedOutputStream(new FileOutputStream(new File(file)));
            IOUtils.copy(cin,bos);
            bos.flush();
		}finally{
			if(cin!=null){
				cin.close();
			}
            if(bos!=null){
                bos.close();
            }
		}
	}
	
	/**
	 * post提交http请求，并将返回数据接收到String
	 * @param url url
	 * @param params 参数
	 * @param charset 编码，默认UTF-8
	 * @throws java.io.IOException
	 */
	public String postUrlDataToString(String url,Map<String,Object> params,String charset) throws IOException{
		BufferedInputStream cin = null;
		String result = "";
		try{
			cin = postUrlDataInputStream(url, params);
			result = IOUtils.toString(cin,charset==null || charset.isEmpty()?"UTF-8":charset );
		}finally{
			if(cin!=null){
				cin.close();
			}
		}
		return result;
	}
	
	/**
	 * post提交http请求，并将返回数据接收到byte[]
	 * @param url url
	 * @param params 参数
	 * @throws java.io.IOException
	 */
	public byte[] postUrlDataToByte(String url,Map<String,Object> params) throws IOException{
		BufferedInputStream cin = null;
		byte[] result = null;
		try{
			cin = postUrlDataInputStream(url, params);
			result = IOUtils.toByteArray(cin);
		}finally{
			if(cin!=null){
				cin.close();
			}
		}
		return result;
	}
	
	/**
	 * post提交http请求，并将返回数据接收到文件
	 * @param url url
	 * @param params 参数
	 * @param file 文件全路径
	 * @throws java.io.IOException
	 */
	public void postUrlDataToFile(String url,Map<String,Object> params,String file) throws IOException{
		BufferedInputStream cin = null;
        BufferedOutputStream bos = null;
		try{
			cin = postUrlDataInputStream(url, params);
            bos = new BufferedOutputStream(new FileOutputStream(new File(file)));
            IOUtils.copy(cin, bos);
            bos.flush();
		}finally{
			if(cin!=null){
				cin.close();
			}
            if(bos!=null){
                bos.close();
            }
		}
	}
	
	/**
	 * 用get方法请求http，并返回输入流
	 * @throws java.io.IOException
	 */
	public BufferedInputStream getUrlDataInputStream(String url) throws IOException{
		HttpURLConnection httpConn = getGeneralHttpCon(url);
		httpConn.connect();
		return new BufferedInputStream(httpConn.getInputStream());
	}
	
	/**
	 * 用post提交Http请求，并返回输入流
	 * @param url url
	 * @param params 参数
	 * @throws java.io.IOException
	 */
	public BufferedInputStream postUrlDataInputStream(String url,Map<String,Object> params) throws IOException{
        if(this.mapContainsValueType(params,byte[].class)) return this.postUrlDataInputStreamByMultipart(url,params);
        return this.postUrlDataInputStreamByNormal(url,params);
	}

	/**
	 * 文件上传方式提交post
	 * @param url url
	 * @param params 参数
	 * @return
	 * @throws IOException
	 */
    public BufferedInputStream postUrlDataInputStreamByMultipart(String url,Map<String,Object> params) throws IOException{
        HttpURLConnection httpConn = getGeneralHttpCon(url);
        httpConn.setRequestMethod("POST");
        httpConn.setRequestProperty("Content-Type","multipart/form-data; boundary=" + _BOUNDARY);
        httpConn.setDoOutput(true);
        httpConn.setDoInput(true);
        BufferedOutputStream out = null;
        BufferedInputStream is = null;
        try{
            if(params != null && !params.isEmpty()){
                StringBuilder sb = new StringBuilder();
                out = new BufferedOutputStream(httpConn.getOutputStream());

                for(Map.Entry<String, Object> entry : params.entrySet()){
                    String key = entry.getKey();
                    Object value = entry.getValue();
                    if(value==null)continue;
                    if(value.getClass()==byte[].class){
                        String name = key;
                        String filename = "default_file_name";
                        if(key.contains(":")){
                            String[] names = key.split(":");
                            name = names[0];
                            if(!StringUtils.isEmpty(names[1]))filename = names[1];
                        }
                        StringBuilder s = new StringBuilder();
                        s.append("\r\n--").append(_BOUNDARY).append("\r\n");
                        s.append("Content-Disposition: form-data; name=\"").append(name).append("\";filename=\"").append(filename).append("\"\r\n");
                        s.append("Content-Type: application/octet-stream\r\n\r\n");
                        IOUtils.write(s, out, "UTF-8");
                        out.write((byte[])value);
                    }else if(value.getClass().isArray()) {
                        int arraySize = Array.getLength(value);
                        for(int i=0; i<arraySize; i++){
                            Object o = Array.get(value,i);
                            sb.append("\r\n--").append(_BOUNDARY).append("\r\n");
                            sb.append("Content-Disposition: form-data; name=\"").append(key).append("\"\r\n\r\n");
                            sb.append(o.toString());
                        }
                    } else {
                        sb.append("\r\n--").append(_BOUNDARY).append("\r\n");
                        sb.append("Content-Disposition: form-data; name=\"").append(key).append("\"\r\n\r\n");
                        sb.append(value.toString());
                    }
                }
                sb.append("\r\n--").append(_BOUNDARY).append("--\r\n");
                IOUtils.write(sb,out,"UTF-8");
                out.flush();
            }
            is = new BufferedInputStream(httpConn.getInputStream());
        }finally{
            if(out != null){
                out.close();
            }
        }
        return is;
    }

    /**
	 * 非文件上传方式提交post
	 * @param url url
	 * @param params 参数
	 * @return
	 * @throws IOException
	 */
    public BufferedInputStream postUrlDataInputStreamByNormal(String url,Map<String,Object> params) throws IOException{
        HttpURLConnection httpConn = getGeneralHttpCon(url);
        httpConn.setRequestMethod("POST");
        httpConn.setDoOutput(true);
        httpConn.setDoInput(true);
        BufferedOutputStream out = null;
        BufferedInputStream is = null;
        try{
            if(params != null && !params.isEmpty()){
            	StringBuilder sb = new StringBuilder();
                for(Map.Entry<String, Object> entry : params.entrySet()){
                    String key = entry.getKey();
                    Object value = entry.getValue();
                    if(value==null)continue;
                    if(value.getClass().isArray()) {
                        int arraySize = Array.getLength(value);
                        for(int i=0; i<arraySize; i++){
                            Object o = Array.get(value,i);
                            sb.append(URLEncoder.encode(key,"UTF-8")).append("=").append(URLEncoder.encode(o.toString(),"UTF-8")).append("&");
                        }
                    } else {
                        sb.append(URLEncoder.encode(key,"UTF-8")).append("=").append(URLEncoder.encode(value.toString(),"UTF-8")).append("&");
                    }
                }
                if(sb.length()!=0){
                    sb = sb.deleteCharAt(sb.length()-1);
                }
                out = new BufferedOutputStream(httpConn.getOutputStream());
                IOUtils.write(sb,out,"UTF-8");
                out.flush();
            }
            is = new BufferedInputStream(httpConn.getInputStream());
        }finally{
            if(out != null){
                out.close();
            }
        }
        return is;
    }

    private boolean mapContainsValueType(Map<?,?> map,Type type){
        if(type==null)return false;
        if(map==null || map.isEmpty())return false;
        for(Map.Entry<?,?> entry : map.entrySet()){
            if(entry.getValue()!=null && entry.getValue().getClass()==type){
                return true;
            }
        }
        return false;
    }

	/**
	 * 添加http请求头
	 */
	public void addHeader(String key,String value){
		this.headerMap.put(key,value);
	}

    public Map<String,String> getHeaderMap(){
        return this.headerMap;
    }
	
	/**
	 * 获得HttpURLConnection，并进行通用设置
	 * @param url	url
	 * @throws java.io.IOException
	 */
	public HttpURLConnection getGeneralHttpCon(String url) throws IOException{
		URL u = new URL(url);
        HttpURLConnection httpConn = (HttpURLConnection) u.openConnection();
        httpConn.setRequestProperty("accept", "*/*");
        httpConn.setRequestProperty("connection", "Keep-Alive");
        httpConn.setRequestProperty("user-agent", "Mozilla/4.0 (compatible; MSIE 6.0; Windows NT 5.1;SV1)");
        for(String key : this.headerMap.keySet()){
            if(this.headerMap.get(key)!=null){
                httpConn.setRequestProperty(key, this.headerMap.get(key));
            }
        }
        return httpConn;
	}
}
