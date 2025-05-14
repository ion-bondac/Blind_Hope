package PaooGame.GameWindow;

import PaooGame.Game;
import PaooGame.Graphics.Assets;

import javax.swing.*;
//import javax.swing.border.Border;
import java.awt.*;
//import java.awt.event.ActionEvent;
//import java.awt.event.ActionListener;
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
//    private JPanel menuPanel;
//    private JPanel settingsIcon;
    private Menu menu;
    private String  wndTitle;       /*!< titlul ferestrei*/
    private int     wndWidth;       /*!< latimea ferestrei in pixeli*/
    private int     wndHeight;      /*!< inaltimea ferestrei in pixeli*/
//    private boolean menuVisible;
    private Canvas  canvas;         /*!< "panza/tablou" in care se poate desena*/
    public boolean[] keys = new boolean[10];
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
//        menuVisible = true;

        canvas = new Canvas();
        canvas.setPreferredSize(new Dimension(wndWidth,wndHeight));
        canvas.setMaximumSize(new Dimension(wndWidth,wndHeight));
        canvas.setMinimumSize(new Dimension(wndWidth,wndHeight));
        canvas.addKeyListener(this);

        menu = new Menu();

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
            case 68: keys[1] = true; // dreapta D
                break;
            case 65: keys[2] = true; // stanga A
                break;
            case 87: keys[3] = true; // sus W
                break;
            case 80: keys[5] = true; // save P
                System.out.println("P key detected in keyPressed");
                break;
        }
//        System.out.println("You pressed key char: " + e.getKeyChar());
//        System.out.println("You pressed key code: " + e.getKeyCode());
    }

    @Override
    public void keyReleased(KeyEvent e) {
        //keyReleased = called whenever a button is released
        switch(e.getKeyCode()) {
            case 68: keys[1] = false; // dreapta D
                break;
            case 65: keys[2] = false; // stanga A
                break;
            case 87: keys[3] = false; // sus W
                break;
            case 80: keys[5] = false; // save P
                break;
        }
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

        menu = new Menu();
        wndFrame.add(menu,BorderLayout.CENTER);
        wndFrame.setVisible(true);
    }

    public void hideMenu() {
        menu.setVisible(false);
        wndFrame.getContentPane().removeAll();
        wndFrame.add(canvas, BorderLayout.CENTER);
        wndFrame.revalidate();
        wndFrame.repaint();
        canvas.setFocusable(true);
        canvas.requestFocusInWindow();
        System.out.println("Menu hidden. Canvas focus: " + canvas.hasFocus());
    }


    public boolean isMenuShowing()
    {
        return menu.isMenuShowing();
    }

    public Menu getMenu()
    {
        return menu;
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

//    public JPanel getMenuPanel()
//    {
//        return menuPanel;
//    }
}

