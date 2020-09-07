package com.liujun.micro.autocode.generator.builder.operator.ddd.full;

import com.liujun.micro.autocode.constant.Symbol;
import com.liujun.micro.autocode.entity.config.MethodInfo;
import com.liujun.micro.autocode.generator.builder.constant.*;
import com.liujun.micro.autocode.generator.builder.entity.GenerateCodeContext;
import com.liujun.micro.autocode.generator.builder.entity.ImportPackageInfo;
import com.liujun.micro.autocode.generator.builder.operator.GenerateCodeInf;
import com.liujun.micro.autocode.generator.builder.operator.code.junit.GenerateJunitDefine;
import com.liujun.micro.autocode.generator.builder.operator.utils.GenerateOutFileUtils;
import com.liujun.micro.autocode.generator.builder.operator.utils.ImportPackageUtils;
import com.liujun.micro.autocode.generator.builder.operator.utils.TableColumnUtils;
import com.liujun.micro.autocode.generator.database.entity.TableColumnDTO;
import com.liujun.micro.autocode.generator.database.entity.TableInfoDTO;
import com.liujun.micro.autocode.generator.javalanguage.serivce.NameProcess;

import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

/**
 * 生成单元测试相关的代码
 *
 * @since 2018年4月15日 下午4:11:42
 * @version 0.0.1
 * @author liujun
 */
public class JavaCodeRepositoryJunitDaoCreate implements GenerateCodeInf {

  /** 注释 */
  private static final String DATABASE_DOC = "数据库操作单元测试";

  @Override
  public void generateCode(GenerateCodeContext param) {

    Map<String, List<TableColumnDTO>> map = param.getColumnMapList();
    Iterator<Entry<String, List<TableColumnDTO>>> tableNameEntry = map.entrySet().iterator();
    while (tableNameEntry.hasNext()) {
      Entry<String, List<TableColumnDTO>> entry = tableNameEntry.next();
      List<TableColumnDTO> columnList = entry.getValue();
      String tableName = entry.getKey();

      // 获取当前主键列表
      List<TableColumnDTO> primaryKeyList = TableColumnUtils.getPrimaryKey(columnList);

      // 获取表信息
      TableInfoDTO tableInfo = param.getTableMap().get(entry.getKey());

      // 获取所有列的信息
      Map<String, TableColumnDTO> tableColumnMap = param.getColumnMapMap().get(entry.getKey());

      // 获取po的完整路径
      ImportPackageInfo poPackage =
          ImportPackageUtils.getDefineClass(
              param.getPackageMap(), GenerateCodePackageKey.PERSIST_PO.getKey(), tableName);

      // 获取dao的完整路径
      ImportPackageInfo daoPackage =
          ImportPackageUtils.getDefineClass(
              param.getPackageMap(), GenerateCodePackageKey.PERSIST_DAO.getKey(), tableName);

      // 首字母大写
      String className =
          GenerateJunitDefine.DAO_TEST_NAME
              + NameProcess.INSTANCE.toJavaNameFirstUpper(daoPackage.getClassName());

      // 获取以java定义的package路径
      String javaDaoPackageStr = param.getJavaCodePackage().getRepositoryDaoNode().outJavaPackage();

      // 定义项目内的完整目录结构
      String baseJavaPath = param.getProjectPath().getTestJavaNode().outPath();
      javaDaoPackageStr = baseJavaPath + Symbol.PATH + javaDaoPackageStr;

      // 单元测试的路径
      String junitDaoPackageStr =
          param.getJavaCodePackage().getRepositoryDaoNode().outJavaPackage();

      // 包信息
      ImportPackageInfo packageInfo =
          new ImportPackageInfo(junitDaoPackageStr, className, DATABASE_DOC);

      // 单元测试的父类的路径
      String junitParentPackageStr = param.getJavaCodePackage().getPkgRoot().outJavaPackage();

      // 包信息
      ImportPackageInfo junitParent =
          new ImportPackageInfo(
              junitParentPackageStr, GenerateCommImport.JUNIT_PARENT_NAME, Symbol.EMPTY);

      // 方法信息
      List<MethodInfo> methodList = param.getGenerateConfig().getGenerate().getCode();

      // 进行单元测试代码的生成
      StringBuilder sb =
          GenerateJunitDefine.INSTANCE.generateJunit(
              junitParent,
              poPackage,
              daoPackage,
              packageInfo,
              param.getTypeEnum(),
              columnList,
              primaryKeyList,
              methodList,
              tableColumnMap,
              null,
              param.getGenerateConfig().getGenerate().getAuthor());

      // 将数据库存储的文件输出
      GenerateOutFileUtils.outJavaFile(sb, param.getFileBasePath(), javaDaoPackageStr, className);
    }
  }
}
