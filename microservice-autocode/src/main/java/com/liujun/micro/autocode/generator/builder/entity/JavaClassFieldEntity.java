package com.liujun.micro.autocode.generator.builder.entity;

import com.liujun.micro.autocode.generator.builder.constant.CodeAnnotation;
import com.liujun.micro.autocode.generator.javalanguage.constant.JavaKeyWord;
import lombok.Data;
import lombok.ToString;

/**
 * java的类属性定义
 *
 * @author liujun
 * @version 0.0.1
 */
@Data
@ToString
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
    JavaClassFieldEntity instance = new JavaClassFieldEntity();
    instance.visit = JavaKeyWord.PRIVATE;
    instance.annotation = CodeAnnotation.AUTOWIRED;
    instance.type = type;
    instance.name = name;
    instance.comment = comment;

    return instance;
  }

  /**
   * 获取spring注解的私有的属性构建对象
   *
   * @param type 类型信息
   * @param name 名称
   * @param comment 注释
   * @param value 值信息
   */
  public static JavaClassFieldEntity getPrivateField(
      String type, String name, String comment, String value) {
    JavaClassFieldEntity instance = new JavaClassFieldEntity();
    instance.visit = JavaKeyWord.PRIVATE;
    instance.type = type;
    instance.name = name;
    instance.comment = comment;
    instance.value = value;

    return instance;
  }

  /**
   * 获取spring注解的私有的属性构建对象
   *
   * @param type 类型信息
   * @param name 名称
   * @param comment 注释
   */
  public static JavaClassFieldEntity getPrivateField(String type, String name, String comment) {
    JavaClassFieldEntity instance = new JavaClassFieldEntity();
    instance.visit = JavaKeyWord.PRIVATE;
    instance.type = type;
    instance.name = name;
    instance.comment = comment;

    return instance;
  }

  /**
   * 私有静态属性
   *
   * @param type 类型信息
   * @param name 名称
   * @param comment 注释
   */
  public static JavaClassFieldEntity getPrivateStaticFinalField(
      String type, String name, String comment, String value) {
    JavaClassFieldEntity instance = new JavaClassFieldEntity();
    instance.visit = JavaKeyWord.PRIVATE;
    instance.staticFlag = JavaKeyWord.STATIC;
    instance.finalFlag = JavaKeyWord.FINAL;
    instance.type = type;
    instance.name = name;
    instance.comment = comment;
    instance.value = value;

    return instance;
  }
}
