package com.liujun.auto.generator.javalanguage.entity;

import com.liujun.auto.constant.Symbol;
import com.liujun.auto.generator.builder.ddd.constant.ClassCommonCfg;
import com.liujun.auto.generator.javalanguage.constant.PrefixSpaceEnum;
import com.liujun.auto.generator.javalanguage.utils.JavaCodeOutUtils;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

/**
 * 属性的注释
 *
 * @author liujun
 * @since 2022/4/6
 */
@Getter
@Setter
@ToString
public class ContextFieldDocument extends ContentBase implements OutCodeInf {

  /** 类注释信息 */
  private String comment;

  public static ContextFieldDocument buildFieldDoc(String comment) {
    ContextFieldDocument doc = new ContextFieldDocument();
    doc.setTopLine(ClassCommonCfg.FIELD_TOP_LINE);
    doc.setLeftSpace(PrefixSpaceEnum.ONE);
    doc.setComment(comment);
    return doc;
  }

  @Override
  public String outCode() {

    StringBuilder outCode = new StringBuilder();

    // 空行
    outCode.append(JavaCodeOutUtils.emptyLine(this.getTopLine()));

    // 注释输出
    outCode.append(JavaCodeOutUtils.outSpace(this.getLeftSpace()));
    outCode.append(Symbol.PATH).append(Symbol.STAR).append(Symbol.STAR).append(Symbol.ENTER_LINE);
    outCode.append(JavaCodeOutUtils.outSpace(this.getLeftSpace()));
    outCode.append(Symbol.SPACE).append(Symbol.STAR).append(Symbol.SPACE);
    outCode.append(comment).append(Symbol.ENTER_LINE);
    outCode.append(JavaCodeOutUtils.outSpace(this.getLeftSpace()));
    outCode.append(Symbol.SPACE).append(Symbol.STAR).append(Symbol.PATH).append(Symbol.ENTER_LINE);

    return outCode.toString();
  }
}
