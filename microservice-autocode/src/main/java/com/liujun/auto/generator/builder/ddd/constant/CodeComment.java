package com.liujun.auto.generator.builder.ddd.constant;

/**
 * 参数注释相关的信息
 *
 * @author liujun
 * @version 0.0.1
 */
public class CodeComment {

  /** 参数信息 */
  public static final String METHOD_PARAM_DOC = "参数信息";

  /** 响应 */
  public static final String METHOD_RESULT_UPDATE_DOC = "数据库影响的行数";

  /** 响应 */
  public static final String METHOD_RESULT_QUERY_DOC = "数据库查询结果集";

  /** 分页请求参数 */
  public static final String PAGE_REQUEST_COMMENT = "分页查询请求参数";

  /** 分页请求响应 */
  public static final String PAGE_RESPONSE_COMMENT = "分页查询响应";

  /** 数据库执行修改的返回参数 */
  public static final String METHOD_DAO_UPDATE_INT_RETURN = "数据库影响的行数";

  /** 数据库执行修改的返回参数 */
  public static final String METHOD_DAO_UPDATE_BOOLEAN_RETURN = "操作是否成功";

  /** 数据库查询结果 */
  public static final String METHOD_DATABASE_QUERY_RESULT = "数据库查询结果集";

  /** 查询的注释 */
  public static final String METHOD_QUERY_RESULT = "数询结果集";

  /** 用来存储批量操作的容器 */
  public static final String FIELD_BATCH_LIST_COMMENT = "批量操作的数据存储容器";

  /** 数据操作的类型 */
  public static final String FIELD_OPERATOR_TYPE_COMMENT = "数据操作类型，1，单个插入，2批量插入，其他不操作";

  /** 执行修改的返回参数 */
  public static final String METHOD_SERVICE_UPDATE_RETURN = "执行的结果 true/false";

  /** 属性条件列表 */
  public static final String FIELD_CONDITION_LIST = "查询条件列表";

  /** 普通属性的值设置方法的注释 */
  public static final String SET_DATA_FIELD_COMMENT = "设置普通属性值";

  /** 设置主键列属性的注释 */
  public static final String SET_PRIMARY_FIELD_COMMENT = "设置主键属性";

  /** 批量添加的数量 */
  public static final String FINAL_BATCH_INSERT_NUM_COMMENT = "当前批量添加的个数";

  /** 数据准备方法 */
  public static final String JUNIT_BEFORE_COMMENT = "单元测试的数据准备";

  /** 数据准备方法 */
  public static final String JUNIT_GET_DATA_COMMENT = "单元测试的获取数据对象";

  /** 数据集对比 */
  public static final String JUNIT_ASSERT_LIST_COMMENT = "数据集对比方法";

  /** 数据源名称注释 */
  public static final String JUNIT_SRC_LIST_COMMENT = "源数据集";

  /** 目标数据集名称注释 */
  public static final String JUNIT_TARGET_LIST_COMMENT = "目标数据集";

  /** 转换方法的注释 */
  public static final String JUNIT_PARSE_MAP_COMMENT = "将数据集合转换为map";

  /** 提取数据集的主键key */
  public static final String JUNIT_PARSE_KEY_COMMENT = "提取数据集的主键key";

  /** 数据转换的名称 */
  public static final String JUNIT_PARSE_KEY_PO_COMMENT = "实体信息";

  /** 转换函数的参数 */
  public static final String JUNIT_PARSE_LIST_COMMENT = "数据集";

  /** 数据对比的注释 */
  public static final String JUNIT_ASSERT_DATA_VALUE = "数据对比方法";

  /** 源数据信息 */
  public static final String JUNIT_ASSERT_DATA_SRC = "源数据信息";

  /** 目标数据信息 */
  public static final String JUNIT_ASSERT_DATA_TARGET = "目标数据信息";

  /** 在执行完毕后的清理 */
  public static final String JUNIT_AFTER_CLEAN = "数据清理";

  /** 领域对象与实体转换对象的方法 */
  public static final String ASSEMBLER_ENTITY_PERSIST_COMMENT = "将领域对象转换为存储对象";

  /** 将存储对象转换为领域对象的方法 */
  public static final String ASSEMBLER_PERSIST_ENTITY_COMMENT = "将存储对象转换为领域对象";

  /** 领域集合对象与实体集合转换对象的方法 */
  public static final String ASSEMBLER_ENTITY_PERSIST_LIST_COMMENT = "将领域集合对象转换为存储集合对象";

  /** 将存储集合对象转换为领域集合对象的方法 */
  public static final String ASSEMBLER_PERSIST_ENTITY_LIST_COMMENT = "将存储集合对象转换为领域集合对象";

  /** 操作方法的返回结果 */
  public static final String DOMAIN_UPDATE_COMMENT = "true 操作成功,false 操作失败";

  /** 将传输对象转换为领域对象 */
  public static final String ASSEMBLER_TRANSFER_DOMAIN_COMMENT = "将传输对象转换为领域对象";

  /** 将领域对象转换为传输对象 */
  public static final String ASSEMBLER_DOMAIN_TRANSFER_COMMENT = "将领域对象转换为传输对象";

  /** 将传输对象集合转换为传输对象集合 */
  public static final String ASSEMBLER_TRANSFER_DOMAIN_LIST_COMMENT = "将传输对象集合转换为传输对象集合";

  /** 将传输对象集合转换为领域对象集合 */
  public static final String ASSEMBLER_DOMAIN_TRANSFER_LIST_COMMENT = "将传输对象集合转换为领域对象集合";

  /** 模块名称的注释 */
  public static final String MODULE_NAME_COMMENT = "模块名称";

  /** 错误码加载至容器的方法 */
  public static final String ERROR_CODE_LOADER_COMMENT = "错误码加载至容器的方法";
}
