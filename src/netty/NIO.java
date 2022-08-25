package netty;

import com.sun.security.ntlm.Server;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.SocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.SelectionKey;
import java.nio.channels.Selector;
import java.nio.channels.ServerSocketChannel;
import java.nio.channels.SocketChannel;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Set;

/**
 * @Author: ZhouYX
 * @Description:
 * @Date: create in 2022/8/25 17:25
 */
public class NIO {
    public static void main(String[] args) throws Exception {
        // 调用基本的 nio写法
        //basicNIO();

        // 调用多路复用
        selectorNIO();
    }

    private static void basicNIO() throws IOException {
        List<SocketChannel> sockets = new ArrayList<>();
        // 创建NIO ServerSocketChannel
        ServerSocketChannel serverSocketChannel = ServerSocketChannel.open();
        serverSocketChannel.socket().bind(new InetSocketAddress(9000));
        serverSocketChannel.configureBlocking(false);// 设置为非阻塞
        System.out.println("服务器启动成功");
        while(true){
            // 非阻塞的accept方法不会阻塞线程，
            // NIO的非阻塞操作是由操作系统内部实现的，底层调用了linux内核的accept函数
            SocketChannel socketChannel = serverSocketChannel.accept();
            if(socketChannel != null){
                System.out.println(socketChannel.hashCode()+":连接成功");
                socketChannel.configureBlocking(false);// 设置为非阻塞，其read方法就不会阻塞线程了
                sockets.add(socketChannel);
            }
            // 遍历连接进行数据读取
            Iterator<SocketChannel> iterator = sockets.iterator();
            while(iterator.hasNext()){
                SocketChannel sc = iterator.next();
                ByteBuffer buffer = ByteBuffer.allocate(128);
                //SocketChannel 的非阻塞模式，不会阻塞read方法
                int len = sc.read(buffer);
                if(len > 0){
                    System.out.println("接收到消息："+ new String(buffer.array()));
                }else if(len == -1){
                    // 如果客户端断开，则把socket从集合中去掉
                    iterator.remove();
                    System.out.println("客户端断开连接");
                }
            }
        }
    }
    private static void selectorNIO() throws IOException{
        // 创建NIO channel
        ServerSocketChannel serverSocketChannel = ServerSocketChannel.open();
        serverSocketChannel.socket().bind(new InetSocketAddress(9000));
        // 设置为非阻塞
        serverSocketChannel.configureBlocking(false);
        // 打开selector选择器 绑定channel，即创建epoll
        Selector selector = Selector.open();
        // 把ServerSocketChannel注册到selector上，并且selector对客户端accept操作感兴趣
        serverSocketChannel.register(selector, SelectionKey.OP_ACCEPT);
        //serverSocketChannel.register(selector,SelectionKey.OP_CONNECT);
        System.out.println("服务器启动成功");
        while(true){
            //FIXME 阻塞等待需要处理的时间发生
            selector.select();
            //获取selector中注册的全部事件的SelectionKey 实例
            Set<SelectionKey> selectionKeys = selector.selectedKeys();
            Iterator<SelectionKey> iterator = selectionKeys.iterator();

            // 遍历SelectionKey 对事件进行处理
            while(iterator.hasNext()){
                SelectionKey key = iterator.next();
                // 如果是连接事件 accept
                if(key.isAcceptable()){
                    ServerSocketChannel server = (ServerSocketChannel) key.channel();
                    // channel连接
                    SocketChannel socket = server.accept();
                    socket.configureBlocking(false);
                    // 注册读数据事件到selector
                    socket.register(selector,SelectionKey.OP_READ);
                    System.out.println("客户端连接成功，注册读事件成功");
                }
                // 如果是读数据事件
                else if(key.isReadable()){
                    SocketChannel socketChannel = (SocketChannel) key.channel();
                    ByteBuffer buffer = ByteBuffer.allocate(128);
                    int len = socketChannel.read(buffer);
                    // 如果有数据就打印
                    if(len > 0){
                        System.out.println("接收到消息："+new String(buffer.array()));
                    }else if(len == -1){
                        System.out.println("客户端连接断开");
                        socketChannel.close();
                    }
                }
                //处理后删除事件
                iterator.remove();
            }
        }

    }
}
