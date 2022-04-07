package com.liujun.auto.generator.builder.entity;

import com.liujun.auto.generator.javalanguage.constant.JavaKeyWord;
import com.liujun.auto.generator.builder.constant.CodeAnnotation;

import java.util.ArrayList;
import java.util.Arrays;

/**
 * java的类属性定义
 *
 * @author liujun
 * @version 0.0.1
 */
public class JavaClassFieldEntity extends JavaClassElement {

  /** 值信息 */
  private String value;

  /**
   * 获取spring注解的私有的属性构建对象
   *
   * @param type 类型信息
   * @param name 名称
   * @param comment 注释
   */
  public static JavaClassFieldEntity getPrivateAutowiredField(
      String type, String name, String comment) {
    JavaClassFieldEntity instance =
        JavaClassFieldEntity.builder()
            // 访问修饰符
            .visit(JavaKeyWord.PRIVATE)
            // 注解
            .annotation(CodeAnnotation.AUTOWIRED)
            // 类型
            .type(type)
            // 名称
            .name(name)
            // 注释
            .comment(comment)
            // 构建
            .build();

    return instance;
  }

  /**
   * 获取spring注解的私有的属性构建对象
   *
   * @param classInfo 类信息
   */
  public static JavaClassFieldEntity getPrivateAutowiredField(ImportPackageInfo classInfo) {

    JavaClassFieldEntity instance =
        JavaClassFieldEntity.builder()
            // 访问修饰符
            .visit(JavaKeyWord.PRIVATE)
            // 注解
            .annotation(classInfo.getAnnotation())
            // 类型
            .type(classInfo.getClassName())
            // 名称
            .name(classInfo.getVarName())
            // 注释
            .comment(classInfo.getClassComment())
            // 构建
            .build();

    return instance;
  }

  /**
   * 用来进行作为参数的build类
   *
   * @author liujun
   * @vsersion 0.0.1
   */
  public static class Builder {

    /** 方法的注释 */
    private String comment;

    /** 方法注解 */
    private String annotation;

    /** 方法访问修饰符 */
    private String visit;

    /** 静态标识 */
    private String staticFlag;

    /** final标识 */
    private String finalFlag;

    /** 类型信息 */
    private String type;

    /** 名称 */
    private String name;

    /** 值信息 */
    private String value;

    public Builder comment(String comment) {
      this.comment = comment;
      return this;
    }

    public Builder annotation(String annotation) {
      this.annotation = annotation;
      return this;
    }

    public Builder visit(String visit) {
      this.visit = visit;
      return this;
    }

    public Builder staticFlag(String staticFlag) {
      this.staticFlag = staticFlag;
      return this;
    }

    public Builder finalFlag(String finalFlag) {
      this.finalFlag = finalFlag;
      return this;
    }

    public Builder type(String type) {
      this.type = type;
      return this;
    }

    public Builder name(String name) {
      this.name = name;
      return this;
    }

    public Builder value(String value) {
      this.value = value;
      return this;
    }

    public JavaClassFieldEntity build() {
      return new JavaClassFieldEntity(this);
    }
  }

  public JavaClassFieldEntity(Builder builder) {
    this.comment = builder.comment;
    if (builder.annotation != null && !builder.annotation.isEmpty()) {
      this.annotationList = new ArrayList<>(Arrays.asList(builder.annotation));
    }
    this.visit = builder.visit;
    this.staticFlag = builder.staticFlag;
    this.finalFlag = builder.finalFlag;
    this.type = builder.type;
    this.name = builder.name;
    this.value = builder.value;
  }

  public static Builder builder() {
    return new Builder();
  }

  public String getValue() {
    return value;
  }

  public void setValue(String value) {
    this.value = value;
  }

  @Override
  public String toString() {
    final StringBuilder sb = new StringBuilder("JavaClassFieldEntity{");
    sb.append("value='").append(value).append('\'');
    sb.append('}');
    return sb.toString();
  }
}
