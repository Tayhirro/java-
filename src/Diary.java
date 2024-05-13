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
        // ���ļ��ж�ȡ�ռǱ���
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





