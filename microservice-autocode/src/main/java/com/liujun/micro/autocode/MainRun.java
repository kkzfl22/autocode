package com.liujun.micro.autocode;

import com.liujun.micro.autocode.config.generate.GenerateErrorCodePersist;
import com.liujun.micro.autocode.config.generate.entity.Generate;

/**
 * 程序入口
 *
 * @author liujun
 * @version 0.0.1
 */
public class MainRun {

  public static void main(String[] args) {

    int errorCode = -1000120;
    GenerateErrorCodePersist.INSTANCE.save(errorCode);
    int loadCode = GenerateErrorCodePersist.INSTANCE.load();
    System.out.println("加载的错误码:" + loadCode);
  }
}
