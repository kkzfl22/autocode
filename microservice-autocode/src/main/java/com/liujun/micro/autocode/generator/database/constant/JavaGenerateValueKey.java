package com.liujun.micro.autocode.generator.database.constant;

/**
 * @author liujun
 * @version 0.0.1
 */
public class JavaGenerateValueKey {

  /** 随机生成int的value值的方法 */
  public static final String GENERATE_INT_VALUE = "RandomUtils.nextInt(0,1<<#length#)";

  /** 随机字符 */
  public static final String GENERATE_ALPHABETIC_VALUE =
      "RandomStringUtils.randomAlphabetic(#length#)";
}
