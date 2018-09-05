package com.starcor.demo;

import org.springframework.context.support.ClassPathXmlApplicationContext;

import java.io.IOException;

/**
 * Created by susen on 2018/5/11.
 */
public class StartUp {
    public static void main( String[] args ) throws IOException
    {
        ClassPathXmlApplicationContext context = new ClassPathXmlApplicationContext(
                new String[] { "/spring/spring-dubbo-provider.xml" });
        context.start();
        System.out.println("启动成功");
        System.in.read(); // 为保证服务一直开着，利用输入流的阻塞来模拟
    }
}
