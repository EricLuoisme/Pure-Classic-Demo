package com.own.netty.demo.server;

import com.own.netty.demo.TimeEncoder;
import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelOption;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import lombok.RequiredArgsConstructor;

/**
 * Netty simple server
 *
 * @author Roylic
 * 2023/1/12
 */
// @RequiredArgsConstructor
public class TimeServer {

    private final int port;

    public TimeServer(int port) {
        this.port = port;
    }

    /**
     * main set-up for Netty-Server
     */
    public void run() throws Exception {
        // NioEventLoopGroup is a multi-threaded event loop that handles IO operation
        EventLoopGroup bossGroup = new NioEventLoopGroup();
        NioEventLoopGroup workerGroup = new NioEventLoopGroup();
        try {
            // ServerBootstrap is a helper class that sets up a server,
            // but you can still use a Channel directly, which is a tedious process
            ServerBootstrap bootstrap = new ServerBootstrap();
            bootstrap.group(bossGroup, workerGroup)
                    // NioServerSocketChannel class which is used
                    // to instantiate a new Channel to accept incoming connections
                    .channel(NioServerSocketChannel.class)
                    .childHandler(
                            // ChannelInitializer is for helping customize channel pipeline
                            new ChannelInitializer<SocketChannel>() {
                                @Override
                                protected void initChannel(SocketChannel ch) {
                                    // Official recommendation -> always create new Handler instance
//                                    ch.pipeline().addLast(new DiscardServerHandler());
                                    ch.pipeline().addLast(new TimeEncoder(), new TimeServerHandler());
                                }
                            })
                    // optionals by using tcp
                    .option(ChannelOption.SO_BACKLOG, 128)
                    // child options are accepted by the parent ServerChannel -> NioSocketChannel in this case
                    .childOption(ChannelOption.SO_KEEPALIVE, true);

            // bind and start to accept incoming connections
            ChannelFuture channelFuture = bootstrap.bind(port).sync();
            System.out.println("Server start");

            // wait until the server socket is closed
            channelFuture.channel().closeFuture().sync();
        } finally {
            workerGroup.shutdownGracefully();
            bossGroup.shutdownGracefully();
        }
    }

    // server starter
    public static void main(String[] args) throws Exception {
        int port = 8088;
        new TimeServer(port).run();
    }
}
