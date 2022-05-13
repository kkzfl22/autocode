package com.liujun.auto.generator.builder.ddd.code;

import com.liujun.auto.config.generate.entity.MethodInfo;
import com.liujun.auto.constant.MethodOperatorEnum;
import com.liujun.auto.constant.Symbol;
import com.liujun.auto.generator.builder.ddd.constant.GenerateCodePackageKey;
import com.liujun.auto.generator.builder.ddd.constant.ImportCodePackageKey;
import com.liujun.auto.generator.builder.ddd.constant.JavaFieldName;
import com.liujun.auto.generator.builder.ddd.constant.JavaMethodName;
import com.liujun.auto.generator.builder.ddd.constant.JavaVarName;
import com.liujun.auto.generator.builder.ddd.entity.DataParam;
import com.liujun.auto.generator.builder.ddd.entity.GenerateCodeContext;
import com.liujun.auto.generator.builder.ddd.entity.ImportPackageInfo;
import com.liujun.auto.generator.builder.ddd.entity.JavaClassFieldEntity;
import com.liujun.auto.generator.builder.ddd.entity.JavaMethodArguments;
import com.liujun.auto.generator.builder.ddd.entity.JavaMethodEntity;
import com.liujun.auto.generator.builder.utils.JavaClassCodeUtils;
import com.liujun.auto.generator.builder.utils.MethodUtils;
import com.liujun.auto.generator.builder.utils.TableColumnUtils;
import com.liujun.auto.generator.convergence.TypeConvergence;
import com.liujun.auto.generator.database.constant.DatabaseTypeEnum;
import com.liujun.auto.generator.database.entity.TableColumnDTO;
import com.liujun.auto.generator.javalanguage.constant.JavaDataType;
import com.liujun.auto.generator.javalanguage.constant.JavaKeyWord;
import com.liujun.auto.generator.javalanguage.serivce.JavaFormat;
import com.liujun.auto.generator.javalanguage.serivce.NameProcess;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Set;

/**
 * 生成领域的实体对象
 *
 * @author liujun
 * @version 0.0.1
 */
public class GenerateJavaDomainEntity {

  /** 单个添加方法 */
  private static final String ADD_PROCESS_METHOD = "单个添加方法";

  /** 批量添加方法 */
  private static final String BATCH_ADD_PROC_METHOD = "批量添加方法";

  public static final GenerateJavaDomainEntity INSTANCE = new GenerateJavaDomainEntity();

  /** 注解导入包 */
  private static final List<String> ANNOTATION_IMPORT_LIST =
      Arrays.asList(
          // get的注解包
          ImportCodePackageKey.ANNOTATION_GETTER.getPackageInfo().packageOut(),
          // set的注解包
          ImportCodePackageKey.ANNOTATION_SETTER.getPackageInfo().packageOut(),
          // toString的注解
          ImportCodePackageKey.ANNOTATION_TOSTRING.getPackageInfo().packageOut());

  /** 注解 */
  private static final List<String> ANNOTATION_LIST =
      Arrays.asList(
          // get的注解包
          ImportCodePackageKey.ANNOTATION_GETTER.getPackageInfo().getAnnotation(),
          // set的注解包
          ImportCodePackageKey.ANNOTATION_SETTER.getPackageInfo().getAnnotation(),
          // toString的注解
          ImportCodePackageKey.ANNOTATION_TOSTRING.getPackageInfo().getAnnotation());

  /**
   * 生成领域的实体对象
   *
   * @param generateParam 参数信息
   * @return 生成的javabean对象
   */
  public StringBuilder generateDomainEntityCode(
      DataParam generateParam, GenerateCodeContext param) {

    ImportPackageInfo domainObject = generateParam.getPkg(GenerateCodePackageKey.DOMAIN_DO);

    List<MethodInfo> codeMethod = param.getGenerateConfig().getGenerate().getMethodList();
    String author = param.getGenerateConfig().getGenerate().getAuthor();
    DatabaseTypeEnum typeEnum = param.getTypeEnum();

    // 类的定义
    StringBuilder sb =
        JavaClassCodeUtils.classDefine(
            domainObject, getImportList(generateParam), ANNOTATION_LIST, author);
    // 作属性输出
    this.outProperties(generateParam.getColumnList(), sb, typeEnum);
    // in关键输的输出
    this.inCondition(codeMethod, generateParam.getColumnList(), sb, typeEnum);

    // 检查是否存在添加方法
    boolean insertFlag =
        MethodUtils.checkExistsOperatorType(generateParam.getMethodList(), MethodOperatorEnum.INSERT);
    // 检查是否存在需要创建UID
    boolean primaryKeyUidFlag =
        TableColumnUtils.primaryKeyUid(generateParam.getColumnList(), generateParam.getTypeEnum());

    // 存在添加方法，并且需要使用自加序列
    if (insertFlag && primaryKeyUidFlag) {
      // 优先生成单个添加方法
      sb.append(this.addProcessMethod(generateParam));

      // 检查当前参数是否为集合
      boolean checkBatchFlag = MethodUtils.checkBatchOperator(generateParam.getMethodList());
      // 如果存在，批量添加，则生成批量集合操作
      if (checkBatchFlag) {
        sb.append(this.batchAddProcess(generateParam, param));
      }
    }

    // 类结束
    JavaClassCodeUtils.classEnd(sb);

    return sb;
  }

  /**
   * 单个添加方法
   *
   * @param generateParam
   * @return
   */
  public String addProcessMethod(DataParam generateParam) {
    StringBuilder sb = new StringBuilder();

    // 参数信息
    List<JavaMethodArguments> dataArgument =
        Arrays.asList(
            JavaMethodArguments.builder()
                .type(ImportCodePackageKey.DOMAIN_ID_GENERATE.getPackageInfo().getClassName())
                .name(ImportCodePackageKey.DOMAIN_ID_GENERATE.getPackageInfo().getVarName())
                .comment(ImportCodePackageKey.DOMAIN_ID_GENERATE.getPackageInfo().getClassComment())
                .build());

    // 方法实体信息
    JavaMethodEntity methodInfo =
        JavaMethodEntity.builder()
            // 公共的访问修饰符
            .visit(JavaKeyWord.PUBLIC)
            // 方法注释
            .comment(ADD_PROCESS_METHOD)
            // 返回值
            .type(JavaKeyWord.VOID)
            // 单个添加方法
            .name(JavaMethodName.DOMAIN_INSERT_UID)
            .arguments(dataArgument)
            .build();

    // 方法定义生成
    JavaClassCodeUtils.methodDefine(sb, methodInfo);

    // 方法开始
    JavaClassCodeUtils.methodStart(sb);

    // 生成添加操作
    sb.append(JavaFormat.appendTab(2)).append(JavaKeyWord.THIS).append(Symbol.POINT);
    sb.append(JavaFieldName.DOMAIN_ID_KEY).append(Symbol.SPACE);
    sb.append(Symbol.EQUAL).append(Symbol.SPACE);
    sb.append(ImportCodePackageKey.DOMAIN_ID_GENERATE.getPackageInfo().getVarName());
    sb.append(Symbol.POINT).append(JavaMethodName.DOMAIN_UID_NAME).append(Symbol.BRACKET_LEFT);
    sb.append(Symbol.BRACKET_RIGHT);
    sb.append(Symbol.SEMICOLON);
    sb.append(JavaFormat.appendTab(1));
    sb.append(Symbol.ENTER_LINE);
    // 方法结束
    JavaClassCodeUtils.methodEnd(sb);

    return sb.toString();
  }

  /**
   * 批量数据添加的生成id算法
   *
   * @param generateParam
   * @return
   */
  public String batchAddProcess(DataParam generateParam, GenerateCodeContext param) {
    StringBuilder sb = new StringBuilder();

    ImportPackageInfo domainObject =
        generateParam.getPkg(GenerateCodePackageKey.DOMAIN_DO);

    // 参数信息
    List<JavaMethodArguments> dataArgument =
        Arrays.asList(
            // 生成id的对象
            JavaMethodArguments.builder()
                .type(ImportCodePackageKey.DOMAIN_ID_GENERATE.getPackageInfo().getClassName())
                .name(ImportCodePackageKey.DOMAIN_ID_GENERATE.getPackageInfo().getVarName())
                .comment(ImportCodePackageKey.DOMAIN_ID_GENERATE.getPackageInfo().getClassComment())
                .build(),
            // 集合对象
            JavaMethodArguments.builder()
                .type(JavaClassCodeUtils.listType(domainObject.getClassName()))
                .name(ImportCodePackageKey.DOMAIN_BATCH_ID_LIST.getPackageInfo().getVarName())
                .comment(
                    ImportCodePackageKey.DOMAIN_BATCH_ID_LIST.getPackageInfo().getClassComment())
                .build());

    // 方法实体信息
    JavaMethodEntity methodInfo =
        JavaMethodEntity.builder()
            // 公共的访问修饰符
            .visit(JavaKeyWord.PUBLIC)
            // 静态标识
            .staticFlag(JavaKeyWord.STATIC)
            // 方法注释
            .comment(BATCH_ADD_PROC_METHOD)
            // 返回值
            .type(JavaKeyWord.VOID)
            // 批量添加方法
            .name(JavaMethodName.DOMAIN_BATCH_INSERT_UID)
            .arguments(dataArgument)
            .build();

    // 方法定义生成
    JavaClassCodeUtils.methodDefine(sb, methodInfo);

    // 方法开始
    JavaClassCodeUtils.methodStart(sb);

    // 生成添加操作
    // 1,集合判断检查
    sb.append(JavaFormat.appendTab(2));
    sb.append(JavaKeyWord.IF).append(Symbol.BRACKET_LEFT);
    sb.append(JavaKeyWord.NULL);
    sb.append(Symbol.EQUALS).append(Symbol.SPACE);
    ;
    sb.append(ImportCodePackageKey.DOMAIN_BATCH_ID_LIST.getPackageInfo().getVarName());
    sb.append(Symbol.SPACE);
    sb.append(Symbol.OR).append(Symbol.SPACE);
    sb.append(ImportCodePackageKey.DOMAIN_BATCH_ID_LIST.getPackageInfo().getVarName());
    sb.append(Symbol.POINT).append(JavaMethodName.LIST_EMPTY);
    sb.append(Symbol.BRACKET_LEFT).append(Symbol.BRACKET_RIGHT);
    sb.append(Symbol.BRACKET_RIGHT).append(Symbol.SPACE).append(Symbol.BRACE_LEFT);
    sb.append(Symbol.ENTER_LINE);

    sb.append(JavaFormat.appendTab(3));
    sb.append(JavaKeyWord.RETURN).append(Symbol.SEMICOLON);
    sb.append(Symbol.ENTER_LINE);

    sb.append(JavaFormat.appendTab(2));
    sb.append(Symbol.BRACE_RIGHT);
    sb.append(Symbol.ENTER_LINE);

    // 进行id的生成操作
    sb.append(JavaFormat.appendTab(2));
    sb.append(JavaKeyWord.FOR_KEY).append(Symbol.BRACKET_LEFT);
    sb.append(domainObject.getClassName()).append(Symbol.SPACE);
    sb.append(JavaVarName.FOREACH_TEMP_NAME).append(Symbol.SPACE);
    sb.append(Symbol.COLON).append(Symbol.SPACE);
    sb.append(ImportCodePackageKey.DOMAIN_BATCH_ID_LIST.getPackageInfo().getVarName());
    sb.append(Symbol.BRACKET_RIGHT).append(Symbol.BRACE_LEFT);
    sb.append(Symbol.ENTER_LINE);

    sb.append(JavaFormat.appendTab(3));
    sb.append(JavaVarName.FOREACH_TEMP_NAME)
        .append(Symbol.POINT)
        .append(JavaMethodName.DOMAIN_INSERT_UID);
    sb.append(Symbol.BRACKET_LEFT)
        .append(ImportCodePackageKey.DOMAIN_ID_GENERATE.getPackageInfo().getVarName());
    sb.append(Symbol.BRACKET_RIGHT).append(Symbol.SEMICOLON).append(Symbol.ENTER_LINE);

    sb.append(JavaFormat.appendTab(2));
    sb.append(Symbol.BRACE_RIGHT);
    sb.append(Symbol.ENTER_LINE);

    // 方法结束
    JavaClassCodeUtils.methodEnd(sb);

    return sb.toString();
  }

  /**
   * 获取导入的表信息
   *
   * @param generateParam 生成的对象信息
   * @return
   */
  public List<String> getImportList(DataParam generateParam) {
    List<String> importList = new ArrayList<>();
    importList.addAll(ANNOTATION_IMPORT_LIST);
    // 检查是否需要导入list包
    Set<String> inCondition = MethodUtils.getInCondition(generateParam.getMethodList());
    if (!inCondition.isEmpty()) {
      importList.add(JavaKeyWord.IMPORT_LIST);
    }

    // 添加属性的信息
    for (int i = 0; i < generateParam.getColumnList().size(); i++) {
      TableColumnDTO tableBean = generateParam.getColumnList().get(i);
      // 得到java的数据类型
      String javaDataType =
          TypeConvergence.getJavaType(generateParam.getTypeEnum(), tableBean.getDataType());

      // 获取导入的类型
      String importType = JavaDataType.getImportPkg(javaDataType);
      if (null != importType && !importList.contains(importType)) {
        importList.add(importType);
      }
    }

    // 检查是否存在需要创建UID
    if (TableColumnUtils.primaryKeyUid(
        generateParam.getColumnList(), generateParam.getTypeEnum())) {
      importList.add(ImportCodePackageKey.DOMAIN_ID_GENERATE.getPackageInfo().packageOut());
    }

    // 检查当前参数是否为集合
    boolean checkBatchFlag = MethodUtils.checkBatchOperator(generateParam.getMethodList());
    if (checkBatchFlag) {
      importList.add(JavaKeyWord.IMPORT_LIST);
    }

    // 检查当前属性，除开基础类型，其他都需要导入
    return importList;
  }

  /**
   * 进行in条件的condition的输出操作
   *
   * @param codeMethod 需要输出的方法
   * @param columnList 列信息
   * @param sb 输出的对象
   */
  private void inCondition(
      List<MethodInfo> codeMethod,
      List<TableColumnDTO> columnList,
      StringBuilder sb,
      DatabaseTypeEnum typeEnum) {
    // 进行条件的输出
    Set<String> conditionList = MethodUtils.getInCondition(codeMethod);
    // 作为属性输出
    this.outInCondition(conditionList, columnList, sb, typeEnum);
  }

  /**
   * 进行属性的输出操作
   *
   * @param inCondition 条件
   * @param columnList 列
   * @param sb 输出
   */
  private void outInCondition(
      Set<String> inCondition,
      List<TableColumnDTO> columnList,
      StringBuilder sb,
      DatabaseTypeEnum typeEnum) {
    for (String inConditionItem : inCondition) {
      TableColumnDTO tableInfo = TableColumnUtils.getColumn(columnList, inConditionItem);
      if (null == tableInfo) {
        continue;
      }

      // 得到java的数据类型
      String javaDataType = TypeConvergence.getJavaType(typeEnum, tableInfo.getDataType());
      // 得到java输出的名称
      String javaName = NameProcess.INSTANCE.toFieldName(tableInfo.getColumnName());
      // 输出的类型
      javaDataType = JavaKeyWord.LIST_TYPE + javaDataType + JavaKeyWord.LIST_TYPE_END;
      // 输出的名称
      javaName = getInConditionName(javaName);

      JavaClassFieldEntity field =
          JavaClassFieldEntity.builder()
              // 访问修饰符
              .visit(JavaKeyWord.PRIVATE)
              // 数据类型
              .type(javaDataType)
              // 名称
              .name(javaName)
              // 注释
              .comment(tableInfo.getColumnMsg())
              .build();

      sb.append(JavaClassCodeUtils.getClassField(field));
    }
  }

  /**
   * 获取in关键字作为属性的名称
   *
   * @param name 名称
   * @return 获取名称
   */
  public static String getInConditionName(String name) {
    return name + JavaKeyWord.FIELD_SUFFIX_NAME;
  }

  /**
   * 属性输出
   *
   * @param columnList 列集合
   * @param sb 输出的对象 信息
   */
  private void outProperties(
      List<TableColumnDTO> columnList, StringBuilder sb, DatabaseTypeEnum typeEnum) {
    List<JavaClassFieldEntity> javaEntityList = this.propertiesToJavaEntity(columnList, typeEnum);
    // 执行属性对象的输出操作
    for (JavaClassFieldEntity entity : javaEntityList) {
      sb.append(JavaClassCodeUtils.getClassField(entity));
    }
  }

  /**
   * 属性输出为java的实体对象
   *
   * @param columnList 列集合
   * @param typeEnum 输出的对象 信息
   * @return 生成的对象集合
   */
  public List<JavaClassFieldEntity> propertiesToJavaEntity(
      List<TableColumnDTO> columnList, DatabaseTypeEnum typeEnum) {
    List<JavaClassFieldEntity> dataList = new ArrayList<>(columnList.size());
    // 添加属性的信息
    for (int i = 0; i < columnList.size(); i++) {
      TableColumnDTO tableBean = columnList.get(i);
      // 得到java的数据类型
      String javaDataType = TypeConvergence.getJavaType(typeEnum, tableBean.getDataType());
      // 得到java输出的名称
      String javaName = NameProcess.INSTANCE.toFieldName(tableBean.getColumnName());

      JavaClassFieldEntity field =
          JavaClassFieldEntity.builder()
              // 访问修饰符
              .visit(JavaKeyWord.PRIVATE)
              // 数据类型
              .type(javaDataType)
              // 名称
              .name(javaName)
              // 注释
              .comment(tableBean.getColumnMsg())
              .build();

      dataList.add(field);
    }

    return dataList;
  }
}
