package com.liujun.auto.config.generate.entity;

/**
 * 类型信息
 *
 * @author liujun
 * @version 0.0.1
 */
public class TypeInfo {

  /** java中导包的路径 */
  private String importPath;

  /** 导入的文件类名 */
  private String importClassName;

  /**
   * 完整类路径与类名的情况
   *
   * @param importPath 类路径
   * @param importClassName 类名
   */
  public TypeInfo(String importPath, String importClassName) {
    this.importPath = importPath;
    this.importClassName = importClassName;
  }

  /**
   * 仅类名的情况，比如String
   *
   * @param importClassName 类名
   */
  public TypeInfo(String importClassName) {
    this.importClassName = importClassName;
  }

  public String getImportPath() {
    return importPath;
  }

  public void setImportPath(String importPath) {
    this.importPath = importPath;
  }

  public String getImportClassName() {
    return importClassName;
  }

  public void setImportClassName(String importClassName) {
    this.importClassName = importClassName;
  }

  @Override
  public String toString() {
    final StringBuilder sb = new StringBuilder("TypeInfo{");
    sb.append("importPath='").append(importPath).append('\'');
    sb.append(", importClassName='").append(importClassName).append('\'');
    sb.append('}');
    return sb.toString();
  }
}
