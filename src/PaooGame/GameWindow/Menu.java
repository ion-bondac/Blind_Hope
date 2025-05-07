package PaooGame.GameWindow;

import javax.swing.*;
import java.awt.*;

public class Menu {
    private JPanel menuPanel;
    private JPanel settingsIcon;
    private boolean isVisible;

    public Menu(int width,int height)
    {
        menuPanel = new JPanel();
        menuPanel.setLayout(new GridBagLayout());
        menuPanel.setBackground(Color.BLACK);
        isVisible = true;

        setupMenuButtons();
    }

    private void setupMenuButtons()
    {
        JButton NewGameButton = createMenuButton("New Game");
        JButton LoadGameButton = createMenuButton("Load Game");
        JButton ExitButton = createMenuButton("Exit");

        GridBagConstraints gbc = new GridBagConstraints();

        gbc.gridwidth = GridBagConstraints.REMAINDER;
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.insets = new Insets(10, 50, 10, 50);


    }

    private JButton createMenuButton(String text)
    {
        JButton Button = new JButton(text);
        Button.setFont(new Font("Arial",Font.PLAIN,24));
        Button.setBackground(Color.DARK_GRAY);
        Button.setForeground(Color.WHITE);
        Button.setFocusPainted(false);
        Button.setBorder(BorderFactory.createLineBorder(Color.WHITE,2));
        return Button;
    }

    public JPanel getMenuPanel()
    {
        return menuPanel;
    }

    public boolean isMenuShowing()
    {
        return isVisible;
    }
}
