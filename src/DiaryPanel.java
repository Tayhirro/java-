import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.util.Arrays;

public class DiaryPanel extends JPanel {
    private String[] diaryTitles;
    private int[] rate;
    private String[] locations;
    private int[] readnum;
    private Diary_title[] diaries ;


    class DiaryCellRenderer extends JLabel implements ListCellRenderer<Diary_title> {
        private JLabel titleLabel = new JLabel();
        private JLabel authorLabel = new JLabel();
        private JLabel ratingLabel = new JLabel();
        private JLabel viewCountLabel = new JLabel();

        public DiaryCellRenderer() {
            setLayout(new BorderLayout());
            JPanel rightPanel = new JPanel(new GridLayout(2, 1));
            rightPanel.add(ratingLabel);
            rightPanel.add(viewCountLabel);
            add(titleLabel, BorderLayout.WEST);
            add(authorLabel, BorderLayout.SOUTH);
            add(rightPanel, BorderLayout.EAST);
        }
        @Override
        public Component getListCellRendererComponent(JList<? extends Diary_title> list, Diary_title diary, int index, boolean isSelected, boolean cellHasFocus) {
            titleLabel.setText(diary.getTitle());
            authorLabel.setText(diary.getAuthor());
            ratingLabel.setText("Rating: " + diary.getRating());
            viewCountLabel.setText("Views: " + diary.getViewCount());
            return this;
        }
    }


    public DiaryPanel() {
        readAllDiary();
        setLayout(new BorderLayout());
// 创建一个 JList 用于显示日记
        JList<Diary_title> diaryList = new JList<>(diaries);
        diaryList.setFixedCellHeight(70);
        diaryList.setCellRenderer(new DiaryCellRenderer());
        //添加点击事件
        diaryList.addMouseListener(new DiaryMouseListener(diaryList,diaries));

        JScrollPane scrollPane = new JScrollPane(diaryList);
// 将 JScrollPane 添加到面板
        add(scrollPane, BorderLayout.CENTER);


    }

    private void readAllDiary() {
        // 读取所有在./diary_title/下的日记标题
        File folder = new File("./diary_title/");
        File[] listOfFiles = folder.listFiles();
        if (listOfFiles != null) {
            diaryTitles = new String[listOfFiles.length];
            rate = new int[listOfFiles.length];
            locations = new String[listOfFiles.length];
            readnum =new  int[listOfFiles.length];
            diaries = new Diary_title[listOfFiles.length];
            for (int i = 0; i < listOfFiles.length; i++) {
                if (listOfFiles[i].isFile()) {
                    try (BufferedReader reader = new BufferedReader(new FileReader(listOfFiles[i]))) {

                        diaryTitles[i] = reader.readLine();
                        rate[i] = Integer.parseInt(reader.readLine());
                        locations[i] = reader.readLine();
                        readnum[i] = Integer.parseInt(reader.readLine());
                        int num=0;
                        String names =new String();
                        String name = listOfFiles[i].getName();
                        num = Integer.parseInt(name.substring(name.indexOf("_")+1, name.indexOf("t")));
                        names = name.substring(0, name.indexOf("_"));
                        diaries[i] = new Diary_title(diaryTitles[i],names, rate[i], readnum[i],num);
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            }
        }

    }
    private JPanel createText(Diary_title diary) {
        TextPanel textPanel = new TextPanel(diary);
        return textPanel;
    }
    class   DiaryMouseListener extends MouseAdapter {
        private JList<Diary_title> diaryList;
        private Diary_title[] diaries;

        public DiaryMouseListener(JList<Diary_title> diaryList, Diary_title[] diaries) {
            this.diaryList = diaryList;
            this.diaries = diaries;
        }

        @Override
        public void mouseClicked(MouseEvent e) {
            if (e.getClickCount() == 2) {
                int index = diaryList.locationToIndex(e.getPoint());
                if (index >= 0) {
                    Diary_title diary = diaries[index];
                    refreshPanel(diary);
                }
            }
        }
    }
    public void refreshPanel(Diary_title diary) {
        // 移除当前面板上的所有组件
        this.removeAll();

        // 创建新的 JPanel
        JPanel newPanel =createText(diary);

        // 将新的 JPanel 添加到当前面板
        this.add(newPanel);

        // 重新验证组件布局并重绘
        this.revalidate();
        this.repaint();
    }
}
class Diary_title{
    private String title;
    private String author;
    private int num;
    private int rating;
    private int viewCount;
    public String getTitle() {
        return title;
    }
    public String getAuthor() {
        return author;
    }
    public int getRating() {
        return rating;
    }
    public int getViewCount() {
        return viewCount;
    }
    public int getNum() {
        return num;
    }
    public Diary_title(String title, String author, int rating, int viewCount,int num) {
        this.title = title;
        this.author = author;
        this.rating = rating;
        this.viewCount = viewCount;
        this.num=num;
    }
}

