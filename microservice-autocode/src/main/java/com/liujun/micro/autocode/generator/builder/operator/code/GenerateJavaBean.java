package com.liujun.micro.autocode.generator.builder.operator.code;

import com.liujun.micro.autocode.constant.Symbol;
import com.liujun.micro.autocode.entity.config.MethodInfo;
import com.liujun.micro.autocode.entity.config.WhereInfo;
import com.liujun.micro.autocode.generator.builder.constant.CodeCommentEnum;
import com.liujun.micro.autocode.generator.builder.constant.MyBatisOperatorFlag;
import com.liujun.micro.autocode.generator.builder.utils.TypeProcessUtils;
import com.liujun.micro.autocode.generator.database.entity.TableColumnDTO;
import com.liujun.micro.autocode.generator.database.entity.TableInfoDTO;
import com.liujun.micro.autocode.generator.javalanguage.constant.JavaKeyWord;
import com.liujun.micro.autocode.generator.javalanguage.serivce.JavaFormat;
import com.liujun.micro.autocode.generator.javalanguage.serivce.NameProcess;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

/**
 * @author liujun
 * @version 0.0.1
 */
public class GenerateJavaBean {

  public static final GenerateJavaBean INSTANCE = new GenerateJavaBean();

  /**
   * 进行javaBean文件的生成操作
   *
   * @param tableMsgBean 表信息
   * @param className 类名
   * @param columnList 列信息
   * @param comment 类注释
   * @param outJavaPackage 输出路径信息
   * @param codeMethod 需要生成的方法
   * @return 生成的javabean对象
   */
  public StringBuilder generateJavaBean(
      TableInfoDTO tableMsgBean,
      String className,
      List<TableColumnDTO> columnList,
      String comment,
      String outJavaPackage,
      List<MethodInfo> codeMethod) {
    // 构建字符串对象
    StringBuilder sb = new StringBuilder();

    // 输出定义头信息
    this.classDefine(sb, tableMsgBean, className, comment, outJavaPackage, codeMethod);
    // 作属性输出
    this.outProperties(columnList, sb);
    // in关键输的输出
    this.inCondition(codeMethod, columnList, sb);
    // 类结束
    this.classFinish(sb);

    return sb;
  }

  /**
   * 进行in条件的condition的输出操作
   *
   * @param codeMethod 需要输出的方法
   * @param columnList 列信息
   * @param sb 输出的对象
   */
  private void inCondition(
      List<MethodInfo> codeMethod, List<TableColumnDTO> columnList, StringBuilder sb) {
    // 进行条件的输出
    Set<String> conditionList = this.getInCondition(codeMethod);
    // 作为属性输出
    this.outInCondition(conditionList, columnList, sb);
  }

  /**
   * 获取所有带in的条件件信息
   *
   * @param codeMethod 方法信息
   * @return 结果
   */
  private Set<String> getInCondition(List<MethodInfo> codeMethod) {

    Set<String> addWhereColumn = new HashSet<>();

    for (MethodInfo method : codeMethod) {
      for (WhereInfo whereIn : method.getWhereInfo()) {
        // 检查当前是否存在in关键字
        if (MyBatisOperatorFlag.IN.equals(whereIn.getOperatorFlag())) {
          addWhereColumn.add(whereIn.getSqlColumn());
        }
      }
    }
    return addWhereColumn;
  }

  /**
   * 进行属性的输出操作
   *
   * @param inCondition 条件
   * @param columnList 列
   * @param sb 输出
   */
  private void outInCondition(
      Set<String> inCondition, List<TableColumnDTO> columnList, StringBuilder sb) {
    for (String inConditionItem : inCondition) {
      TableColumnDTO tableInfo = this.getColumn(columnList, inConditionItem);

      if (null == tableInfo) {
        continue;
      }

      // 得到java的数据类型
      String javaDataType = TypeProcessUtils.getJavaType(tableInfo.getDataType());
      // 得到java输出的名称
      String javaName = NameProcess.INSTANCE.toFieldName(tableInfo.getColumnName());

      // 输出的类型
      javaDataType = JavaKeyWord.LIST_TYPE + javaDataType + JavaKeyWord.LIST_TYPE_END;

      // 输出的名称
      javaName = getInConditionName(javaName);

      // 属性输出
      this.outField(
          sb,
          javaDataType,
          javaName,
          tableInfo.getColumnMsg() + CodeCommentEnum.FIELD_CONDITION_LIST);
    }
  }

  /**
   * 获取in关键字作为属性的名称
   *
   * @param name 名称
   * @return 获取名称
   */
  public static String getInConditionName(String name) {
    String outName = name + JavaKeyWord.FIELD_SUFFIX_NAME;

    return outName;
  }

  private TableColumnDTO getColumn(List<TableColumnDTO> columnList, String inConditionItem) {
    for (TableColumnDTO tableColumnItem : columnList) {
      // 如果能列名能匹配上
      if (tableColumnItem.getColumnName().equalsIgnoreCase(inConditionItem)) {
        return tableColumnItem;
      }
    }

    return null;
  }

  /**
   * 文件头信息定义
   *
   * @param sb 构建输出对象
   * @param tableMsgBean 当前的数据文件描述
   * @param className 类名
   * @param classComment 类注释
   * @param definePackage 定义的包路径
   * @param methodList 方法列表
   */
  private void classDefine(
      StringBuilder sb,
      TableInfoDTO tableMsgBean,
      String className,
      String classComment,
      String definePackage,
      List<MethodInfo> methodList) {

    // 定义包
    sb.append(JavaKeyWord.PACKAGE)
        .append(Symbol.SPACE)
        .append(definePackage)
        .append(Symbol.SEMICOLON)
        .append(Symbol.ENTER_LINE);
    sb.append(Symbol.ENTER_LINE);
    sb.append(Symbol.ENTER_LINE);

    // 1,导包
    sb.append(JavaKeyWord.BEAN_IMPORT_DATA).append(Symbol.ENTER_LINE);
    sb.append(JavaKeyWord.BEAN_IMPORT_TOSTRING).append(Symbol.ENTER_LINE);

    // 检查是否需要导入list包
    Set<String> inCondition = this.getInCondition(methodList);
    if (!inCondition.isEmpty()) {
      sb.append(JavaKeyWord.IMPORT)
          .append(Symbol.SPACE)
          .append(JavaKeyWord.IMPORT_LIST)
          .append(Symbol.SEMICOLON);
      sb.append(Symbol.ENTER_LINE);
    }

    sb.append(Symbol.ENTER_LINE);

    // 添加类注释信息
    sb.append(JavaKeyWord.ANNO_CLASS)
        .append(Symbol.ENTER_LINE)
        .append(JavaKeyWord.ANNO_CLASS_MID)
        .append(Symbol.SPACE)
        .append(tableMsgBean.getTableComment())
        .append(Symbol.BRACKET_LEFT)
        .append(tableMsgBean.getTableName())
        .append(Symbol.BRACKET_RIGHT)
        .append(classComment)
        .append(Symbol.ENTER_LINE)
        .append(JavaKeyWord.ANNO_CLASS_MID)
        .append(Symbol.ENTER_LINE)
        .append(JavaKeyWord.DOC_VERSION)
        .append(Symbol.ENTER_LINE)
        .append(JavaKeyWord.DOC_AUTH)
        .append(Symbol.ENTER_LINE)
        .append(JavaKeyWord.ANNO_OVER)
        .append(Symbol.ENTER_LINE);
    // 引入@data和@toString
    sb.append(JavaKeyWord.BEAN_USE_DATA).append(Symbol.ENTER_LINE);
    sb.append(JavaKeyWord.BEAN_USE_TOSTRING).append(Symbol.ENTER_LINE);
    sb.append(JavaKeyWord.ClASS_START)
        .append(className)
        .append(Symbol.SPACE)
        .append(Symbol.BRACE_LEFT);
    sb.append(Symbol.ENTER_LINE);
    sb.append(Symbol.ENTER_LINE);
    sb.append(Symbol.ENTER_LINE);
  }

  /**
   * 属性输出
   *
   * @param columnList 列集合
   * @param sb 输出的对象 信息
   */
  private void outProperties(List<TableColumnDTO> columnList, StringBuilder sb) {
    // 添加属性的信息
    for (int i = 0; i < columnList.size(); i++) {
      TableColumnDTO tableBean = columnList.get(i);

      // 得到java的数据类型
      String javaDataType = TypeProcessUtils.getJavaType(tableBean.getDataType());
      // 得到java输出的名称
      String javaName = NameProcess.INSTANCE.toFieldName(tableBean.getColumnName());

      // 属性输出
      this.outField(sb, javaDataType, javaName, tableBean.getColumnMsg());
    }
  }

  /**
   * 属性输出
   *
   * @param sb 输出的代码信息
   * @param javaDataType 类型信息
   * @param javaName java的类名
   * @param comment 注释
   */
  private void outField(StringBuilder sb, String javaDataType, String javaName, String comment) {

    sb.append(JavaFormat.appendTab(1)).append(JavaKeyWord.ANNO_CLASS).append(Symbol.ENTER_LINE);
    sb.append(JavaFormat.appendTab(1))
        .append(JavaKeyWord.ANNO_CLASS_MID)
        .append(Symbol.SPACE)
        .append(comment)
        .append(Symbol.ENTER_LINE);
    sb.append(JavaFormat.appendTab(1)).append(JavaKeyWord.ANNO_OVER).append(Symbol.ENTER_LINE);
    sb.append(JavaFormat.appendTab(1))
        .append(JavaKeyWord.PRIVATE)
        .append(Symbol.SPACE)
        .append(javaDataType)
        .append(Symbol.SPACE)
        .append(javaName)
        .append(Symbol.SEMICOLON);
    sb.append(Symbol.ENTER_LINE);
    sb.append(Symbol.ENTER_LINE);
  }

  /**
   * class文件结束
   *
   * @param sb 字符信息
   */
  private void classFinish(StringBuilder sb) {
    // 结束
    sb.append(Symbol.BRACE_RIGHT);
  }
}
