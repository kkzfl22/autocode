package com.liujun.micro.autocode.generator.builder.entity;

import lombok.Builder;

import java.util.List;

/**
 * java的类对象实体
 *
 * @author liujun
 * @version 0.0.1
 */
@Builder
public class JavaClassEntity {

  /** 类关键字，用于标识是class还是interface */
  public String classKey;

  /** java的包路径，以点分隔开 */
  private String packagePath;

  /** 类文件名 */
  private String className;

  /** 类的注释 */
  private String classComment;

  /** 导入的包信息 */
  private List<String> importList;

  /** 注解信息 */
  private List<String> annotationList;

  /** 作者 */
  private String author;

  /** 继承的类信息 */
  private String extendClass;

  /** 接口类 */
  private String interfaceClass;

  public String getClassKey() {
    return classKey;
  }

  public void setClassKey(String classKey) {
    this.classKey = classKey;
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

  public List<String> getImportList() {
    return importList;
  }

  public void setImportList(List<String> importList) {
    this.importList = importList;
  }

  public List<String> getAnnotationList() {
    return annotationList;
  }

  public void setAnnotationList(List<String> annotationList) {
    this.annotationList = annotationList;
  }

  public String getAuthor() {
    return author;
  }

  public void setAuthor(String author) {
    this.author = author;
  }

  public String getExtendClass() {
    return extendClass;
  }

  public void setExtendClass(String extendClass) {
    this.extendClass = extendClass;
  }

  public String getInterfaceClass() {
    return interfaceClass;
  }

  public void setInterfaceClass(String interfaceClass) {
    this.interfaceClass = interfaceClass;
  }

  @Override
  public String toString() {
    final StringBuilder sb = new StringBuilder("JavaClassEntity{");
    sb.append("classKey='").append(classKey).append('\'');
    sb.append(", packagePath='").append(packagePath).append('\'');
    sb.append(", className='").append(className).append('\'');
    sb.append(", classComment='").append(classComment).append('\'');
    sb.append(", importList=").append(importList);
    sb.append(", annotationList=").append(annotationList);
    sb.append(", author='").append(author).append('\'');
    sb.append(", extendClass='").append(extendClass).append('\'');
    sb.append(", interfaceClass='").append(interfaceClass).append('\'');
    sb.append('}');
    return sb.toString();
  }
}
