package com.liujun.auto.generator.javalanguage.entity;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

/**
 * 导包的class文件路径信息
 *
 * @author liujun
 * @since 2022/4/6
 */
@Setter
@Getter
@ToString
public class JavaClassImportClass {

  /** 是否为静态导入 */
  private boolean staticImport;

  /** 导入的相对路径 */
  private String referencePath;

  public static JavaClassImportClass staticImport(String referencePath) {
    JavaClassImportClass importClass = new JavaClassImportClass();
    importClass.setStaticImport(Boolean.TRUE);
    importClass.setReferencePath(referencePath);
    return importClass;
  }

  /**
   * 普通的属性导入
   *
   * @param referencePath
   * @return
   */
  public static JavaClassImportClass normalImport(String referencePath) {
    JavaClassImportClass importClass = new JavaClassImportClass();
    importClass.setReferencePath(referencePath);
    return importClass;
  }
}
