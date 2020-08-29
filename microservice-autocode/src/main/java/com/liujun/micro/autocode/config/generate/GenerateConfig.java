package com.liujun.micro.autocode.config.generate;

import com.liujun.micro.autocode.constant.GenerateDefineFlag;
import com.liujun.micro.autocode.constant.Symbol;
import com.liujun.micro.autocode.entity.config.GenerateConfigEntity;
import com.liujun.micro.autocode.entity.config.MethodInfo;
import com.liujun.micro.autocode.entity.config.TypeInfo;
import com.liujun.micro.autocode.entity.config.WhereInfo;
import com.liujun.micro.autocode.generator.builder.constant.MethodTypeEnum;
import com.liujun.micro.autocode.generator.javalanguage.constant.JavaKeyWord;
import com.liujun.micro.autocode.utils.StreamUtils;
import org.apache.commons.lang3.StringUtils;
import org.yaml.snakeyaml.Yaml;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

/**
 * 生成代码的相关配制信息
 *
 * @author liujun
 * @version 0.0.1
 * @date 2019/09/10
 */
public class GenerateConfig {

  /** 配制的文件路径 */
  private static final String LOAD_CONFIG = "config/generate/generate.yml";

  public static final GenerateConfig INSTANCE = new GenerateConfig();

  /** 数据加载操作 */
  private final GenerateConfigEntity cfgEntity;

  private GenerateConfig() {
    cfgEntity = loadCfg();

    // 执行数据处理
    process(cfgEntity.getGenerate().getCode());
  }

  /**
   * 进行数据解析操作
   *
   * @param methodList 方法列表
   */
  private void process(List<MethodInfo> methodList) {
    // 1,对代码方法进行排序
    Collections.sort(cfgEntity.getGenerate().getCode());

    // 进行方法的参数设置
    for (MethodInfo method : methodList) {
      // 设置参数信息
      method.setParamType(this.getParamType(method.getParams()));
      // 设置返回值
      method.setReturnType(this.getReturnType(method, method.getReturns()));
      // 设置where条件
      method.setWhereInfo(this.getWhereInfo(method.getWhere()));
    }
  }

  /**
   * 获取返回的类型对象信息
   *
   * @param dataItem 数所类型信息
   * @return 类型信息
   */
  private List<TypeInfo> getParamType(String dataItem) {

    if (StringUtils.isEmpty(dataItem)) {
      return Arrays.asList(this.getTypeEntity(GenerateDefineFlag.TABLE_NAME.getDefineFlag()));
    }

    return this.getTypeInfo(dataItem);
  }

  /**
   * 获取返回的类型对象信息
   *
   * @param method 方法
   * @param dataItem 数所类型信息
   * @return 类型信息
   */
  private TypeInfo getReturnType(MethodInfo method, String dataItem) {

    if (method.getOperator().equals(MethodTypeEnum.UPDATE.getType())) {
      return new TypeInfo(JavaKeyWord.INT_TYPE);
    }

    return this.getTypeEntity(dataItem);
  }

  /**
   * 其存在以下这几种情况
   *
   * <p>java.util.List<$table_name> 集合直接与数据库对应
   *
   * <p>$table_name 单对象 与数据库对象对应
   *
   * <p>String,String 参数 通过where条件中的字段与数据库对应
   *
   * @param paramTypeStr
   * @return
   */
  private List<TypeInfo> getTypeInfo(String paramTypeStr) {
    if (StringUtils.isEmpty(paramTypeStr)) {
      return Collections.EMPTY_LIST;
    }

    String[] dataInfo = paramTypeStr.split(Symbol.COMMA);
    List<TypeInfo> dataList = new ArrayList<>(dataInfo.length);
    for (String dataItem : dataInfo) {
      TypeInfo entity = getTypeEntity(dataItem);
      if (null != entity) {
        dataList.add(entity);
      }
    }

    return dataList;
  }

  /**
   * 获取类型的实体信息
   *
   * @param dataItem 字符配制
   * @return
   */
  private TypeInfo getTypeEntity(String dataItem) {
    if (StringUtils.isEmpty(dataItem)) {
      return null;
    }

    // 如果存在尖括号说明为集合带泛型的情况
    int leftIndex = dataItem.indexOf(Symbol.ANGLE_BRACKETS_LEFT);
    if (leftIndex != -1) {
      String name = dataItem.substring(0, leftIndex);
      int subIndex = name.lastIndexOf(Symbol.POINT);
      subIndex = subIndex == -1 ? 0 : subIndex + 1;
      String className = dataItem.substring(subIndex);

      TypeInfo type = new TypeInfo(name, className);
      return type;
    }

    // 检查当前是否存在.的情况，带路径，需要导入
    int indexPoint = dataItem.indexOf(Symbol.COMMA);
    if (indexPoint != -1) {
      String className = dataItem.substring(indexPoint);
      TypeInfo type = new TypeInfo(dataItem, className);
      return type;
    }

    // 当其他情况都不匹配，说当前不需要导入包
    return new TypeInfo(dataItem);
  }

  private List<WhereInfo> getWhereInfo(String whereStr) {
    if (StringUtils.isEmpty(whereStr)) {
      return Collections.EMPTY_LIST;
    }

    String[] dataInfo = whereStr.split(Symbol.COMMA);
    List<WhereInfo> dataList = new ArrayList<>(dataInfo.length);

    for (String whereColumn : dataInfo) {
      if (StringUtils.isEmpty(whereColumn)) {
        continue;
      }

      whereColumn = whereColumn.toLowerCase();

      int spaceIndex = whereColumn.indexOf(Symbol.SPACE);
      if (spaceIndex == -1) {
        dataList.add(new WhereInfo(whereColumn));
      } else {
        String[] info = whereColumn.split(Symbol.SPACE);
        dataList.add(new WhereInfo(info[0], info[1]));
      }
    }
    return dataList;
  }

  /**
   * 载加配制信息
   *
   * @return
   */
  private static GenerateConfigEntity loadCfg() {
    Yaml yaml = new Yaml();
    InputStream input = null;
    GenerateConfigEntity config = null;

    try {
      // 优先加载外部配制文件
      input = getOutFileStream(LOAD_CONFIG);
      if (null == input) {
        // 当外部文件不存时，则使用内部配制文件
        input = GenerateConfig.class.getClassLoader().getResourceAsStream(LOAD_CONFIG);
      }
      if (null == input) {
        throw new IllegalArgumentException(LOAD_CONFIG + " load error");
      }
      config = yaml.loadAs(input, GenerateConfigEntity.class);
    } finally {
      StreamUtils.close(input);
    }

    return config;
  }

  /**
   * 获取外部文件的流
   *
   * @param path 外部文件路径
   * @return 文件流
   */
  private static InputStream getOutFileStream(String path) {
    InputStream outFileStream = null;

    try {
      outFileStream = new FileInputStream(path);
    }
    // 当外部文件不存在时，会报出文件不存在异常，此异常需忽略，后续加载内置文件即可
    catch (FileNotFoundException e) {
    }

    return outFileStream;
  }

  /**
   * 配制 文件的实体信息
   *
   * @return
   */
  public GenerateConfigEntity getCfgEntity() {
    return cfgEntity;
  }
}
