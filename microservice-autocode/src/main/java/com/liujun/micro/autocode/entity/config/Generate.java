package com.liujun.micro.autocode.entity.config;

import lombok.Data;
import lombok.ToString;

import java.util.List;
import java.util.Map;

/**
 * 数据生成对象的相关配制
 *
 * @author liujun
 * @version 0.0.1
 */
@Data
@ToString
public class Generate {

  /** 当前的模式：1，先删除后添加,2，追加模式,3,追加覆盖模式 */
  private Integer model;

  /** 生成代码的类型：mvc，使用spring mvc的三层架构，ddd，使用springboot的ddd四层架构 */
  private String type;

  /** 生成的目标数据库类型:mysql目标数据库为mysql;oralce，目标数据库为oracle */
  private String databaseType;

  /** 错误码开始编号，依次向后；如果需要重新以此开始，需删除序列文件.否则将以序列化后的文件数字，取整加2000开始 */
  private Integer startErrorCode;

  /** */
  private Boolean updateCode;

  /** 生成的代码相关的方法，按指定的顺序进行生成 */
  private List<MethodInfo> code;

  /** 代码内的目录树配制 */
  private CodeMenuTree codeMenuTree;

  /** 输出文件的配制 */
  private String output;

  /** 公共文件和路径 */
  private String commonPath;
}
