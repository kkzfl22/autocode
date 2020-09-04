package com.liujun.micro.autocode.generator.builder.operator.utils;

import com.liujun.micro.autocode.constant.Symbol;
import com.liujun.micro.autocode.generator.builder.entity.ImportPackageInfo;
import com.liujun.micro.autocode.generator.builder.entity.JavaClassEntity;
import com.liujun.micro.autocode.generator.builder.entity.JavaMethodArguments;
import com.liujun.micro.autocode.generator.builder.entity.JavaMethodEntity;
import com.liujun.micro.autocode.generator.javalanguage.constant.JavaKeyWord;
import com.liujun.micro.autocode.generator.javalanguage.serivce.JavaFormat;
import org.apache.commons.lang3.StringUtils;

import java.util.List;

/**
 * java的公共类操作
 *
 * @author liujun
 * @version 0.0.1
 */
public class JavaClassCodeUtils {

  /**
   * 接口文件头定义
   *
   * @param classInfo 类定义信息
   * @param importList 导入包
   * @param author 作者
   */
  public static StringBuilder interfaceDefine(
      ImportPackageInfo classInfo, List<String> importList, String author) {

    JavaClassEntity classEntityInfo =
        JavaClassEntity.builder()
            // 类的关键字
            .classKey(JavaKeyWord.INTERFACE_KEY)
            // 类名
            .className(classInfo.getClassName())
            // 类注释
            .classComment(classInfo.getClassComment())
            // 包类路径信息
            .packagePath(classInfo.getPackagePath())
            // 导入包信息
            .importList(importList)
            // 作者
            .author(author)
            .build();

    // 文件类定义
    return javaClassDefine(classEntityInfo);
  }

  /**
   * javaClass文件头定义
   *
   * @param classInfo 类定义信息
   * @param importList 导入包
   * @param author 作者
   */
  public static StringBuilder classDefine(
      ImportPackageInfo classInfo, List<String> importList, String author) {

    JavaClassEntity classEntityInfo =
        JavaClassEntity.builder()
            // 类的关键字
            .classKey(JavaKeyWord.CLASS_KEY)
            // 类名
            .className(classInfo.getClassName())
            // 类注释
            .classComment(classInfo.getClassComment())
            // 包类路径信息
            .packagePath(classInfo.getPackagePath())
            // 导入包信息
            .importList(importList)
            // 作者
            .author(author)
            .build();

    // 文件类定义
    return javaClassDefine(classEntityInfo);
  }

  /**
   * javaClass文件头定义
   *
   * @param classInfo 类定义信息
   * @param importList 导入包
   * @param annotationList 注解
   * @param author 作者
   */
  public static StringBuilder classDefine(
      ImportPackageInfo classInfo,
      List<String> importList,
      List<String> annotationList,
      String author) {

    JavaClassEntity classEntityInfo =
        JavaClassEntity.builder()
            // 类的关键字
            .classKey(JavaKeyWord.CLASS_KEY)
            // 类名
            .className(classInfo.getClassName())
            // 类注释
            .classComment(classInfo.getClassComment())
            // 包类路径信息
            .packagePath(classInfo.getPackagePath())
            // 导入包信息
            .importList(importList)
            // 注解
            .annotationList(annotationList)
            // 作者
            .author(author)
            .build();

    // 文件类定义
    return javaClassDefine(classEntityInfo);
  }

  /**
   * 类文件定义
   *
   * @param classEntity
   * @return
   */
  public static StringBuilder javaClassDefine(JavaClassEntity classEntity) {
    StringBuilder sb = new StringBuilder();

    // 定义包
    sb.append(JavaKeyWord.PACKAGE)
        .append(Symbol.SPACE)
        .append(classEntity.getPackagePath())
        .append(Symbol.SEMICOLON)
        .append(Symbol.ENTER_LINE);
    sb.append(Symbol.ENTER_LINE);

    // 导入包
    if (null != classEntity.getImportList() && !classEntity.getImportList().isEmpty()) {
      for (String importClass : classEntity.getImportList()) {
        sb.append(JavaKeyWord.IMPORT).append(Symbol.SPACE);
        sb.append(importClass).append(Symbol.SEMICOLON).append(Symbol.ENTER_LINE);
      }
      // 导包结束添加一个换行
      sb.append(Symbol.ENTER_LINE);
    }

    // 添加类注释信息
    sb.append(JavaKeyWord.ANNO_CLASS).append(Symbol.ENTER_LINE).append(JavaKeyWord.ANNO_CLASS_MID);
    sb.append(Symbol.SPACE).append(classEntity.getClassComment()).append(Symbol.ENTER_LINE);
    sb.append(JavaKeyWord.ANNO_CLASS_MID).append(Symbol.ENTER_LINE).append(JavaKeyWord.DOC_VERSION);
    sb.append(Symbol.ENTER_LINE).append(JavaKeyWord.DOC_AUTH).append(classEntity.getAuthor());
    sb.append(Symbol.ENTER_LINE).append(JavaKeyWord.ANNO_OVER).append(Symbol.ENTER_LINE);

    // 注解
    if (null != classEntity.getAnnotationList() && !classEntity.getAnnotationList().isEmpty()) {
      for (String annotation : classEntity.getAnnotationList()) {
        sb.append(annotation).append(Symbol.ENTER_LINE);
      }
    }

    // 文件头声明
    sb.append(JavaKeyWord.PUBLIC).append(Symbol.SPACE).append(classEntity.getClassKey());
    sb.append(Symbol.SPACE).append(classEntity.getClassName());

    // 检查是否有继承
    if (classEntity.getExtendClass() != null) {
      sb.append(Symbol.SPACE).append(JavaKeyWord.EXTEND);
      sb.append(Symbol.SPACE).append(classEntity.getExtendClass());
    }

    // 检查是否需要实现接口
    if (classEntity.getInterfaceClass() != null) {
      sb.append(Symbol.SPACE).append(JavaKeyWord.IMPLEMENTS);
      sb.append(Symbol.SPACE).append(classEntity.getInterfaceClass());
    }

    sb.append(Symbol.SPACE).append(Symbol.BRACE_LEFT);
    sb.append(Symbol.ENTER_LINE);
    sb.append(Symbol.ENTER_LINE);
    sb.append(Symbol.ENTER_LINE);

    return sb;
  }

  /**
   * javaClass文件结尾
   *
   * @param sb
   */
  public static void classEnd(StringBuilder sb) {
    sb.append(Symbol.BRACE_RIGHT);
  }

  /**
   * 方法的定义
   *
   * @param sb
   * @param method 方法对象信息
   */
  public static void methodDefine(StringBuilder sb, JavaMethodEntity method) {
    int tabIndex = 1;

    // 方法的注释
    sb.append(JavaFormat.appendTab(tabIndex)).append(JavaKeyWord.ANNO_CLASS);
    sb.append(Symbol.ENTER_LINE);
    sb.append(JavaFormat.appendTab(tabIndex)).append(JavaKeyWord.ANNO_CLASS_MID);
    sb.append(Symbol.SPACE).append(method.getMethodComment()).append(Symbol.ENTER_LINE);
    sb.append(JavaFormat.appendTab(tabIndex)).append(JavaKeyWord.ANNO_CLASS_MID);
    sb.append(Symbol.ENTER_LINE);

    // 参数注释
    if (method.getArguments() != null) {
      for (JavaMethodArguments argument : method.getArguments()) {
        sb.append(JavaFormat.appendTab(tabIndex)).append(JavaKeyWord.ANNO_CLASS_MID);
        sb.append(JavaKeyWord.METHOD_PARAM).append(argument.getName()).append(Symbol.SPACE);
        sb.append(argument.getComment()).append(Symbol.ENTER_LINE);
      }
    }
    // 返回类型注释
    if (StringUtils.isNotEmpty(method.getReturnComment())) {
      sb.append(JavaFormat.appendTab(tabIndex)).append(JavaKeyWord.ANNO_CLASS_MID);
      sb.append(JavaKeyWord.METHOD_RETURN_COMMENT).append(method.getReturnComment());
      sb.append(Symbol.ENTER_LINE);
    }

    sb.append(JavaFormat.appendTab(tabIndex)).append(JavaKeyWord.ANNO_OVER);
    sb.append(Symbol.ENTER_LINE);

    // 检查当前是否存在注解
    if (StringUtils.isNotEmpty(method.getAnnotation())) {
      sb.append(JavaFormat.appendTab(tabIndex)).append(method.getAnnotation());
      sb.append(Symbol.ENTER_LINE);
    }

    // 方法的声明
    sb.append(JavaFormat.appendTab(tabIndex));
    // 访问修饰符的检查
    if (StringUtils.isNotEmpty(method.getVisitMethod())) {
      sb.append(method.getVisitMethod()).append(Symbol.SPACE);
    }
    // 静态字段检查
    if (StringUtils.isNotEmpty(method.getStaticKey())) {
      sb.append(method.getStaticKey()).append(Symbol.SPACE);
    }

    sb.append(method.getReturnType()).append(Symbol.SPACE).append(method.getMethodName());
    sb.append(Symbol.BRACKET_LEFT);

    // 方法的参数声明
    if (method.getArguments() != null && !method.getArguments().isEmpty()) {
      for (int i = 0; i < method.getArguments().size(); i++) {
        JavaMethodArguments argument = method.getArguments().get(i);
        sb.append(argument.getType()).append(Symbol.SPACE).append(argument.getName());

        if (i != method.getArguments().size() - 1) {
          sb.append(Symbol.COMMA);
        }
      }
    }
    sb.append(Symbol.BRACKET_RIGHT);
  }

  /**
   * 方法的开始
   *
   * @param sb
   */
  public static void methodStart(StringBuilder sb) {
    sb.append(Symbol.BRACE_LEFT).append(Symbol.ENTER_LINE);
  }

  /**
   * 方法结束
   *
   * @param sb
   */
  public static void methodEnd(StringBuilder sb) {
    // 方法的结束
    sb.append(JavaFormat.appendTab(1));
    sb.append(Symbol.BRACE_RIGHT).append(Symbol.ENTER_LINE);
    sb.append(Symbol.ENTER_LINE);
  }

  /**
   * 集合类型
   *
   * @param type
   * @return
   */
  public static String listType(String type) {
    StringBuilder dataList = new StringBuilder();

    dataList.append(JavaKeyWord.LIST_TYPE).append(type);
    dataList.append(JavaKeyWord.LIST_TYPE_END);

    return dataList.toString();
  }

  /**
   * map类型
   *
   * @param type
   * @return
   */
  public static String mapStringKey(String type) {
    StringBuilder dataList = new StringBuilder();

    dataList.append(JavaKeyWord.MAP_TYPE).append(Symbol.ANGLE_BRACKETS_LEFT);
    dataList.append(JavaKeyWord.TYPE_STRING).append(Symbol.COMMA);
    dataList.append(type).append(JavaKeyWord.LIST_TYPE_END);

    return dataList.toString();
  }
}
