package com.netty.rpc.test.server;


import com.netty.rpc.server.RPCServer;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.support.ClassPathXmlApplicationContext;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;


public class RPCStart {
    public static void main(String[] args) {
        ClassPathXmlApplicationContext applicationContext = new ClassPathXmlApplicationContext("server-spring.xml");
        RPCServer rpcserver = applicationContext.getBean("rpcserver", RPCServer.class);
        rpcserver.start();
    }
}
