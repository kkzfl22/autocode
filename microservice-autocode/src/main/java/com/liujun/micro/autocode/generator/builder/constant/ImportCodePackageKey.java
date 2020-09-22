package com.liujun.micro.autocode.generator.builder.constant;

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
          "com.common.entity",
          "PageDataInfo",
          CodeComment.PAGE_REQUEST_COMMENT,
          JavaVarName.PAGE_REQUEST)),

  /** 查询分页结果集 */
  PAGE_RESULT(
      new ImportPackageInfo(
          "com.common.entity",
          "PageDataInfo",
          CodeComment.PAGE_RESPONSE_COMMENT,
          JavaVarName.PAGE_RESPONSE)),

  /** 用于进行方法检查 */
  PARAM_CHECK(new ImportPackageInfo("com.common.check", "ParamCheckUtils")),

  /** 错误的对象信息 */
  ERROR_DATA(new ImportPackageInfo("com.common.entity", "ErrorData", "错误码对象", "codeData")),

  /** 错误码容器集合 */
  ERROR_COLLECT(new ImportPackageInfo("com.common.collect", "ErrorCodeCollect", "错误码集合")),

  /** 错误码加载器对象 */
  ERROR_LOADER_COLLECT(new ImportPackageInfo("com.common.collect", "ErrorLoaderCollect", "错误码加载器"));

  /** 包定义信息 */
  private final ImportPackageInfo packageInfo;

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
