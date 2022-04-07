package com.liujun.auto.generator.builder.ddd.constant;

/**
 * spring的关键字信息
 *
 * @author liujun
 * @version 0.0.1
 */
public class JunitKey {

  /** 注入标识 */
  public static final String AUTOWIRED = "@Autowired";

  /** 方法执行之前的注解 */
  public static final String JUNIT_BEFORE = "@Before";

  /** 测试的标识 */
  public static final String ANNO_TEST = "@Test";

  /** 方法执行后的方法 */
  public static final String ANNO_AFTER = "@After";

  /** 进行结果的断言 */
  public static final String ASSERT = "Assert.assertEquals(";
}
