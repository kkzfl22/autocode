package com.liujun.micro.autocode.generator.builder.operator.utils;

import com.liujun.micro.autocode.constant.Symbol;
import com.liujun.micro.autocode.utils.StreamUtils;
import lombok.extern.slf4j.Slf4j;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;

/**
 * 文件读取的公共类
 *
 * <p>文件优先读取外部类，当外部文件未加载到时，才进行内部文件加载
 *
 * @author liujun
 * @version 0.0.1
 */
public class FileReaderUtils {

  private static final Logger log = LoggerFactory.getLogger(FileReaderUtils.class);

  /**
   * 读取文件内容
   *
   * @param path 文件路径
   * @return 文件内容
   */
  public static String readFile(String path) {
    StringBuilder outValue = new StringBuilder();

    InputStream input = null;
    InputStreamReader inputStreamReader = null;
    BufferedReader bufferedReader = null;
    try {
      input = getFileInputStream(path);
      inputStreamReader = new InputStreamReader(input, StandardCharsets.UTF_8);
      bufferedReader = new BufferedReader(inputStreamReader);

      String lineValue;

      while ((lineValue = bufferedReader.readLine()) != null) {
        outValue.append(JavaEncodeUtils.outCodeUtf8(lineValue)).append(Symbol.ENTER_LINE);
      }
    } catch (IOException e) {
      e.printStackTrace();
      log.error("read file {} ", e);
    } finally {
      StreamUtils.close(bufferedReader);
      StreamUtils.close(inputStreamReader);
      StreamUtils.close(input);
    }
    return outValue.toString();
  }

  /**
   * 文件获取,优先加载本地配制文件，未找则则从内部找文件
   *
   * @param absPath
   * @return
   * @throws IllegalArgumentException 当未加载配制时，收报错
   */
  public static InputStream getFileInputStream(String absPath) {
    // 优先加载外部配制文件
    InputStream input = getOutFileStream(absPath);
    // 当外部文件不存时，则使用内部配制文件,文件也可能存在于jar包或者普通工程中
    if (null == input) {
      input = FileReaderUtils.class.getClassLoader().getResourceAsStream(absPath);
    }
    if (input == null) {
      input = FileReaderUtils.class.getResourceAsStream(absPath);
    }
    if (input == null) {
      input = Thread.currentThread().getContextClassLoader().getResourceAsStream(absPath);
    }

    if (null == input) {
      throw new IllegalArgumentException(absPath + " not found");
    }

    return input;
  }

  /**
   * 获取外部文件的流
   *
   * @return 文件流
   */
  private static InputStream getOutFileStream(String path) {
    InputStream outFileStream = null;

    try {
      outFileStream = new FileInputStream(path);
    }
    // 当外部文件不存在时，会报出文件不存在异常，此异常需忽略，后续加载内置文件即可
    catch (FileNotFoundException e) {
      log.info("out file not exists :" + path);
    }

    return outFileStream;
  }
}
