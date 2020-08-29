package com.liujun.micro.autocode.entity.config;

import lombok.Data;
import lombok.ToString;

/**
 * 代码生成器的配制文件
 *
 * @author liujun
 * @version 0.0.1
 * @date 2019/09/10
 */
@Data
@ToString
public class GenerateConfigEntity {

  /** 生成的代码相关的配制信息 */
  private Generate generate;
}
