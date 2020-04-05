package com.netty.rpc.test.server;

import com.netty.rpc.server.RPCService;
import com.netty.rpc.test.client.HelloService;
import com.netty.rpc.test.client.Person;

@RPCService(HelloService.class)
public class HelloServiceImpl implements HelloService {

    public HelloServiceImpl(){

    }

    @Override
    public String hello(String name) {
        return "Hello! " + name;
    }

    @Override
    public String hello(Person person) {
        return "Hello! " + person.getFirstName() + " " + person.getLastName();
    }
}
