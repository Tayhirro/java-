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
        //将所有日记全部读入title和content中
        title = new ArrayList<>();  //日记标题
        content = new ArrayList<>();  //日记内容
        spotNum = new ArrayList<>();  //景点评分
        grade = new ArrayList<>();  //日记评分  
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
    //content从文件中读取，文件名为title，属于username的日记
    //文件格式为：username_titlex.txt
    //x 为数字，从1开始，表示第几篇日记，使用正则表达式匹配文件名


    public void injectContent(String username){
        //正则匹配文件名
        int i = 1;
        while (true) {
            //正则表示只要后面位数字就行
            String filename = username + "_title" + i + ".txt";
            i++;
            if(!new File(filename).exists()){
                break;
            }
            try (Scanner scanner = new Scanner(new File(filename),"UTF-8")) {
                //对这个文件进行解压，使用HuffmanCode类
                HuffmanCode huffmanCode = new HuffmanCode();
                huffmanCode.writeInFile(filename);
                //huffmanCode.readoutFile(filename, i);
                //将解压后的内容存入content
                

            } catch (FileNotFoundException e) {
                e.printStackTrace();
            }
        }
    }
    public void writeInContent(String text){
        //正则匹配文件名
        int i = 1;
        String filename = username + "_title" + i + ".txt";
        while (true) {
            //正则表示只要后面位数字就行
            filename = username + "_title" + i + ".txt";
            i++;
            if(!new File(filename).exists()){
                break;
            }
        }
        //将text写入文件
        try (PrintWriter writer = new PrintWriter(filename, "UTF-8")) {
            writer.println(text);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }

    }
}





