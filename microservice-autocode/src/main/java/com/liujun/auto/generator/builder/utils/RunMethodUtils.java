package com.liujun.auto.generator.builder.utils;

import com.liujun.auto.config.generate.entity.MethodInfo;
import com.liujun.auto.constant.MethodOperatorEnum;

import java.util.List;

/**
 * 方法相关的检查
 *
 * @author liujun
 * @since 2021/10/9
 */
public class RunMethodUtils {

  private RunMethodUtils() {}

  /**
   * 检查方法是否存在
   *
   * @param methodList 生成的方法信息
   * @param methodType 指定的类型
   * @return true 存在, false 不存在
   */
  public static boolean checkMethodExists(List<MethodInfo> methodList, MethodOperatorEnum methodType) {

    if (null == methodList || methodList.isEmpty()) {
      return false;
    }

    for (MethodInfo method : methodList) {
      if (methodType.getType().equals(method.getOperator())) {
        return true;
      }
    }

    return false;
  }
}
