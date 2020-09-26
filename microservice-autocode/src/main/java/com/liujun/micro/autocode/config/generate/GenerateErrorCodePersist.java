package com.liujun.micro.autocode.config.generate;

import com.liujun.micro.autocode.utils.StreamUtils;
import lombok.extern.slf4j.Slf4j;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.OutputStream;
import java.net.URL;

/**
 * 错误码的持久化层
 *
 * @author liujun
 * @version 0.0.1
 */
@Slf4j
public class GenerateErrorCodePersist {

  /** 配制的持久的文件 */
  private static final String LOAD_CONFIG = "/config/errorPersist.code";

  /** 当前的路径标识 */
  private static final String CURR_PATH = ".";

  /** 默认获取值 */
  public static final int DEFAULT_GET = -1;

  public static final GenerateErrorCodePersist INSTANCE = new GenerateErrorCodePersist();

  private GenerateErrorCodePersist() {}

  /**
   * 错误码的保存操作
   *
   * @param errorCodeStart 待保存的错误码
   */
  public void save(int errorCodeStart) {

    OutputStream outputStream = null;
    ObjectOutputStream objectValueStream = null;
    String outPath = getBasePath() + LOAD_CONFIG;
    try {
      outputStream = new FileOutputStream(outPath);
      objectValueStream = new ObjectOutputStream(outputStream);
      objectValueStream.writeInt(errorCodeStart);
    } catch (FileNotFoundException e) {
      e.printStackTrace();
      log.error("save FileNotFoundException : {}", outPath, e);
    } catch (IOException e) {
      e.printStackTrace();
      log.error("save IOException : {}", outPath, e);
    } finally {
      StreamUtils.close(objectValueStream);
      StreamUtils.close(outputStream);
    }
  }

  /**
   * 获取当前所有的根路径
   *
   * @return 基础路径信息
   */
  private String getBasePath() {
    URL fileUrl = GenerateErrorCodePersist.class.getClassLoader().getResource(CURR_PATH);
    String basePath = null;

    if (null != fileUrl) {
      basePath = fileUrl.getPath();
    }

    if (null == fileUrl) {
      File f = new File(CURR_PATH);
      try {
        basePath = f.getCanonicalPath();
      } catch (IOException e) {
        log.error("path error", e);
      }
    }
    return basePath;
  }

  /**
   * 错误码文件的加载
   *
   * @return 错误码
   */
  public int load() {
    int value = -1;

    InputStream inputStream = null;
    ObjectInputStream inputObjectStream = null;
    try {
      inputStream = new FileInputStream(getBasePath() + LOAD_CONFIG);
      inputObjectStream = new ObjectInputStream(inputStream);
      value = inputObjectStream.readInt();
    }
    // 文件未加载到属性正常情况，所以异常就被吞了
    catch (FileNotFoundException e) {
    } catch (IOException e) {
      log.error("load IOException:", e);
    } finally {
      StreamUtils.close(inputObjectStream);
      StreamUtils.close(inputStream);
    }

    return value;
  }
}
