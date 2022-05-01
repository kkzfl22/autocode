package com.liujun.auto.generator.builder.ddd.full.sql;

import com.liujun.auto.constant.Symbol;
import com.liujun.auto.generator.builder.GenerateCodeInf;
import com.liujun.auto.generator.builder.ddd.constant.SymbolParseEnum;
import com.liujun.auto.generator.builder.ddd.entity.GenerateCodeContext;
import com.liujun.auto.generator.builder.utils.DateTimeUtils;
import com.liujun.auto.generator.builder.utils.GenerateOutFileUtils;
import com.liujun.auto.generator.builder.utils.GeneratePathUtils;
import com.liujun.auto.generator.database.constant.SqlKeyWord;
import com.liujun.auto.generator.database.entity.TableColumnDTO;
import com.liujun.auto.generator.database.entity.TableDataOutDTO;
import com.liujun.auto.generator.database.entity.TableInfoDTO;
import com.liujun.auto.generator.database.service.table.mysql.DatabaseMysqlDataOutputImpl;

import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

/**
 * 将Mysql数据库库中数据导出为标准的SQL语句
 *
 * @author liujun
 * @version 1.0.0
 */
public class OracleOutputDataToSqlCreate implements GenerateCodeInf {

  public static final OracleOutputDataToSqlCreate INSTANCE = new OracleOutputDataToSqlCreate();

  /** 特殊的时间 */
  private static final String ZERO_TIME = "0000-00-00 00:00:00";

  /** 清理文件名 */
  private static final String FILE_NAME = "oracle-data-output.sql";

  @Override
  public void generateCode(GenerateCodeContext param) {

    // 执行文件清理操作
    GenerateOutFileUtils.cleanFile(GeneratePathUtils.outServicePath(param), "", FILE_NAME);

    Map<String, TableInfoDTO> tableMap = param.getTableMap();
    Map<String, List<TableColumnDTO>> map = param.getColumnMapList();
    Iterator<Entry<String, List<TableColumnDTO>>> tableNameEntry = map.entrySet().iterator();

    while (tableNameEntry.hasNext()) {
      Entry<String, List<TableColumnDTO>> tableNameItem = tableNameEntry.next();

      // 将查询的数据输出到文件中
      outFile(tableNameItem, param);
    }
  }

  private void outFile(
      Entry<String, List<TableColumnDTO>> tableNameItem, GenerateCodeContext param) {

    int start = 1;
    int pageSize = 256;
    List<Map<String, Object>> dataResult;

    do {
      TableDataOutDTO tableData = new TableDataOutDTO();

      tableData.setPage(start);
      tableData.setSize(pageSize);
      tableData.setTableColumn(tableNameItem);

      // 按表去库中查询数据
      dataResult = DatabaseMysqlDataOutputImpl.INSTANCE.dbQueryResult(tableData);

      if (dataResult == null || dataResult.isEmpty()) {
        break;
      }

      // 将数据转换为SQL
      String dataList = parseToSql(dataResult, tableNameItem);

      // 将转换为的SQL写入文件
      // 输到文件中,持续的追加
      GenerateOutFileUtils.outFile(
          dataList, GeneratePathUtils.outServicePath(param), "", FILE_NAME, true);

      // 每次完成分页到下一页
      start += 1;

      // 当结果为空时，停止再次查询
    } while (dataResult != null && !dataResult.isEmpty());

    return;
  }

  /**
   * 将数据转换为结果信息
   *
   * @param dataResult
   * @param column
   * @return
   */
  private String parseToSql(
      List<Map<String, Object>> dataResult, Entry<String, List<TableColumnDTO>> column) {

    StringBuilder outSql = new StringBuilder();

    for (int i = 0; i < dataResult.size(); i++) {
      Map<String, Object> data = dataResult.get(i);

      boolean firstFlag = false;

      if (i == 0) {
        firstFlag = true;
      }

      boolean lastFlag = false;
      if (i == dataResult.size() - 1) {
        lastFlag = true;
      }

      outSql.append(parseSql(data, column, firstFlag, lastFlag));
    }

    return outSql.toString();
  }

  /**
   * 将数据转换为SQL的操作
   *
   * @param dataResult
   * @param column
   * @param firstFlag
   * @param lastFlag
   * @return
   */
  private String parseSql(
      Map<String, Object> dataResult,
      Entry<String, List<TableColumnDTO>> column,
      boolean firstFlag,
      boolean lastFlag) {
    StringBuilder outInsertSql = new StringBuilder();

    // 如果为首行，则需要添加列信息
    if (firstFlag) {
      outInsertSql.append(outColumn(column));
    }

    // 添加数据值
    outInsertSql.append(outValue(column, dataResult));

    // 如果为结束，则输出分号
    if (lastFlag) {
      outInsertSql.append(Symbol.SEMICOLON);
      outInsertSql.append(Symbol.ENTER_LINE);
      outInsertSql.append(Symbol.ENTER_LINE);
    } else {
      outInsertSql.append(Symbol.COMMA);
    }

    outInsertSql.append(Symbol.ENTER_LINE);

    return outInsertSql.toString();
  }

  /**
   * 带列信息的输出
   *
   * @param column
   * @return
   */
  private String outColumn(Entry<String, List<TableColumnDTO>> column) {
    StringBuilder outInsertSql = new StringBuilder();
    outInsertSql.append(SqlKeyWord.INSERT).append(Symbol.SPACE).append(SqlKeyWord.INTO);
    outInsertSql.append(Symbol.SPACE);
    outInsertSql.append(Symbol.QUOTE);
    outInsertSql.append(column.getKey());
    outInsertSql.append(Symbol.QUOTE);
    outInsertSql.append(Symbol.BRACKET_LEFT);

    for (int i = 0; i < column.getValue().size(); i++) {
      outInsertSql.append(Symbol.QUOTE);
      outInsertSql.append(column.getValue().get(i).getColumnName());
      outInsertSql.append(Symbol.QUOTE);

      if (i != column.getValue().size() - 1) {
        outInsertSql.append(Symbol.COMMA);
      }
    }

    outInsertSql.append(Symbol.BRACKET_RIGHT);

    outInsertSql.append(Symbol.ENTER_LINE);
    outInsertSql.append(SqlKeyWord.VALUES);

    return outInsertSql.toString();
  }

  /**
   * 列值输出
   *
   * @param column
   * @param dataResult
   * @return
   */
  private String outValue(
      Entry<String, List<TableColumnDTO>> column, Map<String, Object> dataResult) {
    StringBuilder outInsertSql = new StringBuilder();
    outInsertSql.append(Symbol.BRACKET_LEFT);

    for (int i = 0; i < column.getValue().size(); i++) {

      Object value = dataResult.get(column.getValue().get(i).getColumnName());
      if (value instanceof String) {
        if (ZERO_TIME.equals(value)) {
          value = DateTimeUtils.localDataTimeOut(DateTimeUtils.getCurrDateTime());
        }

        outInsertSql.append(Symbol.SINGLE_QUOTE);

        // 进行值的处理操作
        value = valueProcess(String.valueOf(value));

        outInsertSql.append(value);
        outInsertSql.append(Symbol.SINGLE_QUOTE);

      } else {
        outInsertSql.append(value);
      }

      if (i != column.getValue().size() - 1) {
        outInsertSql.append(Symbol.COMMA);
      }
    }

    outInsertSql.append(Symbol.BRACKET_RIGHT);

    return outInsertSql.toString();
  }

  private String valueProcess(String value) {
    String result = value;

    // 进行字符的转义处理操作
    result = SymbolParseEnum.symbolParse(result);

    return result;
  }
}
