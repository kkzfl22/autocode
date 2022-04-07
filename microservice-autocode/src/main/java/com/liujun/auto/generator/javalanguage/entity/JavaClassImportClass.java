/*
 * Copyright (C), 2008-2021, Paraview All Rights Reserved.
 */
package com.liujun.auto.generator.javalanguage.entity;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

/**
 * 导包的class文件路径信息
 *
 * @author liujun
 * @since 2022/4/6
 */
@Setter
@Getter
@ToString
@Builder
public class JavaClassImportClass {

    /**
     * 是否为静态导入
     */
    private boolean staticImport;


    /**
     * 导入的相对路径
     */
    private String referencePath;

}
