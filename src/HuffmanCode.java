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
        String data; //�ַ�
        int weight; //Ȩֵ
        int parent; //���ڵ�
        int lchild; //����
        int rchild; //�Һ���
    }
    ArrayList<HuffmanNode> huffmantree; //��������
    Map<String, Integer> map;           //�洢�ַ�����Ȩֵ
    Map<String, String> code;           //�洢�ַ��������
    public HuffmanCode(){
        huffmantree = new ArrayList<HuffmanNode>();
        map = new HashMap<String, Integer>();
    }
    private boolean judgeEng(byte c) {
        if ((c & 0x80)==0)
            return true;
        return false;
    }
    //��ȡ�ļ�������Ǻ��֣����ٶ�ȡ2���ֽڣ������ȡһ���ֽ�



    private void  buildAll(String filename){
        //���ֽ�Ϊ��λ��ȡ�ļ�����
        File file = new File(filename);
        try (FileInputStream fileInputStream = new FileInputStream(file)) {
            byte[] bytes = new byte[1];
            int len = 0;
            while((len=fileInputStream.read(bytes))!=-1){
                if(judgeEng(bytes[0])){ //�����Ӣ���ַ�
                    String s = new String(bytes);   //���ֽ�����ת��Ϊ�ַ���
                    if(map.containsKey(s)){
                        map.put(s, map.get(s)+1);   //���map���Ѿ����ڸ��ַ����򽫸��ַ���Ȩֵ��1
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
    //��map�е����ݴ���arraylist��

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
    //������������
    private void buildHuffmantrees(){
        int n = map.size();
        for (Map.Entry<String, Integer> entry : map.entrySet()) {
            HuffmanNode node = new HuffmanNode();
            node.data = entry.getKey();
            node.weight = entry.getValue(); //getvalue����int����
            node.parent = -1;
            node.lchild = -1;
            node.rchild = -1;
            huffmantree.add(node);
        }   //��map�е����ݴ���arraylist��
        int[] s1 = new int[2];
        for (int i =n ; i <2*n-1; i++) {
            HuffmanNode node = new HuffmanNode();
            selectTwo(i, s1);
            //�ҵ�Ȩֵ��С�������ڵ�
            huffmantree.get(s1[0]).parent = i;
            huffmantree.get(s1[1]).parent = i;
            
            //�����½ڵ�
            node.weight = huffmantree.get(s1[0]).weight + huffmantree.get(s1[1]).weight;
            node.lchild = s1[0];
            node.rchild = s1[1];
            node.parent = -1;
            huffmantree.add(node);
        }
    }
    //����
    private void encoding(){
        int n = map.size();
        for (int i = 0; i < n; i++) {   //��Ҷ�ӽڵ㿪ʼ���룬��ÿ��Ҷ�ӽڵ���б���
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
    //ѹ���ļ�   ���������������뵽һ���ļ��У�����ļ�����ѹ��
    public void compress(String filename,String compressedFilename){
        //�����map��arraylist
            huffmantree.clear();
            map.clear();
            code.clear();

            buildAll(filename);
            buildHuffmantrees();
            encoding();
            //���ֽ�Ϊ��λ��ȡ�ļ�����
            File file = new File(filename);
            try (FileInputStream fileInputStream = new FileInputStream(file)) {
                byte[] bytes = new byte[1];
                int len = 0;
                StringBuilder sb = new StringBuilder();
                while((len=fileInputStream.read(bytes))!=-1){
                    if(judgeEng(bytes[0])){ //�����Ӣ���ַ�
                        String s = new String(bytes);   //���ֽ�����ת��Ϊ�ַ���
                        sb.append(code.get(s));         //���ַ���Ӧ�ı�����ӵ�sb��
                    }else{
                        byte[] bytes2 = new byte[3];
                        bytes2[1] = bytes[1];
                        bytes2[2] = (byte)fileInputStream.read();
                        String s = new String(bytes2);
                        //�����ֽڶ�Ӧһ���ַ�
                        sb.append(code.get(s));
                    }
                }
                //��sb�е�����д���ļ�
                //���ֽ�Ϊ��λд���ļ�
                int add0Num = 8 - sb.length() % 8;
                for (int i = 0; i < add0Num; i++) {
                    sb.append("0");
                }
                
                File file2 = new File(compressedFilename);
                try (java.io.FileOutputStream fileOutputStream = new java.io.FileOutputStream(file2)) {     //������ʽΪ map.size() add0Num map������ sb������
                    //��������Ϣд���ļ���map�����ݺ�add0Num
                    StringBuilder sb2 = new StringBuilder();
                    sb2.append(map.size());
                    sb2.append(" ");
                    sb2.append(add0Num);
                    sb2.append(" ");
                    for (Map.Entry<String, Integer> entry : map.entrySet()) {
                        sb2.append(entry.getKey());     //get������һ��String����
                        sb2.append(" ");
                        sb2.append(entry.getValue());
                        sb2.append(" ");
                    }
                    //��String��sb�Ķ������ַ���ÿ��λת��Ϊһ���ַ�д���ļ�
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
    //��ѹ  ��һ��ѹ���ļ��н�ѹ��ԭ�ļ�
    private void decompressFile(String filename, String decompressFilename){
        //�����map��arraylist
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
            //��ȡmap�Ĵ�С��add0Num
            String[] s = sb.toString().split(" ");
            int mapSize = Integer.parseInt(s[0]);   //�����ո�ͷָ�
            int add0Num = Integer.parseInt(s[1]);
            index = 2;
            for (int i = 0; i < mapSize; i++) { //��map�е����ݴ���map��
                String key = s[index++];
                int value = Integer.parseInt(s[index++]);
                map.put(key, value);
            }
            //������������
            buildHuffmantrees();
            //����
            StringBuilder sb2 = new StringBuilder();
            //sb��ʣ�µ����ݣ�ȫ������Ϊ�������ַ���
            for (int i = index; i < s.length-add0Num; i++) {
                sb2.append(s[i]);
            }   //��sb2�е�����д���ļ�,s.length-add0Num����Ϊ���add0Num���ǲ�0��
            //���ݹ���Ĺ����������н���
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
    public void readoutFile(String String,int i){       //iΪ��ǰ�û��µĵڼ�ƪ�ռ�
        //��ѹ���ļ��ж�ȡ
        decompressFile(String,String+ "decompress"+i+".txt");   //�����һ���ļ���  
    }
    public void writeInFile(String filenameIn){   //iΪ��ǰ�û��µĵڼ�ƪ�ռ�
        String filenameOut= new String();
        String name = new String ();
        String  num = new String();
        char c;
        int tag=0;
        //ɨ���ļ���,һ��һ���ַ���ȡ
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
