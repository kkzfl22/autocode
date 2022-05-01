package com.liujun.auto.generator.builder.ddd.code;

import com.liujun.auto.generator.builder.utils.MethodUtils;
import com.liujun.auto.generator.builder.utils.TableColumnUtils;
import com.liujun.auto.config.generate.entity.MethodInfo;
import com.liujun.auto.config.generate.entity.WhereInfo;
import com.liujun.auto.constant.MethodTypeEnum;
import com.liujun.auto.constant.MyBatisOperatorFlag;
import com.liujun.auto.constant.Symbol;
import com.liujun.auto.generator.builder.ddd.constant.GenerateCodePackageKey;
import com.liujun.auto.generator.builder.ddd.constant.MyBatisKey;
import com.liujun.auto.generator.builder.ddd.entity.GenerateCodeContext;
import com.liujun.auto.generator.builder.ddd.entity.ImportPackageInfo;
import com.liujun.auto.generator.convergence.TypeConvergence;
import com.liujun.auto.generator.database.constant.DatabaseTypeEnum;
import com.liujun.auto.generator.database.constant.DatabaseTypeSourceEnum;
import com.liujun.auto.generator.database.entity.DatabaseTypeMsgBO;
import com.liujun.auto.generator.database.entity.TableColumnDTO;
import com.liujun.auto.generator.database.entity.TableInfoDTO;
import com.liujun.auto.generator.database.service.datatype.DataTypeResource;
import com.liujun.auto.generator.javalanguage.serivce.JavaFormat;
import com.liujun.auto.generator.javalanguage.serivce.NameProcess;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * 生成mybatis数据库操作的xml文件
 *
 * @author liujun
 * @version 0.0.1
 */
public class GenerateJavaMybatisMapperXml {

  /** 实例对象 */
  public static final GenerateJavaMybatisMapperXml INSTANCE = new GenerateJavaMybatisMapperXml();

  /**
   * 进行mapperXML文件的生成操作
   *
   * @param tableName 表名信息
   * @param param 上下文参数信息
   * @param mapperXmlDoc mapper的注释
   * @return 生成的mapper的xml文件
   */
  public StringBuilder generateMapperXml(
      String tableName, GenerateCodeContext param, String mapperXmlDoc) {
    List<TableColumnDTO> columnList = param.getColumnMapList().get(tableName);
    List<TableColumnDTO> primaryKeyList = TableColumnUtils.getPrimaryKeyDefaultOne(columnList);
    // 获取po的完整路径
    ImportPackageInfo poPackage = param.getPkg(tableName, GenerateCodePackageKey.PERSIST_PO);
    // 获取dao的完整路径
    ImportPackageInfo daoPackage = param.getPkg(tableName, GenerateCodePackageKey.PERSIST_DAO);

    List<MethodInfo> methodList = param.getGenerateConfig().getGenerate().getMethodList();
    Map<String, TableColumnDTO> columnMap = param.getColumnMap().get(tableName);
    DatabaseTypeEnum typeEnum = param.getTypeEnum();
    TableInfoDTO tableMsg = param.getTableMap().get(tableName);

    // 生成的的结果集的id
    String resultMapId =
        NameProcess.INSTANCE.toFieldName(tableName) + MyBatisKey.RESULT_SUFFIX_NAME;

    // 进行头的定义
    StringBuilder sb = defineMapperHead(tableMsg, tableName, daoPackage, mapperXmlDoc);

    // 定义查询结果集
    queryResponse(sb, columnList, primaryKeyList, methodList, poPackage, resultMapId);

    for (MethodInfo methodItem : methodList) {
      // 添加方法
      if (MethodTypeEnum.INSERT.getType().equals(methodItem.getOperator())) {
        sb.append(insertMethod(tableMsg, methodItem, poPackage, columnList, typeEnum));
      }
      // 数据库修改
      else if (MethodTypeEnum.UPDATE.getType().equals(methodItem.getOperator())) {
        updateMethod(
            sb, tableMsg, methodItem, poPackage, columnList, columnMap, primaryKeyList, typeEnum);
      }
      // 数据删除
      else if (MethodTypeEnum.DELETE.getType().equals(methodItem.getOperator())) {
        deleteMethod(sb, tableMsg, methodItem, poPackage, columnMap, primaryKeyList, typeEnum);
      }
      // 数据查询
      else if (MethodTypeEnum.QUERY_PAGE.getType().equals(methodItem.getOperator())
          || MethodTypeEnum.QUERY.getType().equals(methodItem.getOperator())) {
        this.queryMethod(
            sb,
            tableMsg,
            methodItem,
            resultMapId,
            poPackage,
            columnList,
            columnMap,
            primaryKeyList,
            typeEnum);
      }
    }

    // 文件输出
    mapperFinish(sb);

    return sb;
  }

  /**
   * mapper文件头
   *
   * @param tableMsg 表信息
   * @param tableName 表名
   * @param daoPackageInfo 数据库定义信息
   * @param docComment 注释信息
   * @return
   */
  private StringBuilder defineMapperHead(
      TableInfoDTO tableMsg,
      String tableName,
      ImportPackageInfo daoPackageInfo,
      String docComment) {
    StringBuilder sb = new StringBuilder();

    // 定义头
    sb.append(MyBatisKey.HEAD).append(Symbol.ENTER_LINE);
    // xml文件头定义
    sb.append(MyBatisKey.DEFINE).append(Symbol.ENTER_LINE);
    sb.append(Symbol.ENTER_LINE);
    // xml注释
    sb.append(MyBatisKey.DOC_START)
        .append(tableMsg.getTableComment())
        .append(Symbol.BRACKET_LEFT)
        .append(tableName)
        .append(Symbol.BRACKET_RIGHT)
        .append(docComment)
        .append(MyBatisKey.DOC_END)
        .append(Symbol.ENTER_LINE);

    // 定义命名空间
    sb.append(MyBatisKey.NAMESPACE_START)
        .append(daoPackageInfo.packageOut())
        .append(Symbol.QUOTE)
        .append(MyBatisKey.END)
        .append(Symbol.ENTER_LINE);

    return sb;
  }

  /**
   * 添加where条件操作
   *
   * @param whereColumn 列信息
   * @param columnMap 表表
   * @param primary 主键
   * @param tabNum 添加的tab次数
   * @return 生成的条件
   */
  private String addWhere(
      List<WhereInfo> whereColumn,
      Map<String, TableColumnDTO> columnMap,
      List<TableColumnDTO> primary,
      int tabNum,
      DatabaseTypeEnum typeEnum) {

    // 1,如果配制了where条件，则使用
    if (whereColumn != null && !whereColumn.isEmpty()) {
      return addCfgWhere(whereColumn, columnMap, tabNum, typeEnum);
    }

    // 如果未配制使用主键
    return addWhere(primary, tabNum, typeEnum);
  }

  /**
   * 使用数据操作的条件
   *
   * @param whereColumn 使用配制列作查询条件
   * @param columnMap 列信息
   * @param tabNum 当前索引
   * @return
   */
  private String addCfgWhere(
      List<WhereInfo> whereColumn,
      Map<String, TableColumnDTO> columnMap,
      int tabNum,
      DatabaseTypeEnum typeEnum) {

    StringBuilder sb = new StringBuilder();

    WhereInfo column = null;

    for (int i = 0; i < whereColumn.size(); i++) {

      column = whereColumn.get(i);
      String columnName = column.getSqlColumn();

      String javaName = NameProcess.INSTANCE.toFieldName(columnName);

      // 仅首次不添加and，其他都 需要添加and
      boolean addAndFlag = i != 0;

      // 如果当前为判断相等
      if (MyBatisOperatorFlag.EQUAL.equals(column.getOperatorFlag())) {
        this.conditionEquals(sb, columnName, columnMap, tabNum + 1, addAndFlag, typeEnum);
      }
      // 如果当前为in，则需要进行遍历
      else if (MyBatisOperatorFlag.IN.equals(column.getOperatorFlag())) {
        this.conditionIn(sb, columnName, javaName, columnMap, tabNum + 1, addAndFlag);
      }
    }
    return sb.toString();
  }

  /**
   * 进行等于的关键字处理
   *
   * @param sb 字符信息
   * @param columnName 类名
   * @param columnMap 列属性
   * @param tabNum 制表符个数
   * @param addAndFlag 添加and的关键字的标识
   */
  private void conditionEquals(
      StringBuilder sb,
      String columnName,
      Map<String, TableColumnDTO> columnMap,
      int tabNum,
      boolean addAndFlag,
      DatabaseTypeEnum typeEnum) {

    TableColumnDTO tableMapper = columnMap.get(columnName);

    // 添加列注释信息
    sb.append(JavaFormat.appendTab(tabNum))
        .append(MyBatisKey.DOC_START)
        .append(tableMapper.getColumnMsg())
        .append(MyBatisKey.DOC_END)
        .append(Symbol.ENTER_LINE);

    String javaName = NameProcess.INSTANCE.toFieldName(columnName);
    String typeName = TypeConvergence.dbTypeParseMyBatis(typeEnum, tableMapper);

    // 添加条件的连接符
    sb.append(JavaFormat.appendTab(tabNum));
    if (addAndFlag) {
      sb.append(MyBatisKey.KEY_AND);
      sb.append(Symbol.SPACE);
    }

    // 添加列相关的信息
    sb.append(columnName)
        .append(Symbol.SPACE)
        .append(Symbol.EQUAL)
        .append(Symbol.SPACE)
        .append(Symbol.POUND)
        .append(Symbol.BRACE_LEFT)
        .append(javaName)
        .append(MyBatisKey.JDBC_TYPE)
        .append(typeName)
        .append(Symbol.BRACE_RIGHT)
        .append(Symbol.ENTER_LINE);
  }

  /**
   * 进行关键字in的处理
   *
   * @param sb 字符信息
   * @param columnName 类名
   * @param javaFieldName 属性名
   * @param columnMap 列属性
   * @param tabNum 制表符个数
   * @param addAndFlag 添加and关键字的标识符
   */
  private void conditionIn(
      StringBuilder sb,
      String columnName,
      String javaFieldName,
      Map<String, TableColumnDTO> columnMap,
      int tabNum,
      boolean addAndFlag) {

    // 1,提取在实体中in的名称
    String fieldName = GenerateJavaBean.getInConditionName(javaFieldName);

    TableColumnDTO tableMapper = columnMap.get(columnName);

    // 添加列注释信息
    sb.append(JavaFormat.appendTab(tabNum))
        .append(MyBatisKey.DOC_START)
        .append(tableMapper.getColumnMsg())
        .append(MyBatisKey.DOC_END)
        .append(Symbol.ENTER_LINE);

    // 添加条件的连接符
    sb.append(JavaFormat.appendTab(tabNum));

    if (addAndFlag) {
      sb.append(MyBatisKey.KEY_AND);
      sb.append(Symbol.SPACE);
    }

    // 使用in关键字
    sb.append(columnName)
        .append(Symbol.SPACE)
        .append(MyBatisOperatorFlag.IN.getOperator())
        .append(Symbol.SPACE)
        .append(Symbol.ENTER_LINE);

    // 添加xml中的遍历头
    sb.append(JavaFormat.appendTab(tabNum))
        .append(MyBatisKey.FOREACH_LIST_START)
        .append(fieldName)
        .append(MyBatisKey.FOREACH_ITEM)
        .append(MyBatisKey.FOREACH_ITEM_NAME)
        .append(MyBatisKey.FOREACH_CONDITION_IN)
        .append(Symbol.ENTER_LINE);

    sb.append(JavaFormat.appendTab(tabNum + 1))
        .append(Symbol.POUND)
        .append(Symbol.BRACE_LEFT)
        .append(MyBatisKey.FOREACH_ITEM_NAME)
        .append(Symbol.BRACE_RIGHT);

    // 换行符
    sb.append(Symbol.ENTER_LINE);

    // 添加xml中的遍历头
    sb.append(JavaFormat.appendTab(tabNum)).append(MyBatisKey.FOREACH_LIST_END);
    sb.append(Symbol.ENTER_LINE);
  }

  /**
   * 使用数据操作的条件
   *
   * @param columnList 主键列表明
   * @param tabNum 当前索引
   * @return
   */
  private String addWhere(List<TableColumnDTO> columnList, int tabNum, DatabaseTypeEnum typeEnum) {

    StringBuilder sb = new StringBuilder();

    TableColumnDTO column = null;

    for (int i = 0; i < columnList.size(); i++) {
      column = columnList.get(i);
      String columnName = column.getColumnName();
      String javaName = NameProcess.INSTANCE.toFieldName(columnName);
      String typeName = TypeConvergence.dbTypeParseMyBatis(typeEnum, column);

      // 定义输出列注释
      sb.append(JavaFormat.appendTab(tabNum + 1))
          .append(MyBatisKey.DOC_START)
          .append(column.getColumnMsg())
          .append(MyBatisKey.DOC_END)
          .append(Symbol.ENTER_LINE);

      sb.append(JavaFormat.appendTab(tabNum + 1));
      // 添加条件的连接符
      if (i > 0) {
        sb.append(MyBatisKey.KEY_AND);
        sb.append(Symbol.SPACE);
      }

      // 添加列相关的信息
      sb.append(columnName)
          .append(Symbol.SPACE)
          .append(Symbol.EQUAL)
          .append(Symbol.SPACE)
          .append(Symbol.POUND)
          .append(Symbol.BRACE_LEFT)
          .append(javaName)
          .append(MyBatisKey.JDBC_TYPE)
          .append(typeName)
          .append(Symbol.BRACE_RIGHT)
          .append(Symbol.ENTER_LINE);
    }
    return sb.toString();
  }

  /**
   * 进行类型的判断
   *
   * @param tableMapper
   * @param javaPropertiesName
   */
  private String switchType(TableColumnDTO tableMapper, String javaPropertiesName) {

    String switchStr = null;
    // 检查类型是否为varchar类型
    DatabaseTypeMsgBO typeBo =
        DataTypeResource.INSTANCE.getTargetTypeinfo(
            DatabaseTypeEnum.MYSQL, DatabaseTypeSourceEnum.DATABASE_TYPE_VARCHAR);

    boolean varcharFlag = typeBo.getDbType().equalsIgnoreCase(tableMapper.getDataType());

    // 检查类型是否为timestamp类型
    DatabaseTypeMsgBO timestampBo =
        DataTypeResource.INSTANCE.getTargetTypeinfo(
            DatabaseTypeEnum.MYSQL, DatabaseTypeSourceEnum.DATABASE_TYPE_TIMESTAMP);

    boolean timestampFlag = timestampBo.getDbType().equalsIgnoreCase(tableMapper.getDataType());

    // 检查char类型
    DatabaseTypeMsgBO charBo =
        DataTypeResource.INSTANCE.getTargetTypeinfo(
            DatabaseTypeEnum.MYSQL, DatabaseTypeSourceEnum.DATABASE_TYPE_CHAR);

    boolean charFlag = charBo.getDbType().equalsIgnoreCase(tableMapper.getDataType());

    if (varcharFlag || timestampFlag || charFlag) {
      switchStr =
          javaPropertiesName
              + Symbol.SPACE
              + Symbol.EQUAL_NOT
              + Symbol.SPACE
              + MyBatisKey.NULL_VALUE
              + Symbol.SPACE
              + MyBatisKey.KEY_AND
              + Symbol.SPACE
              + MyBatisKey.EMPTY_VALUE
              + Symbol.SPACE
              + Symbol.EQUAL_NOT
              + Symbol.SPACE
              + javaPropertiesName;
    } else {
      switchStr =
          javaPropertiesName
              + Symbol.SPACE
              + Symbol.EQUAL_NOT
              + Symbol.SPACE
              + MyBatisKey.NULL_VALUE;
    }

    return switchStr;
  }

  /**
   * 查询返回的结果集定义
   *
   * @param sb 构建的返回对象
   * @param columnList 列对象
   * @param primaryKeyList 主键
   * @param methodList 方法
   * @param poPackageInfo 实体包的定义
   * @param resultMapId 返回的结果集定义的id
   */
  private void queryResponse(
      StringBuilder sb,
      List<TableColumnDTO> columnList,
      List<TableColumnDTO> primaryKeyList,
      List<MethodInfo> methodList,
      ImportPackageInfo poPackageInfo,
      String resultMapId) {

    // 1,检查当间是否存在查询方法
    boolean queryFlag = checkQueryMethod(methodList);
    // 如果当前不存在查询方法，则不用生成相关的返回
    if (!queryFlag) {
      return;
    }

    // 定义resultmap信息
    sb.append(JavaFormat.appendTab(1)).append(MyBatisKey.RESULT_MAP);
    // 输出实体信息
    sb.append(poPackageInfo.packageOut()).append(Symbol.QUOTE);
    sb.append(Symbol.SPACE).append(MyBatisKey.ID).append(Symbol.EQUAL);
    sb.append(Symbol.QUOTE).append(resultMapId);
    sb.append(Symbol.QUOTE).append(MyBatisKey.END).append(Symbol.ENTER_LINE);

    // 输出相关的返回结果集中的字段信息
    sb.append(outResultMap(columnList, primaryKeyList));

    // 结束
    sb.append(JavaFormat.appendTab(1)).append(MyBatisKey.RESULT_MAP_END).append(Symbol.ENTER_LINE);
  }

  /**
   * 输出相关的字段信息
   *
   * @param columnList
   * @param primaryKeyList
   * @return
   */
  public String outResultMap(List<TableColumnDTO> columnList, List<TableColumnDTO> primaryKeyList) {
    StringBuilder sb = new StringBuilder();

    // 进行主键列的输出
    this.primaryProc(sb, primaryKeyList, columnList);

    // 其他正常的列处理
    this.normalColumnProc(sb, columnList);

    return sb.toString();
  }

  /**
   * 普通的列处理
   *
   * @param sb 字符
   * @param delKeyList 删除的列信息
   */
  private void normalColumnProc(StringBuilder sb, List<TableColumnDTO> delKeyList) {
    // 定义查询列信息
    for (int i = 0; i < delKeyList.size(); i++) {
      // 输出注释
      sb.append(JavaFormat.appendTab(2))
          .append(MyBatisKey.DOC_START)
          .append(delKeyList.get(i).getColumnMsg())
          .append(MyBatisKey.DOC_END)
          .append(Symbol.ENTER_LINE);

      // 得到java输出的名称
      String javaName = NameProcess.INSTANCE.toFieldName(delKeyList.get(i).getColumnName());

      // 输出列信息
      sb.append(JavaFormat.appendTab(2))
          .append(MyBatisKey.RESULT)
          .append(MyBatisKey.PROPERTY)
          .append(Symbol.EQUAL)
          .append(Symbol.QUOTE)
          .append(javaName)
          .append(Symbol.QUOTE)
          .append(Symbol.SPACE)
          .append(MyBatisKey.COLUMN)
          .append(Symbol.EQUAL)
          .append(Symbol.QUOTE)
          .append(delKeyList.get(i).getColumnName())
          .append(Symbol.QUOTE)
          .append(MyBatisKey.OVER)
          .append(Symbol.ENTER_LINE);
    }
  }

  /**
   * 主键输出
   *
   * @param sb 输出信息
   * @param primaryKeyList 主键信息
   * @param delKeyList 所有列信息
   */
  private void primaryProc(
      StringBuilder sb, List<TableColumnDTO> primaryKeyList, List<TableColumnDTO> delKeyList) {
    // 定义主键信息
    for (TableColumnDTO tableColumn : primaryKeyList) {

      // 定义输出列注释
      sb.append(JavaFormat.appendTab(2))
          .append(MyBatisKey.DOC_START)
          .append(tableColumn.getColumnMsg())
          .append(MyBatisKey.DOC_END)
          .append(Symbol.ENTER_LINE);

      // 得到java输出的名称
      String javaName = NameProcess.INSTANCE.toFieldName(tableColumn.getColumnName());

      // 定义列的对应
      sb.append(JavaFormat.appendTab(2))
          .append(MyBatisKey.START)
          .append(MyBatisKey.ID_PROPERTY)
          .append(Symbol.EQUAL)
          .append(Symbol.QUOTE)
          .append(javaName)
          .append(Symbol.QUOTE)
          .append(Symbol.SPACE)
          .append(MyBatisKey.COLUMN)
          .append(Symbol.EQUAL)
          .append(Symbol.QUOTE)
          .append(tableColumn.getColumnName())
          .append(Symbol.QUOTE)
          .append(MyBatisKey.OVER)
          .append(Symbol.ENTER_LINE);

      // 是否为自增长主键
      if (tableColumn.getAutoIncrement()) {
        // 去队主键
        delKeyList.remove(tableColumn);
      }
    }
  }

  /**
   * 检查当前是否存在查询方法
   *
   * @param methodList 方法信息
   * @return true 当前存在查询方法,false 当前不存在查询方法
   */
  private boolean checkQueryMethod(List<MethodInfo> methodList) {
    if (null == methodList) {
      return false;
    }

    for (MethodInfo methodItem : methodList) {
      if (methodItem.getOperator().equals(MethodTypeEnum.QUERY.getType())) {
        return true;
      }
    }

    return false;
  }

  /**
   * 添加方法
   *
   * @param tableMsg 表信息
   * @param methodInfo 方法
   * @param poPackageInfo PO实体信息
   * @param columnList 列信息
   */
  public String insertMethod(
      TableInfoDTO tableMsg,
      MethodInfo methodInfo,
      ImportPackageInfo poPackageInfo,
      List<TableColumnDTO> columnList,
      DatabaseTypeEnum typeEnum) {
    StringBuilder sb = new StringBuilder();

    // 添加操作的注释
    sb.append(JavaFormat.appendTab(1))
        .append(MyBatisKey.DOC_START)
        .append(tableMsg.getTableComment())
        .append(Symbol.BRACKET_LEFT)
        .append(tableMsg.getTableName())
        .append(Symbol.BRACKET_RIGHT)
        .append(methodInfo.getComment())
        .append(MyBatisKey.DOC_END)
        .append(Symbol.ENTER_LINE);

    String poClassPath = poPackageInfo.packageOut();

    // 插入的开始语句
    sb.append(JavaFormat.appendTab(1))
        .append(MyBatisKey.INSERT_XML_START)
        .append(methodInfo.getName())
        .append(MyBatisKey.TYPE_XML)
        .append(poClassPath)
        .append(Symbol.QUOTE)
        .append(MyBatisKey.END)
        .append(Symbol.ENTER_LINE);

    // 检查当前是否为批量操作
    boolean batchFlag = MethodUtils.checkBatch(methodInfo.getParamType());

    // 进行数所在的插入操作
    sb.append(insertContext(columnList, typeEnum, batchFlag));

    sb.append(JavaFormat.appendTab(1)).append(MyBatisKey.INSERT_XML_END).append(Symbol.ENTER_LINE);
    sb.append(Symbol.ENTER_LINE);
    sb.append(Symbol.ENTER_LINE);

    return sb.toString();
  }

  /**
   * 插入的信息
   *
   * @param columnList 列信息
   * @param typeEnum 当前库的枚举类型信息
   * @param batchFlag 是否为批量添加的标识
   * @return
   */
  public String insertContext(
      List<TableColumnDTO> columnList, DatabaseTypeEnum typeEnum, boolean batchFlag) {
    StringBuilder sb = new StringBuilder();

    // 插入语句的开始
    sb.append(JavaFormat.appendTab(2))
        .append(MyBatisKey.INSERT_SQL_START)
        .append(columnList.get(0).getTableName())
        .append(Symbol.SPACE)
        .append(Symbol.ENTER_LINE);

    // 清除多余的逗号
    sb.append(JavaFormat.appendTab(2)).append(MyBatisKey.TRIM_XML_START).append(Symbol.ENTER_LINE);

    for (int i = 0; i < columnList.size(); i++) {
      TableColumnDTO tableMapper = columnList.get(i);
      String columnName = tableMapper.getColumnName();
      // 添加表注释信息
      sb.append(JavaFormat.appendTab(3))
          .append(MyBatisKey.DOC_START)
          .append(tableMapper.getColumnMsg())
          .append(MyBatisKey.DOC_END)
          .append(Symbol.ENTER_LINE);

      // 列不再判断,直接写入列
      sb.append(JavaFormat.appendTab(3)).append(columnName).append(Symbol.COMMA);

      sb.append(Symbol.ENTER_LINE);
    }
    sb.append(JavaFormat.appendTab(2)).append(MyBatisKey.TRIM_XML_END).append(Symbol.ENTER_LINE);
    sb.append(JavaFormat.appendTab(2));
    sb.append(MyBatisKey.INSERT_SQL_VALUE_KEY);
    sb.append(Symbol.ENTER_LINE);

    if (batchFlag) {
      insertBatch(sb, columnList, typeEnum);
    } else {
      // 单行数据添加
      this.insertOne(sb, columnList, typeEnum);
    }

    return sb.toString();
  }

  /**
   * 单行数据添加
   *
   * @param sb 数据集
   * @param columnList 列信息
   */
  private void insertBatch(
      StringBuilder sb, List<TableColumnDTO> columnList, DatabaseTypeEnum typeEnum) {
    // 添加xml中的遍历头
    sb.append(JavaFormat.appendTab(2))
        .append(MyBatisKey.FOREACH_LIST_START)
        .append(MyBatisKey.FOREACH_LIST_DEFAULT_NAME)
        .append(MyBatisKey.FOREACH_ITEM)
        .append(MyBatisKey.FOREACH_ITEM_NAME)
        .append(MyBatisKey.FOREACH_BATCH_INSERT_FINISH)
        .append(Symbol.ENTER_LINE);
    // 生成批量的SQL
    for (int i = 0; i < columnList.size(); i++) {
      TableColumnDTO tableMapper = columnList.get(i);
      String javaName = NameProcess.INSTANCE.toFieldName(tableMapper.getColumnName());
      String typeName = TypeConvergence.dbTypeParseMyBatis(typeEnum, tableMapper);
      // 添加列注释信息
      sb.append(JavaFormat.appendTab(3))
          .append(MyBatisKey.DOC_START)
          .append(tableMapper.getColumnMsg())
          .append(MyBatisKey.DOC_END)
          .append(Symbol.ENTER_LINE);

      sb.append(JavaFormat.appendTab(3))
          .append(Symbol.POUND)
          .append(Symbol.BRACE_LEFT)
          .append(MyBatisKey.FOREACH_ITEM_NAME)
          .append(Symbol.POINT)
          .append(javaName)
          .append(MyBatisKey.JDBC_TYPE)
          .append(typeName)
          .append(Symbol.BRACE_RIGHT);

      if (i != columnList.size() - 1) {
        sb.append(Symbol.COMMA);
      }
      // 换行符
      sb.append(Symbol.ENTER_LINE);
    }

    // 添加xml中的遍历头
    sb.append(JavaFormat.appendTab(2)).append(MyBatisKey.FOREACH_LIST_END);
    sb.append(Symbol.ENTER_LINE);
  }

  /**
   * 单行数据添加
   *
   * @param sb 数据集
   * @param columnList 列信息
   */
  private void insertOne(
      StringBuilder sb, List<TableColumnDTO> columnList, DatabaseTypeEnum typeEnum) {
    sb.append(JavaFormat.appendTab(2))
        .append(MyBatisKey.TRIM_VALUE_XML_START)
        .append(Symbol.ENTER_LINE);

    for (int i = 0; i < columnList.size(); i++) {
      TableColumnDTO tableMapper = columnList.get(i);
      String javaName = NameProcess.INSTANCE.toFieldName(tableMapper.getColumnName());
      String typeName = TypeConvergence.dbTypeParseMyBatis(typeEnum, tableMapper);
      // 添加列注释信息
      sb.append(JavaFormat.appendTab(3))
          .append(MyBatisKey.DOC_START)
          .append(tableMapper.getColumnMsg())
          .append(MyBatisKey.DOC_END)
          .append(Symbol.ENTER_LINE);

      sb.append(JavaFormat.appendTab(3))
          .append(Symbol.POUND)
          .append(Symbol.BRACE_LEFT)
          .append(javaName)
          .append(MyBatisKey.JDBC_TYPE)
          .append(typeName)
          .append(Symbol.BRACE_RIGHT)
          .append(Symbol.COMMA);

      // 换行符
      sb.append(Symbol.ENTER_LINE);
    }

    sb.append(JavaFormat.appendTab(2)).append(MyBatisKey.TRIM_XML_END).append(Symbol.ENTER_LINE);
  }

  /**
   * 进行生成修改数据方法
   *
   * @param sb 构建的SQL对象
   * @param tableMsg 表对象
   * @param methodInfo 方法信息
   * @param poPackageInfo 数据库实体信息
   * @param columnList 所有列
   * @param columnMap 处理列
   * @param primaryKeyList 主键列
   */
  private void updateMethod(
      StringBuilder sb,
      TableInfoDTO tableMsg,
      MethodInfo methodInfo,
      ImportPackageInfo poPackageInfo,
      List<TableColumnDTO> columnList,
      Map<String, TableColumnDTO> columnMap,
      List<TableColumnDTO> primaryKeyList,
      DatabaseTypeEnum typeEnum) {

    List<TableColumnDTO> copyColumnList = new ArrayList<>(columnList);
    // 数据不能修改主键
    copyColumnList.removeAll(primaryKeyList);

    // 修改
    sb.append(JavaFormat.appendTab(1)).append(MyBatisKey.DOC_START);
    sb.append(tableMsg.getTableComment()).append(Symbol.BRACKET_LEFT);
    sb.append(tableMsg.getTableName()).append(Symbol.BRACKET_RIGHT);
    sb.append(methodInfo.getComment()).append(MyBatisKey.DOC_END).append(Symbol.ENTER_LINE);

    // po的类定义
    String poClassPath = poPackageInfo.packageOut();
    sb.append(JavaFormat.appendTab(1)).append(MyBatisKey.UPDATE_XML_START);
    sb.append(methodInfo.getName()).append(MyBatisKey.TYPE_XML).append(poClassPath);
    sb.append(Symbol.QUOTE).append(MyBatisKey.END).append(Symbol.ENTER_LINE);

    // update SQL信息
    sb.append(JavaFormat.appendTab(2)).append(MyBatisKey.UPDATE_SQL_START);
    sb.append(Symbol.SPACE).append(tableMsg.getTableName()).append(Symbol.ENTER_LINE);
    // 设置
    sb.append(JavaFormat.appendTab(2)).append(MyBatisKey.UPDATE_SET).append(Symbol.ENTER_LINE);

    // 列设置操作
    sb.append(this.updateSet(copyColumnList, typeEnum));

    sb.append(JavaFormat.appendTab(2)).append(MyBatisKey.UPDATE_SET_END).append(Symbol.ENTER_LINE);
    sb.append(JavaFormat.appendTab(2)).append(MyBatisKey.WHERE_START).append(Symbol.ENTER_LINE);
    // 选择条件
    sb.append(this.addWhere(methodInfo.getWhereInfo(), columnMap, primaryKeyList, 2, typeEnum));

    sb.append(JavaFormat.appendTab(2)).append(MyBatisKey.WHERE_END).append(Symbol.ENTER_LINE);
    sb.append(JavaFormat.appendTab(1)).append(MyBatisKey.UPDATE_XML_END).append(Symbol.ENTER_LINE);

    sb.append(Symbol.ENTER_LINE);
  }

  /**
   * 修改的列信息
   *
   * @param typeEnum 类型
   * @param copyColumnList 列信息
   */
  public String updateSet(List<TableColumnDTO> copyColumnList, DatabaseTypeEnum typeEnum) {
    StringBuilder sb = new StringBuilder();
    // trim开始
    sb.append(JavaFormat.appendTab(3)).append(MyBatisKey.UPDATE_TRIM_START);
    sb.append(Symbol.ENTER_LINE);

    for (int i = 0; i < copyColumnList.size(); i++) {
      TableColumnDTO tableMapper = copyColumnList.get(i);
      String columnName = tableMapper.getColumnName();
      String javaName = NameProcess.INSTANCE.toFieldName(tableMapper.getColumnName());
      String typeName = TypeConvergence.dbTypeParseMyBatis(typeEnum, tableMapper);

      // 添加列注释信息
      sb.append(JavaFormat.appendTab(4))
          .append(MyBatisKey.DOC_START)
          .append(tableMapper.getColumnMsg())
          .append(MyBatisKey.DOC_END)
          .append(Symbol.ENTER_LINE);

      // 进行类型检查判断
      String switchStr = this.switchType(tableMapper, javaName);

      // 先判断
      sb.append(JavaFormat.appendTab(4))
          .append(MyBatisKey.IF_START)
          .append(switchStr)
          .append(Symbol.QUOTE)
          .append(MyBatisKey.END)
          .append(Symbol.ENTER_LINE);
      // 修改对应的值信息
      sb.append(JavaFormat.appendTab(5))
          .append(Symbol.SPACE)
          .append(columnName)
          .append(Symbol.SPACE)
          .append(Symbol.EQUAL)
          .append(Symbol.SPACE)
          .append(Symbol.POUND)
          .append(Symbol.BRACE_LEFT)
          .append(javaName)
          .append(MyBatisKey.JDBC_TYPE)
          .append(typeName)
          .append(Symbol.BRACE_RIGHT)
          .append(Symbol.COMMA)
          .append(Symbol.ENTER_LINE);

      sb.append(JavaFormat.appendTab(4)).append(MyBatisKey.IF_END).append(Symbol.ENTER_LINE);
    }
    sb.append(JavaFormat.appendTab(3)).append(MyBatisKey.TRIM_XML_END).append(Symbol.ENTER_LINE);
    return sb.toString();
  }

  /**
   * 数据删除操作
   *
   * @param sb 数据构建对象
   * @param tableMsg 表信息
   * @param methodInfo 方法信息
   * @param poPackageInfo 实体信息
   * @param columnList 所有列
   * @param primaryKeyList 主键
   */
  private void deleteMethod(
      StringBuilder sb,
      TableInfoDTO tableMsg,
      MethodInfo methodInfo,
      ImportPackageInfo poPackageInfo,
      Map<String, TableColumnDTO> columnList,
      List<TableColumnDTO> primaryKeyList,
      DatabaseTypeEnum typeEnum) {

    // 删除
    sb.append(JavaFormat.appendTab(1))
        .append(MyBatisKey.DOC_START)
        .append(tableMsg.getTableComment())
        .append(Symbol.BRACKET_LEFT)
        .append(tableMsg.getTableName())
        .append(Symbol.BRACKET_RIGHT)
        .append(methodInfo.getComment())
        .append(MyBatisKey.DOC_END)
        .append(Symbol.ENTER_LINE);

    // po的类定义
    String poClassPath = poPackageInfo.packageOut();

    // 删除的xml文件的开始
    sb.append(JavaFormat.appendTab(1))
        .append(MyBatisKey.DELETE_XML_START)
        .append(methodInfo.getName())
        .append(MyBatisKey.TYPE_XML)
        .append(poClassPath)
        .append(Symbol.QUOTE)
        .append(MyBatisKey.END)
        .append(Symbol.ENTER_LINE);

    sb.append(JavaFormat.appendTab(2))
        .append(MyBatisKey.DELETE_SQL)
        .append(tableMsg.getTableName())
        .append(Symbol.ENTER_LINE);
    sb.append(JavaFormat.appendTab(2)).append(MyBatisKey.WHERE_START).append(Symbol.ENTER_LINE);
    // 选择条件
    sb.append(this.addWhere(methodInfo.getWhereInfo(), columnList, primaryKeyList, 2, typeEnum));
    sb.append(JavaFormat.appendTab(2)).append(MyBatisKey.WHERE_END).append(Symbol.ENTER_LINE);
    sb.append(JavaFormat.appendTab(1)).append(MyBatisKey.DELETE_XML_END).append(Symbol.ENTER_LINE);

    sb.append(Symbol.ENTER_LINE);
  }

  /**
   * 执行查询方法
   *
   * @param sb 当前追加的字符串
   * @param tableMsg 表信息
   * @param methodInfo 方法信息
   * @param resultMapId 查询结果集的id
   * @param columnList 列信息
   * @param keyColumn 主键列
   */
  private void queryMethod(
      StringBuilder sb,
      TableInfoDTO tableMsg,
      MethodInfo methodInfo,
      String resultMapId,
      ImportPackageInfo poPackageInfo,
      List<TableColumnDTO> columnList,
      Map<String, TableColumnDTO> columnMap,
      List<TableColumnDTO> keyColumn,
      DatabaseTypeEnum typeEnum) {
    // 查询注释
    sb.append(JavaFormat.appendTab(1))
        .append(MyBatisKey.DOC_START)
        .append(tableMsg.getTableComment())
        .append(Symbol.BRACKET_LEFT)
        .append(tableMsg.getTableName())
        .append(Symbol.BRACKET_RIGHT)
        .append(methodInfo.getComment())
        .append(MyBatisKey.DOC_END)
        .append(Symbol.ENTER_LINE);

    // po的类定义
    String poClassPath = poPackageInfo.packageOut();

    // 查询开始
    sb.append(JavaFormat.appendTab(1))
        .append(MyBatisKey.QUERY_ID_XML_START)
        .append(methodInfo.getName())
        .append(MyBatisKey.TYPE_XML)
        .append(poClassPath)
        .append(MyBatisKey.QUERY_RSP_MAPPER)
        .append(resultMapId)
        .append(Symbol.QUOTE)
        .append(MyBatisKey.END)
        .append(Symbol.ENTER_LINE);

    // 查询的列字段
    sb.append(JavaFormat.appendTab(2)).append(MyBatisKey.QUERY_SQL).append(Symbol.ENTER_LINE);

    // 输出列信息
    sb.append(this.queryField(columnList));

    sb.append(JavaFormat.appendTab(2)).append(MyBatisKey.QUERY_KEY_FROM).append(Symbol.ENTER_LINE);
    sb.append(JavaFormat.appendTab(2)).append(tableMsg.getTableName()).append(Symbol.ENTER_LINE);
    sb.append(JavaFormat.appendTab(2)).append(MyBatisKey.WHERE_START).append(Symbol.ENTER_LINE);

    // 条件的生成
    sb.append(this.addWhere(methodInfo.getWhereInfo(), columnMap, keyColumn, 2, typeEnum));

    sb.append(JavaFormat.appendTab(2)).append(MyBatisKey.WHERE_END).append(Symbol.ENTER_LINE);
    sb.append(JavaFormat.appendTab(1)).append(MyBatisKey.QUERY_XML_END).append(Symbol.ENTER_LINE);
    sb.append(Symbol.ENTER_LINE);
  }

  /**
   * 查询的属性信息
   *
   * @param columnList 列集合信息
   * @return 返回查询信息
   */
  public String queryField(List<TableColumnDTO> columnList) {
    StringBuilder sb = new StringBuilder();
    for (int i = 0; i < columnList.size(); i++) {
      String columnName = columnList.get(i).getColumnName();
      sb.append(JavaFormat.appendTab(2)).append(columnName);
      if (i != columnList.size() - 1) {
        sb.append(Symbol.COMMA).append(Symbol.ENTER_LINE);
      }
    }
    sb.append(Symbol.ENTER_LINE);
    return sb.toString();
  }

  /**
   * mapper文件结束文件结束
   *
   * @param sb 字符信息
   */
  private void mapperFinish(StringBuilder sb) {
    // 结束
    sb.append(MyBatisKey.NAMESPACE_END);
  }
}
