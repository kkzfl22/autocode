package com.liujun.micro.autocode.generator.builder.operator.ddd;

import com.liujun.micro.autocode.config.menuTree.DomainMenuTree;
import com.liujun.micro.autocode.config.menuTree.MenuNode;
import com.liujun.micro.autocode.config.menuTree.MenuTreePackagePath;
import com.liujun.micro.autocode.config.menuTree.MenuTreeProjectPath;
import com.liujun.micro.autocode.constant.Symbol;
import com.liujun.micro.autocode.generator.builder.constant.GenerateCodeDomainKey;
import com.liujun.micro.autocode.generator.builder.entity.GenerateCodeContext;
import com.liujun.micro.autocode.generator.builder.entity.ImportPackageInfo;
import com.liujun.micro.autocode.generator.builder.operator.GenerateCodeInf;
import com.liujun.micro.autocode.generator.builder.operator.code.GenerateJavaDao;
import com.liujun.micro.autocode.generator.builder.operator.utils.ImportPackageUtils;
import com.liujun.micro.autocode.generator.builder.utils.MenuTreeProcessUtil;
import com.liujun.micro.autocode.generator.database.entity.TableColumnDTO;
import com.liujun.micro.autocode.generator.database.entity.TableInfoDTO;
import com.liujun.micro.autocode.generator.javalanguage.constant.JavaKeyWord;
import com.liujun.micro.autocode.generator.javalanguage.serivce.NameProcess;
import com.liujun.micro.autocode.utils.FileUtils;

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

  public static final String DAO_SUFFIX = "DAO";
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

      // 将dao信息进行储存至流程中
      ImportPackageInfo daoPackageInfo = new ImportPackageInfo(javaPackageStr, className);
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
          GenerateJavaDao.INSTANCE.generateJavaDao(
              tableInfo,
              className,
              poPackageInfo,
              daoPackageInfo,
              param.getGenerateConfig().getGenerate().getCode(),
              DAO_COMMENT);


      // 定义mapper文件
      MenuNode mapperNode = MenuTreeProjectPath.getSrcJavaNode(param.getProjectMenuTree());
      String baseJavaPath = MenuTreeProcessUtil.outPath(mapperNode);
      javaPackageStr = baseJavaPath + Symbol.PATH + javaPackageStr;

      // 输出的文件路径及名称
      this.outFile(sb, param.getFileBasePath(), className, javaPackageStr);
    }
  }

  /**
   * 进行文件输出操作
   *
   * @param sb 输出字符
   * @param path 路径
   * @param className 类名
   * @param definePackage 定义的输出目录
   */
  private void outFile(StringBuilder sb, String path, String className, String definePackage) {
    // 获取源代码

    // 获取以路径/进行输出
    String javaPathStr = MenuTreeProcessUtil.outPath(definePackage);

    StringBuilder outPath = new StringBuilder();
    // 输出的基础路径
    outPath.append(path);
    outPath.append(Symbol.PATH);
    outPath.append(javaPathStr);

    // 文件名
    String fileName = className + JavaKeyWord.FILE_SUFFIX;

    // 文件输出操作
    FileUtils.writeFile(outPath.toString(), fileName, sb);
  }
}
