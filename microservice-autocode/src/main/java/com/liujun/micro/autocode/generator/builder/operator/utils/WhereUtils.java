package com.liujun.micro.autocode.generator.builder.operator.utils;

import com.liujun.micro.autocode.entity.config.WhereInfo;
import com.liujun.micro.autocode.generator.builder.constant.MyBatisOperatorFlag;

import java.util.List;

/**
 * 条件的判断操作
 *
 * @author liujun
 * @version 0.0.1
 */
public class WhereUtils {

  /** where条件检查 */
  private static final int WHERE_CHECK_NUM = 1;

  /**
   * 是否需要对数据进行修改
   *
   * <p>目前存在的情况为：where条件中存在in,并且还再在其他字段，则需要
   *
   * @return true 修改字段，false 不需要修改数据
   */
  public static boolean batchUpdateData(List<WhereInfo> whereList) {
    if (null == whereList) {
      return false;
    }

    // 进行in的条件检查
    boolean inFlag = checkInCondition(whereList);

    // 当where条件中使用了in，并且字段超过了1个，则说明需要修改字段
    if (inFlag && whereList.size() > WHERE_CHECK_NUM) {
      return true;
    }
    return false;
  }

  /**
   * 进行in的条件检查
   *
   * @param whereList where数据
   * @return true，存在条件in, false 不存在
   */
  public static boolean checkInCondition(List<WhereInfo> whereList) {

    if (null == whereList) {
      return false;
    }

    boolean inFlag = false;

    for (WhereInfo whereItem : whereList) {
      if (null != whereItem.getOperatorFlag()
          && MyBatisOperatorFlag.IN.equals(whereItem.getOperatorFlag())) {
        inFlag = true;
        break;
      }
    }

    return inFlag;
  }
}
