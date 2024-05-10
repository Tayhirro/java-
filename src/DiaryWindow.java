import javax.swing.*;

import javax.swing.*;

/**
 * DiaryWindow 是 JFrame 的子类，代表日记应用程序的主窗口。
 * 它设置了窗口的标题、大小、默认关闭操作和位置。
 */
public class DiaryWindow extends JFrame {
    /**
     * DiaryWindow 类的构造函数。
     * 它初始化窗口，设置标题、大小、默认关闭操作和位置。
     * 应在指定位置添加其他组件。
     */
    public DiaryWindow() {
        setTitle("游学日记"); // 设置窗口的标题
        setSize(320, 200); // 设置窗口的大小
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE); // 设置默认关闭操作
        setLocationRelativeTo(null); // 将窗口居中
        // 在这里添加你的组件
    }
}