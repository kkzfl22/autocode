package com.liujun.auto.generator.builder.ddd.full.domain;

import com.liujun.auto.constant.Symbol;
import com.liujun.auto.generator.builder.ddd.code.GenerateJavaDomainEntity;
import com.liujun.auto.generator.builder.ddd.constant.GenerateCodePackageKey;
import com.liujun.auto.generator.builder.ddd.constant.JavaVarName;
import com.liujun.auto.generator.builder.ddd.constant.PkgBuildMethod;
import com.liujun.auto.generator.builder.ddd.entity.DataParam;
import com.liujun.auto.generator.builder.ddd.entity.GenerateCodeContext;
import com.liujun.auto.generator.builder.ddd.entity.ImportPackageInfo;
import com.liujun.auto.generator.builder.GenerateCodeInf;
import com.liujun.auto.generator.builder.utils.GenerateOutFileUtils;
import com.liujun.auto.generator.builder.utils.GeneratePathUtils;
import com.liujun.auto.generator.builder.utils.JavaCommentUtil;
import com.liujun.auto.generator.database.entity.TableColumnDTO;
import com.liujun.auto.generator.database.entity.TableInfoDTO;
import com.liujun.auto.generator.javalanguage.serivce.NameProcess;

import java.util.Iterator;
import java.util.List;
import java.util.Map;

/**
 * 创建领域层的实体对象
 *
 * @author liujun
 * @version 0.0.1
 * @since 2020/04/08
 */
public class JavaCodeDomainObjectCreate implements GenerateCodeInf {

  /** 注释中的描述 */
  private static final String DOC_ANNOTATION = "的领域实体信息";

  /** 领域层的后缀名 */
  private static final String DOMAIN_PO = "DO";

  public static final JavaCodeDomainObjectCreate INSTANCE = new JavaCodeDomainObjectCreate();

  @Override
  public void generateCode(GenerateCodeContext param) {

    Map<String, TableInfoDTO> tableMap = param.getTableMap();
    Map<String, List<TableColumnDTO>> map = param.getColumnMapList();
    Iterator<Map.Entry<String, List<TableColumnDTO>>> tableNameEntry = map.entrySet().iterator();

    while (tableNameEntry.hasNext()) {
      Map.Entry<String, List<TableColumnDTO>> tableEntry = tableNameEntry.next();
      // 表名
      String tableName = tableEntry.getKey();

      // 获取类名
      String className = this.getClassName(tableName);

      // 表信息
      TableInfoDTO tableInfo = tableMap.get(tableName);

      // 领域对象依赖存储信息的构建
      domainObjectPkg(param, tableInfo);

      // 构建参数信息
      DataParam generateParam =
          DataParam.builder()
              .putPkg(
                  GenerateCodePackageKey.DOMAIN_DO,
                  param.getPkg(tableName, GenerateCodePackageKey.DOMAIN_DO))
              .author(param.getGenerateConfig().getGenerate().getAuthor())
              .methodList(param.getGenerateConfig().getGenerate().getMethodList())
              .tableInfo(tableInfo)
              .databaseType(param.getTypeEnum())
              .columnList(tableEntry.getValue())
              .build();

      // 进行领域层的bean代码生成
      StringBuilder persistBean =
          GenerateJavaDomainEntity.INSTANCE.generateDomainEntityCode(generateParam, param);

      // 获取以java定义的package路径
      String javaPackageStr = param.getJavaCodePackage().getDomainObjectNode().outJavaPackage();
      // 定义项目内的完整目录结构
      String outJavaPackageStr =
          param.getProjectPath().getSrcJavaNode().outPath() + Symbol.PATH + javaPackageStr;

      // 进行领域层的实体输出
      GenerateOutFileUtils.outJavaFile(
          persistBean, GeneratePathUtils.outServicePath(param), outJavaPackageStr, className);
    }
  }

  /**
   * 领域的依赖
   *
   * @param param 参数
   * @param tableInfo 表信息
   */
  public void domainObjectPkg(GenerateCodeContext param, TableInfoDTO tableInfo) {
    // 获取以java定义的package路径
    String javaPackageStr = param.getJavaCodePackage().getDomainObjectNode().outJavaPackage();
    // 类名
    String className = JavaCodeDomainObjectCreate.INSTANCE.getClassName(tableInfo.getTableName());
    // 注释
    String docComment =
        JavaCommentUtil.tableCommentProc(tableInfo.getTableComment())
            + JavaCodeDomainObjectCreate.DOC_ANNOTATION;
    // 将当前包信息存入到上下文对象信息中
    // 构建类路径及名称记录下
    ImportPackageInfo packageInfo =
        PkgBuildMethod.classInfoVarInfo(
            javaPackageStr, className, docComment, JavaVarName.INSTANCE_NAME_ENTITY);

    // 将领域对象记录到公共的上下文对象中，领域层的实体对象
    param.putPkg(tableInfo.getTableName(), GenerateCodePackageKey.DOMAIN_DO, packageInfo);
  }

  /**
   * 得到类名
   *
   * @param tableName 表名
   * @return 当前的类名
   */
  private String getClassName(String tableName) {
    // 得到类名
    return NameProcess.INSTANCE.toJavaClassName(tableName) + DOMAIN_PO;
  }
}
