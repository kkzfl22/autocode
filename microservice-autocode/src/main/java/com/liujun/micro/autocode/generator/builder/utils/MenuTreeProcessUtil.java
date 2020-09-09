package com.liujun.micro.autocode.generator.builder.utils;

import com.liujun.micro.autocode.constant.Symbol;
import org.apache.commons.lang3.StringUtils;

/**
 * 目录树的处理
 *
 * @author liujun
 * @version 0.0.1
 */
public class MenuTreeProcessUtil {

  /**
   * 按路径/进行输出
   *
   * @return
   */
  public static String outPath(String javaDefinePackage) {
    if (StringUtils.isEmpty(javaDefinePackage)) {
      return javaDefinePackage;
    }

      return javaDefinePackage.replaceAll(Symbol.SPIT_POINT, Symbol.PATH);
  }
}
