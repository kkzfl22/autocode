package com.liujun.auto.generator.database.entity;

import java.util.List;

/**
 * 表名信息
 *
 * @author liujun
 * @version 1.0.0
 */
public class TableInfoDTO {

  /** 表名 */
  private String tableName;

  /** 表描述信息 */
  private String tableComment;

  /** 列信息 */
  private List<TableColumnDTO> columnList;

  public TableInfoDTO() {}

  public TableInfoDTO(String tableName, String tableComment) {
    super();
    this.tableName = tableName;
    this.tableComment = tableComment;
  }

  @Override
  public String toString() {
    final StringBuilder sb = new StringBuilder("TableInfoDTO{");
    sb.append("tableName='").append(tableName).append('\'');
    sb.append(", tableComment='").append(tableComment).append('\'');
    sb.append(", columnList=").append(columnList);
    sb.append('}');
    return sb.toString();
  }

  public String getTableName() {
    return tableName;
  }

  public void setTableName(String tableName) {
    this.tableName = tableName;
  }

  public String getTableComment() {
    return tableComment;
  }

  public void setTableComment(String tableComment) {
    this.tableComment = tableComment;
  }

  public List<TableColumnDTO> getColumnList() {
    return columnList;
  }

  public void setColumnList(List<TableColumnDTO> columnList) {
    this.columnList = columnList;
  }
}
