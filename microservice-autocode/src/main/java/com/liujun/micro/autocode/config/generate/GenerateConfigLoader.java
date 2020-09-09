package com.liujun.micro.autocode.config.generate;

import com.liujun.micro.autocode.config.generate.entity.GenerateConfigEntity;
import com.liujun.micro.autocode.utils.StreamUtils;
import lombok.extern.slf4j.Slf4j;
import org.yaml.snakeyaml.Yaml;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.InputStream;

/**
 * 生成代码的相关配制信息
 *
 * @author liujun
 * @version 0.0.1
 * @date 2019/09/10
 */
@Slf4j
public class GenerateConfigLoader {

  /** 配制的文件路径 */
  private static final String LOAD_CONFIG = "config/generate/generate.yml";

  public static final GenerateConfigLoader INSTANCE = new GenerateConfigLoader();

  /** 数据加载操作 */
  private final GenerateConfigEntity cfgEntity;

  private GenerateConfigLoader() {
    cfgEntity = loadCfg();
  }

  /**
   * 载加配制信息
   *
   * @return
   */
  private static GenerateConfigEntity loadCfg() {
    Yaml yaml = new Yaml();
    InputStream input = null;
    GenerateConfigEntity config = null;

    try {
      // 优先加载外部配制文件
      input = getOutFileStream();
      if (null == input) {
        // 当外部文件不存时，则使用内部配制文件
        input = GenerateConfigLoader.class.getClassLoader().getResourceAsStream(LOAD_CONFIG);
      }
      if (null == input) {
        throw new IllegalArgumentException(LOAD_CONFIG + " load error");
      }
      config = yaml.loadAs(input, GenerateConfigEntity.class);
    } finally {
      StreamUtils.close(input);
    }

    return config;
  }

  /**
   * 获取外部文件的流
   *
   * @return 文件流
   */
  private static InputStream getOutFileStream() {
    InputStream outFileStream = null;

    try {
      outFileStream = new FileInputStream(LOAD_CONFIG);
    }
    // 当外部文件不存在时，会报出文件不存在异常，此异常需忽略，后续加载内置文件即可
    catch (FileNotFoundException e) {
      log.info("out file not exists :" + LOAD_CONFIG);
    }

    return outFileStream;
  }

  /**
   * 配制 文件的实体信息
   *
   * @return
   */
  public GenerateConfigEntity getCfgEntity() {
    return cfgEntity;
  }
}
