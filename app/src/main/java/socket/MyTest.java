package socket;

import android.util.Log;

/**
 * Created by HERO on 2018/6/17.
 */

public class MyTest {
    private static int length;
    private static String strISSame;
    private static byte[] bytes1,bufferRead2;
    private static int size;
    public static void main(String[]args){
        byte[] bytes={12,34,56,78,90,123,1,2,3,4,5,6,7,8,9};//15位
        size=15;
        bufferRead2=new byte[50];
        processData(bytes,1);
        for (byte b:bufferRead2){
            System.out.println(b);
        }
        System.out.println();
        processData(bufferRead2);
        for (byte b:bytes1){
            System.out.println(b);
        }
    }
    /**
     * 对数据进行加工处理
     */
    private static void processData(byte[] bufferRead1,int index) {
        /**
         * 长度和类型信息
         */
        byte[] type=IntToByteArray2(65);
        byte[] length=IntToByteArray2(size);
        byte[] order=IntToByteArray2(index);
        /**
         * 放入数组
         */
        System.arraycopy(type,0,bufferRead2,0,4);
        System.arraycopy(length,0,bufferRead2,4,4);
        System.arraycopy(order,0,bufferRead2,8,4);

        System.arraycopy(bufferRead1,0,bufferRead2,12,size);
        xor(bufferRead1);
        System.arraycopy(bufferRead1,0,bufferRead2,size+12,size);
    }

    /**
     * int转byte
     * @param value
     * @return
     */
    public static byte[] IntToByteArray2(int value)
    {
        byte[] src = new byte[4];
        src[0] = (byte) ((value>>24) & 0xFF);
        src[1] = (byte) ((value>>16)& 0xFF);
        src[2] = (byte) ((value>>8)&0xFF);
        src[3] = (byte) (value & 0xFF);
        return src;
    }


    /**
     * 自定义协议
     * @param bufferReader
     */
    private static void processData(byte[] bufferReader) {
        byte[] typeByte=new byte[4];
        byte[] lengthByte=new byte[4];
        byte[] index=new byte[4];
        System.arraycopy(bufferReader,0,typeByte,0,4);
        System.arraycopy(bufferReader,4,lengthByte,0,4);
        System.arraycopy(bufferReader,8,index,0,4);

        int type=ByteArrayToInt2(typeByte);
        length=ByteArrayToInt2(lengthByte);
        int order=ByteArrayToInt2(index);
        System.out.println("length="+length+",type="+type+",order="+order);
        bytes1=new byte[length];
        byte[] bytes2=new byte[length];
        System.arraycopy(bufferReader,12,bytes1,0,bytes1.length);
        System.arraycopy(bufferReader,12+bytes1.length,bytes2,0,bytes2.length);

        xor(bytes2);
        for (int i=0;i<length;i++){
            if (bytes1[i]==bytes2[i]){
                strISSame="true";
            }else {
                strISSame="false";
                return;
            }
        }

    }

    /**
     * 异或
     * @param bufferRead1
     */
    private static void xor(byte[] bufferRead1) {
        byte[] temp=new byte[10240];
        for (int i=0;i<temp.length;i++){
            temp[i]=(byte)i;
        }
        for (int i=0;i<bufferRead1.length;i++){
            bufferRead1[i]= (byte) (temp[i]^bufferRead1[i]);
        }
    }

    /**
     * byte转int
     * @param bArr
     * @return
     */
    public static int ByteArrayToInt2(byte[] bArr) {
        if(bArr.length!=4){
            return -1;
        }
        return (int) ((((bArr[0] & 0xff) << 24)
                | ((bArr[1] & 0xff) << 16)
                | ((bArr[2] & 0xff) << 8)
                | ((bArr[3] & 0xff) << 0)));
    }

}
