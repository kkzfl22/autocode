package com.liujun.auto.generator.builder.operator.ddd.full;

import com.liujun.auto.constant.Symbol;
import com.liujun.auto.generator.builder.constant.GenerateCodePackageKey;
import com.liujun.auto.generator.builder.entity.GenerateCodeContext;
import com.liujun.auto.generator.builder.entity.ImportPackageInfo;
import com.liujun.auto.generator.builder.operator.ddd.code.GenerateJavaRepositoryPersistenceInvoke;
import com.liujun.auto.generator.builder.operator.utils.GenerateOutFileUtils;
import com.liujun.auto.generator.builder.operator.utils.GeneratePathUtils;
import com.liujun.auto.generator.builder.operator.utils.JavaCommentUtil;
import com.liujun.auto.generator.database.entity.TableColumnDTO;
import com.liujun.auto.generator.database.entity.TableInfoDTO;
import com.liujun.auto.generator.javalanguage.serivce.NameProcess;
import com.liujun.auto.generator.builder.operator.GenerateCodeInf;
import com.liujun.micro.autocode.generator.builder.operator.utils.ImportPackageUtils;

import java.util.Iterator;
import java.util.List;
import java.util.Map;

/**
 * 领域层存储接口的实现，实现与dao层的调用
 *
 * @author liujun
 * @version 0.0.1
 */
public class JavaCodeRepositoryPersistenceCreate implements GenerateCodeInf {

  private static final String NAME_SUFFIX = "RepositoryImpl";
  private static final String CLASS_COMMENT = "的领域存储接口实现";

  public static final JavaCodeRepositoryPersistenceCreate INSTANCE =
      new JavaCodeRepositoryPersistenceCreate();

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

      // 获取类名
      String className = this.getClassName(tableName);

      // 依赖信息导入
      this.dependencyPersistence(param, tableInfo, tableMap.size());

      // 获取以java定义的package路径
      String javaPackageStr =
          param.getJavaCodePackage().getRepositoryPersistenceNode().outJavaPackage();

      Map<String, ImportPackageInfo> packageMap = param.getPackageMap().get(tableName);

      // 定义项目内的完整目录结构
      String baseJavaPath = param.getProjectPath().getSrcJavaNode().outPath();
      javaPackageStr = baseJavaPath + Symbol.PATH + javaPackageStr;

      // 调用存储接口
      StringBuilder sb =
          GenerateJavaRepositoryPersistenceInvoke.INSTANCE.generateRepository(
              packageMap,
              param.getGenerateConfig().getGenerate().getCode(),
              param.getGenerateConfig().getGenerate().getAuthor());

      // 进行存储层的接口输出
      GenerateOutFileUtils.outJavaFile(
          sb, GeneratePathUtils.outServicePath(param), javaPackageStr, className);
    }
  }

  public void dependencyPersistence(
      GenerateCodeContext param, TableInfoDTO tableInfo, int initSize) {

    String className = this.getClassName(tableInfo.getTableName());

    // 注释
    String docComment =
        JavaCommentUtil.tableCommentProc(tableInfo.getTableComment()) + CLASS_COMMENT;

    // 获取以java定义的package路径
    String javaPackageStr =
        param.getJavaCodePackage().getRepositoryPersistenceNode().outJavaPackage();

    // 将领域的存储实现存至流程中
    ImportPackageInfo repositoryPersistPackage =
        new ImportPackageInfo(javaPackageStr, className, docComment);
    ImportPackageUtils.putPackageInfo(
        tableInfo.getTableName(),
        param.getPackageMap(),
        GenerateCodePackageKey.PERSIST_PERSISTENCE.getKey(),
        repositoryPersistPackage,
        initSize);
  }

  /**
   * 得到类名
   *
   * @param tableName 表名
   * @return 类信息
   */
  private String getClassName(String tableName) {
    return NameProcess.INSTANCE.toJavaClassName(tableName) + NAME_SUFFIX;
  }
}
