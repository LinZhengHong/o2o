package com.linzhenghong.o2o.util;

import net.coobird.thumbnailator.Thumbnails;
import org.springframework.web.multipart.commons.CommonsMultipartFile;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Objects;
import java.util.Random;

/**
 * 图片压缩工具类
 * @author LinZhenHong
 */
public class ImageUtil {

    private static String basePath= Objects.requireNonNull(Thread.currentThread().getContextClassLoader().getResource("")).getPath();
    private static final SimpleDateFormat sDateFormat=new SimpleDateFormat("yyyyMMddHHmmss");
    private static final Random r=new Random();

    /**
     *
     * @param thumbnailInputStream 处理的文件流
     * @param targetAddr 图片的存储路径
     * @return relativeAddr
     */
    public static String generateThumbnail(InputStream thumbnailInputStream,String filename, String targetAddr){
        //获取文件的随机名
        String realFileName=getRandomFileName();
        //获取文件图片的扩展名
        String extension=getFileExtension(filename);
        //创建文件目录
        makeDirPath(targetAddr);

        //获取图片的相对路径
        String relativeAddr=targetAddr+realFileName+extension;

        //根路径+相对路径
        File dest=new File(PathUtil.getImgBasePath()+relativeAddr);
        try {
            Thumbnails.of(thumbnailInputStream).size(200,200)
                    //增加图片水印
                    /*.watermark(Positions.BOTTOM_RIGHT, ImageIO.read(new File(basePath+"/watermark.jpg")),0.25f)*/
                    .outputQuality(0.8f).toFile(dest);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return relativeAddr;
    }


    /**
     * 生成随机文件名。当前年月日小时分钟秒+五位随机数
     * @return r
     */
    public static String getRandomFileName() {
        //获取随机的五位数
        int rannum=r.nextInt(89999)+10000;
        String nowTimeStr=sDateFormat.format(new Date());
        return nowTimeStr+rannum;
    }


    /**
     * 获取输入文件流的扩展名
     */
    private static String getFileExtension(String filename) {
        return filename.substring(filename.lastIndexOf("."));
}



    /**
     * 创建目标路径所涉及的目录
     * @param targetAddr
     */
    private static void makeDirPath(String targetAddr) {
        String realFileParentPath=PathUtil.getImgBasePath()+targetAddr;
        File dirPath=new File(realFileParentPath);
        if (!dirPath.exists()){
            dirPath.mkdirs();
        }
    }

    /**
     * storePath是文件的路径还是目录的路径
     * 如果storePath是文件路径则删除该文件下，
     * 如果storePath是目录路径则删除该目录下的所有文件
     * @param storePath
     */
    public static void deleteFileOrPath(String storePath) {
        File fileOrPath = new File(PathUtil.getImgBasePath() + storePath);
        if (fileOrPath.exists()) {
            if (fileOrPath.isDirectory()) {
                File files[] = fileOrPath.listFiles();
                for (int i = 0; i < files.length; i++) {
                    files[i].delete();
                }
            }
            fileOrPath.delete();
        }
    }

}
