package com.liujun.auto.generator.builder.constant;

/**
 * 构建领域驱动设计的相关的key
 *
 * @author liujun
 * @version 0.0.1
 */
public enum GenerateCodePackageKey {

  /** 存储层的资源实体 */
  PERSIST_PO("persist_object"),

  /** 数据库操作的接口 */
  PERSIST_DAO("persist_dao"),

  /** 领域层与存储层的转换类 */
  PERSIST_ASSEMBLER("persist_assembler"),

  /** mybatis的xml文件 */
  PERSIST_MYBATIS_MAPPER_XML("persist_mybatis_mapper_xml"),

  /** 领域层的存储实现 */
  PERSIST_PERSISTENCE("persist_persistence"),

  /** dao的单元测试 */
  PERSIST_JUNIT_DAO("persist_junit_dao"),

  /** 领域层的实体对象 */
  DOMAIN_DO("domain_object"),

  /** 领域层的存储接口 */
  DOMAIN_PERSIST_FACADE("persist_facade"),

  /** 领域服务 */
  DOMAIN_SERVICE("domain_service"),

  /** 将数据库层进行mock化 */
  DOMAIN_MOCK_MAPPER("domain_mock_mapper"),

  /** 领域的单元测试服务 */
  DOMAIN_JUNIT_SERVICE("domain_junit_service"),

  /** 应用服务 */
  APPLICATION_SERVICE("application_service"),

  /** 对外的ＡＰＩ对象 */
  INTERFACE_FACADE("interface_facade"),

  /** 参数校验 */
  INTERFACE_VALID_PARAM("interface_valid_param"),

  /** api层的传输对象 */
  INTERFACE_OBJECT("interface_dto"),

  /** api与领域对象的转换 */
  INTERFACE_ASSEMBLER("interface_assembler"),

  /** 错误前端错误码对象信息 */
  INTERFACE_ERROR_CODE("interface_errorCode"),

  /** 错误检查的静态常量 */
  INTERFACE_ERROR_CONSTANT("interface_error_constant"),

  /** 参数的检查 */
  INTERFACE_CHECK_PARAM("interface_check_param"),

  /** 存储的配制 */
  REPOSITORY_MAPPER_CONFIG("repository_mapper_config"),

  /** 分页的对象信息 */
  DOMAIN_PAGE_ENTITY("domain_page_entity"),
  ;

  private final String key;

  GenerateCodePackageKey(String key) {
    this.key = key;
  }

  public String getKey() {
    return key;
  }

  @Override
  public String toString() {
    final StringBuilder sb = new StringBuilder("CreateCommKey{");
    sb.append("key='").append(key).append('\'');
    sb.append('}');
    return sb.toString();
  }
}
