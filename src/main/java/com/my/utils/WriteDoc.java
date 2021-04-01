package com.my.utils;

import freemarker.template.Configuration;
import freemarker.template.Template;
import freemarker.template.TemplateException;

import java.io.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author whm
 * @date 2019/11/15
 */
public class WriteDoc {

//    JAVA生成word模板程序步骤：
//        1、 将freemarker-2.3.13.jar复制到项目\WEB-INF\lib目录下
//        2、 编辑模板文件：将DOC文件另存为xml文件，将xml文件后缀改为ftl。此处注意xml文件属性是UTF-8。
//        注意：
//        1、表格的处理方法 如果模板中有表格，则word文件中只留一个表头和一个表格行。然后在转换后的xml文件中找到该表格行的位置，“<w:tr…”开头，”</w:tr>”结尾。 在表格行之前加： <#list wordBeans as w> 在表格行之后加： </#list>
//        2、图片的处理方法 把需要保存的图片转换成二进制字符串，保存到变量中并在xml文件中将二进制串替换 注意dataMap里存放的数据Key值要与模板中的参数相对应
//        参考：https://blog.csdn.net/bobozai86/article/details/100049734

    public static void main(String args[]){
        List<DocKeyValueModel> list = new ArrayList<>();
        list.add(new DocKeyValueModel("CPH","冀A02e5321", DocKeyValueModel.ValueEnum.OTHER));
        list.add(new DocKeyValueModel("img1", "G:/我的图片/52417ac83b85e438d2085cef4356c9e9.jpg", DocKeyValueModel.ValueEnum.PICTURE));
        try {
            writeDoc("Erterter.ftl", "E:/workSpace/otherSpace/baseFrame/doc/docTemplate", "docTest", "E:/", list);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * 生成word文档
     * @param templateDocName       模板文档名称
     * @param templatePath      模板文档路径
     * @param docName       生成文档名称
     * @param docFilePath      生成文档路径
     * @param docKeyValue   模板中需要的键值对
     * @return
     * @throws IOException
     */
    public static String writeDoc(String templateDocName, String templatePath, String docName, String docFilePath, Map<String, Object> docKeyValue) throws IOException {
        //判断文件是否存在，不存在则创建
        checkFileDir(docFilePath);

        //设置文件名
        if(!docName.contains(".doc") && !docName.contains(".docx")){
            docName += ".doc";
        }
        String fileDir = docFilePath + File.separator + docName;

        //创建Word
        createDoc(templateDocName, templatePath, fileDir, docKeyValue);

        //返回Word地址
        return docName;
    }

    /**
     * 生成word文档
     * @param templateDocName       模板文档名称
     * @param templatePath      模板文档路径
     * @param docName       生成文档名称
     * @param docFilePath      生成文档路径
     * @param docKeyValueList   模板中需要的键值对集合
     * @return
     * @throws IOException
     */
    public static String writeDoc(String templateDocName, String templatePath, String docName, String docFilePath, List<DocKeyValueModel> docKeyValueList) throws IOException {
        //判断文件是否存在，不存在则创建
        checkFileDir(docFilePath);

        //设置文件名
        if(!docName.contains(".doc") && !docName.contains(".docx")){
            docName += ".doc";
        }
        String fileDir = docFilePath + File.separator + docName;

        //遍历处理map信息
        Map<String, Object> resultDataMap = new HashMap();
        for (DocKeyValueModel docKeyValue : docKeyValueList) {
            String name = docKeyValue.getName();
            DocKeyValueModel.ValueEnum type = docKeyValue.getType();

            //图片
            if (type.equals(DocKeyValueModel.ValueEnum.PICTURE)) {
                String picturePath = docKeyValue.getValue().toString();
                //图片路径编码
                resultDataMap.put(name, Img2Base64.getWatermarkImage(picturePath));
            }
            //其他
            if (type.equals(DocKeyValueModel.ValueEnum.OTHER)) {
                resultDataMap.put(name, docKeyValue.getValue());
            }
        }

        //创建Word
        createDoc(templateDocName, templatePath, fileDir, resultDataMap);

        //返回Word地址
        return docName;
    }

    /**
     * 给word文档写入数据
     * @param templateDocName       模板文档名称
     * @param templatePath      模板文档路径
     * @param fileDir       生成文档的名称 + 路径
     * @param resultDataMap   模板中需要的键值对集合
     * @throws IOException
     */
    private static void createDoc(String templateDocName, String templatePath, String fileDir, Map<String, Object> resultDataMap) throws IOException {
        //Configuration用于读取ftl文件
        Configuration configuration = new Configuration();
        configuration.setDefaultEncoding("utf-8");

        Writer out = null;
        try {
            //输出文档路径及名称
            File outFile = new File(fileDir);
            out = new BufferedWriter(new OutputStreamWriter(new FileOutputStream(outFile), "utf-8"), 10240);
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }

        // 加载文档模板
        Template template = null;
        try {
            //指定路径，例如C:/a.ftl 注意：此处指定ftl文件所在目录的路径，而不是ftl文件的路径
            configuration.setDirectoryForTemplateLoading(new File(templatePath));
            //以utf-8的编码格式读取文件
            if(templateDocName.contains(".ftl")){
                template = configuration.getTemplate(templateDocName, "utf-8");
            }else{
                template = configuration.getTemplate(templateDocName+".ftl", "utf-8");
            }
        } catch (IOException e) {
            e.printStackTrace();
            throw new RuntimeException("文件模板加载失败！", e);
        }

        // 填充数据
        try {
            template.process(resultDataMap, out);
        } catch (TemplateException e) {
            e.printStackTrace();
            throw new RuntimeException("模板数据填充异常！", e);
        } catch (IOException e) {
            e.printStackTrace();
            throw new RuntimeException("模板数据填充异常！", e);
        } finally {
            if (null != out) {
                try {
                    out.close();
                } catch (IOException e) {
                    e.printStackTrace();
                    throw new RuntimeException("文件输出流关闭异常！", e);
                }
            }
        }
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