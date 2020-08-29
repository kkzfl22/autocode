package com.liujun.micro.autocode.generator.builder.entity;

import lombok.Data;
import lombok.ToString;

/**
 * 方法参数信息
 *
 * @author liujun
 * @version 0.0.1
 */
@Data
@ToString
public class MethodParamInfo {

  /** 参数类型 */
  private String paramType;

  /** 参数名称 */
  private String paramName;

  /** 参数的注释 */
  private String paramComment;
}
