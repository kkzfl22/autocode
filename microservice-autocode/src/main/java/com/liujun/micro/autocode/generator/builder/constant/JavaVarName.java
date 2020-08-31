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

  /** 方法的参数注释 */
  public static final String METHOD_PARAM = " @param ";

  /** 数据库实例的名称 */
  public static final String INSTANCE_NAME = "instDao";

  /** 临时变量名 */
  public static final String PARAM_BEAN = "paramBean";

  /** 数据库是否执行插入语句的标识 */
  public static final String JUNIT_VAR_INSERT = "insertFlag";

  /** 实例po的名称 */
  public static final String INSTANCE_NAME_PO = "operatorPo";

  /** 数据库操作的返回 */
  public static final String INVOKE_METHOD_OPERATOR_RSP = "operatorRsp";

  /** 指定插入的数据定义 */
  public static final String BATCH_INSERT_NAME = "insertBatchMax";

  /** 删除方法返回的行数 */
  public static final String DELETE_METHOD_RSP = "deleteRsp";

  /** 数据信的名称 */
  public static final String LIST_NAME = "dataList";

  /** 循环中的变量 */
  public static final String FOR_TEMP_INDEX = "i";
}
