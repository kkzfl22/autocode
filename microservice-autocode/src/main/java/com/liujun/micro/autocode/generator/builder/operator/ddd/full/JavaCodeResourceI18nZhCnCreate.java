package com.liujun.micro.autocode.generator.builder.operator.ddd.full;

import com.liujun.micro.autocode.constant.Symbol;
import com.liujun.micro.autocode.generator.builder.entity.GenerateCodeContext;
import com.liujun.micro.autocode.generator.builder.operator.GenerateCodeInf;
import com.liujun.micro.autocode.generator.builder.operator.code.GenerateJavaErrorCode;
import com.liujun.micro.autocode.generator.builder.operator.utils.GenerateOutFileUtils;
import com.liujun.micro.autocode.generator.database.entity.TableColumnDTO;

import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

/**
 * 生成中文的错误码信息
 *
 * @author liujun
 * @version 1.0.0
 */
public class JavaCodeResourceI18nZhCnCreate implements GenerateCodeInf {

  /** 中文的资源名称 */
  private static final String RESOURCE_CHINA_NAME = "resource_zh_CN.properties";

  /** 为空的提示 */
  private static final String NULL_TITLE = "不能为空";

  /** 超长长度的提示 */
  private static final String MAX_TITLE = "超过最大长度";

  @Override
  public void generateCode(GenerateCodeContext param) {

    Map<String, List<TableColumnDTO>> map = param.getColumnMapList();
    Iterator<Entry<String, List<TableColumnDTO>>> tableNameEntry = map.entrySet().iterator();
    while (tableNameEntry.hasNext()) {
      Entry<String, List<TableColumnDTO>> tableNameItem = tableNameEntry.next();

      // 获取以错误码包的路径
      String resourceI18nPath = param.getProjectPath().getI18nNode().outPath();

      // 代码的生成操作
      StringBuilder sb = this.generateResource(tableNameItem.getValue());

      // 进行存储层的接口输出
      GenerateOutFileUtils.outFile(
          sb, param.getFileBasePath(), resourceI18nPath, RESOURCE_CHINA_NAME);
    }
  }

  /**
   * 生成错误码
   *
   * @param tableColumnList 表列的信息
   * @return 生成的代码
   */
  public StringBuilder generateResource(List<TableColumnDTO> tableColumnList) {

    // 类的定义
    StringBuilder sb = new StringBuilder();

    // 错误码的类处理操作
    for (TableColumnDTO columnInfo : tableColumnList) {

      // 1为空的检查的错误码,当前列不能为空，则需要进行设置操作错误码
      if (!columnInfo.getNullFlag()) {
        String outPropertyKeyNull =
            GenerateJavaErrorCode.propertiesErrorKeyNull(
                columnInfo.getTableName(), columnInfo.getColumnName());
        String title = columnInfo.getColumnMsg() + NULL_TITLE;
        sb.append(outResource(outPropertyKeyNull, columnInfo.getColumnMsg(), title));
      }

      // 2，超过长度的检查
      String outPropertyKeyMax =
          GenerateJavaErrorCode.propertiesErrorKeyMax(
              columnInfo.getTableName(), columnInfo.getColumnName());

      String title = columnInfo.getColumnMsg() + MAX_TITLE;
      sb.append(outResource(outPropertyKeyMax, columnInfo.getColumnMsg(), title));
    }

    return sb;
  }

  private String outResource(String outPropertyKeyNull, String comment, String title) {
    StringBuilder sb = new StringBuilder();
    sb.append(Symbol.POUND).append(comment).append(Symbol.SPACE);
    sb.append(Symbol.ENTER_LINE);
    sb.append(outPropertyKeyNull).append(Symbol.EQUAL);
    sb.append(title);
    sb.append(Symbol.ENTER_LINE);
    return sb.toString();
  }
}
