/*
 * Copyright (C), 2008-2021, Paraview All Rights Reserved.
 */
package com.liujun.auto.generator.javalanguage.entity;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

/**
 * 类文件的结构信息
 *
 * @author liujun
 * @since 2022/4/6
 */
@Getter
@Setter
@ToString
@Builder
public class JavaClassDocument {


    /**
     * 类注释信息
     */
    private String comment;


    /**
     * 作者信息
     */
    private String author;


    /**
     * 类创建的时间
     */
    private String since;

}
