package com.liujun.auto.generator.javalanguage.entity;

import com.liujun.auto.constant.Symbol;
import com.liujun.auto.generator.builder.ddd.constant.ClassCommonCfg;
import com.liujun.auto.generator.javalanguage.constant.PrefixSpaceEnum;
import com.liujun.auto.generator.javalanguage.utils.JavaCodeOutUtils;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import org.apache.commons.lang3.StringUtils;

import java.util.List;

/**
 * 方法的注释
 *
 * @author liujun
 * @since 2022/4/6
 */
@Getter
@Setter
@ToString
public class ContextMethodDocument extends ContentBase {

  /** 类注释信息 */
  private String comment;

  /** 参数信息 */
  private List<ContextMethodParamDocument> paramDocuments;

  public static ContextMethodDocument buildMethodDoc(
      String comment, List<ContextMethodParamDocument> paramDocuments) {
    ContextMethodDocument doc = new ContextMethodDocument();
    doc.setTopLine(ClassCommonCfg.FIELD_TOP_LINE);
    doc.setLeftSpace(PrefixSpaceEnum.ONE);
    doc.setComment(comment);
    doc.setParamDocuments(paramDocuments);
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
    outCode.append(Symbol.SPACE).append(Symbol.STAR);
    outCode.append(Symbol.ENTER_LINE);

    // 输出参数信息
    for (ContextMethodParamDocument param : paramDocuments) {
      outCode.append(JavaCodeOutUtils.outSpace(this.getLeftSpace()));
      outCode.append(Symbol.SPACE).append(Symbol.STAR).append(Symbol.SPACE);
      outCode.append(param.getAnnotation()).append(Symbol.SPACE);

      // 检查参数名是否存在
      if (StringUtils.isNotEmpty(param.getParamName())) {
        outCode.append(param.getParamName()).append(Symbol.SPACE);
      }
      outCode.append(param.getComment());
      outCode.append(Symbol.ENTER_LINE);
    }

    outCode.append(JavaCodeOutUtils.outSpace(this.getLeftSpace()));
    outCode.append(Symbol.SPACE).append(Symbol.STAR).append(Symbol.PATH).append(Symbol.ENTER_LINE);

    return outCode.toString();
  }
}
