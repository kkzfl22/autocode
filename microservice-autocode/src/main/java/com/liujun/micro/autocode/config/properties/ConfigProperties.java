package com.liujun.micro.autocode.config.properties;

import com.liujun.micro.autocode.constant.ConfigEnum;
import com.liujun.micro.autocode.generator.builder.operator.utils.FileReaderUtils;
import com.liujun.micro.autocode.utils.StreamUtils;
import lombok.extern.slf4j.Slf4j;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.util.HashMap;
import java.util.Map;
import java.util.Properties;

/**
 * 属性文件读取
 *
 * @author liujun
 * @version 1.0.0
 */
@Slf4j
public class ConfigProperties {

  /** 属性配制文件信息 */
  private static final String PRO_FILE = "config/autocode.properties";

  /** 存储值的值 */
  private static final Map<ConfigEnum, String> CFG_MAP = new HashMap<>();

  /** 实例对象 */
  private static final ConfigProperties INSTANCE = new ConfigProperties();

  private ConfigProperties() {
    // 进行数据加载
    loadProperties();
  }

  /** 获取实例对象 方法描述 */
  public static ConfigProperties getInstance() {
    return INSTANCE;
  }

  /** 得到测试的属性文件信息 方法描述 */
  private void loadProperties() {
    Properties prop = new Properties();

    InputStream in = null;
    String config = PRO_FILE;
    try {
      in = FileReaderUtils.getFileInputStream(config);

      // 加载数据
      prop.load(in);

      // 进行数据的存储至map中
      dataPutMap(prop);
    } catch (IOException e) {
      log.error(PRO_FILE + " loader error ", e);
    } finally {
      StreamUtils.close(in);
    }
    prop = null;
  }

  /**
   * 数据存储
   *
   * @param prop
   */
  private void dataPutMap(Properties prop) {
    for (Map.Entry<Object, Object> entryItem : prop.entrySet()) {
      ConfigEnum cfgEnum = ConfigEnum.getCfg(String.valueOf(entryItem.getKey()));
      CFG_MAP.put(cfgEnum, entryItem.getValue().toString());
    }
  }

  /**
   * 获取数据信息
   *
   * @param key 获取数据枚举
   * @return 配制项的值
   */
  public String getValue(ConfigEnum key) {
    return CFG_MAP.get(key);
  }
}
