package com.liujun.micro.autocode.utils;

import java.io.Closeable;
import java.io.IOException;

/**
 * 公共流操作
 *
 * @author liujun
 * @version 0.0.1
 */
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
        e.printStackTrace();
      }
    }
  }
}
