package domain;

import java.util.ArrayList;
import java.util.List;

/**
 * @Author: ZhouYX
 * @Description:
 * @Date: create in 2022/8/22 16:30
 */
public class Generics {
    public synchronized static void test(){
        return;
    }
    public static void main(String[] args){
        List<? extends B> list1 = new ArrayList<>();
        List<? super B> list2 = new ArrayList<>();
        A a = new A();
        B b = new B();
        C c = new C();
        Object o = new Object();
        B bb = list1.get(0);
        A aa = list1.get(0);
        list2.add(c);

    }
}
class A {}
class B extends A {}
class C extends B {
    public void testC(){
        return;
    }
}
