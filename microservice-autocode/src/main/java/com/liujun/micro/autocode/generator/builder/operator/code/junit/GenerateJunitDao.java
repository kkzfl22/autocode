package com.liujun.micro.autocode.generator.builder.operator.code.junit;

import com.liujun.micro.autocode.config.menuTree.DomainMenuTree;
import com.liujun.micro.autocode.config.menuTree.MenuNode;
import com.liujun.micro.autocode.config.menuTree.MenuTreePackagePath;
import com.liujun.micro.autocode.constant.Symbol;
import com.liujun.micro.autocode.entity.config.MethodInfo;
import com.liujun.micro.autocode.generator.builder.constant.*;
import com.liujun.micro.autocode.generator.builder.entity.ImportPackageInfo;
import com.liujun.micro.autocode.generator.builder.operator.utils.ImportPackageUtils;
import com.liujun.micro.autocode.generator.builder.operator.utils.MethodUtils;
import com.liujun.micro.autocode.generator.builder.utils.MenuTreeProcessUtil;
import com.liujun.micro.autocode.generator.database.constant.DatabaseTypeEnum;
import com.liujun.micro.autocode.generator.database.entity.TableColumnDTO;
import com.liujun.micro.autocode.generator.database.entity.TableInfoDTO;
import com.liujun.micro.autocode.generator.database.service.DatabaseValue;
import com.liujun.micro.autocode.generator.javalanguage.constant.JavaKeyWord;
import com.liujun.micro.autocode.generator.javalanguage.serivce.JavaFormat;
import com.liujun.micro.autocode.generator.javalanguage.serivce.NameProcess;

import java.util.ArrayList;
import java.util.List;

/**
 * @author liujun
 * @version 0.0.1
 */
public class GenerateJunitDao {

  /** 测试的标识 */
  public static final String DAO_TEST_NAME = "Test";

  /** 需要导入的包 */
  private static final String[] IMPORT_PKG =
      new String[] {
        "org.junit.*",
        "org.apache.commons.lang3.RandomStringUtils",
        "org.apache.commons.lang3.RandomUtils",
        JavaKeyWord.IMPORT_LIST,
        JavaKeyWord.IMPORT_ARRAYLIST,
        JavaKeyWord.IMPORT_MAP,
        "com.paraview.security.pap.microservice.InsertType",
        "com.paraview.security.pap.microservice.TestParent",
        "java.util.HashMap",
        "org.springframework.beans.factory.annotation.Autowired"
      };

  /** 注释 */
  private static final String DATABASE_DOC = "数据库操作单元测试";

  /** 测试方法的前缀 */
  public static final String JUNIT_METHOD_BEFORE = "test";

  /** 进行构建操作 */
  public static final GenerateJunitDao INSTANCE = new GenerateJunitDao();

  /**
   * 文件头定义
   *
   * @param poPackage 数据库实体
   * @param daoPackage 数据库操作接口
   * @param menuTree 路径结构
   * @param tableInfo 数据库信息
   * @return
   */
  public StringBuilder defineHead(
      ImportPackageInfo poPackage,
      ImportPackageInfo daoPackage,
      DomainMenuTree menuTree,
      TableInfoDTO tableInfo,
      String outClassName) {

    StringBuilder sb = new StringBuilder();

    // 单元测试的路径
    MenuNode poNode = MenuTreePackagePath.getRepositoryDaoNode(menuTree);
    String junitDaoPackageStr = MenuTreeProcessUtil.outJavaPackage(poNode);

    // 定义包
    sb.append(JavaKeyWord.PACKAGE).append(Symbol.SPACE);
    sb.append(junitDaoPackageStr).append(Symbol.SEMICOLON);
    sb.append(Symbol.ENTER_LINE).append(Symbol.ENTER_LINE);

    for (String importPackage : IMPORT_PKG) {
      sb.append(JavaKeyWord.IMPORT).append(Symbol.SPACE).append(importPackage);
      sb.append(Symbol.SEMICOLON).append(Symbol.ENTER_LINE);
    }

    // 导入数据库存储的包
    String poPackagePath = ImportPackageUtils.packageOut(poPackage);
    sb.append(JavaKeyWord.IMPORT).append(Symbol.SPACE);
    sb.append(poPackagePath).append(Symbol.SEMICOLON);
    sb.append(Symbol.ENTER_LINE);

    // 导入dao的包
    String daoPackagePath = ImportPackageUtils.packageOut(daoPackage);
    sb.append(JavaKeyWord.IMPORT).append(Symbol.SPACE);
    sb.append(daoPackagePath).append(Symbol.SEMICOLON).append(Symbol.ENTER_LINE);
    sb.append(Symbol.ENTER_LINE).append(Symbol.ENTER_LINE);

    // 添加类注释信息
    sb.append(JavaKeyWord.ANNO_CLASS).append(Symbol.ENTER_LINE);
    sb.append(JavaKeyWord.ANNO_CLASS_MID).append(Symbol.SPACE).append(tableInfo.getTableComment());
    sb.append(Symbol.BRACKET_LEFT).append(tableInfo.getTableName()).append(Symbol.BRACKET_RIGHT);
    sb.append(DATABASE_DOC).append(Symbol.ENTER_LINE);
    sb.append(JavaKeyWord.ANNO_CLASS_MID).append(Symbol.ENTER_LINE);
    sb.append(JavaKeyWord.DOC_VERSION).append(Symbol.ENTER_LINE);
    sb.append(JavaKeyWord.DOC_AUTH).append(Symbol.ENTER_LINE);
    sb.append(JavaKeyWord.ANNO_OVER).append(Symbol.ENTER_LINE);

    // 添加类的信息
    sb.append(JavaKeyWord.ClASS_START).append(outClassName);
    sb.append(Symbol.SPACE).append(JavaKeyWord.EXTEND).append(Symbol.SPACE);
    sb.append(JunitKey.TEST_PARENT).append(Symbol.BRACE_LEFT);
    sb.append(Symbol.ENTER_LINE).append(Symbol.ENTER_LINE);

    return sb;
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
    setPrimaryFieldMethod(sb, poPackage, typeEnum, primaryList, 0);
    // 2,调用设置其他属性
    setDataFieldMethod(sb, poPackage, typeEnum, columnList, primaryList, 0);
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
      List<TableColumnDTO> primaryList,
      int tabNum) {

    // 方法注释
    sb.append(JavaFormat.appendTab(tabNum + 1)).append(JavaKeyWord.ANNO_CLASS);
    sb.append(Symbol.ENTER_LINE);
    sb.append(JavaFormat.appendTab(tabNum + 1)).append(JavaKeyWord.ANNO_CLASS_MID);
    sb.append(Symbol.SPACE);
    sb.append(CodeComment.SET_PRIMARY_FIELD_COMMENT);
    sb.append(Symbol.ENTER_LINE);
    sb.append(JavaFormat.appendTab(tabNum + 1)).append(JavaKeyWord.ANNO_CLASS_MID);
    sb.append(Symbol.ENTER_LINE);
    sb.append(JavaFormat.appendTab(tabNum + 1)).append(JavaKeyWord.ANNO_CLASS_MID);
    sb.append(JavaKeyWord.METHOD_PARAM);
    sb.append(JavaVarName.PARAM_BEAN).append(Symbol.SPACE);
    sb.append(CodeComment.METHOD_PARAM_DOC);
    sb.append(Symbol.ENTER_LINE);
    sb.append(JavaFormat.appendTab(tabNum + 1)).append(JavaKeyWord.ANNO_OVER);
    sb.append(Symbol.ENTER_LINE);

    // 属性相关值的获取操作
    sb.append(JavaFormat.appendTab(tabNum + 1)).append(JavaKeyWord.PRIVATE).append(Symbol.SPACE);
    sb.append(JavaKeyWord.VOID).append(Symbol.SPACE);
    sb.append(JavaMethodName.SET_PRIMARY_FIELD).append(Symbol.BRACKET_LEFT);
    sb.append(poPackage.getClassName()).append(Symbol.SPACE);
    sb.append(JavaVarName.PARAM_BEAN).append(Symbol.BRACKET_RIGHT);
    sb.append(Symbol.BRACE_LEFT).append(Symbol.ENTER_LINE);
    // 属性的值设置
    this.setProperties(sb, primaryList, typeEnum);
    sb.append(JavaFormat.appendTab(tabNum + 1))
        .append(Symbol.BRACE_RIGHT)
        .append(Symbol.ENTER_LINE);
    sb.append(Symbol.ENTER_LINE);
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
      List<TableColumnDTO> primaryList,
      int tabNum) {

    // 1,通过数据需在排除掉集合数据
    List<TableColumnDTO> dataList = new ArrayList<>(columnList);
    // 移除主键列
    dataList.removeAll(primaryList);

    // 方法注释
    sb.append(JavaFormat.appendTab(tabNum + 1)).append(JavaKeyWord.ANNO_CLASS);
    sb.append(Symbol.ENTER_LINE);
    sb.append(JavaFormat.appendTab(tabNum + 1)).append(JavaKeyWord.ANNO_CLASS_MID);
    sb.append(Symbol.SPACE);
    sb.append(CodeComment.SET_DATA_FIELD_COMMENT);
    sb.append(Symbol.ENTER_LINE);
    sb.append(JavaFormat.appendTab(tabNum + 1)).append(JavaKeyWord.ANNO_CLASS_MID);
    sb.append(Symbol.ENTER_LINE);
    sb.append(JavaFormat.appendTab(tabNum + 1)).append(JavaKeyWord.ANNO_CLASS_MID);
    sb.append(JavaKeyWord.METHOD_PARAM).append(JavaVarName.PARAM_BEAN);
    sb.append(Symbol.SPACE).append(CodeComment.METHOD_PARAM_DOC);
    sb.append(Symbol.ENTER_LINE);
    sb.append(JavaFormat.appendTab(tabNum + 1)).append(JavaKeyWord.ANNO_OVER);
    sb.append(Symbol.ENTER_LINE);

    // 属性相关值的获取操作
    sb.append(JavaFormat.appendTab(tabNum + 1)).append(JavaKeyWord.PRIVATE).append(Symbol.SPACE);
    sb.append(JavaKeyWord.VOID).append(Symbol.SPACE);
    sb.append(JavaMethodName.SET_DATA_FIELD).append(Symbol.BRACKET_LEFT);
    sb.append(poPackage.getClassName()).append(Symbol.SPACE);
    sb.append(JavaVarName.PARAM_BEAN).append(Symbol.BRACKET_RIGHT);
    sb.append(Symbol.BRACE_LEFT).append(Symbol.ENTER_LINE);
    // 属性的值设置
    this.setProperties(sb, dataList, typeEnum);
    sb.append(JavaFormat.appendTab(tabNum)).append(Symbol.BRACE_RIGHT).append(Symbol.ENTER_LINE);
    sb.append(Symbol.ENTER_LINE);
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
    this.classFieldDefine(sb, tabIndex, poPackage, daoPackage);

    // 调用数据准备方法
    this.junitBeforeMethod(sb, tabIndex);

    // 数据生成方法
    this.junitGetData(sb, tabIndex, poPackage);
  }

  /**
   * 类属性的定义
   *
   * @param sb
   * @param tabIndex 索引
   * @param poPackage po的类信息
   * @param daoPackage dao包信息
   */
  private void classFieldDefine(
      StringBuilder sb, int tabIndex, ImportPackageInfo poPackage, ImportPackageInfo daoPackage) {
    // 静态常量
    sb.append(JavaFormat.appendTab(tabIndex + 1));
    sb.append(JavaKeyWord.PRIVATE).append(Symbol.SPACE);
    sb.append(JavaKeyWord.STATIC).append(Symbol.SPACE);
    sb.append(JavaKeyWord.FINAL).append(Symbol.SPACE);
    sb.append(JavaKeyWord.INT_TYPE).append(Symbol.SPACE);
    sb.append(JavaVarName.FINAL_BATCH_INSERT_NUM).append(Symbol.SPACE);
    sb.append(Symbol.EQUAL).append(Symbol.SPACE);
    sb.append(JavaVarValue.BATCH_NUM).append(Symbol.SEMICOLON);
    sb.append(Symbol.ENTER_LINE);
    sb.append(Symbol.ENTER_LINE);

    // 添加前置方法
    sb.append(JavaFormat.appendTab(tabIndex + 1));
    sb.append(JunitKey.AUTOWIRED);
    sb.append(Symbol.ENTER_LINE);
    // 实体的spring注入
    sb.append(JavaFormat.appendTab(tabIndex + 1)).append(JavaKeyWord.PRIVATE);
    sb.append(Symbol.SPACE).append(daoPackage.getClassName());
    sb.append(Symbol.SPACE);
    sb.append(JavaVarName.INSTANCE_NAME).append(Symbol.SEMICOLON).append(Symbol.ENTER_LINE);
    sb.append(Symbol.ENTER_LINE);

    // 定义批量操作的集合对象
    sb.append(JavaFormat.appendTab(tabIndex + 1)).append(JavaKeyWord.PRIVATE);
    sb.append(Symbol.SPACE).append(JavaKeyWord.LIST_TYPE);
    sb.append(poPackage.getClassName()).append(JavaKeyWord.LIST_TYPE_END);
    sb.append(Symbol.SPACE).append(JavaVarName.BATCH_LIST_NAME);
    sb.append(Symbol.SPACE).append(Symbol.EQUAL).append(Symbol.SPACE);
    sb.append(JavaKeyWord.NEW).append(Symbol.SPACE);
    sb.append(JavaKeyWord.LIST_TYPE_ARRAYLIST).append(Symbol.BRACKET_LEFT);
    sb.append(JavaVarName.FINAL_BATCH_INSERT_NUM).append(Symbol.BRACKET_RIGHT);
    sb.append(Symbol.SEMICOLON).append(Symbol.ENTER_LINE);
    sb.append(Symbol.ENTER_LINE);

    // 定义属性的类型
    sb.append(JavaFormat.appendTab(tabIndex + 1)).append(JavaKeyWord.PRIVATE);
    sb.append(Symbol.SPACE).append(poPackage.getClassName());
    sb.append(Symbol.SPACE).append(JavaVarName.INSTANCE_NAME_PO);
    sb.append(Symbol.SEMICOLON).append(Symbol.ENTER_LINE);
    sb.append(Symbol.ENTER_LINE);

    // 添加数据执行的类型,标识当前为单插入，批量插入，或者不插入数据
    sb.append(JavaFormat.appendTab(tabIndex + 1)).append(JavaKeyWord.PRIVATE);
    sb.append(Symbol.SPACE).append(JavaKeyWord.INT_TYPE);
    sb.append(Symbol.SPACE).append(JavaVarName.JUNIT_VAR_BATCH_INSERT);
    sb.append(Symbol.SEMICOLON).append(Symbol.ENTER_LINE);
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
    // 获取数据的方法调用
    sb.append(JavaFormat.appendTab(tabIndex + 1)).append(JavaKeyWord.PUBLIC);
    sb.append(Symbol.SPACE).append(poPackage.getClassName());
    sb.append(Symbol.SPACE).append(JavaMethodName.GET_DATA_METHOD);
    sb.append(Symbol.BRACKET_LEFT).append(Symbol.BRACKET_RIGHT);
    sb.append(Symbol.BRACE_LEFT).append(Symbol.ENTER_LINE);
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

    sb.append(JavaFormat.appendTab(tabIndex + 1)).append(Symbol.BRACE_RIGHT);
    sb.append(Symbol.ENTER_LINE);
    sb.append(Symbol.ENTER_LINE);
  }

  /**
   * junit的数据准备方法
   *
   * @param sb
   * @param tabIndex
   */
  private void junitBeforeMethod(StringBuilder sb, int tabIndex) {
    // 添加初始化实体对象的方法
    // 注释
    sb.append(JavaFormat.appendTab(tabIndex + 1)).append(JavaKeyWord.ANNO_CLASS);
    sb.append(Symbol.ENTER_LINE);
    sb.append(JavaFormat.appendTab(tabIndex + 1)).append(JavaKeyWord.ANNO_CLASS_MID);
    sb.append(Symbol.SPACE).append(CodeComment.JUNIT_BEFORE_COMMENT);
    sb.append(Symbol.ENTER_LINE);
    sb.append(JavaFormat.appendTab(tabIndex + 1)).append(JavaKeyWord.ANNO_CLASS_MID);
    sb.append(Symbol.ENTER_LINE);
    sb.append(JavaFormat.appendTab(tabIndex + 1)).append(JavaKeyWord.ANNO_OVER);
    sb.append(Symbol.ENTER_LINE);

    // 定义方法
    sb.append(JavaFormat.appendTab(tabIndex + 1)).append(JunitKey.JUNIT_BEFORE);
    sb.append(Symbol.ENTER_LINE);
    sb.append(JavaFormat.appendTab(tabIndex + 1)).append(JavaKeyWord.PUBLIC).append(Symbol.SPACE);
    sb.append(JavaKeyWord.VOID).append(Symbol.SPACE);
    sb.append(JavaMethodName.BEFORE_SET_DATA)
        .append(Symbol.BRACKET_LEFT)
        .append(Symbol.BRACKET_RIGHT);
    sb.append(Symbol.BRACE_LEFT).append(Symbol.ENTER_LINE);

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
    sb.append(JavaFormat.appendTab(tabIndex + 2)).append(JavaVarName.INSTANCE_NAME_PO);
    sb.append(Symbol.SPACE).append(Symbol.EQUAL).append(Symbol.SPACE);
    sb.append(JavaVarName.BATCH_LIST_NAME).append(Symbol.POINT).append(JavaMethodName.GET);
    sb.append(Symbol.BRACKET_LEFT).append(JavaVarValue.FOR_INDEX_START);
    sb.append(Symbol.BRACKET_RIGHT);
    sb.append(Symbol.SEMICOLON).append(Symbol.ENTER_LINE);
    sb.append(JavaFormat.appendTab(tabIndex + 1)).append(Symbol.BRACE_RIGHT);
    sb.append(Symbol.ENTER_LINE).append(Symbol.ENTER_LINE);
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
   * @param sb
   * @param tabIndex
   * @param insertType 插入数据的类型
   */
  public void setBatchInsertFlag(StringBuilder sb, int tabIndex, String insertType) {
    // 对当前插入标识进行设置，以在after中进行数据的删除操作
    sb.append(JavaFormat.appendTab(tabIndex + 2))
        .append(JavaVarName.JUNIT_VAR_BATCH_INSERT)
        .append(Symbol.SPACE);
    sb.append(Symbol.EQUAL).append(Symbol.SPACE).append(insertType);
    sb.append(Symbol.SEMICOLON).append(Symbol.ENTER_LINE);
  }

  /**
   * 查询结果的比较
   *
   * @param sb 字符
   */
  public void queryResponseAssert(
      StringBuilder sb,
      List<TableColumnDTO> columnList,
      ImportPackageInfo poPackage,
      List<MethodInfo> methodList,
      List<TableColumnDTO> primaryColumn) {

    // 1,检查当前结果集中是否存在结果集的情况
    boolean checkResult = MethodUtils.checkResultList(methodList);
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

  /** 数据集合对比方法 */
  private void generateAssertList(StringBuilder sb, int tabIndex, ImportPackageInfo poPackage) {
    // 生成对比结果集合的方法
    // 1,生成注释
    sb.append(JavaFormat.appendTab(tabIndex + 1)).append(JavaKeyWord.ANNO_CLASS);
    sb.append(Symbol.ENTER_LINE);
    sb.append(JavaFormat.appendTab(tabIndex + 1)).append(JavaKeyWord.ANNO_CLASS_MID);
    sb.append(Symbol.SPACE).append(CodeComment.JUNIT_ASSERT_LIST_COMMENT);
    sb.append(Symbol.ENTER_LINE);
    sb.append(JavaFormat.appendTab(tabIndex + 1)).append(JavaKeyWord.ANNO_CLASS_MID);
    sb.append(Symbol.ENTER_LINE);
    sb.append(JavaFormat.appendTab(tabIndex + 1)).append(JavaKeyWord.ANNO_CLASS_MID);
    sb.append(JavaKeyWord.METHOD_PARAM).append(JavaVarName.ASSERT_PARAM_SRC_LIST);
    sb.append(Symbol.SPACE).append(CodeComment.JUNIT_SRC_LIST_COMMENT);
    sb.append(Symbol.ENTER_LINE);
    sb.append(JavaFormat.appendTab(tabIndex + 1)).append(JavaKeyWord.ANNO_CLASS_MID);
    sb.append(JavaKeyWord.METHOD_PARAM).append(JavaVarName.ASSERT_PARAM_TARGET_LIST);
    sb.append(Symbol.SPACE).append(CodeComment.JUNIT_TARGET_LIST_COMMENT);
    sb.append(Symbol.ENTER_LINE);
    sb.append(JavaFormat.appendTab(tabIndex + 1)).append(JavaKeyWord.ANNO_OVER);
    sb.append(Symbol.ENTER_LINE);

    // 生成数据集方法名
    sb.append(JavaFormat.appendTab(tabIndex + 1)).append(JavaKeyWord.PRIVATE).append(Symbol.SPACE);
    sb.append(JavaKeyWord.VOID).append(Symbol.SPACE).append(JavaMethodName.ASSERT_DATA_LIST);
    sb.append(Symbol.BRACKET_LEFT).append(JavaKeyWord.LIST_TYPE).append(poPackage.getClassName());
    sb.append(JavaKeyWord.LIST_TYPE_END);
    sb.append(Symbol.SPACE).append(JavaVarName.ASSERT_PARAM_SRC_LIST).append(Symbol.COMMA);
    sb.append(Symbol.SPACE).append(JavaKeyWord.LIST_TYPE).append(poPackage.getClassName());
    sb.append(JavaKeyWord.LIST_TYPE_END);
    sb.append(Symbol.SPACE).append(JavaVarName.ASSERT_PARAM_TARGET_LIST);
    sb.append(Symbol.BRACKET_RIGHT).append(Symbol.BRACE_LEFT).append(Symbol.ENTER_LINE);

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

    // 结束
    sb.append(JavaFormat.appendTab(tabIndex + 1));
    sb.append(Symbol.BRACE_RIGHT).append(Symbol.ENTER_LINE);
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
    // 注释
    sb.append(JavaFormat.appendTab(tabIndex + 1)).append(JavaKeyWord.ANNO_CLASS);
    sb.append(Symbol.ENTER_LINE);
    sb.append(JavaFormat.appendTab(tabIndex + 1)).append(JavaKeyWord.ANNO_CLASS_MID);
    sb.append(Symbol.SPACE).append(CodeComment.JUNIT_PARSE_MAP_COMMENT);
    sb.append(Symbol.ENTER_LINE);
    sb.append(JavaFormat.appendTab(tabIndex + 1)).append(JavaKeyWord.ANNO_CLASS_MID);
    sb.append(Symbol.ENTER_LINE);
    sb.append(JavaFormat.appendTab(tabIndex + 1)).append(JavaKeyWord.ANNO_CLASS_MID);
    sb.append(JavaKeyWord.METHOD_PARAM).append(JavaVarName.LIST_NAME);
    sb.append(Symbol.SPACE).append(CodeComment.JUNIT_PARSE_LIST_COMMENT);
    sb.append(Symbol.ENTER_LINE);
    sb.append(JavaFormat.appendTab(tabIndex + 1)).append(JavaKeyWord.ANNO_OVER);
    sb.append(Symbol.ENTER_LINE);

    // 方法定义
    sb.append(JavaFormat.appendTab(tabIndex + 1)).append(JavaKeyWord.PRIVATE);
    sb.append(Symbol.SPACE).append(JavaKeyWord.MAP_TYPE).append(Symbol.ANGLE_BRACKETS_LEFT);
    sb.append(JavaKeyWord.TYPE_STRING).append(Symbol.COMMA).append(Symbol.SPACE);
    sb.append(poPackage.getClassName()).append(Symbol.ANGLE_BRACKETS_RIGHT);
    sb.append(Symbol.SPACE).append(JavaMethodName.PARSE_MAP).append(Symbol.BRACKET_LEFT);
    sb.append(JavaKeyWord.LIST_TYPE).append(poPackage.getClassName());
    sb.append(JavaKeyWord.LIST_TYPE_END).append(Symbol.SPACE).append(JavaVarName.LIST_NAME);
    sb.append(Symbol.BRACKET_RIGHT).append(Symbol.BRACE_LEFT).append(Symbol.ENTER_LINE);

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

    // 方法的结束
    sb.append(JavaFormat.appendTab(tabIndex + 1)).append(Symbol.BRACE_RIGHT);
    sb.append(Symbol.ENTER_LINE);
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
    // 生成获取key的方法注释
    // 注释
    sb.append(JavaFormat.appendTab(tabIndex + 1)).append(JavaKeyWord.ANNO_CLASS);
    sb.append(Symbol.ENTER_LINE);
    sb.append(JavaFormat.appendTab(tabIndex + 1)).append(JavaKeyWord.ANNO_CLASS_MID);
    sb.append(Symbol.SPACE).append(CodeComment.JUNIT_PARSE_MAP_COMMENT);
    sb.append(Symbol.ENTER_LINE);
    sb.append(JavaFormat.appendTab(tabIndex + 1)).append(JavaKeyWord.ANNO_CLASS_MID);
    sb.append(Symbol.ENTER_LINE);
    sb.append(JavaFormat.appendTab(tabIndex + 1)).append(JavaKeyWord.ANNO_CLASS_MID);
    sb.append(JavaKeyWord.METHOD_PARAM).append(JavaVarName.PARAM_BEAN);
    sb.append(Symbol.SPACE).append(CodeComment.JUNIT_PARSE_KEY_PO_COMMENT);
    sb.append(Symbol.ENTER_LINE);
    sb.append(JavaFormat.appendTab(tabIndex + 1)).append(JavaKeyWord.ANNO_OVER);
    sb.append(Symbol.ENTER_LINE);

    // 方法声明
    sb.append(JavaFormat.appendTab(tabIndex + 1)).append(JavaKeyWord.PRIVATE);
    sb.append(Symbol.SPACE).append(JavaKeyWord.TYPE_STRING).append(Symbol.SPACE);
    sb.append(JavaMethodName.PARSE_MAP_KEY).append(Symbol.BRACKET_LEFT);
    sb.append(poPackage.getClassName()).append(Symbol.SPACE);
    sb.append(JavaVarName.PARAM_BEAN).append(Symbol.BRACKET_RIGHT);
    sb.append(Symbol.BRACE_LEFT).append(Symbol.ENTER_LINE);

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
    sb.append(Symbol.POINT).append(JavaMethodName.TOSTRING).append(Symbol.BRACKET_LEFT);
    sb.append(Symbol.BRACKET_RIGHT).append(Symbol.SEMICOLON).append(Symbol.ENTER_LINE);

    // 方法的结束
    sb.append(JavaFormat.appendTab(tabIndex + 1)).append(Symbol.BRACE_RIGHT);

    sb.append(Symbol.ENTER_LINE);
    sb.append(Symbol.ENTER_LINE);
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
    // 注释
    sb.append(JavaFormat.appendTab(tabIndex + 1)).append(JavaKeyWord.ANNO_CLASS);
    sb.append(Symbol.ENTER_LINE);
    sb.append(JavaFormat.appendTab(tabIndex + 1)).append(JavaKeyWord.ANNO_CLASS_MID);
    sb.append(Symbol.SPACE).append(CodeComment.JUNIT_ASSERT_DATA_VALUE);
    sb.append(Symbol.ENTER_LINE);
    sb.append(JavaFormat.appendTab(tabIndex + 1)).append(JavaKeyWord.ANNO_CLASS_MID);
    sb.append(Symbol.ENTER_LINE);
    sb.append(JavaFormat.appendTab(tabIndex + 1)).append(JavaKeyWord.ANNO_CLASS_MID);
    sb.append(JavaKeyWord.METHOD_PARAM).append(JavaVarName.ASSERT_DATA_SRC);
    sb.append(Symbol.SPACE).append(CodeComment.JUNIT_ASSERT_DATA_SRC);
    sb.append(Symbol.ENTER_LINE);
    sb.append(JavaFormat.appendTab(tabIndex + 1)).append(JavaKeyWord.ANNO_CLASS_MID);
    sb.append(JavaKeyWord.METHOD_PARAM).append(JavaVarName.ASSERT_DATA_TARGET);
    sb.append(Symbol.SPACE).append(CodeComment.JUNIT_ASSERT_DATA_TARGET);
    sb.append(Symbol.ENTER_LINE);
    sb.append(JavaFormat.appendTab(tabIndex + 1)).append(JavaKeyWord.ANNO_OVER);
    sb.append(Symbol.ENTER_LINE);

    // 方法声明
    sb.append(JavaFormat.appendTab(tabIndex + 1)).append(JavaKeyWord.PRIVATE);
    sb.append(Symbol.SPACE).append(JavaKeyWord.VOID).append(Symbol.SPACE);
    sb.append(JavaMethodName.ASSERT_DATA).append(Symbol.BRACKET_LEFT);
    sb.append(poPackage.getClassName()).append(Symbol.SPACE);
    sb.append(JavaVarName.ASSERT_DATA_SRC).append(Symbol.COMMA);
    sb.append(Symbol.SPACE);
    sb.append(poPackage.getClassName()).append(Symbol.SPACE);
    sb.append(JavaVarName.ASSERT_DATA_TARGET).append(Symbol.BRACKET_RIGHT);
    sb.append(Symbol.BRACE_LEFT).append(Symbol.ENTER_LINE);

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

    // 方法的结束
    sb.append(JavaFormat.appendTab(tabIndex + 1)).append(Symbol.BRACE_RIGHT);

    sb.append(Symbol.ENTER_LINE);
    sb.append(Symbol.ENTER_LINE);
  }
}
