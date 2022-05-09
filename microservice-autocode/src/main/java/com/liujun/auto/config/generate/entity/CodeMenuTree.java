package com.liujun.auto.config.generate.entity;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

/**
 * 代码内的目录树相关的配制
 *
 * @author liujun
 * @version 0.0.1
 */
@Getter
@Setter
@ToString
public class CodeMenuTree {

  /** 最基础的目录 */
  private String baseDir;

  /** 代码内的领域名称 */
  private String domainName;

  /** 基础层中的前缀信息 */
  private String infrastructurePrefix;
}
