package com.liujun.micro.autocode.generator.builder.operator.ddd.full;

import com.liujun.micro.autocode.config.menuTree.DomainMenuTree;
import com.liujun.micro.autocode.config.menuTree.MenuNode;
import com.liujun.micro.autocode.config.menuTree.MenuTreePackagePath;
import com.liujun.micro.autocode.config.menuTree.MenuTreeProjectPath;
import com.liujun.micro.autocode.constant.Symbol;
import com.liujun.micro.autocode.generator.builder.constant.GenerateCodeDomainKey;
import com.liujun.micro.autocode.generator.builder.entity.GenerateCodeContext;
import com.liujun.micro.autocode.generator.builder.entity.ImportPackageInfo;
import com.liujun.micro.autocode.generator.builder.operator.GenerateCodeInf;
import com.liujun.micro.autocode.generator.builder.operator.code.GenerateJavaInterface;
import com.liujun.micro.autocode.generator.builder.operator.utils.GenerateOutFileUtils;
import com.liujun.micro.autocode.generator.builder.operator.utils.ImportPackageUtils;
import com.liujun.micro.autocode.generator.builder.utils.MenuTreeProcessUtil;
import com.liujun.micro.autocode.generator.database.entity.TableColumnDTO;
import com.liujun.micro.autocode.generator.database.entity.TableInfoDTO;
import com.liujun.micro.autocode.generator.javalanguage.serivce.NameProcess;

import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

/**
 * 生成数据库的相关操作接口
 *
 * @author liujun
 * @version 1.0.0
 */
public class JavaCodeRepositoryDaoInfCreate implements GenerateCodeInf {

  private static final String DAO_SUFFIX = "DAO";
  private static final String DAO_COMMENT = "的数据库操作";

  @Override
  public void generateCode(GenerateCodeContext param) {

    Map<String, TableInfoDTO> tableMap = param.getTableMap();
    Map<String, List<TableColumnDTO>> map = param.getColumnMapList();
    Iterator<Entry<String, List<TableColumnDTO>>> tableNameEntry = map.entrySet().iterator();
    while (tableNameEntry.hasNext()) {
      Entry<String, List<TableColumnDTO>> tableNameItem = tableNameEntry.next();
      // 获取表信息
      TableInfoDTO tableInfo = param.getTableMap().get(tableNameItem.getKey());

      // 表名
      String tableName = tableNameItem.getKey();

      // 得到类名
      String tableClassName = NameProcess.INSTANCE.toJavaClassName(tableName);
      String className = tableClassName + DAO_SUFFIX;

      // 获取以java定义的package路径
      DomainMenuTree menuTree = param.getMenuTree();
      MenuNode poNode = MenuTreePackagePath.getRepositoryDaoNode(menuTree);
      String javaPackageStr = MenuTreeProcessUtil.outJavaPackage(poNode);

      // 注释
      String docComment =
          tableInfo.getTableComment()
              + Symbol.BRACKET_LEFT
              + tableInfo.getTableName()
              + Symbol.BRACKET_RIGHT
              + DAO_COMMENT;

      // 将dao信息进行储存至流程中
      ImportPackageInfo daoPackageInfo =
          new ImportPackageInfo(javaPackageStr, className, docComment);
      ImportPackageUtils.putPackageInfo(
          tableName,
          param.getPackageMap(),
          GenerateCodeDomainKey.PERSIST_DAO.getKey(),
          daoPackageInfo,
          tableMap.size());

      // 获取实体信息
      ImportPackageInfo poPackageInfo =
          ImportPackageUtils.getDefineClass(
              param.getPackageMap(), GenerateCodeDomainKey.PERSIST_PO.getKey(), tableName);

      // 进行dao的相关方法的生成
      StringBuilder sb =
          GenerateJavaInterface.INSTANCE.generateJavaInterface(
              poPackageInfo,
              daoPackageInfo,
              param.getGenerateConfig().getGenerate().getCode(),
              param.getGenerateConfig().getGenerate().getAuthor());

      // 定义项目内的完整目录结构
      MenuNode mapperNode = MenuTreeProjectPath.getSrcJavaNode(param.getProjectMenuTree());
      String baseJavaPath = MenuTreeProcessUtil.outPath(mapperNode);
      javaPackageStr = baseJavaPath + Symbol.PATH + javaPackageStr;

      // 进行存储层的接口输出
      GenerateOutFileUtils.outJavaFile(sb, param.getFileBasePath(), javaPackageStr, className);
    }
  }
}
