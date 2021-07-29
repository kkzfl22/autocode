package com.liujun.micro.autocode.generator.builder.entity;

import com.liujun.micro.autocode.constant.Symbol;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

/**
 * 导入的文件包信息
 *
 * @author liujun
 * @version 0.0.1
 */
public class ImportPackageInfo {

  /** java的包路径，以点分隔开 */
  private String packagePath;

  /** 类文件名 */
  private String className;

  /** 类的注释 */
  private String classComment;

  /** 声明的变量的名称 */
  private String varName;

  /** 注解标识 */
  private String annotation;

  /** 泛型标识 */
  private String generic;

  public ImportPackageInfo(String packagePath, String className) {
    this.packagePath = packagePath;
    this.className = className;
  }

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

  /**
   * 声明引入对象
   *
   * @param packagePath 包路径
   * @param className 类名称信息
   * @param classComment 描述
   * @param varName 变量名
   * @param generic 泛型
   */
  public ImportPackageInfo(
      String packagePath, String className, String classComment, String varName, String generic) {
    this(packagePath, className, classComment, varName);
    this.generic = generic;
  }

  /**
   * 构建注解的对象信息
   *
   * @param packagePath
   * @param className
   * @param annotation
   * @return
   */
  public static ImportPackageInfo getAnnotationPkg(
      String packagePath, String className, String annotation) {
    ImportPackageInfo instance = new ImportPackageInfo(packagePath, className);
    instance.setAnnotation(annotation);
    return instance;
  }

  /**
   * 进行package信息的输出操作
   *
   * @return 包路径信息，以点分隔
   */
  public String packageOut() {
    return this.getPackagePath() + Symbol.POINT + this.getClassName();
  }

  public String getPackagePath() {
    return packagePath;
  }

  public void setPackagePath(String packagePath) {
    this.packagePath = packagePath;
  }

  public String getClassName() {
    return className;
  }

  public void setClassName(String className) {
    this.className = className;
  }

  public String getClassComment() {
    return classComment;
  }

  public void setClassComment(String classComment) {
    this.classComment = classComment;
  }

  public String getVarName() {
    return varName;
  }

  public void setVarName(String varName) {
    this.varName = varName;
  }

  public String getAnnotation() {
    return annotation;
  }

  public void setAnnotation(String annotation) {
    this.annotation = annotation;
  }

  public String getGeneric() {
    return generic;
  }

  @Override
  public String toString() {
    final StringBuilder sb = new StringBuilder("ImportPackageInfo{");
    sb.append("packagePath='").append(packagePath).append('\'');
    sb.append(", className='").append(className).append('\'');
    sb.append(", classComment='").append(classComment).append('\'');
    sb.append(", varName='").append(varName).append('\'');
    sb.append(", annotation='").append(annotation).append('\'');
    sb.append(", generic='").append(generic).append('\'');
    sb.append('}');
    return sb.toString();
  }
}
