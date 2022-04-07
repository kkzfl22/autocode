package com.liujun.auto.generator.builder.ddd.constant;

import com.liujun.auto.generator.builder.ddd.entity.ImportPackageInfo;

/**
 * 公共导入包的相关定义
 *
 * @author liujun
 * @version 0.0.1
 */
public enum ImportCodeActionDefaultPackageKey {

  /** API请求响应对象 */
  API_RESPONSE(
      PkgBuildMethod.classInfoVarInfo("com.common.entity", "ApiResponse", "返回结果信息", "response")),

  /** API分页请求响应对象 */
  API_PAGE_RESPONSE(
      PkgBuildMethod.classInfoVarInfo(
          "com.common.entity", "ApiPageResponse", "分页结果信息", "response")),

  /** API查询请求响应对象 */
  API_DATA_RESPONSE(
      PkgBuildMethod.classInfoVarInfo(
          "com.common.entity", "ApiDataResponse", "分页结果信息", "response")),

  /** API分页请求响应对象 */
  API_PAGE_REQUEST(
      PkgBuildMethod.classInfoVarInfo("com.common.entity", "PageDTO", "分页查询公共请求对象", "pageRequest")),

  /** API分页请求响应对象 */
  API_ASSEMBLER_PAGE(
      PkgBuildMethod.classInfoVarInfo(
          "com.common.assembler", "PageAssembler", "分页转换对象", "assembler")),

  /** 响应的错误码信息 */
  API_RESPONSE_CODE(PkgBuildMethod.classInfo("com.common.entity", "APICodeEnum")),

  /** 领域对象信息 */
  DOMAIN_PAGE(
      ImportPackageInfo.builder()
          .pkgPath("com.paraview.zsc.domain.shared.entity")
          .className("DomainPage")
          .classComment("领域对象")
          .varName("domainPage")
          .build());

  /** 包定义信息 */
  private final ImportPackageInfo packageInfo;

  private ImportCodeActionDefaultPackageKey(ImportPackageInfo pkgInfo) {
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
