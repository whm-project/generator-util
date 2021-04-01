package com.my.utils;

import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 读excel数据
 * @author whm
 * @date 2018/5/25
 */
@Service("readExcel")
public class ReadExcel {

    // 总行数
    private int totalRows = 0;
    // 总条数
    private int totalCells = 0;
    //错误信息接收器
    private String errorMsg = "";
    //标题List
    private List<String> titleList = new ArrayList<>();
    //数据List
    private List<Map<String, Object>> dataList = new ArrayList<>();

    /**
     * 构造方法
     */
    private ReadExcel(){

    }

    /**
     * 获取总行数
     * @return
     */
    public int getTotalRows() {
        return totalRows;
    }

    /**
     * 获取总列数
     * @return
     */
    public int getTotalCells() {
        return totalCells;
    }

    /**
     * 获取错误信息
     * @return
     */
    public String getErrorMsg() {
        return errorMsg;
    }

    /**
     * 获取标题集合
     * @return
     */
    public List<String> getTitleList() {
        return titleList;
    }

    /**
     * 获取数据集合
     * @return
     */
    public List<Map<String, Object>> getDataList() {
        return dataList;
    }

    /**
     * 初始化信息
     */
    public void initInfo(){
        // 总行数
        this.totalRows = 0;
        // 总条数
        this.totalCells = 0;
        //错误信息接收器
        this.errorMsg = "";
        //标题List
        this.titleList = new ArrayList<>();
        //数据List
        this.dataList = new ArrayList<>();
    }

    /**
     * 读EXCEL文件，获取信息集合
     * @param mFile
     */
    public ReadExcel readExcelInfo(MultipartFile mFile) {
        //初始化信息
        initInfo();

        // 获取文件名
        String fileName = mFile.getOriginalFilename();
        try {
            // 验证文件名是否合格
            if (!validateExcel(fileName)) {
                this.errorMsg = "error fileName";
            }
            // 根据文件名判断文件是2003版本还是2007版本
            boolean isExcel2003 = true;
            if (isExcel2007(fileName)) {
                isExcel2003 = false;
            }
            createWb(mFile.getInputStream(), isExcel2003);
        } catch (Exception e) {
            e.printStackTrace();
            this.errorMsg = "error exception";
        }
        return this;
    }

    /**
     * 根据excel里面的内容读取客户信息
     * @param is      输入流
     * @param isExcel2003   excel是2003还是2007版本
     * @return
     * @throws IOException
     */
    public void createWb(InputStream is, boolean isExcel2003) {
        try {
            Workbook wb = null;
            // 当excel是2003时,创建excel2003
            if (isExcel2003) {
                wb = new HSSFWorkbook(is);
            }
            // 当excel是2007时,创建excel2007
            else {
                wb = new XSSFWorkbook(is);
            }
            // 读取Excel里面客户的信息
            analysisExcelValue(wb);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * 读取Excel里面的信息
     * @param wb
     */
    private void analysisExcelValue(Workbook wb) {
        // 得到第一个shell
        Sheet sheet = wb.getSheetAt(0);
        // 得到Excel的行数
        this.totalRows = sheet.getPhysicalNumberOfRows();
        // 得到Excel的列数(前提是有行数)
        if (totalRows > 1 && sheet.getRow(0) != null) {
            this.totalCells = sheet.getRow(0).getPhysicalNumberOfCells();
        }

        //第0行是标题
        Row titleRow = sheet.getRow(0);
        //标题List
        for(int c=0; c<this.totalCells; c++){
            Cell titleCell = titleRow.getCell(c);
            this.titleList.add(titleCell.getStringCellValue());
        }

        //循环Excel行数【数据从第1行开始】
        for (int r = 1; r < totalRows; r++) {
            Map<String, Object> map = new HashMap<>();
            Row row = sheet.getRow(r);
            if (row == null) {
                continue;
            }
            // 循环Excel的列
            for (int c = 0; c < this.totalCells; c++) {
                Cell cell = row.getCell(c);
                if (null != cell) {
                    switch (cell.getCellTypeEnum()){
                        case STRING:
                            map.put(titleList.get(c), cell.getStringCellValue());
                            break;
                        case NUMERIC:
                            map.put(titleList.get(c), cell.getNumericCellValue());
                            break;
                        case BOOLEAN:
                            map.put(titleList.get(c), cell.getBooleanCellValue());
                            break;
                        default:
                            map.put(titleList.get(c), "");;
                    }
                }
            }
            // 添加到list
            this.dataList.add(map);
        }
    }

    /**
     * 验证EXCEL文件
     * @param filePath
     * @return
     */
    public boolean validateExcel(String filePath) {
        if (filePath == null || !(isExcel2003(filePath) || isExcel2007(filePath))) {
            errorMsg = "文件名不是excel格式";
            return false;
        }
        return true;
    }

    // @描述：是否是2003的excel，返回true是2003
    public static boolean isExcel2003(String filePath) {
        return filePath.matches("^.+\\.(?i)(xls)$");
    }

    // @描述：是否是2007的excel，返回true是2007
    public static boolean isExcel2007(String filePath) {
        return filePath.matches("^.+\\.(?i)(xlsx)$");
    }

}
