package com.liujun.auto.constant;

/**
 * 全局的枚举配制
 *
 * @author liujun
 * @version 0.0.1
 */
public enum ConfigEnum {

  /** 数据库的驱动 */
  DRIVER_NAME("driverClassName"),

  /** 连接的URL */
  CONN_URL("url"),

  /** 用户名 */
  USER_NAME("username"),

  /** 密码 */
  USER_PASSWORD("password"),

  /** 初始连接 */
  CONN_INIT("initialSize"),

  /** 最大连接数 */
  MAX_ACTIVE("maxActive"),

  /** 最小连接池数量 */
  CONN_IDLE("minIdle"),

  /** 获取连接时最大等待时间，单位毫秒 */
  CONN_MAX_IDLE("maxWait"),

  /** 数据库的类型 */
  DATABASE_TYPE("dbtype"),
  ;

  /** 配制的key */
  private final String key;

  ConfigEnum(String key) {
    this.key = key;
  }

  /**
   * 获取配制参数所对应的枚举
   *
   * @param key
   * @return
   */
  public static ConfigEnum getCfg(String key) {
    ConfigEnum cfg = null;
    for (int i = 0; i < values().length; i++) {
      cfg = values()[i];
      if (cfg.getKey().equals(key)) {
        return cfg;
      }
    }
    return null;
  }

  public String getKey() {
    return key;
  }

  @Override
  public String toString() {
    final StringBuilder sb = new StringBuilder("ConfigEnum{");
    sb.append("key='").append(key).append('\'');
    sb.append('}');
    return sb.toString();
  }
}
