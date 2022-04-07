package com.liujun.auto.generator.builder.operator.ddd.increment.field.matcher;

import com.liujun.auto.algorithm.ahocorsick.MatcherBusi;
import com.liujun.auto.generator.database.constant.DatabaseTypeEnum;
import com.liujun.auto.generator.database.entity.TableColumnDTO;
import com.liujun.auto.generator.database.entity.TableInfoDTO;
import lombok.ToString;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 构建字段处理的上下文信息
 *
 * @author liujun
 * @version 0.0.1
 */
@ToString
public class MatcherFieldContext {

  /** 数据的原始字符串 */
  private final char[] srcDataArray;

  /** 当前被查询到的对象信息 */
  private MatcherBusi matcherBusi;

  /** 默认的输出实现，用于存储输出的字符串结果 */
  private StringBuilder outValueIncrement;

  /** 最终输出的字符信息,相比原有的字符，扩容20% */
  private Map<String, Object> outValueIncrementMap = new HashMap<>();

  /** 表结构信息 */
  private TableInfoDTO tableInfo;

  /** 列的map信息 */
  private Map<String, TableColumnDTO> columnMap;

  /** 列的集合 */
  private List<TableColumnDTO> columnList;

  /** 主键列信息 */
  private List<TableColumnDTO> primaryKeyList;

  /** 最后操作的位置 */
  private int lastIndex;

  /** 数据库枚举信息 */
  private DatabaseTypeEnum typeEnum;

  public StringBuilder getOutValueIncrement() {
    return outValueIncrement;
  }

  public void setOutValueIncrement(StringBuilder outValueIncrement) {
    this.outValueIncrement = outValueIncrement;
  }

  /**
   * 进行值的放入操作
   *
   * @param key key信息
   * @param value 值
   * @param <T>
   */
  public <T> void putValue(String key, T value) {
    outValueIncrementMap.put(key, value);
  }

  /**
   * 获取数据信息
   *
   * @param key 获取的的key
   * @param <T> 获取的泛型类型
   * @return 返回的对象值
   */
  public <T> T getValue(String key) {
    return (T) outValueIncrementMap.get(key);
  }

  public MatcherFieldContext(String srcData) {
    this.srcDataArray = srcData.toCharArray();
  }

  public char[] getSrcDataArray() {
    return srcDataArray;
  }

  public MatcherBusi getMatcherBusi() {
    return matcherBusi;
  }

  public void setMatcherBusi(MatcherBusi matcherBusi) {
    this.matcherBusi = matcherBusi;
  }

  public TableInfoDTO getTableInfo() {
    return tableInfo;
  }

  public void setTableInfo(TableInfoDTO tableInfo) {
    this.tableInfo = tableInfo;
  }

  public Map<String, TableColumnDTO> getColumnMap() {
    return columnMap;
  }

  public void setColumnMap(Map<String, TableColumnDTO> columnMap) {
    this.columnMap = columnMap;
  }

  public List<TableColumnDTO> getColumnList() {
    return columnList;
  }

  public void setColumnList(List<TableColumnDTO> columnList) {
    this.columnList = columnList;
  }

  public List<TableColumnDTO> getPrimaryKeyList() {
    return primaryKeyList;
  }

  public void setPrimaryKeyList(List<TableColumnDTO> primaryKeyList) {
    this.primaryKeyList = primaryKeyList;
  }

  public int getLastIndex() {
    return lastIndex;
  }

  public void setLastIndex(int lastIndex) {
    this.lastIndex = lastIndex;
  }

  public DatabaseTypeEnum getTypeEnum() {
    return typeEnum;
  }

  public void setTypeEnum(DatabaseTypeEnum typeEnum) {
    this.typeEnum = typeEnum;
  }
}
