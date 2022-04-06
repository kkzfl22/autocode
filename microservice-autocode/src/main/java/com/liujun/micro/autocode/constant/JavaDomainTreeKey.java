package com.liujun.micro.autocode.constant;

/**
 * java代码目录树中的名称
 *
 * @author liujun
 * @version 0.0.1
 */
public enum JavaDomainTreeKey {
  /** 应用层顶层目录 */
  APPLICATION("application"),

  /** 领域层目录 */
  DOMAIN("domain"),

  /** 领域对象中的实体层 */
  DOMAIN_CONSTANT("constant"),

  /** 领域层中的实体 */
  DOMAIN_ENTITY("entity"),

  /** 领域层中的存储 */
  DOMAIN_REPOSITORY("repository"),

  /** 存储层的目录结构 */
  REPOSITORY_ASSEMBLER("converter"),

  /** 存储层的与领域层交互的接口 */
  REPOSITORY_FACADE("facade"),

  /** 存储层的数据库接口 */
  REPOSITORY_MAPPER("mapper"),

  /** 在mapper目录下添加配制,用于单元测试 */
  MAPPER_CONFIG("config"),

  /** 存储层与领域层交互的接口实现 */
  REPOSITORY_PERSISTENCE("persistence"),

  /** 存储的接口实体 */
  REPOSITORY_PO("po"),

  /** 领域层中的服务 */
  DOMAIN_SERVICE("service"),

  /** 基础资源目录 */
  INFRASTRUCTURE("infrastructure"),

  /** 基础资源的算法目录 */
  INFRASTRUCTURE_ALGORITHM("algorithm"),

  /** 基础的api相关目录 */
  INFRASTRUCTURE_API("api"),

  /** 基础层中作为客户端调用其他服务的接口 */
  INFRASTRUCTURE_CLIENT("client"),

  /** 基础层中的公共信息 */
  INFRASTRUCTURE_COMM("comm"),

  /** 用于作资源请求验证的公共类 */
  COMM_CHECK("check"),

  /** 异常相关类 */
  COMM_EXCEPTION("exception"),

  /** 使用mq进行消息通道的传输操作 */
  COMM_RABBITMQ("rabbitmq"),

  /** 公共的处理类 */
  COMM_UTILS("utils"),

  /** 转换类 */
  RABBITMQ_ASSEMBLER("assembler"),

  /** rabbitmq的配制信息 */
  RABBITMQ_CONFIG("config"),

  /** 进行mq消息通讯的实体 */
  RABBITMQ_ENTITY("entity"),

  /** 事件处理 */
  RABBITMQ_EVENT("event"),

  /** 基础层中公共 */
  INFRASTRUCTURE_CONSTANT("constant"),

  /** 基础资源的公共实体 */
  INFRASTRUCTURE_ENTITY("entity"),

  /** 对外的API目录 */
  INTERFACE("facade"),

  /** 参数校验的类 */
  INTERFACE_VALID("validator"),

  /** 类转换处理,将前端的类转换为领域层对象 */
  INTERFACE_ASSEMBLER("assembler"),

  /** 接收接口后的参数校验 */
  INTERFACE_CHECK("check"),

  /** 接口的公共参数 */
  INTERFACE_COMM("comm"),

  /** 接口的参数实体目录 */
  INTERFACE_DTO("dto"),

  /** 配制信息 */
  INTERFACE_CONFIG("config"),

  /** 对外服务接口 */
  INTERFACE_FACADE("facade"),

  /** 统一的错误码 */
  INTERFACE_ERROR_CODE("errorcode"),

  /** 静态常量信息 */
  INTERFACE_CONSTANT("constant"),
  ;

  private final String key;

  JavaDomainTreeKey(String key) {
    this.key = key;
  }

  public String getKey() {
    return key;
  }
}
