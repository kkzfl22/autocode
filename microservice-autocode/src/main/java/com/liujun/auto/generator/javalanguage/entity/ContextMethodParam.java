package com.liujun.auto.generator.javalanguage.entity;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

/**
 * 类的方法信息
 *
 * @author liujun
 * @since 2022/4/6
 */
@Getter
@Setter
@ToString
@Builder
public class ContextMethodParam {

  /** 类名信息 */
  private String className;

  /** 引用名称 */
  private String referenceName;
}
