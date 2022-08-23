import java.util.Scanner;

/**
 * @Author: ZhouYX
 * @Description:
 * @Date: create in 2022/8/18 13:05
 */
public class newke {
    public static int res(String s){
        String[] ss = s.split(" ");
        if(ss.length == 0){
            return 0;
        }
        return ss[ss.length-1].length();
    }
    public  static void  main(String [] args) throws Exception{
        Scanner in = new Scanner(System.in);
        String s = "";
        // 注意 hasNext 和 hasNextLine 的区别
        while (in.hasNext()) { // 注意 while 处理多个 case
            s = in.nextLine();
        }
        System.out.println(res(s));
    }
}
