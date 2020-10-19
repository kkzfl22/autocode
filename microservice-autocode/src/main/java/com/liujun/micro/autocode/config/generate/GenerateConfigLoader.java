package com.liujun.micro.autocode.config.generate;

import com.liujun.micro.autocode.config.generate.entity.GenerateConfigEntity;
import com.liujun.micro.autocode.generator.builder.operator.utils.FileReaderUtils;
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
      input = FileReaderUtils.getFileInputStream(LOAD_CONFIG);
      config = yaml.loadAs(input, GenerateConfigEntity.class);
    } finally {
      StreamUtils.close(input);
    }

    return config;
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
