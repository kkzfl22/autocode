package com.liujun.micro.autocode.generator.builder.operator.ddd.full.application;

import com.liujun.micro.autocode.constant.Symbol;
import com.liujun.micro.autocode.generator.builder.constant.GenerateCodePackageKey;
import com.liujun.micro.autocode.generator.builder.constant.JavaVarName;
import com.liujun.micro.autocode.generator.builder.constant.PkgBuildMethod;
import com.liujun.micro.autocode.generator.builder.entity.DataParam;
import com.liujun.micro.autocode.generator.builder.entity.GenerateCodeContext;
import com.liujun.micro.autocode.generator.builder.entity.ImportPackageInfo;
import com.liujun.micro.autocode.generator.builder.operator.GenerateCodeInf;
import com.liujun.micro.autocode.generator.builder.operator.base.GenerateJavaApplicationServiceInvoke;
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
 * 聚合领域层服务的实现，主要是调用领域服务
 *
 * @author liujun
 * @version 0.0.1
 */
public class JavaCodeApplicationServiceCreate implements GenerateCodeInf {

  private static final String NAME_SUFFIX = "ApplicationService";
  private static final String CLASS_COMMENT = "的应用服务";

  public static final JavaCodeApplicationServiceCreate INSTANCE =
      new JavaCodeApplicationServiceCreate();

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
      String className = this.getClassName(tableName);

      // 进行应用的依赖
      this.applicationDependency(param, tableInfo);

      // 获取以java定义的package路径
      String javaPackageStr =
          param.getJavaCodePackage().getApplicationServiceNode().outJavaPackage();
      // 定义项目内的完整目录结构
      String baseJavaPath = param.getProjectPath().getSrcJavaNode().outPath();
      javaPackageStr = baseJavaPath + Symbol.PATH + javaPackageStr;

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
                  GenerateCodePackageKey.APPLICATION_SERVICE,
                  param.getPkg(tableName, GenerateCodePackageKey.APPLICATION_SERVICE))
              .author(param.getGenerateConfig().getGenerate().getAuthor())
              .methodList(param.getGenerateConfig().getGenerate().getMethodList())
              .tableInfo(tableInfo)
              .databaseType(param.getTypeEnum())
              .columnList(tableNameItem.getValue())
              .build();

      // 方法生成操作
      StringBuilder sb =
          GenerateJavaApplicationServiceInvoke.INSTANCE.generateApplicationService(generateParam);

      // 进行存储层的接口输出
      GenerateOutFileUtils.outJavaFile(
          sb, GeneratePathUtils.outServicePath(param), javaPackageStr, className);
    }
  }

  /**
   * * 获取类名
   *
   * @param tableName 表名
   * @return 类名
   */
  public String getClassName(String tableName) {
    // 得到类名
    String tableClassName = NameProcess.INSTANCE.toJavaClassName(tableName);
    return tableClassName + NAME_SUFFIX;
  }

  /**
   * 应用层的依赖
   *
   * @param param 参数
   * @param tableInfo 表信息
   */
  public void applicationDependency(GenerateCodeContext param, TableInfoDTO tableInfo) {

    String javaPackageStr = param.getJavaCodePackage().getApplicationServiceNode().outJavaPackage();

    // 类名
    String className =
        JavaCodeApplicationServiceCreate.INSTANCE.getClassName(tableInfo.getTableName());

    String docComment =
        JavaCommentUtil.tableCommentProc(tableInfo.getTableComment()) + CLASS_COMMENT;

    // 将领域的存储实现存至流程中
    ImportPackageInfo applicationServicePackage =
        PkgBuildMethod.classInfoVarInfo(
            javaPackageStr, className, docComment, JavaVarName.APPLICATION_INSTANCE_NAME);

    // 放入上下文对象中
    param.putPkg(
        tableInfo.getTableName(),
        GenerateCodePackageKey.APPLICATION_SERVICE,
        applicationServicePackage);
  }
}
