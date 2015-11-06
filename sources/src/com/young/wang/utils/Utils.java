package com.young.wang.utils;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.Calendar;
import java.util.Collection;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import javax.mail.internet.MimeUtility;
import javax.servlet.http.HttpServletRequest;

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
 * Created by Young Wang on 2015年11月5日 下午12:11:09.
 *
 */
public final class Utils {
	private Utils(){}

	private final static String[] RMB_UNITS = new String[] { "零", "壹", "贰",
			"叁", "肆", "伍", "陆", "柒", "捌", "玖" };

	public static String toRmb(double value) {
		double decimal = (value - ((long) value)) * 100;
		long v1 = Math.round(decimal);

		long v = (long) value;
		if (v == 0 && v1 == 0)
			return "零元";
		if (v == 0 && v1 != 0)
			return decimal2Rmb(v1);

		StringBuffer sb = new StringBuffer();
		long d1 = v / 100000000L;
		long d2 = (v % 100000000L) / 10000L;
		long d3 = (v % 10000L);
		if (d1 > 0)
			sb.append(part2Rmb(d1) + "亿");
		if (d1 > 0) {
			if (d2 > 0 && d2 >= 1000)
				sb.append(part2Rmb(d2) + "万");
			else if (d2 > 0 && d2 < 1000)
				sb.append("零" + part2Rmb(d2) + "万");
			else if (d2 == 0 && d1 > 0)
				sb.append("零");
		} else {
			if (d2 > 0)
				sb.append(part2Rmb(d2) + "万");
		}
		if (sb.length() > 0) {
			if (d3 >= 1000) {
				sb.append(part2Rmb(d3));
			} else if (d3 > 0 && d3 < 1000) {
				if (sb.charAt(sb.length() - 1) == '零')
					sb.append(part2Rmb(d3));
				else
					sb.append("零" + part2Rmb(d3));
			}
		} else {
			sb.append(part2Rmb(d3));
		}

		if (v1 == 0)
			return sb.toString() + "元";
		else if (v1 < 10)
			return sb.toString() + "元" + "零" + decimal2Rmb(v1);
		else
			return sb.toString() + "元" + decimal2Rmb(v1);
	}

	private static String decimal2Rmb(long part) {
		if (part < 10)
			return RMB_UNITS[(int) part] + "分";
		else {
			StringBuffer sb = new StringBuffer();
			long d1 = part / 10;
			long d2 = part % 10;
			sb.append(RMB_UNITS[(int) d1] + "角");
			if (d2 > 0)
				sb.append(RMB_UNITS[(int) d2] + "分");
			return sb.toString();
		}
	}

	private static String part2Rmb(long part) {
		if (part < 0 || part > 9999)
			throw new IllegalArgumentException("0 - 9999");
		if (part == 0)
			return "";
		StringBuffer sb = new StringBuffer();
		long d1 = part / 1000;
		long d2 = (part % 1000) / 100;
		long d3 = (part % 100) / 10;
		long d4 = part % 10;
		if (d1 != 0)
			sb.append(RMB_UNITS[(int) d1] + "仟");
		if (d2 != 0)
			sb.append(RMB_UNITS[(int) d2] + "佰");
		else if (sb.length() > 0)
			sb.append("零");
		if (d3 != 0)
			sb.append(RMB_UNITS[(int) d3] + "拾");
		else if (sb.length() > 0 && sb.charAt(sb.length() - 1) != '零')
			sb.append("零");
		if (d4 != 0)
			sb.append(RMB_UNITS[(int) d4]);
		while (sb.charAt(sb.length() - 1) == '零') {
			sb.deleteCharAt(sb.length() - 1);
		}

		return sb.toString();

	}

	/**
	 * 编码下载文件名
	 */
	public static String encodeDownloadFileName(HttpServletRequest request,
			String fileName) {
		String result;
		try {
			String agent = request.getHeader("USER-AGENT");
			if (agent == null) {
				result = MimeUtility.encodeText(fileName, "UTF-8", "B");
			} else if (agent.contains("MSIE")) {
				result = URLEncoder.encode(fileName, "UTF-8").replaceAll("\\+",
						"%20");
			} else {
				result = MimeUtility.encodeText(fileName, "UTF-8", "B");
			}
		} catch (UnsupportedEncodingException e1) {
			result = fileName;
		}
		return result;
	}
	
	public static Map<String, Object> toMap(Object ... values) {
		HashMap<String, Object> map = new HashMap<String, Object>();
		for(int i = 0; i < values.length; i+=2) {
			map.put((String) values[i], values[i + 1]);
		}
		return map;
	}
	
	public static String collectionToStr(Collection<?> collection,String separate,String prefix,String suffix ){
        if(collection==null||collection.isEmpty())return "";
        if(separate==null) separate = ",";
        if(prefix==null) prefix = "";
        if(suffix==null) suffix = "";

        StringBuffer sb = new StringBuffer();
        for(Object o : collection){
            if(o==null) o = "";
            sb.append(prefix).append(o.toString()).append(suffix).append(separate);
        }
        if(sb.length()==0)return sb.toString();
        return sb.substring(0, sb.length() - separate.length());
    }
    public static String collectionToStr(Collection<?> collection,String separate,boolean isQuotes){
        if(isQuotes)return collectionToStr(collection,separate,"'","'");
        else return collectionToStr(collection,separate,null,null);
    }
    public static String collectionToStr(Collection<?> collection,String separate){
        return collectionToStr(collection,separate,null,null);
    }
    
    
    /**
     * 得到dt是周几
     */
    public static String getWeekOfDate(Date dt) {
        String[] weekDays = {"星期日", "星期一", "星期二", "星期三", "星期四", "星期五", "星期六"};
        Calendar cal = Calendar.getInstance();
        cal.setTime(dt);
        int w = cal.get(Calendar.DAY_OF_WEEK) - 1;
        if (w < 0)
            w = 0;
        return weekDays[w];
    }
}
