package com.young.wang.utils;

import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.net.URLEncoder;
import java.util.HashMap;
import java.util.Map;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

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
 * Created by Young Wang on 2015年11月5日 上午10:44:28.
 *
 */
public final class CookieUtil {
	private CookieUtil(){}
    /**
     * 通过name得到cookie的值
     */
    public static String getCookieValueByName(HttpServletRequest request,String name){
        Cookie cookie = getCookieByName(request,name);
        if(cookie!=null){
            try {
                return URLDecoder.decode(cookie.getValue(),"UTF-8");
            } catch (UnsupportedEncodingException e) {
                throw new RuntimeException(e.getMessage());
            }
        }else{
            return null;
        }
    }

    /**
     * 通过name得到cookie
     */
    public static Cookie getCookieByName(HttpServletRequest request,String name){
        Map<String,Cookie> cookieMap = ReadCookieMap(request);
        if(cookieMap.containsKey(name)){
            Cookie cookie = cookieMap.get(name);
            return cookie;
        }else{
            return null;
        }
    }

    /**
     * 将cookie读取到map里
     * @param request
     * @return
     */
    private static Map<String,Cookie> ReadCookieMap(HttpServletRequest request){
        Map<String,Cookie> cookieMap = new HashMap<String,Cookie>();
        Cookie[] cookies = request.getCookies();
        if(null!=cookies){
            for(Cookie cookie : cookies){
                cookieMap.put(cookie.getName(), cookie);
            }
        }
        return cookieMap;
    }

    /**
     * 设置一个cookie
     * @param response 
     * @param name cookie name
     * @param value cookie value
     * @param maxAge cookie有效期，单位秒。0表示立即失效，即删除。
     */
    public static void addCookie(HttpServletResponse response,String name,String value,int maxAge){
        try {
            Cookie cookie = new Cookie(name, URLEncoder.encode(value, "UTF-8"));
            cookie.setPath("/");
            if(maxAge>0)  cookie.setMaxAge(maxAge);
            response.addCookie(cookie);
        } catch (UnsupportedEncodingException e) {
            throw new RuntimeException(e.getMessage());
        }
    }

    /**
     * 删除一个cookie
     */
    public static void deleteCookie(HttpServletRequest request,HttpServletResponse response,String name){
        Cookie cookie = getCookieByName(request,name);
        if(cookie!=null){
            cookie.setPath("/");
            cookie.setMaxAge(0);
            response.addCookie(cookie);
        }
    }
}
