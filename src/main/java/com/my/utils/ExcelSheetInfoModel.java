package com.my.utils;

import java.util.List;
import java.util.Map;

/**
 * 导出excel需要的实体类
 * @author whm
 * @date 2018/6/22
 */
public class ExcelSheetInfoModel {

    /**
     * sheet名称
     */
    private String sheetName;

    /**
     * 标题
     */
    private String title;

    /**
     * 副标题
     */
    private String subTitle;

    /**
     * 标题名集合
     */
    private List<String> titleNameList;

    /**
     * 字段名集合
     */
    private List<String> columnNameList;

    /**
     * 数据，字段名对应字段值
     */
    private List<Map<String, Object>> dataList;

    /**
     * 无参构造器
     */
    public ExcelSheetInfoModel(){
    }

    /**
     * 构造器（无标题，无副标题）
     * @param sheetName
     * @param titleNameList
     * @param columnNameList
     * @param dataList
     */
    public ExcelSheetInfoModel(String sheetName, List<String> titleNameList, List<String> columnNameList, List<Map<String, Object>> dataList){
        this.sheetName = sheetName;
        this.titleNameList = titleNameList;
        this.columnNameList = columnNameList;
        this.dataList = dataList;
    }

    /**
     * 构造器
     * @param sheetName
     * @param titleNameList
     * @param columnNameList
     * @param dataList
     */
    public ExcelSheetInfoModel(String sheetName, String title, List<String> titleNameList, List<String> columnNameList, List<Map<String, Object>> dataList){
        this.sheetName = sheetName;
        this.title = title;
        this.titleNameList = titleNameList;
        this.columnNameList = columnNameList;
        this.dataList = dataList;
    }

    /**
     * 构造器
     * @param sheetName
     * @param titleNameList
     * @param columnNameList
     * @param dataList
     */
    public ExcelSheetInfoModel(String sheetName, String title, String subTitle, List<String> titleNameList, List<String> columnNameList, List<Map<String, Object>> dataList){
        this.sheetName = sheetName;
        this.title = title;
        this.subTitle = subTitle;
        this.titleNameList = titleNameList;
        this.columnNameList = columnNameList;
        this.dataList = dataList;
    }

    public String getSheetName() {
        return sheetName;
    }

    public void setSheetName(String sheetName) {
        this.sheetName = sheetName;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getSubTitle() {
        return subTitle;
    }

    public void setSubTitle(String subTitle) {
        this.subTitle = subTitle;
    }

    public List<String> getTitleNameList() {
        return titleNameList;
    }

    public void setTitleNameList(List<String> titleNameList) {
        this.titleNameList = titleNameList;
    }

    public List<String> getColumnNameList() {
        return columnNameList;
    }

    public void setColumnNameList(List<String> columnNameList) {
        this.columnNameList = columnNameList;
    }

    public List<Map<String, Object>> getDataList() {
        return dataList;
    }

    public void setDataList(List<Map<String, Object>> dataList) {
        this.dataList = dataList;
    }
}
