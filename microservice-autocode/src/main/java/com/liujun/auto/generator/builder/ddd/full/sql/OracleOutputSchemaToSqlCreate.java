package com.liujun.auto.generator.builder.ddd.full.sql;

import com.liujun.auto.constant.Symbol;
import com.liujun.auto.generator.builder.GenerateCodeInf;
import com.liujun.auto.generator.builder.ddd.entity.GenerateCodeContext;
import com.liujun.auto.generator.builder.utils.GenerateOutFileUtils;
import com.liujun.auto.generator.builder.utils.GeneratePathUtils;
import com.liujun.auto.generator.convergence.TypeConvergence;
import com.liujun.auto.generator.database.constant.DatabaseTypeEnum;
import com.liujun.auto.generator.database.constant.MysqlIndexTypeEnum;
import com.liujun.auto.generator.database.constant.SqlKeyWord;
import com.liujun.auto.generator.database.entity.TableColumnDTO;
import com.liujun.auto.generator.database.entity.TableIndexColumnDTO;
import com.liujun.auto.generator.database.entity.TableIndexDTO;
import com.liujun.auto.generator.database.entity.TableInfoDTO;
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
public class OracleOutputSchemaToSqlCreate implements GenerateCodeInf {

  public static final OracleOutputSchemaToSqlCreate INSTANCE = new OracleOutputSchemaToSqlCreate();

  private static final String FILE_NAME = "sql-oracle-schema.sql";

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

      // 表信息
      TableInfoDTO tableInfo = tableMap.get(tableName);

      // SQL的输出
      outOracleSql.append(columnSql(tableInfo, tableNameItem, param));

      outOracleSql.append(Symbol.ENTER_LINE);
    }

    // 输到文件中
    GenerateOutFileUtils.outFile(
        outOracleSql, GeneratePathUtils.outServicePath(param), "", FILE_NAME);
  }

  private String columnSql(
      TableInfoDTO tableInfo,
      Entry<String, List<TableColumnDTO>> tableNameItem,
      GenerateCodeContext param) {
    StringBuilder outOracleSql = new StringBuilder();

    outOracleSql.append(SqlKeyWord.DROP).append(Symbol.SPACE);
    outOracleSql.append(SqlKeyWord.TABLE).append(Symbol.SPACE);
    outOracleSql.append(SqlKeyWord.IF).append(Symbol.SPACE);
    outOracleSql.append(SqlKeyWord.EXISTS).append(Symbol.SPACE);
    outOracleSql.append(Symbol.QUOTE).append(tableInfo.getTableName()).append(Symbol.QUOTE);
    outOracleSql.append(Symbol.SEMICOLON);

    outOracleSql.append(Symbol.ENTER_LINE);
    outOracleSql.append(Symbol.ENTER_LINE);

    // 输出列信息及主键
    outOracleSql.append(outColumn(tableInfo, tableNameItem, param));

    // 输出注释
    outOracleSql.append(outComment(tableInfo, tableNameItem));

    // 约束输出
    outOracleSql.append(outColumnKey(tableInfo, param.getTableIndexMap()));

    outOracleSql.append(Symbol.ENTER_LINE);
    outOracleSql.append(Symbol.ENTER_LINE);

    return outOracleSql.toString();
  }

  /**
   * 列输出
   *
   * @param tableInfo
   * @param tableNameItem
   * @param param
   * @return
   */
  private String outColumn(
      TableInfoDTO tableInfo,
      Entry<String, List<TableColumnDTO>> tableNameItem,
      GenerateCodeContext param) {
    StringBuilder outOracleSql = new StringBuilder();

    outOracleSql.append(SqlKeyWord.CREATE).append(Symbol.SPACE);
    outOracleSql.append(SqlKeyWord.TABLE).append(Symbol.SPACE);
    outOracleSql.append(Symbol.QUOTE).append(tableInfo.getTableName()).append(Symbol.QUOTE);
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
        // 当存在精度时，需需要将精度填充
        if (null != column.getDataScale() && 0 != column.getDataScale()) {
          outOracleSql.append(Symbol.COMMA);
          outOracleSql.append(column.getDataScale());
        }
        outOracleSql.append(Symbol.BRACKET_RIGHT);
      }
      outOracleSql.append(Symbol.SPACE);

      // 为空标识
      if (column.getNullFlag() != null && !column.getNullFlag()) {
        outOracleSql.append(SqlKeyWord.NOT).append(Symbol.SPACE);
        outOracleSql.append(SqlKeyWord.NULL);
      }
      outOracleSql.append(Symbol.COMMA);
      outOracleSql.append(Symbol.ENTER_LINE);
    }

    // 当没有主键，则删除最后那个逗号
    outOracleSql.deleteCharAt(outOracleSql.length() - Symbol.ENTER_LINE.length());
    outOracleSql.deleteCharAt(outOracleSql.length() - Symbol.COMMA.length());
    outOracleSql.append(Symbol.ENTER_LINE);

    outOracleSql.append(Symbol.BRACKET_RIGHT);
    outOracleSql.append(Symbol.SEMICOLON);
    outOracleSql.append(Symbol.ENTER_LINE);

    return outOracleSql.toString();
  }

  /**
   * 注释的输出操作
   *
   * @param tableInfo 表信息
   * @param tableNameItem 表名操作
   * @return 输出
   */
  private String outComment(
      TableInfoDTO tableInfo, Entry<String, List<TableColumnDTO>> tableNameItem) {
    StringBuilder outOracleSql = new StringBuilder();
    // 输出表注释
    outOracleSql.append(SqlKeyWord.COMMENT).append(Symbol.SPACE);
    outOracleSql.append(SqlKeyWord.ON).append(Symbol.SPACE);
    outOracleSql.append(SqlKeyWord.TABLE).append(Symbol.SPACE);
    outOracleSql
        .append(Symbol.QUOTE)
        .append(tableInfo.getTableName())
        .append(Symbol.QUOTE)
        .append(Symbol.SPACE);
    outOracleSql.append(SqlKeyWord.IS).append(Symbol.SPACE);
    outOracleSql.append(Symbol.SINGLE_QUOTE).append(Symbol.SPACE);
    outOracleSql.append(tableInfo.getTableComment());
    outOracleSql.append(Symbol.SINGLE_QUOTE);
    outOracleSql.append(Symbol.SEMICOLON);
    outOracleSql.append(Symbol.ENTER_LINE);

    // 输出列注释
    for (TableColumnDTO column : tableNameItem.getValue()) {
      outOracleSql.append(SqlKeyWord.COMMENT).append(Symbol.SPACE);
      outOracleSql.append(SqlKeyWord.ON).append(Symbol.SPACE);
      outOracleSql.append(SqlKeyWord.COLUMN).append(Symbol.SPACE);
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

      outOracleSql.append(SqlKeyWord.IS).append(Symbol.SPACE);

      outOracleSql.append(Symbol.SINGLE_QUOTE);
      if (StringUtils.isNotEmpty(tableInfo.getTableComment())) {
        outOracleSql.append(column.getColumnMsg());
      } else {
        outOracleSql.append(Symbol.EMPTY);
      }
      outOracleSql.append(Symbol.SINGLE_QUOTE);

      outOracleSql.append(Symbol.SEMICOLON);
      outOracleSql.append(Symbol.ENTER_LINE);
    }

    return outOracleSql.toString();
  }

  /**
   * 输出列的key
   *
   * @param tableInfo 表信息
   * @param tableIndexMap 索引信息
   * @return
   */
  private String outColumnKey(
      TableInfoDTO tableInfo, Map<String, Map<String, TableIndexDTO>> tableIndexMap) {

    Map<String, TableIndexDTO> tableIndex = tableIndexMap.get(tableInfo.getTableName());

    if (null == tableIndex) {
      return Symbol.EMPTY;
    }

    StringBuilder outSql = new StringBuilder();
    for (Entry<String, TableIndexDTO> columnIndexIter : tableIndex.entrySet()) {

      outSql.append(SqlKeyWord.ALTER).append(Symbol.SPACE);
      outSql.append(SqlKeyWord.TABLE).append(Symbol.SPACE);
      outSql.append(Symbol.QUOTE);
      outSql.append(tableInfo.getTableName());
      outSql.append(Symbol.QUOTE);
      outSql.append(Symbol.SPACE);
      outSql.append(SqlKeyWord.ADD).append(Symbol.SPACE);
      outSql.append(SqlKeyWord.CONSTRAINT).append(Symbol.SPACE);

      // 如果当前添加为主键
      if (MysqlIndexTypeEnum.PRIMARY_KEY
          .getType()
          .equals(columnIndexIter.getValue().getConstraintType())) {

        outSql.append(Symbol.QUOTE);
        outSql.append("PKI_");
        outSql.append(tableInfo.getTableName());
        outSql.append(Symbol.UNDER_LINE);
        outSql.append(columnIndexIter.getKey());
        outSql.append(Symbol.QUOTE);
        outSql.append(Symbol.SPACE);

        outSql.append(SqlKeyWord.PRIMARY_KEY).append(Symbol.SPACE);
        outSql.append(Symbol.BRACKET_LEFT);
        outSql.append(outColumnKey(columnIndexIter.getValue().getColumn()));
        outSql.append(Symbol.BRACKET_RIGHT);
      }

      // 如果当前存在唯一主键
      if (MysqlIndexTypeEnum.UNIQUE
          .getType()
          .equals(columnIndexIter.getValue().getConstraintType())) {

        outSql.append(Symbol.QUOTE);
        outSql.append("UNI_");
        outSql.append(tableInfo.getTableName());
        outSql.append(Symbol.UNDER_LINE);
        outSql.append(columnIndexIter.getKey());
        outSql.append(Symbol.QUOTE);
        outSql.append(Symbol.SPACE);

        outSql.append(SqlKeyWord.UNIQUE).append(Symbol.SPACE);
        outSql.append(Symbol.BRACKET_LEFT);
        outSql.append(outColumnKey(columnIndexIter.getValue().getColumn()));
        outSql.append(Symbol.BRACKET_RIGHT);
      }

      outSql.append(Symbol.SEMICOLON);
      outSql.append(Symbol.ENTER_LINE);
    }

    return outSql.toString();
  }

  /**
   * 输出列信息
   *
   * @param columnIndexList
   * @return
   */
  private String outColumnKey(List<TableIndexColumnDTO> columnIndexList) {

    StringBuilder outSql = new StringBuilder();

    for (int i = 0; i < columnIndexList.size(); i++) {
      TableIndexColumnDTO columnItem = columnIndexList.get(i);
      outSql.append(Symbol.QUOTE);
      outSql.append(columnItem.getColumnName());
      outSql.append(Symbol.QUOTE);
      if (i != columnIndexList.size() - 1) {
        outSql.append(Symbol.COMMA);
      }
    }

    return outSql.toString();
  }
}
