package com.own.netty.demo.client;

import com.own.netty.demo.TimeDecoder;
import io.netty.bootstrap.Bootstrap;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelOption;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioSocketChannel;

public class TimeClient {

    public static void main(String[] args) throws Exception {
        String serverHost = "127.0.0.1";
        int serverPort = 8088;
        NioEventLoopGroup workerGroup = new NioEventLoopGroup();

        try {
            // client use Bootstrap for initialization, rather then ServerBootstrap
            Bootstrap b = new Bootstrap();
            b.group(workerGroup);
            // rather than NioServerSocketChannel, NioSocketChannel is for client side
            b.channel(NioSocketChannel.class);
            b.option(ChannelOption.SO_KEEPALIVE, true);
            b.handler(new ChannelInitializer<SocketChannel>() {
                @Override
                protected void initChannel(SocketChannel ch) throws Exception {
                    ch.pipeline().addLast(new TimeDecoder(), new TimeClientHandler());
                }
            });

            // start the client
            ChannelFuture f = b.connect(serverHost, serverPort).sync();

            // wait until the connection is closed
            f.channel().closeFuture().sync();

        } finally {
            workerGroup.shutdownGracefully();
        }
    }
}
