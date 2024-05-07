import javax.swing.*;

public class RoutePlanningWindow extends JFrame {
    public RoutePlanningWindow() {
        setTitle("路线规划");
        setSize(320, 200);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setLocationRelativeTo(null); // Center the window

        // Create buttons
        JButton viewMapButton = new JButton("查看地图");
        JButton routePlanningButton = new JButton("路线规划");

        // Create a panel and add the buttons
        JPanel panel = new JPanel();
        panel.add(viewMapButton);
        panel.add(routePlanningButton);

        // Add the panel to the frame
        add(panel);
    }
}