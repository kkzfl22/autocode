package com.liujun.micro.autocode.config.generate.entity;

import com.liujun.micro.autocode.constant.MyBatisOperatorFlag;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

/**
 * @author liujun
 * @version 0.0.1
 */
@ToString
@Getter
@Setter
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
}
