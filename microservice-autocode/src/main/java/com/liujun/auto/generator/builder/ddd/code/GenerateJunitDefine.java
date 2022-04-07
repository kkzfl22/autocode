package com.liujun.auto.generator.builder.ddd.code;

import com.liujun.auto.config.generate.entity.MethodInfo;
import com.liujun.auto.constant.Symbol;
import com.liujun.auto.generator.builder.ddd.constant.CodeComment;
import com.liujun.auto.generator.builder.ddd.constant.ImportCodeJavaPackageKey;
import com.liujun.auto.generator.builder.ddd.constant.ImportCodePackageKey;
import com.liujun.auto.generator.builder.ddd.constant.ImportJunitPkgKey;
import com.liujun.auto.generator.builder.ddd.constant.JavaMethodName;
import com.liujun.auto.generator.builder.ddd.constant.JavaVarName;
import com.liujun.auto.generator.builder.ddd.constant.JavaVarValue;
import com.liujun.auto.generator.builder.ddd.constant.JunitKey;
import com.liujun.auto.generator.builder.ddd.entity.ImportPackageInfo;
import com.liujun.auto.generator.builder.ddd.entity.JavaAnnotation;
import com.liujun.auto.generator.builder.ddd.entity.JavaClassEntity;
import com.liujun.auto.generator.builder.ddd.entity.JavaClassFieldEntity;
import com.liujun.auto.generator.builder.ddd.entity.JavaMethodArguments;
import com.liujun.auto.generator.builder.ddd.entity.JavaMethodEntity;
import com.liujun.auto.generator.builder.utils.JavaClassCodeUtils;
import com.liujun.auto.generator.builder.utils.MethodUtils;
import com.liujun.auto.generator.builder.utils.ReturnUtils;
import com.liujun.auto.generator.database.constant.DatabaseTypeEnum;
import com.liujun.auto.generator.database.entity.TableColumnDTO;
import com.liujun.auto.generator.database.service.DatabaseValue;
import com.liujun.auto.generator.javalanguage.constant.JavaKeyWord;
import com.liujun.auto.generator.javalanguage.serivce.JavaFormat;
import com.liujun.auto.generator.javalanguage.serivce.NameProcess;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

/**
 * @author liujun
 * @version 0.0.1
 */
public class GenerateJunitDefine {

  /** 测试的标识 */
  public static final String TEST_SUFFIX_NAME = "Test";

  /** 需要导入的包 */
  private static final String[] IMPORT_PKG =
      new String[] {
        "org.junit.After",
        "org.junit.Assert",
        "org.junit.Before",
        "org.junit.Test",
        "org.junit.runner.RunWith",
        "org.apache.commons.lang3.RandomStringUtils",
        "org.apache.commons.lang3.RandomUtils",
        "org.springframework.beans.factory.annotation.Autowired",
        "com.alibaba.druid.spring.boot.autoconfigure.DruidDataSourceAutoConfigure",
        "org.mybatis.spring.boot.autoconfigure.MybatisAutoConfiguration",
        ImportJunitPkgKey.SPRING_BOOT_TEST_IMPORT.getPackageInfo().packageOut(),
        ImportJunitPkgKey.SPRING_RUNNER.getPackageInfo().packageOut(),
        ImportJunitPkgKey.SPRING_BOOT_TEST.getPackageInfo().packageOut(),
        ImportJunitPkgKey.SPRING_BOOT_TEST_TRANSACTIONAL.getPackageInfo().packageOut(),
        ImportJunitPkgKey.SPRING_BOOT_TEST_PROPERTY_SOURCE.getPackageInfo().packageOut(),
        JavaKeyWord.IMPORT_LIST,
        JavaKeyWord.IMPORT_ARRAYLIST,
        ImportCodeJavaPackageKey.MAP.getPackageInfo().packageOut(),
        "com.common.constant.InsertType",
        "java.util.HashMap",
        "java.math.BigDecimal",
        "java.time.LocalDate",
        "java.time.LocalDateTime",
        "java.time.LocalTime",
        ImportJunitPkgKey.SPRING_BOOT_TEST_DATETIME_UTILS.getPackageInfo().packageOut()
      };

  /** 测试方法的前缀 */
  public static final String JUNIT_METHOD_BEFORE = "test";

  /** 注解的后缀 */
  private static final String ANNOTATION_VALUE_SUFFIX = ".class";

  /** 最小依赖的类属性 */
  private static final String SPRING_BOOT_TEST_CLASSES = "classes";

  /** 事务注解 */
  private static final String SPRING_BOOT_TEST_TRANSACTIONAL_MANAGER = "transactionManager";

  /** 导入包的设置 */
  private static final String SPRING_BOOT_TEST_IMPORT_VALUE = "value";

  /** 测试的资源文件 */
  private static final String TEST_CONFIG_FILE = "classpath:application.yml";

  /** 进行构建操作 */
  public static final GenerateJunitDefine INSTANCE = new GenerateJunitDefine();

  /**
   * 文件头定义
   *
   * @param poPackage 数据库实体
   * @param methodList 方法集
   * @param dependencyClass 单元测试最小的依赖的文件列表
   * @param junitPackage 单元测试的包路径
   * @param author 作者
   * @return 输出文件的定义信息
   */
  public StringBuilder defineHead(
      ImportPackageInfo poPackage,
      ImportPackageInfo junitPackage,
      List<MethodInfo> methodList,
      List<String> dependencyClass,
      ImportPackageInfo mybatisScanConfig,
      List<ImportPackageInfo> importAnnotationPkgList,
      String author) {
    return defineHead(
        poPackage,
        junitPackage,
        methodList,
        dependencyClass,
        mybatisScanConfig,
        importAnnotationPkgList,
        Collections.emptyList(),
        author);
  }

  /**
   * 文件头定义
   *
   * @param poPackage 数据库实体
   * @param methodList 方法集
   * @param dependencyClass 单元测试最小的依赖的文件列表
   * @param junitPackage 单元测试的包路径
   * @param mybatisScanConfig mybatis的找包路径
   * @param importAnnotationPkgList 注解导入信息
   * @param importPkgList 自定义导入包信息
   * @param author 作者
   * @return 输出文件的定义信息
   */
  public StringBuilder defineHead(
      ImportPackageInfo poPackage,
      ImportPackageInfo junitPackage,
      List<MethodInfo> methodList,
      List<String> dependencyClass,
      ImportPackageInfo mybatisScanConfig,
      List<ImportPackageInfo> importAnnotationPkgList,
      List<String> importPkgList,
      String author) {

    JavaClassEntity classEntityInfo =
        JavaClassEntity.builder()
            // 类的关键字
            .classKey(JavaKeyWord.CLASS_KEY)
            // 类名
            .className(junitPackage.getClassName())
            // 类注释
            .classComment(junitPackage.getClassComment())
            // 包类路径信息
            .packagePath(junitPackage.getPackagePath())
            // 导入包信息
            .importList(
                getImportList(
                    poPackage,
                    methodList,
                    mybatisScanConfig,
                    importAnnotationPkgList,
                    importPkgList))
            // 作者
            .author(author)
            // 加入注解
            .annotationList(junitClassAnnotation(dependencyClass, importAnnotationPkgList))
            .build();

    // 生成接口的定义
    return JavaClassCodeUtils.javaClassDefine(classEntityInfo);
  }

  /**
   * 获取导包信息
   *
   * @param poPackage 实体包信息
   * @param methodList 方法集合
   * @param mybatisScanConfig 扫描定制定
   * @param importAnnotationPkgList 注解导入的包
   * @param importPkgLists 手动导入包信息
   * @return
   */
  private List<String> getImportList(
      ImportPackageInfo poPackage,
      List<MethodInfo> methodList,
      ImportPackageInfo mybatisScanConfig,
      List<ImportPackageInfo> importAnnotationPkgList,
      List<String> importPkgLists) {
    List<String> importList = new ArrayList<>();

    for (String importPackage : IMPORT_PKG) {
      importList.add(importPackage);
    }
    // mybatis的扫描包
    importList.add(mybatisScanConfig.packageOut());

    if (null != importAnnotationPkgList && !importAnnotationPkgList.isEmpty()) {
      // 添加必须依赖的包
      for (ImportPackageInfo importPackage : importAnnotationPkgList) {
        if (null == importPackage) {
          continue;
        }
        importList.add(importPackage.packageOut());
      }
    }
    // 导入po包
    importList.add(poPackage.packageOut());
    // 如果存在分页方法，则需要导入分页查询的包
    boolean queryFlag = MethodUtils.checkPageQuery(methodList);
    if (queryFlag) {
      importList.add(ImportCodePackageKey.PAGE_PARAM.getPackageInfo().packageOut());
    }

    // 其他额外的包导入
    if (null != importPkgLists && !importPkgLists.isEmpty()) {
      for (String importPkg : importPkgLists) {
        importList.add(importPkg);
      }
    }

    return importList;
  }

  /**
   * 获取类的注解
   *
   * @return
   */
  private List<String> junitClassAnnotation(
      List<String> dependencyClass, List<ImportPackageInfo> importPkgList) {
    List<String> annotationList = new ArrayList<>();

    // @RunWith(SpringRunner.class)
    String runWithAnnotation =
        JavaAnnotation.builder()
            .annotation(ImportJunitPkgKey.SPRING_RUNNER.getPackageInfo().getAnnotation())
            .annotationValue(
                true,
                ImportJunitPkgKey.SPRING_RUNNER.getPackageInfo().getClassName()
                    + ANNOTATION_VALUE_SUFFIX)
            .build()
            .outAnnotation();
    annotationList.add(runWithAnnotation);

    // @SpringBootTest(classes = {DruidDataSourceAutoConfigure.class,
    // MybatisAutoConfiguration.class, MyBatisScanConfiguration.class})
    String springBootTestAnnotation =
        JavaAnnotation.builder()
            .annotation(ImportJunitPkgKey.SPRING_BOOT_TEST.getPackageInfo().getAnnotation())
            .annotationValue(true, SPRING_BOOT_TEST_CLASSES, runDependencyClass(dependencyClass))
            .build()
            .outAnnotation();
    annotationList.add(springBootTestAnnotation);

    // @Transactional(transactionManager = "walletTransactionManager")
    String transactionalTestAnnotation =
        JavaAnnotation.builder()
            .annotation(
                ImportJunitPkgKey.SPRING_BOOT_TEST_TRANSACTIONAL.getPackageInfo().getAnnotation())
            .annotationValue(
                SPRING_BOOT_TEST_TRANSACTIONAL_MANAGER, JavaVarName.JUNIT_TRANSACTION_MANAGER_NAME)
            .build()
            .outAnnotation();
    annotationList.add(transactionalTestAnnotation);

    // 添加@Import(value = {ParaRoleInfoRepositoryImpl.class, ParaRoleInfoDomainService.class})
    if (null != importPkgList && !importPkgList.isEmpty()) {
      String importAnnotation =
          JavaAnnotation.builder()
              .annotation(
                  ImportJunitPkgKey.SPRING_BOOT_TEST_IMPORT.getPackageInfo().getAnnotation())
              .annotationValue(true, SPRING_BOOT_TEST_IMPORT_VALUE, runImportClass(importPkgList))
              .build()
              .outAnnotation();
      annotationList.add(importAnnotation);
    }

    // 注解加载测试文件
    String configFileTestAnnotation =
        JavaAnnotation.builder()
            .annotation(
                ImportJunitPkgKey.SPRING_BOOT_TEST_PROPERTY_SOURCE.getPackageInfo().getAnnotation())
            .annotationValue("", TEST_CONFIG_FILE)
            .build()
            .outAnnotation();
    annotationList.add(configFileTestAnnotation);

    return annotationList;
  }

  /**
   * 导入依赖的文件
   *
   * @param importPkgList
   * @return
   */
  private String runImportClass(List<ImportPackageInfo> importPkgList) {
    StringBuilder outDependencyClass = new StringBuilder();

    outDependencyClass.append(Symbol.BRACE_LEFT);
    for (ImportPackageInfo dependencyClassItem : importPkgList) {
      if (null == dependencyClassItem) {
        continue;
      }

      outDependencyClass.append(dependencyClassItem.getClassName()).append(ANNOTATION_VALUE_SUFFIX);
      outDependencyClass.append(Symbol.COMMA);
      outDependencyClass.append(Symbol.SPACE);
    }
    // 去掉最后两位的字符
    outDependencyClass.deleteCharAt(outDependencyClass.length() - 1);
    outDependencyClass.deleteCharAt(outDependencyClass.length() - 1);
    outDependencyClass.append(Symbol.BRACE_RIGHT);

    return outDependencyClass.toString();
  }

  private String runDependencyClass(List<String> dependencyClass) {
    StringBuilder outDependencyClass = new StringBuilder();

    outDependencyClass.append(Symbol.BRACE_LEFT);
    for (String dependencyClassItem : dependencyClass) {
      outDependencyClass.append(dependencyClassItem).append(ANNOTATION_VALUE_SUFFIX);
      outDependencyClass.append(Symbol.COMMA);
      outDependencyClass.append(Symbol.SPACE);
    }
    // 去掉最后两位的字符
    outDependencyClass.deleteCharAt(outDependencyClass.length() - 1);
    outDependencyClass.deleteCharAt(outDependencyClass.length() - 1);
    outDependencyClass.append(Symbol.BRACE_RIGHT);

    return outDependencyClass.toString();
  }

  /**
   * 单元测试的前置方法
   *
   * @param sb
   * @param poPackage 导入的包
   * @param daoPackage 数据库的实体包
   * @param typeEnum 类型信息
   * @param columnList 列信息
   * @param primaryList 主键列
   */
  public void beforeMethod(
      StringBuilder sb,
      ImportPackageInfo poPackage,
      ImportPackageInfo daoPackage,
      DatabaseTypeEnum typeEnum,
      List<TableColumnDTO> columnList,
      List<TableColumnDTO> primaryList) {

    // bore的设置操作
    junitBefore(sb, daoPackage, poPackage);
    // 1,调用设置主键属性
    setPrimaryFieldMethod(sb, poPackage, typeEnum, primaryList);
    // 2,调用设置其他属性
    setDataFieldMethod(sb, poPackage, typeEnum, columnList, primaryList);
  }

  /**
   * 主键属性的设置操作
   *
   * @param sb
   * @param poPackage 导入的包
   * @param typeEnum 类型信息
   * @param primaryList 主键列
   */
  private void setPrimaryFieldMethod(
      StringBuilder sb,
      ImportPackageInfo poPackage,
      DatabaseTypeEnum typeEnum,
      List<TableColumnDTO> primaryList) {

    // 方法实体信息
    JavaMethodEntity methodInfo =
        JavaMethodEntity.builder()
            // 公共的访问修饰符
            .visit(JavaKeyWord.PRIVATE)
            // 方法注释
            .comment(CodeComment.SET_PRIMARY_FIELD_COMMENT)
            // 返回值
            .type(JavaKeyWord.VOID)
            // 方法名
            .name(JavaMethodName.SET_PRIMARY_FIELD)
            // 参数
            .arguments(
                Arrays.asList(
                    JavaMethodArguments.builder()
                        .type(poPackage.getClassName())
                        .name(JavaVarName.PARAM_BEAN)
                        .comment(CodeComment.METHOD_PARAM_DOC)
                        .build()))
            .build();

    // 方法定义生成
    JavaClassCodeUtils.methodDefine(sb, methodInfo);

    // 方法开始
    JavaClassCodeUtils.methodStart(sb);

    // 属性的值设置
    this.setProperties(sb, primaryList, typeEnum);

    // 方法结束
    JavaClassCodeUtils.methodEnd(sb);
  }

  /**
   * 设置属性的数据
   *
   * @param sb
   * @param poPackage 导入的包
   * @param typeEnum 类型信息
   * @param primaryList 主键列
   */
  private void setDataFieldMethod(
      StringBuilder sb,
      ImportPackageInfo poPackage,
      DatabaseTypeEnum typeEnum,
      List<TableColumnDTO> columnList,
      List<TableColumnDTO> primaryList) {

    // 1,通过数据需在排除掉集合数据
    List<TableColumnDTO> dataList = new ArrayList<>(columnList);
    // 移除主键列
    dataList.removeAll(primaryList);

    // 方法实体信息
    JavaMethodEntity methodInfo =
        JavaMethodEntity.builder()
            // 公共的访问修饰符
            .visit(JavaKeyWord.PRIVATE)
            // 方法注释
            .comment(CodeComment.SET_DATA_FIELD_COMMENT)
            // 返回值
            .type(JavaKeyWord.VOID)
            // 方法名
            .name(JavaMethodName.SET_DATA_FIELD)
            // 参数
            .arguments(
                Arrays.asList(
                    JavaMethodArguments.builder()
                        .type(poPackage.getClassName())
                        .name(JavaVarName.PARAM_BEAN)
                        .comment(CodeComment.METHOD_PARAM_DOC)
                        .build()))
            .build();

    // 方法定义生成
    JavaClassCodeUtils.methodDefine(sb, methodInfo);

    // 方法开始
    JavaClassCodeUtils.methodStart(sb);

    // 属性的值设置
    this.setProperties(sb, dataList, typeEnum);

    // 方法结束
    JavaClassCodeUtils.methodEnd(sb);
  }

  /**
   * 单元测试之前运行的数据
   *
   * @param sb 字符信息
   * @param daoPackage 数据库操作的文件
   * @param poPackage 实体的包
   */
  private void junitBefore(
      StringBuilder sb, ImportPackageInfo daoPackage, ImportPackageInfo poPackage) {
    int tabIndex = 0;

    // 类属性的定义
    this.classFieldDefine(sb, poPackage, daoPackage);

    // 调用数据准备方法
    this.junitBeforeMethod(sb, tabIndex);

    // 数据生成方法
    this.junitGetData(sb, tabIndex, poPackage);
  }

  /**
   * 类属性的定义
   *
   * @param sb
   * @param entityPackage 实体的类信息
   * @param targetPackage 目标调用包信息
   */
  private void classFieldDefine(
      StringBuilder sb, ImportPackageInfo entityPackage, ImportPackageInfo targetPackage) {

    // 静态常量
    // 声明批量添加属性
    JavaClassFieldEntity field =
        JavaClassFieldEntity.builder()
            // 访问修饰符
            .visit(JavaKeyWord.PRIVATE)
            // 静态标识
            .staticFlag(JavaKeyWord.STATIC)
            // final标识
            .finalFlag(JavaKeyWord.FINAL)
            // 类型
            .type(JavaKeyWord.INT_TYPE)
            // 名称
            .name(JavaVarName.FINAL_BATCH_INSERT_NUM)
            // 注释
            .comment(CodeComment.FINAL_BATCH_INSERT_NUM_COMMENT)
            // 值
            .value(JavaVarValue.BATCH_NUM)
            // 构建
            .build();

    sb.append(JavaClassCodeUtils.getClassField(field));

    sb.append(Symbol.ENTER_LINE);

    // spring属性的注入操作,
    JavaClassFieldEntity fieldDomainService =
        JavaClassFieldEntity.getPrivateAutowiredField(
            targetPackage.getClassName(),
            targetPackage.getVarName(),
            targetPackage.getClassComment());
    sb.append(JavaClassCodeUtils.getClassField(fieldDomainService));

    sb.append(Symbol.ENTER_LINE);

    // 定义批量操作的集合对象
    String dataType =
        JavaKeyWord.LIST_TYPE + entityPackage.getClassName() + JavaKeyWord.LIST_TYPE_END;
    String newList =
        JavaKeyWord.NEW
            + Symbol.SPACE
            + JavaKeyWord.LIST_TYPE_ARRAYLIST
            + Symbol.BRACKET_LEFT
            + JavaVarName.FINAL_BATCH_INSERT_NUM
            + Symbol.BRACKET_RIGHT;

    JavaClassFieldEntity batchList =
        JavaClassFieldEntity.builder()
            // 访问修饰符
            .visit(JavaKeyWord.PRIVATE)
            // 类型
            .type(dataType)
            // 名称
            .name(JavaVarName.BATCH_LIST_NAME)
            // 注释
            .comment(CodeComment.FIELD_BATCH_LIST_COMMENT)
            // 值
            .value(newList)
            // 构建
            .build();

    sb.append(JavaClassCodeUtils.getClassField(batchList));

    sb.append(Symbol.ENTER_LINE);

    // 单个插入的属性的定义
    JavaClassFieldEntity fieldEntity =
        JavaClassFieldEntity.builder()
            // 访问修饰符
            .visit(JavaKeyWord.PRIVATE)
            // 类型
            .type(entityPackage.getClassName())
            // 名称
            .name(entityPackage.getVarName())
            // 注释
            .comment(entityPackage.getClassComment())
            // 构建
            .build();

    sb.append(JavaClassCodeUtils.getClassField(fieldEntity));

    sb.append(Symbol.ENTER_LINE);

    // 添加数据执行的类型,标识当前为单插入，批量插入，或者不插入数据
    JavaClassFieldEntity operatorType =
        JavaClassFieldEntity.builder()
            // 访问修饰符
            .visit(JavaKeyWord.PRIVATE)
            // 类型
            .type(JavaKeyWord.INT_TYPE)
            // 名称
            .name(JavaVarName.JUNIT_VAR_BATCH_INSERT)
            // 注释
            .comment(CodeComment.FIELD_OPERATOR_TYPE_COMMENT)
            // 构建
            .build();

    sb.append(JavaClassCodeUtils.getClassField(operatorType));

    sb.append(Symbol.ENTER_LINE);
  }

  /**
   * junit的数据获取
   *
   * @param sb sb对象
   * @param tabIndex 索引
   * @param poPackage 实体导入
   */
  private void junitGetData(StringBuilder sb, int tabIndex, ImportPackageInfo poPackage) {

    // 方法实体信息
    JavaMethodEntity methodInfo =
        JavaMethodEntity.builder()
            // 公共的访问修饰符
            .visit(JavaKeyWord.PUBLIC)
            // 方法注释
            .comment(CodeComment.JUNIT_GET_DATA_COMMENT)
            // 返回值
            .type(poPackage.getClassName())
            // 方法名
            .name(JavaMethodName.GET_DATA_METHOD)
            .build();
    // 方法定义生成
    JavaClassCodeUtils.methodDefine(sb, methodInfo);
    // 方法开始
    JavaClassCodeUtils.methodStart(sb);

    sb.append(JavaFormat.appendTab(tabIndex + 2)).append(poPackage.getClassName());
    sb.append(Symbol.SPACE);
    sb.append(JavaVarName.PARAM_BEAN).append(Symbol.SPACE).append(Symbol.EQUAL);
    sb.append(Symbol.SPACE).append(JavaKeyWord.NEW).append(Symbol.SPACE);
    sb.append(poPackage.getClassName()).append(Symbol.BRACKET_LEFT).append(Symbol.BRACKET_RIGHT);
    sb.append(Symbol.SEMICOLON).append(Symbol.ENTER_LINE);

    // 设置设置主键的方法
    sb.append(JavaFormat.appendTab(tabIndex + 2)).append(JavaKeyWord.THIS).append(Symbol.POINT);
    sb.append(JavaMethodName.SET_PRIMARY_FIELD).append(Symbol.BRACKET_LEFT);
    sb.append(JavaVarName.PARAM_BEAN).append(Symbol.BRACKET_RIGHT);
    sb.append(Symbol.SEMICOLON).append(Symbol.ENTER_LINE);

    // 设置普通属性
    sb.append(JavaFormat.appendTab(tabIndex + 2)).append(JavaKeyWord.THIS).append(Symbol.POINT);
    sb.append(JavaMethodName.SET_DATA_FIELD).append(Symbol.BRACKET_LEFT);
    sb.append(JavaVarName.PARAM_BEAN).append(Symbol.BRACKET_RIGHT);
    sb.append(Symbol.SEMICOLON).append(Symbol.ENTER_LINE);

    // 返回语句
    sb.append(JavaFormat.appendTab(tabIndex + 2)).append(JavaKeyWord.RETURN);
    sb.append(Symbol.SPACE).append(JavaVarName.PARAM_BEAN);
    sb.append(Symbol.SEMICOLON).append(Symbol.ENTER_LINE);

    // 方法结束
    JavaClassCodeUtils.methodEnd(sb);
  }

  /**
   * junit的数据准备方法
   *
   * @param sb
   * @param tabIndex
   */
  private void junitBeforeMethod(StringBuilder sb, int tabIndex) {

    // 方法实体信息
    JavaMethodEntity methodInfo =
        JavaMethodEntity.builder()
            // 注解符
            .annotation(JunitKey.JUNIT_BEFORE)
            // 公共的访问修饰符
            .visit(JavaKeyWord.PUBLIC)
            // 方法注释
            .comment(CodeComment.JUNIT_BEFORE_COMMENT)
            // 返回值
            .type(JavaKeyWord.VOID)
            // 方法名
            .name(JavaMethodName.BEFORE_SET_DATA)
            .build();

    // 方法定义生成
    JavaClassCodeUtils.methodDefine(sb, methodInfo);

    // 方法开始
    JavaClassCodeUtils.methodStart(sb);

    // 1,为集合对象赋值
    // 循环进行数据的生成
    sb.append(JavaFormat.appendTab(tabIndex + 2)).append(JavaKeyWord.FOR_KEY).append(Symbol.SPACE);
    sb.append(Symbol.BRACKET_LEFT).append(JavaKeyWord.INT_TYPE).append(Symbol.SPACE);
    sb.append(JavaVarName.FOR_TEMP_INDEX).append(Symbol.SPACE).append(Symbol.EQUAL);
    sb.append(Symbol.SPACE).append(JavaVarValue.FOR_INDEX_START).append(Symbol.SEMICOLON);
    sb.append(Symbol.SPACE).append(JavaVarName.FOR_TEMP_INDEX).append(Symbol.SPACE);
    sb.append(Symbol.ANGLE_BRACKETS_LEFT).append(Symbol.SPACE);
    sb.append(JavaVarName.FINAL_BATCH_INSERT_NUM);
    sb.append(Symbol.SEMICOLON).append(Symbol.SPACE).append(JavaVarName.FOR_TEMP_INDEX);
    sb.append(JavaVarValue.ADD_ADD).append(Symbol.BRACKET_RIGHT).append(Symbol.SPACE);
    sb.append(Symbol.BRACE_LEFT).append(Symbol.ENTER_LINE);

    // 数据循环插入
    sb.append(JavaFormat.appendTab(tabIndex + 3)).append(JavaVarName.BATCH_LIST_NAME);
    sb.append(Symbol.POINT).append(JavaMethodName.LIST_ADD);
    sb.append(Symbol.BRACKET_LEFT).append(JavaMethodName.GET_DATA_METHOD);
    sb.append(Symbol.BRACKET_LEFT).append(Symbol.BRACKET_RIGHT);
    sb.append(Symbol.BRACKET_RIGHT).append(Symbol.SEMICOLON).append(Symbol.ENTER_LINE);

    // 循环结束
    sb.append(JavaFormat.appendTab(tabIndex + 2)).append(Symbol.BRACE_RIGHT);
    sb.append(Symbol.ENTER_LINE);

    // 为单个对象属性赋值
    sb.append(JavaFormat.appendTab(tabIndex + 2)).append(JavaVarName.INSTANCE_NAME_ENTITY);
    sb.append(Symbol.SPACE).append(Symbol.EQUAL).append(Symbol.SPACE);
    sb.append(JavaVarName.BATCH_LIST_NAME).append(Symbol.POINT).append(JavaMethodName.GET);
    sb.append(Symbol.BRACKET_LEFT).append(JavaVarValue.FOR_INDEX_START);
    sb.append(Symbol.BRACKET_RIGHT);
    sb.append(Symbol.SEMICOLON).append(Symbol.ENTER_LINE);

    // 方法结束
    JavaClassCodeUtils.methodEnd(sb);
  }

  /**
   * 对属性进行设置
   *
   * @param sb 字符串
   * @param columnListParam 列信息
   */
  private void setProperties(
      StringBuilder sb, List<TableColumnDTO> columnListParam, DatabaseTypeEnum typeEnum) {

    // 拷贝集合
    List<TableColumnDTO> columnList = new ArrayList<>(columnListParam);
    TableColumnDTO columnInfo = null;

    for (int i = 0; i < columnList.size(); i++) {
      // 检查当前列是否为自增长
      columnInfo = columnList.get(i);

      // 如果当前列为自增长，则不设置
      if (columnInfo.getAutoIncrement()) {
        continue;
      }

      String value = DatabaseValue.INSTANCE.createValue(typeEnum, columnList.get(i));
      String setName = NameProcess.INSTANCE.toProJavaName(columnList.get(i).getColumnName());

      // 添加当前的属性值的注释
      sb.append(JavaFormat.appendTab(2)).append(Symbol.PATH).append(Symbol.PATH);
      sb.append(columnList.get(i).getColumnMsg());
      sb.append(Symbol.ENTER_LINE);
      // 添加生成代码的方法
      sb.append(JavaFormat.appendTab(2)).append(JavaVarName.PARAM_BEAN).append(Symbol.POINT);
      sb.append(JavaMethodName.SET).append(setName).append(Symbol.BRACKET_LEFT);
      sb.append(value);

      sb.append(Symbol.BRACKET_RIGHT).append(Symbol.SEMICOLON).append(Symbol.ENTER_LINE);
    }
  }

  /**
   * 进行批量插入操作
   *
   * @param insertType 插入数据的类型
   */
  public String setBatchInsertFlag(String insertType) {

    int tabIndex = 0;

    StringBuilder sb = new StringBuilder();
    // 对当前插入标识进行设置，以在after中进行数据的删除操作
    sb.append(JavaFormat.appendTab(tabIndex + 2))
        .append(JavaVarName.JUNIT_VAR_BATCH_INSERT)
        .append(Symbol.SPACE);
    sb.append(Symbol.EQUAL).append(Symbol.SPACE).append(insertType);
    sb.append(Symbol.SEMICOLON).append(Symbol.ENTER_LINE);

    return sb.toString();
  }

  /**
   * 查询结果的比较
   *
   * @param sb 字符
   */
  public void queryResponseListAssert(
      StringBuilder sb,
      List<TableColumnDTO> columnList,
      ImportPackageInfo poPackage,
      List<MethodInfo> methodList,
      List<TableColumnDTO> primaryColumn) {

    // 1,检查当前结果集中是否存在结果集的情况
    boolean checkResult = ReturnUtils.checkList(methodList);
    // 如果当前不存在返回结果集，则退出生成
    if (!checkResult) {
      return;
    }

    int tabIndex = 0;

    // 生成数据对比的方法
    generateAssertList(sb, tabIndex, poPackage);

    // 生成转换为map方法，用于根据key获取数据
    generateParseMap(sb, tabIndex, poPackage);

    // 生成获取实体key的方法
    generateMapKey(sb, tabIndex, poPackage, primaryColumn);

    // 数据对比的方法
    generateAssertData(sb, tabIndex, poPackage, columnList);
  }

  /**
   * 单数据对象方法的生成
   *
   * @param sb 字符
   */
  public void queryResponseDataAssert(
      StringBuilder sb, List<TableColumnDTO> columnList, ImportPackageInfo poPackage) {

    int tabIndex = 0;

    // 已经生成对象的对比 ，则返回
    if (sb.indexOf(JavaMethodName.ASSERT_DATA) != -1) {
      return;
    }

    // 数据对比的方法
    generateAssertData(sb, tabIndex, poPackage, columnList);
  }

  /** 数据集合对比方法 */
  private void generateAssertList(StringBuilder sb, int tabIndex, ImportPackageInfo poPackage) {
    // 生成对比结果集合的方法
    List<JavaMethodArguments> argumentList = new ArrayList<>(2);
    // 源数据集参数
    argumentList.add(
        JavaMethodArguments.builder()
            .type(JavaClassCodeUtils.listType(poPackage.getClassName()))
            .name(JavaVarName.ASSERT_PARAM_SRC_LIST)
            .comment(CodeComment.JUNIT_SRC_LIST_COMMENT)
            .build());
    // 目标数据集参数
    argumentList.add(
        JavaMethodArguments.builder()
            .type(JavaClassCodeUtils.listType(poPackage.getClassName()))
            .name(JavaVarName.ASSERT_PARAM_TARGET_LIST)
            .comment(CodeComment.JUNIT_TARGET_LIST_COMMENT)
            .build());

    // 方法实体信息
    JavaMethodEntity methodInfo =
        JavaMethodEntity.builder()
            // 公共的访问修饰符
            .visit(JavaKeyWord.PRIVATE)
            // 方法注释
            .comment(CodeComment.JUNIT_ASSERT_LIST_COMMENT)
            // 返回值
            .type(JavaKeyWord.VOID)
            // 方法名
            .name(JavaMethodName.ASSERT_DATA_LIST)
            // 参数
            .arguments(argumentList)
            .build();

    // 方法定义生成
    JavaClassCodeUtils.methodDefine(sb, methodInfo);

    // 方法开始
    JavaClassCodeUtils.methodStart(sb);

    // 数据集对比
    this.assertListData(sb, tabIndex, poPackage);

    // 结束
    // 方法开始
    JavaClassCodeUtils.methodEnd(sb);
  }

  /**
   * 数据集对比
   *
   * @param sb
   * @param tabIndex 1
   * @param poPackage 导入的包
   */
  private void assertListData(StringBuilder sb, int tabIndex, ImportPackageInfo poPackage) {
    // 1,调用转换map方法，将目标转换为map
    sb.append(JavaFormat.appendTab(tabIndex + 2)).append(JavaKeyWord.MAP_TYPE);
    sb.append(Symbol.ANGLE_BRACKETS_LEFT).append(JavaKeyWord.TYPE_STRING);
    sb.append(Symbol.COMMA).append(Symbol.SPACE);
    sb.append(poPackage.getClassName()).append(Symbol.ANGLE_BRACKETS_RIGHT);
    sb.append(Symbol.SPACE).append(JavaVarName.PARSE_MAP_TEMP).append(Symbol.SPACE);
    sb.append(Symbol.EQUAL).append(Symbol.SPACE).append(JavaKeyWord.THIS);
    sb.append(Symbol.POINT).append(JavaMethodName.PARSE_MAP).append(Symbol.BRACKET_LEFT);
    sb.append(JavaVarName.ASSERT_PARAM_TARGET_LIST).append(Symbol.BRACKET_RIGHT);
    sb.append(Symbol.SEMICOLON).append(Symbol.ENTER_LINE);

    // 循环遍历src集合，获取每个数据的内容，进行对比操作
    // 循环遍历
    sb.append(JavaFormat.appendTab(tabIndex + 2)).append(JavaKeyWord.FOR_KEY);
    sb.append(Symbol.SPACE).append(Symbol.BRACKET_LEFT);
    sb.append(poPackage.getClassName()).append(Symbol.SPACE);
    sb.append(JavaVarName.LIST_GET_NAME_TEMP_NAME).append(Symbol.SPACE).append(Symbol.COLON);
    sb.append(Symbol.SPACE).append(JavaVarName.ASSERT_PARAM_SRC_LIST).append(Symbol.BRACKET_RIGHT);
    sb.append(Symbol.BRACE_LEFT).append(Symbol.ENTER_LINE);

    // 调用当前转换为key的方法
    sb.append(JavaFormat.appendTab(tabIndex + 3)).append(JavaKeyWord.TYPE_STRING);
    sb.append(Symbol.SPACE).append(JavaVarName.GET_KEY_NAME).append(Symbol.SPACE);
    sb.append(Symbol.EQUAL).append(Symbol.SPACE).append(JavaKeyWord.THIS);
    sb.append(Symbol.POINT).append(JavaMethodName.PARSE_MAP_KEY);
    sb.append(Symbol.BRACKET_LEFT).append(JavaVarName.LIST_GET_NAME_TEMP_NAME);
    sb.append(Symbol.BRACKET_RIGHT).append(Symbol.SEMICOLON);
    sb.append(Symbol.ENTER_LINE);

    // 从map中获取数据
    sb.append(JavaFormat.appendTab(tabIndex + 3));
    sb.append(poPackage.getClassName()).append(Symbol.SPACE);
    sb.append(JavaVarName.ASSERT_DATA_TARGET).append(Symbol.SPACE);
    sb.append(Symbol.EQUAL).append(Symbol.SPACE);
    sb.append(JavaVarName.PARSE_MAP_TEMP);
    sb.append(Symbol.POINT).append(JavaMethodName.GET).append(Symbol.BRACKET_LEFT);
    sb.append(JavaVarName.GET_KEY_NAME).append(Symbol.BRACKET_RIGHT);
    sb.append(Symbol.SEMICOLON).append(Symbol.ENTER_LINE);

    // 调用对比方法
    sb.append(JavaFormat.appendTab(tabIndex + 3));
    sb.append(JavaKeyWord.THIS).append(Symbol.POINT);
    sb.append(JavaMethodName.ASSERT_DATA).append(Symbol.BRACKET_LEFT);
    sb.append(JavaVarName.LIST_GET_NAME_TEMP_NAME).append(Symbol.COMMA);
    sb.append(Symbol.SPACE).append(JavaVarName.ASSERT_DATA_TARGET).append(Symbol.BRACKET_RIGHT);
    sb.append(Symbol.SEMICOLON).append(Symbol.ENTER_LINE);

    // 循环结束
    sb.append(JavaFormat.appendTab(tabIndex + 2)).append(Symbol.BRACE_RIGHT);
    sb.append(Symbol.ENTER_LINE);
  }

  /**
   * 转换为map的方法
   *
   * @param sb
   * @param tabIndex
   * @param poPackage
   */
  private void generateParseMap(StringBuilder sb, int tabIndex, ImportPackageInfo poPackage) {
    // 生成map，用于根据key获取数据

    List<JavaMethodArguments> argumentList = new ArrayList<>(1);
    // 源数据集参数
    argumentList.add(
        JavaMethodArguments.builder()
            .type(JavaClassCodeUtils.listType(poPackage.getClassName()))
            .name(JavaVarName.LIST_NAME)
            .comment(CodeComment.JUNIT_PARSE_LIST_COMMENT)
            .build());

    // 方法实体信息
    JavaMethodEntity methodInfo =
        JavaMethodEntity.builder()
            // 公共的访问修饰符
            .visit(JavaKeyWord.PRIVATE)
            // 方法注释
            .comment(CodeComment.JUNIT_PARSE_MAP_COMMENT)
            // 返回值
            .type(JavaClassCodeUtils.mapStringKey(poPackage.getClassName()))
            // 方法名
            .name(JavaMethodName.PARSE_MAP)
            // 参数
            .arguments(argumentList)
            .build();

    // 方法定义生成
    JavaClassCodeUtils.methodDefine(sb, methodInfo);

    // 方法开始
    JavaClassCodeUtils.methodStart(sb);

    // 将数据转换为map
    this.assertParseMap(sb, tabIndex, poPackage);

    // 方法的结束
    JavaClassCodeUtils.methodEnd(sb);
  }

  /**
   * 将数据转换为map
   *
   * @param sb
   * @param tabIndex
   * @param poPackage
   */
  private void assertParseMap(StringBuilder sb, int tabIndex, ImportPackageInfo poPackage) {
    // map的定义
    sb.append(JavaFormat.appendTab(tabIndex + 2)).append(JavaKeyWord.MAP_TYPE);
    sb.append(Symbol.ANGLE_BRACKETS_LEFT).append(JavaKeyWord.TYPE_STRING);
    sb.append(Symbol.COMMA).append(Symbol.SPACE).append(poPackage.getClassName());
    sb.append(Symbol.ANGLE_BRACKETS_RIGHT).append(Symbol.SPACE);
    sb.append(JavaVarName.PARSE_MAP_TEMP).append(Symbol.SPACE);
    sb.append(Symbol.EQUAL).append(Symbol.SPACE).append(JavaKeyWord.NEW);
    sb.append(Symbol.SPACE).append(JavaKeyWord.MAP_TYPE_HASHMAP);
    sb.append(Symbol.ANGLE_BRACKETS_LEFT).append(Symbol.ANGLE_BRACKETS_RIGHT);
    sb.append(Symbol.BRACKET_LEFT).append(JavaVarName.LIST_NAME).append(Symbol.POINT);
    sb.append(JavaMethodName.SIZE).append(Symbol.BRACKET_LEFT);
    sb.append(Symbol.BRACKET_RIGHT).append(Symbol.BRACKET_RIGHT);
    sb.append(Symbol.SEMICOLON).append(Symbol.ENTER_LINE);

    // 循环遍历
    sb.append(JavaFormat.appendTab(tabIndex + 2)).append(JavaKeyWord.FOR_KEY);
    sb.append(Symbol.SPACE).append(Symbol.BRACKET_LEFT);
    sb.append(poPackage.getClassName()).append(Symbol.SPACE);
    sb.append(JavaVarName.LIST_GET_NAME_TEMP_NAME).append(Symbol.SPACE).append(Symbol.COLON);
    sb.append(Symbol.SPACE).append(JavaVarName.LIST_NAME).append(Symbol.BRACKET_RIGHT);
    sb.append(Symbol.BRACE_LEFT).append(Symbol.ENTER_LINE);

    // 调用当前转换为key的方法
    sb.append(JavaFormat.appendTab(tabIndex + 3)).append(JavaKeyWord.TYPE_STRING);
    sb.append(Symbol.SPACE).append(JavaVarName.GET_KEY_NAME).append(Symbol.SPACE);
    sb.append(Symbol.EQUAL).append(Symbol.SPACE).append(JavaKeyWord.THIS);
    sb.append(Symbol.POINT).append(JavaMethodName.PARSE_MAP_KEY);
    sb.append(Symbol.BRACKET_LEFT).append(JavaVarName.LIST_GET_NAME_TEMP_NAME);
    sb.append(Symbol.BRACKET_RIGHT).append(Symbol.SEMICOLON);
    sb.append(Symbol.ENTER_LINE);

    // 将数据存储至map中
    sb.append(JavaFormat.appendTab(tabIndex + 3)).append(JavaVarName.PARSE_MAP_TEMP);
    sb.append(Symbol.POINT).append(JavaMethodName.PUT).append(Symbol.BRACKET_LEFT);
    sb.append(JavaVarName.GET_KEY_NAME).append(Symbol.COMMA);
    sb.append(JavaVarName.LIST_GET_NAME_TEMP_NAME).append(Symbol.BRACKET_RIGHT);
    sb.append(Symbol.SEMICOLON).append(Symbol.ENTER_LINE);

    // 循环结束
    sb.append(JavaFormat.appendTab(tabIndex + 2)).append(Symbol.BRACE_RIGHT);
    sb.append(Symbol.ENTER_LINE);

    // 返回语句
    sb.append(JavaFormat.appendTab(tabIndex + 2)).append(JavaKeyWord.RETURN);
    sb.append(Symbol.SPACE).append(JavaVarName.PARSE_MAP_TEMP).append(Symbol.SEMICOLON);
    sb.append(Symbol.ENTER_LINE);
  }

  /**
   * 生成map的key
   *
   * @param sb 字符信息
   * @param tabIndex tab的索引号
   * @param poPackage 实体包
   */
  private void generateMapKey(
      StringBuilder sb,
      int tabIndex,
      ImportPackageInfo poPackage,
      List<TableColumnDTO> primaryColumn) {

    // 方法实体信息
    JavaMethodEntity methodInfo =
        JavaMethodEntity.builder()
            // 公共的访问修饰符
            .visit(JavaKeyWord.PRIVATE)
            // 方法注释
            .comment(CodeComment.JUNIT_PARSE_KEY_COMMENT)
            // 返回值
            .type(JavaKeyWord.TYPE_STRING)
            // 方法名
            .name(JavaMethodName.PARSE_MAP_KEY)
            // 参数
            .arguments(
                Arrays.asList(
                    JavaMethodArguments.builder()
                        .type(poPackage.getClassName())
                        .name(JavaVarName.PARAM_BEAN)
                        .comment(CodeComment.JUNIT_PARSE_KEY_PO_COMMENT)
                        .build()))
            .build();

    // 方法定义生成
    JavaClassCodeUtils.methodDefine(sb, methodInfo);

    // 方法开始
    JavaClassCodeUtils.methodStart(sb);

    // 声明StringBuilder对象
    sb.append(JavaFormat.appendTab(tabIndex + 2)).append(JavaKeyWord.TYPE_STRING_BUILDER_NAME);
    sb.append(Symbol.SPACE).append(JavaVarName.GET_KEY_NAME).append(Symbol.SPACE);
    sb.append(Symbol.EQUAL).append(Symbol.SPACE).append(JavaKeyWord.NEW);
    sb.append(Symbol.SPACE).append(JavaKeyWord.TYPE_STRING_BUILDER_NAME);
    sb.append(Symbol.BRACKET_LEFT).append(Symbol.BRACKET_RIGHT);
    sb.append(Symbol.SEMICOLON).append(Symbol.ENTER_LINE);

    // 将主键构建为key
    for (TableColumnDTO columnItem : primaryColumn) {
      String getName = NameProcess.INSTANCE.toFieldName(columnItem.getColumnName());
      String outName = NameProcess.INSTANCE.toJavaNameFirstUpper(getName);

      sb.append(JavaFormat.appendTab(tabIndex + 2));
      sb.append(JavaVarName.GET_KEY_NAME).append(Symbol.POINT).append(JavaMethodName.APPEND);
      sb.append(Symbol.BRACKET_LEFT);
      sb.append(JavaVarName.PARAM_BEAN).append(Symbol.POINT).append(JavaMethodName.GET);
      sb.append(outName).append(Symbol.BRACKET_LEFT).append(Symbol.BRACKET_RIGHT);
      sb.append(Symbol.BRACKET_RIGHT).append(Symbol.SEMICOLON);
      sb.append(Symbol.ENTER_LINE);
    }
    sb.append(JavaFormat.appendTab(tabIndex + 2));
    sb.append(JavaKeyWord.RETURN).append(Symbol.SPACE).append(JavaVarName.GET_KEY_NAME);
    sb.append(Symbol.POINT).append(JavaMethodName.TO_STRING).append(Symbol.BRACKET_LEFT);
    sb.append(Symbol.BRACKET_RIGHT).append(Symbol.SEMICOLON).append(Symbol.ENTER_LINE);

    // 方法的结束
    JavaClassCodeUtils.methodEnd(sb);
  }

  /**
   * 生成数据对比的方法
   *
   * @param sb 字符信息
   * @param tabIndex tab的索引号
   * @param poPackage 实体包
   * @param columnList 列信息
   */
  private void generateAssertData(
      StringBuilder sb,
      int tabIndex,
      ImportPackageInfo poPackage,
      List<TableColumnDTO> columnList) {
    // 生成对比的数据方法
    // 源数据集
    JavaMethodArguments srcArguments =
        JavaMethodArguments.builder()
            .type(poPackage.getClassName())
            .name(JavaVarName.ASSERT_DATA_SRC)
            .comment(CodeComment.JUNIT_ASSERT_DATA_SRC)
            .build();

    // 目标数据集
    JavaMethodArguments targetArguments =
        JavaMethodArguments.builder()
            .type(poPackage.getClassName())
            .name(JavaVarName.ASSERT_DATA_TARGET)
            .comment(CodeComment.JUNIT_ASSERT_DATA_TARGET)
            .build();

    // 方法实体信息
    JavaMethodEntity methodInfo =
        JavaMethodEntity.builder()
            // 公共的访问修饰符
            .visit(JavaKeyWord.PRIVATE)
            // 方法注释
            .comment(CodeComment.JUNIT_ASSERT_DATA_VALUE)
            // 返回值
            .type(JavaKeyWord.VOID)
            // 方法名
            .name(JavaMethodName.ASSERT_DATA)
            // 参数
            .arguments(Arrays.asList(srcArguments, targetArguments))
            .build();

    // 方法定义生成
    JavaClassCodeUtils.methodDefine(sb, methodInfo);

    // 方法开始
    JavaClassCodeUtils.methodStart(sb);

    // 进行数据的对比
    for (TableColumnDTO columnInfo : columnList) {
      // 1,获取数据的名称
      String fieldName = NameProcess.INSTANCE.toFieldName(columnInfo.getColumnName());
      String getName = NameProcess.INSTANCE.toJavaNameFirstUpper(fieldName);

      // 先生成注释
      sb.append(JavaFormat.appendTab(tabIndex + 2)).append(Symbol.PATH).append(Symbol.PATH);
      sb.append(columnInfo.getColumnMsg()).append(Symbol.ENTER_LINE);
      // 对比的代码
      sb.append(JavaFormat.appendTab(tabIndex + 2));
      sb.append(JunitKey.ASSERT).append(JavaVarName.ASSERT_DATA_SRC).append(Symbol.POINT);
      sb.append(JavaMethodName.GET).append(getName).append(Symbol.BRACKET_LEFT);
      sb.append(Symbol.BRACKET_RIGHT).append(Symbol.COMMA);
      sb.append(Symbol.SPACE).append(JavaVarName.ASSERT_DATA_TARGET);
      sb.append(Symbol.POINT).append(JavaMethodName.GET).append(getName);
      sb.append(Symbol.BRACKET_LEFT).append(Symbol.BRACKET_RIGHT);
      sb.append(Symbol.BRACKET_RIGHT).append(Symbol.SEMICOLON);
      sb.append(Symbol.ENTER_LINE);
    }

    // 结束
    JavaClassCodeUtils.methodEnd(sb);
  }
}
