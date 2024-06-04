
import java.awt.*;
import java.awt.event.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import javax.swing.*;

// 游学推荐页面
public class RecommendationPanel extends JPanel {

    private JTextField searchField;// 搜索框
    private JComboBox<String> sortComboBox;// 排序下拉框
    private JPanel preferencePanel;// 偏好设置面板
    private JPanel schoolPanel;// 学校展示面板
    private JPanel attractionPanel;// 景点展示面板
    private Map<Spot, JPanel> schoolItemPanelDy;// 学校动态展示面板
    private Map<Spot, JPanel> schoolItemPanelSt;// 学校静态展示面板
    private Map<Spot, JPanel> attractionItemPanel;// 景点展示面板
    private JScrollPane schoolScrollPane;// 学校滚动面板
    private JScrollPane attractionScrollPane;// 景点滚动面板
    private JPanel leftPanel;// 左侧面板
    private RecommendationSystem recommendationSystem;

    public RecommendationPanel(SpotManagement spotManagement) {

        this.recommendationSystem = new RecommendationSystem(spotManagement);

        schoolItemPanelDy = new HashMap<>();
        schoolItemPanelSt = new HashMap<>();
        attractionItemPanel = new HashMap<>();

        for (Spot spot : recommendationSystem.getInitialSchools()) {
            JPanel panel = createItemPanel(spot, true);
            schoolItemPanelDy.put(spot, panel);
            panel = createItemPanel(spot, false);
            schoolItemPanelSt.put(spot, panel);
        }
        for (Spot spot : recommendationSystem.getInitialAttractions()) {
            JPanel panel = createItemPanel(spot, false);
            attractionItemPanel.put(spot, panel);
        }

//        setLayout(new BorderLayout());
        setLayout(new GridLayout(1, 3));

        leftPanel = getLeftPanel();

        schoolScrollPane = getMiddlePanel();

        attractionScrollPane = getRightPanel();

        add(leftPanel, BorderLayout.WEST);
        add(schoolScrollPane, BorderLayout.CENTER);
        add(attractionScrollPane, BorderLayout.EAST);

        displaySpots(recommendationSystem.getInitialSchools(), "school", schoolItemPanelDy);
        displaySpots(recommendationSystem.getInitialAttractions(), "attraction", schoolItemPanelDy);

    }

    private JPanel createItemPanel(Spot spot, boolean type) {
        JPanel ItemPanel = new JPanel();
        BoxLayout boxLayout = new BoxLayout(ItemPanel, BoxLayout.Y_AXIS);
        ItemPanel.setLayout(boxLayout);

        ImageIcon originalIcon = new ImageIcon(spot.getImages()[0]);
        Image originalImage = originalIcon.getImage();
        Image scaledImage = originalImage.getScaledInstance(200, 200, Image.SCALE_SMOOTH);
        JLabel Image = new JLabel(new ImageIcon(scaledImage));
        Image.setAlignmentX(Component.CENTER_ALIGNMENT); // 设置图片居中

        if (type) {
            Image.addMouseListener(new MouseAdapter() {
                @Override
                public void mouseEntered(MouseEvent e) {
                    updateAttractionsForSchool(spot);
                }

                @Override
                public void mouseExited(MouseEvent e) {
                    restoreOriginalState();
                }

                @Override
                public void mouseClicked(MouseEvent e) {
                    showSpotDetails(spot);
                }
            });
        }

        JLabel Name = new JLabel(spot.getName());
        Name.setHorizontalAlignment(JLabel.CENTER); // 设置文本居中
        Name.setAlignmentX(Component.CENTER_ALIGNMENT); // 设置文本居中

        JLabel Tags = new JLabel(String.join(", ", spot.getTags()));
        Tags.setHorizontalAlignment(JLabel.CENTER); // 设置文本居中
        Tags.setAlignmentX(Component.CENTER_ALIGNMENT); // 设置文本居中

        ItemPanel.add(Image);
        ItemPanel.add(Box.createVerticalStrut(5)); // 创建一个垂直间距，高度为5
        ItemPanel.add(Name);
        ItemPanel.add(Box.createVerticalStrut(5)); // 创建一个垂直间距，高度为5
        ItemPanel.add(Tags);

        return ItemPanel;
    }

    private void showSpotDetails(Spot spot) {
        JFrame detailsFrame = new JFrame("Spot Details");
        detailsFrame.setSize(400, 600);
        detailsFrame.setLayout(new BorderLayout());

        JLabel nameLabel = new JLabel(spot.getName(), JLabel.CENTER);
        detailsFrame.add(nameLabel, BorderLayout.NORTH);

        JPanel imagesPanel = new JPanel();
        imagesPanel.setLayout(new BoxLayout(imagesPanel, BoxLayout.Y_AXIS));
        for (String imagePath : spot.getImages()) {
            ImageIcon originalIcon = new ImageIcon(imagePath);
            Image originalImage = originalIcon.getImage();
            Image scaledImage = originalImage.getScaledInstance(200, 200, Image.SCALE_SMOOTH);
            JLabel imageLabel = new JLabel(new ImageIcon(scaledImage));
            imagesPanel.add(imageLabel);
        }
        JScrollPane imagesScrollPane = new JScrollPane(imagesPanel);
        detailsFrame.add(imagesScrollPane, BorderLayout.CENTER);

        JTextArea descriptionArea = new JTextArea(spot.getDescription());
        descriptionArea.setLineWrap(true);
        descriptionArea.setWrapStyleWord(true);
        descriptionArea.setEditable(false);
        JScrollPane descriptionScrollPane = new JScrollPane(descriptionArea);
        detailsFrame.add(descriptionScrollPane, BorderLayout.SOUTH);

        detailsFrame.setVisible(true);
    }

    private void displaySpots(List<Spot> spots, String type, Map<Spot, JPanel> schoolItemPanel) {
        if (type.equals("school")) {
            schoolPanel.removeAll();
            for (Spot spot : spots) {
                JPanel ItemPanel = schoolItemPanel.get(spot);
                if (ItemPanel != null) {
                    schoolPanel.add(ItemPanel);
                }
            }
        } else if (type.equals("attraction")) {
            attractionPanel.removeAll();
            for (Spot spot : spots) {
                JPanel attractionItemPanels = attractionItemPanel.get(spot);
                if (attractionItemPanel != null) {
                    attractionPanel.add(attractionItemPanels);
                }
            }
        }
        revalidate();
        repaint();
    }

    private void performSearch() {
        String query = searchField.getText();
        List<Spot> filteredSpots = recommendationSystem.searchSpots(query);
        display(filteredSpots, false);
    }

    private void display(List<Spot> filteredSpots, boolean type) {
        List<Spot> schools = filteredSpots.stream().filter(spot -> spot.getType().equals("school")).collect(Collectors.toList());
        List<Spot> attractions = filteredSpots.stream().filter(spot -> spot.getType().equals("attraction")).collect(Collectors.toList());

        schoolPanel.removeAll();
        attractionPanel.removeAll();
        if (schools.isEmpty() && attractions.isEmpty()) {
            JOptionPane.showMessageDialog(this, "No results found", "Search Results", JOptionPane.INFORMATION_MESSAGE);
            return;
        }
        if (!schools.isEmpty() && !attractions.isEmpty()) {
            if (type) {
                displaySpots(schools, "school", schoolItemPanelDy);
                remove(schoolScrollPane);
                if (schoolScrollPane.getParent() == null) {
                    add(schoolScrollPane, BorderLayout.CENTER);
                }
            } else {
                displaySpots(schools, "school", schoolItemPanelSt);
                remove(schoolScrollPane);
                if (schoolScrollPane.getParent() == null) {
                    add(schoolScrollPane, BorderLayout.CENTER);
                }
            }
            displaySpots(attractions, "attraction", attractionItemPanel);
            remove(attractionScrollPane);
            if (attractionScrollPane.getParent() == null) {
                add(attractionScrollPane, BorderLayout.EAST);
            }
        } else if (schools.isEmpty()) {
            displaySpots(attractions, "attraction", attractionItemPanel);
            remove(schoolScrollPane);
            remove(attractionScrollPane);
            if (attractionScrollPane.getParent() == null) {
                add(attractionScrollPane, BorderLayout.CENTER);
            }
        } else {
            if (type) {
                displaySpots(schools, "school", schoolItemPanelDy);
                remove(attractionScrollPane);
                remove(schoolScrollPane);
                if (schoolScrollPane.getParent() == null) {
                    add(schoolScrollPane, BorderLayout.CENTER);
                }
            } else {
                displaySpots(schools, "school", schoolItemPanelSt);
                remove(schoolScrollPane);
                remove(attractionScrollPane);
                if (schoolScrollPane.getParent() == null) {
                    add(schoolScrollPane, BorderLayout.CENTER);
                }
            }
        }

        revalidate();
        repaint();
    }

    private void sortResults() {
        String sortCriteria = (String) sortComboBox.getSelectedItem();
        List<Spot> spots = recommendationSystem.getAllSpots();
        List<Spot> sortedSpots = recommendationSystem.sortSpots(spots, sortCriteria);

        display(sortedSpots, false);
    }

    private void updateAttractionsForSchool(Spot school) {
        // 更新右部景点展示逻辑
        Spot[] relatedSpots = school.getRelatedSpots();
        if (relatedSpots != null) {
            attractionPanel.removeAll();
            for (Spot spot : relatedSpots) {
                JPanel panel;
                if (spot.getType().equals("school")) {
                    panel = schoolItemPanelSt.get(spot);
                } else {
                    panel = attractionItemPanel.get(spot);
                }
                if (panel != null) {
                    attractionPanel.add(panel);
                }
            }
            revalidate();
            repaint();
        }
    }

    private void restoreOriginalState() {
        // 恢复右部景点展示逻辑
        schoolScrollPane.setVisible(true);
        attractionScrollPane.setVisible(true);
        display(recommendationSystem.getAllSpots(), true);
        setLayout(new GridLayout(1, 3));
    }

    private JPanel getLeftPanel() {
        // 左部：搜索和偏好设置
        leftPanel = new JPanel();
        leftPanel.setLayout(new BorderLayout());

        JPanel searchPanel = new JPanel();
        searchPanel.setLayout(new BoxLayout(searchPanel, BoxLayout.Y_AXIS));

        JPanel fieldPanel = new JPanel();
        fieldPanel.setLayout(new FlowLayout());
        searchField = new JTextField(15);
        fieldPanel.add(searchField);

        JPanel buttonPanel = new JPanel();
        buttonPanel.setLayout(new FlowLayout());
        JButton searchButton = new JButton("搜索");
        searchButton.addActionListener(e -> performSearch());
        buttonPanel.add(searchButton);

        searchPanel.add(fieldPanel);
        searchPanel.add(buttonPanel);

        JPanel sortPanel = new JPanel();
        sortPanel.setLayout(new FlowLayout());
        sortComboBox = new JComboBox<>(new String[]{"选择排序方式", "热度", "评分"});
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

            display(recommendedSpots, false);
        });
        preferencePanel.add(recommendButton);

        JButton backButton = new JButton("返回初始状态");
        backButton.addActionListener(e -> restoreOriginalState());
        preferencePanel.add(backButton);

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
