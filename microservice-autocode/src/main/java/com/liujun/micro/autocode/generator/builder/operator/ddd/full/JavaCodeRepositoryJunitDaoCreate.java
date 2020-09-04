package com.liujun.micro.autocode.generator.builder.operator.ddd.full;

import com.liujun.micro.autocode.config.menuTree.DomainMenuTree;
import com.liujun.micro.autocode.config.menuTree.MenuNode;
import com.liujun.micro.autocode.config.menuTree.MenuTreePackagePath;
import com.liujun.micro.autocode.config.menuTree.MenuTreeProjectPath;
import com.liujun.micro.autocode.constant.Symbol;
import com.liujun.micro.autocode.entity.config.MethodInfo;
import com.liujun.micro.autocode.generator.builder.constant.*;
import com.liujun.micro.autocode.generator.builder.entity.GenerateCodeContext;
import com.liujun.micro.autocode.generator.builder.entity.ImportPackageInfo;
import com.liujun.micro.autocode.generator.builder.operator.GenerateCodeInf;
import com.liujun.micro.autocode.generator.builder.operator.code.junit.GenerateJunitDefine;
import com.liujun.micro.autocode.generator.builder.operator.code.junit.GenerateJunitQuery;
import com.liujun.micro.autocode.generator.builder.operator.code.junit.GenerateJunitUpdate;
import com.liujun.micro.autocode.generator.builder.operator.utils.GenerateOutFileUtils;
import com.liujun.micro.autocode.generator.builder.operator.utils.MethodUtils;
import com.liujun.micro.autocode.generator.builder.operator.utils.TableColumnUtils;
import com.liujun.micro.autocode.generator.builder.utils.MenuTreeProcessUtil;
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
          param.getPackageMap().get(GenerateCodeDomainKey.PERSIST_PO.getKey()).get(tableName);

      // 获取dao的完整路径
      ImportPackageInfo daoPackage =
          param.getPackageMap().get(GenerateCodeDomainKey.PERSIST_DAO.getKey()).get(tableName);

      // 首字母大写
      String className =
          GenerateJunitDefine.DAO_TEST_NAME
              + NameProcess.INSTANCE.toJavaNameFirstUpper(daoPackage.getClassName());

      // 获取以java定义的package路径
      DomainMenuTree menuTree = param.getMenuTree();
      MenuNode daoNode = MenuTreePackagePath.getRepositoryDaoNode(menuTree);
      String javaDaoPackageStr = MenuTreeProcessUtil.outJavaPackage(daoNode);

      // 定义项目内的完整目录结构
      MenuNode mapperNode = MenuTreeProjectPath.getTestJavaNode(param.getProjectMenuTree());
      String baseJavaPath = MenuTreeProcessUtil.outPath(mapperNode);
      javaDaoPackageStr = baseJavaPath + Symbol.PATH + javaDaoPackageStr;

      // 单元测试的路径
      MenuNode poNode = MenuTreePackagePath.getRepositoryDaoNode(menuTree);
      String junitDaoPackageStr = MenuTreeProcessUtil.outJavaPackage(poNode);

      // 包信息
      ImportPackageInfo packageInfo =
          new ImportPackageInfo(junitDaoPackageStr, className, DATABASE_DOC);

      // 方法信息
      List<MethodInfo> methodList = param.getGenerateConfig().getGenerate().getCode();

      // 公共需要导入的数据包信息
      List<String> importList =
          (List<String>) param.getDataMap().get(ContextCfgEnum.CONTEXT_JUNIT_IMPORT.getKey());

      // 进行单元测试代码的生成
      StringBuilder sb =
          GenerateJunitDefine.INSTANCE.generateJunit(
              poPackage,
              daoPackage,
              packageInfo,
              param.getTypeEnum(),
              columnList,
              primaryKeyList,
              methodList,
              tableColumnMap,
              importList,
              param.getGenerateConfig().getGenerate().getAuthor());

      // 将数据库存储的文件输出
      GenerateOutFileUtils.outJavaFile(sb, param.getFileBasePath(), javaDaoPackageStr, className);
    }
  }
}
