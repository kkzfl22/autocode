package com.liujun.micro.autocode.generator.builder.operator.ddd.custom.view.constant;

import com.liujun.micro.autocode.generator.builder.constant.PkgBuildMethod;
import com.liujun.micro.autocode.generator.builder.entity.ImportPackageInfo;

/**
 * 公共导入包的相关定义
 *
 * @author liujun
 * @version 0.0.1
 */
public enum ImportCodeActionViewPackageKey {

  /** API请求响应对象 */
  API_RESPONSE(
      PkgBuildMethod.genericClassInfo(
          "com.paraview.common.facade.common", "Result", "返回结果信息", "response", "<Void>")),

  /** API分页请求响应对象 */
  API_PAGE_RESPONSE(
      PkgBuildMethod.genericClassInfo(
          "com.paraview.common.facade.common",
          "PageableResult",
          "分页结果信息",
          "response",
          "<Map<String, Object>>")),

  /** API分页请求响应对象 */
  API_QUERY_RESPONSE(
      PkgBuildMethod.classInfoVarInfo(
          "com.paraview.common.facade.common", "Result", "查询响应信息", "response")),

  /** 公共处理类 */
  API_RESPONSE_UTILS(
      ImportPackageInfo.builder()
          .pkgPath("com.paraview.common.facade.common")
          .className("ResultUtils")
          .classComment("公共返回")
          .build()),

  /** 公共的返回信息 */
  API_PAGE_RESULT_RESPONSE(
      ImportPackageInfo.builder()
          .pkgPath("com.paraview.common.facade.common")
          .className("PageableResult")
          .classComment("分页查询结果")
          .varName("pageRsp")
          .generic("<Map<String, Object>>")
          .build()),

  /** 查询的参数名称 */
  API_QUERY_FILE_PARAM(
      ImportPackageInfo.builder()
          .pkgPath("com.paraview.zsc.interfaces.dto.request")
          .className("QueryFilterDTO")
          .varName("pageReq")
          .build()),

  /** 查询的自动引入 */
  API_PAGE_AUTOWIRE(
      ImportPackageInfo.builder()
          .pkgPath("")
          .className("QueryFilterDTO")
          .varName("pageReq")
          .build()),
  ;

  /** 包定义信息 */
  private final ImportPackageInfo packageInfo;

  private ImportCodeActionViewPackageKey(ImportPackageInfo pkgInfo) {
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
