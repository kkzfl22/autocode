package com.liujun.auto.generator.builder.entity;

import com.liujun.auto.constant.MethodTypeEnum;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.util.ArrayList;
import java.util.List;

/**
 * 操作方法信息
 *
 * @author liujun
 * @version 0.0.1
 */
@Getter
@Setter
@ToString
public class OperatorMethodInfo {

  /** 方法的返回类型，在查询时使用 */
  private String methodQueryRsp;

  /** 方法名 */
  private String methodName;

  /** 方法注释 */
  private String methodComment;

  /** 方法返回值注释 */
  private String rspComment;

  /** 数据操作类型,主要用于区别查询与修改 */
  private MethodTypeEnum operatorType;

  /** 方法参数 */
  private List<MethodParamInfo> paramList = new ArrayList<>(3);
}
