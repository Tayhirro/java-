import java.io.File;
import java.io.FileNotFoundException;
import java.util.Map;
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
    ArrayList<HuffmanNode> huffmantree;
    Map<String, Integer> map;
    Map<String, String> code;
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
    private void  readingFile(String filename){
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
                    byte[] bytes2 = new byte[2];
                    bytes2[0] = bytes[0];
                    bytes2[1] = (byte)fileInputStream.read();
                    String s = new String(bytes2);
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
    //ѹ���ļ�


}
