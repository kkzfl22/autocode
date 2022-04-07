/*
 * Copyright (C), 2008-2021, Paraview All Rights Reserved.
 */
package com.liujun.auto.generator.javalanguage.entity;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.util.List;

/**
 * 方法的注释
 *
 * @author liujun
 * @since 2022/4/6
 */
@Getter
@Setter
@ToString
@Builder
public class ContextMethodDocument {

  /** 类注释信息 */
  private String comment;

  /** 参数信息 */
  private List<com.liujun.micro.autocode.generator.javalanguage.entity.ContextMethodParamDocument> paramDocuments;
}
