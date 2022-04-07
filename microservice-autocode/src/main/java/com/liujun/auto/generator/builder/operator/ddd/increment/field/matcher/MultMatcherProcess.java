package com.liujun.auto.generator.builder.operator.ddd.increment.field.matcher;

import com.liujun.auto.algorithm.ahocorsick.AhoCorasickChar;
import com.liujun.auto.algorithm.ahocorsick.BaseAhoCorasick;
import com.liujun.auto.algorithm.ahocorsick.MatcherBusi;
import lombok.extern.slf4j.Slf4j;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.function.Consumer;

/**
 * 多字符的匹配处理
 *
 * @author liujun
 * @version 0.0.1
 */
@Slf4j
public class MultMatcherProcess {

  /** 多字符串匹配的实例构建 */
  private final BaseAhoCorasick ahoInstance;

  /** 操作示例 */
  public final Map<String, Consumer<MatcherFieldContext>> runFieldMap;

  private MultMatcherProcess(Map<String, Consumer<MatcherFieldContext>> runFieldMap) {
    List<String> dataList = new ArrayList<>(runFieldMap.keySet());
    this.runFieldMap = new HashMap<>(runFieldMap);
    // 实例化
    this.ahoInstance = new AhoCorasickChar();
    // 构建多模式串
    this.ahoInstance.buildFailure(dataList);
    // 构建多模式串匹配的指针
    this.ahoInstance.builderFailurePointer();
  }

  /**
   * 实例化工厂方法
   *
   * @param runFieldMap
   * @return
   */
  public static MultMatcherProcess getInstance(
      Map<String, Consumer<MatcherFieldContext>> runFieldMap) {

    if (null == runFieldMap || runFieldMap.isEmpty()) {
      throw new IllegalArgumentException("mult matcher is null");
    }

    MultMatcherProcess instance = new MultMatcherProcess(runFieldMap);
    return instance;
  }

  /**
   * 多字符串的默认处理
   *
   * <p>在默认处理中，会进行默认的字符的增量处理
   *
   * @param context 处理的上下文对象信息
   * @return
   */
  public String charValueProcDefault(MatcherFieldContext context) {

    // 执行字符的替换操作
    int matNum = this.charValueProc(context);
    log.info("log mult matcher num {}", matNum);

    // 将最后剩余的字符拷贝到结果中
    if (context.getLastIndex() < context.getSrcDataArray().length) {
      String lastValue =
          new String(
              context.getSrcDataArray(),
              context.getLastIndex(),
              context.getSrcDataArray().length - context.getLastIndex());
      context.getOutValueIncrement().append(lastValue);
    }

    return context.getOutValueIncrement().toString();
  }

  /**
   * 进行字符串的处理 w *
   *
   * @param context
   */
  public int charValueProc(MatcherFieldContext context) {
    int matchNum = 0;
    int i = 0;
    // 匹配模式，按最小一个模式匹配，估得大次数匹配,不使用true，担心小心使用成死循环
    while (i < context.getSrcDataArray().length) {
      MatcherBusi matcherBusi =
          ahoInstance.matcherOne(context.getSrcDataArray(), context.getLastIndex());

      // 当模式串被找到，则执行对应的操作
      if (matcherBusi.getMatcherIndex() != -1) {
        // 执行替换操作
        Consumer<MatcherFieldContext> runReplace = runFieldMap.get(matcherBusi.getMatcherKey());

        // 设置当前查询到的信息
        context.setMatcherBusi(matcherBusi);
        runReplace.accept(context);

        // 每执行一次字符的处理就对当前的变量进行加1操作
        matchNum++;
      }
      // 当查找为-1，说明已经匹配结束
      else {
        break;
      }
      i++;
    }

    return matchNum;
  }
}
