package com.liujun.auto.utils;

import com.liujun.auto.constant.Symbol;

/**
 * 公共的字符处理
 *
 * @author liujun
 * @since 2021/9/28
 */
public class StringUtils {

  private StringUtils() {}

  /**
   * 执行文本内容的替换换行符操作
   *
   * @param context
   * @return
   */
  public static String containerTrim(String context) {
    String outContext = context;

    outContext = outContext.replaceAll(Symbol.ENTER, Symbol.EMPTY);
    outContext = outContext.replaceAll(Symbol.ENTER_LINE, Symbol.EMPTY);

    return outContext;
  }
}
