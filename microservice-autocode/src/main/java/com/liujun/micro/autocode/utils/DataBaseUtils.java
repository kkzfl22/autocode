package com.liujun.micro.autocode.utils;

import lombok.extern.slf4j.Slf4j;

/**
 * 数据库的公共操作
 *
 * @author liujun
 * @version 1.0.0
 */
@Slf4j
public class DataBaseUtils {

  /**
   * 进行关闭操作
   *
   * @param close jdbc的连接操作
   */
  public static void close(AutoCloseable close) {
    if (null != close) {
      try {
        close.close();
      } catch (Exception e) {
        log.error("close exception:", e);
      }
    }
  }
}
