package com.liujun.auto.generator.javalanguage.constant;

/**
 * 生成代码需要使用的java的类信息
 *
 * @author liujun
 * @version 0.0.1
 */
public enum JavaUseClassEnum {

  /** java的byte的类型 */
  STRING_BUILDER("StringBuilder"),
  ;

  /** 类型信息 */
  private String name;

  /** 需要导入的包 */
  private String importPkg;

  JavaUseClassEnum(String name) {
    this.name = name;
  }

  JavaUseClassEnum(String name, String importPkg) {
    this.name = name;
    this.importPkg = importPkg;
  }

  /**
   * 获取导包的信息
   *
   * @param type 类型信息
   * @return 当前待导入的包
   */
  public static String getImportPkg(String type) {
    for (JavaUseClassEnum typeInfo : values()) {
      if (typeInfo.getName().equals(type) && null != typeInfo.getImportPkg()) {
        return typeInfo.getImportPkg();
      }
    }

    return null;
  }

  public String getName() {
    return name;
  }

  public String getImportPkg() {
    return importPkg;
  }

  @Override
  public String toString() {
    final StringBuilder sb = new StringBuilder("JavaDataType{");
    sb.append("type='").append(name).append('\'');
    sb.append(", importPkg='").append(importPkg).append('\'');
    sb.append('}');
    return sb.toString();
  }
}
