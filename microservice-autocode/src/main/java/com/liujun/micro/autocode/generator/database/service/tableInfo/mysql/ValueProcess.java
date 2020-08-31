package com.liujun.micro.autocode.generator.database.service.tableInfo.mysql;

/**
 * 数据值的处理
 *
 * @author liujun
 * @version 0.0.1
 * @date 2019/03/13
 */
public class ValueProcess {

  /** 实例信息 */
  public static final ValueProcess INSTANCE = new ValueProcess();

  /**
   * 得到java的默认值 方法描述
   *
   * @param type type info
   * @return 类型信息
   */
  public String getJavaDefValue(String type) {
    String defValue = "";

    switch (type) {
      case "INT":
        defValue = "= 0";
        break;
      case "FLOAT":
        defValue = "= 0f";
        break;
      case "NUMERIC":
        defValue = "= 0";
        break;
      case "DOUBLE":
        defValue = "= 0";
        break;
      case "BIGINT":
        defValue = "= 0l";
        break;
      default:
        break;
    }

    return defValue;
  }
}
