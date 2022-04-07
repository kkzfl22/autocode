package com.liujun.auto.config.generate;

/**
 * 生成的错误码处理
 *
 * <p>错误码使用负数，依次增加
 *
 * @author liujun
 * @version 0.0.1
 */
public class GenerateErrorCodeProcess {

  /** 每个之前留出20个空余 */
  public static final int INCREMENT_VALUE = -10;

  /** 表与表之前，默认按10000的增长 */
  private static final int INCREMENT_TABLE_VALUE = -5000;

  /** 实例 */
  public static final GenerateErrorCodeProcess INSTANCE = new GenerateErrorCodeProcess();

  /** 当前的错误码 */
  private int currCode;

  /**
   * 获取开始错误码
   *
   * @return 当前开始错误码
   */
  public int getStartCode() {
    // 1,优先从文件中读取
    int value = GenerateErrorCodePersist.INSTANCE.load();

    // 如果数据未加载到，则从文件中读取
    if (GenerateErrorCodePersist.DEFAULT_GET == value) {
      value = GenerateConfigProcess.INSTANCE.getCfgEntity().getGenerate().getStartErrorCode();
    }

    // 设置当前的错误码
    currCode = value;
    // 将数据值更新到序列化文件中
    int newValue = value + INCREMENT_TABLE_VALUE;
    GenerateErrorCodePersist.INSTANCE.save(newValue);

    return value;
  }

  /**
   * 增量添加
   *
   * @return
   */
  public int increment() {
    // 对当前的增量文件进行更新操作
    currCode = currCode + INCREMENT_VALUE;
    return currCode;
  }
}
