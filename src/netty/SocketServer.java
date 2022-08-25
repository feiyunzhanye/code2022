package netty;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.nio.charset.StandardCharsets;

/**
 * @Author: ZhouYX
 * @Description:
 * @Date: create in 2022/8/24 18:13
 */
public class SocketServer {
    public static void main(String[] args) throws Exception {
        ServerSocket serverSocket =new ServerSocket(9000);
        while(true){
            System.out.println("等待连接...");
            // 阻塞方法
            Socket clientSocket = serverSocket.accept();
            System.out.println("有客户端连接");
            // 支持并发连接
            new Thread(()->{
                try {
                    handler(clientSocket);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }).start();

        }
    }
    public static void handler(Socket clientSocket) throws Exception{
        byte[] bytes = new byte[1024];
        System.out.println("准备read:");
        int read  = clientSocket.getInputStream().read(bytes);
        System.out.println("read完毕:");
        if(read != -1){
            System.out.println("接收到客户端的数据："+new String(bytes,0,read));
        }
        clientSocket.getOutputStream().write("HelloClient".getBytes());
        clientSocket.getOutputStream().flush();
    }

}
