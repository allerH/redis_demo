package datastruct;

import java.util.Arrays;

public class SimpleDynamicString {
    private static final int initSize = 20;
    private static final int lowLimit = 1024 * 1024;

    private byte[] buf = null; // 存储二进制数据，二进制安全
    private int len = 0; // 已使用的长度
    private int free = 0; // 未使用的空间
    public SimpleDynamicString(){
        this.buf = new byte[initSize];
        this.len = 0;
        this.free = 0;
    }

    /**
     * 长度判断防止溢出
     * @param val
     * @return
     */
    public String set(String val) {
        byte[] valBytes = val.getBytes();
        int valLen = valBytes.length;
        int nowLen = this.len;
        if (valLen > this.free) { //进行空间分配
            mallocSpace(valLen);
        }
        byte[] zeroBytes = "\0".getBytes();
        for(int i =0 ; i < valLen + zeroBytes.length ; i ++){
            this.buf[nowLen + i] = i < valLen ? valBytes[i] : zeroBytes[i];
        }

        this.len = nowLen + valLen;
        this.free = this.buf.length - nowLen - valLen - 1;

        return "ok";
    }

    /**
     * 额外空间分配
     * @param size
     */
    private void mallocSpace(int size){
        if (size <= lowLimit) { //要分配的空间大小不超过 1M
            this.buf = Arrays.copyOf(this.buf, 2 * size + 1); //分配两倍的size + 1
        } else {
            this.buf = Arrays.copyOf(this.buf, size + lowLimit + 1); // 超过1M时，额外分配1M的空间
        }
        int nowLen = this.buf.length;
        this.free = nowLen - this.len - 1;
    }
}
