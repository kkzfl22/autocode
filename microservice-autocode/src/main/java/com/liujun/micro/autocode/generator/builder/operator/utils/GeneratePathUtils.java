package com.liujun.micro.autocode.generator.builder.operator.utils;

import com.liujun.micro.autocode.constant.Symbol;
import com.liujun.micro.autocode.generator.builder.entity.GenerateCodeContext;

/**
 * 生成的路径处理
 *
 * @author liujun
 * @version 0.0.1
 */
public class GeneratePathUtils {

    private static final String MODULE_SUFFIX_NAME = "-api";


    /**
     * 获取服务层的基础路径
     *
     * @param param 上下文信息
     * @return
     */
    public static String outServicePath(GenerateCodeContext param) {
        return param.getFileBasePath() + Symbol.PATH + param.getModuleName();
    }


    /**
     * 获取服务层的基础路径
     *
     * @param param 上下文信息
     * @return
     */
    public static String outApiPath(GenerateCodeContext param) {
        return param.getFileBasePath() + Symbol.PATH + param.getModuleName() + MODULE_SUFFIX_NAME;
    }

}
