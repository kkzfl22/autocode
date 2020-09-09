package com.liujun.micro.autocode.generator.builder.operator.utils;

import com.liujun.micro.autocode.constant.Symbol;
import org.apache.commons.lang3.StringUtils;

/**
 * java的注释处理
 *
 * @author liujun
 * @version 0.0.1
 */
public class JavaCommentUtil {

  public static final String TABLE = "表";

  /**
   * 数据库的字符处理，主要是去掉表字
   *
   * @param tableComment
   * @return
   */
  public static String tableCommentProc(String tableComment) {
    if (StringUtils.isEmpty(tableComment)) {
      return Symbol.EMPTY;
    }

    tableComment = tableComment.trim();

    // 将表字符替换为-号
    tableComment = tableComment.replaceAll(TABLE, Symbol.MINUS);

    return tableComment;
  }
}
