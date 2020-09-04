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
import com.liujun.micro.autocode.generator.javalanguage.constant.JavaKeyWord;
import com.liujun.micro.autocode.generator.javalanguage.serivce.NameProcess;

import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 * 领域层存储接口的实现，实现与dao层的调用
 *
 * @author liujun
 * @version 0.0.1
 */
public class JavaCodeRepositoryPersistenceCreate implements GenerateCodeInf {

  private static final String NAME_SUFFIX = "RepositoryImpl";
  private static final String CLASS_COMMENT = "的领域存储接口实现";

  @Override
  public void generateCode(GenerateCodeContext param) {
    Map<String, TableInfoDTO> tableMap = param.getTableMap();
    Map<String, List<TableColumnDTO>> map = param.getColumnMapList();
    Iterator<Map.Entry<String, List<TableColumnDTO>>> tableNameEntry = map.entrySet().iterator();
    while (tableNameEntry.hasNext()) {
      Map.Entry<String, List<TableColumnDTO>> tableNameItem = tableNameEntry.next();
      // 获取表信息
      TableInfoDTO tableInfo = param.getTableMap().get(tableNameItem.getKey());

      // 表名
      String tableName = tableNameItem.getKey();

      // 得到类名
      String tableClassName = NameProcess.INSTANCE.toJavaClassName(tableName);
      String className = tableClassName + NAME_SUFFIX;

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
              + CLASS_COMMENT;

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

      // 定义项目内的完整目录结构
      MenuNode mapperNode = MenuTreeProjectPath.getSrcJavaNode(param.getProjectMenuTree());
      String baseJavaPath = MenuTreeProcessUtil.outPath(mapperNode);
      javaPackageStr = baseJavaPath + Symbol.PATH + javaPackageStr;

      // 进行存储层的接口输出
     // GenerateOutFileUtils.outJavaFile(sb, param.getFileBasePath(), javaPackageStr, className);
    }
  }

  // private StringBuilder persistenceDefine(
  //    ImportPackageInfo persistClass,
  //    ImportPackageInfo domainEntityPackage,
  //    ImportPackageInfo poPackage,
  //    String comment) {
  //  StringBuilder classInfo = new StringBuilder();
  //
  //  // 定义包
  //  classInfo
  //      .append(JavaKeyWord.PACKAGE)
  //      .append(Symbol.SPACE)
  //      .append(persistClass.getPackagePath())
  //      .append(Symbol.SEMICOLON)
  //      .append(Symbol.ENTER_LINE);
  //  classInfo.append(Symbol.ENTER_LINE);
  //  classInfo.append(Symbol.ENTER_LINE);
  //
  //  // 1,导包
  //  classInfo.append(JavaKeyWord.BEAN_IMPORT_DATA).append(Symbol.ENTER_LINE);
  //  classInfo.append(JavaKeyWord.BEAN_IMPORT_TOSTRING).append(Symbol.ENTER_LINE);
  //
  //
  //
  //  classInfo.append(Symbol.ENTER_LINE);
  //
  //  // 添加类注释信息
  //  classInfo
  //      .append(JavaKeyWord.ANNO_CLASS)
  //      .append(Symbol.ENTER_LINE)
  //      .append(JavaKeyWord.ANNO_CLASS_MID);
  //  classInfo.append(Symbol.SPACE).append(classComment).append(Symbol.ENTER_LINE);
  //  classInfo
  //      .append(JavaKeyWord.ANNO_CLASS_MID)
  //      .append(Symbol.ENTER_LINE)
  //      .append(JavaKeyWord.DOC_VERSION);
  //  classInfo.append(Symbol.ENTER_LINE).append(JavaKeyWord.DOC_AUTH).append(author);
  //  classInfo.append(Symbol.ENTER_LINE).append(JavaKeyWord.ANNO_OVER).append(Symbol.ENTER_LINE);
  //
  //  // 引入@data和@toString
  //  classInfo.append(JavaKeyWord.BEAN_USE_DATA).append(Symbol.ENTER_LINE);
  //  classInfo.append(JavaKeyWord.BEAN_USE_TOSTRING).append(Symbol.ENTER_LINE);
  //  classInfo.append(JavaKeyWord.ClASS_START).append(className);
  //  classInfo.append(Symbol.SPACE).append(Symbol.BRACE_LEFT);
  //  classInfo.append(Symbol.ENTER_LINE);
  //  classInfo.append(Symbol.ENTER_LINE);
  //  classInfo.append(Symbol.ENTER_LINE);
  // }
}
