package com.liujun.auto.constant;

import lombok.Getter;
import lombok.ToString;
import org.apache.commons.lang3.StringUtils;

/**
 * 代码中的占位符信息
 *
 * @author liujun
 * @version 0.0.1
 */
@Getter
@ToString
public enum GenerateDefineFlag {

  /** 表名的定义符,表示使用以表名转换为对象信息 */
  TABLE_NAME("table_name"),
  ;

  /** 定义的占位符信息 */
  private final String defineFlag;

  GenerateDefineFlag(String defineFlag) {
    this.defineFlag = defineFlag;
  }

  /**
   * 表名占位符的处理
   *
   * @param data 字符串原始串
   * @param tableEntity 表对应的实体信息
   * @return 处理后的结果
   */
  public static String placeholderTableName(String data, String tableEntity) {

    if (StringUtils.isEmpty(data)) {
      return Symbol.EMPTY;
    }

    String result = data;

    if (result.contains(TABLE_NAME.getDefineFlag())) {
      result = result.replaceAll(TABLE_NAME.getDefineFlag(), tableEntity);
    }

    return result;
  }
}
