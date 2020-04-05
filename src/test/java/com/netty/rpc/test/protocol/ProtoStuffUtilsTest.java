package com.netty.rpc.test.protocol;

import com.dyuproject.protostuff.ProtostuffIOUtil;
import com.netty.rpc.protocol.ProtoStuffUtils;
import com.netty.rpc.protocol.RPCEncoder;
import com.netty.rpc.protocol.RPCRequest;
import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Arrays;

public class ProtoStuffUtilsTest {
    @Test
    public void testSerialize(){
        RPCRequest request=new RPCRequest();
        request.setClassName("Hello");
        request.setMethodName("hello");
        request.setRequestID("111");
        request.setParameterTypes(new Class[]{String.class});
        request.setParameters(new Object[]{"1"});
        byte[] bytes = ProtoStuffUtils.serialize(request);
        System.out.println(Arrays.toString(bytes));
        RPCRequest deSerialize = ProtoStuffUtils.deSerialize(bytes, RPCRequest.class);
        System.out.println(deSerialize);
    }
}
