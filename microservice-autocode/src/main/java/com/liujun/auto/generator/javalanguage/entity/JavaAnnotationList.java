package com.liujun.auto.generator.javalanguage.entity;

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
  private List<com.liujun.micro.autocode.generator.javalanguage.entity.ContextAnnotation> annotationList = new ArrayList<>();

  /**
   * 添加操作
   *
   * @param annotation
   */
  public void add(com.liujun.micro.autocode.generator.javalanguage.entity.ContextAnnotation annotation) {
    this.annotationList.add(annotation);
  }

  /**
   * 添加操作
   *
   * @param annotationList
   */
  public void addList(List<com.liujun.micro.autocode.generator.javalanguage.entity.ContextAnnotation> annotationList) {
    this.annotationList.addAll(annotationList);
  }

  /**
   * 使用空格输出注解信息
   *
   * @return
   */
  public String outSpaceAnnotation() {
    StringBuilder outSpace = new StringBuilder();

    for (com.liujun.micro.autocode.generator.javalanguage.entity.ContextAnnotation annotation : annotationList) {
      outSpace.append(annotation.outAnnotation());
      outSpace.append(Symbol.SPACE);
    }
    outSpace.deleteCharAt(outSpace.length() - 1);

    return outSpace.toString();
  }
}
