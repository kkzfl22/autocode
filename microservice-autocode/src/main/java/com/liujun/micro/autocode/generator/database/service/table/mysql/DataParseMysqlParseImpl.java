package com.liujun.micro.autocode.generator.database.service.table.mysql;

import com.liujun.micro.autocode.generator.builder.utils.TypeProcessUtils;
import com.liujun.micro.autocode.generator.database.constant.JavaDataTypeGenerateValueEnum;
import com.liujun.micro.autocode.generator.database.constant.MysqlDataTypeEnum;
import com.liujun.micro.autocode.generator.database.constant.StandardTypeEnum;
import com.liujun.micro.autocode.generator.database.entity.TableColumnDTO;
import com.liujun.micro.autocode.generator.database.service.table.DataParseInf;

/**
 * mysql的数据库类型转换查询
 *
 * @author liujun
 * @version 0.0.1
 */
public class DataParseMysqlParseImpl implements DataParseInf {

  public static final DataParseMysqlParseImpl INSTANCE = new DataParseMysqlParseImpl();

  @Override
  public String createValue(TableColumnDTO bean) {

    // 1,得到标准的类型
    StandardTypeEnum standType = MysqlDataTypeEnum.databaseToStandKey(bean.getDataType());

    String outGenerate = JavaDataTypeGenerateValueEnum.getGenerateFun(standType, bean.getDataLength());

    return outGenerate;
  }

  @Override
  public String parseJavaType(TableColumnDTO bean) {
    return TypeProcessUtils.getJavaType(bean.getDataType());
  }
}
