package com.liujun.micro.autocode.config.generate.entity;

import lombok.Data;
import lombok.ToString;

/**
 * 类型信息
 *
 * @author liujun
 * @version 0.0.1
 */
@ToString
@Data
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
}
