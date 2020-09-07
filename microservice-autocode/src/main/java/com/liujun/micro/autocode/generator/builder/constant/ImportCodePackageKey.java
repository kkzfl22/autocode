package com.liujun.micro.autocode.generator.builder.constant;

import com.liujun.micro.autocode.config.menutree.MenuTreeCodePackage;
import com.liujun.micro.autocode.generator.builder.entity.ImportPackageInfo;

/**
 * 公共导入包的相关定义
 *
 * @author liujun
 * @version 0.0.1
 */
public enum ImportCodePackageKey {

  /** 查询分页参数 */
  PAGE_PARAM(
      new ImportPackageInfo(
          MenuTreeCodePackage.INSTANCE.getInfrastructureEntity().outJavaPackage(),
          "PageDO",
          CodeComment.PAGE_REQUEST_COMMENT)),

  /** 查询分页结果集 */
  PAGE_RESULT(
      new ImportPackageInfo(
          MenuTreeCodePackage.INSTANCE.getInfrastructureEntity().outJavaPackage(),
          "PageResult",
          CodeComment.PAGE_RESPONSE_COMMENT)),
  ;

  /** 包定义信息 */
  private ImportPackageInfo packageInfo;

  ImportCodePackageKey(ImportPackageInfo packageInfo) {
    this.packageInfo = packageInfo;
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