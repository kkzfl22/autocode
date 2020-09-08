package com.liujun.micro.autocode.generator.builder.entity;

import lombok.Data;
import lombok.ToString;

/**
 * 导入的文件包信息
 *
 * @author liujun
 * @version 0.0.1
 */
@Data
@ToString
public class ImportPackageInfo {

  /** java的包路径，以点分隔开 */
  private String packagePath;

  /** 类文件名 */
  private String className;

  /** 类的注释 */
  private String classComment;

  /** 声明的变量的名称 */
  private String varName;

  /**
   * 仅声明文件类名
   *
   * @param packagePath 包的路径
   * @param className 文件类名
   * @param classComment 类的注释
   */
  public ImportPackageInfo(String packagePath, String className, String classComment) {
    this.packagePath = packagePath;
    this.className = className;
    this.classComment = classComment;
  }

  /**
   * 声明带有变量变量名的文件
   *
   * @param packagePath
   * @param className
   * @param classComment
   * @param varName
   */
  public ImportPackageInfo(
      String packagePath, String className, String classComment, String varName) {
    this(packagePath, className, classComment);
    this.varName = varName;
  }
}
