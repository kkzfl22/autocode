package com.liujun.micro.autocode.generator.javalanguage.serivce;

import com.liujun.micro.autocode.constant.Symbol;

/**
 * 名字处理服务
 *
 * @author liujun
 * @version 0.0.1
 * @date 2018/10/19
 */
public class NameProcess {

  /** 实例对象 */
  public static final NameProcess INSTANCE = new NameProcess();

  /**
   * 类名 转换java的类名信息,首字母大写
   *
   * @param tableName
   * @return
   */
  public String toJavaClassName(String tableName) {
    tableName = tableName.toLowerCase();
    String[] strs = tableName.split(Symbol.UNDER_LINE);
    StringBuilder sb = new StringBuilder();

    boolean firstOne = false;

    for (int i = 0; i < strs.length; i++) {
      String string = strs[i];

      // 检查首字母是否为单字符
      if (i == 0) {
        firstOne = string.length() == 1 ? true : false;
      }

      // 首字母大写
      if (string.length() > 1) {

        // 如果首字母为单字符，第二个字符小写
        if (i == 1 && firstOne) {
          sb.append(string);
        }
        // 其他字符都使用驼峰命名法
        else {
          // 中间开头的第一个字母都小写
          String f = string.substring(0, 1).toUpperCase();
          sb.append(f);
          sb.append(string.substring(1));
        }
      } else {
        sb.append(string.toUpperCase());
      }
    }

    return sb.toString();
  }

  /**
   * 转换为action的名字
   *
   * @param str
   * @return
   */
  protected String toActionStr(String str) {
    return str.substring(0, 1).toLowerCase() + str.substring(1);
  }

  /**
   * java属性的命名,首字线小写，其他的首字母大写
   *
   * @param str
   * @return
   */
  public String toFieldName(String str) {
    String[] nameTemps = str.split(Symbol.UNDER_LINE);
    StringBuilder sb = new StringBuilder();
    for (int i = 0; i < nameTemps.length; i++) {
      if (i == 0) {
        sb.append(nameTemps[i].toLowerCase());
      } else {
        sb.append(nameTemps[i].substring(0, 1).toUpperCase());
        sb.append(nameTemps[i].substring(1).toLowerCase());
      }
    }
    return sb.toString();
  }

  /**
   * 转换为java属性get与set命名规则
   *
   * @param str
   * @return
   */
  public String toProJavaName(String str) {
    String[] strs = str.split(Symbol.UNDER_LINE);
    StringBuilder sb = new StringBuilder();
    for (int i = 0; i < strs.length; i++) {
      sb.append(strs[i].substring(0, 1).toUpperCase());
      sb.append(strs[i].substring(1).toLowerCase());
    }
    return sb.toString();
  }

  /**
   * 获取Spring的名称的信息 方法描述
   *
   * @param name
   * @return
   */
  protected String getSpringInstanceName(String name) {
    String tmpName = name;

    tmpName = tmpName.substring(0, 1).toLowerCase() + tmpName.substring(1);

    return tmpName;
  }

  /**
   * 转换为java命名规则,首字线小写，其他的首字母大写 方法描述
   *
   * @param str
   * @return
   */
  public String toJavaNameFirstLower(String str) {
    StringBuilder sb = new StringBuilder();
    sb.append(str.substring(0, 1).toLowerCase());
    sb.append(str.substring(1));
    return sb.toString();
  }

  /**
   * 转换为java命名规则,首字线小写，其他的首字母大写 方法描述
   *
   * @param str
   * @return
   */
  public String toJavaNameFirstUpper(String str) {
    StringBuilder sb = new StringBuilder();
    sb.append(str.substring(0, 1).toUpperCase());
    sb.append(str.substring(1));
    return sb.toString();
  }
}
