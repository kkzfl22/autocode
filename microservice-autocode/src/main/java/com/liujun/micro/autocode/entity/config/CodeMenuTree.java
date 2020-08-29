package com.liujun.micro.autocode.entity.config;

import lombok.Data;
import lombok.ToString;

/**
 * 代码内的目录树相关的配制
 *
 * @author liujun
 * @version 0.0.1
 */
@Data
@ToString
public class CodeMenuTree {

  /** 最基础的目录 */
  private String baseMenu;

  /** 代码内的模块名称 */
  private String modelName;
}
