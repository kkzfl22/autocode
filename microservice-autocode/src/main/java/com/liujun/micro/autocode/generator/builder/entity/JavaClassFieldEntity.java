package com.liujun.micro.autocode.generator.builder.entity;

import com.liujun.micro.autocode.generator.builder.constant.CodeAnnotation;
import com.liujun.micro.autocode.generator.javalanguage.constant.JavaKeyWord;
import lombok.Builder;
import lombok.Data;
import lombok.ToString;

import java.util.List;

/**
 * java的类属性定义
 *
 * @author liujun
 * @version 0.0.1
 */
@Data
@ToString
public class JavaClassFieldEntity {

  /** 访问修饰符 */
  private String visit;

  /** 注解 */
  private String annotation;

  /** 类型 */
  private String type;

  /** 名称 */
  private String name;

  /** 注释 */
  private String comment;

  /**
   * 获取spring注解的私有的属性构建对象
   *
   * @param type 类型信息
   * @param name 名称
   * @param comment 注释
   */
  public static JavaClassFieldEntity getPrivateAutowiredField(
      String type, String name, String comment) {
    JavaClassFieldEntity instance = new JavaClassFieldEntity();
    instance.visit = JavaKeyWord.PRIVATE;
    instance.annotation = CodeAnnotation.AUTOWIRED;
    instance.type = type;
    instance.name = name;
    instance.comment = comment;

    return instance;
  }
}