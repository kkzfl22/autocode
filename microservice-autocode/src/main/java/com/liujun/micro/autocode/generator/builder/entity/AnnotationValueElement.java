package com.liujun.micro.autocode.generator.builder.entity;

import lombok.Getter;
import lombok.ToString;

/**
 * 注解的元素信息
 *
 * @author liujun
 * @version 0.0.1
 */
@Getter
@ToString
public class AnnotationValueElement {

  /** 标识信息 */
  private String key;

  /** 输出字符的标识,当值为默认时，输出字符，为true，则按原字符进行输出,即不加引号 */
  private boolean notCharFlag;

  /** 值信息 */
  private String value;

  public AnnotationValueElement(String key, String value) {
    this.key = key;
    this.value = value;
  }

  public AnnotationValueElement(String value) {
    this.value = value;
  }

  public AnnotationValueElement(boolean notCharFlag, String key, String value) {
    this(key, value);
    this.notCharFlag = notCharFlag;
  }
}
