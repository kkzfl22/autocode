package com.liujun.auto.generator.database.service.table.mysql;

import com.liujun.auto.generator.database.entity.TableColumnDTO;
import com.liujun.auto.generator.database.entity.TableIndexColumnDTO;
import com.liujun.auto.generator.database.entity.TableIndexDTO;
import com.liujun.auto.generator.database.entity.TableInfoDTO;
import com.liujun.auto.generator.database.service.table.DatabaseProcessInf;
import com.liujun.auto.constant.Symbol;
import com.liujun.auto.generator.database.service.table.TableParseFunction;
import com.liujun.auto.utils.DataBaseUtils;
import lombok.extern.slf4j.Slf4j;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.function.Function;

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
      "select table_name,table_type,table_comment from information_schema.tables  where table_type = 'BASE TABLE' and  table_schema = ? ";

  /** 查询所有的列信息 */
  private static final String QUERY_ALL_COLUMN =
      "select t.table_name,t.column_name,t.data_type,t.COLUMN_COMMENT,t.COLUMN_KEY,t.EXTRA,"
          + "t.NUMERIC_PRECISION,t.NUMERIC_SCALE,t.CHARACTER_MAXIMUM_LENGTH,t.CHARACTER_OCTET_LENGTH,"
          + "t.IS_NULLABLE,t.COLUMN_DEFAULT,t.COLUMN_TYPE "
          + "from information_schema.COLUMNS t  "
          + "where  t.table_schema = ?  "
          + "and t.table_name in ( "
          + "select table_name from information_schema.tables scma "
          + "where  scma.table_type = 'BASE TABLE') "
          + "order by t.table_name";

  /** 查询所有的索引信息，关过滤主键类型 */
  private static final String INDEX_QUERY =
      "select CONSTRAINT_NAME,TABLE_NAME,CONSTRAINT_TYPE from information_schema.TABLE_CONSTRAINTS "
          + "where table_schema = ? ";

  /** 查询索引关联的列信息 */
  private static final String INDEX_COLUMN_QUERY =
      "select CONSTRAINT_NAME,TABLE_NAME,COLUMN_NAME,ORDINAL_POSITION "
          + "from information_schema.KEY_COLUMN_USAGE where  table_schema = ?";

  public static final DatabaseMysqlProcessImpl INSTANCE = new DatabaseMysqlProcessImpl();

  @Override
  public Map<String, TableInfoDTO> getTableInfo(String tableFlag) {

    // 数据查询操作
    List<TableInfoDTO> dataList =
        query(QUERY_ALL_TABLE, Arrays.asList(tableFlag), DatabaseMysqlProcessImpl::parseTable);

    // 按map的查询返回
    Map<String, TableInfoDTO> map = new HashMap<>(dataList.size(), 1);
    for (TableInfoDTO tableInfo : dataList) {
      map.put(tableInfo.getTableName(), tableInfo);
    }

    return map;
  }

  /**
   * 通用的一个查询
   *
   * @param sql
   * @param param
   * @param parseFun
   * @param <T>
   * @return
   */
  private static <T> List<T> query(
      String sql, List<Object> param, TableParseFunction<ResultSet, T> parseFun) {
    List<T> result = new ArrayList<>();

    Connection conn = null;
    PreparedStatement pstmt = null;
    ResultSet rs = null;
    try {
      conn = MysqlJdbcUtils.getConnection();
      pstmt = conn.prepareStatement(sql);

      // 进行参数的设置操作
      if (null != param && !param.isEmpty()) {
        for (int i = 0; i < param.size(); i++) {
          pstmt.setObject(i + 1, param.get(i));
        }
      }

      rs = pstmt.executeQuery();

      while (rs.next()) {
        result.add(parseFun.apply(rs));
      }
    } catch (SQLException e) {
      e.printStackTrace();
      log.error("query ", e);
    } finally {
      DataBaseUtils.close(rs);
      DataBaseUtils.close(pstmt);
      DataBaseUtils.close(conn);
    }

    return result;
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
    String tableType = rs.getString("table_type");
    // 在存储时所有字母小写
    tableName = tableName.toLowerCase();
    int spitIndex = columnName.indexOf(Symbol.SEMICOLON);
    if (spitIndex != -1) {
      columnName = columnName.substring(0, spitIndex);
    }

    TableInfoDTO tableInfo = new TableInfoDTO(tableName, columnName);
    tableInfo.setTableType(tableType);

    return tableInfo;
  }

  @Override
  public Map<String, List<TableColumnDTO>> getTableColumn(String tableSpace) {

    // 数据查询操作
    List<TableColumnDTO> dataList =
        query(QUERY_ALL_COLUMN, Arrays.asList(tableSpace), DatabaseMysqlProcessImpl::parse);

    List<TableColumnDTO> tempList;
    // 按map的查询返回
    Map<String, List<TableColumnDTO>> map = new HashMap<>(dataList.size(), 1);
    for (TableColumnDTO tableColumnInfo : dataList) {
      tempList = map.get(tableColumnInfo.getTableName());
      if (!map.containsKey(tableColumnInfo.getTableName())) {
        tempList = new ArrayList<>();
        map.put(tableColumnInfo.getTableName(), tempList);
      }
      tempList.add(tableColumnInfo);
    }

    // 再查询索引信息

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
    String columnKey = rs.getString("COLUMN_KEY");
    String extra = rs.getString("EXTRA");
    String precision = rs.getString("NUMERIC_PRECISION");
    String scale = rs.getString("NUMERIC_SCALE");
    String charMax = rs.getString("CHARACTER_MAXIMUM_LENGTH");
    String octLength = rs.getString("CHARACTER_OCTET_LENGTH");
    String isNullAble = rs.getString("IS_NULLABLE");
    String columnDefault = rs.getString("COLUMN_DEFAULT");
    String columnType = rs.getString("COLUMN_TYPE");

    columnName = columnName.toLowerCase();

    boolean priKey = "PRI".equals(columnKey);
    boolean nullFlag = "YES".equals(isNullAble);
    TableColumnDTO bean =
        new TableColumnDTO(columnName, columnMsg, dataType, priKey, nullFlag, columnDefault);

    bean.setColumnKey(columnKey);

    // 数值类型的长度
    if (null != precision) {
      bean.setDataLength(Long.parseLong(precision));
    }
    // 数据的存储长度
    if (null != octLength) {
      bean.setDataLength(Long.parseLong(octLength));
    }
    // 数据的长度
    if (null != charMax) {
      bean.setDataLength(Long.parseLong(charMax));
    }
    // 精度
    if (null != scale) {
      bean.setDataScale(Integer.parseInt(scale));
    }

    bean.setAutoIncrement("auto_increment".equals(extra));
    bean.setTableName(tableName.toLowerCase());
    bean.setSqlColumnType(columnType);

    // 检查数据库创建表语句中是否存在长度
    int bracketStartIndex = bean.getSqlColumnType().indexOf(Symbol.BRACKET_LEFT);

    // 如果两个长度不一，使用建表语句为准
    if (bracketStartIndex != -1) {
      int bracketIndexEnd = bean.getSqlColumnType().indexOf(Symbol.BRACKET_RIGHT);
      String valueStr = columnType.substring(bracketStartIndex + 1, bracketIndexEnd);

      if (valueStr.indexOf(Symbol.COMMA) != -1) {
        String[] dataScale = valueStr.split(Symbol.COMMA);
        // 设置数据长度
        long sqlDataLength = Long.parseLong(dataScale[0]);
        if (null == bean.getDataLength() || sqlDataLength != bean.getDataLength()) {
          bean.setDataLength(sqlDataLength);
        }

        // 设置精度
        int scaleValue = Integer.parseInt(dataScale[1]);
        if (null == bean.getDataScale() || scaleValue != bean.getDataScale()) {
          bean.setDataScale(scaleValue);
        }

      } else {
        long sqlDataLength = Long.parseLong(valueStr);
        if (null == bean.getDataLength() || sqlDataLength != bean.getDataLength()) {
          bean.setDataLength(sqlDataLength);
        }
      }
    }

    if (bean.getDataLength() == null) {
      bean.setDataLength(0L);
    }

    return bean;
  }

  @Override
  public Map<String, Map<String, TableIndexDTO>> getTableIndex(String tableFlag) {

    // 数据查询操作
    List<TableIndexDTO> dataList =
        query(INDEX_QUERY, Arrays.asList(tableFlag), DatabaseMysqlProcessImpl::parseTableIndex);

    if (null == dataList || dataList.isEmpty()) {
      return Collections.emptyMap();
    }

    // 再查询索引关联的列信息
    List<TableIndexColumnDTO> indexColumnList =
        query(
            INDEX_COLUMN_QUERY,
            Arrays.asList(tableFlag),
            DatabaseMysqlProcessImpl::parseTableIndexColumn);

    // 表列map,做完了合并与排序
    Map<String, Map<String, List<TableIndexColumnDTO>>> tableColumnMap =
        groupTableIndexColumn(dataList.size(), indexColumnList);

    // 一级表名，二级索引名，
    Map<String, Map<String, TableIndexDTO>> result = new HashMap<>(dataList.size(), 1);

    // 进行列填充操作
    for (TableIndexDTO tableInfo : dataList) {

      if (tableInfo.getTableName().equals("sys_i18n_message")) {
        System.out.println("find");
      }

      Map<String, List<TableIndexColumnDTO>> indexColumnMap =
          tableColumnMap.get(tableInfo.getTableName());
      if (null == indexColumnMap || indexColumnMap.isEmpty()) {
        continue;
      }

      List<TableIndexColumnDTO> indexColumn = indexColumnMap.get(tableInfo.getConstraintName());
      tableInfo.setColumn(indexColumn);

      Map<String, TableIndexDTO> tableIndexMap = result.get(tableInfo.getTableName());

      if (null == tableIndexMap) {
        tableIndexMap = new HashMap<>();
        result.put(tableInfo.getTableName(), tableIndexMap);
      }

      tableIndexMap.put(tableInfo.getConstraintName(), tableInfo);
    }

    return result;
  }

  /**
   * 按表名为一级key，索引名称为二级key
   *
   * @param tableIndexSize
   * @param indexColumnList
   * @return
   */
  private static Map<String, Map<String, List<TableIndexColumnDTO>>> groupTableIndexColumn(
      int tableIndexSize, List<TableIndexColumnDTO> indexColumnList) {
    // 进行列索引相关数据的填充操作
    // 按map的查询
    Map<String, Map<String, List<TableIndexColumnDTO>>> map = new HashMap<>(tableIndexSize, 1);
    for (TableIndexColumnDTO indexColumn : indexColumnList) {
      // 一级以表名为key
      Map<String, List<TableIndexColumnDTO>> tableIndexMap = map.get(indexColumn.getTableName());
      if (null == tableIndexMap) {
        tableIndexMap = new HashMap<>();
        map.put(indexColumn.getTableName(), tableIndexMap);
      }

      // 二级以索引名称为key
      List<TableIndexColumnDTO> tableList = tableIndexMap.get(indexColumn.getConstraintName());
      if (null == tableList) {
        tableList = new ArrayList<>();
        tableIndexMap.put(indexColumn.getConstraintName(), tableList);
      }

      tableList.add(indexColumn);
    }

    // 完成后执行排序操作
    for (Map.Entry<String, Map<String, List<TableIndexColumnDTO>>> entryItem : map.entrySet()) {
      for (Map.Entry<String, List<TableIndexColumnDTO>> twoItem : entryItem.getValue().entrySet()) {
        if (twoItem.getValue().isEmpty()) {
          continue;
        }
        if (twoItem.getValue().size() == 1) {
          continue;
        }
        List<TableIndexColumnDTO> columnIndex = twoItem.getValue();
        Collections.sort(columnIndex);
        twoItem.setValue(columnIndex);
      }
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
  private static TableIndexDTO parseTableIndex(ResultSet rs) throws SQLException {

    String constraintName = rs.getString("CONSTRAINT_NAME");
    String tableName = rs.getString("TABLE_NAME");
    String constraintType = rs.getString("CONSTRAINT_TYPE");

    // 在存储时所有字母小写
    tableName = tableName.toLowerCase();

    TableIndexDTO tableIndex = new TableIndexDTO();

    tableIndex.setConstraintName(constraintName);
    tableIndex.setConstraintType(constraintType);
    tableIndex.setTableName(tableName);

    return tableIndex;
  }

  /**
   * 转换为表的实体信息
   *
   * @param rs 结果集
   * @return 表信息
   * @throws SQLException 异常
   */
  private static TableIndexColumnDTO parseTableIndexColumn(ResultSet rs) throws SQLException {

    String constraintName = rs.getString("CONSTRAINT_NAME");
    String tableName = rs.getString("TABLE_NAME");
    String columnName = rs.getString("COLUMN_NAME");
    int ordinalPosition = rs.getInt("ORDINAL_POSITION");

    // 在存储时所有字母小写
    tableName = tableName.toLowerCase();

    TableIndexColumnDTO tableIndex = new TableIndexColumnDTO();

    tableIndex.setConstraintName(constraintName);
    tableIndex.setTableName(tableName);
    tableIndex.setColumnName(columnName);
    tableIndex.setOrdinalPosition(ordinalPosition);

    return tableIndex;
  }
}
