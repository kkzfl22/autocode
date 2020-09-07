package com.liujun.micro.autocode.generator.builder.constant;

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

  /** 领域层的存储接口 */
  PERSIST_FACADE("persist_facade"),

  /** 领域层的存储实现 */
  PERSIST_PERSISTENCE("persist_persistence"),

  /** 领域层的实体对象 */
  DOMAIN_DO("domain_object"),



  ;

  private String key;

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
