package com.liujun.micro.autocode.config.generate.entity;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

/**
 * 生成的方法信息
 *
 * @author liujun
 * @version 0.0.1
 */
public class MethodInfo implements Comparable<MethodInfo> {

  /** 方法名称 */
  private String name;

  /** 发前方法是否启用 */
  private Boolean disable;

  /** 此标识用于在方法操作时对返回类型进行改变，修改在数据库层返回int，操作的行数，在业务层返回boolean，标识成功与失败 */
  private String operator;

  /** 方法的注释 */
  private String comment;

  /** where条件 */
  private String where;

  /** where条件信息 */
  private List<WhereInfo> whereInfo = new ArrayList<>(4);

  /** 参数列表 */
  private String params;

  /** 参数类型,可能存在多个参数的情况,默认三个参数以内 */
  private List<TypeInfo> paramType = new ArrayList<>(3);

  /** 标识当前是否为主键操作，一般用于删除时，主键删除 */
  private Boolean primaryFlag;

  /** 批量操作标识。batch，针对添加与删除生效。 */
  private Boolean batchFlag;

  /** 针对查询生效,返回多行的结果 */
  private Boolean multiRow;

  /** 返回类型信息 */
  private String returns;

  /** 返回类型，只存在一个返回类型 */
  private TypeInfo returnType;

  /** 排序的字段 */
  private Integer order;

  /**
   * 使用从小到大的排序方式，进行方法的输出
   *
   * @param o 对比的类型
   * @return 对比的结果
   */
  @Override
  public int compareTo(MethodInfo o) {

    if (this.getOrder() > o.getOrder()) {
      return 1;
    } else if (this.getOrder() < o.getOrder()) {
      return -1;
    }
    return 0;
  }

  public String getName() {
    return name;
  }

  public void setName(String name) {
    this.name = name;
  }

  public Boolean getDisable() {
    return disable;
  }

  public void setDisable(Boolean disable) {
    this.disable = disable;
  }

  public String getOperator() {
    return operator;
  }

  public void setOperator(String operator) {
    this.operator = operator;
  }

  public String getComment() {
    return comment;
  }

  public void setComment(String comment) {
    this.comment = comment;
  }

  public String getWhere() {
    return where;
  }

  public void setWhere(String where) {
    this.where = where;
  }

  public List<WhereInfo> getWhereInfo() {
    return whereInfo;
  }

  public void setWhereInfo(List<WhereInfo> whereInfo) {
    this.whereInfo = whereInfo;
  }

  public String getParams() {
    return params;
  }

  public void setParams(String params) {
    this.params = params;
  }

  public List<TypeInfo> getParamType() {
    return paramType;
  }

  public void setParamType(List<TypeInfo> paramType) {
    this.paramType = paramType;
  }

  public Boolean getPrimaryFlag() {
    return primaryFlag;
  }

  public void setPrimaryFlag(Boolean primaryFlag) {
    this.primaryFlag = primaryFlag;
  }

  public String getReturns() {
    return returns;
  }

  public void setReturns(String returns) {
    this.returns = returns;
  }

  public TypeInfo getReturnType() {
    return returnType;
  }

  public void setReturnType(TypeInfo returnType) {
    this.returnType = returnType;
  }

  public Integer getOrder() {
    return order;
  }

  public void setOrder(Integer order) {
    this.order = order;
  }

  public Boolean getBatchFlag() {
    return batchFlag;
  }

  public void setBatchFlag(Boolean batchFlag) {
    this.batchFlag = batchFlag;
  }

  public Boolean getMultiRow() {
    return multiRow;
  }

  public void setMultiRow(Boolean multiRow) {
    this.multiRow = multiRow;
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    MethodInfo that = (MethodInfo) o;
    return Objects.equals(name, that.name)
        && Objects.equals(disable, that.disable)
        && Objects.equals(operator, that.operator)
        && Objects.equals(comment, that.comment)
        && Objects.equals(where, that.where)
        && Objects.equals(whereInfo, that.whereInfo)
        && Objects.equals(params, that.params)
        && Objects.equals(paramType, that.paramType)
        && Objects.equals(primaryFlag, that.primaryFlag)
        && Objects.equals(returns, that.returns)
        && Objects.equals(returnType, that.returnType)
        && Objects.equals(order, that.order);
  }

  @Override
  public int hashCode() {
    return Objects.hash(
        name,
        disable,
        operator,
        comment,
        where,
        whereInfo,
        params,
        paramType,
        primaryFlag,
        returns,
        returnType,
        order);
  }

  @Override
  public String toString() {
    final StringBuilder sb = new StringBuilder("MethodInfo{");
    sb.append("name='").append(name).append('\'');
    sb.append(", disable=").append(disable);
    sb.append(", operator='").append(operator).append('\'');
    sb.append(", comment='").append(comment).append('\'');
    sb.append(", where='").append(where).append('\'');
    sb.append(", whereInfo=").append(whereInfo);
    sb.append(", params='").append(params).append('\'');
    sb.append(", paramType=").append(paramType);
    sb.append(", primaryFlag=").append(primaryFlag);
    sb.append(", returns='").append(returns).append('\'');
    sb.append(", returnType=").append(returnType);
    sb.append(", order=").append(order);
    sb.append('}');
    return sb.toString();
  }
}
