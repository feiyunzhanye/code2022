import javafx.util.Pair;

import java.util.*;
import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

/**
 * @Author: ZhouYX
 * @Description:
 * @Date: create in 2022/1/20 16:35
 */
public class code {

    public int search(int[] nums, int target) {
        HashMap map = new HashMap();
        return res(nums, 0, nums.length - 1, target);
    }

    private int res(int[] nums, int i, int j, int target) {

        int x = (i + j) / 2;
        if (nums[x] == target) {
            return x;
        }
        if (i == j) {
            return -1;
        }

        if (nums[x] > target) {
            return res(nums, i, x, target);
        } else {
            return res(nums, x + 1, j, target);
        }
    }

    // a b相除不用/、*、&
    private static int res(int a, int b) {
        //a/b 可以变相的写为 a = b*n = b 加 n次 ，因此可以遍历统计a减去多少次b得到的值res 小于b，及为结果
        // 首先去除符号
        // 记录符号的最终值
        // 边界值处理 -2 的31次方 ~2的31次方-1，若全部按正数来处理会溢出
        // 如果a = -2^31 ; b = 1 则a/b = 2^31-1;
        if (a == Integer.MIN_VALUE && b == -1) return Integer.MAX_VALUE;
        // 记录符号异同
        int ne = 0;
        // 转为负值处理
        if (a > 0) {
            ne++;
            a = -a;
        }
        if (b > 0) {
            ne++;
            b = -b;
        }
        int ans = divide(a, b);
        return ne == 1 ? -ans : ans;
    }

    // 减法
    public static int divide(int a, int b) {
        int res = 0;
        while (a <= b) {
            // 最小为1
            int v = b;
            int count = 1;
            while (a - v <= v) {
                count += count;
                v += v;
            }
            // 计算出 a减去最小v时，经历了几个b
            res += count;
            a -= v;
        }
        return res;
    }
    //给定一个非负整数 n ，请计算 0 到 n 之间的每个数字的二进制表示中 1 的个数，并输出一个数组。
    public static int[] countBits(int n) {
        // 此题的重点在于找出规律
        // 0 0;1 1;2 10;3 11;4 100;5 101;6 110;7 111;8 1000
        // 首先分析二进制 偶数时最后一位是0，奇数最后一位是1
        // 当为奇数时例: 111 它的1的个数为 110 + 1
        // 当为偶数时 例子 110 他的一个数为 11的1的个数，及右移了一位

        // 初始化结果
        int[] res = new int[n+1];
        for(int i=1;i <= n;i++){
            if((i & 1) == 0){
                res[i] = res[i >> 1];
            }else{
                res[i] = res[i-1] + 1;
            }
        }
        return res;
    }
    //给你一个整数数组 nums ，除某个元素仅出现 一次 外，其余每个元素都恰出现 三次 。请你找出并返回那个只出现了一次的元素。
    public static int singleNumber(int[] nums) {
        /**
         * if two == 0:
         *   if n == 0:
         *     one = one
         *   if n == 1:
         *     one = ~one
         * if two == 1:
         *     one = 0
         *
         *  if two == 0:
         *     one = one ^ n
         * if two == 1:
         *     one = 0
         *
         *  one = one ^ n & ~two
         *
         *
         *  two = two ^ n & ~one
         */
        // 可以利用异或  a^a = 0,a^0 = a;
        int res = 0;
        for(int n : nums){
            res ^= n;
        }
        return res;
    }

    /**
     * 给定一个字符串数组 words，请计算当两个字符串 words[i] 和 words[j] 不包含相同字符时，它们长度的乘积的最大值。假设字符串中只包含英语的小写字母。如果没有不包含相同字符的一对字符串，返回 0。
     * 输入: words = ["abcw","baz","foo","bar","fxyz","abcdef"]
     * 输出: 16
     * 解释: 这两个单词为 "abcw", "fxyz"。它们不包含相同字符，且长度的乘积最大。
     *
     * 来源：力扣（LeetCode）
     * 链接：https://leetcode.cn/problems/aseY1I
     * 著作权归领扣网络所有。商业转载请联系官方授权，非商业转载请注明出处。
     */
    public int maxProduct(String[] words) {
        // for 循环两两遍历
        // 若字符串不重复 则更新最大乘积
        // 计算 两字符串是否重叠
            // 字符转换为位数，1 a，2 b， 值为0表示无该字符，为1则为有，若两者相与为0表示一定不重复（布隆过滤器原理）
        // int[] 记录字符串下标，值记录位掩码
        int n = words.length;
        int[] flag = new int[n];
        for(int i=0;i< n;i++){
            for(char c:words[i].toCharArray()){
                // 位掩码转换
                flag[i] |= 1 << (c-'a');
            }
        }
        int res = 0;
        // 计算字符串是否重叠
        for(int i=0; i<n;i++){
            for(int j = 1;j<n;j++){
                if((flag[i] & flag[j]) == 0){
                    res = Math.max(words[i].length() * words[j].length(),res);
                }
            }
        }
        return res;
    }

    /**
     * 给定一个已按照 升序排列  的整数数组 numbers ，请你从数组中找出两个数满足相加之和等于目标数 target 。
     *
     * 函数应该以长度为 2 的整数数组的形式返回这两个数的下标值。numbers 的下标 从 0 开始计数 ，所以答案数组应当满足 0 <= answer[0] < answer[1] < numbers.length 。
     *
     * 假设数组中存在且只存在一对符合条件的数字，同时一个数字不能使用两次。
     * 输入：numbers = [1,2,4,6,10], target = 8
     * 输出：[1,3]
     * 解释：2 与 6 之和等于目标数 8 。因此 index1 = 1, index2 = 3 。
     */
    public int[] twoSum(int[] numbers, int target) {
        // 双指针
        // low ,high,
        // if num[low] + num[high] > target ,then high-- else low++
        // 直到 low==hign 则结束
        if(numbers.length < 2){
            return new int[2];
        }
        int l = 0;
        int h = numbers.length-1;
        while(l != h){
            int flag = numbers[l] + numbers[h];
            if(flag > target){
                h--;
            }else if(flag < target){
                l++;
            }else{
                return new int[]{l,h};
            }
        }
        return new int[2];
    }

    /**
     * 给定一个包含 n 个整数的数组 nums，判断 nums 中是否存在三个元素 a ，b ，c ，
     * 使得 a + b + c = 0 ？请找出所有和为 0 且 不重复 的三元组。
     *
     * 输入：nums = [-1,0,1,2,-1,-4]
     * 输出：[[-1,-1,2],[-1,0,1]]
     */
    private static HashMap<Pair<Integer,Integer>,Integer> threeSumMap = new HashMap<>();
    static List<List<Integer>> resList = new ArrayList<>();
    public static List<List<Integer>> threeSum(int[] nums) {
        // 先排序
        // 选择一个数 a，计算a之后的数满足 b+c = -a，可以采用双指针法
        // 升序排序
        resList = new ArrayList<>();
        threeSumMap = new HashMap<>();
        if(nums == null || nums.length == 0){
            return resList;
        }
        Arrays.sort(nums);
        int len = nums.length;
        for(int i=0;i<len;i++){
            twoSumIndex(nums,i+1,len-1,-nums[i]);
        }
        return resList;
    }
    public static void twoSumIndex(int[] nums,int l,int h,int res){
        while(l<h){
            if(nums[l] + nums[h] > res){
                h--;
            }else if(nums[l] + nums[h] < res){
                l++;
            }else{
                if(!threeSumMap.containsKey(new Pair<>(-res,nums[l]))){
                    List<Integer> list = new ArrayList<>();
                    list.add(-res);
                    list.add(nums[l]);
                    list.add(nums[h]);
                    resList.add(list);
                    threeSumMap.put(new Pair<>(-res,nums[l]),nums[h]);
                }
                l++;
            }
        }
    }
    public static void main(String[] args) throws InterruptedException {
        Object o = new Object();
        new Thread(() -> {
            synchronized (o){
                System.out.println(Thread.currentThread().getName() + "执行 wait");
                try {
                    o.wait();
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                System.out.println(Thread.currentThread().getName() + "结束");
            }
        }).start();

        new Thread(() -> {
            synchronized (o){
                System.out.println(Thread.currentThread().getName() + "开始");


                System.out.println(Thread.currentThread().getName() + "结束");
                try {
                    Thread.sleep(3000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                System.out.println(Thread.currentThread().getName() + "执行 notify");
                try {
                    o.notify();
                }catch (Exception e){
                    e.printStackTrace();
                }
            }
        }).start();

        Lock lock = new ReentrantLock();
        Condition condition = lock.newCondition();
        // 可以创建多个条件队列
        Condition condition1 = lock.newCondition();
        new Thread(() -> {
            lock.lock();
            try{
                System.out.println(Thread.currentThread().getName() + "开始处理任务");
                // 阻塞该线程 放到了条件队列
                condition.await();
                System.out.println(Thread.currentThread().getName() + "结束处理任务");
            }catch (Exception e){
                e.printStackTrace();
            }finally {
                lock.unlock();
            }
        }).start();

        new Thread(() -> {
            lock.lock();
            try{
                System.out.println(Thread.currentThread().getName() + "开始处理任务");
                Thread.sleep(2000);
                condition.signal();
                System.out.println(Thread.currentThread().getName() + " 结束处理任务");
            }catch (Exception e){
                e.printStackTrace();
            }finally {
                lock.unlock();
            }
        }).start();
    }
}
