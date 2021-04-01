package com.my.utils;

import java.io.File;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Random;

import javax.servlet.http.HttpServletRequest;

import org.springframework.web.multipart.MultipartFile;

/**
 * @author whm
 * @date 2019/3/16
 */
public class UploadUtil {

    /**
     * 上传图片
     * @param file      要上传的图片
     * @param request
     * @return      上传地址（失败返回null）
     */
    public static String uploadPicture(
            String orgCd, int type, MultipartFile file, HttpServletRequest request
    ){
        //获取文件名加后缀
        String fileName=file.getOriginalFilename();
        if(fileName!=null&&fileName!=""){
            //存储路径
            String returnUrl = request.getScheme() + "://" + request.getServerName() + ":" + request.getServerPort() + request.getContextPath() +"/static/images/"+orgCd+"/"+type+"/";
            //文件存储位置
            String path = request.getSession().getServletContext().getRealPath("WEB-INF/static/images/"+orgCd+"/"+type);
            //文件后缀
            String fileF = fileName.substring(fileName.lastIndexOf("."), fileName.length());
            //新的文件名
            fileName=System.currentTimeMillis()+"_"+new Random().nextInt(1000)+fileF;

            //先判断文件是否存在
            SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMdd");
            String fileAdd = sdf.format(new Date());
            //获取文件夹路径
            File file1 =new File(path+"/"+fileAdd);
            //如果文件夹不存在则创建
            if(!file1 .exists()  && !file1 .isDirectory()){
                file1 .mkdirs();
            }
            //将图片存入文件夹
            File targetFile = new File(file1, fileName);
            try {
                //将上传的文件写到服务器上指定的文件。
                file.transferTo(targetFile);

                //返回成功
                return returnUrl+fileAdd+"/"+fileName;
            } catch (Exception e) {
                e.printStackTrace();
                //返回失败
                return null;
            }
        }

        return null;
    }

    /**
     * 上传图片
     * @param file      要上传的图片
     * @param request
     * @return      上传地址（失败返回null）
     */
    public static String uploadPicture(
            MultipartFile file, HttpServletRequest request
    ){
        //获取文件名加后缀
        String fileName=file.getOriginalFilename();
        if(fileName!=null&&fileName!=""){
            //存储路径
            String returnUrl = request.getScheme() + "://" + request.getServerName() + ":" + request.getServerPort() + request.getContextPath() +"/static/images/";
            //文件存储位置
            String path = request.getSession().getServletContext().getRealPath("WEB-INF/static/images");
            //文件后缀
            String fileF = fileName.substring(fileName.lastIndexOf("."), fileName.length());
            //新的文件名
            fileName=System.currentTimeMillis()+"_"+new Random().nextInt(1000)+fileF;

            //先判断文件是否存在
            SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMdd");
            String fileAdd = sdf.format(new Date());
            //获取文件夹路径
            File file1 =new File(path+"/"+fileAdd);
            //如果文件夹不存在则创建
            if(!file1 .exists()  && !file1 .isDirectory()){
                file1 .mkdirs();
            }
            //将图片存入文件夹
            File targetFile = new File(file1, fileName);
            try {
                //将上传的文件写到服务器上指定的文件。
                file.transferTo(targetFile);

                //返回成功
                return returnUrl+fileAdd+"/"+fileName;
            } catch (Exception e) {
                e.printStackTrace();
                //返回失败
                return null;
            }
        }

        return null;
    }

}
