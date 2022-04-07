package com.liujun.auto.generator.builder.constant;

/**
 * @author liujun
 * @version 0.0.1
 */
public class MyBatisKey {

  /** xml文件的定义头 */
  public static final String HEAD = "<?xml version=\"1.0\" encoding=\"UTF-8\"?>";

  /** 定义的头信息 */
  public static final String DEFINE =
      "<!DOCTYPE mapper PUBLIC \"-//mybatis.org//DTD Mapper 3.0//EN\"  \"http://mybatis.org/dtd/mybatis-3-mapper.dtd\">";

  /** 开始 */
  public static final String START = "<";

  /** 结束括号 */
  public static final String END = ">";

  /** 结束符 */
  public static final String OVER = "/>";

  /** 注释 开始 */
  public static final String DOC_START = "<!--";

  /** 注释结束 */
  public static final String DOC_END = "-->";

  /** 空 */
  public static final String NULL_VALUE = "null";

  /** mysql的空值 */
  public static final String EMPTY_VALUE = "''";

  /** 标识开始 */
  public static final String NAMESPACE_START = "<mapper namespace=\"";

  /** 结束 */
  public static final String NAMESPACE_END = "</mapper>";

  /** 查询结果集开始 */
  public static final String RESULT_MAP_START = "<resultMap";

  /** 查询开始 */
  public static final String RESULT_MAP = RESULT_MAP_START + " type=\"";

  /** 查询结束 */
  public static final String RESULT_MAP_END = "</resultMap>";

  /** 定义的查询文件后缀名 */
  public static final String RESULT_SUFFIX_NAME = "ResultMap";

  /** 定义的id */
  public static final String ID = "id";

  /** 属性关键定定义 */
  public static final String PROPERTY = "property";
  /** 进行列的对应 */
  public static final String ID_PROPERTY = "id property";

  /** 列的标识 */
  public static final String COLUMN = "column";

  /** 结果集 */
  public static final String RESULT = "<result ";

  /** 判断开始 */
  public static final String IF_START = "<if test=\" ";

  /** 判断结束 */
  public static final String IF_END = "</if>";

  /** 开字符 */
  public static final String INSERT_TAB_START = "<insert";

  /** 插入 */
  public static final String INSERT_XML_START = INSERT_TAB_START + " id=\"";

  /** 类型信息 */
  public static final String TYPE_XML = "\" parameterType=\"";

  /** 插入SQL */
  public static final String INSERT_SQL_START = "insert into ";

  /** 数据插入值的关键字 */
  public static final String INSERT_SQL_VALUE_KEY = "values";

  /** 逗号补充 */
  public static final String TRIM_XML_START =
      "<trim prefix=\"(\" suffix=\")\" suffixOverrides=\",\">";

  /** 带value的去空格 */
  public static final String TRIM_VALUE_XML_START =
      "<trim prefix=\"(\" suffix=\")\" suffixOverrides=\",\">";

  /** 循环开始 */
  public static final String FOREACH_TAG_START = "<foreach";

  /** 批量操作开始 */
  public static final String FOREACH_LIST_START = FOREACH_TAG_START + " collection=\"";

  /** 批量添加的默认名称 */
  public static final String FOREACH_LIST_DEFAULT_NAME = "list";

  /** 遍历的关键字 */
  public static final String FOREACH_ITEM = "\" item=\"";

  /** 当前遍历名称 */
  public static final String FOREACH_ITEM_NAME = "item";

  /** 批量插入的定义 */
  public static final String FOREACH_BATCH_INSERT_FINISH =
      "\" open=\"(\" separator=\"),(\" close=\")\" index=\"index\">";

  /** in的条件的情况 */
  public static final String FOREACH_CONDITION_IN =
      "\" open=\"(\" separator=\",\" close=\")\" index=\"index\">";

  /** 遍历的结束 */
  public static final String FOREACH_LIST_END = "</foreach>";

  /** 去空格结束 */
  public static final String TRIM_XML_END = "</trim>";

  /** 类型 */
  public static final String JDBC_TYPE = ",jdbcType=";

  /** 插入结束 */
  public static final String INSERT_XML_END = "</insert>";

  /** 修改数据的开始 */
  public static final String UPDATE_TAG_START = "<update";

  /** 修改 */
  public static final String UPDATE_XML_START = UPDATE_TAG_START + " id=\"";

  /** 修改的SQL */
  public static final String UPDATE_SQL_START = "update";
  /** 修改结束 */
  public static final String UPDATE_XML_END = "</update>";

  /** 修改的条件 */
  public static final String UPDATE_SET = "<set>";

  /** 条件结束 */
  public static final String UPDATE_SET_END = "</set>";
  /** 去尾逗号操作 */
  public static final String UPDATE_TRIM_START = "<trim  suffixOverrides=\",\">";

  /** 条件开始 */
  public static final String WHERE_START = "<where> ";

  /** 条件结束 */
  public static final String WHERE_END = "</where> ";

  /** 删除操作 */
  public static final String DELETE_XML_START = "<delete id=\"";

  /** 删除结束 */
  public static final String DELETE_XML_END = "</delete>";

  /** 删除的ＳＱＬ */
  public static final String DELETE_SQL = "delete from  ";

  /** 查询开始标签 */
  public static final String QUERY_TAG_START = "<select";

  /** 按id进行查询 */
  public static final String QUERY_ID_XML_START = QUERY_TAG_START + " id=\"";

  /** 查询结束 */
  public static final String QUERY_XML_END = "</select>";

  /** 返回结果字段 */
  public static final String QUERY_RSP_MAPPER = "\" resultMap=\"";

  /** 查询的关键字 */
  public static final String QUERY_KEY = "select";

  /** 查询关键字 */
  public static final String QUERY_SQL = QUERY_KEY + " ";

  /** form关键字 */
  public static final String QUERY_KEY_FORM = "from";

  /** 指向表的关键字 */
  public static final String QUERY_KEY_FROM = " " + QUERY_KEY_FORM + " ";

  /** 条件关联 */
  public static final String KEY_AND = "and";
}
