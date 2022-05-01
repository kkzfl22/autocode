package com.liujun.auto.generator.builder.ddd.constant;

import com.liujun.auto.generator.builder.ddd.entity.ImportPackageInfo;

/**
 * 构建包信息的一些公共方法，用于旧方法的转换操作
 *
 * @author liujun
 * @since 2021/8/26
 */
public class PkgBuildMethod {

  private PkgBuildMethod() {}

  /**
   * 创建一个类信息，仅包括路径与名称
   *
   * @param pkgPath 路径信息
   * @param className 类名信息
   * @return 生成对象信息
   */
  public static ImportPackageInfo classInfo(String pkgPath, String className) {
    return ImportPackageInfo.builder().pkgPath(pkgPath).className(className).build();
  }

  /**
   * 泛型类信息
   *
   * @param pkgPath 路径信息
   * @param className 名称
   * @param classComment 描述
   * @param varName 变量名
   * @param generic 泛型
   * @return
   */
  public static ImportPackageInfo genericClassInfo(
      String pkgPath, String className, String classComment, String varName, String generic) {
    return ImportPackageInfo.builder()
        .pkgPath(pkgPath)
        .className(className)
        .classComment(classComment)
        .varName(varName)
        .generic(generic)
        .build();
  }

  /**
   * 类带有描述信息
   *
   * @param pkgPath
   * @param className
   * @param classComment
   * @return
   */
  public static ImportPackageInfo classInfoComment(
      String pkgPath, String className, String classComment) {
    return ImportPackageInfo.builder()
        .pkgPath(pkgPath)
        .className(className)
        .classComment(classComment)
        .build();
  }

  /**
   * 类带有描述信息
   *
   * @param pkgPath
   * @param className
   * @param classComment
   * @param varName
   * @return
   */
  public static ImportPackageInfo classInfoVarInfo(
      String pkgPath, String className, String classComment, String varName) {
    return ImportPackageInfo.builder()
        .pkgPath(pkgPath)
        .className(className)
        .classComment(classComment)
        .varName(varName)
        .build();
  }

  /**
   * 构建注解的对象信息
   *
   * @param pkgPath 包路径信息
   * @param className 类名信息
   * @param annotation 注解
   * @return
   */
  public static ImportPackageInfo getAnnotationPkg(
      String pkgPath, String className, String annotation) {
    ImportPackageInfo annotationPkgInfo =
        ImportPackageInfo.builder()
            .pkgPath(pkgPath)
            .className(className)
            .annotation(annotation)
            .build();
    return annotationPkgInfo;
  }
}
