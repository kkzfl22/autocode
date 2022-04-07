package com.liujun.auto.generator.builder.ddd.full.other;

import com.liujun.auto.constant.Symbol;
import com.liujun.auto.generator.builder.ddd.code.GenerateJavaErrorCode;
import com.liujun.auto.generator.builder.ddd.entity.GenerateCodeContext;
import com.liujun.auto.generator.builder.GenerateCodeInf;
import com.liujun.auto.generator.builder.utils.GenerateOutFileUtils;
import com.liujun.auto.generator.builder.utils.GeneratePathUtils;
import com.liujun.auto.generator.builder.utils.JavaEncodeUtils;
import com.liujun.auto.generator.database.entity.TableColumnDTO;
import com.liujun.auto.generator.javalanguage.serivce.NameProcess;

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
public class JavaCodeResourceI18nEnUsCreate implements GenerateCodeInf {

  /** 中文的资源名称 */
  private static final String RESOURCE_CHINA_NAME = "resource_en_US.properties";

  /** 为空的提示 */
  private static final String NULL_TITLE = " can't be empty";

  /** 超长长度的提示 */
  private static final String MAX_TITLE = " Exceeding the limit in length (#maxLength#) ";

  public static final JavaCodeResourceI18nEnUsCreate INSTANCE =
      new JavaCodeResourceI18nEnUsCreate();

  @Override
  public void generateCode(GenerateCodeContext param) {

    // 获取以错误码包的路径
    String resourceI18nPath = param.getProjectPath().getI18nNode().outPath();

    // 需要执行文件清理，再生成
    GenerateOutFileUtils.cleanFile(
        GeneratePathUtils.outServicePath(param), resourceI18nPath, RESOURCE_CHINA_NAME);

    Map<String, List<TableColumnDTO>> map = param.getColumnMapList();
    Iterator<Entry<String, List<TableColumnDTO>>> tableNameEntry = map.entrySet().iterator();
    while (tableNameEntry.hasNext()) {
      Entry<String, List<TableColumnDTO>> tableNameItem = tableNameEntry.next();

      // 代码的生成操作
      StringBuilder sb = this.generateResource(tableNameItem.getValue());

      // 进行存储层的接口输出
      GenerateOutFileUtils.outFile(
          sb, GeneratePathUtils.outServicePath(param), resourceI18nPath, RESOURCE_CHINA_NAME, true);
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

      String fieldName = NameProcess.INSTANCE.toFieldName(columnInfo.getColumnName());

      // 1为空的检查的错误码,当前列不能为空，则需要进行设置操作错误码
      if (!columnInfo.getNullFlag()) {
        String outPropertyKeyNull =
            GenerateJavaErrorCode.propertiesErrorKeyNull(
                columnInfo.getTableName(), columnInfo.getColumnName());
        sb.append(
            outResource(outPropertyKeyNull, columnInfo.getColumnMsg(), fieldName + NULL_TITLE));
      }

      // 2，超过长度的检查
      String outPropertyKeyMax =
          GenerateJavaErrorCode.propertiesErrorKeyMax(
              columnInfo.getTableName(), columnInfo.getColumnName());
      sb.append(outResource(outPropertyKeyMax, columnInfo.getColumnMsg(), fieldName + MAX_TITLE));
    }

    return sb;
  }

  private String outResource(String outPropertyKeyNull, String comment, String title) {
    StringBuilder sb = new StringBuilder();
    sb.append(Symbol.POUND)
        .append(JavaEncodeUtils.native2ascii(comment))
        .append(Symbol.SPACE);
    sb.append(Symbol.ENTER_LINE);
    sb.append(outPropertyKeyNull).append(Symbol.EQUAL);
    sb.append(JavaEncodeUtils.native2ascii(title));
    sb.append(Symbol.ENTER_LINE);
    return sb.toString();
  }
}
