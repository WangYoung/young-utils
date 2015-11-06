package com.young.wang.utils;

import java.awt.image.BufferedImage;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;

import javax.imageio.ImageIO;

import net.coobird.thumbnailator.Thumbnails;

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
 * Created by Young Wang on 2015年11月5日 下午12:03:18.
 *
 */
public final class ImageUtil {
	private ImageUtil(){}
	
	/**
     * 将图片压缩至指定分辨率
     */
    public static byte[] compressImage(InputStream is,int outWidth,int outHeight) throws IOException {
        if(outWidth <=0 || outHeight <=0)throw new IllegalArgumentException();

        ByteArrayOutputStream bout = new ByteArrayOutputStream();
        Thumbnails.of(is).size(outWidth, outHeight).keepAspectRatio(false).useOriginalFormat().toOutputStream(bout);
        return bout.toByteArray();
    }

    /**
     * 将图片压缩至指定分辨率
     */
    public static byte[] compressImage(byte[] data,int outWidth,int outHeight) throws IOException {
        if(outWidth <=0 || outHeight <=0)throw new IllegalArgumentException();

        return compressImage(new ByteArrayInputStream(data),outWidth,outHeight);
    }

    /**
     * 按照百分比压缩
     * @param ratio 压缩比例，推荐0-1之间
     */
    public static byte[] compressImageByPercent(byte[] imageData, double ratio)throws IOException{
        if(ratio<=0 || imageData==null || imageData.length==0)throw new IllegalArgumentException();
        ByteArrayOutputStream bout = new ByteArrayOutputStream();
        Thumbnails.of(new ByteArrayInputStream(imageData)).scale(ratio).useOriginalFormat().toOutputStream(bout);
        return bout.toByteArray();
    }

    /**
     * 指定高度压缩图片，宽度自动按比例计算
     */
    public static byte[] compressImageByHeight(byte[] imageData,int outHeight)throws IOException{
        if(outHeight<=0 || imageData==null || imageData.length==0)throw new IllegalArgumentException();
        ByteArrayOutputStream bout = new ByteArrayOutputStream();
        Thumbnails.of(new ByteArrayInputStream(imageData)).height(outHeight).useOriginalFormat().toOutputStream(bout);
        return bout.toByteArray();
    }

    /**
     * 指定宽度压缩图片，高度自动按比例计算
     */
    public static byte[] compressImageByWidth(byte[] imageData,int outWidth)throws IOException{
        if(outWidth<=0 || imageData==null || imageData.length==0)throw new IllegalArgumentException();
        ByteArrayOutputStream bout = new ByteArrayOutputStream();
        Thumbnails.of(new ByteArrayInputStream(imageData)).width(outWidth).useOriginalFormat().toOutputStream(bout);
        return bout.toByteArray();
    }

    /**
     * 裁剪图片
     */
    public static byte[] cutImage(InputStream is,int x,int y,int width,int height) throws IOException{

        ByteArrayOutputStream bout=new ByteArrayOutputStream();
        Thumbnails.of(is).sourceRegion(x,y,width,height).size(width, height).keepAspectRatio(false).useOriginalFormat().toOutputStream(bout);
        return bout.toByteArray();
    }

    /**
     * 裁剪图片
     */
    public static byte[] cutImage(byte[] data,int x,int y,int width,int height) throws IOException{
        return cutImage(new ByteArrayInputStream(data), x, y, width, height);
    }

    /**
     * 裁剪图片，适合页面缩放之后
     */
    public static byte[] cutImagePage(byte[] data,int x,int y,int width,int height,int pageImgWidth,int pageImgHeight)throws IOException{
        BufferedImage bufferedImage = ImageIO.read(new ByteArrayInputStream(data));
        int cutX,cutY,cutWidth,cutHeight;
        int imgWidth = bufferedImage.getWidth();
        int imgHeight = bufferedImage.getHeight();

        double widthRate = ((double)pageImgWidth)/((double)imgWidth); //宽度比例
        double heightRate = ((double)pageImgHeight)/((double)imgHeight); //高度比例

        cutX = (int)(((double)x)/widthRate);
        cutY = (int)(((double)y)/heightRate);
        cutWidth = (int)(((double)width)/widthRate);
        cutHeight = (int)(((double)height)/heightRate);

        return cutImage(data, cutX, cutY, cutWidth, cutHeight);
    }
}
