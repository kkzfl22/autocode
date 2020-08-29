package com.liujun.micro.autocode.generator.database.service.tableInfo.mysql;

import com.liujun.micro.autocode.constant.Symbol;
import com.liujun.micro.autocode.generator.database.entity.TableColumnDTO;
import com.liujun.micro.autocode.generator.database.entity.TableInfoDTO;
import com.liujun.micro.autocode.generator.database.service.tableInfo.DatabaseProcessInf;
import com.liujun.micro.autocode.utils.DataBaseUtils;
import lombok.extern.slf4j.Slf4j;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * mysql相关的数据库操作
 *
 * @author liujun
 * @version 0.0.1
 */
@Slf4j
public class DatabaseMysqlProcessImpl implements DatabaseProcessInf {

  /** 查询所有的表信息 */
  private static final String QUERY_ALL_TABLE =
      "select table_name,table_comment from information_schema.tables  where table_schema = ? ";

  /** 查询所有的列信息 */
  private static final String QUERY_ALL_COLUMN =
      "select table_name,column_name,data_type,COLUMN_COMMENT,COLUMN_KEY,EXTRA,"
          + "NUMERIC_PRECISION,NUMERIC_SCALE,CHARACTER_MAXIMUM_LENGTH,CHARACTER_OCTET_LENGTH,"
          + "IS_NULLABLE,COLUMN_DEFAULT "
          + "from information_schema.COLUMNS where table_schema = ? order by table_name";

  public static final DatabaseMysqlProcessImpl INSTANCE = new DatabaseMysqlProcessImpl();

  /** * 初始化大小 */
  private static final int INIT_SIZE = 16;

  @Override
  public Map<String, TableInfoDTO> getTableInfo(String tableFlag) {
    Map<String, TableInfoDTO> map = new HashMap<>(INIT_SIZE);

    Connection conn = null;
    PreparedStatement pstmt = null;
    ResultSet rs = null;
    try {
      conn = MysqlJdbcUtils.getConnection();
      pstmt = conn.prepareStatement(QUERY_ALL_TABLE);
      pstmt.setString(1, tableFlag);

      rs = pstmt.executeQuery();

      TableInfoDTO tableInfo;
      while (rs.next()) {
        tableInfo = parseTable(rs);
        map.put(tableInfo.getTableName(), tableInfo);
      }
    } catch (SQLException e) {
      e.printStackTrace();
      log.error("getTableInfo ", e);
    } finally {
      DataBaseUtils.close(rs);
      DataBaseUtils.close(pstmt);
      DataBaseUtils.close(conn);
    }

    return map;
  }

  /**
   * 转换为表的实体信息
   *
   * @param rs 结果集
   * @return 表信息
   * @throws SQLException 异常
   */
  private static TableInfoDTO parseTable(ResultSet rs) throws SQLException {
    String tableName = rs.getString("table_name");
    String columnName = rs.getString("table_comment");
    // 在存储时所有字母小写
    tableName = tableName.toLowerCase();
    int spitIndex = columnName.indexOf(Symbol.SEMICOLON);
    if (spitIndex != -1) {
      columnName = columnName.substring(0, spitIndex);
    }

    return new TableInfoDTO(tableName, columnName);
  }

  @Override
  public Map<String, List<TableColumnDTO>> getTableColumn(String tableSpace) {
    Map<String, List<TableColumnDTO>> map = new HashMap<>(INIT_SIZE);

    Connection conn = null;
    PreparedStatement pstmt = null;
    ResultSet rs = null;
    try {
      // 获取连接
      conn = MysqlJdbcUtils.getConnection();
      pstmt = conn.prepareStatement(QUERY_ALL_COLUMN);
      // 设置查询的命名空间
      pstmt.setString(1, tableSpace);
      rs = pstmt.executeQuery();

      TableColumnDTO tableColumnInfo = null;
      List<TableColumnDTO> tempList = null;
      while (rs.next()) {
        tableColumnInfo = parse(rs);
        tempList = map.get(tableColumnInfo.getTableName());
        if (!map.containsKey(tableColumnInfo.getTableName())) {
          tempList = new ArrayList<>();
          map.put(tableColumnInfo.getTableName(), tempList);
        }
        tempList.add(tableColumnInfo);
      }
    } catch (SQLException e) {
      e.printStackTrace();
      log.error("SQLException ", e);
    } catch (Exception e) {
      e.printStackTrace();
      log.error("Exception ", e);
    } finally {
      DataBaseUtils.close(rs);
      DataBaseUtils.close(pstmt);
      DataBaseUtils.close(conn);
    }

    return map;
  }

  /**
   * 数据类型信息转换操作
   *
   * @param rs 结果集
   * @return 返回的java对象
   * @throws SQLException sql异常
   */
  private static TableColumnDTO parse(ResultSet rs) throws SQLException {

    String tableName = rs.getString("table_name");
    String columnName = rs.getString("column_name");
    String columnMsg = rs.getString("COLUMN_COMMENT");
    String dataType = rs.getString("DATA_TYPE");
    String primaryKey = rs.getString("COLUMN_KEY");
    String extra = rs.getString("EXTRA");
    String precision = rs.getString("NUMERIC_PRECISION");
    String scale = rs.getString("NUMERIC_SCALE");
    String charMax = rs.getString("CHARACTER_MAXIMUM_LENGTH");
    String octLength = rs.getString("CHARACTER_OCTET_LENGTH");
    String isNullAble = rs.getString("IS_NULLABLE");
    String columnDefault = rs.getString("COLUMN_DEFAULT");

    columnName = columnName.toLowerCase();

    boolean priKey = "PRI".equals(primaryKey) ? true : false;
    boolean nullFlag = "YES".equals(isNullAble) ? true : false;
    TableColumnDTO bean =
        new TableColumnDTO(columnName, columnMsg, dataType, priKey, nullFlag, columnDefault);
    if (null != precision) {
      bean.setDataLength(Integer.parseInt(precision));
    }

    if (null != octLength) {
      bean.setDataLength(Integer.parseInt(octLength));
    }

    if (null != charMax) {
      bean.setDataLength(Integer.parseInt(charMax));
    }

    if (null != scale) {
      bean.setDataScale(Integer.parseInt(scale));
    }
    bean.setAutoIncrement("auto_increment".equals(extra) ? true : false);
    bean.setTableName(tableName);

    return bean;
  }
}
