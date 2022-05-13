package com.liujun.auto.generator.builder.ddd.full.domain;

import com.liujun.auto.config.generate.GenerateConfigProcess;
import com.liujun.auto.constant.Symbol;
import com.liujun.auto.generator.builder.GenerateCodeInf;
import com.liujun.auto.generator.builder.ddd.code.GenerateCodeJavaEntity;
import com.liujun.auto.generator.builder.ddd.constant.ClassCommonCfg;
import com.liujun.auto.generator.builder.ddd.constant.CodeAnnotation;
import com.liujun.auto.generator.builder.ddd.constant.GenerateCodePackageKey;
import com.liujun.auto.generator.builder.ddd.constant.ImportCodePackageKey;
import com.liujun.auto.generator.builder.ddd.constant.JavaMethodName;
import com.liujun.auto.generator.builder.ddd.constant.JavaVarName;
import com.liujun.auto.generator.builder.ddd.constant.PkgBuildMethod;
import com.liujun.auto.generator.builder.ddd.entity.GenerateCodeContext;
import com.liujun.auto.generator.builder.ddd.entity.ImportPackageInfo;
import com.liujun.auto.generator.builder.utils.GenerateOutFileUtils;
import com.liujun.auto.generator.builder.utils.GeneratePathUtils;
import com.liujun.auto.generator.convergence.TypeConvergence;
import com.liujun.auto.generator.database.entity.TableColumnDTO;
import com.liujun.auto.generator.database.entity.TableInfoDTO;
import com.liujun.auto.generator.javalanguage.constant.ClassKeyWordEnum;
import com.liujun.auto.generator.javalanguage.constant.JavaKeyWord;
import com.liujun.auto.generator.javalanguage.constant.JavaUseClassEnum;
import com.liujun.auto.generator.javalanguage.constant.PrefixSpaceEnum;
import com.liujun.auto.generator.javalanguage.constant.VisitEnum;
import com.liujun.auto.generator.javalanguage.entity.ContentBase;
import com.liujun.auto.generator.javalanguage.entity.ContentField;
import com.liujun.auto.generator.javalanguage.entity.ContentMethod;
import com.liujun.auto.generator.javalanguage.entity.ContextAnnotation;
import com.liujun.auto.generator.javalanguage.entity.ContextFieldDocument;
import com.liujun.auto.generator.javalanguage.entity.ContextLineCode;
import com.liujun.auto.generator.javalanguage.entity.ContextMethodParam;
import com.liujun.auto.generator.javalanguage.entity.JavaClassDocument;
import com.liujun.auto.generator.javalanguage.entity.JavaClassImportClass;
import com.liujun.auto.generator.javalanguage.entity.JavaClassStruct;
import com.liujun.auto.generator.javalanguage.serivce.NameProcess;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
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
public class CodeDDDomainObjectCreate implements GenerateCodeInf {

  /** 注释中的描述 */
  private static final String DOC_ANNOTATION = "的领域实体信息";

  /** 领域层的后缀名 */
  private static final String DOMAIN_PO = "DO";

  /** 实例对象 */
  public static final CodeDDDomainObjectCreate INSTANCE = new CodeDDDomainObjectCreate();

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
      ImportPackageInfo packageInfo = domainEntitySaveToContext(param, tableMap.get(tableName));

      // 获取表信息
      TableInfoDTO tableInfo = param.getTableMap().get(tableName);

      // 将需要生成的的代码。转化为代码结构
      JavaClassStruct classStruct = buildRepositoryStruct(tableInfo, param);

      // 执行代码的计算输出操作
      String javaClassCode = classStruct.outCode();

      // 进行存储层的实体输出
      GenerateOutFileUtils.outJavaFile(
          // 代码内容
          javaClassCode,
          // 项目的输出路径
          GeneratePathUtils.outServicePath(param),
          // 定义项目内的完整目录结构
          GeneratePathUtils.outRelativePath(param.getProjectPath(), packageInfo),
          // 文件名
          packageInfo.getClassName());
    }
  }

  /**
   * 构建存储层的实体对象
   *
   * @param tableInfo 表信息
   * @param context 上下文对象信息
   * @return 构建的存储实体对象
   */
  private JavaClassStruct buildRepositoryStruct(
      TableInfoDTO tableInfo, GenerateCodeContext context) {
    JavaClassStruct classStruct = new JavaClassStruct();
    // 最开始的版本信息
    classStruct.setCopyRight(
        GenerateConfigProcess.INSTANCE.getCfgEntity().getGenerate().getCopyRight());

    // package的定义
    classStruct.setPkgPath(context.getJavaCodePackage().getRepositoryObjectNode().outJavaPackage());

    // 导包信息
    classStruct.setReferenceImport(GenerateCodeJavaEntity.packageImpport(context));

    // 空行
    classStruct.setSpaceLine(ClassCommonCfg.IMPORT_DOC_SPACE_LINE);

    ImportPackageInfo packagePo =
        context.getPkg(tableInfo.getTableName(), GenerateCodePackageKey.DOMAIN_DO);
    // 类的注释
    classStruct.setClassDocument(
        JavaClassDocument.buildDoc(
            packagePo, context.getGenerateConfig().getGenerate().getAuthor()));

    // 类的注解信息
    classStruct.setClassAnnotation(GenerateCodeJavaEntity.classAnnotation(context));

    // 访问修饰符
    classStruct.setClassVisit(VisitEnum.PUBLIC);

    // 类或者接口的关键字
    classStruct.setClassKeyWord(ClassKeyWordEnum.CLASS);

    // 类名
    classStruct.setClassName(getClassName(tableInfo.getTableName()));

    // 构建类的内部代码结构信息
    classStruct.setContent(builderContent(tableInfo, context));

    return classStruct;
  }

  /**
   * 构建代码的内容信息
   *
   * @return
   */
  private List<ContentBase> builderContent(TableInfoDTO tableInfo, GenerateCodeContext context) {
    List<ContentBase> contentList = new ArrayList<>();

    // 1,输出属性
    contentList.addAll(GenerateCodeJavaEntity.countFieldList(tableInfo, context));

    // 输出Get方法
    contentList.addAll(GenerateCodeJavaEntity.countFieldMethodGetList(tableInfo, context));

    // 输出Set方法
    contentList.addAll(GenerateCodeJavaEntity.countFieldMethodSetList(tableInfo, context));

    // 输出toString方法
    contentList.addAll(GenerateCodeJavaEntity.countFieldMethodToString(tableInfo, context));

    return contentList;
  }

  /**
   * 存储层实体的依赖
   *
   * @param param 参数
   * @param tableInfo 表信息
   */
  public ImportPackageInfo domainEntitySaveToContext(
      GenerateCodeContext param, TableInfoDTO tableInfo) {
    // 获取以java定义的package路径
    String javaPackageStr = param.getJavaCodePackage().getDomainObjectNode().outJavaPackage();

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

    param.putPkg(tableInfo.getTableName(), GenerateCodePackageKey.DOMAIN_DO, packageInfo);

    return packageInfo;
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

    return tableClassName + DOMAIN_PO;
  }
}
