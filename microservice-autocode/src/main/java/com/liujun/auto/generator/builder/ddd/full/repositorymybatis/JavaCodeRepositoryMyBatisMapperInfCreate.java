package com.liujun.auto.generator.builder.ddd.full.repositorymybatis;

import com.liujun.auto.constant.Symbol;
import com.liujun.auto.generator.builder.ddd.constant.GenerateCodePackageKey;
import com.liujun.auto.generator.builder.ddd.constant.JavaVarName;
import com.liujun.auto.generator.builder.ddd.constant.PkgBuildMethod;
import com.liujun.auto.generator.builder.ddd.entity.GenerateCodeContext;
import com.liujun.auto.generator.builder.ddd.entity.ImportPackageInfo;
import com.liujun.auto.generator.builder.GenerateCodeInf;
import com.liujun.auto.generator.builder.ddd.code.GenerateJavaDaoInterface;
import com.liujun.auto.generator.builder.utils.GenerateOutFileUtils;
import com.liujun.auto.generator.builder.utils.GeneratePathUtils;
import com.liujun.auto.generator.database.entity.TableColumnDTO;
import com.liujun.auto.generator.database.entity.TableInfoDTO;
import com.liujun.auto.generator.javalanguage.serivce.NameProcess;

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
public class JavaCodeRepositoryMyBatisMapperInfCreate implements GenerateCodeInf {

  private static final String DAO_SUFFIX = "Mapper";
  private static final String DAO_COMMENT = "的数据库操作";

  public static final JavaCodeRepositoryMyBatisMapperInfCreate INSTANCE =
      new JavaCodeRepositoryMyBatisMapperInfCreate();

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
      String className = this.getClassName(tableName);

      // 基础包的依赖构建
      this.daoRepositoryDependency(param, tableInfo);

      // 获取以java定义的package路径
      String javaPackageStr = param.getJavaCodePackage().getRepositoryMapperNode().outJavaPackage();

      // 进行dao的相关方法的生成
      StringBuilder sb = GenerateJavaDaoInterface.INSTANCE.generateJavaInterface(param, tableName);

      // 定义项目内的完整目录结构
      String baseJavaPath = param.getProjectPath().getSrcJavaNode().outPath();
      javaPackageStr = baseJavaPath + Symbol.PATH + javaPackageStr;

      // 进行存储层的接口输出
      GenerateOutFileUtils.outJavaFile(
          sb, GeneratePathUtils.outServicePath(param), javaPackageStr, className);
    }
  }

  /**
   * 获取类名
   *
   * @param tableName
   * @return
   */
  private String getClassName(String tableName) {
    // 得到类名
    String tableClassName = NameProcess.INSTANCE.toJavaClassName(tableName);
    return tableClassName + DAO_SUFFIX;
  }

  /**
   * dao接口的依赖
   *
   * @param param
   * @param tableInfo
   */
  public void daoRepositoryDependency(GenerateCodeContext param, TableInfoDTO tableInfo) {
    // 获取以java定义的package路径
    String javaPackageStr = param.getJavaCodePackage().getRepositoryMapperNode().outJavaPackage();

    // 注释
    String docComment =
        tableInfo.getTableComment()
            + Symbol.BRACKET_LEFT
            + tableInfo.getTableName()
            + Symbol.BRACKET_RIGHT
            + DAO_COMMENT;

    String className = this.getClassName(tableInfo.getTableName());

    // 将dao信息进行储存至流程中
    ImportPackageInfo daoPackageInfo =
        PkgBuildMethod.classInfoVarInfo(
            javaPackageStr, className, docComment, JavaVarName.SPRING_INSTANCE_NAME);

    param.putPkg(tableInfo.getTableName(), GenerateCodePackageKey.PERSIST_MAPPER, daoPackageInfo);
  }
}
