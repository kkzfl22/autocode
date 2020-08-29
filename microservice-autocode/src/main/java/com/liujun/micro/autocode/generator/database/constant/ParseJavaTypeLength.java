package com.liujun.micro.autocode.generator.database.constant;

/**
 * @author liujun
 * @version 0.0.1
 * @date 2019/01/14
 */
public class ParseJavaTypeLength {

  /** long max bit */
  public static final int MAX_LONG = String.valueOf(Long.MAX_VALUE).length() - 1;

  /** union flag info */
  public static final String UNION_FLAG = "UNION";

  /** one type info */
  public static final int ONE_TYPE_SIZE = 5;

  /** union type info */
  public static final int UNION_TYPE_SIZE = 8;
}
