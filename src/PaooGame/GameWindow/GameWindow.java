package PaooGame.GameWindow;

import PaooGame.Game;
import PaooGame.Graphics.Assets;

import javax.swing.*;
import javax.swing.border.Border;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

/*! \class GameWindow
    \brief Implementeaza notiunea de fereastra a jocului.

    Membrul wndFrame este un obiect de tip JFrame care va avea utilitatea unei
    ferestre grafice si totodata si cea a unui container (toate elementele
    grafice vor fi continute de fereastra).
 */
public class GameWindow
{
    private JFrame  wndFrame;       /*!< fereastra principala a jocului*/
    //private JFrame menuFrame;
    private JPanel menuPanel;
    private String  wndTitle;       /*!< titlul ferestrei*/
    private int     wndWidth;       /*!< latimea ferestrei in pixeli*/
    private int     wndHeight;      /*!< inaltimea ferestrei in pixeli*/
    private boolean menuVisible;
    private Canvas  canvas;         /*!< "panza/tablou" in care se poate desena*/

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
        BuildGameWindow();
    }

    /*! \fn private void BuildGameWindow()
        \brief Construieste/creaza fereastra si seteaza toate proprietatile
        necesare: dimensiuni, pozitionare in centrul ecranului, operatia de
        inchidere, invalideaza redimensionarea ferestrei, afiseaza fereastra.

     */
    public void BuildGameWindow()
    {
        wndFrame = new JFrame(wndTitle);
        wndFrame.setSize(wndWidth, wndHeight);
        wndFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        wndFrame.setResizable(false);
        wndFrame.setLocationRelativeTo(null);
        wndFrame.setLayout(new BorderLayout());


        menuPanel = new JPanel();
        menuPanel.setLayout(new GridBagLayout());
        menuPanel.setBackground(Color.BLACK);

        JButton NewGameButton = createMenuButton("New Game");
        JButton LoadGameButton = createMenuButton("Load Game");
        JButton ExitButton = createMenuButton("Exit");

        GridBagConstraints gbc = new GridBagConstraints();

        gbc.gridwidth = GridBagConstraints.REMAINDER;
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.insets = new Insets(10,50,10,50);

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

        wndFrame.add(menuPanel , BorderLayout.CENTER);
        wndFrame.setVisible(true);

//        startButton.setFont(new Font("Arial", Font.PLAIN, 20));
//        startButton.setPreferredSize(new Dimension(200, 50));
//
//        startButton.addActionListener(new ActionListener() {
//            @Override
//            public void actionPerformed(ActionEvent e) {
//                hideMenu();
//            }
//        });
//
//        menuPanel.add(startButton);
//        wndFrame.add(menuPanel);
//        wndFrame.setVisible(true);

//            /// Daca fereastra a mai fost construita intr-un apel anterior
//            /// se renunta la apel
//
//        if(wndFrame != null)
//        {
//            return;
//        }
//            /// Aloca memorie pentru obiectul de tip fereastra si seteaza denumirea
//            /// ce apare in bara de titlu
//        wndFrame = new JFrame(wndTitle);
//            /// Seteaza dimensiunile ferestrei in pixeli
//        wndFrame.setSize(wndWidth, wndHeight);
//            /// Operatia de inchidere (fereastra sa poata fi inchisa atunci cand
//            /// este apasat butonul x din dreapta sus al ferestrei). Totodata acest
//            /// lucru garanteaza ca nu doar fereastra va fi inchisa ci intregul
//            /// program
//        wndFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
//            /// Avand in vedere ca dimensiunea ferestrei poate fi modificata
//            /// si corespunzator continutul actualizat (aici ma refer la dalele
//            /// randate) va recomand sa constrangeti deocamdata jucatorul
//            /// sa se joace in fereastra stabilitata de voi. Puteti reveni asupra
//            /// urmatorului apel ulterior.
//        wndFrame.setResizable(false);
//            /// Recomand ca fereastra sa apara in centrul ecranului. Pentru orice
//            /// alte pozitie se va apela "wndFrame.setLocation(x, y)" etc.
//        wndFrame.setLocationRelativeTo(null);
//            /// Implicit o fereastra cand este creata nu este vizibila motiv pentru
//            /// care trebuie setata aceasta proprietate
//        wndFrame.setVisible(true);
//
//            /// Creaza obiectul de tip canvas (panza) pe care se poate desena.
//        canvas = new Canvas();
//            /// In aceeasi maniera trebuiesc setate proprietatile pentru acest obiect
//            /// canvas (panza): dimensiuni preferabile, minime, maxime etc.
//            /// Urmotorul apel de functie seteaza dimensiunea "preferata"/implicita
//            /// a obiectului de tip canvas.
//            /// Functia primeste ca parametru un obiect de tip Dimension ca incapsuleaza
//            /// doua proprietati: latime si inaltime. Cum acest obiect nu exista
//            /// a fost creat unul si dat ca parametru.
//        canvas.setPreferredSize(new Dimension(wndWidth, wndHeight));
//            /// Avand in vedere ca elementele unei ferestre pot fi scalate atunci cand
//            /// fereastra este redimensionata
//        canvas.setMaximumSize(new Dimension(wndWidth, wndHeight));
//        canvas.setMinimumSize(new Dimension(wndWidth, wndHeight));
//            /// Avand in vedere ca obiectul de tip canvas, proaspat creat, nu este automat
//            /// adaugat in fereastra trebuie apelata metoda add a obiectul wndFrame
//        wndFrame.add(canvas);
//            /// Urmatorul apel de functie are ca scop eventuala redimensionare a ferestrei
//            /// ca tot ce contine sa poate fi afisat complet
//        wndFrame.pack();
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


        // Initialize game canvas
        canvas = new Canvas();
        canvas.setPreferredSize(new Dimension(wndWidth, wndHeight));
        canvas.setMaximumSize(new Dimension(wndWidth, wndHeight));
        canvas.setMinimumSize(new Dimension(wndWidth, wndHeight));
        wndFrame.add(canvas);
        wndFrame.revalidate();
        wndFrame.repaint();
    }

//    public void showMenu()
//    {
//        menuVisible = true;
//        if(menuFrame == null) {
//            // Creează fereastra pentru meniul principal
//            menuFrame = new JFrame("Meniu Principal");
//            menuFrame.setSize(800, 480);
//            menuFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
//            menuFrame.setLocationRelativeTo(null); // Centrat pe ecran
//
//            // Creează un buton pentru a începe jocul
//            JButton startButton = new JButton("Începe jocul");
//            startButton.setFont(new Font("Arial", Font.PLAIN, 20));
//            startButton.setPreferredSize(new Dimension(200, 50));
//
//            // La apăsarea butonului, jocul începe
//            startButton.addActionListener(new ActionListener() {
//                @Override
//                public void actionPerformed(ActionEvent e) {
//                    menuFrame.setVisible(false);  // Închide meniul
//                    menuVisible = false;
//                    BuildGameWindow();            // Construiește fereastra jocului
//                }
//            });
//
//            // Adaugă butonul în fereastra de meniu
//            menuFrame.setLayout(new FlowLayout());
//            menuFrame.add(startButton);
//        }
//        // Afișează meniul
//        menuFrame.setVisible(true);
//    }

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

    public JPanel getMenuPanel()
    {
        return menuPanel;
    }
}

