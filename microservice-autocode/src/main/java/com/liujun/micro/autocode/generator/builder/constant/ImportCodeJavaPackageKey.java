package com.liujun.micro.autocode.generator.builder.constant;

import com.liujun.micro.autocode.generator.builder.entity.ImportPackageInfo;

/**
 * 导入java语言的包信息
 *
 * @author liujun
 * @version 0.0.1
 */
public enum ImportCodeJavaPackageKey {
  /** map类型 */
  MAP(ImportPackageInfo.builder().pkgPath("java.util").className("Map").varName("map").build());
  ;

  /** 包定义信息 */
  private final ImportPackageInfo packageInfo;

  private ImportCodeJavaPackageKey(ImportPackageInfo pkgInfo) {
    this.packageInfo = pkgInfo;
  }

  public ImportPackageInfo getPackageInfo() {
    return packageInfo;
  }

  @Override
  public String toString() {
    final StringBuilder sb = new StringBuilder("CreateCommKey{");
    sb.append('}');
    return sb.toString();
  }
}
