package com.liujun.micro.autocode.generator.builder.operator.utils;

import com.liujun.micro.autocode.constant.GenerateDefineFlag;
import com.liujun.micro.autocode.generator.javalanguage.constant.JavaKeyWord;
import org.apache.commons.lang3.StringUtils;

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

    if (returnInfo.indexOf(JavaKeyWord.IMPORT_LIST) != -1) {
      return true;
    }

    return false;
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

    if (returnInfo.indexOf(GenerateDefineFlag.TABLE_NAME.getDefineFlag()) != -1) {
      return true;
    }

    return false;
  }
}
