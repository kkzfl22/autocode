package com.liujun.micro.autocode.generator.builder.operator.ddd;

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
import com.liujun.micro.autocode.generator.builder.operator.code.junit.GenerateJunitDao;
import com.liujun.micro.autocode.generator.builder.operator.code.junit.GenerateJunitDaoQuery;
import com.liujun.micro.autocode.generator.builder.operator.code.junit.GenerateJunitDaoUpdate;
import com.liujun.micro.autocode.generator.builder.operator.utils.MethodUtils;
import com.liujun.micro.autocode.generator.builder.operator.utils.TableColumnUtils;
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
 * 生成单元测试相关的代码
 *
 * @since 2018年4月15日 下午4:11:42
 * @version 0.0.1
 * @author liujun
 */
public class JavaCodeRepositoryJunitDaoCreate implements GenerateCodeInf {

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
          GenerateJunitDao.DAO_TEST_NAME
              + NameProcess.INSTANCE.toJavaNameFirstUpper(daoPackage.getClassName());

      // 获取以java定义的package路径
      DomainMenuTree menuTree = param.getMenuTree();
      MenuNode daoNode = MenuTreePackagePath.getRepositoryDaoNode(menuTree);
      String javaDaoPackageStr = MenuTreeProcessUtil.outJavaPackage(daoNode);

      // 文件头定义
      StringBuilder sb =
          GenerateJunitDao.INSTANCE.defineHead(
              poPackage, daoPackage, param.getMenuTree(), tableInfo, className);

      // 操作数据之前的初始化相关的工作
      GenerateJunitDao.INSTANCE.beforeMethod(
          sb, poPackage, daoPackage, param.getTypeEnum(), columnList, primaryKeyList);

      // 配制的方法
      List<MethodInfo> methodList = param.getGenerateConfig().getGenerate().getCode();

      // 公共的数据对比方法
      GenerateJunitDao.INSTANCE.queryResponseAssert(
          sb, columnList, poPackage, methodList, primaryKeyList);

      for (MethodInfo methodItem : methodList) {
        // 添加方法
        if (MethodTypeEnum.INSERT.getType().equals(methodItem.getOperator())) {
          GenerateJunitDaoUpdate.INSTANCE.insertMethod(sb, methodItem, poPackage, methodList);
        }
        // 修改方法
        else if (MethodTypeEnum.UPDATE.getType().equals(methodItem.getOperator())) {
          // 插入方法
          MethodInfo insertMethod = MethodUtils.getInsertMethod(methodList);
          GenerateJunitDaoUpdate.INSTANCE.oneUpdateMethod(sb, methodItem, insertMethod);
        }
        // 非主键的删除方法
        else if (MethodTypeEnum.DELETE.getType().equals(methodItem.getOperator())
            && (methodItem.getPrimaryFlag() == null || !methodItem.getPrimaryFlag())) {
          GenerateJunitDaoUpdate.INSTANCE.batchDelete(
              sb,
              methodItem,
              poPackage,
              tableColumnMap,
              methodList,
              param.getTypeEnum(),
              primaryKeyList);
        }
        // 数据查询
        else if (MethodTypeEnum.QUERY.getType().equals(methodItem.getOperator())) {
          GenerateJunitDaoQuery.INSTANCE.queryMethod(
              sb,
              methodItem,
              poPackage,
              tableColumnMap,
              methodList,
              param.getTypeEnum(),
              primaryKeyList);
        }
      }

      // 最后执执行after的清理方法，调用主键删除
      MethodInfo methodItem = MethodUtils.getPrimaryDeleteMethod(methodList);
      if (null != methodItem) {
        GenerateJunitDaoUpdate.INSTANCE.deleteMethod(sb, methodItem, poPackage);
      }

      // 结束
      sb.append(Symbol.BRACE_RIGHT);

      // 定义mapper文件
      MenuNode mapperNode = MenuTreeProjectPath.getTestJavaNode(param.getProjectMenuTree());
      String baseJavaPath = MenuTreeProcessUtil.outPath(mapperNode);
      javaDaoPackageStr = baseJavaPath + Symbol.PATH + javaDaoPackageStr;

      // 将代码输出到文件中
      this.outFile(sb, param.getFileBasePath(), className, javaDaoPackageStr);
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
