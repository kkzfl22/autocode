package com.liujun.micro.autocode.constant;

/**
 * 工程目录树中的名称
 *
 * @author liujun
 * @version 0.0.1
 */
public enum ProjectMenuTreeKey {

  /** 最顶层的java目录 */
  SRC("src"),

  /** 主代码目录 */
  SRC_MAIN("main"),

  /** 主要的java源代码目录 */
  MAIN_JAVA("java"),

  /** 资源文件目录 */
  MAIN_RESOURCES("resources"),

  /** 数据库操作的资源文件目录 */
  RESOURCES_MAPPER("mapper"),

  /** mapper下的二级存储目录 */
  RESOURCES_REPOSITORY("repository"),

  /** 测试目录 */
  SRC_TEST("test"),

  /** 测试的源代码目录 */
  TEST_JAVA("java"),

  /** 测试的资源目录 */
  TEST_RESOURCES("resources"),


  ;

  private String key;

  ProjectMenuTreeKey(String key) {
    this.key = key;
  }

  public String getKey() {
    return key;
  }
}
