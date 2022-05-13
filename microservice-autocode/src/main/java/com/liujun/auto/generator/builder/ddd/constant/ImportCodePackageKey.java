package com.liujun.auto.generator.builder.ddd.constant;

import com.liujun.auto.generator.builder.ddd.entity.ImportPackageInfo;
import com.liujun.auto.generator.javalanguage.constant.JavaKeyWord;

/**
 * 公共导入包的相关定义
 *
 * @author liujun
 * @version 0.0.1
 */
public enum ImportCodePackageKey {

  /** 查询分页参数 */
  PAGE_PARAM(
      PkgBuildMethod.genericClassInfo(
          "com.ddd.common.infrastructure.entity",
          "DomainPage",
          CodeComment.PAGE_REQUEST_COMMENT,
          JavaVarName.PAGE_REQUEST,
          "<table_name>")),

  /** 查询分页结果集 */
  PAGE_RESULT(
      PkgBuildMethod.genericClassInfo(
          "com.ddd.common.infrastructure.entity",
          "DomainPage",
          CodeComment.PAGE_RESPONSE_COMMENT,
          JavaVarName.PAGE_RESPONSE,
          "<List<table_name>>")),

  /** 查询分页结果集 */
  PAGE_DTO(PkgBuildMethod.classInfo("com.common.entity", "PageDTO")),

  /** 用于进行方法检查 */
  PARAM_CHECK(PkgBuildMethod.classInfoComment("com.common.check", "ParamCheckUtils", "错误码检查方法")),

  /** 错误的对象信息 */
  ERROR_DATA(
      PkgBuildMethod.classInfoVarInfo("com.common.entity", "ErrorData", "错误码对象", "codeData")),

  /** 错误码容器集合 */
  ERROR_COLLECT(PkgBuildMethod.classInfoComment("com.common.collect", "ErrorCodeCollect", "错误码集合")),

  /** 公共错误码枚举 */
  ERROR_CODE_COMMON(PkgBuildMethod.classInfoComment("com.common.entity", "APICodeEnum", "公共错误码")),

  /** 错误码加载器对象 */
  ERROR_LOADER_COLLECT(
      PkgBuildMethod.classInfoComment("com.common.collect", "ErrorLoaderCollect", "错误码加载器")),

  /** Getter的注解 */
  ANNOTATION_GETTER(PkgBuildMethod.getAnnotationPkg("lombok", "Getter", "@Getter")),

  /** Setter的注解 */
  ANNOTATION_SETTER(PkgBuildMethod.getAnnotationPkg("lombok", "Setter", "@Setter")),

  /** toString的注解 */
  ANNOTATION_TOSTRING(PkgBuildMethod.getAnnotationPkg("lombok", "ToString", "@ToString")),

  /** swagger的ApiModel的注解 */
  ANNOTATION_API_MODEL(
      PkgBuildMethod.getAnnotationPkg("io.swagger.annotations", "ApiModel", "@ApiModel")),

  /** swagger的Api的注解 */
  ANNOTATION_API(PkgBuildMethod.getAnnotationPkg("io.swagger.annotations", "Api", "@Api")),

  /** swagger的Api的注解 */
  ANNOTATION_API_OPERATION(
      PkgBuildMethod.getAnnotationPkg("io.swagger.annotations", "ApiOperation", "@ApiOperation")),

  /** swagger的ApiModelProperty的注解 */
  ANNOTATION_API_MODEL_PROPERTY(
      PkgBuildMethod.getAnnotationPkg(
          "io.swagger.annotations", "ApiModelProperty", "@ApiModelProperty")),

  /** spring的RestController注解 */
  SPRING_REST_CONTROLLER(
      PkgBuildMethod.getAnnotationPkg(
          "org.springframework.web.bind.annotation", "RestController", "@RestController")),

  /** spring的RestController注解 */
  SPRING_REQUEST_MAPPING(
      PkgBuildMethod.getAnnotationPkg(
          "org.springframework.web.bind.annotation", "RequestMapping", "@RequestMapping")),

  /** spring的RestController注解 */
  SPRING_SERVICE_ANNOTATION(
      PkgBuildMethod.getAnnotationPkg("org.springframework.stereotype", "Service", "@Service")),

  /** @Slf4j的注解信息 */
  SLF4J_ANNOTATION(PkgBuildMethod.getAnnotationPkg("lombok.extern.slf4j", "Slf4j", "@Slf4j")),

  /** spring的request Method注解 */
  SPRING_REQUEST_METHOD(
      PkgBuildMethod.classInfo("org.springframework.web.bind.annotation", "RequestMethod")),

  /** spring的RequestBody注解 */
  SPRING_REQUEST_BODY(
      PkgBuildMethod.getAnnotationPkg(
          "org.springframework.web.bind.annotation", "RequestBody", "@RequestBody")),

  /** 参数验证 */
  SPRING_VALID_PARAM(
      PkgBuildMethod.getAnnotationPkg(
          "org.springframework.validation.annotation", "Validated", "@Validated")),

  /** spring的RequestBody注解 */
  HTTP_SERVLET_REQUEST(
      PkgBuildMethod.classInfoVarInfo(
          "javax.servlet.http", "HttpServletRequest", "http请求信息", "request")),

  /** 动态加载的接口 */
  API_CONFIG_INTERFACE(
      PkgBuildMethod.classInfo("org.springframework.beans.factory.config", "BeanPostProcessor")),

  /** 加载注解 */
  API_CONFIG_LOAD_ANNOTATION(
      PkgBuildMethod.getAnnotationPkg("javax.annotation", "PostConstruct", "@PostConstruct")),

  /** mybatis相关的注解信息 */
  MYBATIS_PLUS_TABLE_NAME(
      PkgBuildMethod.getAnnotationPkg(
          "com.baomidou.mybatisplus.annotation", "TableName", "@TableName")),

  /** mybatis相关的注解信息 */
  MYBATIS_PLUS_TABLE_FIELD(
      PkgBuildMethod.getAnnotationPkg(
          "com.baomidou.mybatisplus.annotation", "TableField", "@TableField")),

  /** mybatis-plus的基础父类信息 */
  MYBATIS_PLUS_MAPPER_BASE(
      PkgBuildMethod.classInfo("com.baomidou.mybatisplus.core.mapper", "BaseMapper")),

  /** 领域服务中的id生成 */
  DOMAIN_ID_GENERATE(
      ImportPackageInfo.builder()
          .pkgPath("com.paraview.uid")
          .className("UidGenerator")
          .classComment("分布式id生成")
          .varName("idGenerator")
          .annotation("@Autowired\n" + "    @Qualifier(\"cachedUidGenerator\")")
          .build()),

  /** 批量生成数据的参数 */
  DOMAIN_BATCH_ID_LIST(
      ImportPackageInfo.builder()
          .pkgPath(JavaKeyWord.IMPORT_LIST)
          .className("List")
          .classComment("批量操作的数据集")
          .varName("batchData")
          .build()),

  /** @Qualifier注解 */
  SPRING_BOOT_QUALIFIER(
      PkgBuildMethod.getAnnotationPkg(
          "org.springframework.beans.factory.annotation", "Qualifier", "@Qualifier"));

  /** 包定义信息 */
  private final ImportPackageInfo packageInfo;

  private ImportCodePackageKey(ImportPackageInfo pkgInfo) {
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
