package com.liujun.auto.generator.javalanguage.entity;

import com.liujun.auto.generator.javalanguage.constant.PrefixSpaceEnum;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

/**
 * 基础的class内容信息
 *
 * @author liujun
 * @since 2022/4/6
 */
@Setter
@Getter
@ToString
public abstract class ContentBase implements OutCodeInf {

  /** 空格数 */
  private int topLine;

  /** 左边空格数 */
  private PrefixSpaceEnum leftSpace;
}
