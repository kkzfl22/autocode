package com.liujun.micro.autocode.generator.builder.operator.utils;

import com.liujun.micro.autocode.config.generate.entity.MethodInfo;
import com.liujun.micro.autocode.constant.GenerateDefineFlag;
import com.liujun.micro.autocode.constant.Symbol;
import com.liujun.micro.autocode.generator.builder.constant.ImportCodePackageKey;
import com.liujun.micro.autocode.generator.builder.constant.JavaVarName;
import com.liujun.micro.autocode.generator.builder.entity.JavaMethodArguments;
import org.apache.commons.lang3.StringUtils;

import java.util.ArrayList;
import java.util.List;

/**
 * java的注释处理
 *
 * @author liujun
 * @version 0.0.1
 */
public class JavaCommentUtil {

  public static final String TABLE = "表";

  /**
   * 数据库的字符处理，主要是去掉表字
   *
   * @param tableComment
   * @return
   */
  public static String tableCommentProc(String tableComment) {
    if (StringUtils.isEmpty(tableComment)) {
      return Symbol.EMPTY;
    }

    tableComment = tableComment.trim();

    // 将表字符替换为-号
    tableComment = tableComment.replaceAll(TABLE, Symbol.MINUS);

    return tableComment;
  }

  /**
   * 执行分页查询的参数处理
   *
   * @param methodItem 方法信息
   * @param poClassName 当前的类信息
   * @return
   */
  public static List<JavaMethodArguments> pageQueryParam(
      MethodInfo methodItem, String poClassName) {
    List<JavaMethodArguments> argumentsList = new ArrayList<>(methodItem.getParamType().size());

    // 带泛型处理
    String outClass =
        ImportCodePackageKey.PAGE_PARAM.getPackageInfo().getClassName()
            + ImportCodePackageKey.PAGE_PARAM.getPackageInfo().getGeneric();
    if (outClass.indexOf(GenerateDefineFlag.TABLE_NAME.getDefineFlag()) != -1) {
      outClass = outClass.replaceAll(GenerateDefineFlag.TABLE_NAME.getDefineFlag(), poClassName);
    }

    // 分页添加分页参数
    JavaMethodArguments pageReq =
        JavaMethodArguments.builder()
            .type(outClass)
            .name(JavaVarName.PAGE_REQUEST)
            .comment(ImportCodePackageKey.PAGE_PARAM.getPackageInfo().getClassComment())
            .build();

    argumentsList.add(pageReq);

    return argumentsList;
  }

  /**
   * 执行分页查询的返回处理
   *
   * @param poClassName 当前的类信息
   * @return
   */
  public static String pageQueryReturn(String poClassName) {
    String resultClass =
        ImportCodePackageKey.PAGE_RESULT.getPackageInfo().getClassName()
            + ImportCodePackageKey.PAGE_RESULT.getPackageInfo().getGeneric();

    if (StringUtils.isNotEmpty(ImportCodePackageKey.PAGE_RESULT.getPackageInfo().getGeneric())) {
      resultClass =
          resultClass.replaceAll(GenerateDefineFlag.TABLE_NAME.getDefineFlag(), poClassName);
    }

    return resultClass;
  }
}
