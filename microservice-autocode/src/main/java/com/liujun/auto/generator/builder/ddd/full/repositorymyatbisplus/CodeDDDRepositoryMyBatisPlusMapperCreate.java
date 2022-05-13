package com.liujun.auto.generator.builder.ddd.full.repositorymyatbisplus;

import com.liujun.auto.config.generate.GenerateConfigProcess;
import com.liujun.auto.config.generate.entity.MethodInfo;
import com.liujun.auto.config.generate.entity.TypeInfo;
import com.liujun.auto.constant.GenerateDefineFlag;
import com.liujun.auto.constant.Symbol;
import com.liujun.auto.generator.builder.GenerateCodeInf;
import com.liujun.auto.generator.builder.ddd.code.GenerateCodeJavaEntity;
import com.liujun.auto.generator.builder.ddd.constant.ClassCommonCfg;
import com.liujun.auto.generator.builder.ddd.constant.CodeAnnotation;
import com.liujun.auto.generator.builder.ddd.constant.CodeComment;
import com.liujun.auto.generator.builder.ddd.constant.GenerateCodePackageKey;
import com.liujun.auto.generator.builder.ddd.constant.ImportCodePackageKey;
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
import com.liujun.auto.generator.javalanguage.entity.ContextAnnotation;
import com.liujun.auto.generator.javalanguage.entity.ContextMethodDocument;
import com.liujun.auto.generator.javalanguage.entity.ContextMethodParam;
import com.liujun.auto.generator.javalanguage.entity.ContextMethodParamDocument;
import com.liujun.auto.generator.javalanguage.entity.JavaClassDocument;
import com.liujun.auto.generator.javalanguage.entity.JavaClassImportClass;
import com.liujun.auto.generator.javalanguage.entity.JavaClassStruct;
import com.liujun.auto.generator.javalanguage.serivce.NameProcess;
import org.apache.commons.lang3.StringUtils;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 * 创建数据库访问层的Mapper的接口层
 *
 * @author liujun
 * @version 0.0.1
 * @since 2020/04/08
 */
public class CodeDDDRepositoryMyBatisPlusMapperCreate implements GenerateCodeInf {

  /** 注释中的描述 */
  private static final String DOC_ANNOTATION = "mybatis-plus的数据库操作接口";

  /** 用来生成存储层的实体名称 */
  private static final String PERSIST_MAPPER = "Mapper";

  /** 实例对象 */
  public static final CodeDDDRepositoryMyBatisPlusMapperCreate INSTANCE =
      new CodeDDDRepositoryMyBatisPlusMapperCreate();

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
      ImportPackageInfo packageInfo = repositoryMapperSaveToContext(param, tableMap.get(tableName));

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
    classStruct.setReferenceImport(this.pkgImport(tableInfo, context));

    // 空行
    classStruct.setSpaceLine(ClassCommonCfg.IMPORT_DOC_SPACE_LINE);

    ImportPackageInfo packagePo =
        context.getPkg(tableInfo.getTableName(), GenerateCodePackageKey.PERSIST_PO);
    // 类的注释
    classStruct.setClassDocument(
        JavaClassDocument.buildDoc(
            packagePo, context.getGenerateConfig().getGenerate().getAuthor()));

    // 访问修饰符
    classStruct.setClassVisit(VisitEnum.PUBLIC);

    // 类或者接口的关键字
    classStruct.setClassKeyWord(ClassKeyWordEnum.INTERFACE);

    // 类名
    classStruct.setClassName(getClassName(tableInfo.getTableName()));

    // 继承
    classStruct.setExtendClass(getBaseClass(packagePo));

    // 构建类的内部代码结构信息
    classStruct.setContent(builderContent(tableInfo, context));

    return classStruct;
  }

  private String getBaseClass(ImportPackageInfo packagePo) {
    StringBuilder baseClass = new StringBuilder();
    baseClass.append(ImportCodePackageKey.MYBATIS_PLUS_MAPPER_BASE.getPackageInfo().getClassName());
    baseClass.append(Symbol.ANGLE_BRACKETS_LEFT);
    baseClass.append(packagePo.getClassName());
    baseClass.append(Symbol.ANGLE_BRACKETS_RIGHT);
    return baseClass.toString();
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
  private List<ContentBase> builderContent(TableInfoDTO tableInfo, GenerateCodeContext context) {
    List<ContentBase> contentList = new ArrayList<>();

    // 1,输出属性
    contentList.addAll(countMethodList(tableInfo, context));

    return contentList;
  }

  /**
   * 执行所有属性的输出
   *
   * @param context 上下文信息
   * @return 属性信息
   */
  public static List<ContentBase> countMethodList(
      TableInfoDTO tableInfo, GenerateCodeContext context) {
    List<ContentBase> result = new ArrayList<>();

    for (MethodInfo methodInfo : context.getGenerateCfg().getMethodList()) {
      result.add(builderMethod(methodInfo, tableInfo, context));
    }

    return result;
  }

  /**
   * 构建属性
   *
   * @param methodInfo 方法信息
   * @return
   */
  private static ContentMethod builderMethod(
      MethodInfo methodInfo, TableInfoDTO tableInfo, GenerateCodeContext context) {
    ContentMethod method = new ContentMethod();

    ImportPackageInfo packagePo =
        context.getPkg(tableInfo.getTableName(), GenerateCodePackageKey.PERSIST_PO);

    // 注释输出
    method.setDocument(getMethodComment(methodInfo));
    // 间隔行数1
    method.setTopLine(ClassCommonCfg.FIELD_TOP_LINE);
    // 左边空格2个
    method.setLeftSpace(PrefixSpaceEnum.ONE);
    // 类型
    method.setVisit(VisitEnum.DEFAULT);
    // 得到java的数据类型
    method.setReturnClass(getResultType(methodInfo, packagePo.getClassName()));
    // 名称
    method.setName(methodInfo.getName());
    // 方法参数
    method.setParam(methodParam(methodInfo, packagePo.getClassName()));

    return method;
  }

  /**
   * 方法的返回类型处理
   *
   * @param method
   * @return
   */
  private static String getResultType(MethodInfo method, String tableEntity) {
    if (method.getOperatorType().isUpdate()) {
      return JavaKeyWord.INT_TYPE;
    }

    return GenerateDefineFlag.placeholderTableName(
        method.getReturnType().getImportClassName(), tableEntity);
  }

  /**
   * 方法参数构建
   *
   * @param method 方法信息
   * @return
   */
  private static List<ContextMethodParam> methodParam(MethodInfo method, String tableEntity) {

    List<ContextMethodParam> paramList = new ArrayList<>();
    // 参数注解
    for (TypeInfo typeInfo : method.getParamType()) {

      String importClassName =
          GenerateDefineFlag.placeholderTableName(typeInfo.getImportClassName(), tableEntity);

      paramList.add(ContextMethodParam.builderParam(importClassName, typeInfo.getVarName()));
    }

    return paramList;
  }

  /**
   * 方法注释信息
   *
   * @param methodInfo 方法描述信息
   * @return 方法信息
   */
  private static ContextMethodDocument getMethodComment(MethodInfo methodInfo) {

    List<ContextMethodParamDocument> docList = new ArrayList<>();

    // 参数注解
    for (TypeInfo typeInfo : methodInfo.getParamType()) {
      docList.add(
          ContextMethodParamDocument.builder()
              // 参数注解
              .annotation(CodeAnnotation.METHOD_PARAM)
              // 参数变量名
              .paramName(typeInfo.getVarName())
              // 参数注释
              .comment(typeInfo.getComment())
              .build());
    }

    // 响应注解
    docList.add(
        ContextMethodParamDocument.builder()
            // 参数注解
            .annotation(CodeAnnotation.METHOD_RESULT)
            // 参数注释
            .comment(getResultComment(methodInfo))
            .build());

    // 构建方法注释信息
    return ContextMethodDocument.buildMethodDoc(methodInfo.getComment(), docList);
  }

  private static String getResultComment(MethodInfo methodInfo) {
    if (methodInfo.getOperatorType().isUpdate()) {
      return CodeComment.METHOD_RESULT_UPDATE_DOC;
    } else if (methodInfo.getOperatorType().isQuery()) {
      return CodeComment.METHOD_RESULT_QUERY_DOC;
    }
    return null;
  }

  /**
   * 存储层实体mapper的依赖
   *
   * @param param 参数
   * @param tableInfo 表信息
   */
  public ImportPackageInfo repositoryMapperSaveToContext(
      GenerateCodeContext param, TableInfoDTO tableInfo) {
    // 获取以java定义的package路径
    String javaPackageStr = param.getJavaCodePackage().getRepositoryMapperNode().outJavaPackage();

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

    param.putPkg(tableInfo.getTableName(), GenerateCodePackageKey.PERSIST_MAPPER, packageInfo);

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

    return tableClassName + PERSIST_MAPPER;
  }
}
