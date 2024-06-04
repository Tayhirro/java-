import javax.swing.*;
import java.awt.*;

public class TextPanel extends JPanel {
    private String title;
    private String author;
    private  int num;
    private int rating;
    private int viewCount;
    private String text;
    private HuffmanCode huffmanCode;
    public TextPanel(Diary_title diary) {
        this.title = diary.getTitle();
        this.author = diary.getAuthor();
        this.num = diary.getNum();
        this.rating = diary.getRating();
        this.viewCount = diary.getViewCount();
        huffmanCode = new HuffmanCode();
        //解码，使用huffmancode类中的方法
        this.text = huffmanCode.readoutCache(author, num);
        // 设置布局管理器
        setLayout(new BorderLayout());

        // 创建标题标签
        JLabel titleLabel = new JLabel(this.title, SwingConstants.CENTER);
        titleLabel.setFont(new Font("Serif", Font.BOLD, 24));
        add(titleLabel, BorderLayout.NORTH);

        // 创建作者和正文面板
        JPanel centerPanel = new JPanel();
        centerPanel.setLayout(new BorderLayout());

        // 创建作者标签
        JLabel authorLabel = new JLabel("作者: " + this.author, SwingConstants.CENTER);
        authorLabel.setFont(new Font("Serif", Font.PLAIN, 18));
        centerPanel.add(authorLabel, BorderLayout.NORTH);

        // 创建正文文本区域
        JTextArea textArea = new JTextArea(this.text);
        textArea.setLineWrap(true);
        textArea.setWrapStyleWord(true);
        textArea.setEditable(false);
        JScrollPane scrollPane = new JScrollPane(textArea);
        centerPanel.add(scrollPane, BorderLayout.CENTER);

        // 添加中心面板到主面板
        add(centerPanel, BorderLayout.CENTER);

        // 创建评分和浏览量面板
        JPanel bottomPanel = new JPanel();
        bottomPanel.setLayout(new GridLayout(1, 2)); // 使用 GridLayout 布局

        // 创建评分标签
        JLabel ratingLabel = new JLabel("评分: " + this.rating, SwingConstants.CENTER);
        bottomPanel.add(ratingLabel);

        // 创建浏览量标签
        JLabel viewCountLabel = new JLabel("浏览量: " + this.viewCount, SwingConstants.CENTER);
        bottomPanel.add(viewCountLabel);

        // 添加底部面板到主面板
        add(bottomPanel, BorderLayout.SOUTH);
    }

}
