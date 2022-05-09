package com.liujun.auto.generator.javalanguage.entity;

import com.liujun.auto.constant.Symbol;
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
public class ContextMethodParam implements OutCodeInf {

  /** 类名信息 */
  private String className;

  /** 引用名称 */
  private String referenceName;

  public static ContextMethodParam builderParam(String className, String referenceName) {
    ContextMethodParam param = new ContextMethodParam();

    param.setClassName(className);
    param.setReferenceName(referenceName);

    return param;
  }

  @Override
  public String outCode() {
    StringBuilder paramOut = new StringBuilder();
    paramOut.append(className).append(Symbol.SPACE).append(referenceName);
    return paramOut.toString();
  }
}
