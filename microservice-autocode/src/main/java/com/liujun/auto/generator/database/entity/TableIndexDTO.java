package com.liujun.auto.generator.database.entity;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.util.List;
import java.util.Map;

/**
 * 表中的索引信息
 *
 * @author liujun
 * @version 1.0.0
 */
@Getter
@Setter
@ToString
public class TableIndexDTO {

  /** 表名信息 */
  private String tableName;

  /** 索引的名称 */
  private String constraintName;

  /** 索引的的类型 */
  private String constraintType;

  /** 索引关联的列信息 */
  private List<TableIndexColumnDTO> column;
}
