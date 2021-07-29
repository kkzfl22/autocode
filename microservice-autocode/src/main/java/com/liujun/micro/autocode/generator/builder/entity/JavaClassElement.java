package com.liujun.micro.autocode.generator.builder.entity;

import java.util.List;

/**
 * 类公共元素信息
 *
 * @author liujun
 * @version 0.0.1
 */
public class JavaClassElement {

  /** 方法的注释 */
  protected String comment;

  /** 方法注解 */
  protected List<String> annotationList;

  /** 方法访问修饰符 */
  protected String visit;

  /** 静态标识 */
  protected String staticFlag;

  /** final标识 */
  protected String finalFlag;

  /** 类型信息 */
  protected String type;

  /** 名称 */
  protected String name;

  public String getComment() {
    return comment;
  }

  public void setComment(String comment) {
    this.comment = comment;
  }

  public List<String> getAnnotationList() {
    return annotationList;
  }

  public void setAnnotationList(List<String> annotationList) {
    this.annotationList = annotationList;
  }

  public String getVisit() {
    return visit;
  }

  public void setVisit(String visit) {
    this.visit = visit;
  }

  public String getStaticFlag() {
    return staticFlag;
  }

  public void setStaticFlag(String staticFlag) {
    this.staticFlag = staticFlag;
  }

  public String getFinalFlag() {
    return finalFlag;
  }

  public void setFinalFlag(String finalFlag) {
    this.finalFlag = finalFlag;
  }

  public String getType() {
    return type;
  }

  public void setType(String type) {
    this.type = type;
  }

  public String getName() {
    return name;
  }

  public void setName(String name) {
    this.name = name;
  }

  @Override
  public String toString() {
    final StringBuilder sb = new StringBuilder("JavaClassElement{");
    sb.append("comment='").append(comment).append('\'');
    sb.append(", annotationList=").append(annotationList);
    sb.append(", visit='").append(visit).append('\'');
    sb.append(", staticFlag='").append(staticFlag).append('\'');
    sb.append(", finalFlag='").append(finalFlag).append('\'');
    sb.append(", type='").append(type).append('\'');
    sb.append(", name='").append(name).append('\'');
    sb.append('}');
    return sb.toString();
  }
}
