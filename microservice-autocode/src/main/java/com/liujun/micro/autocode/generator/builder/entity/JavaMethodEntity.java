package com.liujun.micro.autocode.generator.builder.entity;

import lombok.Builder;
import lombok.Data;
import lombok.ToString;

import java.util.List;

/**
 * java方法对象
 *
 * @author liujun
 * @version 0.0.1
 */
@Data
@ToString
public class JavaMethodEntity extends JavaClassElement {

  /** 返回对象的注释 */
  private String returnComment;

  /** 参数信息 */
  private List<JavaMethodArguments> arguments;

  /**
   * 用来进行作为参数的build类
   *
   * @author liujun
   * @vsersion 0.0.1
   */
  public static class Builder {

    /** 返回对象的注释 */
    private String returnComment;

    /** 参数信息 */
    private List<JavaMethodArguments> arguments;

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

    public Builder returnComment(String returnComment) {
      this.returnComment = returnComment;
      return this;
    }

    public Builder arguments(List<JavaMethodArguments> arguments) {
      this.arguments = arguments;
      return this;
    }

    public JavaMethodEntity build() {
      return new JavaMethodEntity(this);
    }
  }

  public JavaMethodEntity(Builder builder) {
    this.comment = builder.comment;
    this.annotation = builder.annotation;
    this.visit = builder.visit;
    this.staticFlag = builder.staticFlag;
    this.finalFlag = builder.finalFlag;
    this.type = builder.type;
    this.name = builder.name;
    this.arguments = builder.arguments;
    this.returnComment = builder.returnComment;
  }

  public static Builder builder() {
    return new Builder();
  }
}
