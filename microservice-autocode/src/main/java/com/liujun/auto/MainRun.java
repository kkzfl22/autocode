package com.liujun.auto;

import com.liujun.auto.generator.run.CodeCreateMicroServiceRun;

/**
 * 程序入口
 *
 * @author liujun
 * @version 0.0.1
 */
public class MainRun {

    public static void main(String[] args) {
        //代码生成
        CodeCreateMicroServiceRun.INSTANCE.generate();

        System.out.println("finish");
    }
}
