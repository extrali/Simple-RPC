package com.netty.rpc.client;

import com.netty.rpc.protocol.RPCRequest;
import io.netty.bootstrap.Bootstrap;
import io.netty.channel.ChannelFuture;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.nio.NioSocketChannel;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.lang.reflect.Proxy;
import java.util.UUID;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class RPCClient {
    private String host;
    private int port;
    private static ExecutorService executors= Executors.newFixedThreadPool(10);
    private static Logger logger= LoggerFactory.getLogger(RPCClient.class);
    private static RPCClientHandler handler;
    private EventLoopGroup eventLoopGroup;

    public RPCClient(String host,int port){
        this.host=host;
        this.port=port;
    }
    @SuppressWarnings("unchecked")
    public <T> T getBean(final Class<T> clazz){
        return (T) Proxy.newProxyInstance(RPCClient.class.getClassLoader(), new Class<?>[]{clazz}, new InvocationHandler() {
            @Override
            public Object invoke(Object o, Method method, Object[] args) throws Throwable {
                if(handler==null){
                    start();
                }
                RPCRequest request=new RPCRequest();
                request.setRequestID(UUID.randomUUID().toString());
                request.setClassName(clazz.getName());
                request.setMethodName(method.getName());
                request.setParameterTypes(method.getParameterTypes());
                request.setParameters(args);
                handler.setRpcRequest(request);
                return executors.submit(handler).get();
            }
        });
    }

    public void stop(){
        executors.shutdown();
        eventLoopGroup.shutdownGracefully();
    }

    public void start(){
        handler=new RPCClientHandler();
        eventLoopGroup=new NioEventLoopGroup();
        try{
            Bootstrap bootstrap=new Bootstrap();
            bootstrap.group(eventLoopGroup)
                    .channel(NioSocketChannel.class)
                    .handler(new RPCClientInitializer(handler));
            ChannelFuture future = bootstrap.connect(host, port).sync();
        } catch (InterruptedException e) {
            logger.error("client start error",e);
            e.printStackTrace();
        }
    }

}
