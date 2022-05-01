package com.liujun.auto.generator.javalanguage.entity;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

/**
 * 最基础的代码行信息
 *
 * @author liujun
 * @since 2022/4/6
 */
@Getter
@Setter
@ToString
public class ContextLineCodeData extends ContextLineCodeBase {

  /** final的标识 */
  private Boolean finalFlag;

  /** 类的名称 */
  private String className;

  /** 引用名称 */
  private String referenceName;

  /** 引用的值信息 */
  private String referenceValue;
}
