package com.liujun.micro.autocode.generator.javalanguage.constant;

/**
 * java的数据类型
 *
 * @author liujun
 * @version 0.0.1
 */
public enum JavaDataType {

  /** java的byte的类型 */
  BYTE("Byte"),

  /** 知整型 */
  SHORT("Short"),

  /** 包装的int类型 */
  INTEGER("Integer"),

  /** long类型 */
  LONG("Long"),

  /** 单精度浮点数 */
  FLOAT("Float"),

  /** 双精度浮点数 */
  DOUBLE("Double"),

  /** 大整数 */
  BIG_DECIMAL("BigDecimal", "java.math.BigDecimal"),

  /** 字符串 */
  STRING("String"),

  /** 时间类型 */
  DATE("LocalDate", "java.time.LocalDate"),

  /** 时间类型 */
  TIME("LocalTime", "java.time.LocalTime"),

  /** 时间类型 */
  DATETIME("LocalDateTime", "java.time.LocalDateTime"),
  ;

  /** 类型信息 */
  private String type;

  /** 需要导入的包 */
  private String importPkg;

  /** java的数据类型信息 */
  private Class javaType;

  JavaDataType(String type) {
    this.type = type;
  }

  JavaDataType(String type, String importPkg) {
    this.type = type;
    this.importPkg = importPkg;
  }

  /**
   * 获取导包的信息
   *
   * @param type 类型信息
   * @return 当前待导入的包
   */
  public static String getImportPkg(String type) {
    for (JavaDataType typeInfo : values()) {
      if (typeInfo.getType().equals(type) && null != typeInfo.getImportPkg()) {
        return typeInfo.getImportPkg();
      }
    }

    return null;
  }

  public String getType() {
    return type;
  }

  public String getImportPkg() {
    return importPkg;
  }

  public Class getJavaType() {
    return javaType;
  }

  @Override
  public String toString() {
    final StringBuilder sb = new StringBuilder("JavaDataType{");
    sb.append("type='").append(type).append('\'');
    sb.append(", importPkg='").append(importPkg).append('\'');
    sb.append(", javaType=").append(javaType);
    sb.append('}');
    return sb.toString();
  }
}
