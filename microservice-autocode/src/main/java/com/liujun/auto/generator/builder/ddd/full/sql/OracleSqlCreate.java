package com.liujun.auto.generator.builder.ddd.full.sql;

import com.liujun.auto.constant.Symbol;
import com.liujun.auto.generator.builder.GenerateCodeInf;
import com.liujun.auto.generator.builder.ddd.entity.GenerateCodeContext;
import com.liujun.auto.generator.builder.utils.GenerateOutFileUtils;
import com.liujun.auto.generator.builder.utils.GeneratePathUtils;
import com.liujun.auto.generator.builder.utils.TableColumnUtils;
import com.liujun.auto.generator.convergence.TypeConvergence;
import com.liujun.auto.generator.database.constant.DatabaseTypeEnum;
import com.liujun.auto.generator.database.entity.TableColumnDTO;
import com.liujun.auto.generator.database.entity.TableInfoDTO;
import com.liujun.auto.generator.javalanguage.serivce.NameProcess;
import org.apache.commons.lang3.StringUtils;

import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

/**
 * 输出oracle的标准建表语句
 *
 * @author liujun
 * @version 1.0.0
 */
public class OracleSqlCreate implements GenerateCodeInf {

  public static final OracleSqlCreate INSTANCE = new OracleSqlCreate();

  @Override
  public void generateCode(GenerateCodeContext param) {

    Map<String, TableInfoDTO> tableMap = param.getTableMap();
    Map<String, List<TableColumnDTO>> map = param.getColumnMapList();
    Iterator<Entry<String, List<TableColumnDTO>>> tableNameEntry = map.entrySet().iterator();

    StringBuilder outOracleSql = new StringBuilder();

    while (tableNameEntry.hasNext()) {
      Entry<String, List<TableColumnDTO>> tableNameItem = tableNameEntry.next();

      // 表名
      String tableName = tableNameItem.getKey();

      TableInfoDTO tableInfo = tableMap.get(tableName);

      // 得到类名
      String tableClassName = NameProcess.INSTANCE.toJavaClassName(tableName);

      // 输出一个表的注释
      outOracleSql.append(columnSql(tableInfo, tableNameItem, param));

      outOracleSql.append(Symbol.ENTER_LINE);
    }

    // 输到文件中
    GenerateOutFileUtils.outFile(
        outOracleSql, GeneratePathUtils.outServicePath(param), "", "sql-oracle-schema.sql");
  }

  private String columnSql(
      TableInfoDTO tableInfo,
      Entry<String, List<TableColumnDTO>> tableNameItem,
      GenerateCodeContext param) {
    StringBuilder outOracleSql = new StringBuilder();

    outOracleSql
        .append(" DROP TABLE IF EXISTS ")
        .append(Symbol.QUOTE)
        .append(tableInfo.getTableName())
        .append(Symbol.QUOTE)
        .append(Symbol.SEMICOLON);

    outOracleSql.append(Symbol.ENTER_LINE);
    outOracleSql.append(Symbol.ENTER_LINE);

    // 输出列信息及主键
    outOracleSql.append(outColumn(tableInfo, tableNameItem, param));

    // 输出注释
    outOracleSql.append(outComment(tableInfo, tableNameItem));

    outOracleSql.append(Symbol.ENTER_LINE);
    outOracleSql.append(Symbol.ENTER_LINE);

    return outOracleSql.toString();
  }

  private String outColumn(
      TableInfoDTO tableInfo,
      Entry<String, List<TableColumnDTO>> tableNameItem,
      GenerateCodeContext param) {
    StringBuilder outOracleSql = new StringBuilder();

    outOracleSql
        .append("CREATE TABLE ")
        .append(Symbol.QUOTE)
        .append(tableInfo.getTableName())
        .append(Symbol.QUOTE);
    outOracleSql.append(Symbol.BRACKET_LEFT);
    outOracleSql.append(Symbol.ENTER_LINE);

    // 输出列信息
    for (TableColumnDTO column : tableNameItem.getValue()) {
      outOracleSql
          .append(Symbol.QUOTE)
          .append(column.getColumnName())
          .append(Symbol.QUOTE)
          .append(Symbol.SPACE);

      String oracleType =
          TypeConvergence.getDbType(
              param.getTypeEnum(), column.getDataType(), DatabaseTypeEnum.ORACLE);

      outOracleSql.append(oracleType);

      // 长度检查
      if (null != column.getDataLength() && 0L != column.getDataLength()) {
        outOracleSql.append(Symbol.BRACKET_LEFT);
        outOracleSql.append(column.getDataLength());
        outOracleSql.append(Symbol.BRACKET_RIGHT);
      }
      outOracleSql.append(Symbol.SPACE);

      // 为空标识
      if (column.getNullFlag() != null && !column.getNullFlag()) {
        outOracleSql.append("not null");
      }
      outOracleSql.append(Symbol.COMMA);
      outOracleSql.append(Symbol.ENTER_LINE);
    }

    // 输出主键
    // 获取当前主键列表
    List<TableColumnDTO> primaryKeyList = TableColumnUtils.getPrimaryKey(tableNameItem.getValue());
    if (null != primaryKeyList && !primaryKeyList.isEmpty()) {
      StringBuilder primaryKey = new StringBuilder();

      for (int i = 0; i < primaryKeyList.size(); i++) {
        primaryKey
            .append(Symbol.QUOTE)
            .append(primaryKeyList.get(i).getColumnName())
            .append(Symbol.QUOTE);
        if (i != primaryKeyList.size() - 1) {
          primaryKey.append(Symbol.COMMA);
        }
      }
      outOracleSql.append("primary key").append(Symbol.BRACKET_LEFT);
      outOracleSql.append(primaryKey);
      outOracleSql.append(Symbol.BRACKET_RIGHT);
      outOracleSql.append(Symbol.ENTER_LINE);
    } else {
      // 当没有主键，则删除最后那个逗号
      outOracleSql.deleteCharAt(outOracleSql.length() - Symbol.ENTER_LINE.length());
      outOracleSql.deleteCharAt(outOracleSql.length() - Symbol.COMMA.length());
      outOracleSql.append(Symbol.ENTER_LINE);
    }

    outOracleSql.append(Symbol.BRACKET_RIGHT);
    outOracleSql.append(Symbol.SEMICOLON);
    outOracleSql.append(Symbol.ENTER_LINE);

    return outOracleSql.toString();
  }

  private String outComment(
      TableInfoDTO tableInfo, Entry<String, List<TableColumnDTO>> tableNameItem) {
    StringBuilder outOracleSql = new StringBuilder();
    // 输出表注释
    outOracleSql.append(" comment on table").append(Symbol.SPACE);
    outOracleSql
        .append(Symbol.QUOTE)
        .append(tableInfo.getTableName())
        .append(Symbol.QUOTE)
        .append(Symbol.SPACE);
    outOracleSql.append("is '").append(Symbol.SPACE);
    outOracleSql.append(tableInfo.getTableComment());
    outOracleSql.append("'");
    outOracleSql.append(Symbol.SEMICOLON);
    outOracleSql.append(Symbol.ENTER_LINE);

    // 输出列注释
    for (TableColumnDTO column : tableNameItem.getValue()) {
      outOracleSql.append(" comment on column ").append(Symbol.SPACE);
      outOracleSql
          .append(Symbol.QUOTE)
          .append(tableInfo.getTableName())
          .append(Symbol.QUOTE)
          .append(Symbol.POINT);
      outOracleSql
          .append(Symbol.QUOTE)
          .append(column.getColumnName())
          .append(Symbol.QUOTE)
          .append(Symbol.SPACE);
      outOracleSql.append("is").append(Symbol.SPACE);

      outOracleSql.append("'");
      if (StringUtils.isNotEmpty(tableInfo.getTableComment())) {
        outOracleSql.append(column.getColumnMsg());
      } else {
        outOracleSql.append(Symbol.EMPTY);
      }
      outOracleSql.append("'");

      outOracleSql.append(Symbol.SEMICOLON);
      outOracleSql.append(Symbol.ENTER_LINE);
    }

    return outOracleSql.toString();
  }
}
