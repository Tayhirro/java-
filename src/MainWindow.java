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

//-----------------�Ƽ�ϵͳ---------------------
    private RecommendationSystem recommendationSystem; // �Ƽ�ϵͳ
    private List<String> userInterests; // �û���Ȥ�б�
    private SearchSystem searchSystem; // ����ϵͳ
    private List<CenterPlace> tagRecommendations;   //�����Ƽ�


    private CenterPlaceDataLoader dataLoader; // ���ݼ�����
    private List<CenterPlace> searchResults;
    private JPanel subPage1ContentPanel;
    private JPanel subPage1TabBarPanel;
    private JButton subPage1Tab1Button;
    private JButton subPage1Tab2Button;
    private JButton subPage1Tab3Button;
    private HashSet<String> tagList;
    private JList<String> tagListJList;
//---------------------------------------------
//ǰʮ�Ƽ�
    private List<CenterPlace> top10CenterPlaces;

    /**
     * ��ʾǰʮ�Ƽ������ĵص㡣
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
//����
    /**
     * ��ʾ���������
     */
    private List<CenterPlace> showSearchResults(List<CenterPlace> searchResults, String query) {
       // String query = JOptionPane.showInputDialog(this, "�������ѯ�ؼ���"); // ��ȡ�û�����
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
//�����Ƽ�

    /**
     * ���ݱ�ǩ��ʾ�Ƽ������ĵص㡣
     */
    private List<CenterPlace> showRecommendationsByTags(List<CenterPlace> tagRecommendations, List<String> tags) {


        // String input = JOptionPane.showInputDialog(this, "�������ǩ���ö��ŷָ�"); // ��ȡ�û�����
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


    



//-----------------·�߹滮---------------------


    private JPanel subPage2ContentPanel;
    private JPanel subPage2TabBarPanel;
    private JButton subPage2Tab1Button;
    private JButton subPage2Tab2Button;
    private JButton subPage2Tab3Button;
    

//-----------------������ѯ---------------------
    
        private JPanel subPage3ContentPanel;
        private JPanel subPage3TabBarPanel;
        private JButton subPage3Tab1Button;
        private JButton subPage3Tab2Button;
        private JButton subPage3Tab3Button;



//-----------------��ѧ�ռ�---------------------







    public MainWindow(RecommendationSystem recommendationSystem, SearchSystem searchSystem, List<String> userInterests, CenterPlaceDataLoader dataLoader) {
        this.recommendationSystem = recommendationSystem;
        this.userInterests = userInterests;
        this.searchSystem = searchSystem;
        this.dataLoader = dataLoader;

        // ���ļ��ж�ȡ��ǩ�б�
        List<CenterPlace> centerPlaces = dataLoader.getCenterPlaces();
        tagList = new HashSet<>(new HashSet<>());


        for (CenterPlace centerPlace : centerPlaces) {
            tagList.addAll(centerPlace.getKeywords());
        }


        tagListJList = new JList<>(tagList.toArray(new String[0])); // ��ǩ�б�,��hashsetת��Ϊ����



        frame = new JFrame("��ѧ�Ƽ�ϵͳ");
        contentPanel = new JPanel();
        tabBarPanel = new JPanel(new GridLayout(1, 4));
        tab1Button = createTabButton("��ѧ�Ƽ�");
        tab2Button = createTabButton("·�߹滮");
        tab3Button = createTabButton("������ѯ");
        tab4Button = createTabButton("��ѧ�ռ�");

        // ���ѡ���ť��ѡ����
        tabBarPanel.add(tab1Button);
        tabBarPanel.add(tab2Button);
        tabBarPanel.add(tab3Button);
        tabBarPanel.add(tab4Button);

        // ����ѡ�����ڵײ�
        tabBarPanel.setBorder(BorderFactory.createEmptyBorder(10, 0, 0, 0));
        tabBarPanel.setBackground(Color.WHITE);

        // �����������Ĳ��ֺͱ�����ɫ
        contentPanel.setLayout(new BorderLayout());
        contentPanel.setBackground(Color.LIGHT_GRAY);
        //-----------------------------------------------------------------------------------------------------------------------------------------



        // ����ѡ���ť���¼�������
        ActionListener tabButtonListener = new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                // ���ݵ����ѡ���ť�л�����
                if (e.getSource() == tab1Button) {
                    contentPanel.removeAll();
                  //  contentPanel.add(label1, BorderLayout.CENTER);
                    contentPanel.add(createSubPage1ContentPanel(), BorderLayout.CENTER);
                



                } else if (e.getSource() == tab2Button) {
                    contentPanel.removeAll();
                    contentPanel.add(createSubPage2ContentPanel(), BorderLayout.CENTER);










                } else if (e.getSource() == tab3Button) {
                    contentPanel.removeAll();
                    JLabel label3 = new JLabel("����ѡ�3������");
                    label3.setHorizontalAlignment(SwingConstants.CENTER);
                    contentPanel.add(label3, BorderLayout.CENTER);
                } else if (e.getSource() == tab4Button) {
                    contentPanel.removeAll();
                    JLabel label4 = new JLabel("����ѡ�4������");
                    label4.setHorizontalAlignment(SwingConstants.CENTER);
                    contentPanel.add(label4, BorderLayout.CENTER);
                }

                contentPanel.revalidate();
                contentPanel.repaint();
            }
        };
        // ��ѡ���ť����¼�������
        tab1Button.addActionListener(tabButtonListener);
        tab2Button.addActionListener(tabButtonListener);
        tab3Button.addActionListener(tabButtonListener);
        tab4Button.addActionListener(tabButtonListener);

        // ģ������һ��ѡ���ť
        tab1Button.doClick();
        // ��ѡ��������������ӵ�������
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


    // ������ҳ��1���������
    private JPanel createSubPage1ContentPanel() {
        subPage1ContentPanel = new JPanel();
        subPage1TabBarPanel = new JPanel(new GridLayout(1, 3));
        subPage1Tab1Button = createTabButton("ǰʮ�Ƽ�");
        subPage1Tab2Button = createTabButton("����/ѧУ��ѯ");
        subPage1Tab3Button= createTabButton("�����Ƽ�");


        // �����ѡ���ť����ѡ����
        subPage1TabBarPanel.add(subPage1Tab1Button);
        subPage1TabBarPanel.add(subPage1Tab2Button);
        subPage1TabBarPanel.add(subPage1Tab3Button);



        // ������ѡ�����ڶ���
        subPage1TabBarPanel.setBorder(BorderFactory.createEmptyBorder(0, 0, 10, 0));
        subPage1TabBarPanel.setBackground(Color.WHITE);

        // ������ѡ��������Ĳ��ֺͱ�����ɫ
        subPage1ContentPanel.setLayout(new BorderLayout());
        subPage1ContentPanel.setBackground(Color.LIGHT_GRAY);


        
        // ������ѡ���ť���¼�������
        ActionListener subPageTabButtonListener = new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                // ���ݵ������ѡ���ť�л�����
                if (e.getSource() == subPage1Tab1Button) {
                    subPage1ContentPanel.removeAll();   
                    //��ʾǰʮ�Ƽ�
                    top10CenterPlaces = showTop10Recommendations(top10CenterPlaces);
                    //��ʾ��subPage1ContentPanel��
                    //JLabel subPageLabel1���ڶ���
                    

                    //ʹ�ô�ֱ����������ʾǰʮ�Ƽ������ĵص�
                    Box centerPlacesBox = Box.createVerticalBox();
                    for (CenterPlace centerPlace : top10CenterPlaces) {
                        // ������ǩ����ӵ���ֱ����������
                        JLabel centerPlaceLabel = new JLabel(centerPlace.getName());
                        centerPlaceLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
                        centerPlacesBox.add(centerPlaceLabel);
                    }
                    // ����������岢��ӵ��������
                    JPanel centerPlacesPanel = new JPanel();
                    centerPlacesPanel.setLayout(new BorderLayout());
                    centerPlacesPanel.add(centerPlacesBox, BorderLayout.CENTER);
                    //��ʾ��subPage1ContentPanel��
                    StringBuilder message = new StringBuilder("Top 10 Recommended CenterPlaces:\n");
                    JLabel subPageLabel1 = new JLabel(message.toString());
                    //JLabel subPageLabel1���ڶ���
                    subPageLabel1.setHorizontalAlignment(SwingConstants.CENTER);
                    subPage1ContentPanel.removeAll();
                    subPage1ContentPanel.setLayout(new BorderLayout());
                    subPage1ContentPanel.add(subPageLabel1, BorderLayout.NORTH);
                    subPage1ContentPanel.add(centerPlacesPanel, BorderLayout.CENTER);
        


                } else if (e.getSource() == subPage1Tab2Button) {
                    subPage1ContentPanel.removeAll();

                    JTextField searchField = new JTextField();
                    //��ʾ��������ص㡱
                    searchField.setText("������ص�");
                    searchField.addActionListener(new ActionListener() {
                        @Override
                        public void actionPerformed(ActionEvent e) {
                            String query = searchField.getText();
                            searchResults = showSearchResults(searchResults, query);
                    
                            //��ʾ��subPage1ContentPanel��
                            StringBuilder message = new StringBuilder("Search Results:\n");
                            for (CenterPlace centerPlace : searchResults) {
                                message.append(centerPlace.getName()).append("\n");
                            }
                            JLabel subPageLabel2 = new JLabel(message.toString());
                            subPageLabel2.setHorizontalAlignment(SwingConstants.CENTER);
                            //JLabel subPageLabel2���ڶ���
                            subPage1ContentPanel.add(subPageLabel2, BorderLayout.NORTH);
                            refreshSubPage1ContentPanel(searchResults);
                        }
                    });
                    subPage1ContentPanel.add(searchField, BorderLayout.NORTH);
                    searchField.requestFocusInWindow(); // �����ı����ý���
                    frame.revalidate(); // ������֤��ˢ�½���
                    //��ʾ��subPage1ContentPanel��

                } else if (e.getSource() == subPage1Tab3Button) {
                    subPage1ContentPanel.removeAll();
                    //��ʾ�����Ƽ�
                    //�ұ���ʾ�Ƽ������ĵص�
                    StringBuilder message = new StringBuilder("Recommendations by Tags:\n");
                    JLabel subPageLabel3 = new JLabel(message.toString());
                    subPageLabel3.setHorizontalAlignment(SwingConstants.CENTER);
                    subPage1ContentPanel.add(subPageLabel3, BorderLayout.NORTH);
                    JScrollPane scrollPane = new JScrollPane(tagListJList); 
                    //��ʾ��subPage3ContentPanel��
                    subPage1ContentPanel.setLayout(new BorderLayout());
                    subPage1ContentPanel.add(scrollPane, BorderLayout.WEST);
                }

                subPage1ContentPanel.revalidate();
                subPage1ContentPanel.repaint();
            }
        };

        // ����ѡ���ť����¼�������
 
        subPage1Tab1Button.addActionListener(subPageTabButtonListener);
        subPage1Tab2Button.addActionListener(subPageTabButtonListener);
        subPage1Tab3Button.addActionListener(subPageTabButtonListener);

        //tagListJList���¼�������
        tagListJList.addListSelectionListener(new ListSelectionListener() {
            @Override
            public void valueChanged(ListSelectionEvent e) {
                if (!e.getValueIsAdjusting()) {
                    String selectedTag = tagListJList.getSelectedValue();
                    if (selectedTag != null) {
                        // ����ѡ�еı�ǩ��ʾ�Ƽ������ĵص�
                        tagRecommendations = showRecommendationsByTags(tagRecommendations, Arrays.asList(selectedTag));
                        //��ʾ��subPage1ContentPanel��
                        //ʹ�ô�ֱ����������ʾ�Ƽ������ĵص�
                        Box centerPlacesBox = Box.createVerticalBox();
                        for (CenterPlace centerPlace : tagRecommendations) {
                            // ������ǩ����ӵ���ֱ����������
                            JLabel centerPlaceLabel = new JLabel(centerPlace.getName());
                            centerPlaceLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
                            centerPlacesBox.add(centerPlaceLabel);
                        }
                        subPage1ContentPanel.removeAll();
                        // ����������岢��ӵ��������
                        JPanel centerPlacesPanel = new JPanel();
                        centerPlacesPanel.setLayout(new BorderLayout());
                        centerPlacesPanel.add(centerPlacesBox, BorderLayout.CENTER);
                        //��ʾ��subPage1ContentPanel��
                        subPage1ContentPanel.setLayout(new BorderLayout());
                        subPage1ContentPanel.add(centerPlacesPanel, BorderLayout.CENTER);
                        frame.revalidate(); // ������֤��ˢ�½���
                    }

                }
            }
        });
        
        // ����ѡ�������ѡ����������ӵ���ҳ����
        JPanel subPagePanel = new JPanel(new BorderLayout());
        subPagePanel.add(subPage1TabBarPanel, BorderLayout.NORTH);
        subPagePanel.add(subPage1ContentPanel, BorderLayout.CENTER);

        return subPagePanel;
    }
    // ������ҳ��2���������
    private JPanel createSubPage2ContentPanel() {
        subPage2ContentPanel = new JPanel();
        subPage2TabBarPanel = new JPanel(new GridLayout(1, 3));
        subPage2Tab1Button = createTabButton("�鿴��ͼ");
        subPage2Tab2Button = createTabButton("����·�߹滮");
        subPage2Tab3Button= createTabButton("���·�߹滮");
        
        // �����ѡ���ť����ѡ����
        subPage2TabBarPanel.add(subPage2Tab1Button);
        subPage2TabBarPanel.add(subPage2Tab2Button);
        subPage2TabBarPanel.add(subPage2Tab3Button);
        // ������ѡ�����ڶ���
        subPage2TabBarPanel.setBorder(BorderFactory.createEmptyBorder(0, 0, 10, 0));
        subPage2TabBarPanel.setBackground(Color.WHITE);
        // ������ѡ��������Ĳ��ֺͱ�����ɫ
        subPage2ContentPanel.setLayout(new BorderLayout());
        subPage2ContentPanel.setBackground(Color.LIGHT_GRAY);
        // ������ѡ���ť���¼�������
        ActionListener subPageTabButtonListener = new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                // ���ݵ������ѡ���ť�л�����
                if (e.getSource() == subPage2Tab1Button) {
                    subPage2ContentPanel.removeAll();
                    //��ʾ��ͼ
                    //������ͼ
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
        // ����ѡ���ť����¼�������
        subPage2Tab1Button.addActionListener(subPageTabButtonListener);
        subPage2Tab2Button.addActionListener(subPageTabButtonListener);
        subPage2Tab3Button.addActionListener(subPageTabButtonListener);
        // ����ѡ�������ѡ����������ӵ���ҳ����
        JPanel subPagePanel = new JPanel(new BorderLayout());
        subPagePanel.add(subPage2TabBarPanel, BorderLayout.NORTH);
        subPagePanel.add(subPage2ContentPanel, BorderLayout.CENTER);
        return subPagePanel;
    }

    // ˢ��subPage1ContentPanel��ʾ������
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
        frame.revalidate(); // ������֤��ˢ�½���

    }
}

