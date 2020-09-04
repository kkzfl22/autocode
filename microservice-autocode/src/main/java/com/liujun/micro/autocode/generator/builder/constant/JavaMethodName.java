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
  public static final String ASSEMBLER_PERSIST_ENTITY_NAME = "toDomainEntity";

  /** 集合的判空方法 */
  public static final String LIST_EMPTY = "isEmpty";


  /** 将领域集合对象转换为存储集合实体对象的方法 */
  public static final String ASSEMBLER_ENTITY_PERSIST_LIST_NAME = "toListPersistObject";

  /** 将存储集合实体转换为领域集合对象的方法 */
  public static final String ASSEMBLER_PERSIST_ENTITY_LIST_NAME = "toListDomainEntity";
}
