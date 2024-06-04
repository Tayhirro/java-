import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.Map;
import java.util.HashMap;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
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
        code = new HashMap<String, String>();
    }


    private boolean judgeEng(byte c) {  //判断是否是英文字符
        if ((c & 0x80)==0)
            return true;
        return false;
    }
    private void  buildAll(String filename){
        this.map.clear();

        //以字节为单位读取文件内容
        File file = new File(filename);
        try (FileInputStream fileInputStream = new FileInputStream("./diary/"+file)) {
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
                    String s = new String(bytes3,StandardCharsets.UTF_8);           //s已经变成了asic码
                    byte [] bytes2 = s.getBytes(StandardCharsets.UTF_8);


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
    private void selectTwo(int father,int[] s1) { // 选择权值最小的两个节点
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
    private void buildHuffmantrees(){
        this.huffmantree.clear();

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
        String code = "";
        for (int i = 0; i < n; i++) {   //从叶子节点开始编码，对每个叶子节点进行编码
            code="";
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


        //以字节为单位读取文件内容
        File file = new File(filename);
        try (FileInputStream fileInputStream = new FileInputStream("./diary/"+file)) {
            byte[] bytes = new byte[1];
            int len = 0;
            StringBuilder sb = new StringBuilder();
            while((len=fileInputStream.read(bytes))!=-1){
                if(judgeEng(bytes[0])){ //如果是英文字符
                    String s = new String(bytes);   //将字节数组转换为字符串
                    sb.append(code.get(s));         //将字符对应的编码添加到sb中
                }else{
                    byte[] bytes2 = new byte[3];
                    bytes2[0] = bytes[0];
                    bytes2[1] = (byte)fileInputStream.read();
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
            File head_file2=new File("./diary_compress/"+"head_"+compressedFilename);
            File file2 = new File("./diary_compress/"+compressedFilename);
            try (java.io.FileOutputStream fileOutputStream = new java.io.FileOutputStream(head_file2)) {     //存入后格式为 map.size() add0Num map的内容 sb的内容
                //将辅助信息写入文件，map的内容和add0Num
                StringBuilder sb2 = new StringBuilder();
                sb2.append(String.valueOf(map.size()));
                sb2.append(" ");
                sb2.append(String.valueOf(add0Num));
                sb2.append(" ");



                for (Map.Entry<String, Integer> entry : map.entrySet()) {
                    String key = entry.getKey();
                    sb2.append(key);
                    sb2.append(" ");
                    sb2.append(entry.getValue());
                    sb2.append(" ");
                }
                fileOutputStream.write(sb2.toString().getBytes());


            } catch (IOException e) {
                e.printStackTrace();
            }
            try(java.io.FileOutputStream fileOutputStream1=new java.io.FileOutputStream(file2)){
                //将String的sb的二进制字符串每八位转换为一个字符写入文件
                for (int i = 0; i < sb.length(); i+=8) {
                    String s = sb.substring(i, i+8);
                    int num = Integer.parseInt(s, 2);   //将二进制字符串转换为十进制
                    byte[] bytes2 = new byte[1];
                    bytes2[0] = (byte)num;
                    fileOutputStream1.write(bytes2);
                }
            }catch (IOException e) {
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

        File file1=new File(filename);
        File head_file1=new File("head_"+filename);
        try(FileInputStream fileInputStream = new FileInputStream(head_file1)){
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
            int k=0;
            for (int i = 0; i < mapSize; i++) { //将map中的数据存入map中
                //读三位转换为一个字符
                String key = s[index++];
            }
            //构建哈夫曼树
            buildHuffmantrees();
            //解码
            StringBuilder sb2 = new StringBuilder();
            //sb中剩下的数据，全部读入为二进制字符串
            for (int i = index; i < s.length; i++) {
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
    public String decompressFileInCache(String filename){
        String result= new String();
        int mapSize=0;
        int add0Num=0;
        String filepath = "./diary_compress/";
        File file1=new File(filepath+filename);
        File head_file1=new File(filepath+"head_"+filename);

        try(FileInputStream fileInputStream = new FileInputStream(head_file1)){
            byte[] bytes = new byte[1048576];
            int index = 0;
            StringBuilder sb = new StringBuilder();
            int len = 0;
            while((len=fileInputStream.read(bytes))!=-1){
                sb.append(new String(bytes,0, len,StandardCharsets.UTF_8));
            }
            int is=0;
            String[] s = sb.toString().split(" ");
            mapSize = Integer.parseInt(s[0]);   //遇到空格就分割
            add0Num = Integer.parseInt(s[1]);
            index = 2;
            for (int i = 0; i < mapSize; i++) { //将map中的数据存入map中
                String key = s[index++];
                int value = Integer.parseInt(s[index++]);
                map.put(key, value);
            }
            //构建哈夫曼树
            buildHuffmantrees();

            int k=0;


        }catch(FileNotFoundException e){
            e.printStackTrace();
        }catch(IOException e){
            e.printStackTrace();
        }
        try(FileInputStream fileInputStream1=new FileInputStream(file1)){
            int len2=0;
            int lenreal=0;
            byte[] bytes1=new byte[1048576];

            while((len2=fileInputStream1.read(bytes1))!=-1){
                lenreal=len2;
            }
            String temp=new String();
            StringBuilder sb4 =new StringBuilder(temp);
            for(int k=0;k<lenreal;k++){
                byte b=bytes1[k];
                for(int i=7;i>=0;i--){
                    int bit=(b>>i)&1;
                    sb4.append(String.valueOf(bit));
                    int is=0;
                }
            }
            String results=sb4.toString();
            //删除后面的
            results=results.substring(0,results.length()-add0Num);
            //根据构造的哈夫曼树进行解码
            int current = 2*mapSize-2;


            StringBuilder resultBuilder = new StringBuilder();
            for (int i = 0; i < results.length(); i++) {

                if ( results.charAt(i)== '0') {
                    current = huffmantree.get(current).lchild;
                } else {
                    current = huffmantree.get(current).rchild;
                }
                if (huffmantree.get(current).lchild == -1 && huffmantree.get(current).rchild == -1) {
                    String data = huffmantree.get(current).data;
                    resultBuilder.append(data);
                    current = 2*mapSize-2;
                }
            }
            result = resultBuilder.toString();

        }catch(FileNotFoundException e){
            e.printStackTrace();
        }catch (IOException e){
            e.printStackTrace();
        }

        return result;
    }
    public String readoutCache(String author,int num){
        String filename = author+"_"+num+"compress.txt";

        String result = decompressFileInCache(filename);
        return result;
    }
    public void writeInFile(String filenameIn){
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
                if(c>='0'&&c<='9'){
                    num+=c;
                }
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
        filenameOut=name+"_"+num+"compress.txt";
        buildAll(filenameIn);
        buildHuffmantrees();
        encoding();
        compress(filenameIn,filenameOut);
    }


}
