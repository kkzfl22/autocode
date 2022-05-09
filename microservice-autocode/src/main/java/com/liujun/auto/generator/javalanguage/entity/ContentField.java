package com.liujun.auto.generator.javalanguage.entity;

import com.liujun.auto.constant.Symbol;
import com.liujun.auto.generator.javalanguage.constant.VisitEnum;
import com.liujun.auto.generator.javalanguage.utils.JavaCodeOutUtils;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import org.apache.commons.lang3.StringUtils;

import java.util.List;

/**
 * 类的属性信息
 *
 * @author liujun
 * @since 2022/4/6
 */
@Getter
@Setter
@ToString
public class ContentField extends ContentBase {

  /** 属性的注释信息 */
  private ContextFieldDocument document;

  /** 属性的的注解信息 */
  private List<ContextAnnotation> annotation;

  /** 访问修饰符 */
  private VisitEnum visit;

  /** 类的类型信息 */
  private String classType;

  /** 属性名称 */
  private String name;

  /** 属性值信息 */
  private String value;

  @Override
  public String outCode() {

    StringBuilder outCode = new StringBuilder();

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
    // 访问修饰符
    outCode.append(visit.getVisit()).append(Symbol.SPACE).append(classType);
    outCode.append(Symbol.SPACE).append(name);

    if (StringUtils.isNotEmpty(value)) {
      outCode.append(Symbol.SPACE);
      outCode.append(this.getValue());
    }
    outCode.append(Symbol.SEMICOLON);
    outCode.append(Symbol.ENTER_LINE);

    return outCode.toString();
  }
}
