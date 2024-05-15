import java.io.File;
import java.io.FileNotFoundException;
import java.util.Map;
import java.util.Scanner;
import java.util.HashMap;
import java.util.ArrayList;
import java.io.FileInputStream;
import java.io.IOException;
public class HuffmanCode {
    private class HuffmanNode{
        String data; //字符
        int weight; //权值
        int parent; //父节点
        int lchild; //左孩子
        int rchild; //右孩子
    }
    ArrayList<HuffmanNode> huffmantree; //哈夫曼树
    Map<String, Integer> map;           //存储字符及其权值
    Map<String, String> code;           //存储字符及其编码
    public HuffmanCode(){
        huffmantree = new ArrayList<HuffmanNode>();
        map = new HashMap<String, Integer>();
    }
    private boolean judgeEng(byte c) {
        if ((c & 0x80)==0)
            return true;
        return false;
    }
    //读取文件，如果是汉字，则再读取2个字节，否则读取一个字节



    private void  buildAll(String filename){
        //以字节为单位读取文件内容
        File file = new File(filename);
        try (FileInputStream fileInputStream = new FileInputStream(file)) {
            byte[] bytes = new byte[1];
            int len = 0;
            while((len=fileInputStream.read(bytes))!=-1){
                if(judgeEng(bytes[0])){ //如果是英文字符
                    String s = new String(bytes);   //将字节数组转换为字符串
                    if(map.containsKey(s)){
                        map.put(s, map.get(s)+1);   //如果map中已经存在该字符，则将该字符的权值加1
                    }else{
                        map.put(s, 1);
                    }
                }else{
                    byte[] bytes3 = new byte[3];
                    bytes3[0] = bytes[0];
                    bytes3[1] = (byte)fileInputStream.read();
                    bytes3[2] = (byte)fileInputStream.read();
                    String s = new String(bytes3);
                    if(map.containsKey(s)){
                        map.put(s, map.get(s)+1);
                    }else{
                        map.put(s, 1);
                    }
                }
            }
        } catch (FileNotFoundException e) {
            } catch (IOException e) {
                e.printStackTrace();
            } 
    }
    //将map中的数据存入arraylist中

    private void selectTwo(int father,int[] s1) { // Fix the type declarations    
        int min1 = Integer.MAX_VALUE;
        int min2 = Integer.MAX_VALUE;
        for (int j = 0; j < father; j++) {
            if (huffmantree.get(j).parent == -1) {
                if (huffmantree.get(j).weight < min1) {
                    min2 = min1;
                    s1[1] = s1[0];
                    min1 = huffmantree.get(j).weight;
                    s1[0] = j;
                } else if (huffmantree.get(j).weight < min2) {
                    min2 = huffmantree.get(j).weight;
                    s1[1] = j;
                }
            }
        }
    }
    //构建哈夫曼树
    private void buildHuffmantrees(){
        int n = map.size();
        for (Map.Entry<String, Integer> entry : map.entrySet()) {
            HuffmanNode node = new HuffmanNode();
            node.data = entry.getKey();
            node.weight = entry.getValue(); //getvalue返回int类型
            node.parent = -1;
            node.lchild = -1;
            node.rchild = -1;
            huffmantree.add(node);
        }   //将map中的数据存入arraylist中
        int[] s1 = new int[2];
        for (int i =n ; i <2*n-1; i++) {
            HuffmanNode node = new HuffmanNode();
            selectTwo(i, s1);
            //找到权值最小的两个节点
            huffmantree.get(s1[0]).parent = i;
            huffmantree.get(s1[1]).parent = i;
            
            //构建新节点
            node.weight = huffmantree.get(s1[0]).weight + huffmantree.get(s1[1]).weight;
            node.lchild = s1[0];
            node.rchild = s1[1];
            node.parent = -1;
            huffmantree.add(node);
        }
    }
    //编码
    private void encoding(){
        int n = map.size();
        for (int i = 0; i < n; i++) {   //从叶子节点开始编码，对每个叶子节点进行编码
            String code = "";
            int current = i;
            int parent = huffmantree.get(current).parent;
            while (parent != -1) {
                if (huffmantree.get(parent).lchild == current) {
                    code = "0" + code;
                } else {
                    code = "1" + code;
                }
                current = parent;
                parent = huffmantree.get(current).parent;
            }
            this.code.put(huffmantree.get(i).data, code);
        }
    }
    //压缩文件   从输入框的文字输入到一个文件中，这个文件进行压缩
    public void compress(String filename,String compressedFilename){
        //清空俩map和arraylist
            huffmantree.clear();
            map.clear();
            code.clear();

            buildAll(filename);
            buildHuffmantrees();
            encoding();
            //以字节为单位读取文件内容
            File file = new File(filename);
            try (FileInputStream fileInputStream = new FileInputStream(file)) {
                byte[] bytes = new byte[1];
                int len = 0;
                StringBuilder sb = new StringBuilder();
                while((len=fileInputStream.read(bytes))!=-1){
                    if(judgeEng(bytes[0])){ //如果是英文字符
                        String s = new String(bytes);   //将字节数组转换为字符串
                        sb.append(code.get(s));         //将字符对应的编码添加到sb中
                    }else{
                        byte[] bytes2 = new byte[3];
                        bytes2[1] = bytes[1];
                        bytes2[2] = (byte)fileInputStream.read();
                        String s = new String(bytes2);
                        //三个字节对应一个字符
                        sb.append(code.get(s));
                    }
                }
                //将sb中的数据写入文件
                //以字节为单位写入文件
                int add0Num = 8 - sb.length() % 8;
                for (int i = 0; i < add0Num; i++) {
                    sb.append("0");
                }
                
                File file2 = new File(compressedFilename);
                try (java.io.FileOutputStream fileOutputStream = new java.io.FileOutputStream(file2)) {     //存入后格式为 map.size() add0Num map的内容 sb的内容
                    //将辅助信息写入文件，map的内容和add0Num
                    StringBuilder sb2 = new StringBuilder();
                    sb2.append(map.size());
                    sb2.append(" ");
                    sb2.append(add0Num);
                    sb2.append(" ");
                    for (Map.Entry<String, Integer> entry : map.entrySet()) {
                        sb2.append(entry.getKey());     //get到的是一个String类型
                        sb2.append(" ");
                        sb2.append(entry.getValue());
                        sb2.append(" ");
                    }
                    //将String的sb的二进制字符串每八位转换为一个字符写入文件
                    for (int i = 0; i < sb.length(); i+=8) {
                        String s = sb.substring(i, i+8);
                        int num = Integer.parseInt(s, 2);
                        byte[] bytes2 = new byte[1];
                        bytes2[0] = (byte)num;
                        fileOutputStream.write(bytes2);
                    }


                } catch (IOException e) {
                    e.printStackTrace();
                }
            } catch (FileNotFoundException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
        }
    }
    //解压  从一个压缩文件中解压出原文件
    private void decompressFile(String filename, String decompressFilename){
        //清空俩map和arraylist
        huffmantree.clear();
        map.clear();
        code.clear();

        File file1=new File(filename);
        try(FileInputStream fileInputStream = new FileInputStream(file1)){
            byte[] bytes = new byte[1];
            int index = 0;
            StringBuilder sb = new StringBuilder();
            int len = 0;
            while((len=fileInputStream.read(bytes))!=-1){
                sb.append(new String(bytes));
            }
            //读取map的大小和add0Num
            String[] s = sb.toString().split(" ");
            int mapSize = Integer.parseInt(s[0]);   //遇到空格就分割
            int add0Num = Integer.parseInt(s[1]);
            index = 2;
            for (int i = 0; i < mapSize; i++) { //将map中的数据存入map中
                String key = s[index++];
                int value = Integer.parseInt(s[index++]);
                map.put(key, value);
            }
            //构建哈夫曼树
            buildHuffmantrees();
            //解码
            StringBuilder sb2 = new StringBuilder();
            //sb中剩下的数据，全部读入为二进制字符串
            for (int i = index; i < s.length-add0Num; i++) {
                sb2.append(s[i]);
            }   //将sb2中的数据写入文件,s.length-add0Num是因为最后add0Num个是补0的
            //根据构造的哈夫曼树进行解码
            int current = 2*mapSize-2;
            File file2 = new File(decompressFilename);
            try(java.io.FileOutputStream fileOutputStream = new java.io.FileOutputStream(file2)){
                for (int i = 0; i < sb2.length(); i++) {
                    if (sb2.charAt(i) == '0') {
                        current = huffmantree.get(current).lchild;
                    } else {
                        current = huffmantree.get(current).rchild;
                    }
                    if (huffmantree.get(current).lchild == -1 && huffmantree.get(current).rchild == -1) {
                        byte[] bytes2 = huffmantree.get(current).data.getBytes();
                        fileOutputStream.write(bytes2);
                        current = 2*mapSize-2;
                    }
                }
            }catch(IOException e){
                e.printStackTrace();
            }
        }catch(FileNotFoundException e){
            e.printStackTrace();
        }catch(IOException e){
            e.printStackTrace();
        }
    }
    public void readoutFile(String String,int i){       //i为当前用户下的第几篇日记
        //从压缩文件中读取
        decompressFile(String,String+ "decompress"+i+".txt");   //输出到一个文件中  
    }
    public void writeInFile(String filenameIn){   //i为当前用户下的第几篇日记
        String filenameOut= new String();
        String name = new String ();
        String  num = new String();
        char c;
        int tag=0;
        //扫描文件名,一个一个字符读取
        for (int i = 0; i < filenameIn.length(); i++) {
            c = filenameIn.charAt(i);
                if(c=='_'){
                    tag=1;
                    continue;   
                }
                if(tag==0){
                    name+=c;
                }
                if(tag==2){
                    num+=c;
                    if(c=='.'){
                        break;
                    }
                }
                if(tag==1){
                    if(c>='0'&&c<='9'){
                        tag=2;
                        num+=c;
                        continue;
                    }
                }
        }
        filenameOut=name+"_title"+num+"compress.txt";
        
        buildAll(filenameIn);
        buildHuffmantrees();
        encoding();
        compress(filenameIn,filenameOut);
    } 
}
