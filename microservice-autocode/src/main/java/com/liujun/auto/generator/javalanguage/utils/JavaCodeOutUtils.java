/*
 * Copyright (C), 2008-2021, Paraview All Rights Reserved.
 */
package com.liujun.auto.generator.javalanguage.utils;

import com.liujun.auto.constant.Symbol;
import com.liujun.auto.generator.javalanguage.constant.PrefixSpaceEnum;
import com.liujun.auto.generator.javalanguage.entity.ContextAnnotation;
import com.liujun.auto.generator.javalanguage.entity.ContextMethodParam;

import java.util.List;

/**
 * @author liujun
 * @since 2022/5/9
 */
public class JavaCodeOutUtils {

  private JavaCodeOutUtils() {}

  /**
   * 空行的输出
   *
   * @param num
   * @return
   */
  public static String emptyLine(int num) {

    if (num <= 0) {
      return Symbol.EMPTY;
    }

    StringBuilder outEmptyLine = new StringBuilder();

    for (int i = 0; i < num; i++) {
      outEmptyLine.append(Symbol.ENTER_LINE);
    }

    return outEmptyLine.toString();
  }

  /**
   * 输出空格
   *
   * @param leftSpace
   * @return
   */
  public static String outSpace(PrefixSpaceEnum leftSpace) {
    if (leftSpace == null || PrefixSpaceEnum.NONE.equals(leftSpace)) {
      return Symbol.EMPTY;
    }

    StringBuilder space = new StringBuilder();

    for (int i = 0; i < leftSpace.getLevel(); i++) {
      space.append(Symbol.SPACE);
    }
    return space.toString();
  }

  /**
   * 执行注解的输出
   *
   * @param annotation
   * @return
   */
  public static String outAnnotation(List<ContextAnnotation> annotation) {

    if (null == annotation || annotation.isEmpty()) {
      return Symbol.EMPTY;
    }

    StringBuilder annotationOut = new StringBuilder();

    for (ContextAnnotation annotationItem : annotation) {
      annotationOut.append(annotationItem.outAnnotation()).append(Symbol.ENTER_LINE);
    }

    return annotationOut.toString();
  }

  /**
   * 参数的输出操作
   *
   * @param param 参数信息
   * @return 参数的代码输出
   */
  public static String outParam(List<ContextMethodParam> param) {
    if (null == param || param.isEmpty()) {
      return Symbol.BRACKET_LEFT + Symbol.BRACKET_RIGHT;
    }

    StringBuilder outParam = new StringBuilder();
    // 左括号
    outParam.append(Symbol.BRACKET_LEFT);
    for (int i = 0; i < param.size(); i++) {
      outParam.append(param.get(i).outCode());
      if (i != param.size() - 1) {
        outParam.append(Symbol.COMMA);
        outParam.append(Symbol.SPACE);
      }
    }
    // 右括号
    outParam.append(Symbol.BRACKET_RIGHT);

    return outParam.toString();
  }
}
