import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

// 游学推荐页面
public class RecommendationPanel extends JPanel {

    private final User currUser;// 当前用户
    private final UserManagement userManagement;// 用户管理对象
    private final SpotManagement spotManagement;// 景点管理对象


    private JTextField searchField;// 搜索框
    private JComboBox<String> sortComboBox;// 排序下拉框
    private JPanel preferencePanel;// 偏好设置面板
    private JPanel schoolPanel;// 学校展示面板
    private JPanel attractionPanel;// 景点展示面板
    private JScrollPane schoolScrollPane;// 学校滚动面板
    private JScrollPane attractionScrollPane;// 景点滚动面板
    private JPanel leftPanel;// 左侧面板
    private RecommendationSystem recommendationSystem;


    public RecommendationPanel(User currUser, UserManagement userManagement, SpotManagement spotManagement) {
        this.currUser = currUser;
        this.userManagement = userManagement;
        this.spotManagement = spotManagement;

        this.recommendationSystem = new RecommendationSystem(currUser, userManagement, spotManagement);

        setLayout(new BorderLayout());
        add(new JLabel("游学推荐页面"), BorderLayout.CENTER);

        leftPanel = getLeftPanel();

        schoolScrollPane = getMiddlePanel();

        attractionScrollPane = getRightPanel();

        add(leftPanel, BorderLayout.WEST);
        add(schoolScrollPane, BorderLayout.CENTER);
        add(attractionScrollPane, BorderLayout.EAST);

        setSpots(recommendationSystem.getInitialSchools(), "school");
        setSpots(recommendationSystem.getInitialAttractions(), "attraction");

    }


    public void setSpots(List<Spot> spots, String type) {
        if (type.equals("school")) {
            schoolPanel.removeAll();
            for (Spot spot : spots) {
                JPanel schoolItemPanel = new JPanel();
                BoxLayout boxLayout = new BoxLayout(schoolItemPanel, BoxLayout.Y_AXIS);
                schoolItemPanel.setLayout(boxLayout);

                ImageIcon originalIcon = new ImageIcon(spot.getImages()[0]);
                Image originalImage = originalIcon.getImage();
                Image scaledImage = originalImage.getScaledInstance(200, 200, Image.SCALE_SMOOTH);
                JLabel schoolImage = new JLabel(new ImageIcon(scaledImage));
                schoolImage.setAlignmentX(Component.CENTER_ALIGNMENT); // 设置图片居中

                schoolImage.addMouseListener(new MouseAdapter() {
                    @Override
                    public void mouseEntered(MouseEvent e) {
                        updateAttractionsForSchool(spot);
                    }
                    @Override
                    public void mouseExited(MouseEvent e) {
                        restoreOriginalState();
                    }
                });

                JLabel schoolName = new JLabel(spot.getName());
                schoolName.setHorizontalAlignment(JLabel.CENTER); // 设置文本居中
                schoolName.setAlignmentX(Component.CENTER_ALIGNMENT); // 设置文本居中

                JLabel schoolTags = new JLabel(String.join(", ", spot.getTags()));
                schoolTags.setHorizontalAlignment(JLabel.CENTER); // 设置文本居中
                schoolTags.setAlignmentX(Component.CENTER_ALIGNMENT); // 设置文本居中

                schoolItemPanel.add(schoolImage);
                schoolItemPanel.add(Box.createVerticalStrut(5)); // 创建一个垂直间距，高度为5
                schoolItemPanel.add(schoolName);
                schoolItemPanel.add(Box.createVerticalStrut(5)); // 创建一个垂直间距，高度为5
                schoolItemPanel.add(schoolTags);

                schoolPanel.add(schoolItemPanel);
            }
        } else if (type.equals("attraction")) {
            attractionPanel.removeAll();
            for (Spot spot : spots) {
                JPanel attractionItemPanel = new JPanel();
                BoxLayout boxLayout = new BoxLayout(attractionItemPanel, BoxLayout.Y_AXIS);
                attractionItemPanel.setLayout(boxLayout);

                ImageIcon originalIcon = new ImageIcon(spot.getImages()[0]);
                Image originalImage = originalIcon.getImage();
                Image scaledImage = originalImage.getScaledInstance(200, 200, Image.SCALE_SMOOTH);
                JLabel attractionImage = new JLabel(new ImageIcon(scaledImage));
                attractionImage.setAlignmentX(Component.CENTER_ALIGNMENT); // 设置图片居中

                JLabel attractionName = new JLabel(spot.getName());
                attractionName.setHorizontalAlignment(JLabel.CENTER); // 设置文本居中
                attractionName.setAlignmentX(Component.CENTER_ALIGNMENT); // 设置文本居中

                JLabel attractionTags = new JLabel(String.join(", ", spot.getTags()));
                attractionTags.setHorizontalAlignment(JLabel.CENTER); // 设置文本居中
                attractionTags.setAlignmentX(Component.CENTER_ALIGNMENT); // 设置文本居中

                attractionItemPanel.add(attractionImage);
                attractionItemPanel.add(Box.createVerticalStrut(5)); // 创建一个垂直间距，高度为5
                attractionItemPanel.add(attractionName);
                attractionItemPanel.add(Box.createVerticalStrut(5)); // 创建一个垂直间距，高度为5
                attractionItemPanel.add(attractionTags);

                attractionPanel.add(attractionItemPanel);
            }
        }
        revalidate();
        repaint();
    }

    public void setSpots1(List<Spot> spots, String type) {
        if (type.equals("school")) {
            for (Spot spot : spots) {
                JPanel schoolItemPanel = new JPanel();
                BoxLayout boxLayout = new BoxLayout(schoolItemPanel, BoxLayout.Y_AXIS);
                schoolItemPanel.setLayout(boxLayout);

                ImageIcon originalIcon = new ImageIcon(spot.getImages()[0]);
                Image originalImage = originalIcon.getImage();
                Image scaledImage = originalImage.getScaledInstance(200, 200, Image.SCALE_SMOOTH);
                JLabel schoolImage = new JLabel(new ImageIcon(scaledImage));
                schoolImage.setAlignmentX(Component.CENTER_ALIGNMENT); // 设置图片居中

                JLabel schoolName = new JLabel(spot.getName());
                schoolName.setHorizontalAlignment(JLabel.CENTER); // 设置文本居中
                schoolName.setAlignmentX(Component.CENTER_ALIGNMENT); // 设置文本居中

                JLabel schoolTags = new JLabel(String.join(", ", spot.getTags()));
                schoolTags.setHorizontalAlignment(JLabel.CENTER); // 设置文本居中
                schoolTags.setAlignmentX(Component.CENTER_ALIGNMENT); // 设置文本居中

                schoolItemPanel.add(schoolImage);
                schoolItemPanel.add(Box.createVerticalStrut(5)); // 创建一个垂直间距，高度为5
                schoolItemPanel.add(schoolName);
                schoolItemPanel.add(Box.createVerticalStrut(5)); // 创建一个垂直间距，高度为5
                schoolItemPanel.add(schoolTags);

                schoolPanel.add(schoolItemPanel);
            }
        } else if (type.equals("attraction")) {
            for (Spot spot : spots) {
                JPanel attractionItemPanel = new JPanel();
                BoxLayout boxLayout = new BoxLayout(attractionItemPanel, BoxLayout.Y_AXIS);
                attractionItemPanel.setLayout(boxLayout);

                ImageIcon originalIcon = new ImageIcon(spot.getImages()[0]);
                Image originalImage = originalIcon.getImage();
                Image scaledImage = originalImage.getScaledInstance(200, 200, Image.SCALE_SMOOTH);
                JLabel attractionImage = new JLabel(new ImageIcon(scaledImage));
                attractionImage.setAlignmentX(Component.CENTER_ALIGNMENT); // 设置图片居中

                JLabel attractionName = new JLabel(spot.getName());
                attractionName.setHorizontalAlignment(JLabel.CENTER); // 设置文本居中
                attractionName.setAlignmentX(Component.CENTER_ALIGNMENT); // 设置文本居中

                JLabel attractionTags = new JLabel(String.join(", ", spot.getTags()));
                attractionTags.setHorizontalAlignment(JLabel.CENTER); // 设置文本居中
                attractionTags.setAlignmentX(Component.CENTER_ALIGNMENT); // 设置文本居中

                attractionItemPanel.add(attractionImage);
                attractionItemPanel.add(Box.createVerticalStrut(5)); // 创建一个垂直间距，高度为5
                attractionItemPanel.add(attractionName);
                attractionItemPanel.add(Box.createVerticalStrut(5)); // 创建一个垂直间距，高度为5
                attractionItemPanel.add(attractionTags);

                attractionPanel.add(attractionItemPanel);
            }
        }
        revalidate();
        repaint();
    }

    private void performSearch() {
        String query = searchField.getText();
        List<Spot> filteredSpots = recommendationSystem.searchSpots(query);
        List<Spot> schools = filteredSpots.stream().filter(spot -> spot.getType().equals("school")).collect(Collectors.toList());
        List<Spot> attractions = filteredSpots.stream().filter(spot -> spot.getType().equals("attraction")).collect(Collectors.toList());

        schoolPanel.removeAll();
        attractionPanel.removeAll();
        if(schools.size() == 0 && attractions.size() == 0) {
            JOptionPane.showMessageDialog(this, "No results found", "Search Results", JOptionPane.INFORMATION_MESSAGE);
            return;
        }
        if(schools.size() != 0) {
            setSpots1(schools, "school");
            schoolScrollPane.setVisible(true);
        }
        else{
            schoolScrollPane.setVisible(false);
        }
        if(attractions.size() != 0) {
            setSpots1(attractions, "attraction");
            attractionScrollPane.setVisible(true);
        }
        else{
            attractionScrollPane.setVisible(false);
        }
    }

    private void sortResults() {
        String sortCriteria = (String) sortComboBox.getSelectedItem();
        List<Spot> spots = recommendationSystem.getAllSpots();
        List<Spot> sortedSpots = recommendationSystem.sortSpots(spots, sortCriteria);

        List<Spot> schools = sortedSpots.stream().filter(spot -> spot.getType().equals("school")).collect(Collectors.toList());
        List<Spot> attractions = sortedSpots.stream().filter(spot -> spot.getType().equals("attraction")).collect(Collectors.toList());

        schoolPanel.removeAll();
        attractionPanel.removeAll();
        if(schools.size() == 0 && attractions.size() == 0) {
            JOptionPane.showMessageDialog(this, "No results found", "Search Results", JOptionPane.INFORMATION_MESSAGE);
            return;
        }
        if(schools.size() != 0) {
            setSpots1(schools, "school");
            schoolScrollPane.setVisible(true);
        }
        else{
            schoolScrollPane.setVisible(false);
        }
        if(attractions.size() != 0) {
            setSpots1(attractions, "attraction");
            attractionScrollPane.setVisible(true);
        }
        else{
            attractionScrollPane.setVisible(false);
        }
    }


    private void updateAttractionsForSchool(Spot school) {
        // 更新右部景点展示逻辑
        Spot[] relatedSpots = school.getRelatedSpots();
        if (relatedSpots != null) {
            attractionPanel.removeAll();
            for (Spot spot : relatedSpots) {
                JPanel attractionItemPanel = new JPanel();
                BoxLayout boxLayout = new BoxLayout(attractionItemPanel, BoxLayout.Y_AXIS);
                attractionItemPanel.setLayout(boxLayout);

                ImageIcon originalIcon = new ImageIcon(spot.getImages()[0]);
                Image originalImage = originalIcon.getImage();
                Image scaledImage = originalImage.getScaledInstance(200, 200, Image.SCALE_SMOOTH);
                JLabel attractionImage = new JLabel(new ImageIcon(scaledImage));
                attractionImage.setAlignmentX(Component.CENTER_ALIGNMENT); // 设置图片居中

                JLabel attractionName = new JLabel(spot.getName());
                attractionName.setHorizontalAlignment(JLabel.CENTER); // 设置文本居中
                attractionName.setAlignmentX(Component.CENTER_ALIGNMENT); // 设置文本居中

                JLabel attractionTags = new JLabel(String.join(", ", spot.getTags()));
                attractionTags.setHorizontalAlignment(JLabel.CENTER); // 设置文本居中
                attractionTags.setAlignmentX(Component.CENTER_ALIGNMENT); // 设置文本居中

                attractionItemPanel.add(attractionImage);
                attractionItemPanel.add(Box.createVerticalStrut(5)); // 创建一个垂直间距，高度为5
                attractionItemPanel.add(attractionName);
                attractionItemPanel.add(Box.createVerticalStrut(5)); // 创建一个垂直间距，高度为5
                attractionItemPanel.add(attractionTags);

                attractionPanel.add(attractionItemPanel);
            }
            revalidate();
            repaint();
        }
    }
    private void restoreOriginalState() {
        // 恢复右部景点展示逻辑
        List<Spot> attractions = recommendationSystem.getInitialAttractions();
        setSpots(attractions, "attraction");
    }


    private JPanel getLeftPanel() {
        // 左部：搜索和偏好设置
        leftPanel = new JPanel();
        leftPanel.setLayout(new BorderLayout());

        JPanel searchPanel = new JPanel();
        searchPanel.setLayout(new FlowLayout());
        searchField = new JTextField(15);
        JButton searchButton = new JButton("Search");
        searchButton.addActionListener(e -> performSearch());
        searchPanel.add(searchField);
        searchPanel.add(searchButton);

        JPanel sortPanel = new JPanel();
        sortPanel.setLayout(new FlowLayout());
        sortComboBox = new JComboBox<>(new String[]{"Sort by Popularity", "Sort by Rating"});
        sortComboBox.addActionListener(e -> sortResults());
        sortPanel.add(sortComboBox);

        preferencePanel = new JPanel();
        preferencePanel.setLayout(new GridLayout(0, 1));
        JLabel preferenceLabel = new JLabel("Preferences:");
        preferencePanel.add(preferenceLabel);
        JCheckBox preference1 = new JCheckBox("名校");
        JCheckBox preference2 = new JCheckBox("景点");
        JCheckBox preference3 = new JCheckBox("古建筑");
        JCheckBox preference4 = new JCheckBox("211");
        preferencePanel.add(preference1);
        preferencePanel.add(preference2);
        preferencePanel.add(preference3);
        preferencePanel.add(preference4);

        JButton recommendButton = new JButton("推荐");
        recommendButton.addActionListener(e -> {
            List<String> selectedPreferences = new ArrayList<>();
            if (preference1.isSelected()) {
                selectedPreferences.add(preference1.getText());
            }
            if (preference2.isSelected()) {
                selectedPreferences.add(preference2.getText());
            }
            if (preference3.isSelected()) {
                selectedPreferences.add(preference3.getText());
            }
            if (preference4.isSelected()) {
                selectedPreferences.add(preference4.getText());
            }

            List<Spot> recommendedSpots = recommendationSystem.filterAndSortSpots(selectedPreferences);

            List<Spot> schools = recommendedSpots.stream().filter(spot -> spot.getType().equals("school")).collect(Collectors.toList());
            List<Spot> attractions = recommendedSpots.stream().filter(spot -> spot.getType().equals("attraction")).collect(Collectors.toList());

            schoolPanel.removeAll();
            attractionPanel.removeAll();
            if(schools.size() == 0 && attractions.size() == 0) {
                JOptionPane.showMessageDialog(this, "No results found", "Search Results", JOptionPane.INFORMATION_MESSAGE);
                return;
            }
            if(schools.size() != 0) {
                setSpots1(schools, "school");
                schoolScrollPane.setVisible(true);
            }
            else{
                schoolScrollPane.setVisible(false);
            }
            if(attractions.size() != 0) {
                setSpots1(attractions, "attraction");
                attractionScrollPane.setVisible(true);
            }
            else{
                attractionScrollPane.setVisible(false);
            }
        });
        preferencePanel.add(recommendButton);

        leftPanel.add(searchPanel, BorderLayout.NORTH);
        leftPanel.add(sortPanel, BorderLayout.CENTER);
        leftPanel.add(preferencePanel, BorderLayout.SOUTH);
        return leftPanel;
    }


    private JScrollPane getMiddlePanel() {
        // 中部：学校展示
        schoolPanel = new JPanel();
        schoolPanel.setLayout(new GridLayout(0, 1));
        schoolScrollPane = new JScrollPane(schoolPanel);
        return schoolScrollPane;
    }


    private JScrollPane getRightPanel() {
        // 右部：景点展示
        attractionPanel = new JPanel();
        attractionPanel.setLayout(new GridLayout(0, 1));
        attractionScrollPane = new JScrollPane(attractionPanel);
        return attractionScrollPane;
    }
}

