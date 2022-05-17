package com.liujun.auto.generator.builder.ddd.full.repositorymyatbisplus;

import com.liujun.auto.config.generate.GenerateConfigProcess;
import com.liujun.auto.config.generate.entity.MethodInfo;
import com.liujun.auto.config.generate.entity.TypeInfo;
import com.liujun.auto.constant.Symbol;
import com.liujun.auto.generator.builder.GenerateCodeInf;
import com.liujun.auto.generator.builder.ddd.constant.ClassCommonCfg;
import com.liujun.auto.generator.builder.ddd.constant.CodeAnnotation;
import com.liujun.auto.generator.builder.ddd.constant.CodeComment;
import com.liujun.auto.generator.builder.ddd.constant.GenerateCodePackageKey;
import com.liujun.auto.generator.builder.ddd.constant.ImportCodePackageKey;
import com.liujun.auto.generator.builder.ddd.constant.JavaMethodName;
import com.liujun.auto.generator.builder.ddd.constant.JavaVarName;
import com.liujun.auto.generator.builder.ddd.constant.PkgBuildMethod;
import com.liujun.auto.generator.builder.ddd.entity.GenerateCodeContext;
import com.liujun.auto.generator.builder.ddd.entity.ImportPackageInfo;
import com.liujun.auto.generator.builder.utils.GenerateOutFileUtils;
import com.liujun.auto.generator.builder.utils.GeneratePathUtils;
import com.liujun.auto.generator.database.entity.TableColumnDTO;
import com.liujun.auto.generator.database.entity.TableInfoDTO;
import com.liujun.auto.generator.javalanguage.constant.ClassKeyWordEnum;
import com.liujun.auto.generator.javalanguage.constant.JavaKeyWord;
import com.liujun.auto.generator.javalanguage.constant.PrefixSpaceEnum;
import com.liujun.auto.generator.javalanguage.constant.VisitEnum;
import com.liujun.auto.generator.javalanguage.entity.ContentBase;
import com.liujun.auto.generator.javalanguage.entity.ContentMethod;
import com.liujun.auto.generator.javalanguage.entity.ContextLineCode;
import com.liujun.auto.generator.javalanguage.entity.ContextMethodDocument;
import com.liujun.auto.generator.javalanguage.entity.ContextMethodParam;
import com.liujun.auto.generator.javalanguage.entity.ContextMethodParamDocument;
import com.liujun.auto.generator.javalanguage.entity.JavaClassDocument;
import com.liujun.auto.generator.javalanguage.entity.JavaClassImportClass;
import com.liujun.auto.generator.javalanguage.entity.JavaClassStruct;
import com.liujun.auto.generator.javalanguage.serivce.NameProcess;
import org.apache.commons.lang3.StringUtils;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 * 领域对象与存储层对象相互转换的方法
 *
 * @author liujun
 * @version 0.0.1
 * @since 2020/04/08
 */
public class CodeDDDRepositoryMyBatisPlusConverterCreate implements GenerateCodeInf {

  /** 注释中的描述 */
  private static final String DOC_ANNOTATION = "领域对象与存储层对象相互转化";

  /** 领域对象与实体层转换后缀名 */
  private static final String CONVERTER_NAME = "Converter";

  /** 实例对象 */
  public static final CodeDDDRepositoryMyBatisPlusConverterCreate INSTANCE =
      new CodeDDDRepositoryMyBatisPlusConverterCreate();

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
      ImportPackageInfo packageInfo =
          repositoryAssemblerSaveToContext(param, tableMap.get(tableName));

      // 获取表信息
      TableInfoDTO tableInfo = param.getTableMap().get(tableName);

      // 将需要生成的的代码。转化为代码结构
      JavaClassStruct classStruct = buildRepositoryConvertStruct(tableInfo, param);

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
  private JavaClassStruct buildRepositoryConvertStruct(
      TableInfoDTO tableInfo, GenerateCodeContext context) {
    JavaClassStruct classStruct = new JavaClassStruct();
    // 最开始的版本信息
    classStruct.setCopyRight(
        GenerateConfigProcess.INSTANCE.getCfgEntity().getGenerate().getCopyRight());

    // package的定义
    classStruct.setPkgPath(context.getJavaCodePackage().getRepositoryObjectNode().outJavaPackage());

    // 导包信息
    classStruct.setReferenceImport(this.pkgImport(tableInfo, context));

    // 空行
    classStruct.setSpaceLine(ClassCommonCfg.IMPORT_DOC_SPACE_LINE);

    ImportPackageInfo packagePo =
        context.getPkg(tableInfo.getTableName(), GenerateCodePackageKey.PERSIST_ASSEMBLER);
    // 类的注释
    classStruct.setClassDocument(
        JavaClassDocument.buildDoc(
            packagePo, context.getGenerateConfig().getGenerate().getAuthor()));

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
  private List<JavaClassImportClass> pkgImport(
      TableInfoDTO tableInfo, GenerateCodeContext context) {
    List<JavaClassImportClass> result = new ArrayList<>();

    // BaseMapper类的导入
    result.add(
        JavaClassImportClass.normalImport(
            ImportCodePackageKey.MYBATIS_PLUS_MAPPER_BASE.getPackageInfo().packageOut()));

    ImportPackageInfo packagePo =
        context.getPkg(tableInfo.getTableName(), GenerateCodePackageKey.PERSIST_PO);

    // 实体的导入
    result.add(JavaClassImportClass.normalImport(packagePo.packageOut()));

    // 检查当前是否需要导入集合
    result.addAll(getImportPkg(context.getGenerateCfg().getMethodList()));

    return result;
  }

  /**
   * 获取导包信息
   *
   * @param methodList
   * @return
   */
  private List<JavaClassImportClass> getImportPkg(List<MethodInfo> methodList) {
    Set<String> importPkgSet = new HashSet<>();

    // 找出所有方法需要导入的对象
    for (MethodInfo methodItem : methodList) {
      importPkgSet.addAll(getImportMethod(methodItem));
    }

    List<JavaClassImportClass> parseImportClass = new ArrayList<>();
    for (String methInfo : importPkgSet) {
      parseImportClass.add(JavaClassImportClass.normalImport(methInfo));
    }

    return parseImportClass;
  }

  /**
   * 获取所有需要导入的包
   *
   * @param methodItem
   * @return
   */
  private Set<String> getImportMethod(MethodInfo methodItem) {
    Set<String> importSet = new HashSet<>();

    // 1，所有的参数
    for (TypeInfo paramInfo : methodItem.getParamType()) {
      // 在加入导包前需要检查当前是否存在路径，存在路径，则才需要导入
      if (StringUtils.isNotEmpty(paramInfo.getImportPath())) {
        importSet.add(paramInfo.getImportPath());
      }
    }

    // 返回类型同样需样检查
    if (null != methodItem.getReturnType()
        && StringUtils.isNotEmpty(methodItem.getReturnType().getImportPath())) {
      // 返回类型
      importSet.add(methodItem.getReturnType().getImportPath());
    }

    return importSet;
  }

  /**
   * 构建代码的内容信息
   *
   * @return
   */
  private static List<ContentBase> builderContent(
      TableInfoDTO tableInfo, GenerateCodeContext context) {
    List<ContentBase> contentList = new ArrayList<>();

    // 1,私有化构建方法
    contentList.add(builderToConstructor(tableInfo, context));

    // 2,转换为存储实体的方法
    contentList.add(builderToPersistMethod(tableInfo, context));

    // 3,转换为领域对象的方法
    contentList.add(builderToDomainMethod(tableInfo, context));

    return contentList;
  }

  /**
   * 构建构造方法
   *
   * @param tableInfo 表信息
   * @param context 上下文参数
   * @return
   */
  private static ContentMethod builderToConstructor(
      TableInfoDTO tableInfo, GenerateCodeContext context) {
    ContentMethod method = new ContentMethod();

    ImportPackageInfo persistAssembler =
        context.getPkg(tableInfo.getTableName(), GenerateCodePackageKey.PERSIST_ASSEMBLER);

    // 间隔行数1
    method.setTopLine(ClassCommonCfg.FIELD_TOP_LINE);
    // 左边空格2个
    method.setLeftSpace(PrefixSpaceEnum.ONE);
    // 类型
    method.setVisit(VisitEnum.PRIVATE);
    // 名称
    method.setName(persistAssembler.getClassName());
    // 构建方法内容
    method.setEmptyCode(Boolean.TRUE);

    return method;
  }

  /**
   * 构建将领域对象转换为存储对象的方法
   *
   * @param tableInfo 表信息
   * @param context 上下文参数
   * @return
   */
  private static ContentMethod builderToPersistMethod(
      TableInfoDTO tableInfo, GenerateCodeContext context) {
    ContentMethod method = new ContentMethod();

    ImportPackageInfo domain =
        context.getPkg(tableInfo.getTableName(), GenerateCodePackageKey.DOMAIN_DO);

    ImportPackageInfo persist =
        context.getPkg(tableInfo.getTableName(), GenerateCodePackageKey.PERSIST_PO);

    // 注释输出
    method.setDocument(
        toPersistMethodComment(CodeComment.ASSEMBLER_ENTITY_PERSIST_COMMENT, domain, persist));
    // 间隔行数1
    method.setTopLine(ClassCommonCfg.FIELD_TOP_LINE);
    // 左边空格2个
    method.setLeftSpace(PrefixSpaceEnum.ONE);
    // 类型
    method.setVisit(VisitEnum.PUBLIC);
    // 静态方法
    method.setStaticFlag(Boolean.TRUE);
    // 得到java的数据类型
    method.setReturnClass(persist.getClassName());
    // 名称
    method.setName(JavaMethodName.ASSEMBLER_ENTITY_PERSIST_NAME);
    // 方法参数
    method.setParam(methodParam(domain));
    // 构建方法内容
    method.setCodeLine(builderToPersistContent(tableInfo, context));

    return method;
  }

  /**
   * 构建将领域对象转换为存储对象的方法
   *
   * @param tableInfo 表信息
   * @param context 上下文参数
   * @return
   */
  private static ContentMethod builderToDomainMethod(
      TableInfoDTO tableInfo, GenerateCodeContext context) {
    ContentMethod method = new ContentMethod();

    ImportPackageInfo domain =
        context.getPkg(tableInfo.getTableName(), GenerateCodePackageKey.DOMAIN_DO);

    ImportPackageInfo persist =
        context.getPkg(tableInfo.getTableName(), GenerateCodePackageKey.PERSIST_PO);

    // 注释输出
    method.setDocument(
        toPersistMethodComment(CodeComment.ASSEMBLER_ENTITY_PERSIST_COMMENT, persist, domain));
    // 间隔行数1
    method.setTopLine(ClassCommonCfg.FIELD_TOP_LINE);
    // 左边空格2个
    method.setLeftSpace(PrefixSpaceEnum.ONE);
    // 类型
    method.setVisit(VisitEnum.PUBLIC);
    // 静态方法
    method.setStaticFlag(Boolean.TRUE);
    // 得到java的数据类型
    method.setReturnClass(domain.getClassName());
    // 名称
    method.setName(JavaMethodName.ASSEMBLER_DOMAIN_ENTITY_NAME);
    // 方法参数
    method.setParam(methodParam(persist));
    // 构建方法内容
    method.setCodeLine(builderToDomainContent(tableInfo, context));

    return method;
  }

  /**
   * 构建将领域对象转换为存储对象的方法
   *
   * @return
   */
  private static List<ContextLineCode> builderToDomainContent(
      TableInfoDTO tableInfo, GenerateCodeContext context) {
    List<ContextLineCode> dataLine = new ArrayList<>(tableInfo.getColumnList().size() * 2 + 5);
    // 空对象检查
    dataLine.addAll(builderNullCheck());

    ImportPackageInfo domainPkg =
        context.getPkg(tableInfo.getTableName(), GenerateCodePackageKey.DOMAIN_DO);

    // 声明返回对象
    dataLine.add(builderResultObject(domainPkg, context));

    // 设置返回的属性
    dataLine.addAll(builderPersistObjectField(tableInfo, context));

    // 返回语句
    dataLine.add(builderPersistReturn());

    return dataLine;
  }

  /**
   * 构建将领域对象转换为存储对象的方法
   *
   * @return
   */
  private static List<ContextLineCode> builderToPersistContent(
      TableInfoDTO tableInfo, GenerateCodeContext context) {
    List<ContextLineCode> dataLine = new ArrayList<>(tableInfo.getColumnList().size() * 2 + 5);
    // 空对象检查
    dataLine.addAll(builderNullCheck());

    ImportPackageInfo persist =
        context.getPkg(tableInfo.getTableName(), GenerateCodePackageKey.PERSIST_PO);

    // 声明返回对象
    dataLine.add(builderResultObject(persist, context));

    // 设置返回的属性
    dataLine.addAll(builderPersistObjectField(tableInfo, context));

    // 返回语句
    dataLine.add(builderPersistReturn());

    return dataLine;
  }

  /**
   * 返回语句
   *
   * @return
   */
  private static ContextLineCode builderPersistReturn() {
    StringBuilder result = new StringBuilder();

    result.append(JavaKeyWord.RETURN).append(Symbol.SPACE);
    result.append(JavaVarName.ASSERT_DATA_TARGET).append(Symbol.SEMICOLON);

    return ContextLineCode.builder()
        .topLine(1)
        .leftSpace(PrefixSpaceEnum.TWO)
        .lineCode(result.toString())
        .build();
  }

  /**
   * 设置属性
   *
   * @param tableInfo
   * @param context
   * @return
   */
  private static List<ContextLineCode> builderPersistObjectField(
      TableInfoDTO tableInfo, GenerateCodeContext context) {
    List<ContextLineCode> result = new ArrayList<>(tableInfo.getColumnList().size() * 2);
    for (TableColumnDTO columnItem : tableInfo.getColumnList()) {
      // 行注释信息
      String codeLine = JavaKeyWord.LINE_ANNOTATION + columnItem.getColumnMsg();
      result.add(ContextLineCode.builderCodeUnFinish(PrefixSpaceEnum.TWO, codeLine));
      // 设置属性
      result.add(builderSetField(columnItem));
    }
    return result;
  }

  private static ContextLineCode builderSetField(TableColumnDTO columnItem) {
    StringBuilder setField = new StringBuilder();

    String fieldName = NameProcess.INSTANCE.toProJavaName(columnItem.getColumnName());

    setField.append(JavaVarName.ASSERT_DATA_TARGET).append(Symbol.POINT);
    setField.append(JavaMethodName.SET).append(fieldName);
    setField.append(Symbol.BRACKET_LEFT);
    setField.append(JavaVarName.ASSERT_DATA_SRC).append(Symbol.POINT).append(JavaMethodName.GET);
    setField.append(fieldName).append(Symbol.BRACKET_LEFT).append(Symbol.BRACKET_RIGHT);
    setField.append(Symbol.BRACKET_RIGHT);

    return ContextLineCode.builderCodeFinish(PrefixSpaceEnum.TWO, setField.toString());
  }

  /**
   * 返回对象
   *
   * @param pkg
   * @param context
   * @return
   */
  private static ContextLineCode builderResultObject(
      ImportPackageInfo pkg, GenerateCodeContext context) {

    StringBuilder codeLine = new StringBuilder();

    codeLine.append(pkg.getClassName()).append(Symbol.SPACE);
    codeLine.append(JavaVarName.ASSERT_DATA_TARGET).append(Symbol.SPACE);
    codeLine.append(Symbol.EQUAL).append(Symbol.SPACE);
    codeLine.append(JavaKeyWord.NEW).append(Symbol.SPACE);
    codeLine.append(pkg.getClassName());
    codeLine.append(Symbol.BRACKET_LEFT).append(Symbol.BRACKET_RIGHT);

    return ContextLineCode.builderCodeFinish(PrefixSpaceEnum.TWO, codeLine.toString());
  }

  private static List<ContextLineCode> builderNullCheck() {
    List<ContextLineCode> nullCheckList = new ArrayList<>(3);
    nullCheckList.add(ContextLineCode.builderCodeUnFinish(PrefixSpaceEnum.TWO, checkNullStart()));
    nullCheckList.add(checkNullReturn());
    nullCheckList.add(ContextLineCode.builderCodeUnFinish(PrefixSpaceEnum.TWO, Symbol.BRACE_RIGHT));

    return nullCheckList;
  }

  /**
   * 返回的代码
   *
   * @return
   */
  private static ContextLineCode checkNullReturn() {

    // 返回的语句
    StringBuilder codeData = new StringBuilder();
    codeData.append(JavaKeyWord.RETURN).append(Symbol.SPACE);
    codeData.append(JavaKeyWord.NULL);

    return ContextLineCode.builderCodeFinish(PrefixSpaceEnum.THIRD, codeData.toString());
  }

  private static String checkNullStart() {
    StringBuilder outCode = new StringBuilder();
    outCode.append(JavaKeyWord.IF).append(Symbol.SPACE);
    outCode.append(Symbol.BRACKET_LEFT).append(JavaKeyWord.NULL).append(Symbol.SPACE);
    outCode.append(Symbol.EQUALS).append(Symbol.SPACE).append(JavaVarName.ASSERT_DATA_SRC);
    outCode.append(Symbol.BRACKET_RIGHT).append(Symbol.SPACE).append(Symbol.BRACE_LEFT);

    return outCode.toString();
  }

  /**
   * 方法参数构建
   *
   * @param pkgInfo 包信息
   * @return
   */
  private static List<ContextMethodParam> methodParam(ImportPackageInfo pkgInfo) {

    List<ContextMethodParam> paramList = new ArrayList<>();
    // 参数注解
    paramList.add(
        ContextMethodParam.builderParam(pkgInfo.getClassName(), JavaVarName.ASSERT_DATA_SRC));

    return paramList;
  }

  /**
   * 将领域对象转换为存储对象的方法注释信息
   *
   * @param methodDoc 方法注释
   * @param src 领域对象
   * @param target 存储对象
   * @return 方法信息
   */
  private static ContextMethodDocument toPersistMethodComment(
      String methodDoc, ImportPackageInfo src, ImportPackageInfo target) {

    List<ContextMethodParamDocument> docList = new ArrayList<>();

    docList.add(
        ContextMethodParamDocument.builder()
            // 参数注解
            .annotation(CodeAnnotation.METHOD_PARAM)
            // 参数变量名
            .paramName(JavaVarName.ASSERT_DATA_SRC)
            // 参数注释
            .comment(src.getClassComment())
            .build());

    // 响应注解
    docList.add(
        ContextMethodParamDocument.builder()
            // 参数注解
            .annotation(CodeAnnotation.METHOD_RESULT)
            // 参数注释
            .comment(target.getClassComment())
            .build());

    // 构建方法注释信息
    return ContextMethodDocument.buildMethodDoc(methodDoc, docList);
  }

  /**
   * 存储层实体assembler的依赖
   *
   * @param param 参数
   * @param tableInfo 表信息
   */
  public ImportPackageInfo repositoryAssemblerSaveToContext(
      GenerateCodeContext param, TableInfoDTO tableInfo) {
    // 获取以java定义的package路径
    String javaPackageStr =
        param.getJavaCodePackage().getRepositoryAssemblerNode().outJavaPackage();

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

    param.putPkg(tableInfo.getTableName(), GenerateCodePackageKey.PERSIST_ASSEMBLER, packageInfo);

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

    return tableClassName + CONVERTER_NAME;
  }
}
