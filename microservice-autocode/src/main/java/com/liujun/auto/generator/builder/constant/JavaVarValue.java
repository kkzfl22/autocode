package com.liujun.auto.generator.builder.constant;

/**
 * 相关声明的变量的值
 *
 * @author liujun
 * @version 0.0.1
 */
public class JavaVarValue {

  /** 批量数据插入的行数 */
  public static final String BATCH_NUM = "3";

  /** 索引开始的值 */
  public static final int FOR_INDEX_START = 0;

  /** 当前从1开始的遍历 */
  public static final int FOR_INDEX_START_1 = 1;

  /** 数据值 0 */
  public static final String ZERO = "0";

  /** 默认插入的行数 */
  public static final String DEFAULT_ADD_RSP = "1";

  /** 标准值1 */
  public static final String VALUE_ONE = "1";

  /** 加加 */
  public static final String ADD_ADD = "++";

  /** 对象为null */
  public static final String VALUE_NULL = "null";

  /** 不执行删除标识 */
  public static final String INSERT_TYPE_NONE_KEY = "InsertType.INSERT_TYPE_NONE";

  /** 单个插入标识 */
  public static final String INSERT_TYPE_ONE_KEY = "InsertType.INSERT_TYPE_ONE";

  /** 批量插入标识 */
  public static final String INSERT_TYPE_BATCH_KEY = "InsertType.INSERT_TYPE_BATCH";

  /** 默认的空集合 */
  public static final String LIST_EMPTY_DEFAULT = "Collections.emptyList()";

  /** 默认的空map */
  public static final String MAP_EMPTY = "Collections.emptyMap()";
}
