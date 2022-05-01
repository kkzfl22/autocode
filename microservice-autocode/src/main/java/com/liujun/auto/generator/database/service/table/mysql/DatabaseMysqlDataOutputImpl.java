package com.liujun.auto.generator.database.service.table.mysql;

import com.liujun.auto.constant.Symbol;
import com.liujun.auto.generator.database.constant.DatabaseTypeEnum;
import com.liujun.auto.generator.database.constant.DbToJavaTypeValueEnum;
import com.liujun.auto.generator.database.constant.MysqlKeyWord;
import com.liujun.auto.generator.database.constant.SqlKeyWord;
import com.liujun.auto.generator.database.entity.TableColumnDTO;
import com.liujun.auto.generator.database.entity.TableDataOutDTO;
import com.liujun.auto.utils.DataBaseUtils;
import lombok.extern.slf4j.Slf4j;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.function.BiFunction;

/**
 * mysql相关的数据库操作
 *
 * @author liujun
 * @version 0.0.1
 */
@Slf4j
public class DatabaseMysqlDataOutputImpl {

  public static final DatabaseMysqlDataOutputImpl INSTANCE = new DatabaseMysqlDataOutputImpl();

  /**
   * 数据查询结果
   *
   * @param tableOut 表结构信息
   * @return
   */
  public List<Map<String, Object>> dbQueryResult(TableDataOutDTO tableOut) {
    // 执行mysql的分页逻辑的计算
    tableOut.runMysqlCount();

    return this.dbRunQuery(tableOut, this::dataOutParseResult);
  }

  /**
   * 执行分页查询操作
   *
   * @param tableOut
   * @param dataParse
   * @return
   */
  private List<Map<String, Object>> dbRunQuery(
      TableDataOutDTO tableOut,
      BiFunction<ResultSet, TableDataOutDTO, Map<String, Object>> dataParse) {
    Connection conn = null;
    Statement pstmt = null;
    ResultSet rs = null;

    // 构建查询的SQL语句
    String sql = builderSql(tableOut);

    // 导数据，集合一般较大
    List<Map<String, Object>> tempList = new ArrayList<>(tableOut.getSize());

    try {
      // 获取连接
      conn = MysqlJdbcUtils.getConnection();
      pstmt = conn.createStatement();
      rs = pstmt.executeQuery(sql);

      Map<String, Object> tableColumnInfo = null;

      while (rs.next()) {
        // 执行列的转换操作
        tableColumnInfo = dataParse.apply(rs, tableOut);
        tempList.add(tableColumnInfo);
      }
    } catch (SQLException e) {
      log.error("SQLException ", e);
    } catch (Exception e) {
      log.error("Exception ", e);
    } finally {
      DataBaseUtils.close(rs);
      DataBaseUtils.close(pstmt);
      DataBaseUtils.close(conn);
    }

    return tempList;
  }

  private Map<String, Object> dataOutParseResult(ResultSet rs, TableDataOutDTO column) {
    Map<String, Object> dataValueMap = new HashMap<>(column.getTableColumn().getValue().size());

    try {
      for (TableColumnDTO columnInfo : column.getTableColumn().getValue()) {
        Object value = null;
        value =
            DbToJavaTypeValueEnum.getValue(
                DatabaseTypeEnum.MYSQL, columnInfo.getDataType(), rs, columnInfo.getColumnName());

        dataValueMap.put(columnInfo.getColumnName(), value);
      }
    } catch (SQLException sql) {
      sql.printStackTrace();
    }

    return dataValueMap;
  }

  /**
   * 生成导出的数据的SQL信息
   *
   * @param columnInfo
   * @return
   */
  private String builderSql(TableDataOutDTO columnInfo) {
    StringBuilder outSql = new StringBuilder();

    outSql.append(SqlKeyWord.SELECT).append(Symbol.SPACE);

    // 输出查询的列信息
    List<TableColumnDTO> columnList = columnInfo.getTableColumn().getValue();
    for (int i = 0; i < columnList.size(); i++) {
      outSql.append(columnList.get(i).getColumnName());
      if (i != columnList.size() - 1) {
        outSql.append(Symbol.COMMA);
      }
    }

    // 输出查询
    outSql.append(Symbol.SPACE);
    outSql.append(SqlKeyWord.FROM).append(Symbol.SPACE);
    outSql.append(columnInfo.getTableColumn().getKey());

    outSql.append(Symbol.SPACE);
    outSql.append(MysqlKeyWord.LIMIT).append(Symbol.SPACE);
    outSql.append(columnInfo.getStartIndex());
    outSql.append(Symbol.COMMA);
    outSql.append(columnInfo.getSize());

    return outSql.toString();
  }
}
