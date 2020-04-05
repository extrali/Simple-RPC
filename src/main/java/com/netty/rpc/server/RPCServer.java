package com.netty.rpc.server;

import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelOption;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeansException;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class RPCServer implements ApplicationContextAware {
    private String host;
    private int port;
    private static ExecutorService executors= Executors.newFixedThreadPool(10);
    private final Map<String,Object> handleMap=new HashMap<>();
    private static Logger logger= LoggerFactory.getLogger(RPCServer.class);

    public RPCServer(String host, int port) {
        this.host = host;
        this.port = port;
    }

    public static void submit(Runnable task){
        executors.execute(task);
    }


    public void start(){
        EventLoopGroup bossGroup=new NioEventLoopGroup();
        EventLoopGroup workerGroup=new NioEventLoopGroup();
        try{
            ServerBootstrap serverBootstrap=new ServerBootstrap();
            serverBootstrap.group(bossGroup,workerGroup)
                    .channel(NioServerSocketChannel.class)
                    .option(ChannelOption.SO_BACKLOG, 128)
                    .childOption(ChannelOption.SO_KEEPALIVE, true)
                    .childHandler(new RPCServerInitializer(handleMap));
            ChannelFuture future = serverBootstrap.bind(host,port).sync();
            future.channel().closeFuture().sync();
        } catch (InterruptedException e) {
            logger.error("server start exception",e);
            e.printStackTrace();
        } finally {
            bossGroup.shutdownGracefully();
            workerGroup.shutdownGracefully();
        }

    }

    @Override
    public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
        Map<String, Object> map = applicationContext.getBeansWithAnnotation(RPCService.class);
        for(Object bean:map.values()){
            String interfaceName = bean.getClass().getAnnotation(RPCService.class).value().getName();
            handleMap.put(interfaceName,bean);
        }
    }
}
