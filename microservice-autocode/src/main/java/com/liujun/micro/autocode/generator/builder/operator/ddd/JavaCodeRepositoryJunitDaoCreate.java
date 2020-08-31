package com.liujun.micro.autocode.generator.builder.operator.ddd;

import com.liujun.micro.autocode.config.menuTree.DomainMenuTree;
import com.liujun.micro.autocode.config.menuTree.MenuNode;
import com.liujun.micro.autocode.config.menuTree.MenuTreePackagePath;
import com.liujun.micro.autocode.config.menuTree.MenuTreeProjectPath;
import com.liujun.micro.autocode.constant.Symbol;
import com.liujun.micro.autocode.entity.config.MethodInfo;
import com.liujun.micro.autocode.generator.builder.constant.*;
import com.liujun.micro.autocode.generator.builder.entity.GenerateCodeContext;
import com.liujun.micro.autocode.generator.builder.entity.ImportPackageInfo;
import com.liujun.micro.autocode.generator.builder.operator.GenerateCodeInf;
import com.liujun.micro.autocode.generator.builder.operator.utils.ImportPackageUtils;
import com.liujun.micro.autocode.generator.builder.operator.utils.MethodUtils;
import com.liujun.micro.autocode.generator.builder.operator.utils.TableColumnUtils;
import com.liujun.micro.autocode.generator.builder.utils.MenuTreeProcessUtil;
import com.liujun.micro.autocode.generator.database.constant.DatabaseTypeEnum;
import com.liujun.micro.autocode.generator.database.entity.TableColumnDTO;
import com.liujun.micro.autocode.generator.database.entity.TableInfoDTO;
import com.liujun.micro.autocode.generator.database.service.DatabaseValue;
import com.liujun.micro.autocode.generator.javalanguage.constant.JavaKeyWord;
import com.liujun.micro.autocode.generator.javalanguage.serivce.JavaFormat;
import com.liujun.micro.autocode.generator.javalanguage.serivce.NameProcess;
import com.liujun.micro.autocode.utils.FileUtils;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

/**
 * 生成单元测试相关的代码
 *
 * @since 2018年4月15日 下午4:11:42
 * @version 0.0.1
 * @author liujun
 */
public class JavaCodeRepositoryJunitDaoCreate implements GenerateCodeInf {

  /** 测试的标识 */
  private static final String DAO_TEST_NAME = "Test";

  /** 需要导入的包 */
  private static final String[] IMPORT_PKG =
      new String[] {
        "org.junit.*",
        "org.apache.commons.lang3.RandomStringUtils",
        "org.apache.commons.lang3.RandomUtils",
        "org.springframework.beans.factory.annotation.Autowired"
      };

  /** 注释 */
  private static final String DATABASE_DOC = "数据库操作单元测试";

  /** 测试方法的前缀 */
  private static final String JUNIT_METHOD_BEFORE = "test";

  @Override
  public void generateCode(GenerateCodeContext param) {

    Map<String, List<TableColumnDTO>> map = param.getColumnMapList();
    Iterator<Entry<String, List<TableColumnDTO>>> tableNameEntry = map.entrySet().iterator();
    while (tableNameEntry.hasNext()) {
      Entry<String, List<TableColumnDTO>> entry = tableNameEntry.next();
      List<TableColumnDTO> columnList = entry.getValue();
      String tableName = entry.getKey();

      // 得到类名
      String classNameSuffix = NameProcess.INSTANCE.toJavaClassName(tableName);

      // 获取当前主键列表
      List<TableColumnDTO> primaryKeyList = TableColumnUtils.getPrimaryKey(columnList);

      // 获取表信息
      TableInfoDTO tableInfo = param.getTableMap().get(entry.getKey());

      // 获取po的完整路径
      ImportPackageInfo poPackage =
          param.getPackageMap().get(GenerateCodeDomainKey.PERSIST_PO.getKey()).get(tableName);

      // 获取dao的完整路径
      ImportPackageInfo daoPackage =
          param.getPackageMap().get(GenerateCodeDomainKey.PERSIST_DAO.getKey()).get(tableName);

      // 首字母大写
      String className =
          DAO_TEST_NAME + NameProcess.INSTANCE.toJavaNameFirstUpper(daoPackage.getClassName());

      // 获取以java定义的package路径
      DomainMenuTree menuTree = param.getMenuTree();
      MenuNode daoNode = MenuTreePackagePath.getRepositoryDaoNode(menuTree);
      String javaDaoPackageStr = MenuTreeProcessUtil.outJavaPackage(daoNode);

      // 文件头定义
      StringBuilder sb =
          defineHead(poPackage, daoPackage, param.getMenuTree(), tableInfo, className);

      // 操作数据之前的初始化相关的工作
      beforeMethod(sb, poPackage, daoPackage, param.getTypeEnum(), columnList, primaryKeyList);

      // 配制的方法
      List<MethodInfo> methodList = param.getGenerateConfig().getGenerate().getCode();

      for (MethodInfo methodItem : methodList) {
        // 添加方法
        if (MethodTypeEnum.INSERT.getType().equals(methodItem.getOperator())) {
          insertMethod(sb, methodItem, poPackage, methodList);
        }
        // 修改方法
        else if (MethodTypeEnum.UPDATE.getType().equals(methodItem.getOperator())) {

        }
        // 数据查询
        else if (MethodTypeEnum.QUERY.getType().equals(methodItem.getOperator())) {

        }
      }

      // 最后执执行after的清理方法，也就是delete方法
      // 删除方法
      MethodInfo methodItem = MethodUtils.getDeleteMethod(methodList);
      if (null != methodItem) {}

      // 结束
      sb.append(Symbol.BRACE_RIGHT);

      // 定义mapper文件
      MenuNode mapperNode = MenuTreeProjectPath.getTestJavaNode(param.getProjectMenuTree());
      String baseJavaPath = MenuTreeProcessUtil.outPath(mapperNode);
      javaDaoPackageStr = baseJavaPath + Symbol.PATH + javaDaoPackageStr;

      // 将代码输出到文件中
      this.outFile(sb, param.getFileBasePath(), className, javaDaoPackageStr);
    }
  }

  /**
   * 文件头定义
   *
   * @param poPackage 数据库实体
   * @param daoPackage 数据库操作接口
   * @param menuTree 路径结构
   * @param tableInfo 数据库信息
   * @return
   */
  private StringBuilder defineHead(
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
    sb.append(JavaKeyWord.PACKAGE)
        .append(Symbol.SPACE)
        .append(junitDaoPackageStr)
        .append(Symbol.SEMICOLON)
        .append(Symbol.ENTER_LINE)
        .append(Symbol.ENTER_LINE);

    for (String importPackage : IMPORT_PKG) {
      sb.append(JavaKeyWord.IMPORT).append(Symbol.SPACE).append(importPackage);
      sb.append(Symbol.SEMICOLON).append(Symbol.ENTER_LINE);
    }

    // 导入数据库存储的包
    String poPackagePath = ImportPackageUtils.packageOut(poPackage);
    sb.append(JavaKeyWord.IMPORT)
        .append(Symbol.SPACE)
        .append(poPackagePath)
        .append(Symbol.SEMICOLON)
        .append(Symbol.ENTER_LINE);

    // 导入dao的包
    String daoPackagePath = ImportPackageUtils.packageOut(daoPackage);
    sb.append(JavaKeyWord.IMPORT)
        .append(Symbol.SPACE)
        .append(daoPackagePath)
        .append(Symbol.SEMICOLON)
        .append(Symbol.ENTER_LINE);

    sb.append(Symbol.ENTER_LINE);
    sb.append(Symbol.ENTER_LINE);

    // 添加类注释信息
    sb.append(JavaKeyWord.ANNO_CLASS).append(Symbol.ENTER_LINE);
    sb.append(JavaKeyWord.ANNO_CLASS_MID)
        .append(Symbol.SPACE)
        .append(tableInfo.getTableComment())
        .append(Symbol.BRACKET_LEFT)
        .append(tableInfo.getTableName())
        .append(Symbol.BRACKET_RIGHT)
        .append(DATABASE_DOC)
        .append(Symbol.ENTER_LINE);

    sb.append(JavaKeyWord.ANNO_CLASS_MID).append(Symbol.ENTER_LINE);

    sb.append(JavaKeyWord.DOC_VERSION).append(Symbol.ENTER_LINE);
    sb.append(JavaKeyWord.DOC_AUTH).append(Symbol.ENTER_LINE);
    sb.append(JavaKeyWord.ANNO_OVER).append(Symbol.ENTER_LINE);

    // 添加类的信息
    sb.append(JavaKeyWord.ClASS_START)
        .append(DAO_TEST_NAME)
        .append(outClassName)
        .append(Symbol.SPACE)
        .append(JavaKeyWord.EXTEND)
        .append(Symbol.SPACE)
        .append(JunitKey.TEST_PARENT)
        .append(Symbol.BRACE_LEFT)
        .append(Symbol.ENTER_LINE)
        .append(Symbol.ENTER_LINE);

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
  private void beforeMethod(
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
    sb.append(JavaMethodName.SET_PRIMARY_FIELD_COMMENT);
    sb.append(Symbol.ENTER_LINE);
    sb.append(JavaFormat.appendTab(tabNum + 1))
        .append(JavaKeyWord.ANNO_CLASS_MID)
        .append(Symbol.ENTER_LINE);
    sb.append(JavaFormat.appendTab(tabNum + 1))
        .append(JavaKeyWord.ANNO_CLASS_MID)
        .append(JavaVarName.METHOD_PARAM);
    sb.append(JavaVarName.PARAM_BEAN).append(Symbol.SPACE);
    sb.append(CodeCommentEnum.METHOD_PARAM_DOC);
    sb.append(Symbol.ENTER_LINE);
    sb.append(JavaFormat.appendTab(tabNum + 1)).append(JavaKeyWord.ANNO_OVER);
    sb.append(Symbol.ENTER_LINE);

    // 属性相关值的获取操作
    sb.append(JavaFormat.appendTab(tabNum + 1)).append(JavaKeyWord.PRIVATE).append(Symbol.SPACE);
    sb.append(JavaKeyWord.VOID).append(Symbol.SPACE);
    sb.append(JavaMethodName.SET_PRIMARY_FIELD).append(Symbol.BRACKET_LEFT);
    sb.append(poPackage.getClassName()).append(Symbol.SPACE);
    sb.append(JavaVarName.PARAM_BEAN).append(Symbol.BRACKET_RIGHT);
    sb.append(Symbol.BRACKET_RIGHT).append(Symbol.BRACE_LEFT).append(Symbol.ENTER_LINE);
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
    sb.append(JavaMethodName.SET_DATA_FIELD_COMMENT);
    sb.append(Symbol.ENTER_LINE);
    sb.append(JavaFormat.appendTab(tabNum + 1))
        .append(JavaKeyWord.ANNO_CLASS_MID)
        .append(Symbol.ENTER_LINE);
    sb.append(JavaFormat.appendTab(tabNum + 1))
        .append(JavaKeyWord.ANNO_CLASS_MID)
        .append(JavaVarName.METHOD_PARAM)
        .append(JavaVarName.PARAM_BEAN);
    sb.append(Symbol.SPACE).append(CodeCommentEnum.METHOD_PARAM_DOC);
    sb.append(Symbol.ENTER_LINE);
    sb.append(JavaFormat.appendTab(tabNum + 1)).append(JavaKeyWord.ANNO_OVER);
    sb.append(Symbol.ENTER_LINE);

    // 属性相关值的获取操作
    sb.append(JavaFormat.appendTab(tabNum + 1)).append(JavaKeyWord.PRIVATE).append(Symbol.SPACE);
    sb.append(JavaKeyWord.VOID).append(Symbol.SPACE);
    sb.append(JavaMethodName.SET_DATA_FIELD).append(Symbol.BRACKET_LEFT);
    sb.append(poPackage.getClassName()).append(Symbol.SPACE);
    sb.append(JavaVarName.PARAM_BEAN).append(Symbol.BRACKET_RIGHT);
    sb.append(Symbol.BRACKET_RIGHT).append(Symbol.BRACE_LEFT).append(Symbol.ENTER_LINE);
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
    // 添加前置方法
    sb.append(JavaFormat.appendTab(tabIndex + 1));
    sb.append(JunitKey.AUTOWIRED);
    sb.append(Symbol.ENTER_LINE);
    // 实体的spring注入
    sb.append(JavaFormat.appendTab(tabIndex + 1)).append(JavaKeyWord.PRIVATE);
    sb.append(Symbol.SPACE).append(daoPackage.getClassName());
    sb.append(JavaCodeRepositoryDaoInfCreate.DAO_SUFFIX).append(Symbol.SPACE);
    sb.append(JavaVarName.INSTANCE_NAME).append(Symbol.SEMICOLON).append(Symbol.ENTER_LINE);
    sb.append(Symbol.ENTER_LINE);

    // 定义属性的类型
    sb.append(JavaFormat.appendTab(tabIndex + 1)).append(JavaKeyWord.PRIVATE);
    sb.append(Symbol.SPACE).append(poPackage.getClassName());
    sb.append(Symbol.SPACE).append(JavaVarName.INSTANCE_NAME_PO);
    sb.append(Symbol.SEMICOLON).append(Symbol.ENTER_LINE);
    sb.append(Symbol.ENTER_LINE);

    // 定义属性的类型
    sb.append(JavaFormat.appendTab(tabIndex + 1)).append(JavaKeyWord.PRIVATE);
    sb.append(Symbol.SPACE).append(JavaKeyWord.TYPE_BOOLEAN);
    sb.append(Symbol.SPACE).append(JavaVarName.JUNIT_VAR_INSERT);
    sb.append(Symbol.SEMICOLON).append(Symbol.ENTER_LINE);
    sb.append(Symbol.ENTER_LINE);

    // 添加初始化实体对象的方法
    sb.append(JavaFormat.appendTab(tabIndex + 1))
        .append(JunitKey.JUNIT_BEFORE)
        .append(Symbol.ENTER_LINE);
    sb.append(JavaFormat.appendTab(tabIndex + 1)).append(JavaKeyWord.PUBLIC).append(Symbol.SPACE);
    sb.append(JavaKeyWord.STATIC).append(Symbol.SPACE);
    sb.append(JavaMethodName.BEFORE_SETPO).append(Symbol.BRACE_LEFT).append(Symbol.ENTER_LINE);
    sb.append(JavaFormat.appendTab(tabIndex + 2)).append(JavaVarName.INSTANCE_NAME_PO);
    sb.append(Symbol.SPACE).append(Symbol.EQUAL).append(Symbol.SPACE);
    sb.append(JavaKeyWord.THIS).append(Symbol.POINT).append(JavaMethodName.GET_DATA_METHOD);
    sb.append(Symbol.BRACKET_LEFT).append(Symbol.BRACKET_RIGHT);
    sb.append(Symbol.SEMICOLON).append(Symbol.ENTER_LINE);
    sb.append(JavaFormat.appendTab(tabIndex + 1))
        .append(Symbol.BRACE_RIGHT)
        .append(Symbol.ENTER_LINE);
    sb.append(Symbol.ENTER_LINE);

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
    sb.append(JavaVarName.PARAM_BEAN).append(Symbol.BRACKET_RIGHT).append(Symbol.SEMICOLON).append(Symbol.ENTER_LINE);

    // 设置普通属性
    sb.append(JavaFormat.appendTab(tabIndex + 2)).append(JavaKeyWord.THIS).append(Symbol.POINT);
    sb.append(JavaMethodName.SET_DATA_FIELD).append(Symbol.BRACKET_LEFT);
    sb.append(JavaVarName.PARAM_BEAN).append(Symbol.BRACKET_RIGHT).append(Symbol.SEMICOLON).append(Symbol.ENTER_LINE);

    // 返回语句
    sb.append(JavaFormat.appendTab(tabIndex + 2)).append(JavaKeyWord.RETURN);
    sb.append(Symbol.SPACE).append(JavaVarName.PARAM_BEAN);
    sb.append(Symbol.SEMICOLON).append(Symbol.ENTER_LINE);

    sb.append(JavaFormat.appendTab(tabIndex + 1))
        .append(Symbol.BRACE_RIGHT)
        .append(Symbol.ENTER_LINE);
    sb.append(Symbol.ENTER_LINE);
  }

  /**
   * 对属性进行设置
   *
   * @param sb 字符串
   * @param columnListParam 列信息
   */
  protected void setProperties(
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
      sb.append(JavaKeyWord.SET).append(setName).append(Symbol.BRACKET_LEFT);
      sb.append(value);

      sb.append(Symbol.BRACKET_RIGHT).append(Symbol.SEMICOLON).append(Symbol.ENTER_LINE);
    }
  }

  /**
   * 数据的操作方法
   *
   * @param sb
   */
  public void insertMethod(
      StringBuilder sb,
      MethodInfo method,
      ImportPackageInfo poPackage,
      List<MethodInfo> methodList) {
    // 1,检查当前的添加方法是否为批量添加
    boolean batchFlag = MethodUtils.checkBatch(method.getParamType());

    if (batchFlag) {
      // 执行批量的添加
      batchInsertMethod(sb, method, poPackage, methodList);
    } else {
      // 执行单个添加
      this.oneInsertMethod(sb, method);
    }
  }

  /**
   * 批量添加方法
   *
   * @param sb 集合
   * @param method 方法
   * @param poPackage 实体
   * @param methodList 方法集合
   */
  private void batchInsertMethod(
      StringBuilder sb,
      MethodInfo method,
      ImportPackageInfo poPackage,
      List<MethodInfo> methodList) {
    String methodName = NameProcess.INSTANCE.toJavaNameFirstLower(method.getName());
    int tabIndex = 1;

    // 方法的注释
    sb.append(JavaFormat.appendTab(tabIndex)).append(JavaKeyWord.ANNO_CLASS);
    sb.append(Symbol.ENTER_LINE);
    sb.append(JavaFormat.appendTab(tabIndex)).append(JavaKeyWord.ANNO_CLASS_MID);
    sb.append(Symbol.SPACE);
    sb.append(method.getComment()).append(Symbol.ENTER_LINE);
    sb.append(JavaFormat.appendTab(tabIndex)).append(JavaKeyWord.ANNO_OVER);
    sb.append(Symbol.ENTER_LINE);

    // 添加insert方法的定义
    sb.append(JavaFormat.appendTab(tabIndex)).append(JunitKey.ANNO_TEST).append(Symbol.ENTER_LINE);
    sb.append(JavaFormat.appendTab(tabIndex)).append(JavaKeyWord.PUBLIC).append(Symbol.SPACE);
    sb.append(JavaKeyWord.VOID).append(Symbol.SPACE).append(JUNIT_METHOD_BEFORE);
    sb.append(methodName).append(Symbol.BRACKET_LEFT).append(Symbol.BRACKET_RIGHT);
    sb.append(Symbol.BRACE_LEFT).append(Symbol.ENTER_LINE);

    // 1,定义批量插入数据的行数
    sb.append(JavaFormat.appendTab(tabIndex + 1)).append(JavaKeyWord.INT_TYPE).append(Symbol.SPACE);
    sb.append(JavaVarName.BATCH_INSERT_NAME).append(Symbol.SPACE).append(Symbol.EQUAL);
    sb.append(Symbol.SPACE).append(JavaVarValue.BATCH_NUM);
    sb.append(Symbol.SEMICOLON).append(Symbol.ENTER_LINE);

    // 定义集合
    sb.append(JavaFormat.appendTab(tabIndex + 1)).append(JavaKeyWord.LIST_TYPE);
    sb.append(poPackage.getClassName()).append(JavaKeyWord.LIST_TYPE_END);
    sb.append(Symbol.SPACE).append(JavaVarName.LIST_NAME);
    sb.append(Symbol.SPACE).append(Symbol.EQUAL).append(Symbol.SPACE);
    sb.append(JavaKeyWord.NEW).append(Symbol.SPACE);
    sb.append(JavaKeyWord.LIST_TYPE_ARRAYLIST).append(Symbol.BRACKET_LEFT);
    sb.append(JavaVarName.BATCH_INSERT_NAME).append(Symbol.BRACKET_RIGHT);
    sb.append(Symbol.SEMICOLON).append(Symbol.ENTER_LINE);

    // 循环进行数据的生成
    sb.append(JavaFormat.appendTab(tabIndex + 1)).append(JavaKeyWord.FOR_KEY).append(Symbol.SPACE);
    sb.append(Symbol.BRACKET_LEFT).append(JavaKeyWord.INT_TYPE).append(Symbol.SPACE);
    sb.append(JavaVarName.FOR_TEMP_INDEX).append(Symbol.SPACE).append(Symbol.EQUAL);
    sb.append(Symbol.SPACE).append(JavaVarValue.FOR_INDEX_START).append(Symbol.SEMICOLON);
    sb.append(Symbol.SPACE).append(JavaVarName.FOR_TEMP_INDEX).append(Symbol.SPACE);
    sb.append(Symbol.ANGLE_BRACKETS_LEFT).append(Symbol.SPACE);
    sb.append(JavaVarName.BATCH_INSERT_NAME);
    sb.append(Symbol.SEMICOLON).append(Symbol.SPACE).append(JavaVarName.FOR_TEMP_INDEX);
    sb.append(JavaVarValue.ADD_ADD).append(Symbol.BRACKET_RIGHT).append(Symbol.SPACE);
    sb.append(Symbol.BRACE_LEFT).append(Symbol.ENTER_LINE);

    // 数据循环插入
    sb.append(JavaFormat.appendTab(tabIndex + 2)).append(JavaVarName.LIST_NAME);
    sb.append(Symbol.POINT).append(JavaMethodName.LIST_ADD);
    sb.append(Symbol.BRACKET_LEFT).append(JavaMethodName.GET_DATA_METHOD);
    sb.append(Symbol.BRACKET_LEFT).append(Symbol.BRACKET_RIGHT);
    sb.append(Symbol.BRACKET_RIGHT).append(Symbol.SEMICOLON).append(Symbol.ENTER_LINE);

    // 循环结束
    sb.append(JavaFormat.appendTab(tabIndex + 1)).append(Symbol.BRACE_RIGHT);
    sb.append(Symbol.ENTER_LINE);

    // 方法调用
    sb.append(JavaFormat.appendTab(tabIndex + 1)).append(JavaKeyWord.INT_TYPE);
    sb.append(Symbol.SPACE).append(JavaVarName.INVOKE_METHOD_OPERATOR_RSP);
    sb.append(Symbol.SPACE).append(Symbol.EQUAL).append(Symbol.SPACE);
    sb.append(JavaVarName.INSTANCE_NAME).append(Symbol.POINT).append(method.getName());
    sb.append(Symbol.BRACKET_LEFT).append(JavaVarName.LIST_NAME);
    sb.append(Symbol.BRACKET_RIGHT).append(Symbol.SEMICOLON).append(Symbol.ENTER_LINE);

    // 进行插入结果的判断
    sb.append(JavaFormat.appendTab(tabIndex + 1)).append(JunitKey.ASSERT);
    sb.append(JavaVarName.BATCH_INSERT_NAME).append(Symbol.COMMA);
    sb.append(JavaVarName.INVOKE_METHOD_OPERATOR_RSP);
    sb.append(Symbol.BRACKET_RIGHT).append(Symbol.SEMICOLON).append(Symbol.ENTER_LINE);

    // 对当前插入标识进行设置，以在after中进行数据的删除操作
    sb.append(JavaFormat.appendTab(tabIndex + 1)).append(JavaVarName.JUNIT_VAR_INSERT);
    sb.append(Symbol.SPACE).append(Symbol.EQUAL).append(Symbol.SPACE);
    sb.append(Boolean.FALSE).append(Symbol.SEMICOLON).append(Symbol.ENTER_LINE);
    sb.append(Symbol.ENTER_LINE);

    // 检查并调用删除方法
    invokeDelete(sb, tabIndex, methodList);

    sb.append(JavaFormat.appendTab(tabIndex)).append(Symbol.BRACE_RIGHT);
    sb.append(Symbol.ENTER_LINE).append(Symbol.ENTER_LINE);
  }

  /**
   * 执行删除操作
   *
   * @param sb 数据集
   * @param tabIndex 当前的索引
   * @param methodList 方法集合
   */
  private void invokeDelete(StringBuilder sb, int tabIndex, List<MethodInfo> methodList) {
    // 1,获取删除方法，如果存在，则进行调用操作
    MethodInfo deleteMethod = MethodUtils.getDeleteMethod(methodList);

    if (null != deleteMethod) {
      // 循环调用删除方法
      sb.append(JavaFormat.appendTab(tabIndex + 1)).append(JavaKeyWord.FOR_KEY);
      sb.append(Symbol.SPACE);
      sb.append(Symbol.BRACKET_LEFT).append(JavaKeyWord.INT_TYPE).append(Symbol.SPACE);
      sb.append(JavaVarName.FOR_TEMP_INDEX).append(Symbol.SPACE).append(Symbol.EQUAL);
      sb.append(Symbol.SPACE).append(JavaVarValue.FOR_INDEX_START).append(Symbol.SEMICOLON);
      sb.append(Symbol.SPACE).append(JavaVarName.FOR_TEMP_INDEX).append(Symbol.SPACE);
      sb.append(Symbol.ANGLE_BRACKETS_LEFT).append(Symbol.SPACE);
      sb.append(JavaVarName.BATCH_INSERT_NAME);
      sb.append(Symbol.SEMICOLON).append(Symbol.SPACE).append(JavaVarName.FOR_TEMP_INDEX);
      sb.append(JavaVarValue.ADD_ADD).append(Symbol.BRACKET_RIGHT).append(Symbol.SPACE);
      sb.append(Symbol.BRACE_LEFT).append(Symbol.ENTER_LINE);

      // 调用数据删除
      sb.append(JavaFormat.appendTab(tabIndex + 2));
      sb.append(JavaKeyWord.INT_TYPE).append(Symbol.SPACE).append(JavaVarName.DELETE_METHOD_RSP);
      sb.append(Symbol.SPACE).append(Symbol.EQUAL).append(Symbol.SPACE);
      sb.append(JavaKeyWord.THIS).append(Symbol.POINT);
      sb.append(deleteMethod.getName()).append(Symbol.BRACKET_LEFT);
      sb.append(JavaVarName.LIST_NAME).append(Symbol.POINT).append(JavaKeyWord.GET);
      sb.append(Symbol.BRACKET_LEFT).append(JavaVarName.FOR_TEMP_INDEX);
      sb.append(Symbol.BRACKET_RIGHT);
      sb.append(Symbol.BRACKET_RIGHT);
      sb.append(Symbol.SEMICOLON).append(Symbol.ENTER_LINE);

      // 进行插入结果的判断
      sb.append(JavaFormat.appendTab(tabIndex + 2)).append(JunitKey.ASSERT);
      sb.append(JavaVarValue.DEFAULT_ADD_RSP).append(Symbol.COMMA);
      sb.append(JavaVarName.DELETE_METHOD_RSP);
      sb.append(Symbol.BRACKET_RIGHT).append(Symbol.SEMICOLON).append(Symbol.ENTER_LINE);

      sb.append(JavaFormat.appendTab(tabIndex + 1)).append(Symbol.BRACE_RIGHT);
      sb.append(Symbol.ENTER_LINE);
    }
  }

  /**
   * 单个添加方法
   *
   * @param sb 添加的对象
   * @param method 方法
   */
  private void oneInsertMethod(StringBuilder sb, MethodInfo method) {
    String methodName = NameProcess.INSTANCE.toJavaNameFirstLower(method.getName());

    sb.append(JavaFormat.appendTab(1)).append(JavaKeyWord.ANNO_CLASS).append(Symbol.ENTER_LINE);
    sb.append(JavaFormat.appendTab(1)).append(JavaKeyWord.ANNO_CLASS_MID);
    sb.append(Symbol.SPACE).append(method.getComment());
    sb.append(Symbol.ENTER_LINE);
    sb.append(JavaFormat.appendTab(1)).append(JavaKeyWord.ANNO_OVER).append(Symbol.ENTER_LINE);

    // 添加insert方法
    sb.append(JavaFormat.appendTab(1)).append(JunitKey.ANNO_TEST).append(Symbol.ENTER_LINE);
    sb.append(JavaFormat.appendTab(1)).append(JavaKeyWord.PUBLIC);
    sb.append(Symbol.SPACE).append(JavaKeyWord.VOID).append(Symbol.SPACE);
    sb.append(JUNIT_METHOD_BEFORE).append(methodName);
    sb.append(Symbol.BRACKET_LEFT).append(Symbol.BRACKET_RIGHT);
    sb.append(Symbol.BRACE_LEFT).append(Symbol.ENTER_LINE);
    // 方法调用
    sb.append(JavaFormat.appendTab(2)).append(JavaKeyWord.INT_TYPE).append(Symbol.SPACE);
    sb.append(JavaVarName.INVOKE_METHOD_OPERATOR_RSP).append(Symbol.SPACE);
    sb.append(Symbol.EQUAL).append(Symbol.SPACE).append(JavaVarName.INSTANCE_NAME);
    sb.append(Symbol.POINT).append(method.getName()).append(Symbol.BRACKET_LEFT);
    sb.append(JavaVarName.INSTANCE_NAME_PO).append(Symbol.BRACKET_RIGHT).append(Symbol.SEMICOLON);
    sb.append(Symbol.ENTER_LINE);

    // 进行插入结果的判断
    sb.append(JavaFormat.appendTab(2)).append(JunitKey.ASSERT);
    sb.append(JavaVarValue.DEFAULT_ADD_RSP).append(Symbol.COMMA);
    sb.append(JavaVarName.INVOKE_METHOD_OPERATOR_RSP);
    sb.append(Symbol.BRACKET_RIGHT).append(Symbol.SEMICOLON).append(Symbol.ENTER_LINE);

    // 对当前插入标识进行设置，以在after中进行数据的删除操作
    sb.append(JavaFormat.appendTab(2)).append(JavaVarName.JUNIT_VAR_INSERT).append(Symbol.SPACE);
    sb.append(Symbol.EQUAL).append(Symbol.SPACE).append(Boolean.TRUE);
    sb.append(Symbol.SEMICOLON).append(Symbol.ENTER_LINE);

    sb.append(JavaFormat.appendTab(1)).append(Symbol.BRACE_RIGHT);
    sb.append(Symbol.ENTER_LINE).append(Symbol.ENTER_LINE);
  }

  /**
   * 进行文件输出操作
   *
   * @param sb 输出字符
   * @param path 路径
   * @param className 类名
   * @param definePackage 定义的输出目录
   */
  private void outFile(StringBuilder sb, String path, String className, String definePackage) {
    // 获取源代码

    // 获取以路径/进行输出
    String javaPathStr = MenuTreeProcessUtil.outPath(definePackage);

    StringBuilder outPath = new StringBuilder();
    // 输出的基础路径
    outPath.append(path);
    outPath.append(Symbol.PATH);
    outPath.append(javaPathStr);

    // 文件名
    String fileName = className + JavaKeyWord.FILE_SUFFIX;

    // 文件输出操作
    FileUtils.writeFile(outPath.toString(), fileName, sb);
  }
}
