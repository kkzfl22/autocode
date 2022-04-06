package com.liujun.micro.autocode.generator.builder.operator.ddd.full.repositorymybatis;

import com.liujun.micro.autocode.constant.Symbol;
import com.liujun.micro.autocode.generator.builder.constant.GenerateCodePackageKey;
import com.liujun.micro.autocode.generator.builder.constant.PkgBuildMethod;
import com.liujun.micro.autocode.generator.builder.entity.DataParam;
import com.liujun.micro.autocode.generator.builder.entity.GenerateCodeContext;
import com.liujun.micro.autocode.generator.builder.entity.ImportPackageInfo;
import com.liujun.micro.autocode.generator.builder.operator.GenerateCodeInf;
import com.liujun.micro.autocode.generator.builder.operator.base.GenerateJavaRepositoryPersistenceInvoke;
import com.liujun.micro.autocode.generator.builder.operator.utils.GenerateOutFileUtils;
import com.liujun.micro.autocode.generator.builder.operator.utils.GeneratePathUtils;
import com.liujun.micro.autocode.generator.builder.operator.utils.JavaCommentUtil;
import com.liujun.micro.autocode.generator.database.entity.TableColumnDTO;
import com.liujun.micro.autocode.generator.database.entity.TableInfoDTO;
import com.liujun.micro.autocode.generator.javalanguage.serivce.NameProcess;

import java.util.Iterator;
import java.util.List;
import java.util.Map;

/**
 * 领域层存储接口的实现，实现与dao层的调用
 *
 * @author liujun
 * @version 0.0.1
 */
public class JavaCodeRepositoryMyBatisImplementCreate implements GenerateCodeInf {

  private static final String NAME_SUFFIX = "RepositoryImpl";
  private static final String CLASS_COMMENT = "的领域存储接口实现";

  public static final JavaCodeRepositoryMyBatisImplementCreate INSTANCE =
      new JavaCodeRepositoryMyBatisImplementCreate();

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
      this.dependencyPersistence(param, tableInfo);

      // 获取以java定义的package路径
      String javaPackageStr =
          param.getJavaCodePackage().getRepositoryPersistenceNode().outJavaPackage();

      // 构建参数信息
      DataParam generateParam =
          DataParam.builder()
              .putPkg(
                  GenerateCodePackageKey.PERSIST_PO,
                  param.getPkg(tableName, GenerateCodePackageKey.PERSIST_PO))
              .putPkg(
                  GenerateCodePackageKey.DOMAIN_DO,
                  param.getPkg(tableName, GenerateCodePackageKey.DOMAIN_DO))
              .putPkg(
                  GenerateCodePackageKey.PERSIST_DAO,
                  param.getPkg(tableName, GenerateCodePackageKey.PERSIST_DAO))
              .putPkg(
                  GenerateCodePackageKey.PERSIST_ASSEMBLER,
                  param.getPkg(tableName, GenerateCodePackageKey.PERSIST_ASSEMBLER))
              .putPkg(
                  GenerateCodePackageKey.DOMAIN_PERSIST_FACADE,
                  param.getPkg(tableName, GenerateCodePackageKey.DOMAIN_PERSIST_FACADE))
              .putPkg(
                  GenerateCodePackageKey.PERSIST_PERSISTENCE,
                  param.getPkg(tableName, GenerateCodePackageKey.PERSIST_PERSISTENCE))
              .author(param.getGenerateConfig().getGenerate().getAuthor())
              .methodList(param.getGenerateConfig().getGenerate().getMethodList())
              .tableInfo(tableInfo)
              .databaseType(param.getTypeEnum())
              .columnList(tableNameItem.getValue())
              .build();

      // 调用存储接口
      StringBuilder sb =
          GenerateJavaRepositoryPersistenceInvoke.INSTANCE.generateRepository(generateParam);

      // 定义项目内的完整目录结构
      String baseJavaPath = param.getProjectPath().getSrcJavaNode().outPath();
      javaPackageStr = baseJavaPath + Symbol.PATH + javaPackageStr;

      // 进行存储层的接口输出
      GenerateOutFileUtils.outJavaFile(
          sb, GeneratePathUtils.outServicePath(param), javaPackageStr, className);
    }
  }

  public void dependencyPersistence(GenerateCodeContext param, TableInfoDTO tableInfo) {

    String className = this.getClassName(tableInfo.getTableName());

    // 注释
    String docComment =
        JavaCommentUtil.tableCommentProc(tableInfo.getTableComment()) + CLASS_COMMENT;

    // 获取以java定义的package路径
    String javaPackageStr =
        param.getJavaCodePackage().getRepositoryPersistenceNode().outJavaPackage();

    // 将领域的存储实现存至流程中
    ImportPackageInfo repositoryPersistPackage =
        PkgBuildMethod.classInfoComment(javaPackageStr, className, docComment);

    param.putPkg(
        tableInfo.getTableName(),
        GenerateCodePackageKey.PERSIST_PERSISTENCE,
        repositoryPersistPackage);
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
