package com.liujun.auto.generator.javalanguage.entity;

import com.liujun.auto.generator.javalanguage.constant.ClassKeyWordEnum;
import com.liujun.auto.generator.javalanguage.constant.VisitEnum;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.util.List;

/**
 * 类文件的结构信息
 *
 * @author liujun
 * @since 2022/4/6
 */
@Setter
@Getter
@ToString
public class JavaClassStruct {

  /** 文件的路径 */
  private String filePath;

  /** 文件最开始定义的版本信息 */
  private String copyRight;

  /** 定义的包路径信息 */
  private String pkgPath;

  /** 引用的导入 */
  private List<JavaClassImportClass> referenceImport;

  /** 导入与注释之间存在的空行数 */
  private int spaceLine;

  /** 类注释信息 */
  private JavaClassDocument classDocument;

  /** 类的注解信息 */
  private List<ContextAnnotationList> classAnnotation;

  /** 类的访问修饰符 */
  private VisitEnum classVisit;

  /** 类的关键字标识符 */
  private ClassKeyWordEnum classKeyWord;

  /** 类名 */
  private String className;

  /** 所有的类的内容中的信息（包括属性、方法、静态代码块等） */
  private List<ContextBase> context;
}
