package com.liujun.auto.generator.builder.operator.ddd.full.domain;

import com.liujun.auto.constant.Symbol;
import com.liujun.auto.generator.builder.constant.GenerateCodePackageKey;
import com.liujun.auto.generator.builder.constant.JavaVarName;
import com.liujun.auto.generator.builder.constant.PkgBuildMethod;
import com.liujun.auto.generator.builder.entity.DataParam;
import com.liujun.auto.generator.builder.entity.GenerateCodeContext;
import com.liujun.auto.generator.builder.entity.ImportPackageInfo;
import com.liujun.auto.generator.builder.operator.GenerateCodeInf;
import com.liujun.auto.generator.builder.operator.base.GenerateJavaRepositoryFacadeInterface;
import com.liujun.auto.generator.builder.operator.utils.GenerateOutFileUtils;
import com.liujun.auto.generator.builder.operator.utils.GeneratePathUtils;
import com.liujun.auto.generator.builder.operator.utils.JavaCommentUtil;
import com.liujun.auto.generator.database.entity.TableColumnDTO;
import com.liujun.auto.generator.database.entity.TableInfoDTO;
import com.liujun.auto.generator.javalanguage.serivce.NameProcess;

import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

/**
 * 生成领域层的数据存储接口
 *
 * @author liujun
 * @version 1.0.0
 */
public class JavaCodeDomainRepositoryFacadeCreate implements GenerateCodeInf {

  public static final String SUFFIX_NAME = "Repository";
  private static final String COMMENT = "的领域存储接口";

  public static final JavaCodeDomainRepositoryFacadeCreate INSTANCE =
      new JavaCodeDomainRepositoryFacadeCreate();

  @Override
  public void generateCode(GenerateCodeContext param) {

    Map<String, List<TableColumnDTO>> map = param.getColumnMapList();
    Iterator<Entry<String, List<TableColumnDTO>>> tableNameEntry = map.entrySet().iterator();
    while (tableNameEntry.hasNext()) {
      Entry<String, List<TableColumnDTO>> tableNameItem = tableNameEntry.next();
      // 获取表信息
      TableInfoDTO tableInfo = param.getTableMap().get(tableNameItem.getKey());

      // 表名
      String tableName = tableNameItem.getKey();

      // 保存当前的数据至领域存储中
      this.domainRepositorySave(param, tableInfo);

      // 构建参数信息
      DataParam generateParam =
          DataParam.builder()
              .putPkg(
                  GenerateCodePackageKey.DOMAIN_DO,
                  param.getPkg(tableName, GenerateCodePackageKey.DOMAIN_DO))
              .putPkg(
                  GenerateCodePackageKey.DOMAIN_PERSIST_FACADE,
                  param.getPkg(tableName, GenerateCodePackageKey.DOMAIN_PERSIST_FACADE))
              .author(param.getGenerateConfig().getGenerate().getAuthor())
              .methodList(param.getGenerateConfig().getGenerate().getMethodList())
              .tableInfo(tableInfo)
              .databaseType(param.getTypeEnum())
              .columnList(tableNameItem.getValue())
              .build();

      // 进行领域方法的相关方法的生成
      StringBuilder sb =
          GenerateJavaRepositoryFacadeInterface.INSTANCE.generateJavaInterface(generateParam);

      // 文件输出
      this.outFile(param, tableInfo, sb);
    }
  }

  /**
   * 文件输出
   *
   * @param param
   * @param tableInfo
   * @param sb
   */
  private void outFile(GenerateCodeContext param, TableInfoDTO tableInfo, StringBuilder sb) {
    // 定义项目内的完整目录结构
    String baseJavaPath = param.getProjectPath().getSrcJavaNode().outPath();
    // 获取以java定义的package路径
    String javaPackageStr = param.getJavaCodePackage().getRepositoryFacadeNode().outJavaPackage();
    javaPackageStr = baseJavaPath + Symbol.PATH + javaPackageStr;
    String tableClassName = NameProcess.INSTANCE.toJavaClassName(tableInfo.getTableName());
    String className = tableClassName + SUFFIX_NAME;

    // 进行存储层的接口输出
    GenerateOutFileUtils.outJavaFile(
        sb, GeneratePathUtils.outServicePath(param), javaPackageStr, className);
  }

  /**
   * 领域层的服务依赖
   *
   * @param param 流程参数
   * @param tableInfo 表信息
   */
  public void domainRepositorySave(GenerateCodeContext param, TableInfoDTO tableInfo) {

    // 获取以java定义的package路径
    String javaPackageStr = param.getJavaCodePackage().getRepositoryFacadeNode().outJavaPackage();

    // 注释
    String docComment = JavaCommentUtil.tableCommentProc(tableInfo.getTableComment()) + COMMENT;

    // 得到类名
    String tableClassName = NameProcess.INSTANCE.toJavaClassName(tableInfo.getTableName());
    String className = tableClassName + SUFFIX_NAME;

    // 将领域的存储实现存至流程中
    ImportPackageInfo repositoryPersistPackage =
        PkgBuildMethod.classInfoVarInfo(
            javaPackageStr, className, docComment, JavaVarName.DOMAIN_INSTANCE_NAME);

    param.putPkg(
        tableInfo.getTableName(),
        GenerateCodePackageKey.DOMAIN_PERSIST_FACADE,
        repositoryPersistPackage);
  }
}
