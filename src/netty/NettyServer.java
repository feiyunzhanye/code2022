package netty;

/**
 * @Author: ZhouYX
 * @Description:
 * @Date: create in 2022/8/26 13:58
 */
public class NettyServer extends Netty {
    public void test(){

    }
    public static void main(String[] args){
        Netty netty = new NettyServer();
        Class<Netty> clz = Netty.class;
        clz.getFields();// 获取成员变量
        clz.getName();// 获取类全名
        clz.getConstructors();//获取构造方法
        clz.getMethods();//获取成员方法
    }
}
