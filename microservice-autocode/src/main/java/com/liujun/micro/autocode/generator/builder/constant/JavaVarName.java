package com.liujun.micro.autocode.generator.builder.constant;

/**
 * 相关声明的变量名称
 *
 * @author liujun
 * @version 0.0.1
 */
public class JavaVarName {

  /** 用于定义参数的变量名称 */
  public static final String METHOD_PARAM_NAME = "param";

  /** 转换后的数据信息名称 */
  public static final String ASSEMBLER_PARSE_NAME = "parseData";

  /** 分页请求参数 */
  public static final String PAGE_REQUEST = "pageReq";

  /** 分页查询响应 */
  public static final String PAGE_RESPONSE = "pageRsp";

  /** 分页查询对象 */
  public static final String PAGE_RETURN_DATA = "pageResult";

  /** 查询集合响应 */
  public static final String QUERY_LIST_RESPONSE = "queryListRsp";

  /** 查询集合对象 */
  public static final String QUERY_LIST_RETURN_DATA = "queryListResult";

  /** 分页查询的结果集转换后的名称 */
  public static final String PAGE_RSP_ASSEMBLER_PARSE = "pageRspParse";

  /** 普通查询返回对象 */
  public static final String QUERY_RESPONSE_DATA = "queryRspData";

  /** 查询最终返回 */
  public static final String QUERY_RETURN_DATA = "queryReturn";

  /** 数据库实例的名称 */
  public static final String SPRING_INSTANCE_NAME = "serviceBean";

  /** facade的实例对象 */
  public static final String FACADE_INSTANCE_NAME = "instanceFacade";

  /** domain的实例对象 */
  public static final String DOMAIN_INSTANCE_NAME = "instanceDomain";

  /** 应用服务 */
  public static final String APPLICATION_INSTANCE_NAME = "instanceApplication";

  /** 临时变量名 */
  public static final String PARAM_BEAN = "paramBean";

  /** 数据插入的类型，0，不插入数据，1，单个插入，2，批量插入 */
  public static final String JUNIT_VAR_BATCH_INSERT = "insertType";

  /** 实例的名称的名称 */
  public static final String INSTANCE_NAME_ENTITY = "oneData";

  /** 数据库操作的返回 */
  public static final String INVOKE_METHOD_OPERATOR_RSP = "operatorRsp";

  /** 数据库修改操作的返回 */
  public static final String INVOKE_METHOD_UPDATE_RSP = "updateRsp";

  /** 全局定义的批量插入 */
  public static final String FINAL_BATCH_INSERT_NUM = "INSERT_BATCH_NUM";

  /** 操作返回对象名 */
  public static final String INVOKE_METHOD_QUERY_RSP = "dataRsp";

  /** 查询的临时参数名称 */
  public static final String METHOD_PARAM_TEMP_NAME = "paramDataTmp";

  /** 删除方法返回的行数 */
  public static final String DELETE_METHOD_RSP = "deleteRsp";

  /** 数据信的名称 */
  public static final String LIST_NAME = "dataList";

  /** 批量操作的集合 */
  public static final String BATCH_LIST_NAME = "batchDataList";

  /** 数据获取的临时变量 */
  public static final String LIST_GET_NAME_TEMP_NAME = "dataItemTmp";

  /** 循环中的变量 */
  public static final String FOR_TEMP_INDEX = "i";

  /** 集合名称后缀 */
  public static final String NAME_LIST_SUFFIX = "List";

  /** 数据集对比源名称 */
  public static final String ASSERT_PARAM_SRC_LIST = "srcList";

  /** 数据集对比目标名称 */
  public static final String ASSERT_PARAM_TARGET_LIST = "targetList";

  /** 目标数据获取 */
  public static final String ASSERT_DATA_TARGET = "target";

  /** 转换的map对象信息 */
  public static final String PARSE_MAP_TEMP = "dataMapTmp";

  /** 获取key的名称 */
  public static final String GET_KEY_NAME = "key";

  /** 源数据的名称 */
  public static final String ASSERT_DATA_SRC = "src";

  /** 日志输出的对象 */
  public static final String LOG = "log";

  /** 分页的信息 */
  public static final String QUERY_PAGE_PARAM_VAR = "pageData";

  /** 遍历的临时变量 */
  public static final String LOOP_TEMP = "loopTempItem";

  /** 模块名称的变量名 */
  public static final String MODULE_NAME_VAR = "MODULE_NAME";

  /** 实例名称 */
  public static final String INSTANCE_NAME = "INSTANCE";
}
