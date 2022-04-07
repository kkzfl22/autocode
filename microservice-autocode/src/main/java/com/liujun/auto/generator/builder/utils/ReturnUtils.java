package com.liujun.auto.generator.builder.utils;

import com.liujun.auto.constant.GenerateDefineFlag;
import com.liujun.auto.config.generate.entity.MethodInfo;
import com.liujun.auto.generator.javalanguage.constant.JavaKeyWord;
import org.apache.commons.lang3.StringUtils;

import java.util.List;

/**
 * 返回的信息检查
 *
 * @author liujun
 * @version 0.0.1
 */
public class ReturnUtils {

  /**
   * 进行结果的条件检查返回是否为集合
   *
   * @param returnInfo where数据
   * @return true，存在条件in, false 不存在
   */
  public static boolean checkReturnList(String returnInfo) {

    if (StringUtils.isEmpty(returnInfo)) {
      return false;
    }

    return returnInfo.indexOf(JavaKeyWord.IMPORT_LIST) != -1;
  }

  /**
   * 检查返回是否为对象
   *
   * <p>为空则默认为对象
   *
   * <p>其他则进行检查
   *
   * @param returnInfo where数据
   * @return true，存在条件in, false 不存在
   */
  public static boolean checkReturnObject(String returnInfo) {

    if (StringUtils.isEmpty(returnInfo)) {
      return true;
    }

    // 检查到集合，则返回false
    if (checkReturnList(returnInfo)) {
      return false;
    }

    return returnInfo.indexOf(GenerateDefineFlag.TABLE_NAME.getDefineFlag()) != -1;
  }

  /**
   * 方法是否存在结果集的情况
   *
   * @param methodList 所有方法信息
   * @return true 存在结果集返回，false 没有结果集返回
   */
  public static boolean checkList(List<MethodInfo> methodList) {

    if (null == methodList || methodList.isEmpty()) {
      return false;
    }

    for (MethodInfo methodInfo : methodList) {
      // 检查返回类型是否存在集合
      if (methodInfo.getReturns() != null
          && methodInfo.getReturns().indexOf(JavaKeyWord.IMPORT_LIST) != -1) {
        return true;
      }
    }

    return false;
  }
}
