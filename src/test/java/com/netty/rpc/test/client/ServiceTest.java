package com.netty.rpc.test.client;

import com.netty.rpc.client.RPCClient;
import org.junit.After;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import java.util.List;


@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = "classpath:client-spring.xml")
public class ServiceTest {

    @Autowired
    private RPCClient client;
    @Test
    public void helloServiceTest1(){
        HelloService helloService = client.getBean(HelloService.class);
        String res = helloService.hello("thank you");
        Assert.assertEquals(res,"Hello! thank you");
    }

    @Test
    public void helloServiceTest2(){
        HelloService helloService = client.getBean(HelloService.class);
        String res = helloService.hello(new Person("Jay", "Zhou"));
        Assert.assertEquals(res,"Hello! Jay Zhou");
    }

    @Test
    public void personServiceTest(){
        PersonService personService = client.getBean(PersonService.class);
        List<Person> res = personService.GetTestPerson("Lisa", 5);
        System.out.println(res);
    }

    @After
    public void stop(){
        if(client!=null) client.stop();
    }
}
