package com.liujun.micro.autocode.generator.builder.constant;

import com.liujun.micro.autocode.generator.builder.entity.ImportPackageInfo;

/**
 * 公共导入包的相关定义
 *
 * @author liujun
 * @version 0.0.1
 */
public enum ImportCodeActionComparyPackageKey {

  /** API请求响应对象 */
  API_RESPONSE(
      PkgBuildMethod.classInfoVarInfo(
          "com.paraview.common.facade.common", "Result", "返回结果信息", "response")),

  /** API分页请求响应对象 */
  API_PAGE_RESPONSE(
      PkgBuildMethod.classInfoVarInfo(
          "com.paraview.common.facade.common", "PageableResult", "分页结果信息", "response")),

  /** API查询请求响应对象 */
  API_DATA_RESPONSE(
      PkgBuildMethod.classInfoVarInfo(
          "com.paraview.common.facade.common", "ApiDataResponse", "分页结果信息", "response")),

  /** API分页请求响应对象 */
  API_ASSEMBLER_PAGE(
      PkgBuildMethod.classInfoVarInfo(
          "com.common.assembler", "PageAssembler", "分页转换对象", "assembler")),

  /** 响应的错误码信息 */
  API_RESPONSE_CODE(PkgBuildMethod.classInfo("com.paraview.common.facade.common", "APICodeEnum")),
  ;

  /** 包定义信息 */
  private final ImportPackageInfo packageInfo;

  private ImportCodeActionComparyPackageKey(ImportPackageInfo pkgInfo) {
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
