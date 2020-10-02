package com.liujun.micro.autocode.generator.builder.entity;

import com.liujun.micro.autocode.constant.Symbol;
import lombok.Builder;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import org.apache.commons.lang3.StringUtils;

import java.util.ArrayList;
import java.util.List;

/**
 * java的注解信息
 *
 * @author liujun
 * @version 0.0.1
 */
@Getter
@Setter
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
      outValue.append(value);
      outValue.append(Symbol.BRACKET_RIGHT);
    }

    return outValue.toString();
  }

  /**
   * 用来进行作为参数的build类
   *
   * @author liujun
   * @vsersion 0.0.1
   */
  public static class Builder {

    /** 注解符 */
    private String annotation;

    /** 用于进特注解参数的构建 */
    private List<AnnotationValueElement> annotationValue;

    /**
     * 进行数据的构建 当前为注解符
     *
     * @param annotation
     * @return
     */
    public JavaAnnotation.Builder annotation(String annotation) {
      this.annotation = annotation;
      return this;
    }

    /**
     * 进行数据的构建 ，单个注解的值
     *
     * @param value 单个注解的值
     * @return
     */
    public JavaAnnotation.Builder annotationValue(String value) {
      if (null == annotationValue) {
        annotationValue = new ArrayList<>();
      }

      annotationValue.add(new AnnotationValueElement(value));

      return this;
    }

    /**
     * 进行数据的构建 当前为注解符
     *
     * @param key 注解的key
     * @param value 注解的值
     * @return
     */
    public JavaAnnotation.Builder annotationValue(String key, String value) {
      if (null == annotationValue) {
        annotationValue = new ArrayList<>();
      }
      annotationValue.add(new AnnotationValueElement(key, value));
      return this;
    }

    /**
     * 进行数据的构建 当前为注解符
     *
     * @param notOutChar 是否字符输出的标识
     * @param key 注解的key
     * @param value 注解的值
     * @return
     */
    public JavaAnnotation.Builder annotationValue(boolean notOutChar, String key, String value) {
      if (null == annotationValue) {
        annotationValue = new ArrayList<>();
      }
      annotationValue.add(new AnnotationValueElement(notOutChar, key, value));
      return this;
    }

    public JavaAnnotation build() {
      return new JavaAnnotation(this);
    }
  }

  public JavaAnnotation(JavaAnnotation.Builder builder) {
    this.annotation = builder.annotation;
    this.value = getAnnotationValue(builder.annotationValue);
  }

  /**
   * 输出注解参数值信息
   *
   * @param value
   * @return
   */
  private String getAnnotationValue(List<AnnotationValueElement> value) {
    if (null == value || value.isEmpty()) {
      return Symbol.EMPTY;
    }

    StringBuilder outAnnotation = new StringBuilder();
    for (AnnotationValueElement valueElement : value) {
      if (StringUtils.isNotEmpty(valueElement.getKey())) {
        outAnnotation.append(valueElement.getKey()).append(Symbol.SPACE).append(Symbol.EQUAL);
        outAnnotation.append(Symbol.SPACE);
      }

      if (StringUtils.isNotEmpty(valueElement.getValue())) {
        // 默认作为值进行输出
        if (!valueElement.isNotCharFlag()) {
          outAnnotation.append(Symbol.QUOTE);
          outAnnotation.append(valueElement.getValue());
          outAnnotation.append(Symbol.QUOTE);
        }
        // 当值为true时，则不再作为字符串进行输出
        else {
          outAnnotation.append(valueElement.getValue());
        }
      }
      outAnnotation.append(Symbol.COMMA + Symbol.SPACE);
    }
    // 最后两个字符的删除
    outAnnotation.deleteCharAt(outAnnotation.length() - 1);
    outAnnotation.deleteCharAt(outAnnotation.length() - 1);
    return outAnnotation.toString();
  }

  public static JavaAnnotation.Builder builder() {
    return new JavaAnnotation.Builder();
  }
}
