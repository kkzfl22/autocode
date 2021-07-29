package com.liujun.micro.autocode.generator.builder.constant;

/**
 * java方法名
 *
 * @author liujun
 * @version 0.0.1
 */
public class JavaMethodName {

  /** 集合的名称 */
  public static final String LIST_ADD = "add";

  /** 获取方法 */
  public static final String GET = "get";

  /** 设置方法 */
  public static final String SET = "set";

  /** put方法操作 */
  public static final String PUT = "put";

  /** 获取大小的方法 */
  public static final String SIZE = "size";

  /** 字符的追加方法 */
  public static final String APPEND = "append";

  /** 输出为字符的方法 */
  public static final String TOSTRING = "toString";

  /** 方法名 */
  public static final String BEFORE_SET_DATA = "beforeSetData";

  /** 方法完毕后的清理 */
  public static final String AFTER_CLEAN = "afterCleanData";

  /** 临时的方法名 */
  public static final String GET_DATA_METHOD = "getDataBean";

  /** 设置主键列属性的方法名 */
  public static final String SET_PRIMARY_FIELD = "setPrimaryField";

  /** 设置其他值属性的方法 */
  public static final String SET_DATA_FIELD = "setDataField";

  /** 数据集对比 */
  public static final String ASSERT_DATA_LIST = "assertDataList";

  /** 将数据转换为map的方法名 */
  public static final String PARSE_MAP = "parseMap";

  /** 以主键作key的方法 */
  public static final String PARSE_MAP_KEY = "getKey";

  /** 数据对比 */
  public static final String ASSERT_DATA = "assertData";

  /** 将领域对象转换为存储实体对象的方法 */
  public static final String ASSEMBLER_ENTITY_PERSIST_NAME = "toPersistObject";

  /** 将存储实体转换为领域对象的方法 */
  public static final String ASSEMBLER_DOMAIN_ENTITY_NAME = "toDomainEntity";

  /** 集合的判空方法 */
  public static final String LIST_EMPTY = "isEmpty";

  /** 将领域集合对象转换为存储集合实体对象的方法 */
  public static final String ASSEMBLER_ENTITY_PERSIST_LIST_NAME = "toListPersistObject";

  /** 将存储集合实体转换为领域集合对象的方法 */
  public static final String ASSEMBLER_PERSIST_ENTITY_LIST_NAME = "toListDomainEntity";

  /** 将传输对象转换为领域对象 */
  public static final String ASSEMBLER_TRANSFER_DOMAIN_NAME = "toDomainEntity";

  /** 将领域对象转换为传输对象 */
  public static final String ASSEMBLER_DOMAIN_TRANSFER_NAME = "toTransferObject";

  /** 将传输对象集合转换为领域对象集合 */
  public static final String ASSEMBLER_TRANSFER_DOMAIN_LIST_NAME = "toListDomainEntity";

  /** 将领域对象集合转换为传输对象集合 */
  public static final String ASSEMBLER_DOMAIN_TRANSFER_LIST_NAME = "toListTransferObject";

  /** 进行日志的debug输出 */
  public static final String LOG_DEBUG = "debug";

  /** 返回分页参数的构建器 */
  public static final String PAGE_BUILDER = "builder";

  /** 当前页 */
  public static final String PAGE_NUM = "page";

  /** 分页的方法 */
  public static final String PAGE_HELPER_NUM = "getPageNum";

  /** 总条数 */
  public static final String PAGE_TOTAL_SIZE = "total";

  /** 每页显示条数 */
  public static final String PAGE_SIZE = "size";

  /** 每页大小 */
  public static final String PAGE_HELPER_SIZE = "getPageSize";

  /** 查询的数据 */
  public static final String PAGE_DATA = "data";

  /** 数据返回使用对象 */
  public static final String PAGE_DTO_DATA = "data";

  /** 返回对象的构建方法 */
  public static final String PAGE_BUILD = "build";

  /** 枚举值的方法 */
  public static final String ENUM_METHOD_VALUES = "values";

  /** 添加错误码至错误容器的方法 */
  public static final String ERROR_ADD_CODE = "addCode";

  /** 错误码加载方法 */
  public static final String ERROR_CODE_LOADER = "loader";

  /** 属性 */
  public static final String ARRAY_LENGTH = "length";

  /** 错误码的初始化方法,用用指定初始化函数,进行统一的初始化操作 */
  public static final String CODE_METHOD_INIT = "init";

  /** 添加错误码 */
  public static final String ADD_ERROR_CODE = "addErrorCode";

  /** 失败操作 */
  public static final String RESPONSE_FAIL = "fail";

  /** 成功方法 */
  public static final String RESPONSE_SUCCESS = "ok";
}
