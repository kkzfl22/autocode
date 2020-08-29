package com.liujun.micro.autocode.generator.database.service.tableInfo.mysql;

import com.alibaba.druid.pool.DruidDataSourceFactory;
import com.liujun.micro.autocode.config.properties.ConfigProperties;
import com.liujun.micro.autocode.constant.ConfigEnum;

import javax.sql.DataSource;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.Map;

/**
 * @author liujun
 * @version 0.0.1
 */
public class MysqlJdbcUtils {

  /** 数据库连接 */
  private static final DataSource DATASOURCE = getDataSource();

  /**
   * 数据源连接配制
   *
   * @return 配制信息
   */
  private static Map getDruidDataSource() {
    Map cfg = new HashMap(ConfigEnum.values().length);
    for (ConfigEnum cfgEnum : ConfigEnum.values()) {
      cfg.put(cfgEnum.getKey(), ConfigProperties.getInstance().getValue(cfgEnum));
    }
    return cfg;
  }

  /**
   * 获取数据源连接
   *
   * @return
   */
  private static DataSource getDataSource() {
    DataSource dataSource = null;
    try {
      // 初始化数据源信息
      dataSource = DruidDataSourceFactory.createDataSource(getDruidDataSource());
    } catch (Exception e) {
      e.printStackTrace();
      throw new IllegalArgumentException("curr DruidDataSourceFactory error");
    }

    return dataSource;
  }

  /**
   * 获取数据库连接
   *
   * @return 连接信息
   * @throws SQLException 异常信息
   */
  public static Connection getConnection() throws SQLException {
    return DATASOURCE.getConnection();
  }
}
