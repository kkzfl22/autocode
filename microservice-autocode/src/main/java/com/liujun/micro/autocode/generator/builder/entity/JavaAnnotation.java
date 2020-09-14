package com.liujun.micro.autocode.generator.builder.entity;

import com.liujun.micro.autocode.constant.Symbol;
import lombok.Builder;
import lombok.Data;
import lombok.ToString;
import org.apache.commons.lang3.StringUtils;

/**
 * java的注解信息
 *
 * @author liujun
 * @version 0.0.1
 */
@Data
@Builder
@ToString
public class JavaAnnotation {

  /** 注解符 */
  private String annotation;

  /** 注解的值 */
  private String value;

  /**
   * 进行注解的输出
   *
   * @return
   */
  public String outAnnotation() {
    StringBuilder outValue = new StringBuilder();

    outValue.append(annotation);

    // 当值不为空，则进行输出
    if (StringUtils.isNotEmpty(value)) {
      outValue.append(Symbol.BRACKET_LEFT);
      outValue.append(Symbol.QUOTE);
      outValue.append(value);
      outValue.append(Symbol.QUOTE);
      outValue.append(Symbol.BRACKET_RIGHT);
    }

    return outValue.toString();
  }
}
