package com.liujun.auto.generator.run.constant;

import com.liujun.auto.constant.Symbol;

import java.util.ArrayList;
import java.util.List;

/**
 * 生成代码的范围定义
 *
 * @author liujun
 * @version 0.0.1
 */
public enum GenerateScopeEnum {

  /** 当前生成api阶段的代码 */
  API("api"),

  /** 应用聚合层代码生成 */
  APPLICATION("application"),

  /** 领域层的代码生成 */
  DOMAIN("domain"),

  /** 存储层的代码生成,生成实现方式为mybatis */
  REPOSITORY_MYBATIS("repository-mybatis"),

  /** 存储层的代码生成,生成实现方式为mybatis */
  REPOSITORY_MYBATIS_PLUS("repository-mybatis-plus"),

  /** 所有都会执行的阶段 */
  COMMON("common"),

  /** 数据库脚本转换 */
  PARSE_DB("parse-db"),

  /** 数据输出为标准的SQL */
  MYSQL_OUTPUT("mysql-output");

  /** 生成的范围定义 */
  private String scope;

  GenerateScopeEnum(String scope) {
    this.scope = scope;
  }

  /**
   * 获取枚举集合内的范围
   *
   * @param scope
   * @return
   */
  public static List<GenerateScopeEnum> generateScope(String scope) {
    String[] generateArray = scope.split(Symbol.COMMA);

    List<GenerateScopeEnum> dataList = new ArrayList<>(generateArray.length);

    for (String scopeStr : generateArray) {
      GenerateScopeEnum scopeEnum = getScope(scopeStr);

      // 获取scope信息
      dataList.add(scopeEnum);
    }

    return dataList;
  }

  /**
   * 获取错误的枚举信息
   *
   * @param scopeStr 阶段的字符信息
   * @return 范围内的
   */
  private static GenerateScopeEnum getScope(String scopeStr) {
    String dataValue = scopeStr.trim();
    for (GenerateScopeEnum scopeItem : values()) {
      if (scopeItem.getScope().equals(dataValue)) {
        return scopeItem;
      }
    }

    // 当配制错误，详细的指出错误信息
    throw new IllegalArgumentException("generate-def.yml scope config error :" + scopeStr);
  }

  public String getScope() {
    return scope;
  }
}
