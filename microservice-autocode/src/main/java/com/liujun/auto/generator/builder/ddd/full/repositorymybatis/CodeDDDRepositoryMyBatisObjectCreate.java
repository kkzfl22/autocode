package com.liujun.auto.generator.builder.ddd.full.repositorymybatis;

import com.liujun.auto.config.generate.GenerateConfigProcess;
import com.liujun.auto.constant.Symbol;
import com.liujun.auto.generator.builder.GenerateCodeInf;
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
import com.liujun.auto.generator.javalanguage.entity.ContentField;
import com.liujun.auto.generator.javalanguage.entity.ContentMethod;
import com.liujun.auto.generator.javalanguage.entity.ContextAnnotation;
import com.liujun.auto.generator.javalanguage.entity.ContentBase;
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
public class CodeDDDRepositoryMyBatisObjectCreate implements GenerateCodeInf {

  /** 注释中的描述 */
  private static final String DOC_ANNOTATION = "的数据库存储实体信息";

  /** 用来生成存储层的实体名称 */
  public static final String REPOSITORY_PO = "PO";

  /** 实例对象 */
  public static final CodeDDDRepositoryMyBatisObjectCreate INSTANCE =
      new CodeDDDRepositoryMyBatisObjectCreate();

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
      JavaClassStruct classStruct = buildRepositoryObject(tableInfo, param);

      // 执行代码的计算输出操作
      String javaClassCode = classStruct.outCode();

      // 定义项目内的完整目录结构
      String outPackagePath =
          param.getProjectPath().getSrcJavaNode().outPath()
              + Symbol.PATH
              + packageInfo.getPackagePath();

      // 进行存储层的实体输出
      GenerateOutFileUtils.outJavaFile(
          javaClassCode,
          GeneratePathUtils.outServicePath(param),
          outPackagePath,
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
  private JavaClassStruct buildRepositoryObject(
      TableInfoDTO tableInfo, GenerateCodeContext context) {
    JavaClassStruct classStruct = new JavaClassStruct();
    // 最开始的版本信息
    classStruct.setCopyRight(
        GenerateConfigProcess.INSTANCE.getCfgEntity().getGenerate().getCopyRight());

    // package的定义
    classStruct.setPkgPath(context.getJavaCodePackage().getRepositoryObjectNode().outJavaPackage());

    // 导包信息
    classStruct.setReferenceImport(this.packageImpport(context));

    // 空行
    classStruct.setSpaceLine(ClassCommonCfg.IMPORT_DOC_SPACE_LINE);

    ImportPackageInfo packagePo =
        context.getPkg(tableInfo.getTableName(), GenerateCodePackageKey.PERSIST_PO);
    // 类的注释
    classStruct.setClassDocument(
        JavaClassDocument.buildDoc(
            packagePo, context.getGenerateConfig().getGenerate().getAuthor()));

    // 类的注解信息
    classStruct.setClassAnnotation(classAnnotation(context));

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
    contentList.addAll(countFieldList(tableInfo, context));

    // 输出Get方法
    contentList.addAll(countFieldMethodGetList(tableInfo, context));

    // 输出Set方法
    contentList.addAll(countFieldMethodSetList(tableInfo, context));

    // 输出toString方法
    contentList.addAll(countFieldMethodToString(tableInfo, context));

    return contentList;
  }

  /**
   * 执行所有属性的输出
   *
   * @param tableInfo 表信息
   * @return 属性信息
   */
  private List<ContentBase> countFieldMethodToString(
      TableInfoDTO tableInfo, GenerateCodeContext context) {

    // 如果关联，则不再做当前的输出操作
    if (context.lombokOpen()) {
      return Collections.emptyList();
    }

    ContentMethod method = new ContentMethod();

    // 得到java输出的名称
    String methodName = JavaMethodName.TO_STRING;

    // 间隔行数1
    method.setTopLine(ClassCommonCfg.FIELD_TOP_LINE);
    // 左边空格2个
    method.setLeftSpace(PrefixSpaceEnum.ONE);
    // 访问修饰符私有
    method.setVisit(VisitEnum.PUBLIC);
    // 类型信息
    method.setReturnClass(JavaKeyWord.VOID);
    // 名称
    method.setName(methodName);
    // 方法注解
    method.setAnnotation(ContextAnnotation.annotation(CodeAnnotation.OVERRIDE));
    // 代码首行信息
    method.setCodeLine(builderToStringContent(tableInfo));

    return Arrays.asList(method);
  }

  private List<ContextLineCode> builderToStringContent(TableInfoDTO tableInfo) {
    List<ContextLineCode> result = new ArrayList<>(tableInfo.getColumnList().size() + 3);

    // 首行信息加入
    result.add(builderToStringFirstCodeLine(tableInfo));
    // 中间行信息加入
    for (int i = 0; i < tableInfo.getColumnList().size(); i++) {
      TableColumnDTO column = tableInfo.getColumnList().get(i);
      boolean last = false;
      if (i == tableInfo.getColumnList().size() - 1) {
        last = true;
      }
      result.add(builderToStringCodeLine(column, last));
    }
    // 末尾行的引号
    result.add(builderToStringLastEndCodeLine());
    result.add(builderToStringLastParseCodeLine());

    return result;
  }

  /**
   * 构建code操作
   *
   * @param tableInfo 表信息
   * @return
   */
  private ContextLineCode builderToStringFirstCodeLine(TableInfoDTO tableInfo) {
    // 得到java输出的名称
    String className = NameProcess.INSTANCE.toJavaClassName(tableInfo.getTableName());

    StringBuilder outCode = new StringBuilder();
    outCode.append(JavaKeyWord.FINAL);
    outCode.append(Symbol.SPACE);
    outCode.append(JavaUseClassEnum.STRING_BUILDER.getName());
    outCode.append(Symbol.SPACE);
    outCode.append(JavaVarName.TO_STRING_OUT_NAME);
    outCode.append(Symbol.SPACE);
    outCode.append(Symbol.EQUAL);
    outCode.append(Symbol.SPACE);
    outCode.append(JavaKeyWord.NEW);
    outCode.append(Symbol.SPACE);
    outCode.append(JavaUseClassEnum.STRING_BUILDER.getName());
    outCode.append(Symbol.BRACKET_LEFT);
    outCode.append(Symbol.QUOTE);
    outCode.append(className);
    outCode.append(Symbol.BRACE_LEFT);
    outCode.append(Symbol.QUOTE);
    outCode.append(Symbol.BRACKET_RIGHT);

    return ContextLineCode.builderCode(outCode.toString());
  }

  /**
   * 构建code操作
   *
   * @param columnName
   * @return
   */
  private ContextLineCode builderToStringCodeLine(TableColumnDTO columnName, boolean last) {

    // 得到java输出的名称
    String fieldName = NameProcess.INSTANCE.toFieldName(columnName.getColumnName());

    StringBuilder outCode = new StringBuilder();
    outCode.append(JavaVarName.TO_STRING_OUT_NAME);
    outCode.append(Symbol.POINT);
    outCode.append(JavaMethodName.APPEND);
    outCode.append(Symbol.BRACKET_LEFT);
    outCode.append(Symbol.QUOTE);
    outCode.append(fieldName);
    outCode.append(Symbol.EQUAL);
    outCode.append(Symbol.SINGLE_QUOTE);
    outCode.append(Symbol.QUOTE);
    outCode.append(Symbol.BRACKET_RIGHT);

    outCode.append(Symbol.POINT);
    outCode.append(JavaMethodName.APPEND);
    outCode.append(Symbol.BRACKET_LEFT);
    outCode.append(fieldName);
    outCode.append(Symbol.BRACKET_RIGHT);

    outCode.append(Symbol.POINT);
    outCode.append(JavaMethodName.APPEND);
    outCode.append(Symbol.BRACKET_LEFT);
    outCode.append(Symbol.QUOTE);
    outCode.append(Symbol.SINGLE_QUOTE);
    outCode.append(Symbol.QUOTE);
    outCode.append(Symbol.BRACKET_RIGHT);

    // 末尾的引号
    if (!last) {
      outCode.append(Symbol.POINT);
      outCode.append(JavaMethodName.APPEND);
      outCode.append(Symbol.BRACKET_LEFT);
      outCode.append(Symbol.QUOTE);
      outCode.append(Symbol.COMMA);
      outCode.append(Symbol.QUOTE);
      outCode.append(Symbol.BRACKET_RIGHT);
    }

    return ContextLineCode.builderCode(outCode.toString());
  }

  /**
   * 构建最后一行的ToString方法操作
   *
   * @return
   */
  private ContextLineCode builderToStringLastEndCodeLine() {

    StringBuilder outCode = new StringBuilder();
    outCode.append(JavaVarName.TO_STRING_OUT_NAME);

    outCode.append(Symbol.POINT);
    outCode.append(JavaMethodName.APPEND);
    outCode.append(Symbol.BRACKET_LEFT);
    outCode.append(Symbol.QUOTE);
    outCode.append(Symbol.BRACE_RIGHT);
    outCode.append(Symbol.QUOTE);
    outCode.append(Symbol.BRACKET_RIGHT);

    return ContextLineCode.builderCode(outCode.toString());
  }
  /**
   * 构建最后一行的ToString方法操作
   *
   * @return
   */
  private ContextLineCode builderToStringLastParseCodeLine() {

    StringBuilder outCode = new StringBuilder();

    outCode.append(JavaKeyWord.RETURN);
    outCode.append(Symbol.SPACE);
    outCode.append(JavaVarName.TO_STRING_OUT_NAME);
    outCode.append(Symbol.POINT);
    outCode.append(JavaMethodName.TO_STRING);
    outCode.append(Symbol.BRACKET_LEFT);
    outCode.append(Symbol.BRACKET_RIGHT);

    return ContextLineCode.builderCode(outCode.toString());
  }

  /**
   * 执行所有属性的输出
   *
   * @param tableInfo 表信息
   * @return 属性信息
   */
  private List<ContentBase> countFieldMethodGetList(
      TableInfoDTO tableInfo, GenerateCodeContext context) {

    // 如果关联，则不再做当前的输出操作
    if (context.lombokOpen()) {
      return Collections.emptyList();
    }

    List<ContentBase> result = new ArrayList<>();
    for (TableColumnDTO tableColumn : tableInfo.getColumnList()) {
      result.add(builderMethodGetField(tableColumn, context));
    }

    return result;
  }

  /**
   * 做方法输出的构建操作
   *
   * @param tableColumn
   * @param context
   * @return
   */
  private ContentMethod builderMethodGetField(
      TableColumnDTO tableColumn, GenerateCodeContext context) {
    ContentMethod method = new ContentMethod();

    // 得到java输出的名称
    String methodName =
        JavaMethodName.GET + NameProcess.INSTANCE.toJavaClassName(tableColumn.getColumnName());

    // 间隔行数1
    method.setTopLine(ClassCommonCfg.FIELD_TOP_LINE);
    // 左边空格2个
    method.setLeftSpace(PrefixSpaceEnum.ONE);
    // 访问修饰符私有
    method.setVisit(VisitEnum.PUBLIC);
    // 类型
    String javaDataType =
        TypeConvergence.getJavaType(context.getTypeEnum(), tableColumn.getDataType());
    method.setReturnClass(javaDataType);
    // 名称
    method.setName(methodName);
    // 代码行信息
    method.setCodeLine(builderMethodGetCodeLine(tableColumn.getColumnName()));

    return method;
  }

  /**
   * 构建code操作
   *
   * @param columnName
   * @return
   */
  private List<ContextLineCode> builderMethodGetCodeLine(String columnName) {
    // 得到java输出的名称
    String javaName = NameProcess.INSTANCE.toFieldName(columnName);

    StringBuilder outCode = new StringBuilder();
    outCode.append(JavaKeyWord.RETURN);
    outCode.append(Symbol.SPACE);
    outCode.append(JavaKeyWord.THIS);
    outCode.append(Symbol.POINT);
    outCode.append(javaName);

    ContextLineCode line = new ContextLineCode();

    line.setLeftSpace(PrefixSpaceEnum.TWO);
    line.setLineCode(outCode.toString());

    return Arrays.asList(line);
  }

  /**
   * 执行Set方法的输出操作
   *
   * @param tableInfo 表信息
   * @return 属性信息
   */
  private List<ContentBase> countFieldMethodSetList(
      TableInfoDTO tableInfo, GenerateCodeContext context) {

    // 如果关联，则不再做当前的输出操作
    if (context.lombokOpen()) {
      return Collections.emptyList();
    }

    List<ContentBase> result = new ArrayList<>();
    for (TableColumnDTO tableColumn : tableInfo.getColumnList()) {
      result.add(builderMethodSetField(tableColumn, context));
    }

    return result;
  }

  /**
   * 做方法输出的构建操作
   *
   * @param tableColumn
   * @param context
   * @return
   */
  private ContentMethod builderMethodSetField(
      TableColumnDTO tableColumn, GenerateCodeContext context) {
    ContentMethod method = new ContentMethod();

    // 得到java输出的名称
    String methodName =
        JavaMethodName.SET + NameProcess.INSTANCE.toJavaClassName(tableColumn.getColumnName());

    // 类型
    String javaDataType =
        TypeConvergence.getJavaType(context.getTypeEnum(), tableColumn.getDataType());

    // 参数名称
    String referenceName = NameProcess.INSTANCE.toFieldName(tableColumn.getColumnName());

    // 间隔行数1
    method.setTopLine(ClassCommonCfg.FIELD_TOP_LINE);
    // 左边空格2个
    method.setLeftSpace(PrefixSpaceEnum.ONE);
    // 访问修饰符私有
    method.setVisit(VisitEnum.PUBLIC);

    method.setReturnClass(JavaKeyWord.VOID);
    // 名称
    method.setName(methodName);
    // 方法参数
    method.setParam(Arrays.asList(ContextMethodParam.builderParam(javaDataType, referenceName)));
    // 代码信息
    method.setCodeLine(builderMethodSetCodeLine(tableColumn.getColumnName(), referenceName));

    return method;
  }

  /**
   * 构建code操作
   *
   * @param columnName
   * @return
   */
  private List<ContextLineCode> builderMethodSetCodeLine(String columnName, String referenceName) {
    // 得到java输出的名称
    String javaName = NameProcess.INSTANCE.toFieldName(columnName);

    StringBuilder outCode = new StringBuilder();
    outCode.append(JavaKeyWord.THIS);
    outCode.append(Symbol.POINT);
    outCode.append(javaName);
    outCode.append(Symbol.SPACE);
    outCode.append(Symbol.EQUAL);
    outCode.append(Symbol.SPACE);
    outCode.append(referenceName);

    ContextLineCode line = new ContextLineCode();

    line.setLeftSpace(PrefixSpaceEnum.TWO);
    line.setLineCode(outCode.toString());

    return Arrays.asList(line);
  }

  /**
   * 执行所有属性的输出
   *
   * @param tableInfo 表信息
   * @return 属性信息
   */
  private List<ContentBase> countFieldList(TableInfoDTO tableInfo, GenerateCodeContext context) {
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
  private ContentField builderField(TableColumnDTO tableColumn, GenerateCodeContext context) {
    ContentField field = new ContentField();

    // 得到java输出的名称
    String javaName = NameProcess.INSTANCE.toFieldName(tableColumn.getColumnName());

    field.setDocument(ContextFieldDocument.buildFieldDoc(tableColumn.getColumnMsg()));
    // 间隔行数1
    field.setTopLine(ClassCommonCfg.FIELD_TOP_LINE);
    // 左边空格2个
    field.setLeftSpace(PrefixSpaceEnum.ONE);
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
   * 类注释信息
   *
   * @param context
   * @return
   */
  private List<ContextAnnotation> classAnnotation(GenerateCodeContext context) {
    List<ContextAnnotation> annotationList = new ArrayList<>();
    // 检查是否启用loombok插伯来生成相关的get和set及toString方法
    if (context.lombokOpen()) {
      annotationList.add(
          ContextAnnotation.builder()
              .annotation(ImportCodePackageKey.ANNOTATION_SETTER.getPackageInfo().getAnnotation())
              .build());
      annotationList.add(
          ContextAnnotation.builder()
              .annotation(ImportCodePackageKey.ANNOTATION_GETTER.getPackageInfo().getAnnotation())
              .build());
      annotationList.add(
          ContextAnnotation.builder()
              .annotation(ImportCodePackageKey.ANNOTATION_TOSTRING.getPackageInfo().getAnnotation())
              .build());
    }

    return annotationList;
  }

  /**
   * 导包信息
   *
   * @param context
   * @return
   */
  private List<JavaClassImportClass> packageImpport(GenerateCodeContext context) {
    List<JavaClassImportClass> importPkgList = new ArrayList<>();
    // 检查是否启用loombok插伯来生成相关的get和set及toString方法
    if (null != context.getGenerateCfg().getLombok() && context.getGenerateCfg().getLombok()) {
      importPkgList.add(
          JavaClassImportClass.normalImport(
              ImportCodePackageKey.ANNOTATION_SETTER.getPackageInfo().packageOut()));
      importPkgList.add(
          JavaClassImportClass.normalImport(
              ImportCodePackageKey.ANNOTATION_GETTER.getPackageInfo().packageOut()));
      importPkgList.add(
          JavaClassImportClass.normalImport(
              ImportCodePackageKey.ANNOTATION_TOSTRING.getPackageInfo().packageOut()));
    }

    return importPkgList;
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

    return tableClassName + REPOSITORY_PO;
  }
}
