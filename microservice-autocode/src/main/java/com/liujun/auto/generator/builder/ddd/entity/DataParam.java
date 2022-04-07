/*
 * Copyright (C), 2008-2021, Paraview All Rights Reserved.
 */
package com.liujun.auto.generator.builder.ddd.entity;

import com.liujun.auto.config.generate.entity.MethodInfo;
import com.liujun.auto.generator.database.constant.DatabaseTypeEnum;
import com.liujun.auto.generator.database.entity.TableColumnDTO;
import com.liujun.auto.generator.database.entity.TableInfoDTO;
import com.liujun.auto.generator.builder.ddd.constant.GenerateCodePackageKey;
import lombok.ToString;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 数据参数信息
 *
 * @author liujun
 * @since 2021/8/26
 */
@ToString
public class DataParam {

  /** 导包信息 */
  private Map<String, ImportPackageInfo> pkgMap;

  /** 当前的方法信息 */
  private List<MethodInfo> methodList;

  /** 作者信息 */
  private String author;

  /** 表结棍信息 */
  private TableInfoDTO tableInfo;

  /** 列信息 */
  private List<TableColumnDTO> columnList;

  /** 主键列表 */
  private List<TableColumnDTO> primaryKeyList;

  /** 列信息的map */
  private Map<String, TableColumnDTO> columnMap;

  /** 当前的数据库类型 */
  private DatabaseTypeEnum typeEnum;

  /** 依赖的列表 */
  private List<String> dependencyList;

  private DataParam(Builder data) {
    this.pkgMap = data.pkgMap;
    this.methodList = data.methodList;
    this.author = data.author;
    this.tableInfo = data.tableInfo;
    this.typeEnum = data.typeEnum;
    this.columnList = data.columnList;
    this.dependencyList = data.dependencyList;
    this.primaryKeyList = data.primaryKeyList;
    this.columnMap = data.columnMap;
  }

  public static class Builder {
    /** 所需的包信息信息 */
    private Map<String, ImportPackageInfo> pkgMap = new HashMap<>(16);

    /** 当前的方法信息 */
    private List<MethodInfo> methodList;

    /** 作者信息 */
    private String author;

    /** 表结棍信息 */
    private TableInfoDTO tableInfo;

    /** 当前的数据库类型 */
    private DatabaseTypeEnum typeEnum;

    /** 列信息 */
    private List<TableColumnDTO> columnList;

    /** 列信息的map */
    private Map<String, TableColumnDTO> columnMap;

    /** 主键列表 */
    private List<TableColumnDTO> primaryKeyList;

    /** 依赖的列表 */
    private List<String> dependencyList;

    public Builder putPkg(GenerateCodePackageKey key, ImportPackageInfo pkgInfo) {
      this.pkgMap.put(key.getKey(), pkgInfo);
      return this;
    }

    public Builder putPkg(Map<String, ImportPackageInfo> dataImport) {
      this.pkgMap.putAll(dataImport);
      return this;
    }

    public Builder methodList(List<MethodInfo> methodList) {
      this.methodList = methodList;
      return this;
    }

    public Builder author(String author) {
      this.author = author;
      return this;
    }

    public Builder tableInfo(TableInfoDTO table) {
      this.tableInfo = table;
      return this;
    }

    public Builder databaseType(DatabaseTypeEnum typeEnum) {
      this.typeEnum = typeEnum;
      return this;
    }

    public Builder columnList(List<TableColumnDTO> columnList) {
      this.columnList = columnList;
      return this;
    }

    public Builder primaryKeyList(List<TableColumnDTO> primaryKeyList) {
      this.primaryKeyList = primaryKeyList;
      return this;
    }

    public Builder columnMap(Map<String, TableColumnDTO> columnMap) {
      this.columnMap = columnMap;
      return this;
    }

    public Builder dependencyList(List<String> dependencyList) {
      this.dependencyList = dependencyList;
      return this;
    }

    public DataParam build() {
      return new DataParam(this);
    }
  }

  public static Builder builder() {
    return new Builder();
  }

  public ImportPackageInfo getPkg(GenerateCodePackageKey key) {
    return pkgMap.get(key.getKey());
  }

  public ImportPackageInfo getPkg(String key) {
    return pkgMap.get(key);
  }

  public List<MethodInfo> getMethodList() {
    return methodList;
  }

  public String getAuthor() {
    return author;
  }

  public TableInfoDTO getTableInfo() {
    return tableInfo;
  }

  public List<TableColumnDTO> getColumnList() {
    return columnList;
  }

  public DatabaseTypeEnum getTypeEnum() {
    return typeEnum;
  }

  public List<String> getDependencyList() {
    return dependencyList;
  }

  public List<TableColumnDTO> getPrimaryKeyList() {
    return primaryKeyList;
  }

  public Map<String, TableColumnDTO> getColumnMap() {
    return columnMap;
  }
}
