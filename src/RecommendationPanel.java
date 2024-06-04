
import java.awt.*;
import java.awt.event.*;
import java.util.*;
import java.util.List;
import java.util.stream.Collectors;
import javax.swing.*;

public class RecommendationPanel extends JPanel {

    private JTextField searchField;
    private JComboBox<String> sortComboBox;
    private JPanel preferencePanel;
    private JPanel schoolPanel;
    private JPanel attractionPanel;
    private Map<Spot, JPanel> schoolItemPanelDy;
    private Map<Spot, JPanel> schoolItemPanelSt;
    private Map<Spot, JPanel> attractionItemPanel;
    private JScrollPane schoolScrollPane;
    private JScrollPane attractionScrollPane;
    private JPanel leftPanel;
    private RecommendationSystem recommendationSystem;
    private List<Spot> currentDisplayedSchools;
    private List<Spot> currentDisplayedAttractions;

    private Spot lastClickedSchool = null;
    private Spot lastClickedAttraction = null;

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

        setLayout(new GridLayout(1, 3));

        leftPanel = getLeftPanel();
        schoolScrollPane = getMiddlePanel();
        attractionScrollPane = getRightPanel();

        add(leftPanel);
        add(schoolScrollPane);
        add(attractionScrollPane);

        currentDisplayedSchools = recommendationSystem.getInitialSchools();
        currentDisplayedAttractions = recommendationSystem.getInitialAttractions();

        displaySpots(currentDisplayedSchools, "school", schoolItemPanelDy);
        displaySpots(currentDisplayedAttractions, "attraction", attractionItemPanel);
    }

    private JPanel createItemPanel(Spot spot, boolean type) {
        JPanel ItemPanel = new JPanel();
        BoxLayout boxLayout = new BoxLayout(ItemPanel, BoxLayout.Y_AXIS);
        ItemPanel.setLayout(boxLayout);

        ImageIcon originalIcon = new ImageIcon(spot.getImages()[0]);
        Image originalImage = originalIcon.getImage();
        Image scaledImage = originalImage.getScaledInstance(200, 200, Image.SCALE_SMOOTH);
        JLabel Image = new JLabel(new ImageIcon(scaledImage));
        Image.setAlignmentX(Component.CENTER_ALIGNMENT);

        if (type) {
            Image.addMouseListener(new MouseAdapter() {
                @Override
                public void mouseEntered(MouseEvent e) {
                    updateAttractionsForSchool(spot);
                }

                @Override
                public void mouseExited(MouseEvent e) {
                    restoreOriginalState(currentDisplayedSchools, currentDisplayedAttractions, schoolItemPanelDy, false);
                }
            });
        }
        Image.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                showSpotDetails(spot);
            }
        });

        JLabel Name = new JLabel(spot.getName());
        Name.setHorizontalAlignment(JLabel.CENTER);
        Name.setAlignmentX(Component.CENTER_ALIGNMENT);

        JLabel Tags = new JLabel(String.join(", ", spot.getTags()));
        Tags.setHorizontalAlignment(JLabel.CENTER);
        Tags.setAlignmentX(Component.CENTER_ALIGNMENT);

        ItemPanel.add(Image);
        ItemPanel.add(Box.createVerticalStrut(5));
        ItemPanel.add(Name);
        ItemPanel.add(Box.createVerticalStrut(5));
        ItemPanel.add(Tags);

        return ItemPanel;
    }

    private void showSpotDetails(Spot spot) {

        String type = spot.getType();

        JPanel panelToUpdate = new JPanel();
        panelToUpdate.setLayout(new BoxLayout(panelToUpdate, BoxLayout.Y_AXIS));

        if (type.equals("school")) {
            if (lastClickedSchool == spot) {
                attractionPanel.removeAll();
                restoreOriginalState(currentDisplayedSchools, currentDisplayedAttractions, schoolItemPanelSt, false);
                lastClickedSchool = null;
                return;
            }
            lastClickedSchool = spot;
        } else {
            if (lastClickedAttraction == spot) {
                schoolPanel.removeAll();
                restoreOriginalState(currentDisplayedSchools, currentDisplayedAttractions, schoolItemPanelSt, false);
                lastClickedAttraction = null;
                return;
            }
            lastClickedAttraction = spot;
        }

        JLabel nameLabel = new JLabel(spot.getName(), JLabel.CENTER);
        nameLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
        panelToUpdate.add(nameLabel);
        panelToUpdate.add(Box.createVerticalStrut(10));

        JPanel imagesPanel = new JPanel();
        imagesPanel.setLayout(new BoxLayout(imagesPanel, BoxLayout.Y_AXIS));
        for (String imagePath : spot.getImages()) {
            ImageIcon originalIcon = new ImageIcon(imagePath);
            Image originalImage = originalIcon.getImage();
            Image scaledImage = originalImage.getScaledInstance(200, 200, Image.SCALE_SMOOTH);
            JLabel imageLabel = new JLabel(new ImageIcon(scaledImage));
            imageLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
            imagesPanel.add(imageLabel);
            imagesPanel.add(Box.createVerticalStrut(5));
        }
        JScrollPane imagesScrollPane = new JScrollPane(imagesPanel);
        panelToUpdate.add(imagesScrollPane);
        panelToUpdate.add(Box.createVerticalStrut(10));

        JTextArea descriptionArea = new JTextArea(spot.getDescription());
        descriptionArea.setLineWrap(true);
        descriptionArea.setWrapStyleWord(true);
        descriptionArea.setEditable(false);
        descriptionArea.setAlignmentX(Component.CENTER_ALIGNMENT);
        JScrollPane descriptionScrollPane = new JScrollPane(descriptionArea);
        panelToUpdate.add(descriptionScrollPane);

        if (type.equals("school")) {
            attractionPanel.removeAll();
            attractionPanel.add(panelToUpdate);
//            attractionScrollPane.remove(attractionPanel);
//            attractionScrollPane.add(panelToUpdate);
        } else {
            schoolPanel.removeAll();
            schoolPanel.add(panelToUpdate);
//            schoolScrollPane.remove(schoolPanel);
//            schoolScrollPane.add(panelToUpdate);
        }

        revalidate();
        repaint();
    }

    private void displaySpots(List<Spot> spots, String type, Map<Spot, JPanel> itemPanelMap) {
        if (type.equals("school")) {
            schoolPanel.removeAll();
            for (Spot spot : spots) {
                JPanel ItemPanel = itemPanelMap.get(spot);
                if (ItemPanel != null) {
                    schoolPanel.add(ItemPanel);
                }
            }
        } else if (type.equals("attraction")) {
            attractionPanel.removeAll();
            for (Spot spot : spots) {
                JPanel attractionItemPanels = itemPanelMap.get(spot);
                if (attractionItemPanels != null) {
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
        updateDisplayedSpots(filteredSpots);
    }

    private void updateDisplayedSpots(List<Spot> filteredSpots) {
        currentDisplayedSchools = filteredSpots.stream()
                .filter(spot -> spot.getType().equals("school"))
                .collect(Collectors.toList());
        currentDisplayedAttractions = filteredSpots.stream()
                .filter(spot -> spot.getType().equals("attraction"))
                .collect(Collectors.toList());

        displaySpots(currentDisplayedSchools, "school", schoolItemPanelSt);
        displaySpots(currentDisplayedAttractions, "attraction", attractionItemPanel);
    }

    private void sortResults() {
        String sortCriteria = (String) sortComboBox.getSelectedItem();
        List<Spot> sortedSchools = recommendationSystem.sortSpots(currentDisplayedSchools, sortCriteria);
        List<Spot> sortedAttractions = recommendationSystem.sortSpots(currentDisplayedAttractions, sortCriteria);

        displaySpots(sortedSchools, "school", schoolItemPanelSt);
        displaySpots(sortedAttractions, "attraction", attractionItemPanel);
    }

    private void updateAttractionsForSchool(Spot school) {
        Spot[] relatedSpots = school.getRelatedSpots();
        if (relatedSpots != null) {
            attractionPanel.removeAll();
            for (Spot spot : relatedSpots) {
                JPanel panel = spot.getType().equals("school") ? schoolItemPanelSt.get(spot) : attractionItemPanel.get(spot);
                if (panel != null) {
                    attractionPanel.add(panel);
                }
            }
            revalidate();
            repaint();
        }
    }

    private void restoreOriginalState(List<Spot> currentDisplayedSchools1, List<Spot> currentDisplayedAttractions1, Map<Spot, JPanel> schoolItemPanel, boolean type) {
        lastClickedSchool = null;
        lastClickedAttraction = null;
        schoolScrollPane.setVisible(true);
        attractionScrollPane.setVisible(true);
        if (type) {
            currentDisplayedSchools = recommendationSystem.getInitialSchools();
            currentDisplayedAttractions = recommendationSystem.getInitialAttractions();
        }
        displaySpots(currentDisplayedSchools1, "school", schoolItemPanel);
        displaySpots(currentDisplayedAttractions1, "attraction", attractionItemPanel);
    }

    private JPanel getLeftPanel() {
        leftPanel = new JPanel();
        leftPanel.setLayout(new GridLayout(3, 1));

        JPanel searchPanel = new JPanel();
        searchPanel.setLayout(new FlowLayout());
        searchField = new JTextField(20);
        JButton searchButton = new JButton("搜索");
        searchButton.addActionListener(e -> performSearch());
        searchPanel.add(searchField);
        searchPanel.add(searchButton);

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
            updateDisplayedSpots(recommendedSpots);
        });
        preferencePanel.add(recommendButton);

        JButton backButton = new JButton("返回初始状态");
        backButton.addActionListener(e -> restoreOriginalState(recommendationSystem.getInitialSchools(), recommendationSystem.getInitialAttractions(), schoolItemPanelDy, true));
        preferencePanel.add(backButton);

        leftPanel.add(searchPanel);
        leftPanel.add(sortPanel);
        leftPanel.add(preferencePanel);
        return leftPanel;
    }

    private JScrollPane getMiddlePanel() {
        schoolPanel = new JPanel();
        schoolPanel.setLayout(new GridLayout(0, 1));
        schoolScrollPane = new JScrollPane(schoolPanel);
        return schoolScrollPane;
    }

    private JScrollPane getRightPanel() {
        attractionPanel = new JPanel();
        attractionPanel.setLayout(new GridLayout(0, 1));
        attractionScrollPane = new JScrollPane(attractionPanel);
        return attractionScrollPane;
    }
}
