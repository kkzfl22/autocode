package com.liujun.micro.autocode.generator.builder.utils;

import com.liujun.micro.autocode.generator.builder.constant.MybatisDataTypeEnum;
import com.liujun.micro.autocode.generator.database.constant.MysqlDataTypeEnum;
import com.liujun.micro.autocode.generator.database.constant.StandardTypeEnum;
import com.liujun.micro.autocode.generator.javalanguage.constant.JavaDataTypeEnum;

/**
 * 类型处理
 *
 * @author liujun
 * @version 0.0.1
 * @date 2019/03/13
 */
public class TypeProcessUtils {

  /**
   * 得到java的数据类型 方法描述
   *
   * @param databaseType typeinfo
   * @return java的类型信息
   */
  public static String getJavaType(String databaseType) {

    String tempType = databaseType.toLowerCase();

    // 1,从数据库的类型中获取
    StandardTypeEnum standardType = MysqlDataTypeEnum.databaseToStandKey(tempType);

    // 通过数据库的类型转换为java类型信息
    return JavaDataTypeEnum.getJavaType(standardType);
  }

  /**
   * 转换为mybatis的类型信息
   *
   * @param dataType
   * @return
   */
  public static String dbTypeParseMyBatis(String dataType, Integer length) {
    // 1,从数据库的类型中获取
    StandardTypeEnum standardType = MysqlDataTypeEnum.databaseToStandKey(dataType, length);

    // 转换成对应的mybatis类型返回
    return MybatisDataTypeEnum.getMybatisType(standardType);
  }
}
