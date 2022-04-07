package com.liujun.auto.generator.builder.operator.ddd.full.domain;

import com.liujun.auto.constant.Symbol;
import com.liujun.auto.generator.builder.constant.GenerateCodePackageKey;
import com.liujun.auto.generator.builder.constant.JavaVarName;
import com.liujun.auto.generator.builder.constant.PkgBuildMethod;
import com.liujun.auto.generator.builder.entity.DataParam;
import com.liujun.auto.generator.builder.entity.GenerateCodeContext;
import com.liujun.auto.generator.builder.entity.ImportPackageInfo;
import com.liujun.auto.generator.builder.operator.GenerateCodeInf;
import com.liujun.auto.generator.builder.operator.base.GenerateJavaDomainServiceInvoke;
import com.liujun.auto.generator.builder.operator.utils.GenerateOutFileUtils;
import com.liujun.auto.generator.builder.operator.utils.GeneratePathUtils;
import com.liujun.auto.generator.builder.operator.utils.JavaCommentUtil;
import com.liujun.auto.generator.database.entity.TableColumnDTO;
import com.liujun.auto.generator.database.entity.TableInfoDTO;
import com.liujun.auto.generator.javalanguage.serivce.NameProcess;

import java.util.Iterator;
import java.util.List;
import java.util.Map;

/**
 * 领域层服务的实现，主要是调用领域的存储接口
 *
 * @author liujun
 * @version 0.0.1
 */
public class JavaCodeDomainServiceCreate implements GenerateCodeInf {

  private static final String NAME_SUFFIX = "DomainService";
  private static final String CLASS_COMMENT = "的领域服务";

  public static final JavaCodeDomainServiceCreate INSTANCE = new JavaCodeDomainServiceCreate();

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

      // 基础的依赖生成
      this.domainServiceDependency(param, tableInfo);

      // 构建参数信息
      DataParam generateParam =
          DataParam.builder()
              .putPkg(
                  GenerateCodePackageKey.DOMAIN_DO,
                  param.getPkg(tableName, GenerateCodePackageKey.DOMAIN_DO))
              .putPkg(
                  GenerateCodePackageKey.DOMAIN_SERVICE,
                  param.getPkg(tableName, GenerateCodePackageKey.DOMAIN_SERVICE))
              .putPkg(
                  GenerateCodePackageKey.DOMAIN_PERSIST_FACADE,
                  param.getPkg(tableName, GenerateCodePackageKey.DOMAIN_PERSIST_FACADE))
              .author(param.getGenerateConfig().getGenerate().getAuthor())
              .methodList(param.getGenerateConfig().getGenerate().getMethodList())
              .tableInfo(tableInfo)
              .databaseType(param.getTypeEnum())
              .columnList(tableNameItem.getValue())
              .build();

      // 代码生成操作
      StringBuilder sb =
          GenerateJavaDomainServiceInvoke.INSTANCE.generateDomainService(generateParam);

      // 获取以java定义的package路径
      String javaPackageStr = param.getJavaCodePackage().getDomainServiceNode().outJavaPackage();
      // 定义项目内的完整目录结构
      String baseJavaPath = param.getProjectPath().getSrcJavaNode().outPath();
      javaPackageStr = baseJavaPath + Symbol.PATH + javaPackageStr;

      // 进行存储层的接口输出
      GenerateOutFileUtils.outJavaFile(
          sb,
          GeneratePathUtils.outServicePath(param),
          javaPackageStr,
          this.getClassName(tableName));
    }
  }

  /**
   * 得到类名
   *
   * @param tableName
   * @return
   */
  private String getClassName(String tableName) {
    return NameProcess.INSTANCE.toJavaClassName(tableName) + NAME_SUFFIX;
  }

  /**
   * 领域层的服务依赖
   *
   * @param param 流程参数
   * @param tableInfo 表信息
   */
  public void domainServiceDependency(GenerateCodeContext param, TableInfoDTO tableInfo) {
    // 注释
    String docComment =
        JavaCommentUtil.tableCommentProc(tableInfo.getTableComment()) + CLASS_COMMENT;

    // 获取以java定义的package路径
    String javaPackageStr = param.getJavaCodePackage().getDomainServiceNode().outJavaPackage();
    String className = this.getClassName(tableInfo.getTableName());

    // 将领域的存储实现存至流程中
    ImportPackageInfo repositoryPersistPackage =
        PkgBuildMethod.classInfoVarInfo(
            javaPackageStr, className, docComment, JavaVarName.DOMAIN_INSTANCE_NAME);

    param.putPkg(
        tableInfo.getTableName(), GenerateCodePackageKey.DOMAIN_SERVICE, repositoryPersistPackage);
  }
}
