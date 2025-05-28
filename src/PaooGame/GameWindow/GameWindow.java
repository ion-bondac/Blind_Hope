package PaooGame.GameWindow;

import javax.swing.*;
import java.awt.*;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.util.Objects;

/*! \class GameWindow
    \brief Implementeaza notiunea de fereastra a jocului.

    Membrul wndFrame este un obiect de tip JFrame care va avea utilitatea unei
    ferestre grafice si totodata si cea a unui container (toate elementele
    grafice vor fi continute de fereastra).
 */
public class GameWindow implements KeyListener
{
    private JFrame  wndFrame;       /*!< fereastra principala a jocului*/
    private Menu menu;
    private String  wndTitle;       /*!< titlul ferestrei*/
    private int     wndWidth;       /*!< latimea ferestrei in pixeli*/
    private int     wndHeight;      /*!< inaltimea ferestrei in pixeli*/
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
        canvas = new Canvas();
        canvas.setPreferredSize(new Dimension(wndWidth,wndHeight));
        canvas.setMaximumSize(new Dimension(wndWidth,wndHeight));
        canvas.setMinimumSize(new Dimension(wndWidth,wndHeight));
        canvas.addKeyListener(this);

        menu = new Menu();

        BuildGameWindow();
    }

    @Override
    public void keyTyped(KeyEvent e) {}

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
            case 27: keys[4] = true; // esc
                System.out.println("esc key detected in keyPressed");
                break;
            case 32: keys[5] = true; // attack SPACE
                break;
            case 81: keys[6] = true; // blindfold Q
                break;
            case 83: keys[7] = true; // slide S
                break;
            case 88: keys[8] = true; // switch weapon TAB
                break;
        }
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
            case 27: keys[4] = false; // esc
                break;
            case 32: keys[5] = false; // attack SPACE
                break;
            case 81: keys[6] = false; // blindfold Q
                break;
            case 83: keys[7] = false; // slide S
                break;
            case 88: keys[8] = false; // switch weapon TAB
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
        Image icon = new ImageIcon(Objects.requireNonNull(getClass().getResource("/menu/icon.png"))).getImage();
        wndFrame.setIconImage(icon);
        menu = new Menu();
        wndFrame.add(menu,BorderLayout.CENTER);
        wndFrame.setVisible(true);
    }

    public void hideMenu() {
        menu.setVisible(false);
        getWndFrame().getContentPane().removeAll();

        // Prepare canvas
        canvas.setIgnoreRepaint(true);
        canvas.setFocusable(true);
        canvas.requestFocusInWindow();

        getWndFrame().add(canvas, BorderLayout.CENTER);
        getWndFrame().revalidate();
        getWndFrame().repaint();

        // Create buffer strategy if needed
        if (canvas.getBufferStrategy() == null) {
            canvas.createBufferStrategy(3);
        }

        System.out.println("Menu hidden. Canvas ready: " + canvas.isDisplayable() +
                ", focus: " + canvas.hasFocus());
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

}

