package com.liujun.micro.autocode.generator.builder.entity;

import lombok.Builder;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.util.List;

/**
 * java的类对象实体
 *
 * @author liujun
 * @version 0.0.1
 */
@Getter
@Setter
@Builder
@ToString
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
}
