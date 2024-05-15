import java.io.File;
import java.io.FileNotFoundException;
import java.util.Scanner;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import java.util.List;
import java.util.ArrayList;

import java.io.PrintWriter;
import java.io.UnsupportedEncodingException;

public class Diary {
    private List<String> title;
    private List<String> content;
    private List<Integer> spotNum;
    private List<Integer> grade;
    
    private String username;
    private HuffmanCode huffmanCode;




    private List<String> allUsername;
    public Diary(User user) {
        //�������ռ�ȫ������title��content��
        title = new ArrayList<>();  //�ռǱ���
        content = new ArrayList<>();  //�ռ�����
        spotNum = new ArrayList<>();  //��������
        grade = new ArrayList<>();  //�ռ�����  
        username=user.getUsername();
        huffmanCode = new HuffmanCode();
        allUsername = new ArrayList<>();
        try(Scanner scanner = new Scanner(new File("users.txt"))){
            while(scanner.hasNextLine()){
                String line = scanner.nextLine();
                String[] parts = line.split(",");
                allUsername.add(parts[0]);
            }
        }catch(FileNotFoundException e){
            e.printStackTrace();
        }
        injectContentAll();
    }

    public List<String> getTitle() {
        return title;
    }

    public List<String> getContent() {
        return content;
    }

    private void injectContentAll(){
        for(String username: allUsername){
            injectContent(username);
        }
    }
    //content���ļ��ж�ȡ���ļ���Ϊtitle������username���ռ�
    //�ļ���ʽΪ��username_titlex.txt
    //x Ϊ���֣���1��ʼ����ʾ�ڼ�ƪ�ռǣ�ʹ��������ʽƥ���ļ���


    public void injectContent(String username){
        //����ƥ���ļ���
        int i = 1;
        while (true) {
            //�����ʾֻҪ����λ���־���
            String filename = username + "_title" + i + ".txt";
            i++;
            if(!new File(filename).exists()){
                break;
            }
            try (Scanner scanner = new Scanner(new File(filename),"UTF-8")) {
                //������ļ����н�ѹ��ʹ��HuffmanCode��
                HuffmanCode huffmanCode = new HuffmanCode();
                huffmanCode.writeInFile(filename);
                //huffmanCode.readoutFile(filename, i);
                //����ѹ������ݴ���content
                

            } catch (FileNotFoundException e) {
                e.printStackTrace();
            }
        }
    }
    public void writeInContent(String text){
        //����ƥ���ļ���
        int i = 1;
        String filename = username + "_title" + i + ".txt";
        while (true) {
            //�����ʾֻҪ����λ���־���
            filename = username + "_title" + i + ".txt";
            i++;
            if(!new File(filename).exists()){
                break;
            }
        }
        //��textд���ļ�
        try (PrintWriter writer = new PrintWriter(filename, "UTF-8")) {
            writer.println(text);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }

    }
}





