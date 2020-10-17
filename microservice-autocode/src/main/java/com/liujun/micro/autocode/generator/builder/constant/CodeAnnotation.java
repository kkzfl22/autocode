package com.liujun.micro.autocode.generator.builder.constant;

/**
 * 代码中相关的注解
 *
 * @author liujun
 * @version 0.0.1
 */
public class CodeAnnotation {

    /**
     * 存储层的标识
     */
    public static final String REPOSITORY = "@Repository";





    /**
     * spring的注解
     */
    public static final String AUTOWIRED = "@Autowired";

    /**
     * 用于标识重写
     */
    public static final String OVERRIDE = "@Override";

    /**
     * swagger的注解API
     */
    public static final String SWAGGER_API = "@Api";

    /**
     * tags标识
     */
    public static final String SWAGGER_API_TAGS = "tags";

    /**
     * 值
     */
    public static final String SWAGGER_API_VALUE = "value";

    /**
     * 表当前为rest请求
     */
    public static final String SPRING_CONTROLLER = "@RestController";

    /**
     * 请求的地址
     */
    public static final String SPRING_REQUEST_MAPPING = "@RequestMapping";

    /**
     * 请求的地址信息
     */
    public static final String SPRING_REQUEST_MAPPING_VALUE = "value";

    /**
     * 请求方法
     */
    public static final String SPRING_REQUEST_MAPPING_METHOD = "method";


}
