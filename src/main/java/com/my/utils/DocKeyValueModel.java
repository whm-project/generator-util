package com.my.utils;

/**
 * @author whm
 * @date 2020/3/13
 */
public class DocKeyValueModel {

    /**
     * key值
     */
    private String name;

    /**
     * value值
     */
    private Object value;

    /**
     * value类型
     */
    private ValueEnum type;

    /**
     * 无参构造器
     */
    public DocKeyValueModel(){
    }

    /**
     * 构造器
     * @param name
     * @param value
     * @param type
     */
    public DocKeyValueModel(String name, Object value, ValueEnum type){
        this.name = name;
        this.value = value;
        this.type = type;
    }

    public void setName(String name){
        this.name = name;
    }

    public String getName(){
        return this.name;
    }

    public void setValue(Object value){
        this.value = value;
    }

    public Object getValue(){
        return this.value;
    }

    public void setType(ValueEnum typeEnum){
        this.type = typeEnum;
    }

    public ValueEnum getType(){
        return this.type;
    }

    public enum ValueEnum {

        /**
         *	 值类型
         */
        PICTURE("图片"),
        OTHER("其他");

        private String value;

        ValueEnum(String keyValue) {
            this.value = keyValue;
        }

        public String getValue() {
            return value;
        }

        public void setValue(String value) {
            this.value = value;
        }

    }

}
