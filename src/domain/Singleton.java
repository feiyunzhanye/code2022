package domain;

import java.io.*;
import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;

/**
 * @Author: ZhouYX
 * @Description:
 * @Date: create in 2022/8/23 10:18
 */
public class Singleton {
    public static void main(String[] args) throws NoSuchMethodException, InvocationTargetException, InstantiationException, IllegalAccessException, IOException, ClassNotFoundException {
        /*LazySingleton4 lazySingleton4 = LazySingleton4.getInstance();

        Constructor<LazySingleton4> declaredConstructor = LazySingleton4.class.getDeclaredConstructor();
        declaredConstructor.setAccessible(true);
        LazySingleton4 lazySingleton41 = declaredConstructor.newInstance();

        System.out.println(lazySingleton4);
        System.out.println(lazySingleton41);

        InnerClassSingleton2 innerClassSingleton2 = InnerClassSingleton2.getInstance();

        // 获取私有的对象实例构造器
        Constructor<InnerClassSingleton2> innerClassSingleton2Constructor = InnerClassSingleton2.class.getDeclaredConstructor();
        innerClassSingleton2Constructor.setAccessible(true);
        InnerClassSingleton2 innerClassSingleton21 = innerClassSingleton2Constructor.newInstance();
        System.out.println(innerClassSingleton21 == innerClassSingleton2);*/

        // 实例序列化
        LazySingleton4 lazySingleton4 = LazySingleton4.getInstance();
       /* ObjectOutputStream oos = new ObjectOutputStream(new FileOutputStream("LazySingleton4"));
        oos.writeObject(lazySingleton4);
        oos.close();*/
        ObjectInputStream ois = new ObjectInputStream(new FileInputStream("LazySingleton4"));
        LazySingleton4 lazySingleton41 = (LazySingleton4) ois.readObject();
        System.out.println(lazySingleton4);
        System.out.println(lazySingleton41);


    }
}

/**
 * 懒汉模式1
 * 最简单版
 * 缺点：
 * 1、并发问题，如果两个线程同时走到判断语句发现singleton不为空，都会重新创建
 */
class LazySingleton {
    private static LazySingleton lazySingleton;

    // 避免public 方法被外部访问，实现实例化
    private LazySingleton() {

    }

    public static LazySingleton getInstance() {
        if (lazySingleton == null) {
            try {
                Thread.sleep(200);
            } catch (Exception e) {
                e.printStackTrace();
            }
            lazySingleton = new LazySingleton();
        }
        return lazySingleton;
    }
}

/**
 * 懒汉模式2
 * 缺点：锁提前了，如果为了解决并发问题，其实只需要在判断install是否为null的时候，如果为null 加锁，保证只有一个可以创建
 */
class LazySingleton2 {
    private static LazySingleton2 instance;

    private LazySingleton2() {

    }

    public synchronized static LazySingleton2 getInstance() {
        if (instance == null) {
            try {
                Thread.sleep(200);
            } catch (Exception e) {
                e.printStackTrace();
            }
            instance = new LazySingleton2();
        }
        return instance;
    }
}

/**
 * 懒汉模式3
 * 缺点：虽然通过二次检验的方法，延缓了加锁的时机，但是如果出现cpu指令重排，导致还未初始化完成，单第二个线程的判断instance时已经不为null，简单说java new对象非原子操作
 * Java 创建对象 1、开辟内存空间；2初始化；3、对象引用赋值，JIT及时编译器会对其进行指令重排
 */
class LazySingleton3 {
    private static LazySingleton3 instance;

    private LazySingleton3() {

    }

    public static LazySingleton3 getInstance() {
        if (instance == null) {
            synchronized (LazySingleton3.class) {
                // 由于可能有多个线程判断到instance 为null，并且在加锁前已经创建，因此加锁后要再判断下是否为null
                if (instance == null) {
                    instance = new LazySingleton3();
                }
            }
        }
        return instance;
    }
}

/**
 * 懒汉模式4：模式3未解决指令重排的问题，因此需要对变量增加volatile，避免指令重排
 */
class LazySingleton4 implements Serializable {
    static final long serialVersionUID = 42L;
    private volatile static LazySingleton4 instance;

    private LazySingleton4() {

    }

    public static LazySingleton4 getInstance() {
        if (instance == null) {
            synchronized (LazySingleton4.class) {
                if (instance == null) {
                    instance = new LazySingleton4();
                }
            }

        }
        return instance;
    }

    Object readResolve() throws ObjectStreamException{
        return getInstance();
    }
}

/**
 * 懒汉模式5：静态内部类加载
 */
class InnerClassSingleton {
    private InnerClassSingleton() {

    }

    private static class InnerClassHolder {
        private static InnerClassSingleton instance = new InnerClassSingleton();
    }

    public static InnerClassSingleton getInstance() {
        return InnerClassHolder.instance;
    }
}

/**
 * 静态内部类2
 */
class InnerClassSingleton2 {
    private InnerClassSingleton2(){
        // 调用构造方法时判断是否已经不为空了
        if(InnerClassSingletonHolder2.instance != null){
            throw new RuntimeException("不允许有多个实例");
        }
    }
    private static class InnerClassSingletonHolder2{
        private static InnerClassSingleton2 instance = new InnerClassSingleton2();
    }
    public static InnerClassSingleton2 getInstance(){
        return InnerClassSingletonHolder2.instance;
    }
    Object readResolve() throws ObjectStreamException{
        return  InnerClassSingletonHolder2.instance;
    }
}

/**
 * 饿汉模式1
 */
class HungrySingleton {
    private static HungrySingleton instance = new HungrySingleton();

    private HungrySingleton() {

    }

    public static HungrySingleton getInstance() {
        return instance;
    }
}

