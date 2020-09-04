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
@Builder
public class JavaMethodEntity {

  /** 方法的注释 */
  private String methodComment;

  /** 方法访问修饰符 */
  private String visitMethod;

  /** 静态标识 */
  private String staticKey;

  /** 返回类型 */
  private String returnType;

  /** 返回对象的注释 */
  private String returnComment;

  /** 方法名 */
  private String methodName;

  /** 方法注解 */
  private String annotation;

  /** 参数信息 */
  private List<JavaMethodArguments> arguments;
}
