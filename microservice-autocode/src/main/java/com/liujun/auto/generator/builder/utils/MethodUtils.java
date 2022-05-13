package com.liujun.auto.generator.builder.utils;

import com.liujun.auto.config.generate.entity.MethodInfo;
import com.liujun.auto.config.generate.entity.TypeInfo;
import com.liujun.auto.config.generate.entity.WhereInfo;
import com.liujun.auto.constant.MethodOperatorEnum;
import com.liujun.auto.constant.MyBatisOperatorFlag;
import com.liujun.auto.generator.javalanguage.constant.JavaKeyWord;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

/**
 * 公共的方法
 *
 * @author liujun
 * @version 0.0.1
 */
public class MethodUtils {

  private MethodUtils() {}

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
   * 检查是否需要批量添加
   *
   * @param methodList
   * @return
   */
  public static boolean checkBatchOperator(List<MethodInfo> methodList) {
    if (null == methodList || methodList.isEmpty()) {
      return Boolean.FALSE;
    }

    // 进行方法结果集操作
    for (MethodInfo method : methodList) {
      // 如果当前为添加,并且参数存在集合
      if (MethodOperatorEnum.INSERT.getType().equals(method.getOperator())
          && MethodUtils.checkBatch(method.getParamType())) {
        return true;
      }
    }

    return false;
  }

  /**
   * 检查是否存在指定类型操作方法
   *
   * @param methodList
   * @param typeInfo
   * @return
   */
  public static boolean checkExistsOperatorType(
      List<MethodInfo> methodList, MethodOperatorEnum typeInfo) {
    if (null == methodList || methodList.isEmpty()) {
      return Boolean.FALSE;
    }

    // 进行方法结果集操作
    for (MethodInfo method : methodList) {
      // 如果当前为添加
      if (typeInfo.getType().equals(method.getOperator())) {
        return true;
      }
    }
    return false;
  }

  /**
   * 获取当前的主键删除方法
   *
   * @param methodList 配制的方法信息
   * @return true 当前为批量操作，false 当前为单行操作
   */
  public static MethodInfo getPrimaryDeleteMethod(List<MethodInfo> methodList) {
    if (null == methodList || methodList.isEmpty()) {
      return null;
    }

    for (MethodInfo methodInfo : methodList) {
      // 当前是否存在删除方法,并且为主键删除
      if (MethodOperatorEnum.DELETE.getType().equals(methodInfo.getOperator())
          && methodInfo.getPrimaryFlag()) {
        return methodInfo;
      }
    }

    return null;
  }

  /**
   * 获取当前的添加方法
   *
   * @param methodList 配制的方法信息
   * @return true 当前为批量操作，false 当前为单行操作
   */
  public static MethodInfo getInsertMethod(List<MethodInfo> methodList) {
    if (null == methodList || methodList.isEmpty()) {
      return null;
    }

    for (MethodInfo methodInfo : methodList) {
      // 获取添加方法,并且参数不能为集合，则为单数据添加
      if (MethodOperatorEnum.INSERT.getType().equals(methodInfo.getOperator())
          && methodInfo.getParams() == null) {
        return methodInfo;
      }
    }

    return null;
  }

  /**
   * 获取批量的添加方法
   *
   * @param methodList 配制的方法信息
   * @return true 当前为批量操作，false 当前为单行操作
   */
  public static MethodInfo getBatchInsertMethod(List<MethodInfo> methodList) {
    if (null == methodList || methodList.isEmpty()) {
      return null;
    }

    for (MethodInfo methodInfo : methodList) {
      // 类型为插入，数据类型为集合则为批量插入
      if (MethodOperatorEnum.INSERT.getType().equals(methodInfo.getOperator())
          && methodInfo.getParams() != null
          && methodInfo.getParams().indexOf(JavaKeyWord.IMPORT_LIST) != -1) {
        return methodInfo;
      }
    }

    return null;
  }

  /**
   * 检查当前是否存在分页查询
   *
   * @param methodList 配制的方法信息
   * @return true 当前存在分页查询，false 当前不存在分页查询
   */
  public static boolean checkPageQuery(List<MethodInfo> methodList) {
    if (null == methodList || methodList.isEmpty()) {
      return false;
    }

    for (MethodInfo methodInfo : methodList) {
      // 当前存在分页查询，则返回成功
      if (MethodOperatorEnum.QUERY_PAGE.getType().equals(methodInfo.getOperator())) {
        return true;
      }
    }

    return false;
  }

  /**
   * 获取所有带in的条件件信息
   *
   * @param codeMethod 方法信息
   * @return 结果
   */
  public static Set<String> getInCondition(List<MethodInfo> codeMethod) {
    Set<String> addWhereColumn = new HashSet<>();
    for (MethodInfo method : codeMethod) {
      for (WhereInfo whereIn : method.getWhereInfo()) {
        // 检查当前是否存在in关键字
        if (MyBatisOperatorFlag.IN.equals(whereIn.getOperatorFlag())) {
          addWhereColumn.add(whereIn.getSqlColumn());
        }
      }
    }
    return addWhereColumn;
  }
}
