import javax.swing.*;

public class RoutePlanningWindow extends JFrame {
    public RoutePlanningWindow() {
        setTitle("·�߹滮");
        setSize(320, 200);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setLocationRelativeTo(null); // Center the window

        // Create buttons
        JButton viewMapButton = new JButton("�鿴��ͼ");
        JButton routePlanningButton = new JButton("·�߹滮");

        // Create a panel and add the buttons
        JPanel panel = new JPanel();
        panel.add(viewMapButton);
        panel.add(routePlanningButton);

        // Add the panel to the frame
        add(panel);
    }
}