package com.liujun.auto.generator.builder.ddd.full.repositorymybatis;

import com.liujun.auto.constant.Symbol;
import com.liujun.auto.generator.builder.ddd.constant.GenerateCodePackageKey;
import com.liujun.auto.generator.builder.ddd.constant.GenerateCommConstant;
import com.liujun.auto.generator.builder.ddd.constant.PkgBuildMethod;
import com.liujun.auto.generator.builder.ddd.entity.DataParam;
import com.liujun.auto.generator.builder.ddd.entity.GenerateCodeContext;
import com.liujun.auto.generator.builder.ddd.entity.ImportPackageInfo;
import com.liujun.auto.generator.builder.GenerateCodeInf;
import com.liujun.auto.generator.builder.ddd.code.GenerateJavaRepositoryJunitDao;
import com.liujun.auto.generator.builder.ddd.code.GenerateJunitDefine;
import com.liujun.auto.generator.builder.utils.GenerateOutFileUtils;
import com.liujun.auto.generator.builder.utils.GeneratePathUtils;
import com.liujun.auto.generator.builder.utils.TableColumnUtils;
import com.liujun.auto.generator.database.entity.TableColumnDTO;
import com.liujun.auto.generator.database.entity.TableInfoDTO;
import com.liujun.auto.generator.javalanguage.serivce.NameProcess;

import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

/**
 * 生成单元测试相关的代码
 *
 * @author liujun
 * @version 0.0.1
 * @since 2018年4月15日 下午4:11:42
 */
public class JavaCodeRepositoryMyBatisJunitDaoCreate implements GenerateCodeInf {

  /** 注释 */
  private static final String DATABASE_DOC = "数据库操作单元测试";

  public static final JavaCodeRepositoryMyBatisJunitDaoCreate INSTANCE =
      new JavaCodeRepositoryMyBatisJunitDaoCreate();

  @Override
  public void generateCode(GenerateCodeContext param) {

    Map<String, List<TableColumnDTO>> map = param.getColumnMapList();
    Iterator<Entry<String, List<TableColumnDTO>>> tableNameEntry = map.entrySet().iterator();
    while (tableNameEntry.hasNext()) {
      Entry<String, List<TableColumnDTO>> entry = tableNameEntry.next();
      List<TableColumnDTO> columnList = entry.getValue();
      String tableName = entry.getKey();

      // 获取当前主键列表
      List<TableColumnDTO> primaryKeyList = TableColumnUtils.getPrimaryKeyDefaultOne(columnList);

      // 获取所有列的信息
      Map<String, TableColumnDTO> tableColumnMap = param.getColumnMap().get(entry.getKey());

      // 获取dao的完整路径
      ImportPackageInfo daoPackage = param.getPkg(tableName, GenerateCodePackageKey.PERSIST_MAPPER);

      // 首字母大写
      String className =
          GenerateJunitDefine.TEST_SUFFIX_NAME
              + NameProcess.INSTANCE.toJavaNameFirstUpper(daoPackage.getClassName());

      // 获取以java定义的package路径
      String javaDaoPackageStr = param.getJavaCodePackage().getRepositoryMapperNode().outJavaPackage();

      // 定义项目内的完整目录结构
      String baseJavaPath = param.getProjectPath().getTestJavaNode().outPath();
      javaDaoPackageStr = baseJavaPath + Symbol.PATH + javaDaoPackageStr;

      // 单元测试的路径
      String junitDaoPackageStr =
          param.getJavaCodePackage().getRepositoryMapperNode().outJavaPackage();

      // 包信息
      ImportPackageInfo junitPackageInfo =
          PkgBuildMethod.classInfoComment(junitDaoPackageStr, className, DATABASE_DOC);

      // 获取表信息
      TableInfoDTO tableInfo = param.getTableMap().get(tableName);

      // 构建参数信息
      DataParam generateParam =
          DataParam.builder()
              .putPkg(
                  GenerateCodePackageKey.PERSIST_PO,
                  param.getPkg(tableName, GenerateCodePackageKey.PERSIST_PO))
              .putPkg(
                  GenerateCodePackageKey.PERSIST_MAPPER,
                  param.getPkg(tableName, GenerateCodePackageKey.PERSIST_MAPPER))
              .putPkg(GenerateCodePackageKey.PERSIST_JUNIT_DAO, junitPackageInfo)
              .putPkg(
                  GenerateCodePackageKey.REPOSITORY_MAPPER_CONFIG,
                  param.getPkg(
                      GenerateCommConstant.GENERATE_JUNIT_CONFIG,
                      GenerateCodePackageKey.REPOSITORY_MAPPER_CONFIG))
              .putPkg(
                  GenerateCodePackageKey.DOMAIN_DO,
                  param.getPkg(tableName, GenerateCodePackageKey.DOMAIN_DO))
              .author(param.getGenerateConfig().getGenerate().getAuthor())
              .methodList(param.getGenerateConfig().getGenerate().getMethodList())
              .tableInfo(tableInfo)
              .databaseType(param.getTypeEnum())
              .columnList(entry.getValue())
              .primaryKeyList(primaryKeyList)
              .columnMap(tableColumnMap)
              .build();

      // 进行单元测试代码的生成
      StringBuilder sb =
          GenerateJavaRepositoryJunitDao.INSTANCE.generateJunitService(generateParam);

      // 将数据库存储的文件输出
      GenerateOutFileUtils.outJavaFile(
          sb, GeneratePathUtils.outServicePath(param), javaDaoPackageStr, className);
    }
  }
}
