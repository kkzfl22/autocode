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
import com.liujun.micro.autocode.generator.builder.operator.code.GenerateJavaBean;
import com.liujun.micro.autocode.generator.builder.utils.MenuTreeProcessUtil;
import com.liujun.micro.autocode.generator.database.entity.TableColumnDTO;
import com.liujun.micro.autocode.generator.database.entity.TableInfoDTO;
import com.liujun.micro.autocode.generator.javalanguage.constant.JavaKeyWord;
import com.liujun.micro.autocode.generator.javalanguage.serivce.NameProcess;
import com.liujun.micro.autocode.utils.FileUtils;

import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

/**
 * 创建数据库访问层的PO对象
 *
 * @author liujun
 * @version 0.0.1
 * @since 2020/04/08
 */
public class JavaCodeRepositoryObjectCreate implements GenerateCodeInf {

  /** 注释中的描述 */
  private static final String DOC_ANNOTATION = "的数据库存储实体信息";

  /** 用来生成存储层的实体名称 */
  public static final String REPOSITORY_PO = "PO";

  @Override
  public void generateCode(GenerateCodeContext param) {

    Map<String, TableInfoDTO> tableMap = param.getTableMap();
    Map<String, List<TableColumnDTO>> map = param.getColumnMapList();
    Iterator<Map.Entry<String, List<TableColumnDTO>>> tableNameEntry = map.entrySet().iterator();

    while (tableNameEntry.hasNext()) {
      Map.Entry<String, List<TableColumnDTO>> tableEntry = tableNameEntry.next();
      // 表名
      String tableName = tableEntry.getKey();
      // 得到类名
      String tableClassName = NameProcess.INSTANCE.toJavaClassName(tableName);
      String className = tableClassName + REPOSITORY_PO;
      // 表信息
      TableInfoDTO tableInfo = tableMap.get(tableName);
      // 获取以java定义的package路径
      DomainMenuTree menuTree = param.getMenuTree();
      MenuNode poNode = MenuTreePackagePath.getRepositoryObjectNode(menuTree);
      String javaPackageStr = MenuTreeProcessUtil.outJavaPackage(poNode);

      // 将当前包信息存入到上下文对象信息中
      this.putPersistPackageInfo(
          javaPackageStr, tableName, className, param.getPackageMap(), tableMap.size());

      // 进行存储层的bean代码生成
      StringBuilder persistBean =
          GenerateJavaBean.INSTANCE.generateJavaBean(
              tableInfo,
              className,
              tableEntry.getValue(),
              DOC_ANNOTATION,
              javaPackageStr,
              param.getGenerateConfig().getGenerate().getCode());

      // 定义mapper文件
      MenuNode mapperNode = MenuTreeProjectPath.getSrcJavaNode(param.getProjectMenuTree());
      String baseJavaPath = MenuTreeProcessUtil.outPath(mapperNode);
      javaPackageStr = baseJavaPath + Symbol.PATH + javaPackageStr;

      // 将代码输出到文件中
      this.outFile(persistBean, param.getFileBasePath(), className, javaPackageStr);
    }
  }

  /**
   * 将当前数据库存储资源的包信息放入到流程的map中
   *
   * @param javaPackageStr 基础包信息
   * @param tableName 表名
   * @param className 类名
   * @param packageMap 流程中的包路径容器
   * @param initSize 初始化大小
   */
  private void putPersistPackageInfo(
      String javaPackageStr,
      String tableName,
      String className,
      Map<String, Map<String, ImportPackageInfo>> packageMap,
      int initSize) {
    // 将当前的类路径及名称记录下
    ImportPackageInfo packageInfo = new ImportPackageInfo(javaPackageStr, className);

    Map<String, ImportPackageInfo> packageMapInfo =
        packageMap.get(GenerateCodeDomainKey.PERSIST_PO.getKey());

    if (null == packageMapInfo) {
      packageMapInfo = new HashMap<>(initSize);
      packageMap.put(GenerateCodeDomainKey.PERSIST_PO.getKey(), packageMapInfo);
    }
    packageMapInfo.put(tableName, packageInfo);
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
