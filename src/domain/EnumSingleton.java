package domain;

import java.io.*;

/**
 * 枚举模式
 */
public enum EnumSingleton {
    INSTANCE;

    public void print() {
        System.out.println(this.hashCode());
    }
}
class EnumTest {
    public static void main(String[] args) throws IOException, ClassNotFoundException {
        EnumSingleton enumSingleton = EnumSingleton.INSTANCE;

        /*ObjectOutputStream oos = new ObjectOutputStream(new FileOutputStream("EnumSingleton"));
        oos.writeObject(enumSingleton);
        oos.close();*/

        ObjectInputStream ois = new ObjectInputStream(new FileInputStream("EnumSingleton"));
        EnumSingleton enumSingleton1 = (EnumSingleton) ois.readObject();
        System.out.println(enumSingleton1 == enumSingleton);
    }
}
