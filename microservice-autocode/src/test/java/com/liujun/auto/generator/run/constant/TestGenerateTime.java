package com.liujun.auto.generator.run.constant;

import org.junit.Assert;
import org.junit.Test;

import java.time.LocalDateTime;

/**
 * 时间处理
 *
 * @author liujun
 * @version 0.0.1
 */
public class TestGenerateTime {

  @Test
  public void localTimeProc() {
    LocalDateTime time1 = LocalDateTime.parse("2021-03-31T07:47:15.056818100");
    LocalDateTime timeOut = time1.withNano((time1.getNano() / 1000000) * 1000000);
    LocalDateTime time2 = LocalDateTime.parse("2021-03-31T07:47:15.056");

    System.out.println((timeOut.getNano()));
    System.out.println(time2.getNano());
    Assert.assertEquals(timeOut, time2);
  }
}
