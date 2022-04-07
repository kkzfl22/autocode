package com.liujun.auto.generator.database.entity;

import com.liujun.auto.utils.StringUtils;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

/**
 * java的对应数据库的字段信息
 *
 * @author liujun
 * @version 1.0.0
 */
@Getter
@Setter
@ToString
public class TableColumnDTO {

  /** 表名信息 */
  private String tableName;

  /** 列名 */
  private String columnName;

  /** 注释信息 */
  private String columnMsg;

  /** 列的类型信息 */
  private String dataType;

  /** 是否为主键 */
  private Boolean primaryKey;

  /** 长度信息 */
  private Long dataLength;

  /** 精度 */
  private Integer dataScale;

  /** 是否自增长 */
  private Boolean autoIncrement;

  /** 是否允许为空 */
  private Boolean nullFlag;

  /** 数据库设置的默认值 */
  private String defaultValue;

  /** 创建表语句时指定的类型 */
  private String sqlColumnType;

  public TableColumnDTO() {}

  public TableColumnDTO(
      String columnName,
      String columnMsg,
      String dataType,
      boolean primaryKey,
      boolean nullFlag,
      String defaultvalue) {
    super();
    this.columnName = columnName;
    this.columnMsg = StringUtils.containerTrim(columnMsg);
    this.dataType = dataType;
    this.primaryKey = primaryKey;
    this.nullFlag = nullFlag;
    this.defaultValue = defaultvalue;
  }

  /**
   * 以现在对象值生成新的对象
   *
   * @return
   */
  public TableColumnDTO newInstanceForCurrValue() {
    TableColumnDTO columnInfo = new TableColumnDTO();
    columnInfo.setTableName(this.getTableName());
    columnInfo.setColumnName(this.getColumnName());
    columnInfo.setColumnMsg(this.getColumnMsg());
    columnInfo.setDataType(this.getDataType());
    columnInfo.setPrimaryKey(this.getPrimaryKey());
    columnInfo.setDataLength(this.getDataLength());
    columnInfo.setDataScale(this.getDataScale());
    columnInfo.setAutoIncrement(this.getAutoIncrement());
    columnInfo.setNullFlag(this.getNullFlag());
    columnInfo.setDefaultValue(this.getDefaultValue());
    columnInfo.setSqlColumnType(this.getSqlColumnType());
    return columnInfo;
  }
}
