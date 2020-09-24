package com.liujun.micro.autocode.generator.convergence;

import com.liujun.micro.autocode.generator.builder.constant.MybatisDataTypeEnum;
import com.liujun.micro.autocode.generator.database.constant.DatabaseTypeEnum;
import com.liujun.micro.autocode.generator.database.constant.StandardTypeEnum;
import com.liujun.micro.autocode.generator.database.entity.TableColumnDTO;
import com.liujun.micro.autocode.generator.database.service.DatabaseTypeService;
import com.liujun.micro.autocode.generator.javalanguage.constant.JavaDataTypeEnum;

/**
 * 类型的聚合
 *
 * @author liujun
 * @version 0.0.1
 * @date 2019/03/13
 */
public class TypeConvergence {

  /**
   * 将数据库的类型转换为java的类型
   *
   * @param databaseType typeinfo
   * @return java的类型信息
   */
  public static String getJavaType(DatabaseTypeEnum typeEnum, String databaseType) {

    String tempType = databaseType.toLowerCase();

    // 1,从数据库的类型中获取
    StandardTypeEnum standardTypeEnum =
        DatabaseTypeService.INSTANCE.getDatabaseType(typeEnum, tempType);

    // 通过数据库的类型转换为java类型信息
    return JavaDataTypeEnum.getJavaType(standardTypeEnum);
  }

  /**
   * 将数据库的类型转换为mybatis的类型信息
   *
   * @param typeEnum 数据库类型
   * @param tableColumn 表信息
   * @return mybatis的类型
   */
  public static String dbTypeParseMyBatis(DatabaseTypeEnum typeEnum, TableColumnDTO tableColumn) {
    // 1,从数据库的类型中获取
    StandardTypeEnum standardType =
        DatabaseTypeService.INSTANCE.standardAndLengthCheck(
            typeEnum, tableColumn.getDataType(), tableColumn.getDataLength());

    // 转换成对应的mybatis类型返回
    return MybatisDataTypeEnum.getMybatisType(standardType);
  }
}
