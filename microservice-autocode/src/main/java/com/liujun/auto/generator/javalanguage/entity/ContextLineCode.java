package com.liujun.auto.generator.javalanguage.entity;

import com.liujun.auto.constant.Symbol;
import com.liujun.auto.generator.javalanguage.constant.JavaKeyWord;
import com.liujun.auto.generator.javalanguage.constant.PrefixSpaceEnum;
import com.liujun.auto.generator.javalanguage.utils.JavaCodeOutUtils;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

/**
 * 最基础的代码行信息
 *
 * @author liujun
 * @since 2022/4/6
 */
@Getter
@Setter
@ToString
public class ContextLineCode extends ContentBase {

  /** 返回的标识 */
  private String lineCode;

  /**
   * 默认和行构建器，一般正常行为二级
   *
   * @param code 的信息
   * @return
   */
  public static ContextLineCode builderCode(String code) {
    ContextLineCode line = new ContextLineCode();

    line.setLeftSpace(PrefixSpaceEnum.TWO);
    line.setLineCode(code);
    return line;
  }

  @Override
  public String outCode() {
    StringBuilder outCode = new StringBuilder();

    // 空行
    outCode.append(JavaCodeOutUtils.emptyLine(this.getTopLine()));

    // 左边格式的空格
    outCode.append(JavaCodeOutUtils.outSpace(this.getLeftSpace()));
    // 代码输出
    outCode.append(lineCode);
    // 换行
    outCode.append(Symbol.SEMICOLON).append(Symbol.ENTER_LINE);

    return outCode.toString();
  }
}
