package com.my.utils;

import org.apache.poi.hssf.usermodel.*;
import org.apache.poi.ss.usermodel.HorizontalAlignment;
import org.apache.poi.ss.usermodel.VerticalAlignment;
import org.apache.poi.ss.util.CellRangeAddress;

import java.beans.BeanInfo;
import java.beans.Introspector;
import java.beans.PropertyDescriptor;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 创建excel
 *
 * @author whm
 * @date 2018/6/14
 */
public class WriteExcel {

    private static HSSFWorkbook workbook = null;

    /**
     * 多sheet的excel生成，返回生成文件名(增加了excel文件名参数)
     *
     * @param sheetInfoList
     * @param filePath
     * @param xlsName
     * @return
     */
    public static String writeExcel(String xlsName, String filePath, List<ExcelSheetInfoModel> sheetInfoList) throws IOException {
        //判断文件是否存在，不存在则创建
        checkFileDir(filePath);

        //设置文件名
        String fileDir = filePath + File.separator + xlsName + ".xls";

        //创建excel
        createExcel(fileDir, sheetInfoList);

        //返回excel地址
        return xlsName + ".xls";
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

    /**
     * 创建新excel.
     *
     * @param fileDir       excel的路径
     * @param sheetInfoList
     */
    private static void createExcel(String fileDir, List<ExcelSheetInfoModel> sheetInfoList) {
        //创建workbook
        workbook = new HSSFWorkbook();

        //新建文件
        FileOutputStream out = null;

        try {
            //创建输出流
            out = new FileOutputStream(fileDir);

            //循环excel信息，生成sheet
            for (ExcelSheetInfoModel sheetInfo : sheetInfoList) {
                setExcelSheet(sheetInfo);
            }

            //输出到文件
            workbook.write(out);

            //关闭输出流
            out.close();
        } catch (IOException ie) {
            ie.printStackTrace();
        }
    }

    /**
     * 设置sheet信息
     *
     * @param sheetInfo
     */
    private static void setExcelSheet(ExcelSheetInfoModel sheetInfo) {
        String sheetName = sheetInfo.getSheetName();
        String title = StringUtil.notNull(sheetInfo.getTitle());
        String subTitle = StringUtil.notNull(sheetInfo.getSubTitle());
        List<String> titleNameList = sheetInfo.getTitleNameList();
        List<String> columnNameList = sheetInfo.getColumnNameList();
        List<Map<String, Object>> dataList = sheetInfo.getDataList();

        //添加Worksheet（不添加sheet时生成的xls文件打开时会报错)
        HSSFSheet sheet1 = workbook.createSheet(sheetName);
        // 设置表格默认列宽度为15个字节
        sheet1.setDefaultColumnWidth(18);
        //最后一列所在的数值
        int lastCol = 0;
        if (columnNameList.size() > 1) {
            lastCol = columnNameList.size() - 1;
        }
        //数据从第几行开始加载
        int dataBeginRow = 1;

        //如果有标题行，设置标题行信息
        if(title.length() > 0) {
            //起始行,结束行,起始列,结束列
            CellRangeAddress callRangeAddress1 = new CellRangeAddress(0, 0, 0, lastCol);
            //标题合并单元格
            sheet1.addMergedRegion(callRangeAddress1);

            //标题字体
            HSSFFont headfont = workbook.createFont();
            headfont.setFontName("微软雅黑");
            // 字体大小
            headfont.setFontHeightInPoints((short) 20);

            //设置值表头 设置表头居中
            HSSFCellStyle headstyle = workbook.createCellStyle();
            headstyle.setFont(headfont);
            //居中样式（上下居中）
            headstyle.setAlignment(HorizontalAlignment.CENTER);
            headstyle.setVerticalAlignment(VerticalAlignment.CENTER);
            headstyle.setLocked(true);

            //创建第一行，添加表头，列居中显示
            HSSFRow row = sheet1.createRow(0);
            HSSFCell hcell = row.createCell(0);
            hcell.setCellValue(title);
            hcell.setCellStyle(headstyle);

            //有标题后，数据行下移一行
            dataBeginRow += 1;
        }

        //如果有副标题行，设置副标题行信息
        if(subTitle.length() > 0){
            //起始行,结束行,起始列,结束列
            CellRangeAddress callRangeAddress2 = new CellRangeAddress(1, 1, 0, lastCol);
            //副标题合并单元格
            sheet1.addMergedRegion(callRangeAddress2);

            //副标题字体
            HSSFFont headfont2 = workbook.createFont();
            headfont2.setFontName("宋体");
            // 字体大小
            headfont2.setFontHeightInPoints((short) 14);

            //设置值表头 设置表头居中
            HSSFCellStyle headstyle2 = workbook.createCellStyle();
            headstyle2.setFont(headfont2);
            //居中样式（上下居中）
            headstyle2.setAlignment(HorizontalAlignment.CENTER);
            headstyle2.setVerticalAlignment(VerticalAlignment.CENTER);
            headstyle2.setLocked(true);

            //创建第二行，添加表头，列居中显示
            HSSFRow row_sub = sheet1.createRow(1);
            HSSFCell hcell_sub = row_sub.createCell(0);
            hcell_sub.setCellValue(subTitle);
            hcell_sub.setCellStyle(headstyle2);

            //有标题后，数据行下移一行
            dataBeginRow += 1;
        }

        //列标题样式
        HSSFFont font2 = workbook.createFont();
        font2.setFontName("宋体");
        font2.setFontHeightInPoints((short) 12);
        font2.setBold(true);
        HSSFCellStyle style2 = workbook.createCellStyle();
        style2.setFont(font2);

        // 上下居中
        style2.setAlignment(HorizontalAlignment.CENTER);
        style2.setVerticalAlignment(VerticalAlignment.CENTER);
        style2.setLocked(true);
        //列标题
        HSSFRow columnRow = sheet1.createRow(dataBeginRow-1);
        for (int m = 0; m < titleNameList.size(); m++) {
            String columnName = titleNameList.get(m);
            HSSFCell ccell = columnRow.createCell(m);
            ccell.setCellValue(columnName);
            ccell.setCellStyle(style2);
        }

        //数据列内容样式
        HSSFCellStyle style3 = workbook.createCellStyle();
        //上下居中
        style3.setAlignment(HorizontalAlignment.CENTER);
        style3.setVerticalAlignment(VerticalAlignment.CENTER);
        style3.setLocked(true);
        //创建数据行
        for (int i = 0; i < dataList.size(); i++) {
            Map<String, Object> itemObj = dataList.get(i);
            HSSFRow dataRow = sheet1.createRow(i + dataBeginRow);
            for (int j = 0; j < columnNameList.size(); j++) {
                String columnName = columnNameList.get(j);
                HSSFCell dcell = dataRow.createCell(j);
                dcell.setCellValue(itemObj.get(columnName)==null?"":itemObj.get(columnName).toString());
                dcell.setCellStyle(style3);
            }
        }
    }

}

