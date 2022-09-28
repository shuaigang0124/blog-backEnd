package com.gsg.blog.utils;

import lombok.extern.slf4j.Slf4j;
import net.coobird.thumbnailator.Thumbnails;
import org.springframework.util.StringUtils;

import java.io.IOException;

/**
 * webp图片处理
 * @author gaoshenggang
 * @date  2021/12/10 9:22
 */
@Slf4j
public class WebPUtils {


//    public static void main(String args[]){
//        String srcFile = "D:\\Desktop\\1625131548543-1845544485511653.png"; //原图地址
//        String webpFile = "D:\\Desktop\\1625131548543-1845544485511653.webp"; //输出地址
//        encodingToWebp(srcFile, webpFile,1.0f);
//    }

    /** 将图片转成WebP格式，方法，入参：原图全地址，需转换的WebP图全地址，转换图片质量压缩比（浮点数，例 0.9f）*/
    public static void encodingToWebp(String srcFile, String webpFile, Float scale) {

        try {
            Thumbnails.of(srcFile)
                    .scale(scale)
                    .outputFormat("webp")
//                    .size(500,500)
//                    .outputQuality(1.0f)
                    .toFile(webpFile);
        } catch (IOException e) {
            e.printStackTrace();
            log.error("WebP图片转换异常【{}】", e.getMessage());
        }

    }

    /** 将图片后缀更换成webp*/
    public static String convertToWebp(String srcFile) {

        int beginIndex = srcFile.lastIndexOf(".");
        String imgFilePrefix = srcFile.substring(0, beginIndex);
        String imgWebPFile = imgFilePrefix + ".webp";
        return imgWebPFile;

    }

    /** 将图片后缀更换成webp*/
    public static String changePathToWebp(String supportWebp, String imgPath) {
        log.debug("图片格式转换标志位【{}】", supportWebp);
        if(!StringUtils.isEmpty(supportWebp)
                && supportWebp.trim().equals("1")){
            log.debug("图片格式转换：原文件【{}】", imgPath);
            if(imgPath.endsWith(".png")
                    || imgPath.endsWith(".jpg")
                    || imgPath.endsWith(".jpeg")
                    || imgPath.endsWith(".gif")
                    || imgPath.endsWith(".bmp")
                    || imgPath.endsWith(".webp")) {
                imgPath = WebPUtils.convertToWebp(imgPath);
            }
        }
        log.debug("图片格式转换：转换后【{}】", imgPath);
        return imgPath;
    }

}
