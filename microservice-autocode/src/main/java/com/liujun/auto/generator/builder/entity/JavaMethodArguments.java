package com.liujun.auto.generator.builder.entity;

import com.liujun.auto.generator.builder.operator.utils.JavaClassCodeUtils;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

/**
 * java的方法参数类型
 *
 * @author liujun
 * @version 0.0.1
 */
@Getter
@Setter
@ToString
@Builder
public class JavaMethodArguments {

  /** 参数的注解 */
  private String annotation;

  /** 参数类型 */
  private String type;

  /** 参数类型 */
  private String name;

  /** 注释 */
  private String comment;

  /**
   * 将包的信息转换为方法参数信息
   *
   * @param packageInfo 包信息
   * @return 方法参数信息
   */
  public static JavaMethodArguments parsePackage(ImportPackageInfo packageInfo, String name) {


    return JavaMethodArguments.builder()
        // 参数的类型
        .type(packageInfo.getClassName())
        // 名称
        .name(name)
        // 注释
        .comment(packageInfo.getClassComment())
        .build();
  }

  /**
   * 将包的信息转换为方法集合参数信息
   *
   * @param packageInfo 包信息
   * @return 方法参数信息
   */
  public static JavaMethodArguments parsePackageList(ImportPackageInfo packageInfo, String name) {

    return JavaMethodArguments.builder()
        // 集合类型参数
        .type(JavaClassCodeUtils.listType(packageInfo.getClassName()))
        // 名称
        .name(name)
        // 注释
        .comment(packageInfo.getClassComment())
        .build();
  }
}
