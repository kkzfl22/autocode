package com.liujun.auto.generator.builder.operator.utils;

import java.nio.charset.StandardCharsets;

/**
 * java字符的编码工具类
 *
 * @author liujun
 * @version 0.0.1
 */
public class JavaEncodeUtils {

  /** 标识 */
  private static final char FLAG = '\u007f';

  /** 开始编码的第一个字符 */
  private static final char START_ONE = (char) 92;

  /** 编码的第二个字符 */
  private static final char START_TWO = (char) 117;

  /** 0 */
  private static final char ZERO = '0';

  /** 编码长度 */
  private static final char CODE_LENGTH = 4;

  /**
   * 进行字符的编码操作
   *
   * <p>同java自带的工具类:native2ascii效果一致
   *
   * @param originalText 代码
   * @return
   */
  public static String native2ascii(String originalText) {
    final StringBuilder stringBuffer = new StringBuilder();

    char[] array = originalText.toCharArray();
    int n2 = array.length;
    for (int i = 0; i < n2; ++i) {
      if (array[i] > FLAG) {

        stringBuffer.append(START_ONE);
        stringBuffer.append(START_TWO);
        final StringBuilder sb = new StringBuilder(Integer.toHexString(array[i]));
        sb.reverse();
        for (int n3 = CODE_LENGTH - sb.length(), j = 0; j < n3; ++j) {
          sb.append(ZERO);
        }
        for (int k = 0; k < CODE_LENGTH; ++k) {
          stringBuffer.append(sb.charAt(CODE_LENGTH - 1 - k));
        }
      } else {
        stringBuffer.append(array[i]);
      }
    }
    return stringBuffer.toString();
  }

  /**
   * 进行字符的统一编码，以防系统造成乱码
   *
   * @param value
   * @return
   */
  public static String outCodeUtf8(String value) {
    return new String(value.getBytes(), StandardCharsets.UTF_8);
  }
}
