package com.liujun.auto.generator.builder.ddd.increment.field;

import com.liujun.auto.generator.builder.ddd.full.repositorymybatis.JavaCodeRepositoryMyBatisMapperXmlCreate;
import com.liujun.auto.algorithm.booyermoore.CharMatcherBmBadChars;
import com.liujun.auto.constant.Symbol;
import com.liujun.auto.generator.builder.ddd.constant.MyBatisKey;
import com.liujun.auto.generator.builder.ddd.entity.GenerateCodeContext;
import com.liujun.auto.generator.builder.GenerateCodeInf;
import com.liujun.auto.generator.builder.ddd.code.GenerateJavaMybatisMapperXml;
import com.liujun.auto.generator.builder.ddd.increment.field.matcher.MatcherFieldContext;
import com.liujun.auto.generator.builder.ddd.increment.field.matcher.MultMatcherProcess;
import com.liujun.auto.generator.builder.utils.FileReaderUtils;
import com.liujun.auto.generator.builder.utils.GenerateOutFileUtils;
import com.liujun.auto.generator.builder.utils.GeneratePathUtils;
import com.liujun.auto.generator.builder.utils.TableColumnUtils;
import com.liujun.auto.generator.database.entity.TableColumnDTO;
import com.liujun.auto.generator.javalanguage.serivce.JavaFormat;

import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.function.Consumer;

/**
 * 在数据库增加了字段需要对相关的文件进行重新影射
 *
 * <p>why: 当数据据库增加字段后，手动添加字段，费时费力，提供自动化的工具来将程序中的字段与数据进行影射的修改
 *
 * <p>what: 当前提供resultMap,insert,update,delete,query等这些标签中数据库字段的修改
 * resultMap:将resultMap中的字段使用替换的方式，将数据库的字段影射到此 <insert:整个操作SQL的替换生成 <update:由于update带有业务逻辑，不提供替换功能
 * <query:将其中的数据库字段替换为新的数据库字段
 *
 * <p>how: 以数据库中的字段为参数，然后使用ac自动机算法进行多模式串的字符匹配，使用策略模式，交给对应的处理方法函数处理
 *
 * @author liujun
 * @version 0.0.1
 * @since 2018年4月15日 下午3:29:03
 */
public class JavaCodeRepositoryMyBatisMapperFieldReplace implements GenerateCodeInf {

  public static final JavaCodeRepositoryMyBatisMapperFieldReplace INSTANCE =
      new JavaCodeRepositoryMyBatisMapperFieldReplace();

  /** 结束符的匹配 */
  public static final CharMatcherBmBadChars TAG_START_END_MATCHER =
      CharMatcherBmBadChars.getGoodSuffixInstance(Symbol.ANGLE_BRACKETS_RIGHT);

  /** </resultMap> 位置匹配 */
  private static final CharMatcherBmBadChars RESULT_MAP_END_MATCHER =
      CharMatcherBmBadChars.getGoodSuffixInstance(MyBatisKey.RESULT_MAP_END);

  /** <foreach 位置匹配 */
  private static final CharMatcherBmBadChars FOREACH_FLAG_MATCHER =
      CharMatcherBmBadChars.getGoodSuffixInstance(MyBatisKey.FOREACH_TAG_START);

  /** </insert> 位置匹配 */
  private static final CharMatcherBmBadChars INSERT_END_MATCHER =
      CharMatcherBmBadChars.getGoodSuffixInstance(MyBatisKey.INSERT_XML_END);

  /** </update> 位置匹配 */
  private static final CharMatcherBmBadChars UPDATE_END_MATCHER =
      CharMatcherBmBadChars.getGoodSuffixInstance(MyBatisKey.UPDATE_XML_END);

  /** select 位置匹配 */
  private static final CharMatcherBmBadChars QUERY_SELECT_MATCHER =
      CharMatcherBmBadChars.getGoodSuffixInstance(MyBatisKey.QUERY_KEY);

  /** form关键字位置匹配 */
  private static final CharMatcherBmBadChars QUERY_SELECT_FROM_MATCHER =
      CharMatcherBmBadChars.getGoodSuffixInstance(MyBatisKey.QUERY_KEY_FORM);

  /** </query>位置匹配 */
  private static final CharMatcherBmBadChars QUERY_XML_END_MATCHER =
      CharMatcherBmBadChars.getGoodSuffixInstance(MyBatisKey.QUERY_XML_END);

  /** 多模式式串的匹配处理 */
  private static final MultMatcherProcess matcherProcess;

  /** 操作示例 */
  public static final Map<String, Consumer<MatcherFieldContext>> RUN_REPLACE_FIELD;

  static {
    // 初始化容器对象
    RUN_REPLACE_FIELD = new HashMap<>(4);
    // 返回结果集的处理
    RUN_REPLACE_FIELD.put(MyBatisKey.RESULT_MAP_START, INSTANCE::resultMapProc);
    // 添加的处理
    RUN_REPLACE_FIELD.put(MyBatisKey.INSERT_TAB_START, INSTANCE::insertProc);
    // 修改的处理,修改目前不放开，生成可能会覆盖已有的SQL操作
    RUN_REPLACE_FIELD.put(MyBatisKey.UPDATE_TAG_START, INSTANCE::updateProc);
    // 查询的处理
    RUN_REPLACE_FIELD.put(MyBatisKey.QUERY_TAG_START, INSTANCE::queryProc);

    // 进行多模式串的处理
    matcherProcess = MultMatcherProcess.getInstance(RUN_REPLACE_FIELD);
  }

  @Override
  public void generateCode(GenerateCodeContext param) {

    Iterator<Entry<String, List<TableColumnDTO>>> iterTable =
        param.getColumnMapList().entrySet().iterator();

    while (iterTable.hasNext()) {
      Entry<String, List<TableColumnDTO>> entry = iterTable.next();
      String tableName = entry.getKey();

      // 读取已经生成的文件内容
      String mapperContext = this.readerMapper(param, tableName);

      // 构建上下文对象
      MatcherFieldContext context = this.builderContext(param, tableName, mapperContext);

      // 进行数据的属性字段的构建
      String resultValue = matcherProcess.charValueProcDefault(context);

      // 将mybatis文件的mapper输出到文件中
      GenerateOutFileUtils.outFile(
          resultValue,
          GeneratePathUtils.outServicePath(param),
          // 定义项目内的完整目录结构
          param.getProjectPath().getRepositoryMybatisMapperNode().outPath(),
          // 文件名
          JavaCodeRepositoryMyBatisMapperXmlCreate.INSTANCE.getMapperName(tableName),
          false);
    }
  }

  /**
   * 构建上下文对象信息
   *
   * @param param 上下文参数
   * @param tableName 表
   * @param dataContext mapper内容信息
   * @return 构建的操作对象
   */
  public MatcherFieldContext builderContext(
      GenerateCodeContext param, String tableName, String dataContext) {

    MatcherFieldContext context = new MatcherFieldContext(dataContext);

    context.setLastIndex(0);
    context.setTableInfo(param.getTableMap().get(tableName));
    context.setColumnList(param.getColumnMapList().get(tableName));
    context.setColumnMap(param.getColumnMapMap().get(tableName));
    context.setOutValueIncrement(
        new StringBuilder(dataContext.length() + (int) ((dataContext.length()) * 0.2)));
    // 获取当前主键列表
    context.setPrimaryKeyList(TableColumnUtils.getPrimaryKey(context.getColumnList()));
    context.setTypeEnum(param.getTypeEnum());

    return context;
  }

  /**
   * 进行mapper的文件读取操作
   *
   * @param param 上下文参数信息
   * @param tableName 表名
   * @return 读取的文件内容
   */
  private String readerMapper(GenerateCodeContext param, String tableName) {
    // 定义项目内的完整目录结构
    String outMapperPath = param.getProjectPath().getRepositoryMybatisMapperNode().outPath();
    String readPath = GeneratePathUtils.outServicePath(param) + Symbol.PATH + outMapperPath;
    String mapperFileName =
        readPath
            + Symbol.PATH
            + JavaCodeRepositoryMyBatisMapperXmlCreate.INSTANCE.getMapperName(tableName);

    return FileReaderUtils.readFile(mapperFileName);
  }

  /**
   * 用来进行响应查询结果的处理
   *
   * @param context
   */
  private void resultMapProc(MatcherFieldContext context) {

    // resultMap开始标签的结束位置
    int resultMapStartEndIndex =
        TAG_START_END_MATCHER.matcherIndex(
            context.getSrcDataArray(), context.getMatcherBusi().getMatcherIndex());
    takCheck(
        resultMapStartEndIndex, context.getTableInfo().getTableName(), Symbol.ANGLE_BRACKETS_RIGHT);

    // 1,将上一次的位置与当前查找到的位置间的数据，拷贝进结果中
    String startData =
        new String(
            context.getSrcDataArray(),
            context.getLastIndex(),
            resultMapStartEndIndex + 1 - context.getLastIndex());
    context.getOutValueIncrement().append(startData).append(Symbol.ENTER_LINE);

    // 1,生成新的查询结果集中的列结果集信息
    String newResultMap =
        GenerateJavaMybatisMapperXml.INSTANCE.outResultMap(
            context.getColumnList(), context.getPrimaryKeyList());
    // 将新的内容拷贝进
    context.getOutValueIncrement().append(newResultMap);

    // 查找当前的resultMap标签的结束位置
    int resultMapEndIndex =
        RESULT_MAP_END_MATCHER.matcherIndex(context.getSrcDataArray(), resultMapStartEndIndex);

    String addValue = MyBatisKey.RESULT_MAP_END + Symbol.ENTER_LINE;
    // 结束标签拷贝
    context.getOutValueIncrement().append(addValue);

    // 设置当前处理到的结束位置,然后交给下一个处理
    context.setLastIndex(resultMapEndIndex + addValue.length());
  }

  /**
   * 用来进行插入数据的标签的处理
   *
   * <p>插入的逻辑较为简单，直接找到开始标签与结束标签进行替换即可
   *
   * @param context 处理的对象信息
   */
  private void insertProc(MatcherFieldContext context) {

    // insert开始标签的结束位置
    int resultMapStartEndIndex =
        TAG_START_END_MATCHER.matcherIndex(
            context.getSrcDataArray(), context.getMatcherBusi().getMatcherIndex());
    takCheck(
        resultMapStartEndIndex, context.getTableInfo().getTableName(), MyBatisKey.RESULT_MAP_START);

    // </insert>标签的位置
    int resultMapEndIndex =
        INSERT_END_MATCHER.matcherIndex(context.getSrcDataArray(), resultMapStartEndIndex);
    takCheck(resultMapEndIndex, context.getTableInfo().getTableName(), MyBatisKey.INSERT_XML_END);

    // 查找foreach的位置
    int foreachIndex =
        FOREACH_FLAG_MATCHER.matcherIndex(context.getSrcDataArray(), resultMapStartEndIndex);
    takCheck(foreachIndex, context.getTableInfo().getTableName(), MyBatisKey.FOREACH_TAG_START);

    boolean batchFlag = false;
    // 如果当前存在foreach标签，并且在</insert>标签之前，则说明是批量操作
    if (foreachIndex != -1 && foreachIndex <= resultMapEndIndex) {
      batchFlag = true;
    }

    // 1,将上一次的位置与当前查找到的位置间的数据，拷贝进结果中
    String startData =
        new String(
            context.getSrcDataArray(),
            context.getLastIndex(),
            resultMapStartEndIndex + 1 - context.getLastIndex());
    context.getOutValueIncrement().append(startData).append(Symbol.ENTER_LINE);

    // 1,生成新的插入数据内容
    String newInsertContext =
        GenerateJavaMybatisMapperXml.INSTANCE.insertContext(
            context.getColumnList(), context.getTypeEnum(), batchFlag);
    // 将新的内容拷贝进
    context.getOutValueIncrement().append(newInsertContext);

    String insertValue = MyBatisKey.INSERT_XML_END + Symbol.ENTER_LINE;
    // 结束标签拷贝
    context.getOutValueIncrement().append(insertValue);

    // 设置当前处理到的结束位置,然后交给下一个处理
    context.setLastIndex(resultMapEndIndex + insertValue.length());
  }

  /**
   * 修改的处理
   *
   * <p>针对修改的内容设置上下文内容信息
   *
   * @param context 上下文对象
   */
  private void updateProc(MatcherFieldContext context) {

    // </update>位置
    int updateEndTag =
        UPDATE_END_MATCHER.matcherIndex(context.getSrcDataArray(), context.getLastIndex());
    takCheck(updateEndTag, context.getTableInfo().getTableName(), MyBatisKey.UPDATE_XML_END);

    String insertValue = MyBatisKey.UPDATE_XML_END + Symbol.ENTER_LINE;

    // 1,将上一次的位置与当前查找到的位置间的数据，拷贝进结果中
    String startData =
        new String(
            context.getSrcDataArray(),
            context.getLastIndex(),
            updateEndTag + insertValue.length() - context.getLastIndex());
    context.getOutValueIncrement().append(startData);

    // 设置当前处理到的结束位置,然后交给下一个处理
    context.setLastIndex(updateEndTag + insertValue.length());
  }

  /**
   * 查询的处理
   *
   * <p>针对修改的内容设置上下文内容信息
   *
   * @param context 上下文对象
   */
  private void queryProc(MatcherFieldContext context) {

    // query标签的位置
    int queryIndex =
        QUERY_SELECT_MATCHER.matcherIndex(
            context.getSrcDataArray(),
            context.getMatcherBusi().getMatcherIndex()
                + context.getMatcherBusi().getMatcherKey().length());
    takCheck(queryIndex, context.getTableInfo().getTableName(), MyBatisKey.QUERY_KEY);

    // form位置
    int fromIndex = QUERY_SELECT_FROM_MATCHER.matcherIndex(context.getSrcDataArray(), queryIndex);
    takCheck(fromIndex, context.getTableInfo().getTableName(), MyBatisKey.QUERY_KEY_FORM);

    // </select>位置
    int updateEndTag = QUERY_XML_END_MATCHER.matcherIndex(context.getSrcDataArray(), queryIndex);
    takCheck(updateEndTag, context.getTableInfo().getTableName(), MyBatisKey.QUERY_XML_END);

    // 1,将上一次的位置与当前查找到的位置间的数据，拷贝进结果中
    String startData =
        new String(
            context.getSrcDataArray(),
            context.getLastIndex(),
            queryIndex + MyBatisKey.QUERY_KEY.length() - context.getLastIndex());
    context.getOutValueIncrement().append(startData).append(Symbol.ENTER_LINE);

    // 1,生成新的查询列内容
    String newQueryField =
        GenerateJavaMybatisMapperXml.INSTANCE.queryField(context.getColumnList());
    // 将新的内容拷贝进
    context.getOutValueIncrement().append(newQueryField).append(JavaFormat.appendTab(2));

    String queryEndXml =
        new String(
            context.getSrcDataArray(),
            fromIndex,
            updateEndTag + MyBatisKey.QUERY_XML_END.length() - fromIndex);

    String insertValue = queryEndXml + Symbol.ENTER_LINE;
    // 结束标签拷贝
    context.getOutValueIncrement().append(insertValue);

    // 最后位置的值
    String updateEndValue = MyBatisKey.QUERY_XML_END + Symbol.ENTER_LINE;

    // 设置当前处理到的结束位置,然后交给下一个处理
    context.setLastIndex(updateEndTag + updateEndValue.length());
  }

  /**
   * 检查标签异常的情况
   *
   * @param findIndex 查找到的位置
   * @param tableName 表名
   * @param xmlTag 当前的标签
   */
  private void takCheck(int findIndex, String tableName, String xmlTag) {
    if (findIndex == -1) {
      throw new IllegalArgumentException("curr " + tableName + " mapper tag start error " + xmlTag);
    }
  }
}
