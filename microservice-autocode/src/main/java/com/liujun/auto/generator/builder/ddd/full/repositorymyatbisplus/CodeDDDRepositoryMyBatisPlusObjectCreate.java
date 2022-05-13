package com.liujun.auto.generator.builder.ddd.full.repositorymyatbisplus;

import com.liujun.auto.config.generate.GenerateConfigProcess;
import com.liujun.auto.constant.Symbol;
import com.liujun.auto.generator.builder.GenerateCodeInf;
import com.liujun.auto.generator.builder.ddd.code.GenerateCodeJavaEntity;
import com.liujun.auto.generator.builder.ddd.constant.ClassCommonCfg;
import com.liujun.auto.generator.builder.ddd.constant.CodeAnnotation;
import com.liujun.auto.generator.builder.ddd.constant.GenerateCodePackageKey;
import com.liujun.auto.generator.builder.ddd.constant.ImportCodePackageKey;
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
import com.liujun.auto.generator.javalanguage.constant.PrefixSpaceEnum;
import com.liujun.auto.generator.javalanguage.constant.VisitEnum;
import com.liujun.auto.generator.javalanguage.entity.ContentBase;
import com.liujun.auto.generator.javalanguage.entity.ContentField;
import com.liujun.auto.generator.javalanguage.entity.ContextAnnotation;
import com.liujun.auto.generator.javalanguage.entity.ContextFieldDocument;
import com.liujun.auto.generator.javalanguage.entity.JavaClassDocument;
import com.liujun.auto.generator.javalanguage.entity.JavaClassImportClass;
import com.liujun.auto.generator.javalanguage.entity.JavaClassStruct;
import com.liujun.auto.generator.javalanguage.serivce.NameProcess;

import java.util.ArrayList;
import java.util.Arrays;
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
public class CodeDDDRepositoryMyBatisPlusObjectCreate implements GenerateCodeInf {

  /** 注释中的描述 */
  private static final String DOC_ANNOTATION = "mybatis-plus的数据库存储实体信息";

  /** 用来生成存储层的实体名称 */
  private static final String PERSIST_PO = "PO";

  /** 实例对象 */
  public static final CodeDDDRepositoryMyBatisPlusObjectCreate INSTANCE =
      new CodeDDDRepositoryMyBatisPlusObjectCreate();

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
      ImportPackageInfo packageInfo = repositorySaveToContext(param, tableMap.get(tableName));

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
    classStruct.setReferenceImport(this.pkgImport(context));

    // 空行
    classStruct.setSpaceLine(ClassCommonCfg.IMPORT_DOC_SPACE_LINE);

    ImportPackageInfo packagePo =
        context.getPkg(tableInfo.getTableName(), GenerateCodePackageKey.PERSIST_PO);
    // 类的注释
    classStruct.setClassDocument(
        JavaClassDocument.buildDoc(
            packagePo, context.getGenerateConfig().getGenerate().getAuthor()));

    // 类的注解信息
    classStruct.setClassAnnotation(this.classAnnotationList(tableInfo, context));

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
   * 导包
   *
   * @param context
   * @return
   */
  private List<JavaClassImportClass> pkgImport(GenerateCodeContext context) {
    List<JavaClassImportClass> result = GenerateCodeJavaEntity.packageImpport(context);

    // 表类定义的导入
    result.add(
        JavaClassImportClass.normalImport(
            ImportCodePackageKey.MYBATIS_PLUS_TABLE_NAME.getPackageInfo().packageOut()));

    // 字段名
    result.add(
        JavaClassImportClass.normalImport(
            ImportCodePackageKey.MYBATIS_PLUS_TABLE_FIELD.getPackageInfo().packageOut()));

    return result;
  }

  /**
   * 类注解信息
   *
   * @param tableInfo
   * @param context
   * @return
   */
  private List<ContextAnnotation> classAnnotationList(
      TableInfoDTO tableInfo, GenerateCodeContext context) {
    List<ContextAnnotation> annotationList = GenerateCodeJavaEntity.classAnnotation(context);

    // 添加plus的实体注解
    annotationList.add(annotationTable(tableInfo));

    return annotationList;
  }

  /**
   * 注解表名
   *
   * @param tableInfo 表信息
   * @return 表名输出
   */
  private ContextAnnotation annotationTable(TableInfoDTO tableInfo) {

    ContextAnnotation annotation =
        ContextAnnotation.builder()
            // 注解名
            .annotation(
                ImportCodePackageKey.MYBATIS_PLUS_TABLE_NAME.getPackageInfo().getAnnotation())
            // 注解表名
            .annotationValue(tableInfo.getTableName())
            // 构建
            .build();

    return annotation;
  }

  /**
   * 构建代码的内容信息
   *
   * @return
   */
  private List<ContentBase> builderContent(TableInfoDTO tableInfo, GenerateCodeContext context) {
    List<ContentBase> contentList = new ArrayList<>();

    // 1,输出属性
    contentList.addAll(countFieldList(tableInfo, context));

    // 输出Get方法
    contentList.addAll(GenerateCodeJavaEntity.countFieldMethodGetList(tableInfo, context));

    // 输出Set方法
    contentList.addAll(GenerateCodeJavaEntity.countFieldMethodSetList(tableInfo, context));

    // 输出toString方法
    contentList.addAll(GenerateCodeJavaEntity.countFieldMethodToString(tableInfo, context));

    return contentList;
  }

  /**
   * 执行所有属性的输出
   *
   * @param tableInfo 表信息
   * @return 属性信息
   */
  public static List<ContentBase> countFieldList(
      TableInfoDTO tableInfo, GenerateCodeContext context) {
    List<ContentBase> result = new ArrayList<>();

    for (TableColumnDTO tableColumn : tableInfo.getColumnList()) {
      result.add(builderField(tableColumn, context));
    }

    return result;
  }

  /**
   * 构建属性
   *
   * @param tableColumn
   * @param context
   * @return
   */
  private static ContentField builderField(
      TableColumnDTO tableColumn, GenerateCodeContext context) {
    ContentField field = new ContentField();

    // 得到java输出的名称
    String javaName = NameProcess.INSTANCE.toFieldName(tableColumn.getColumnName());

    field.setDocument(ContextFieldDocument.buildFieldDoc(tableColumn.getColumnMsg()));
    // 间隔行数1
    field.setTopLine(ClassCommonCfg.FIELD_TOP_LINE);
    // 左边空格2个
    field.setLeftSpace(PrefixSpaceEnum.ONE);
    // 注解
    field.setAnnotation(Arrays.asList(annotation(tableColumn)));
    // 访问修饰符私有
    field.setVisit(VisitEnum.PRIVATE);
    // 类型
    // 得到java的数据类型
    String javaDataType =
        TypeConvergence.getJavaType(context.getTypeEnum(), tableColumn.getDataType());
    field.setClassType(javaDataType);
    // 名称
    field.setName(javaName);

    return field;
  }

  /**
   * 添加注解操作
   *
   * @param tableBean
   * @return
   */
  private static ContextAnnotation annotation(TableColumnDTO tableBean) {
    ContextAnnotation annotation =
        ContextAnnotation.builder()
            // 注解名
            .annotation(
                ImportCodePackageKey.MYBATIS_PLUS_TABLE_FIELD.getPackageInfo().getAnnotation())
            // 注解中的值
            .annotationValue(CodeAnnotation.SWAGGER_API_VALUE, tableBean.getColumnName())

            // 构建
            .build();

    return annotation;
  }

  /**
   * 存储层实体的依赖
   *
   * @param param 参数
   * @param tableInfo 表信息
   */
  public ImportPackageInfo repositorySaveToContext(
      GenerateCodeContext param, TableInfoDTO tableInfo) {
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

    return tableClassName + PERSIST_PO;
  }
}
