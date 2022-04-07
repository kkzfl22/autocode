package com.liujun.auto.generator.builder.operator.utils;

import com.liujun.auto.constant.Symbol;
import com.liujun.auto.generator.builder.entity.GenerateCodeContext;

/**
 * 生成的路径处理
 *
 * @author liujun
 * @version 0.0.1
 */
public class GeneratePathUtils {

  /**
   * 获取服务层的基础路径
   *
   * @param param 上下文信息
   * @return
   */
  public static String outServicePath(GenerateCodeContext param) {
    return ouProjectPath(param) + Symbol.PATH + param.getModuleName();
  }

  /**
   * 获取服务层的基础路径
   *
   * @param param 上下文信息
   * @return
   */
  public static String outApiPath(GenerateCodeContext param) {
    return ouProjectPath(param) + Symbol.PATH + param.getModuleName();
  }

  /**
   * 获取项目的的基础路径
   *
   * @param param 上下文信息
   * @return
   */
  public static String ouProjectPath(GenerateCodeContext param) {
    return param.getFileBasePath()
        + Symbol.PATH
        + param.getGenerateConfig().getGenerate().getProjectName();
  }
}
