package com.my.utils;

import java.io.*;
import java.util.HashMap;
import java.util.Map;

import freemarker.template.Configuration;
import freemarker.template.Template;
import freemarker.template.TemplateException;
import org.apache.http.client.ClientProtocolException;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import sun.misc.BASE64Decoder;

/**
 * @author whm
 * @date 2020/4/14
 */
public class WriteEcharts {

//    参考网址：https://www.jianshu.com/p/dfc28fd7d786
//    安装phantomjs、EchartsConvert：解压 phantomjs-2.1.1-windows.zip 和 saintlee-echartsconvert-master.zip，命令行输入<phantomjs路径> <EChartsConvert路径> -s -p <服务端口号>，
//    如：C:\Users\Administrator\Desktop\phantomjs-2.1.1-windows\bin>phantomjs C:\Users\Administrator\Desktop\echartsconvert\echarts-convert.js -s -p 6666

    private static String url = "http://localhost:6666";
    private static final String SUCCESS_CODE = "1";

    public static void main(String[] args) throws ClientProtocolException, IOException, TemplateException {
        // 变量
        String title = "水果";
        String[] categories = new String[] { "苹果", "香蕉", "西瓜" };
        int[] values = new int[] { 3, 2, 1 };

        // 模板参数
        HashMap<String, Object> datas = new HashMap<>();
        datas.put("categories", JSON.toJSONString(categories));
        datas.put("values", JSON.toJSONString(values));
        datas.put("title", title);

        writeEcharts_picture("echartsTemplate.ftl", "E:\\workSpace\\otherSpace\\baseFrame\\doc\\echartsTemplate", "ddd.jpg", "E:\\workSpace\\otherSpace\\baseFrame\\doc\\echartsTemplate", datas);
    }

    public static void writeEcharts_picture(String templateEchartsName, String templatePath, String pictureName, String picturePath, Map<String, Object> datas) throws IOException, TemplateException {
        //生成base64
        String base64 = writeEcharts_base64(templateEchartsName, templatePath, datas);

        //判断文件是否存在，不存在则创建
        checkFileDir(picturePath);
        String fileDir = picturePath + File.separator + pictureName;

        BASE64Decoder decoder = new BASE64Decoder();
        try (OutputStream out = new FileOutputStream(fileDir)){
            // 解密
            byte[] b = decoder.decodeBuffer(base64);
            for (int i = 0; i < b.length; ++i) {
                if (b[i] < 0) {
                    b[i] += 256;
                }
            }
            out.write(b);
            out.flush();
        }
    }

    public static String writeEcharts_base64(String templateEchartsName, String templatePath, Map<String, Object> datas) throws IOException, TemplateException {
        String base64 = "";

        //datas生成模板字符串
        String echartsTemplate = datasToTemplateString(templateEchartsName, templatePath, datas);
        if (echartsTemplate == null) {
            return base64;
        }
        echartsTemplate = echartsTemplate.replaceAll("\\s+", "").replaceAll("\"", "'");

        //根据生成的模板生成base64串
        base64 = templateToBase64(echartsTemplate);

        //返回base64串
        return base64;

    }

    public static String datasToTemplateString(String templateEchartsName, String templatePath, Map<String, Object> datas) throws IOException, TemplateException {
        Configuration configuration = new Configuration();

        // 设置默认编码
        configuration.setDefaultEncoding("UTF-8");

        // 设置模板所在文件夹
        configuration.setDirectoryForTemplateLoading(new File(templatePath));

        // 生成模板对象
        Template template = configuration.getTemplate(templateEchartsName);

        // 将datas写入模板并返回
        try (StringWriter stringWriter = new StringWriter()) {
            template.process(datas, stringWriter);
            stringWriter.flush();
            return stringWriter.getBuffer().toString();
        }
    }


    public static String templateToBase64(String echartsTemplate) throws ClientProtocolException, IOException {
        String base64 = "";
        if (echartsTemplate == null) {
            return base64;
        }
        echartsTemplate = echartsTemplate.replaceAll("\\s+", "").replaceAll("\"", "'");

        // 将option字符串作为参数发送给echartsConvert服务器
        Map<String, String> params = new HashMap<>();
        params.put("opt", echartsTemplate);
        String response = HttpUtil.post(url, params, "utf-8");

        // 解析echartsConvert响应
        JSONObject responseJson = JSON.parseObject(response);
        String code = responseJson.getString("code");

        // 如果echartsConvert正常返回
        if (SUCCESS_CODE.equals(code)) {
            base64 = responseJson.getString("data");
        }
        // 未正常返回
        else {
            String string = responseJson.getString("msg");
            throw new RuntimeException(string);
        }

        return base64;
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

}
