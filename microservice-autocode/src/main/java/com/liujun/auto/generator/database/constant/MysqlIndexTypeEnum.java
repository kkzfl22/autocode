/*
 * Copyright (C), 2008-2021, Paraview All Rights Reserved.
 */
package com.liujun.auto.generator.database.constant;

import lombok.Getter;
import lombok.ToString;

/**
 * 索引的类型
 *
 * @author liujun
 * @since 2022/5/2
 */
@ToString
@Getter
public enum MysqlIndexTypeEnum {

  /** 主键 */
  PRIMARY_KEY("PRIMARY KEY"),

  /** 唯一约束 */
  UNIQUE("UNIQUE");

  private String type;

  MysqlIndexTypeEnum(String type) {
    this.type = type;
  }
}
