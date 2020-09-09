package com.liujun.micro.autocode.utils;

import lombok.extern.slf4j.Slf4j;

import java.io.Closeable;
import java.io.IOException;

/**
 * 公共流操作
 *
 * @author liujun
 * @version 0.0.1
 */
@Slf4j
public class StreamUtils {
  /**
   * 关闭流
   *
   * @param stream
   */
  public static void close(Closeable stream) {
    if (null != stream) {
      try {
        stream.close();
      } catch (IOException e) {
        log.error("StreamUtils close IOException: ", e);
      }
    }
  }
}
