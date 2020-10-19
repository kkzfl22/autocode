package com.liujun.micro.autocode.generator.builder.operator.ddd.full;

import com.liujun.micro.autocode.constant.Symbol;
import com.liujun.micro.autocode.generator.builder.constant.GenerateCodePackageKey;
import com.liujun.micro.autocode.generator.builder.constant.JavaVarName;
import com.liujun.micro.autocode.generator.builder.entity.GenerateCodeContext;
import com.liujun.micro.autocode.generator.builder.entity.ImportPackageInfo;
import com.liujun.micro.autocode.generator.builder.operator.GenerateCodeInf;
import com.liujun.micro.autocode.generator.builder.operator.code.GenerateJavaDaoInterface;
import com.liujun.micro.autocode.generator.builder.operator.utils.GenerateOutFileUtils;
import com.liujun.micro.autocode.generator.builder.operator.utils.GeneratePathUtils;
import com.liujun.micro.autocode.generator.builder.operator.utils.ImportPackageUtils;
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

  private static final String DAO_SUFFIX = "Mapper";
  private static final String DAO_COMMENT = "的数据库操作";

  public static final JavaCodeRepositoryDaoInfCreate INSTANCE =
      new JavaCodeRepositoryDaoInfCreate();

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
      String javaPackageStr = param.getJavaCodePackage().getRepositoryDaoNode().outJavaPackage();

      // 注释
      String docComment =
          tableInfo.getTableComment()
              + Symbol.BRACKET_LEFT
              + tableInfo.getTableName()
              + Symbol.BRACKET_RIGHT
              + DAO_COMMENT;

      // 将dao信息进行储存至流程中
      ImportPackageInfo daoPackageInfo =
          new ImportPackageInfo(
              javaPackageStr, className, docComment, JavaVarName.SPRING_INSTANCE_NAME);
      ImportPackageUtils.putPackageInfo(
          tableName,
          param.getPackageMap(),
          GenerateCodePackageKey.PERSIST_DAO.getKey(),
          daoPackageInfo,
          tableMap.size());

      // 获取实体信息
      ImportPackageInfo poPackageInfo =
          ImportPackageUtils.getDefineClass(
              param.getPackageMap(), GenerateCodePackageKey.PERSIST_PO.getKey(), tableName);

      // 进行dao的相关方法的生成
      StringBuilder sb =
          GenerateJavaDaoInterface.INSTANCE.generateJavaInterface(
              poPackageInfo,
              daoPackageInfo,
              param.getGenerateConfig().getGenerate().getCode(),
              param.getGenerateConfig().getGenerate().getAuthor());

      // 定义项目内的完整目录结构
      String baseJavaPath = param.getProjectPath().getSrcJavaNode().outPath();
      javaPackageStr = baseJavaPath + Symbol.PATH + javaPackageStr;

      // 进行存储层的接口输出
      GenerateOutFileUtils.outJavaFile(
          sb, GeneratePathUtils.outServicePath(param), javaPackageStr, className);
    }
  }
}
