package com.liujun.micro.autocode;

import com.liujun.micro.autocode.config.properties.ConfigProperties;
import com.liujun.micro.autocode.constant.ConfigEnum;

/**
 * 程序入口
 *
 * @author liujun
 * @version 0.0.1
 */
public class MainRun {

  public static void main(String[] args) {
    String value = ConfigProperties.getInstance().getValue(ConfigEnum.DATABASE_TYPE);
    System.out.println("get value :" + value);
  }
}
