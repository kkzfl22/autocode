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
          "com.ddd.common.infrastructure.entity",
          "DomainPage",
          CodeComment.PAGE_REQUEST_COMMENT,
          JavaVarName.PAGE_REQUEST,
          "<table_name>")),

  /** 查询分页结果集 */
  PAGE_RESULT(
      new ImportPackageInfo(
          "com.ddd.common.infrastructure.entity",
          "DomainPage",
          CodeComment.PAGE_RESPONSE_COMMENT,
          JavaVarName.PAGE_RESPONSE,
          "<List<table_name>>")),

  /** 查询分页结果集 */
  PAGE_DTO(new ImportPackageInfo("com.common.entity", "PageDTO")),

  /** 用于进行方法检查 */
  PARAM_CHECK(new ImportPackageInfo("com.common.check", "ParamCheckUtils", "错误码检查方法")),

  /** 错误的对象信息 */
  ERROR_DATA(new ImportPackageInfo("com.common.entity", "ErrorData", "错误码对象", "codeData")),

  /** 错误码容器集合 */
  ERROR_COLLECT(new ImportPackageInfo("com.common.collect", "ErrorCodeCollect", "错误码集合")),

  /** 公共错误码枚举 */
  ERROR_CODE_COMMON(new ImportPackageInfo("com.common.entity", "APICodeEnum", "公共错误码")),

  /** 错误码加载器对象 */
  ERROR_LOADER_COLLECT(new ImportPackageInfo("com.common.collect", "ErrorLoaderCollect", "错误码加载器")),

  /** Getter的注解 */
  ANNOTATION_GETTER(ImportPackageInfo.getAnnotationPkg("lombok", "Getter", "@Getter")),

  /** Setter的注解 */
  ANNOTATION_SETTER(ImportPackageInfo.getAnnotationPkg("lombok", "Setter", "@Setter")),

  /** toString的注解 */
  ANNOTATION_TOSTRING(ImportPackageInfo.getAnnotationPkg("lombok", "ToString", "@ToString")),

  /** swagger的ApiModel的注解 */
  ANNOTATION_API_MODEL(
      ImportPackageInfo.getAnnotationPkg("io.swagger.annotations", "ApiModel", "@ApiModel")),

  /** swagger的Api的注解 */
  ANNOTATION_API(ImportPackageInfo.getAnnotationPkg("io.swagger.annotations", "Api", "@Api")),

  /** swagger的Api的注解 */
  ANNOTATION_API_OPERATION(
      ImportPackageInfo.getAnnotationPkg(
          "io.swagger.annotations", "ApiOperation", "@ApiOperation")),

  /** swagger的ApiModelProperty的注解 */
  ANNOTATION_API_MODEL_PROPERTY(
      ImportPackageInfo.getAnnotationPkg(
          "io.swagger.annotations", "ApiModelProperty", "@ApiModelProperty")),

  /** spring的RestController注解 */
  SPRING_REST_CONTROLLER(
      ImportPackageInfo.getAnnotationPkg(
          "org.springframework.web.bind.annotation", "RestController", "@RestController")),

  /** spring的RestController注解 */
  SPRING_REQUEST_MAPPING(
      ImportPackageInfo.getAnnotationPkg(
          "org.springframework.web.bind.annotation", "RequestMapping", "@RequestMapping")),

  /** spring的RestController注解 */
  SPRING_SERVICE_ANNOTATION(
      ImportPackageInfo.getAnnotationPkg("org.springframework.stereotype", "Service", "@Service")),

  /** @Slf4j的注解信息 */
  SLF4J_ANNOTATION(ImportPackageInfo.getAnnotationPkg("lombok.extern.slf4j", "Slf4j", "@Slf4j")),

  /** spring的request Method注解 */
  SPRING_REQUEST_METHOD(
      new ImportPackageInfo("org.springframework.web.bind.annotation", "RequestMethod")),

  /** spring的RequestBody注解 */
  SPRING_REQUEST_BODY(
      ImportPackageInfo.getAnnotationPkg(
          "org.springframework.web.bind.annotation", "RequestBody", "@RequestBody")),

  /** spring的RequestBody注解 */
  HTTP_SERVLET_REQUEST(
      new ImportPackageInfo("javax.servlet.http", "HttpServletRequest", "http请求信息", "request")),

  /** API请求响应对象 */
  API_RESPONSE(new ImportPackageInfo("com.common.entity", "ApiResponse", "返回结果信息", "response")),

  /** API分页请求响应对象 */
  API_PAGE_RESPONSE(
      new ImportPackageInfo("com.common.entity", "ApiPageResponse", "分页结果信息", "response")),

  /** API查询请求响应对象 */
  API_DATA_RESPONSE(
      new ImportPackageInfo("com.common.entity", "ApiDataResponse", "分页结果信息", "response")),

  /** API分页请求响应对象 */
  API_PAGE_REQUEST(
      new ImportPackageInfo("com.common.entity", "PageDTO", "分页查询公共请求对象", "pageRequest")),

  /** API分页请求响应对象 */
  API_ASSEMBLER_PAGE(
      new ImportPackageInfo("com.common.assembler", "PageAssembler", "分页转换对象", "assembler")),

  /** 响应的错误码信息 */
  API_RESPONSE_CODE(new ImportPackageInfo("com.common.entity", "APICodeEnum")),

  /** 动态加载的接口 */
  API_CONFIG_INTERFACE(
      new ImportPackageInfo("org.springframework.beans.factory.config", "BeanPostProcessor")),

  /** 加载注解 */
  API_CONFIG_LOAD_ANNOTATION(
      ImportPackageInfo.getAnnotationPkg("javax.annotation", "PostConstruct", "@PostConstruct")),

  /** mybatis相关的注解信息 */
  MYBATIS_PLUS_TABLE_NAME(
      ImportPackageInfo.getAnnotationPkg(
          "com.baomidou.mybatisplus.annotation", "TableField", "@TableName")),

  /** mybatis相关的注解信息 */
  MYBATIS_PLUS_TABLE_FIELD(
      ImportPackageInfo.getAnnotationPkg(
          "com.baomidou.mybatisplus.annotation", "TableField", "@TableField")),
  ;

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
