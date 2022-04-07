package com.liujun.auto.generator.builder.operator.ddd.increment.field.entity;

import com.liujun.auto.generator.builder.operator.ddd.increment.field.constant.ClassFieldTypeEnum;

import java.util.List;

/**
 * 内部类，存储层实体信息
 *
 * @author liujun
 * @version 0.0.1
 */
public class EntityInfo {

  /** 注释 */
  private String comment;

  /** 注解信息,注解信息可能存在多个 */
  private List<String> annotation;

  /** 访问修饰符 */
  private String visit;

  /** 静态标识 */
  private String staticKey;

  /** final标识 */
  private String finalKey;

  /** 实体类型 */
  private String fieldType;

  /** 实体名称 */
  private String fieldName;

  /** 类的属性类型,属性或者方法 */
  private ClassFieldTypeEnum classType;

  public String getComment() {
    return comment;
  }

  public void setComment(String comment) {
    this.comment = comment;
  }

  public List<String> getAnnotation() {
    return annotation;
  }

  public void setAnnotation(List<String> annotation) {
    this.annotation = annotation;
  }

  public String getVisit() {
    return visit;
  }

  public void setVisit(String visit) {
    this.visit = visit;
  }

  public String getStaticKey() {
    return staticKey;
  }

  public void setStaticKey(String staticKey) {
    this.staticKey = staticKey;
  }

  public String getFinalKey() {
    return finalKey;
  }

  public void setFinalKey(String finalKey) {
    this.finalKey = finalKey;
  }

  public String getFieldType() {
    return fieldType;
  }

  public void setFieldType(String fieldType) {
    this.fieldType = fieldType;
  }

  public String getFieldName() {
    return fieldName;
  }

  public void setFieldName(String fieldName) {
    this.fieldName = fieldName;
  }

  public ClassFieldTypeEnum getClassType() {
    return classType;
  }

  public void setClassType(ClassFieldTypeEnum classType) {
    this.classType = classType;
  }

  @Override
  public String toString() {
    final StringBuilder sb = new StringBuilder("EntityInfo{");
    sb.append("comment='").append(comment).append('\'');
    sb.append(", annotation=").append(annotation);
    sb.append(", visit='").append(visit).append('\'');
    sb.append(", staticKey='").append(staticKey).append('\'');
    sb.append(", finalKey='").append(finalKey).append('\'');
    sb.append(", fieldType='").append(fieldType).append('\'');
    sb.append(", fieldName='").append(fieldName).append('\'');
    sb.append(", classType=").append(classType);
    sb.append('}');
    return sb.toString();
  }
}
