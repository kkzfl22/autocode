package com.liujun.auto.generator.javalanguage.serivce;

import com.liujun.auto.constant.Symbol;

/**
 * java文件输出的格式
 *
 * @author liujun
 * @version 0.0.1
 */
public class JavaFormat {

  /**
   * 进行制表符的输出
   *
   * @param num 输出\t的数量
   * @return 追加tab
   */
  public static String appendTab(int num) {

    StringBuilder toMsg = new StringBuilder();
    if (num <= 0) {
      num = 1;
    }

    for (int i = 0; i < num; i++) {
      toMsg.append(Symbol.TAB);
    }

    return toMsg.toString();
  }
}
