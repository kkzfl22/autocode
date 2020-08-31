package com.liujun.micro.autocode.generator.builder.operator.utils;

import com.liujun.micro.autocode.entity.config.MethodInfo;
import com.liujun.micro.autocode.entity.config.TypeInfo;
import com.liujun.micro.autocode.generator.builder.constant.MethodTypeEnum;
import com.liujun.micro.autocode.generator.javalanguage.constant.JavaKeyWord;

import java.util.List;

/**
 * 公共的方法
 *
 * @author liujun
 * @version 0.0.1
 */
public class MethodUtils {

  /**
   * 检查当前是否为批量操作
   *
   * @param typeList 配制 的文件信息
   * @return true 当前为批量操作，false 当前为单行操作
   */
  public static boolean checkBatch(List<TypeInfo> typeList) {
    if (typeList.isEmpty()) {
      return false;
    }

    for (TypeInfo info : typeList) {
      // 当前是否为集合
      if (JavaKeyWord.IMPORT_LIST.equals(info.getImportPath())) {
        return true;
      }
    }

    return false;
  }

  /**
   * 检查当前是否存在删除方法
   *
   * @param methodList 配制的方法信息
   * @return true 当前为批量操作，false 当前为单行操作
   */
  public static MethodInfo getDeleteMethod(List<MethodInfo> methodList) {
    if (null == methodList || methodList.isEmpty()) {
      return null;
    }

    for (MethodInfo methodInfo : methodList) {
      // 当前是否存在删除方法
      if (MethodTypeEnum.DELETE.getType().equals(methodInfo.getOperator())) {
        return methodInfo;
      }
    }

    return null;
  }
}
