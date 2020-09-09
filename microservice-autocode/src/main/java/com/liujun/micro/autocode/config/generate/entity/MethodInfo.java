package com.liujun.micro.autocode.config.generate.entity;

import lombok.Data;
import lombok.ToString;

import java.util.ArrayList;
import java.util.List;

/**
 * 生成的方法信息
 *
 * @author liujun
 * @version 0.0.1
 */
@Data
@ToString
public class MethodInfo implements Comparable<MethodInfo> {

  /** 方法名称 */
  private String name;

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

  /** 分页查询标识 */
  private Boolean pageQueryFlag;

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
}
