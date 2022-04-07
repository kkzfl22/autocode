package com.liujun.auto.generator.builder.ddd.code;

import com.liujun.auto.config.generate.entity.MethodInfo;
import com.liujun.auto.config.generate.entity.WhereInfo;
import com.liujun.auto.constant.MyBatisOperatorFlag;
import com.liujun.auto.constant.Symbol;
import com.liujun.auto.generator.builder.ddd.constant.JavaVarName;
import com.liujun.auto.generator.builder.ddd.entity.ImportPackageInfo;
import com.liujun.auto.generator.builder.ddd.entity.JavaClassEntity;
import com.liujun.auto.generator.builder.ddd.entity.JavaClassFieldEntity;
import com.liujun.auto.generator.builder.utils.JavaClassCodeUtils;
import com.liujun.auto.generator.builder.utils.TableColumnUtils;
import com.liujun.auto.generator.convergence.TypeConvergence;
import com.liujun.auto.generator.database.constant.DatabaseTypeEnum;
import com.liujun.auto.generator.database.entity.TableColumnDTO;
import com.liujun.auto.generator.javalanguage.constant.JavaKeyWord;
import com.liujun.auto.generator.javalanguage.serivce.NameProcess;

import java.util.List;
import java.util.Map;

/**
 * 生前API层的相关常量信息
 *
 * @author liujun
 * @version 0.0.1
 */
public class GenerateJavaInterfaceConstant {

  public static final GenerateJavaInterfaceConstant INSTANCE = new GenerateJavaInterfaceConstant();

  /** 最大长度的标识符 */
  private static final String MAX_LENGTH_FLAG = "MAX_LENGTH_";

  /** 最大长度的限制 */
  private static final String MAX_LENGTH_TITLE = "最大长度的限制";

  /** 默认集合的最大值 */
  private static final String DEFAULT_MAX_LIST_SIZE = "16";

  /**
   * 生成错误码
   *
   * @param method 方法信息
   * @param interfaceConstant 常量类
   * @param tableColumnList 表列的信息
   * @param author 作者
   * @return 生成的代码
   */
  public StringBuilder generateInterfaceConstant(
      List<MethodInfo> method,
      DatabaseTypeEnum dbType,
      ImportPackageInfo interfaceConstant,
      List<TableColumnDTO> tableColumnList,
      String author) {

    // 类的定义
    StringBuilder sb = new StringBuilder();
    // 常量类文件的定义
    sb.append(classDefine(interfaceConstant, author));

    // 定义最大长度的的常量
    sb.append(fieldMaxAll(dbType, tableColumnList));

    // 定义最大长度的的常量
    sb.append(fieldWhereInMaxList(tableColumnList, method));

    // 类的结束
    JavaClassCodeUtils.classEnd(sb);

    return sb;
  }

  /**
   * 类的定义
   *
   * @param interfaceConstant 常量类的定义信息
   * @param author 作者
   * @return 返回当前构建的类信息
   */
  private String classDefine(ImportPackageInfo interfaceConstant, String author) {
    JavaClassEntity classEntity =
        JavaClassEntity.builder()
            // 包路径
            .packagePath(interfaceConstant.getPackagePath())
            // 类注释
            .classComment(interfaceConstant.getClassComment())
            .classKey(JavaKeyWord.CLASS_KEY)
            // 类名
            .className(interfaceConstant.getClassName())
            // 作者
            .author(author)
            .build();

    return JavaClassCodeUtils.javaClassDefine(classEntity).toString();
  }

  /**
   * 进行常量的输出操作
   *
   * @param dbType 数据库类型
   * @param tableColumnList 列信息
   * @return 输出
   */
  private String fieldMaxAll(DatabaseTypeEnum dbType, List<TableColumnDTO> tableColumnList) {
    StringBuilder outData = new StringBuilder();

    for (TableColumnDTO columnInfo : tableColumnList) {
      outData.append(fieldMaxLengthDefine(dbType, columnInfo));
      outData.append(Symbol.ENTER_LINE);
    }

    return outData.toString();
  }

  /**
   * where条件的in最大值
   *
   * @param tableColumnList 列信息
   * @param method 方法
   * @return 输出
   */
  private String fieldWhereInMaxList(
      List<TableColumnDTO> tableColumnList, List<MethodInfo> method) {
    StringBuilder outData = new StringBuilder();

    for (MethodInfo methodInfo : method) {
      outData.append(this.fieldWhereInMax(tableColumnList, methodInfo, outData));
    }

    return outData.toString();
  }

  /**
   * where条件的in最大值
   *
   * @param tableColumnList 列信息
   * @param method 方法
   * @return 输出
   */
  private String fieldWhereInMax(
      List<TableColumnDTO> tableColumnList, MethodInfo method, StringBuilder whereData) {
    StringBuilder outData = new StringBuilder();

    Map<String, TableColumnDTO> tableMap = TableColumnUtils.parseToMap(tableColumnList);

    for (WhereInfo whereInfo : method.getWhereInfo()) {
      if (MyBatisOperatorFlag.IN.equals(whereInfo.getOperatorFlag())) {

        String outName =
            whereInfo.getSqlColumn() + Symbol.UNDER_LINE + JavaVarName.NAME_LIST_SUFFIX;

        // 检查名称是否已经存
        if (whereData.indexOf(name(outName)) != -1) {
          continue;
        }

        TableColumnDTO tableInfo = tableMap.get(whereInfo.getSqlColumn());

        outData.append(fieldMaxLengthDefineList(outName, tableInfo.getColumnMsg()));
        outData.append(Symbol.ENTER_LINE);
      }
    }

    return outData.toString();
  }

  /**
   * 单个类属性的最大值的定义
   *
   * @param name 名称
   * @param comment 注释
   * @return 定义的代码信息
   */
  private String fieldMaxLengthDefineList(String name, String comment) {

    JavaClassFieldEntity classFieldEntity =
        JavaClassFieldEntity.builder()
            // 访问修饰符
            .visit(JavaKeyWord.PUBLIC)
            // 静态标识
            .staticFlag(JavaKeyWord.STATIC)
            // final
            .finalFlag(JavaKeyWord.FINAL)
            // 类型
            .type(JavaKeyWord.INT_TYPE)
            // 名称,常量以列名全大写，加MAX_LENGTH结束
            .name(name(name))
            // 注释
            .comment(comment + MAX_LENGTH_TITLE)
            // 构建最大值
            .value(DEFAULT_MAX_LIST_SIZE)
            .build();

    // 类属性的输出
    return JavaClassCodeUtils.getClassField(classFieldEntity);
  }

  /**
   * 单个类属性的最大值的定义
   *
   * @param dbType 类型信息
   * @param tableColumn 表的列信息
   * @return 定义的代码信息
   */
  private String fieldMaxLengthDefine(DatabaseTypeEnum dbType, TableColumnDTO tableColumn) {

    JavaClassFieldEntity classFieldEntity =
        JavaClassFieldEntity.builder()
            // 访问修饰符
            .visit(JavaKeyWord.PUBLIC)
            // 静态标识
            .staticFlag(JavaKeyWord.STATIC)
            // final
            .finalFlag(JavaKeyWord.FINAL)
            // 类型
            .type(JavaKeyWord.INT_TYPE)
            // 名称,常量以列名全大写，加MAX_LENGTH结束
            .name(name(tableColumn.getColumnName()))
            // 注释
            .comment(tableColumn.getColumnMsg() + MAX_LENGTH_TITLE)
            // 构建最大值
            .value(String.valueOf(maxLength(dbType, tableColumn)))
            .build();

    // 类属性的输出
    return JavaClassCodeUtils.getClassField(classFieldEntity);
  }

  /**
   * 最大值
   *
   * @param dbType 类型信息
   * @param tableColumn 表列信息
   * @return 类型的最大长度
   */
  private Long maxLength(DatabaseTypeEnum dbType, TableColumnDTO tableColumn) {
    if (tableColumn.getDataLength() != null && tableColumn.getDataLength() != 0) {
      return tableColumn.getDataLength();
    } else {
      return TypeConvergence.getDbTypeMax(dbType, tableColumn);
    }
  }

  /**
   * MAX_LENGTH开始 加以列名全大写结束
   *
   * @param columnName
   * @return
   */
  public static String name(String columnName) {
    StringBuilder outName = new StringBuilder();
    outName.append(MAX_LENGTH_FLAG);
    outName.append(NameProcess.INSTANCE.toNameUpperCase(columnName));

    return outName.toString();
  }
}
