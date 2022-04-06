package com.liujun.micro.autocode.generator.builder.operator.ddd.full.repositorymybatis;

import com.liujun.micro.autocode.constant.Symbol;
import com.liujun.micro.autocode.generator.builder.constant.GenerateCodePackageKey;
import com.liujun.micro.autocode.generator.builder.constant.JavaVarName;
import com.liujun.micro.autocode.generator.builder.constant.PkgBuildMethod;
import com.liujun.micro.autocode.generator.builder.entity.DataParam;
import com.liujun.micro.autocode.generator.builder.entity.GenerateCodeContext;
import com.liujun.micro.autocode.generator.builder.entity.ImportPackageInfo;
import com.liujun.micro.autocode.generator.builder.operator.GenerateCodeInf;
import com.liujun.micro.autocode.generator.builder.operator.code.GenerateJavaBean;
import com.liujun.micro.autocode.generator.builder.operator.utils.GenerateOutFileUtils;
import com.liujun.micro.autocode.generator.builder.operator.utils.GeneratePathUtils;
import com.liujun.micro.autocode.generator.database.entity.TableColumnDTO;
import com.liujun.micro.autocode.generator.database.entity.TableInfoDTO;
import com.liujun.micro.autocode.generator.javalanguage.serivce.NameProcess;

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
public class JavaCodeRepositoryMyBatisObjectCreate implements GenerateCodeInf {

  /** 注释中的描述 */
  private static final String DOC_ANNOTATION = "的数据库存储实体信息";

  /** 用来生成存储层的实体名称 */
  public static final String REPOSITORY_PO = "PO";

  /** 实例对象 */
  public static final JavaCodeRepositoryMyBatisObjectCreate INSTANCE =
      new JavaCodeRepositoryMyBatisObjectCreate();

  @Override
  public void generateCode(GenerateCodeContext param) {

    Map<String, TableInfoDTO> tableMap = param.getTableMap();
    Map<String, List<TableColumnDTO>> map = param.getColumnMapList();
    Iterator<Map.Entry<String, List<TableColumnDTO>>> tableNameEntry = map.entrySet().iterator();

    while (tableNameEntry.hasNext()) {
      Map.Entry<String, List<TableColumnDTO>> tableEntry = tableNameEntry.next();
      // 表名
      String tableName = tableEntry.getKey();

      // 存储层实体
      repositoryObjectDependency(param, tableMap.get(tableName));

      // 将po记录到公共的上下文对象中
      ImportPackageInfo packageInfo = param.getPkg(tableName, GenerateCodePackageKey.PERSIST_PO);

      // 获取表信息
      TableInfoDTO tableInfo = param.getTableMap().get(tableName);

      // 构建参数信息
      DataParam generateParam =
          DataParam.builder()
              .putPkg(
                  GenerateCodePackageKey.PERSIST_PO,
                  param.getPkg(tableName, GenerateCodePackageKey.PERSIST_PO))
              .author(param.getGenerateConfig().getGenerate().getAuthor())
              .methodList(param.getGenerateConfig().getGenerate().getMethodList())
              .tableInfo(tableInfo)
              .databaseType(param.getTypeEnum())
              .columnList(tableEntry.getValue())
              .build();

      // 进行存储层的bean代码生成
      StringBuilder persistBean = GenerateJavaBean.INSTANCE.generateJavaBean(generateParam);

      // 定义项目内的完整目录结构
      String outPackagePath =
          param.getProjectPath().getSrcJavaNode().outPath()
              + Symbol.PATH
              + packageInfo.getPackagePath();

      // 进行存储层的实体输出
      GenerateOutFileUtils.outJavaFile(
          persistBean,
          GeneratePathUtils.outServicePath(param),
          outPackagePath,
          packageInfo.getClassName());
    }
  }

  /**
   * 存储层实体的依赖
   *
   * @param param 参数
   * @param tableInfo 表信息
   */
  public void repositoryObjectDependency(GenerateCodeContext param, TableInfoDTO tableInfo) {
    // 获取以java定义的package路径
    String javaPackageStr = param.getJavaCodePackage().getRepositoryObjectNode().outJavaPackage();

    // 注释
    String docComment =
        tableInfo.getTableComment()
            + Symbol.BRACKET_LEFT
            + tableInfo.getTableName()
            + Symbol.BRACKET_RIGHT
            + DOC_ANNOTATION;

    // 将当前包信息存入到上下文对象信息中
    // 构建类路径及名称记录下
    ImportPackageInfo packageInfo =
        PkgBuildMethod.classInfoVarInfo(
            javaPackageStr,
            this.getClassName(tableInfo.getTableName()),
            docComment,
            JavaVarName.INSTANCE_NAME_ENTITY);

    param.putPkg(tableInfo.getTableName(), GenerateCodePackageKey.PERSIST_PO, packageInfo);
  }

  /**
   * 获取类名
   *
   * @param tableName 表名
   * @return 返回当前类的名称
   */
  public String getClassName(String tableName) {
    // 得到类名
    String tableClassName = NameProcess.INSTANCE.toJavaClassName(tableName);

    return tableClassName + REPOSITORY_PO;
  }
}
