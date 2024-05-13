import java.io.File;
import java.io.FileNotFoundException;
import java.util.Scanner;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.List;
public class Diary {
    private List<String> title;
    private List<String> content;

    public Diary() {
        // 从文件中读取日记标题
        injectContent();
    }

    public String getTitle() {
        return title;
    }

    public String getContent() {
        return content;
    }

    public String getDate() {
        return date;
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
                while (scanner.hasNextLine()) {
                    String line = scanner.nextLine();
                    if (line.startsWith("Title:")) {
                        title.add(line.substring(6));
                    } else {
                        content.add(line);
                    }
                }
            } catch (FileNotFoundException e) {
                e.printStackTrace();
            }
        }
    }



}





