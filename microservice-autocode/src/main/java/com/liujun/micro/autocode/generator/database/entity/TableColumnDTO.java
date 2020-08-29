package com.liujun.micro.autocode.generator.database.entity;

import lombok.Data;
import lombok.ToString;

/**
 * java的对应数据库的字段信息
 *
 * @author liujun
 * @version 1.0.0
 */
@Data
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
  private Integer dataLength;

  /** 精度 */
  private Integer dataScale;

  /** 是否自增长 */
  private Boolean autoIncrement;

  /** 是否允许为空 */
  private Boolean nullFlag;

  /** 数据库设置的默认值 */
  private String defaultValue;

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
    this.columnMsg = columnMsg;
    this.dataType = dataType;
    this.primaryKey = primaryKey;
    this.nullFlag = nullFlag;
    this.defaultValue = defaultvalue;
  }
}
