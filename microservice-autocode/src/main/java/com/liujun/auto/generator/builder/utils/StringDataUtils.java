package com.liujun.auto.generator.builder.utils;

import com.liujun.auto.constant.Symbol;
import org.apache.commons.lang3.StringUtils;

/**
 * 字符串的内容处理
 *
 * @author liujun
 * @since 2022/4/11
 */
public class StringDataUtils {

  private StringDataUtils() {}

  /**
   * 空处理
   *
   * @param data
   * @return
   */
  public static String empty(String data) {
    if (StringUtils.isEmpty(data)) {
      return Symbol.EMPTY;
    }
    return data;
  }
}
