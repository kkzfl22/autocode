package com.liujun.auto.generator.builder.ddd.entity;

import com.liujun.auto.constant.Symbol;

/**
 * 构建器构建器
 *
 * @author liujun
 * @version 0.0.1
 */
public class ErrorCodeGenerate {

  /** 错误码信息开始 */
  private static final String CODE_START = "new ErrorData(";

  /** 错误码 */
  private int code;

  /** 提示信息 */
  private String msg;

  public ErrorCodeGenerate(int code, String msg) {
    this.code = code;
    this.msg = msg;
  }

  public static String outErrorCode(int code, String msg) {
    ErrorCodeGenerate errorData = new ErrorCodeGenerate(code, msg);
    return errorData.outErrorCode();
  }

  /**
   * 执行错误码对象构建输
   *
   * @return 错误码对象
   */
  public String outErrorCode() {
    StringBuilder outErrorCode = new StringBuilder();
    outErrorCode.append(CODE_START).append(code);
    outErrorCode.append(Symbol.COMMA).append(Symbol.SPACE);
    outErrorCode.append(Symbol.QUOTE);
    outErrorCode.append(msg);
    outErrorCode.append(Symbol.QUOTE);
    outErrorCode.append(Symbol.BRACKET_RIGHT);
    return outErrorCode.toString();
  }
}
