package com.liujun.auto.config.generate.entity;

import com.liujun.auto.constant.MyBatisOperatorFlag;

/**
 * @author liujun
 * @version 0.0.1
 */
public class WhereInfo {

  /** 使用条件的字段 */
  private String sqlColumn;

  /** 操作符 */
  private String operator;

  /** 操作符枚举信息 */
  private MyBatisOperatorFlag operatorFlag;

  /**
   * 默认使用相等的符号
   *
   * @param sqlColumn
   */
  public WhereInfo(String sqlColumn) {
    this.sqlColumn = sqlColumn;
    this.operator = MyBatisOperatorFlag.EQUAL.getOperator();
    this.operatorFlag = MyBatisOperatorFlag.EQUAL;
  }

  public WhereInfo(String sqlColumn, String operator) {
    this.sqlColumn = sqlColumn;
    this.operator = operator;
    this.operatorFlag = MyBatisOperatorFlag.getOperator(operator);
  }

  public String getSqlColumn() {
    return sqlColumn;
  }

  public void setSqlColumn(String sqlColumn) {
    this.sqlColumn = sqlColumn;
  }

  public String getOperator() {
    return operator;
  }

  public void setOperator(String operator) {
    this.operator = operator;
  }

  public MyBatisOperatorFlag getOperatorFlag() {
    return operatorFlag;
  }

  public void setOperatorFlag(MyBatisOperatorFlag operatorFlag) {
    this.operatorFlag = operatorFlag;
  }

  @Override
  public String toString() {
    final StringBuilder sb = new StringBuilder("WhereInfo{");
    sb.append("sqlColumn='").append(sqlColumn).append('\'');
    sb.append(", operator='").append(operator).append('\'');
    sb.append(", operatorFlag=").append(operatorFlag);
    sb.append('}');
    return sb.toString();
  }
}
