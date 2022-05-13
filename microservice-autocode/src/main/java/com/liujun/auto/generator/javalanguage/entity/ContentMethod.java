package com.liujun.auto.generator.javalanguage.entity;

import com.liujun.auto.constant.Symbol;
import com.liujun.auto.generator.javalanguage.constant.JavaKeyWord;
import com.liujun.auto.generator.javalanguage.constant.VisitEnum;
import com.liujun.auto.generator.javalanguage.utils.JavaCodeOutUtils;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import org.apache.commons.lang3.StringUtils;

import java.util.List;

/**
 * 类的方法信息
 *
 * @author liujun
 * @since 2022/4/6
 */
@Getter
@Setter
@ToString
public class ContentMethod extends ContentBase {

  /** 属性的注释信息 */
  private ContextMethodDocument document;

  /** 属性的的注解信息 */
  private List<ContextAnnotation> annotation;

  /** 属性的访问修饰符 */
  private VisitEnum visit;

  /** 静态标识 */
  private boolean staticFlag;

  /** 不可修改的标识 final标识的 */
  private boolean finalFlag;

  /** 返回的类型 */
  private String returnClass;

  /** 方法名称 */
  private String name;

  /** 方法的参数信息 */
  private List<ContextMethodParam> param;

  /** 方法中的代码行信息 */
  private List<ContextLineCode> codeLine;

  @Override
  public String outCode() {
    StringBuilder outCode = new StringBuilder();

    // 先输出空行
    outCode.append(JavaCodeOutUtils.emptyLine(this.getTopLine()));
    // 输出注释
    if (null != document) {
      outCode.append(document.outCode());
    }
    if (this.getAnnotation() != null) {
      // 输出左空格数
      outCode.append(JavaCodeOutUtils.outSpace(this.getLeftSpace()));
      // 输出注解
      outCode.append(JavaCodeOutUtils.outAnnotation(this.getAnnotation()));
    }

    // 输出左空格数
    outCode.append(JavaCodeOutUtils.outSpace(this.getLeftSpace()));
    if (StringUtils.isNotEmpty(visit.getVisit())) {
      // 访问修饰符
      outCode.append(visit.getVisit()).append(Symbol.SPACE);
    }

    // 静态标识
    if (staticFlag) {
      outCode.append(JavaKeyWord.STATIC).append(Symbol.SPACE);
    }
    // final标识
    if (finalFlag) {
      outCode.append(JavaKeyWord.FINAL).append(Symbol.SPACE);
    }
    // 返回类型
    outCode.append(returnClass).append(Symbol.SPACE);
    // 方法名
    outCode.append(name);
    // 方法参数
    outCode.append(JavaCodeOutUtils.outParam(param));

    // 执行代码行的输出操作
    if (null != codeLine && !codeLine.isEmpty()) {
      // 方法左括号
      outCode.append(Symbol.SPACE);
      outCode.append(Symbol.BRACE_LEFT).append(Symbol.ENTER_LINE);
      for (ContextLineCode lineCodeItem : codeLine) {
        outCode.append(lineCodeItem.outCode());
      }
      // 右括号
      outCode.append(JavaCodeOutUtils.outSpace(this.getLeftSpace()));
      outCode.append(Symbol.BRACE_RIGHT);
    } else {
      outCode.append(Symbol.SEMICOLON);
    }

    outCode.append(Symbol.ENTER_LINE);

    return outCode.toString();
  }
}
