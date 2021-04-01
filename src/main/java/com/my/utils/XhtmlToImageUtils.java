package com.my.utils;

import freemarker.template.Configuration;
import freemarker.template.Template;
import freemarker.template.TemplateException;
import org.springframework.ui.freemarker.FreeMarkerTemplateUtils;
import org.xhtmlrenderer.swing.Java2DRenderer;
import org.xhtmlrenderer.util.FSImageWriter;

import java.awt.image.BufferedImage;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author whm
 * @date 2020/5/11
 */
public class XhtmlToImageUtils {

    public static String convertXhtmlToImage(String templateFtlName, String templatePath, Map<String, Object> model, String fileDir, String xhtmlFilename, String imgFilename, int widthImage, int heightImage)throws IOException {
        //Configuration用于读取ftl文件
        Configuration configuration = new Configuration();
        configuration.setDefaultEncoding("utf-8");

        // 加载文档模板
        Template template = null;
        try {
            //指定路径，例如C:/a.ftl 注意：此处指定ftl文件所在目录的路径，而不是ftl文件的路径
            configuration.setDirectoryForTemplateLoading(new File(templatePath));
            //以utf-8的编码格式读取文件
            if(templateFtlName.contains(".ftl")){
                template = configuration.getTemplate(templateFtlName, "utf-8");
            }else{
                template = configuration.getTemplate(templateFtlName+".ftl", "utf-8");
            }
        } catch (IOException e) {
            e.printStackTrace();
            throw new RuntimeException("文件模板加载失败！", e);
        }

        //生成html字符串
        String html = "";
        try {
            html = FreeMarkerTemplateUtils.processTemplateIntoString(template, model);
        } catch (TemplateException e) {
            e.printStackTrace();
        }

        //判断文件是否存在，不存在则创建
        checkFileDir(fileDir);

        //将html转成文件
        if(!xhtmlFilename.contains(".xhtml")){
            xhtmlFilename += ".xhtml";
        }
        FileWriter writer = new FileWriter(fileDir + File.separator + xhtmlFilename);
        writer.write(html);
        writer.flush();
        writer.close();

        //将xhtml文件转成图片
        if(!imgFilename.contains(".jpg")){
            imgFilename += ".jpg";
        }
        final File f = new File(fileDir + File.separator + xhtmlFilename);
        final Java2DRenderer renderer = new Java2DRenderer(f, widthImage, heightImage);
        final BufferedImage img = renderer.getImage();
        final FSImageWriter imageWriter = new FSImageWriter();
        imageWriter.setWriteCompressionQuality(0.9f);
        imageWriter.write(img, fileDir + File.separator + imgFilename);

        return "";
    }

    /**
     * 判断文件是否存在，不存在则创建
     *
     * @param filePath 文件路径
     * @return
     */
    private static void checkFileDir(String filePath) throws IOException {
        File folder = new File(filePath);
        if (!folder.exists()) {
            folder.mkdirs();
        }
    }

    public static void main(String[] args){
        List<List<String>> trInfo = new ArrayList<>();
        List<String> tdInfo = new ArrayList<>();
        tdInfo.add("1");
        tdInfo.add("2");
        tdInfo.add("3");
        tdInfo.add("4");
        tdInfo.add("5");
        tdInfo.add("<div style=\"float:left; width:4mm; height:2mm; background-color:#ff0000;\"></div>");
        trInfo.add(tdInfo);
        List<String> tdInfo2 = new ArrayList<>();
        tdInfo2.add("11");
        tdInfo2.add("22");
        tdInfo2.add("33");
        tdInfo2.add("44");
        tdInfo2.add("55");
        tdInfo2.add("<div style=\"float:left; width:4mm; height:2mm; background-color:#00ff00;\"></div>");
        trInfo.add(tdInfo2);

        List<String> imgList = new ArrayList<>();
        imgList.add("http://localhost:8078/well/static/images/bj.jpg");

        //加载邮件模板并生成邮件正文
        Map<String, Object> model = new HashMap<String, Object>();
        model.put("title", "标题");
//        model.put("trInfo", trInfo);
//        model.put("imgPathList", imgList);
        try {
            String image= XhtmlToImageUtils.convertXhtmlToImage("images_table_template", "E:\\workSpace\\otherSpace\\baseFrame\\doc\\ftlImgTemplate", model, "E:\\workSpace\\otherSpace\\baseFrame\\doc\\ftlImgCreate", "two_echarts_template","two_echarts_template",1600,1000);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

}
