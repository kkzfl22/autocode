package com.liujun.micro.autocode.generator.builder.entity;

import com.liujun.micro.autocode.constant.Symbol;

import java.util.ArrayList;
import java.util.List;

/**
 * 注解集合
 *
 * @author liujun
 * @version 0.0.1
 */
public class JavaAnnotationList {

  /** 注解集合 */
  private List<JavaAnnotation> annotationList = new ArrayList<>();

  /**
   * 添加操作
   *
   * @param annotation
   */
  public void add(JavaAnnotation annotation) {
    this.annotationList.add(annotation);
  }

  /**
   * 添加操作
   *
   * @param annotationList
   */
  public void addList(List<JavaAnnotation> annotationList) {
    this.annotationList.addAll(annotationList);
  }

  /**
   * 使用空格输出注解信息
   *
   * @return
   */
  public String outSpaceAnnotation() {
    StringBuilder outSpace = new StringBuilder();

    for (JavaAnnotation annotation : annotationList) {
      outSpace.append(annotation.outAnnotation());
      outSpace.append(Symbol.SPACE);
    }
    outSpace.deleteCharAt(outSpace.length() - 1);

    return outSpace.toString();
  }
}
