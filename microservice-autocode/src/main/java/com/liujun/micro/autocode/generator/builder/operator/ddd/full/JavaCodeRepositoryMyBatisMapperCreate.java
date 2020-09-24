package com.liujun.micro.autocode.generator.builder.operator.ddd.full;

import com.liujun.micro.autocode.config.generate.entity.MethodInfo;
import com.liujun.micro.autocode.generator.builder.constant.GenerateCodePackageKey;
import com.liujun.micro.autocode.generator.builder.constant.MyBatisKey;
import com.liujun.micro.autocode.generator.builder.entity.GenerateCodeContext;
import com.liujun.micro.autocode.generator.builder.entity.ImportPackageInfo;
import com.liujun.micro.autocode.generator.builder.operator.GenerateCodeInf;
import com.liujun.micro.autocode.generator.builder.operator.code.GenerateJavaMybatisMapperXml;
import com.liujun.micro.autocode.generator.builder.operator.utils.GenerateOutFileUtils;
import com.liujun.micro.autocode.generator.builder.operator.utils.ImportPackageUtils;
import com.liujun.micro.autocode.generator.builder.operator.utils.TableColumnUtils;
import com.liujun.micro.autocode.generator.database.entity.TableColumnDTO;
import com.liujun.micro.autocode.generator.database.entity.TableInfoDTO;
import com.liujun.micro.autocode.generator.javalanguage.serivce.NameProcess;

import java.util.*;
import java.util.Map.Entry;

/**
 * 以javabean的方法生成mybatis的mapper代码
 *
 * <p>修改，去掉默认值的限制
 *
 * @since 2018年4月15日 下午3:29:03
 * @version 0.0.1
 * @author liujun
 */
public class JavaCodeRepositoryMyBatisMapperCreate implements GenerateCodeInf {

  public static final String DOC = "数据库操作";

  @Override
  public void generateCode(GenerateCodeContext param) {

    Map<String, TableInfoDTO> tableMap = param.getTableMap();
    Map<String, List<TableColumnDTO>> map = param.getColumnMapList();
    Iterator<Entry<String, List<TableColumnDTO>>> iterTable = map.entrySet().iterator();

    while (iterTable.hasNext()) {
      Entry<String, List<TableColumnDTO>> entry = iterTable.next();
      String tableName = entry.getKey();
      TableInfoDTO tableMsg = tableMap.get(tableName);

      List<TableColumnDTO> columnList = entry.getValue();

      // 获取当前主键列表
      List<TableColumnDTO> primaryKeyList = TableColumnUtils.getPrimaryKey(columnList);
      // 获取po的完整路径
      ImportPackageInfo poPackage =
          ImportPackageUtils.getDefineClass(
              param.getPackageMap(), GenerateCodePackageKey.PERSIST_PO.getKey(), tableName);

      // 获取dao的完整路径
      ImportPackageInfo daoPackage =
          ImportPackageUtils.getDefineClass(
              param.getPackageMap(), GenerateCodePackageKey.PERSIST_DAO.getKey(), tableName);

      // 方法列表
      List<MethodInfo> methodList = param.getGenerateConfig().getGenerate().getCode();

      // 获取数据
      Map<String, TableColumnDTO> parseColumn = param.getColumnMapMap().get(tableName);

      // 进行mapper内容的生成
      StringBuilder sb =
          GenerateJavaMybatisMapperXml.INSTANCE.generateMapperXml(
              tableMsg,
              columnList,
              primaryKeyList,
              poPackage,
              daoPackage,
              methodList,
              parseColumn,
              DOC,
              param.getTypeEnum());

      String javaName = NameProcess.INSTANCE.toFieldName(tableName);

      // 定义项目内的完整目录结构
      String outMapperPath = param.getProjectPath().getRepositoryMybatisMapperNode().outPath();

      // 文件名
      String fileName = javaName + MyBatisKey.MYBATIS_SUFFIX_NAME;
      // 将mybatis文件的mapper输出到文件中
      GenerateOutFileUtils.outFile(sb, param.getFileBasePath(), outMapperPath, fileName);
    }
  }
}
