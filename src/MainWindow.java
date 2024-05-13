import javax.swing.*;
import javax.swing.border.Border;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;

import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.Arrays;
import java.util.List;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.HashSet;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;
import java.text.DecimalFormat;
import java.util.Map;
public class MainWindow extends JFrame {    
    private JFrame frame;
    private JPanel contentPanel;
    private JPanel tabBarPanel;
    private JButton tab1Button;
    private JButton tab2Button;
    private JButton tab3Button;
    private JButton tab4Button;

//-----------------推荐系统---------------------
    private RecommendationSystem recommendationSystem; // 推荐系统
    private List<String> userInterests; // 用户兴趣列表
    private SearchSystem searchSystem; // 搜索系统
    private List<CenterPlace> tagRecommendations;   //个性推荐


    private CenterPlaceDataLoader dataLoader; // 数据加载器
    private List<CenterPlace> searchResults;
    private JPanel subPage1ContentPanel;
    private JPanel subPage1TabBarPanel;
    private JButton subPage1Tab1Button;
    private JButton subPage1Tab2Button;
    private JButton subPage1Tab3Button;
    private HashSet<String> tagList;
    private JList<String> tagListJList;
//---------------------------------------------
//前十推荐
    private List<CenterPlace> top10CenterPlaces;

    /**
     * 显示前十推荐的中心地点。
     */
    private List<CenterPlace> showTop10Recommendations(List<CenterPlace> top10CenterPlaces) {
        top10CenterPlaces = recommendationSystem.recommendCenterPlaces(0.6, 0.5, userInterests);
        StringBuilder message = new StringBuilder("Top 10 Recommended CenterPlaces:\n");
        for (CenterPlace centerPlace : top10CenterPlaces) {
            message.append(centerPlace.getName()).append("\n");
        }
        return top10CenterPlaces;
    
    }
//---------------------------------------------
//搜索
    /**
     * 显示搜索结果。
     */
    private List<CenterPlace> showSearchResults(List<CenterPlace> searchResults, String query) {
       // String query = JOptionPane.showInputDialog(this, "请输入查询关键词"); // 获取用户输入
        double popularityWeight = 0.6;
        double ratingWeight = 0.5;
        searchResults = searchSystem.searchAndSort(query, popularityWeight, ratingWeight);
        StringBuilder message = new StringBuilder("Search Results:\n");
        for (CenterPlace centerPlace : searchResults) {
            message.append(centerPlace.getName()).append("\n");
        }
        return searchResults;
    }
//---------------------------------------------
//个性推荐

    /**
     * 根据标签显示推荐的中心地点。
     */
    private List<CenterPlace> showRecommendationsByTags(List<CenterPlace> tagRecommendations, List<String> tags) {


        // String input = JOptionPane.showInputDialog(this, "请输入标签，用逗号分隔"); // 获取用户输入
        // List<String> tags = Arrays.asList(input.split(","));
        double popularityWeight = 0.6;
        double ratingWeight = 0.5;
        tagRecommendations = recommendationSystem.recommendCenterPlacesByTags(tags, popularityWeight, ratingWeight);
        StringBuilder message = new StringBuilder("Recommendations by Tags:\n");
        for (CenterPlace centerPlace : tagRecommendations) {
            message.append(centerPlace.getName()).append("\n");
        }
        return tagRecommendations;
    }


    



//-----------------路线规划---------------------


    private JPanel subPage2ContentPanel;
    private JPanel subPage2TabBarPanel;
    private JButton subPage2Tab1Button;
    private JButton subPage2Tab2Button;
    private JButton subPage2Tab3Button;
    

//-----------------场所查询---------------------
    
        private JPanel subPage3ContentPanel;
        private JPanel subPage3TabBarPanel;
        private JButton subPage3Tab1Button;
        private JButton subPage3Tab2Button;
        private JButton subPage3Tab3Button;



//-----------------游学日记---------------------







    public MainWindow(RecommendationSystem recommendationSystem, SearchSystem searchSystem, List<String> userInterests, CenterPlaceDataLoader dataLoader) {
        this.recommendationSystem = recommendationSystem;
        this.userInterests = userInterests;
        this.searchSystem = searchSystem;
        this.dataLoader = dataLoader;

        // 从文件中读取标签列表
        List<CenterPlace> centerPlaces = dataLoader.getCenterPlaces();
        tagList = new HashSet<>(new HashSet<>());


        for (CenterPlace centerPlace : centerPlaces) {
            tagList.addAll(centerPlace.getKeywords());
        }


        tagListJList = new JList<>(tagList.toArray(new String[0])); // 标签列表,将hashset转换为数组



        frame = new JFrame("游学推荐系统");
        contentPanel = new JPanel();
        tabBarPanel = new JPanel(new GridLayout(1, 4));
        tab1Button = createTabButton("游学推荐");
        tab2Button = createTabButton("路线规划");
        tab3Button = createTabButton("场所查询");
        tab4Button = createTabButton("游学日记");

        // 添加选项卡按钮到选项卡面板
        tabBarPanel.add(tab1Button);
        tabBarPanel.add(tab2Button);
        tabBarPanel.add(tab3Button);
        tabBarPanel.add(tab4Button);

        // 设置选项卡面板在底部
        tabBarPanel.setBorder(BorderFactory.createEmptyBorder(10, 0, 0, 0));
        tabBarPanel.setBackground(Color.WHITE);

        // 设置内容面板的布局和背景颜色
        contentPanel.setLayout(new BorderLayout());
        contentPanel.setBackground(Color.LIGHT_GRAY);
        //-----------------------------------------------------------------------------------------------------------------------------------------



        // 创建选项卡按钮的事件监听器
        ActionListener tabButtonListener = new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                // 根据点击的选项卡按钮切换内容
                if (e.getSource() == tab1Button) {
                    contentPanel.removeAll();
                  //  contentPanel.add(label1, BorderLayout.CENTER);
                    contentPanel.add(createSubPage1ContentPanel(), BorderLayout.CENTER);
                



                } else if (e.getSource() == tab2Button) {
                    contentPanel.removeAll();
                    contentPanel.add(createSubPage2ContentPanel(), BorderLayout.CENTER);










                } else if (e.getSource() == tab3Button) {
                    contentPanel.removeAll();
                    JLabel label3 = new JLabel("这是选项卡3的内容");
                    label3.setHorizontalAlignment(SwingConstants.CENTER);
                    contentPanel.add(label3, BorderLayout.CENTER);
                } else if (e.getSource() == tab4Button) {
                    contentPanel.removeAll();
                    JLabel label4 = new JLabel("这是选项卡4的内容");
                    label4.setHorizontalAlignment(SwingConstants.CENTER);
                    contentPanel.add(label4, BorderLayout.CENTER);
                }

                contentPanel.revalidate();
                contentPanel.repaint();
            }
        };
        // 给选项卡按钮添加事件监听器
        tab1Button.addActionListener(tabButtonListener);
        tab2Button.addActionListener(tabButtonListener);
        tab3Button.addActionListener(tabButtonListener);
        tab4Button.addActionListener(tabButtonListener);

        // 模拟点击第一个选项卡按钮
        tab1Button.doClick();
        // 将选项卡面板和内容面板添加到窗口中
        frame.getContentPane().setLayout(new BorderLayout());
        frame.getContentPane().add(contentPanel, BorderLayout.CENTER);
        frame.getContentPane().add(tabBarPanel, BorderLayout.SOUTH);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setSize(800, 600);
        frame.setLocationRelativeTo(null);
        frame.setVisible(true);

    }

    private JButton createTabButton(String text) {
        JButton button = new JButton(text);
        button.setFocusPainted(false);
        button.setBorder(BorderFactory.createEmptyBorder(5, 10, 5, 10));
        return button;
    }


    // 创建子页面1的内容面板
    private JPanel createSubPage1ContentPanel() {
        subPage1ContentPanel = new JPanel();
        subPage1TabBarPanel = new JPanel(new GridLayout(1, 3));
        subPage1Tab1Button = createTabButton("前十推荐");
        subPage1Tab2Button = createTabButton("景点/学校查询");
        subPage1Tab3Button= createTabButton("个性推荐");


        // 添加子选项卡按钮到子选项卡面板
        subPage1TabBarPanel.add(subPage1Tab1Button);
        subPage1TabBarPanel.add(subPage1Tab2Button);
        subPage1TabBarPanel.add(subPage1Tab3Button);



        // 设置子选项卡面板在顶部
        subPage1TabBarPanel.setBorder(BorderFactory.createEmptyBorder(0, 0, 10, 0));
        subPage1TabBarPanel.setBackground(Color.WHITE);

        // 设置子选项卡内容面板的布局和背景颜色
        subPage1ContentPanel.setLayout(new BorderLayout());
        subPage1ContentPanel.setBackground(Color.LIGHT_GRAY);


        
        // 创建子选项卡按钮的事件监听器
        ActionListener subPageTabButtonListener = new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                // 根据点击的子选项卡按钮切换内容
                if (e.getSource() == subPage1Tab1Button) {
                    subPage1ContentPanel.removeAll();   
                    //显示前十推荐
                    top10CenterPlaces = showTop10Recommendations(top10CenterPlaces);
                    //显示在subPage1ContentPanel上
                    //JLabel subPageLabel1放在顶上
                    

                    //使用垂直盒子容器显示前十推荐的中心地点
                    Box centerPlacesBox = Box.createVerticalBox();
                    for (CenterPlace centerPlace : top10CenterPlaces) {
                        // 创建标签并添加到垂直盒子容器中
                        JLabel centerPlaceLabel = new JLabel(centerPlace.getName());
                        centerPlaceLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
                        centerPlacesBox.add(centerPlaceLabel);
                    }
                    // 创建滚动面板并添加到中心面板
                    JPanel centerPlacesPanel = new JPanel();
                    centerPlacesPanel.setLayout(new BorderLayout());
                    centerPlacesPanel.add(centerPlacesBox, BorderLayout.CENTER);
                    //显示在subPage1ContentPanel上
                    StringBuilder message = new StringBuilder("Top 10 Recommended CenterPlaces:\n");
                    JLabel subPageLabel1 = new JLabel(message.toString());
                    //JLabel subPageLabel1放在顶上
                    subPageLabel1.setHorizontalAlignment(SwingConstants.CENTER);
                    subPage1ContentPanel.removeAll();
                    subPage1ContentPanel.setLayout(new BorderLayout());
                    subPage1ContentPanel.add(subPageLabel1, BorderLayout.NORTH);
                    subPage1ContentPanel.add(centerPlacesPanel, BorderLayout.CENTER);
        


                } else if (e.getSource() == subPage1Tab2Button) {
                    subPage1ContentPanel.removeAll();

                    JTextField searchField = new JTextField();
                    //显示“请输入地点”
                    searchField.setText("请输入地点");
                    searchField.addActionListener(new ActionListener() {
                        @Override
                        public void actionPerformed(ActionEvent e) {
                            String query = searchField.getText();
                            searchResults = showSearchResults(searchResults, query);
                    
                            //显示在subPage1ContentPanel上
                            StringBuilder message = new StringBuilder("Search Results:\n");
                            for (CenterPlace centerPlace : searchResults) {
                                message.append(centerPlace.getName()).append("\n");
                            }
                            JLabel subPageLabel2 = new JLabel(message.toString());
                            subPageLabel2.setHorizontalAlignment(SwingConstants.CENTER);
                            //JLabel subPageLabel2放在顶上
                            subPage1ContentPanel.add(subPageLabel2, BorderLayout.NORTH);
                            refreshSubPage1ContentPanel(searchResults);
                        }
                    });
                    subPage1ContentPanel.add(searchField, BorderLayout.NORTH);
                    searchField.requestFocusInWindow(); // 设置文本框获得焦点
                    frame.revalidate(); // 重新验证并刷新界面
                    //显示在subPage1ContentPanel上

                } else if (e.getSource() == subPage1Tab3Button) {
                    subPage1ContentPanel.removeAll();
                    //显示个性推荐
                    //右边显示推荐的中心地点
                    StringBuilder message = new StringBuilder("Recommendations by Tags:\n");
                    JLabel subPageLabel3 = new JLabel(message.toString());
                    subPageLabel3.setHorizontalAlignment(SwingConstants.CENTER);
                    subPage1ContentPanel.add(subPageLabel3, BorderLayout.NORTH);
                    JScrollPane scrollPane = new JScrollPane(tagListJList); 
                    //显示在subPage3ContentPanel上
                    subPage1ContentPanel.setLayout(new BorderLayout());
                    subPage1ContentPanel.add(scrollPane, BorderLayout.WEST);
                }

                subPage1ContentPanel.revalidate();
                subPage1ContentPanel.repaint();
            }
        };

        // 给子选项卡按钮添加事件监听器
 
        subPage1Tab1Button.addActionListener(subPageTabButtonListener);
        subPage1Tab2Button.addActionListener(subPageTabButtonListener);
        subPage1Tab3Button.addActionListener(subPageTabButtonListener);

        //tagListJList的事件监听器
        tagListJList.addListSelectionListener(new ListSelectionListener() {
            @Override
            public void valueChanged(ListSelectionEvent e) {
                if (!e.getValueIsAdjusting()) {
                    String selectedTag = tagListJList.getSelectedValue();
                    if (selectedTag != null) {
                        // 根据选中的标签显示推荐的中心地点
                        tagRecommendations = showRecommendationsByTags(tagRecommendations, Arrays.asList(selectedTag));
                        //显示在subPage1ContentPanel上
                        //使用垂直盒子容器显示推荐的中心地点
                        Box centerPlacesBox = Box.createVerticalBox();
                        for (CenterPlace centerPlace : tagRecommendations) {
                            // 创建标签并添加到垂直盒子容器中
                            JLabel centerPlaceLabel = new JLabel(centerPlace.getName());
                            centerPlaceLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
                            centerPlacesBox.add(centerPlaceLabel);
                        }
                        subPage1ContentPanel.removeAll();
                        // 创建滚动面板并添加到中心面板
                        JPanel centerPlacesPanel = new JPanel();
                        centerPlacesPanel.setLayout(new BorderLayout());
                        centerPlacesPanel.add(centerPlacesBox, BorderLayout.CENTER);
                        //显示在subPage1ContentPanel上
                        subPage1ContentPanel.setLayout(new BorderLayout());
                        subPage1ContentPanel.add(centerPlacesPanel, BorderLayout.CENTER);
                        frame.revalidate(); // 重新验证并刷新界面
                    }

                }
            }
        });
        
        // 将子选项卡面板和子选项卡内容面板添加到子页面中
        JPanel subPagePanel = new JPanel(new BorderLayout());
        subPagePanel.add(subPage1TabBarPanel, BorderLayout.NORTH);
        subPagePanel.add(subPage1ContentPanel, BorderLayout.CENTER);

        return subPagePanel;
    }
    // 创建子页面2的内容面板
    private JPanel createSubPage2ContentPanel() {
        subPage2ContentPanel = new JPanel();
        subPage2TabBarPanel = new JPanel(new GridLayout(1, 3));
        subPage2Tab1Button = createTabButton("查看地图");
        subPage2Tab2Button = createTabButton("单点路线规划");
        subPage2Tab3Button= createTabButton("多点路线规划");
        
        // 添加子选项卡按钮到子选项卡面板
        subPage2TabBarPanel.add(subPage2Tab1Button);
        subPage2TabBarPanel.add(subPage2Tab2Button);
        subPage2TabBarPanel.add(subPage2Tab3Button);
        // 设置子选项卡面板在顶部
        subPage2TabBarPanel.setBorder(BorderFactory.createEmptyBorder(0, 0, 10, 0));
        subPage2TabBarPanel.setBackground(Color.WHITE);
        // 设置子选项卡内容面板的布局和背景颜色
        subPage2ContentPanel.setLayout(new BorderLayout());
        subPage2ContentPanel.setBackground(Color.LIGHT_GRAY);
        // 创建子选项卡按钮的事件监听器
        ActionListener subPageTabButtonListener = new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                // 根据点击的子选项卡按钮切换内容
                if (e.getSource() == subPage2Tab1Button) {
                    subPage2ContentPanel.removeAll();
                    //显示地图
                    //创建地图
                    MapView mapView = new MapView();



                } else if (e.getSource() == subPage2Tab2Button) {
                    subPage2ContentPanel.removeAll();

                    


                } else if (e.getSource() == subPage2Tab3Button) {
                    subPage2ContentPanel.removeAll();

                    
                }
                subPage2ContentPanel.revalidate();
                subPage2ContentPanel.repaint();
            }
        };
        // 给子选项卡按钮添加事件监听器
        subPage2Tab1Button.addActionListener(subPageTabButtonListener);
        subPage2Tab2Button.addActionListener(subPageTabButtonListener);
        subPage2Tab3Button.addActionListener(subPageTabButtonListener);
        // 将子选项卡面板和子选项卡内容面板添加到子页面中
        JPanel subPagePanel = new JPanel(new BorderLayout());
        subPagePanel.add(subPage2TabBarPanel, BorderLayout.NORTH);
        subPagePanel.add(subPage2ContentPanel, BorderLayout.CENTER);
        return subPagePanel;
    }

    // 刷新subPage1ContentPanel显示的内容
    private void refreshSubPage1ContentPanel(List<CenterPlace> places) {
        JPanel messagePanel = new JPanel();
        messagePanel.setBorder(BorderFactory.createEmptyBorder(30, 0, 0, 0));   
        StringBuilder message = new StringBuilder("Search Results:\n");
        for (CenterPlace centerPlace : places) {
            message.append(centerPlace.getName()).append("\n");
        }
        JLabel subPageLabel2 = new JLabel(message.toString());
        subPageLabel2.setHorizontalAlignment(SwingConstants.CENTER);
        messagePanel.add(subPageLabel2);  
        subPage1ContentPanel.add(messagePanel, BorderLayout.NORTH);
        frame.revalidate(); // 重新验证并刷新界面

    }
}

