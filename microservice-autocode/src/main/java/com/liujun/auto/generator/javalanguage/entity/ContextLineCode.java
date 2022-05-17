package com.liujun.auto.generator.javalanguage.entity;

import com.liujun.auto.constant.Symbol;
import com.liujun.auto.generator.builder.ddd.entity.AnnotationValueElement;
import com.liujun.auto.generator.builder.ddd.entity.JavaAnnotation;
import com.liujun.auto.generator.javalanguage.constant.JavaKeyWord;
import com.liujun.auto.generator.javalanguage.constant.PrefixSpaceEnum;
import com.liujun.auto.generator.javalanguage.utils.JavaCodeOutUtils;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.util.ArrayList;
import java.util.List;

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

  /** 是否结束分号,默认为添加分号，设置为false则不添加 */
  private Boolean semicolon;

  private ContextLineCode() {}

  public ContextLineCode(Builder builder) {
    this.setTopLine(builder.topLine);
    this.setLeftSpace(builder.leftSpace);
    this.setLineCode(builder.lineCode);
    this.setSemicolon(builder.semicolon);
  }

  /**
   * 默认和行构建器，一般正常行为二级
   *
   * @param code 的信息
   * @return
   */
  public static ContextLineCode builderCodeFinish(String code) {
    return builder().leftSpace(PrefixSpaceEnum.TWO).lineCode(code).semicolon(Boolean.TRUE).build();
  }

  /**
   * 默认和行构建器，
   *
   * @param code 的信息
   * @return
   */
  public static ContextLineCode builderCodeFinish(PrefixSpaceEnum leftSpace, String code) {
    return builder().leftSpace(leftSpace).lineCode(code).semicolon(Boolean.TRUE).build();
  }

  /**
   * 默认和行构建器， 标识当前不需要添加分号分隔符
   *
   * @param code 的信息
   * @return
   */
  public static ContextLineCode builderCodeUnFinish(PrefixSpaceEnum leftSpace, String code) {
    return builder().leftSpace(leftSpace).lineCode(code).semicolon(Boolean.FALSE).build();
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
    if (this.getSemicolon() == null || this.getSemicolon()) {
      outCode.append(Symbol.SEMICOLON);
    }
    outCode.append(Symbol.ENTER_LINE);

    return outCode.toString();
  }

  /**
   * 用来进行作为参数的build类
   *
   * @author liujun
   * @vsersion 0.0.1
   */
  public static class Builder {

    /** 空格数 */
    private int topLine;

    /** 左边空格数 */
    private PrefixSpaceEnum leftSpace;

    /** 返回的标识 */
    private String lineCode;

    /** 是否结束分号,默认为添加分号，设置为false则不添加 */
    private Boolean semicolon;

    /**
     * 头部空行设置
     *
     * @param topLine
     * @return
     */
    public ContextLineCode.Builder topLine(int topLine) {
      this.topLine = topLine;
      return this;
    }
    /**
     * 左边空格设置
     *
     * @param leftSpace
     * @return
     */
    public ContextLineCode.Builder leftSpace(PrefixSpaceEnum leftSpace) {
      this.leftSpace = leftSpace;
      return this;
    }

    /**
     * 代码行信息
     *
     * @param lineCode
     * @return
     */
    public ContextLineCode.Builder lineCode(String lineCode) {
      this.lineCode = lineCode;
      return this;
    }
    /**
     * 是否有结束分隔符
     *
     * @param semicolon
     * @return
     */
    public ContextLineCode.Builder semicolon(Boolean semicolon) {
      this.semicolon = semicolon;
      return this;
    }

    public ContextLineCode build() {
      return new ContextLineCode(this);
    }
  }

  public static ContextLineCode.Builder builder() {
    return new ContextLineCode.Builder();
  }
}
