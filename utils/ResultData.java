package com.alipay.springboot.alipayspringboot.Result;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.Data;

import java.io.Serializable;
import java.util.List;

/**
 * @DESCRIPTION ：数据返回实体
 * @AUTHOR ：sky
 * @CREATETIME ：2018-05-30 9:07
 **/
@Data
public class ResultData implements Serializable {

    //定义jackson对象
    private static final ObjectMapper MAPPER = new ObjectMapper();
    private static final Integer SUCCESS = 200;
    private static final Integer FAILED = 100;
    private static final Integer MISS = 300;
    //状态码
    private Integer code;
    //消息
    private String msg;
    //数据对象
    private Object data;

    /**
     * 无参构造方法
     */
    public ResultData() {
    }

    /**
     * 返回状态码，消息，数据对象
     *
     * @param code
     * @param msg
     * @param data
     */
    public ResultData(Integer code, String msg, Object data) {
        this.code = code;
        this.msg = msg;
        this.data = data;
    }

    /**
     * 返回状态码，消息
     *
     * @param code
     * @param msg
     */
    public ResultData(Integer code, String msg) {
        this.code = code;
        this.msg = msg;
    }

    /**
     * 返回状态码,数据对象
     *
     * @param code
     * @param data
     */
    public ResultData(Integer code, Object data) {
        this.code = code;
        this.data = data;
    }

    /**
     * 请求成功,返回数据对象
     *
     * @param data
     */
    public ResultData(Object data) {
        this.data = data;
        this.code = SUCCESS;
        this.msg = "请求成功";
    }

    public static ResultData build(Integer code, String msg, Object data) {
        return new ResultData(code, msg, data);
    }

    public static ResultData build(Integer code, String msg) {
        return new ResultData(code, msg);
    }

    public static ResultData build(Integer code, Object data) {
        return new ResultData(code, data);
    }

    public static ResultData ok(Object data) {
        return new ResultData(data);
    }

    public static ResultData ok() {
        return new ResultData(null);
    }

    /**
     * 将json对象转化为ResultData对象
     *
     * @param jsonData json数据
     * @param clazz    ResultData对象中的Object类型
     * @return ResultData
     */
    public static ResultData formatToPojo(String jsonData, Class<?> clazz) {
        try {
            if (clazz == null) {
                return MAPPER.readValue(jsonData, ResultData.class);
            }
            JsonNode jsonNode = MAPPER.readTree(jsonData);
            JsonNode data = jsonNode.get("data");
            Object obj = null;
            if (clazz != null) {
                if (data.isObject()) {
                    obj = MAPPER.readValue(data.traverse(), clazz);
                } else if (data.isTextual()) {
                    obj = MAPPER.readValue(data.asText(), clazz);
                }
            }
            return build(jsonNode.get("code").intValue(), jsonNode.get("msg").asText(), obj);
        } catch (Exception e) {
            return null;
        }
    }

    /**
     * 没有Object对象的转化
     *
     * @param json json数据
     * @return ResultData
     */
    public static ResultData format(String json) {
        try {
            return MAPPER.readValue(json, ResultData.class);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    /**
     * Object是集合的转化
     *
     * @param jsonData json数据
     * @param clazz    集合中的类型
     * @return
     */
    public static ResultData formatToList(String jsonData, Class<?> clazz) {
        try {
            JsonNode jsonNode = MAPPER.readTree(jsonData);
            JsonNode data = jsonNode.get("data");
            Object obj = null;
            if (data.size() > 0 && data.isArray()) {
                obj = MAPPER.readValue(data.traverse(),
                        MAPPER.getTypeFactory().constructCollectionType(List.class, clazz));
            }
            return build(jsonNode.get("code").intValue(), jsonNode.get("msg").asText(), obj);
        } catch (Exception e) {
            return null;
        }
    }

}