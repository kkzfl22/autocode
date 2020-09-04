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

  // public ImportPackageInfo(String packagePath, String className) {
  //  this.packagePath = packagePath;
  //  this.className = className;
  // }

  public ImportPackageInfo(String packagePath, String className, String classComment) {
    this.packagePath = packagePath;
    this.className = className;
    this.classComment = classComment;
  }
}
