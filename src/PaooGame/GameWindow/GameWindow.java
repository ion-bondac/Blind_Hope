package PaooGame.GameWindow;

import PaooGame.Game;
import PaooGame.Graphics.Assets;

import javax.swing.*;
import javax.swing.border.Border;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;

/*! \class GameWindow
    \brief Implementeaza notiunea de fereastra a jocului.

    Membrul wndFrame este un obiect de tip JFrame care va avea utilitatea unei
    ferestre grafice si totodata si cea a unui container (toate elementele
    grafice vor fi continute de fereastra).
 */
public class GameWindow implements KeyListener
{
    private JFrame  wndFrame;       /*!< fereastra principala a jocului*/
    //private JFrame menuFrame;
    private JPanel menuPanel;
    private JPanel settingsIcon;
    private String  wndTitle;       /*!< titlul ferestrei*/
    private int     wndWidth;       /*!< latimea ferestrei in pixeli*/
    private int     wndHeight;      /*!< inaltimea ferestrei in pixeli*/
    private boolean menuVisible;
    private Canvas  canvas;         /*!< "panza/tablou" in care se poate desena*/
    public int key;
    /*! \fn GameWindow(String title, int width, int height)
            \brief Constructorul cu parametri al clasei GameWindow

            Retine proprietatile ferestrei proprietatile (titlu, latime, inaltime)
            in variabilele membre deoarece vor fi necesare pe parcursul jocului.
            Crearea obiectului va trebui urmata de crearea ferestrei propriuzise
            prin apelul metodei BuildGameWindow()

            \param title Titlul ferestrei.
            \param width Latimea ferestrei in pixeli.
            \param height Inaltimea ferestrei in pixeli.
         */
    public GameWindow(String title, int width, int height){
        wndTitle    = title;    /*!< Retine titlul ferestrei.*/
        wndWidth    = width;    /*!< Retine latimea ferestrei.*/
        wndHeight   = height;   /*!< Retine inaltimea ferestrei.*/
        //wndFrame    = null;     /*!< Fereastra nu este construita.*/
        menuVisible = true;

        canvas = new Canvas();
        canvas.setPreferredSize(new Dimension(wndWidth,wndHeight));
        canvas.setMaximumSize(new Dimension(wndWidth,wndHeight));
        canvas.setMinimumSize(new Dimension(wndWidth,wndHeight));
        canvas.addKeyListener(this);

        BuildGameWindow();


    }

    @Override
    public void keyTyped(KeyEvent e) {
        //keyTyped = Invoked when a key is typed. Uses KeyChar, char output
//        switch(e.getKeyChar()) {
//            case 'a': label.setLocation(label.getX()-10, label.getY());
//                break;
//            case 'w': label.setLocation(label.getX(), label.getY()-10);
//                break;
//            case 's': label.setLocation(label.getX(), label.getY()+10);
//                break;
//            case 'd': label.setLocation(label.getX()+10, label.getY());
//                break;
//        }

    }

    @Override
    public void keyPressed(KeyEvent e) {
        //keyPressed = Invoked when a physical key is pressed down. Uses KeyCode, int output
        switch(e.getKeyCode()) {
            case 68: key = 1; // dreapta D
                break;
            case 65: key = 2; // stanga A
                break;
            case 87: key = 3; // sus W
                break;
            case 83: key = 4; // jos S
                break;
        }
//        System.out.println("You pressed key char: " + e.getKeyChar());
//        System.out.println("You pressed key code: " + e.getKeyCode());
    }

    @Override
    public void keyReleased(KeyEvent e) {
        //keyReleased = called whenever a button is released
        switch(e.getKeyCode()) {
            case 68: key = 0; // dreapta D
                break;
            case 65: key = 0; // stanga A
                break;
            case 87: key = 0; // sus W
                break;
            case 83: key = 0; // jos S
                break;
        }
//        System.out.println("You released key char: " + e.getKeyChar());
//        System.out.println("You released key code: " + e.getKeyCode());
    }

    /*! \fn private void BuildGameWindow()
        \brief Construieste/creaza fereastra si seteaza toate proprietatile
        necesare: dimensiuni, pozitionare in centrul ecranului, operatia de
        inchidere, invalideaza redimensionarea ferestrei, afiseaza fereastra.

     */
    public void BuildGameWindow() {
        wndFrame = new JFrame(wndTitle);
        wndFrame.setSize(wndWidth, wndHeight);
        wndFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        wndFrame.setResizable(false);
        wndFrame.setLocationRelativeTo(null);
        wndFrame.setLayout(new BorderLayout());
        wndFrame.setFocusable(true);
        wndFrame.requestFocusInWindow();
        wndFrame.addKeyListener(this);

        menuPanel = new JPanel();
        menuPanel.setLayout(new GridBagLayout());
        menuPanel.setBackground(Color.BLACK);

        JButton NewGameButton = createMenuButton("New Game");
        JButton LoadGameButton = createMenuButton("Load Game");
        JButton ExitButton = createMenuButton("Exit");

        GridBagConstraints gbc = new GridBagConstraints();

        gbc.gridwidth = GridBagConstraints.REMAINDER;
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.insets = new Insets(10, 50, 10, 50);

        menuPanel.add(NewGameButton, gbc);
        menuPanel.add(LoadGameButton, gbc);
        menuPanel.add(ExitButton, gbc);

        // Adaugă spațiu flexibil deasupra butoanelor pentru a le împinge în jos
        gbc.weighty = 1.0; // Acordă prioritate spațiului vertical
        gbc.gridy = 0;     // Prima poziție în GridBagLayout
        menuPanel.add(Box.createVerticalGlue(), gbc); // Spațiu elastic

        // Resetează weighty pentru butoane
        gbc.weighty = 0.0;
        gbc.gridy = 1;
        menuPanel.add(NewGameButton, gbc);
        gbc.gridy = 2;
        menuPanel.add(LoadGameButton, gbc);
        gbc.gridy = 3;
        menuPanel.add(ExitButton, gbc);

        // Adaugă spațiu sub butoane (opțional)
        gbc.weighty = 0.2; // Spațiu mai mic sub butoane față de cel de deasupra
        gbc.gridy = 4;
        menuPanel.add(Box.createVerticalGlue(), gbc);


        settingsIcon = new JPanel();
        settingsIcon.setLayout(new GridBagLayout());
        settingsIcon.setBackground(null);
        JButton SettingsBtn = createMenuButton("*");

        GridBagConstraints gbc2 = new GridBagConstraints();

        gbc2.gridwidth = GridBagConstraints.SOUTHEAST;
        gbc2.fill = GridBagConstraints.HORIZONTAL;
        gbc2.insets = new Insets(0, 100, 0, 0);

        settingsIcon.add(SettingsBtn, gbc2);

        menuPanel.add(settingsIcon, gbc);
        wndFrame.add(menuPanel, BorderLayout.CENTER);
        wndFrame.setVisible(true);

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

    public void hideMenu() {
        menuVisible = false;
        wndFrame.remove(menuPanel);
        wndFrame.add(canvas,BorderLayout.CENTER);

        wndFrame.revalidate();
        wndFrame.repaint();
        canvas.requestFocusInWindow();
    }


    public boolean isMenuShowing()
    {
        return menuVisible;
    }

    /*! \fn public int GetWndWidth()
        \brief Returneaza latimea ferestrei.
     */
    public int GetWndWidth()
    {
        return wndWidth;
    }

    /*! \fn public int GetWndWidth()
        \brief Returneaza inaltimea ferestrei.
     */
    public int GetWndHeight()
    {
        return wndHeight;
    }

    /*! \fn public int GetCanvas()
        \brief Returneaza referinta catre canvas-ul din fereastra pe care se poate desena.
     */
    public Canvas GetCanvas() {
        return canvas;
    }

    public JFrame getWndFrame(){return wndFrame;}

    public JPanel getMenuPanel()
    {
        return menuPanel;
    }
}

