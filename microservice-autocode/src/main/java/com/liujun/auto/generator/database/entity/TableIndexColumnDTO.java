package com.liujun.auto.generator.database.entity;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

/**
 * 表中的索引关联的列信息
 *
 * @author liujun
 * @version 1.0.0
 */
@Getter
@Setter
@ToString
public class TableIndexColumnDTO implements Comparable<TableIndexColumnDTO> {

  /** 表名信息 */
  private String tableName;

  /** 索引的名称 */
  private String constraintName;

  /** 列名信息 */
  private String columnName;

  /** 字段的顺序 */
  private Integer ordinalPosition;

  @Override
  public int compareTo(TableIndexColumnDTO o) {

    if (this.getOrdinalPosition() > o.getOrdinalPosition()) {
      return 1;
    } else if (this.getOrdinalPosition() < o.getOrdinalPosition()) {
      return -1;
    }

    return 0;
  }
}
