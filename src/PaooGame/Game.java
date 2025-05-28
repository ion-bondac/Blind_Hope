package PaooGame;

import PaooGame.Database.DatabaseManager;
import PaooGame.Database.GameSession;
import PaooGame.GameWindow.*;
import PaooGame.Graphics.Assets;
import PaooGame.Graphics.BlindOverlay;
import PaooGame.Graphics.Fog;
import PaooGame.Graphics.HealthBar;
import PaooGame.Tiles.Tile;
import PaooGame.Tiles.TileFactory;
import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.image.BufferStrategy;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/*! \class Game
    \brief Clasa principala a intregului proiect. Implementeaza Game - Loop (Update -> Draw)

                ------------
                |           |
                |     ------------
    60 times/s  |     |  Update  |  -->{ actualizeaza variabile, stari, pozitii ale elementelor grafice etc.
        =       |     ------------
     16.7 ms    |           |
                |     ------------
                |     |   Draw   |  -->{ deseneaza totul pe ecran
                |     ------------
                |           |
                -------------

    Implementeaza interfata Runnable:

        public interface Runnable {
            public void run();
        }

    Interfata este utilizata pentru a crea un nou fir de executie avand ca argument clasa Game.
    Clasa Game trebuie sa aiba definita metoda "public void run()", metoda ce va fi apelata
    in noul thread(fir de executie). Mai multe explicatii veti primi la curs.

    In mod obisnuit aceasta clasa trebuie sa contina urmatoarele:
        - public Game();            //constructor
        - private void init();      //metoda privata de initializare
        - private void update();    //metoda privata de actualizare a elementelor jocului
        - private void draw();      //metoda privata de desenare a tablei de joc
        - public run();             //metoda publica ce va fi apelata de noul fir de executie
        - public synchronized void start(); //metoda publica de pornire a jocului
        - public synchronized void stop()   //metoda publica de oprire a jocului
 */
public class Game implements Runnable
{
    private final GameWindow wnd;        /*!< Fereastra in care se va desena tabla jocului*/
    private boolean runState;   /*!< Flag ce starea firului de executie.*/
    private Thread gameThread; /*!< Referinta catre thread-ul de update si draw al ferestrei*/
    private BufferStrategy bs;         /*!< Referinta catre un mecanism cu care se organizeaza memoria complexa pentru un canvas.*/
    private GameMap gameMap;
    private Graphics g;          /*!< Referinta catre un context grafic.*/
    private Player Mihai = new Player(200,200);
    private HealthBar healthBar = new HealthBar(Mihai);
    private BlindOverlay overlay = new BlindOverlay(Mihai);
    private int tileSize = 32;
    private String playerName;

    private ArrayList<Fog> FogList = new ArrayList<>();

    private int currentLevel = 1;

    private ArrayList<Enemy> EnemyList = new ArrayList<>();

    private Camera camera = new Camera(800,480);
    EntityManager entityManager = new EntityManager();
    private DatabaseManager dbManager; // Add DatabaseManager
    private PauseMenu pauseMenu;
    private boolean isPaused; // Track pause state

    private StoryPanel storyPanel;
    private boolean showingStory = false;
    private boolean canvasReady; // Track canvas readiness

    /// Sunt cateva tipuri de "complex buffer strategies", scopul fiind acela de a elimina fenomenul de
    /// flickering (palpaire) a ferestrei.
    /// Modul in care va fi implementata aceasta strategie in cadrul proiectului curent va fi triplu buffer-at

    ///                         |------------------------------------------------>|
    ///                         |                                                 |
    ///                 ****************          *****************        ***************
    ///                 *              *   Show   *               *        *             *
    /// [  ] <---- * Front Buffer *  <------ * Middle Buffer * <----- * Back Buffer * <---- Draw()
    ///                 *              *          *               *        *             *
    ///                 ****************          *****************        ***************

//    private Tile tile; /*!< variabila membra temporara. Este folosita in aceasta etapa doar pentru a desena ceva pe ecran.*/

    /*! \fn public Game(String title, int width, int height)
        \brief Constructor de initializare al clasei Game.

        Acest constructor primeste ca parametri titlul ferestrei, latimea si inaltimea
        acesteia avand in vedere ca fereastra va fi construita/creata in cadrul clasei Game.

        \param title Titlul ferestrei.
        \param width Latimea ferestrei in pixeli.
        \param height Inaltimea ferestrei in pixeli.
     */
    public Game(String title, int width, int height) throws IOException {
        /// Obiectul GameWindow este creat insa fereastra nu este construita
        /// Acest lucru va fi realizat in metoda init() prin apelul
        /// functiei BuildGameWindow();
        wnd = new GameWindow(title, width, height);
        /// Resetarea flagului runState ce indica starea firului de executie (started/stoped)
        runState = false;
        dbManager = new DatabaseManager(); // Initialize DatabaseManager
        isPaused = false; // Initialize pause state
        pauseMenu = null; // Ensure no pause menu
        storyPanel = null;
        showingStory = false;
        canvasReady = false; // Initialize canvas as not ready
    }

    private void saveGameSession() {
        try {
            dbManager.saveSession(playerName, Mihai.getX(), Mihai.getY(), currentLevel, Mihai.health, Mihai.getScore());
            JOptionPane.showMessageDialog(wnd.getWndFrame(), "Game session saved!", "Success", JOptionPane.INFORMATION_MESSAGE);
        } catch (RuntimeException e) {
            JOptionPane.showMessageDialog(wnd.getWndFrame(), "Failed to save session: " + e.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void resetEntitiesForLevel(int level) {
        // Clear existing entities
        for (Enemy e : EnemyList) {
            e.kill();
        }
        FogList.clear();
        EnemyList.clear();
        entityManager.clearEntities();
        entityManager.addEntity(Mihai);

        // Initialize entities based on level
        if (level == 1) {
            FogList = new ArrayList<>(Arrays.asList(
                    new Fog(Mihai, 21, 24),
                    new Fog(Mihai, 23, 23),
                    new Fog(Mihai, 33, 24),
                    new Fog(Mihai, 44, 26),
                    new Fog(Mihai, 70, 28)
            ));
            EnemyList = new ArrayList<>(Arrays.asList(
                    new Enemy(22 * tileSize, 14 * tileSize, Mihai, "Eagle", true),
                    new Enemy(30 * tileSize, 12 * tileSize, Mihai, "Eagle", false),
                    new Enemy(41 * tileSize, 14 * tileSize, Mihai, "Eagle", false),
                    new Enemy(45 * tileSize, 17 * tileSize, Mihai, "Eagle", true),
                    new Enemy(55 * tileSize, 20 * tileSize, Mihai, "Eagle", true),
                    new Enemy(63 * tileSize, 24 * tileSize, Mihai, "Eagle", false),
                    new Enemy(80 * tileSize, 25 * tileSize, Mihai, "Eagle", false),
                    new Enemy(86 * tileSize, 22 * tileSize, Mihai, "Eagle", false)
            ));
        } else if (level == 2) {
            FogList = new ArrayList<>(Arrays.asList(
                    new Fog(Mihai, 25, 10),
                    new Fog(Mihai, 33, 14),
                    new Fog(Mihai, 40, 14),
                    new Fog(Mihai, 48, 16),
                    new Fog(Mihai, 63, 12),
                    new Fog(Mihai, 75, 17)
            ));
            EnemyList = new ArrayList<>(Arrays.asList(
                    new Enemy(17 * 32, 16 * 32, Mihai, "Gnome", true),
                    new Enemy(17 * 32, 22 * 32, Mihai, "Gnome", true),
                    new Enemy(33 * 32, 21 * 32, Mihai, "Gnome", true),
                    new Enemy(33 * 32, 21 * 32, Mihai, "Gnome", false),
                    new Enemy(40 * 32, 22 * 32, Mihai, "Gnome", false),
                    new Enemy(65 * 32, 17 * 32, Mihai, "Gnome", false),
                    new Enemy(82 * 32, 16 * 32, Mihai, "Gnome", true),
                    new Enemy(88 * 32, 14 * 32, Mihai, "Gnome", true),
                    new Enemy(88 * 32, 14 * 32, Mihai, "Gnome", false)
            ));
        } else if (level == 3) {
            FogList = new ArrayList<>(Arrays.asList(
                    new Fog(Mihai, 21, 14),
                    new Fog(Mihai, 23, 11),
                    new Fog(Mihai, 27, 9),
                    new Fog(Mihai, 32, 11),
                    new Fog(Mihai, 47, 5),
                    new Fog(Mihai, 57, 8),
                    new Fog(Mihai, 71, 10),
                    new Fog(Mihai, 80, 17)
            ));
        }

        for (Enemy e : EnemyList) {
            entityManager.addEntity(e);
        }
    }

    private void InitGame() throws IOException {
        Assets.Init(1);
        TileFactory tileFactory = new TileFactory();
        tileFactory.clearCache();
        setupMenuButtons();
        SoundPlayer.playLoopingSound("/sounds/menuMusic.wav");

        resetEntitiesForLevel(currentLevel);
        isPaused = false; // Reset pause state
        pauseMenu = null; // Clear pause menu
        // menu is shown
        wnd.getWndFrame().getContentPane().removeAll();
        wnd.getWndFrame().add(wnd.getMenu(), BorderLayout.CENTER);
        wnd.getWndFrame().revalidate();
        wnd.getWndFrame().repaint();
    }

    /*! \fn public void run()
        \brief Functia ce va rula in thread-ul creat.

        Aceasta functie va actualiza starea jocului si va redesena tabla de joc (va actualiza fereastra grafica)
     */
    public void run()
    {
        /// Initializeaza obiectul game
        try {
            InitGame();
        } catch (IOException e) {
            e.printStackTrace();
            return;
        }

        // Wait for window to be ready
        while (!wnd.getWndFrame().isDisplayable()) {
            try {
                Thread.sleep(10);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }

        long oldTime = System.nanoTime();   /*!< Retine timpul in nanosecunde aferent frame-ului anterior.*/
        long curentTime;                    /*!< Retine timpul curent de executie.*/

        /// Apelul functiilor Update() & Draw() trebuie realizat la fiecare 16.7 ms
        /// sau mai bine spus de 60 ori pe secunda.

        final int framesPerSecond = 60; /*!< Constanta intreaga initializata cu numarul de frame-uri pe secunda.*/
        final double timeFrame = 1000000000 / framesPerSecond; /*!< Durata unui frame in nanosecunde.*/

        /// Atat timp timp cat threadul este pornit Update() & Draw()
        while (runState == true)
        {
            /// Se obtine timpul curent
            curentTime = System.nanoTime();
            /// Daca diferenta de timp dintre curentTime si oldTime mai mare decat 16.6 ms
            if ((curentTime - oldTime) > timeFrame)
            {
                if (!wnd.isMenuShowing() && wnd.GetCanvas().isDisplayable() && !isPaused && !showingStory && canvasReady)
                {
                    /// Actualizeaza pozitiile elementelor
                    Update();
                    /// Deseneaza elementele grafica in fereastra.
                    try {
                        Draw();
                    } catch (IOException e) {
                        throw new RuntimeException(e);
                    }
                }
                oldTime = curentTime;
            }
        }
    }

    private void showNameInputDialog() {
        SwingUtilities.invokeLater(() -> {
            ActionListener startListener = e -> {
                NameInputDialog dialog = (NameInputDialog) ((JButton) e.getSource()).getParent().getParent();
                String enteredName = dialog.getPlayerName();
                if (!enteredName.isEmpty()) {
                    playerName = enteredName;
                    showStoryPanel();
                } else {
                    JOptionPane.showMessageDialog(wnd.getWndFrame(), "Please enter a valid name!", "Warning", JOptionPane.WARNING_MESSAGE);
                }
            };

            ActionListener cancelListener = e -> {
                wnd.getWndFrame().getContentPane().removeAll();
                wnd.getWndFrame().add(wnd.getMenu(), BorderLayout.CENTER);
                wnd.getWndFrame().revalidate();
                wnd.getWndFrame().repaint();
            };

            NameInputDialog dialog = new NameInputDialog(wnd.getWndFrame(), startListener, cancelListener);
            wnd.getWndFrame().getContentPane().removeAll();
            wnd.getWndFrame().add(dialog, BorderLayout.CENTER);
            wnd.getWndFrame().revalidate();
            wnd.getWndFrame().repaint();
            dialog.requestFocusInWindow();
        });
    }

    private void showStoryPanel() {
        SwingUtilities.invokeLater(() -> {
            List<String> initialStory = Arrays.asList(
                    "Într-un trecut îndepărtat, pe planeta Caligo, o primejdie nemaivăzută amenință viața locuitorilor." ,
                    "O ceață densă și otrăvitoare s-a răspândit peste întinderile planetei, punând în pericol pe oricine se apropie.",
                    " Contactul cu acest nor toxic le afectează ochii, doborând pe cei care îndrăznesc să înainteze fără protecție." ,
                    "Însă cel mai grav este că regina planetei a fost afectată, iar viața ei este în mare pericol." ,
                    "Iar tu, " + playerName + "poți fi eroul care va salva acestă planetă." ,
                    " Eu, Gnaritas, te voi îndruma pe calea ta. Pentru început trebuie să găsești floarea unui cactus bătrân de 400 ani.",
                    "Pentru început trebuie să știi că poți să te miști cu tastele a,d ; Să sari cu w ; Să faci slide cu s ; Vei ataca cu tasta space." ,
                    "Cel mai important, nu uita să echipezi eșarfa cu tasta q când încerci să intri în ceață."
            );
            ActionListener continueListener = e -> {
                canvasReady = false; // Reset canvas readiness
                startNewGame();
                // Ensure canvas is fully added and displayable before creating buffer strategy
                SwingUtilities.invokeLater(() -> {
                    wnd.getWndFrame().getContentPane().removeAll();
                    wnd.getWndFrame().add(wnd.GetCanvas(), BorderLayout.CENTER);
                    wnd.getWndFrame().revalidate();
                    wnd.getWndFrame().repaint();
                    // Wait for canvas to be displayable
                    while (!wnd.GetCanvas().isDisplayable()) {
                        try {
                            Thread.sleep(10);
                        } catch (InterruptedException ex) {
                            ex.printStackTrace();
                        }
                    }
                    wnd.GetCanvas().createBufferStrategy(3);
                    wnd.GetCanvas().requestFocusInWindow();
                    canvasReady = true; // Mark canvas as ready
                    showingStory = false;
                    storyPanel = null;
                    System.out.println("Initial story panel continued to game for player: " + playerName);
                });
            };

            storyPanel = new StoryPanel(wnd.getWndFrame(), continueListener, initialStory);
            wnd.getWndFrame().getContentPane().removeAll();
            wnd.getWndFrame().add(storyPanel, BorderLayout.CENTER);
            wnd.getWndFrame().revalidate();
            wnd.getWndFrame().repaint();
            storyPanel.requestFocusInWindow();
            showingStory = true;
        });
    }

    private void showLevelStory(int level) {
        SwingUtilities.invokeLater(() -> {
            List<String> storySentences = getStoryForLevel(level);
            ActionListener continueListener;
            if (level == 4) { // Final story after Level 3
                continueListener = e -> {
                    canvasReady = false; // Reset canvas readiness
                    wnd.getWndFrame().getContentPane().removeAll();
                    wnd.getWndFrame().add(wnd.getMenu(), BorderLayout.CENTER);
                    wnd.getWndFrame().revalidate();
                    wnd.getWndFrame().repaint();
                    showingStory = false;
                    isPaused = false;
                    storyPanel = null;
                    currentLevel = 1; // Reset for new game
                    System.out.println("Final story completed, returning to menu");
                };
            } else {
                continueListener = e -> {
                    canvasReady = false; // Reset canvas readiness
                    loadLevelForStory(level + 1); // Load next level
                    SwingUtilities.invokeLater(() -> {
                        wnd.getWndFrame().getContentPane().removeAll();
                        wnd.getWndFrame().add(wnd.GetCanvas(), BorderLayout.CENTER);
                        wnd.getWndFrame().revalidate();
                        wnd.getWndFrame().repaint();
                        while (!wnd.GetCanvas().isDisplayable()) {
                            try {
                                Thread.sleep(10);
                            } catch (InterruptedException ex) {
                                ex.printStackTrace();
                            }
                        }
                        wnd.GetCanvas().createBufferStrategy(3);
                        wnd.GetCanvas().requestFocusInWindow();
                        canvasReady = true; // Mark canvas as ready
                        hideStory();
                        System.out.println("Level " + level + " story continued, loaded level " + (level + 1));
                    });
                };
            }

            if (storyPanel == null) {
                storyPanel = new StoryPanel(wnd.getWndFrame(), continueListener, storySentences);
                wnd.getWndFrame().getContentPane().removeAll();
                wnd.getWndFrame().add(storyPanel, BorderLayout.CENTER); // Corrected: Use storyPanel directly
            } else {
                storyPanel.setSentences(storySentences);
            }

            showingStory = true;
            isPaused = true;
            storyPanel.setVisible(true);
            wnd.getWndFrame().revalidate();
            wnd.getWndFrame().repaint();
            storyPanel.requestFocusInWindow();
            System.out.println("Showing story for level: " + level);
        });
    }

    private void loadLevelForStory(int level) {
        String filename;
        if (level == 2) {
            filename = "src/PaooGame/LEVEL2MAP.txt";
        } else if (level == 3) {
            filename = "src/PaooGame/Level3MAP.txt";
        } else {
            System.err.println("Invalid level for transition: " + level);
            return; // No valid level to load
        }
        try {
            Assets.Init(level);
            TileFactory tileFactory = new TileFactory();
            tileFactory.clearCache();
            gameMap = new GameMap(filename, tileFactory, level);
            currentLevel = level;
            resetEntitiesForLevel(level);
            Mihai.NextLevelRespawn(200, 400); // Respawn at fixed position
            Mihai.health = 300;
            Mihai.isMoving = false; // Ensure player is not moving
            Mihai.gravity = 0; // Reset gravity to prevent falling
            // Reset input states to prevent automatic movement
            for (int i = 0; i < wnd.keys.length; i++) {
                wnd.keys[i] = false;
            }
            System.out.println("Successfully loaded level: " + level + " with map: " + filename + ", input states reset");
            wnd.GetCanvas().repaint();
        } catch (IOException e) {
            System.err.println("Failed to load game map for level " + level + ": " + e.getMessage());
            JOptionPane.showMessageDialog(wnd.getWndFrame(), "Failed to load level " + level + ": Map not found!", "Error", JOptionPane.ERROR_MESSAGE);
            // Return to menu to prevent game from continuing in broken state
            wnd.getWndFrame().getContentPane().removeAll();
            wnd.getWndFrame().add(wnd.getMenu(), BorderLayout.CENTER);
            wnd.getWndFrame().revalidate();
            wnd.getWndFrame().repaint();
            showingStory = false;
            isPaused = false;
            currentLevel = 1;
        }
    }

    private void hideStory() {
        SwingUtilities.invokeLater(() -> {
            if (storyPanel != null) {
                wnd.getWndFrame().getContentPane().remove(storyPanel);
                storyPanel = null;
                showingStory = false;
                isPaused = false;
                System.out.println("Story hidden, resuming game");
            }
        });
    }


    private List<String> getStoryForLevel(int level) {
        switch (level) {
            case 1:
                return Arrays.asList(
                        "Felicitări cu completarea nivelului 1!, urmează să cauți ciuperca magică, din Pădurea Umbrelor",
                        "Totuși fii atent...",
                        "Drumul spre această comoară este păzit de Frăția Gnomilor, o grupare de războinici mici, dar extrem de agili, care protejează ciuperca cu orice preț."
                );
            case 2:
                return Arrays.asList(
                        "Felicitări cu completarea nivelului 2!, în timp ce mergeai prin pădure ai gasit un arc magic, cred că vei avea nevoie de el în lupta următoare",
                        "Pentru a schimba arma , apăsați pe tasta x, iar atacul pe space.",
                        "Pentru a căpăta ultimul ingredient, tu, " + playerName + ", trebuie să îl învingi pe Lord Saxarion."
                );
            case 3:
                return Arrays.asList(
                );
            case 4: // Final story after Level 3
                return Arrays.asList(
                        "Felicitări , ai colectat toate ingredientele pentru potion.",
                        "Ai invins fortele intunecate si ai adus salvarea pentru regină.",
                        "Eroul " + playerName + " va fi amintit pentru totdeauna!"
                );
            default:
                return Arrays.asList("Bun venit in aventura ta!");
        }
    }

    private void startNewGame() {
        Mihai.respawn(200, 200);
        camera.update(Mihai);
        isPaused = false;
        pauseMenu = null;
        currentLevel = 1;
        Mihai.health = 300;
        Mihai.score = 0;
        try {
            Assets.Init(1);
            TileFactory tileFactory = new TileFactory();
            tileFactory.clearCache();
            gameMap = new GameMap("src/PaooGame/Level1Map.txt", tileFactory, 1);
        } catch (IOException e) {
            System.err.println("Failed to load game map: " + e.getMessage());
            JOptionPane.showMessageDialog(wnd.getWndFrame(), "Failed to load game map", "Error", JOptionPane.ERROR_MESSAGE);
        }

        resetEntitiesForLevel(currentLevel);
        wnd.hideMenu();
    }

    /*! \fn private void init()
        \brief  Metoda construieste fereastra jocului, initializeaza aseturile, listenerul de tastatura etc.

        Fereastra jocului va fi construita prin apelul functiei BuildGameWindow();
        Sunt construite elementele grafice (assets): dale, player, elemente active si pasive.
     */
    private void setupMenuButtons() {
        wnd.getMenu().addActionListenerToButton("New Game", e -> {
            System.out.println("New Game button clicked");
            showNameInputDialog();
        });
        wnd.getMenu().addActionListenerToButton("Load Game", e -> {
            System.out.println("Load Game button clicked");
            handleButtonAction("Load Game");
        });
        wnd.getMenu().addActionListenerToButton("Exit", e -> {
            System.out.println("Exit button clicked");
            handleButtonAction("Exit");
        });
        wnd.getMenu().addSettingsActionListener(e -> {
            System.out.println("Settings button clicked");
        });
    }

    private void loadGameSession() {
        List<GameSession> sessions = dbManager.loadAllSessions();
        if (sessions == null || sessions.isEmpty()) {
            JOptionPane.showMessageDialog(wnd.getWndFrame(), "No saved sessions found!", "Error", JOptionPane.ERROR_MESSAGE);
            return;
        }

        LoadGamePanel loadPanel = new LoadGamePanel(sessions, selectedSession -> {
            if (selectedSession.getLevel() < 1 || selectedSession.getLevel() > 3) {
                JOptionPane.showMessageDialog(wnd.getWndFrame(), "Invalid level in saved session!", "Error", JOptionPane.ERROR_MESSAGE);
                return;
            }
            try {
                Assets.Init(selectedSession.getLevel());
                TileFactory tileFactory = new TileFactory();
                tileFactory.clearCache();
                gameMap = null;
                if (selectedSession.getLevel() == 1) {
                    gameMap = new GameMap("src/PaooGame/LEVEL1MAP.txt", tileFactory, 1);
                } else if (selectedSession.getLevel() == 2) {
                    gameMap = new GameMap("src/PaooGame/LEVEL2MAP.txt", tileFactory, 2);
                } else {
                    gameMap = new GameMap("src/PaooGame/Level3MAP.txt", tileFactory, 3);
                }
            } catch (IOException e) {
                System.err.println("Failed to load game map: " + e.getMessage());
                JOptionPane.showMessageDialog(wnd.getWndFrame(), "Failed to load game map", "Error", JOptionPane.ERROR_MESSAGE);
                return;
            }
            if (gameMap.isWalkable(selectedSession.getPlayerX() / Mihai.getSize(), selectedSession.getPlayerY() / Mihai.getSize())) {
                playerName = selectedSession.getPlayerName();
                Mihai.respawn(selectedSession.getPlayerX(), selectedSession.getPlayerY());
                Mihai.health = selectedSession.getHealth();
                Mihai.score = selectedSession.getScore();
                currentLevel = selectedSession.getLevel();
                resetEntitiesForLevel(currentLevel);
                camera.update(Mihai);
                wnd.hideMenu();
                hidePauseMenu();
                System.out.println("Loaded session: x=" + selectedSession.getPlayerX() + ", y=" + selectedSession.getPlayerY() +
                        ", level=" + selectedSession.getLevel() + ", health=" + selectedSession.getHealth());
                canvasReady = false; // Reset canvas readiness
                SwingUtilities.invokeLater(() -> {
                    wnd.getWndFrame().getContentPane().removeAll();
                    wnd.getWndFrame().add(wnd.GetCanvas(), BorderLayout.CENTER);
                    wnd.getWndFrame().revalidate();
                    wnd.getWndFrame().repaint();
                    while (!wnd.GetCanvas().isDisplayable()) {
                        try {
                            Thread.sleep(10);
                        } catch (InterruptedException ex) {
                            ex.printStackTrace();
                        }
                    }
                    wnd.GetCanvas().createBufferStrategy(3);
                    wnd.GetCanvas().requestFocusInWindow();
                    canvasReady = true; // Mark canvas as ready
                });
            } else {
                JOptionPane.showMessageDialog(wnd.getWndFrame(), "Invalid position in saved session!", "Error", JOptionPane.ERROR_MESSAGE);
            }
        }, wnd.getMenu(), pauseMenu, dbManager);

        wnd.getWndFrame().getContentPane().removeAll();
        wnd.getWndFrame().add(loadPanel, BorderLayout.CENTER);
        wnd.getWndFrame().revalidate();
        wnd.getWndFrame().repaint();
    }

    private void handleButtonAction(String buttonText) {
        System.out.println("Handling button: " + buttonText + ", isPaused: " + isPaused);
        switch (buttonText) {
            case "Load Game":
                loadGameSession();
                break;
            case "Exit":
                System.exit(0);
                break;
        }
    }

    /*! \fn public synchronized void start()
        \brief Creaza si starteaza firul separat de executie (thread).

        Metoda trebuie sa fie declarata synchronized pentru ca apelul acesteia sa fie semaforizat.
     */
    public synchronized void StartGame()
    {
        if (runState == false)
        {
            /// Se actualizeaza flagul de stare a threadului
            runState = true;
            /// Se construieste threadul avand ca parametru obiectul Game. De retinut faptul ca Game class
            /// implementeaza interfata Runnable. Threadul creat va executa functia run() suprascrisa in clasa Game.
            gameThread = new Thread(this);
            /// Threadul creat este lansat in executie (va executa metoda run())
            gameThread.start();
        }
        else
        {
            /// Thread-ul este creat si pornit deja
            return;
        }
    }

    /*! \fn public synchronized void stop()
        \brief Opreste executie thread-ului.

        Metoda trebuie sa fie declarata synchronized pentru ca apelul acesteia sa fie semaforizat.
     */
    public synchronized void StopGame()
    {
        if (runState == true)
        {
            /// Actualizare stare thread
            runState = false;
            /// Metoda join() arunca exceptii motiv pentru care trebuie incadrata intr-un block try - catch.
            try
            {
                /// Metoda join() pune un thread in asteptare panca cand un altul isi termina executie.
                /// Totusi, in situatia de fata efectul apelului este de oprire a threadului.
                gameThread.join();
            }
            catch (InterruptedException ex)
            {
                /// In situatia in care apare o exceptie pe ecran vor fi afisate informatii utile pentru depanare.
                ex.printStackTrace();
            }
        }
        else
        {
            /// Thread-ul este oprit deja.
            return;
        }
    }

    private void showPauseMenu() {
        SwingUtilities.invokeLater(() -> {
            try {
                System.out.println("Attempting to show pause menu");
                pauseMenu = new PauseMenu(
                        this::saveGameSession,
                        this::loadGameSession,
                        () -> System.exit(0),
                        this::hidePauseMenu
                );
                wnd.getWndFrame().getContentPane().removeAll();
                wnd.getWndFrame().add(pauseMenu, BorderLayout.CENTER);
                wnd.getWndFrame().revalidate();
                wnd.getWndFrame().repaint();
                pauseMenu.setFocusable(true);
                pauseMenu.requestFocusInWindow();
                System.out.println("Pause menu should be visible now");
            } catch (Exception e) {
                e.printStackTrace();
                JOptionPane.showMessageDialog(wnd.getWndFrame(), "Error showing pause menu: " + e.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
            }
        });
    }

    private void hidePauseMenu() {
        SwingUtilities.invokeLater(() -> {
            try {
                if (pauseMenu != null) {
                    System.out.println("Hiding pause menu: isPaused = " + isPaused);
                    wnd.getWndFrame().getContentPane().remove(pauseMenu);
                    pauseMenu = null;
                    isPaused = false;
                    wnd.getWndFrame().add(wnd.GetCanvas(), BorderLayout.CENTER);
                    wnd.getWndFrame().revalidate();
                    wnd.getWndFrame().repaint();
                    while (!wnd.GetCanvas().isDisplayable()) {
                        try {
                            Thread.sleep(10);
                        } catch (InterruptedException ex) {
                            ex.printStackTrace();
                        }
                    }
                    wnd.GetCanvas().createBufferStrategy(3);
                    wnd.GetCanvas().requestFocusInWindow();
                    canvasReady = true; // Mark canvas as ready
                    System.out.println("Pause menu hidden: isPaused = " + isPaused);
                }
            } catch (Exception e) {
                e.printStackTrace();
                JOptionPane.showMessageDialog(wnd.getWndFrame(), "Error hiding pause menu: " + e.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
            }
        });
    }

    private void loadLevel(String filename, int level) {
        try {
            Assets.Init(level);
            TileFactory tileFactory = new TileFactory();
            tileFactory.clearCache();
            gameMap = new GameMap(filename, tileFactory, level);
            currentLevel = level;
            resetEntitiesForLevel(level);
            Mihai.NextLevelRespawn(200, 400);
            Mihai.health = 300;
            System.out.println("Loaded level: " + level + " with map: " + filename);
            wnd.GetCanvas().repaint();
        } catch (IOException e) {
            System.err.println("Failed to load game map: " + e.getMessage());
            JOptionPane.showMessageDialog(wnd.getWndFrame(), "Failed to load game map", "Error", JOptionPane.ERROR_MESSAGE);
            System.exit(1);
        }
    }

    /*! \fn private void Update()
        \brief Actualizeaza starea elementelor din joc.

        Metoda este declarata privat deoarece trebuie apelata doar in metoda run()
     */
    private void Update() {
        if (wnd.isMenuShowing() || showingStory) {
            return;
        }

        // Handle escape key for pause menu
        if (wnd.keys[4]) { // Escape key
            System.out.println("Escape key detected in Update. isPaused: " + isPaused);
            if (!isPaused) {
                showPauseMenu();
                isPaused = true;
            } else {
                hidePauseMenu();
//                isPaused = false;
            }
            wnd.keys[4] = false; // Prevent repeated toggling
            return; // Skip other updates this frame
        }

        // Only update game logic if not paused
        if (!isPaused) {
            // Verifica daca jucatorul a atins cactusul (tile-ul cu ID-ul 9)
            int playerTileX = Mihai.getX() / Mihai.getSize();
            int playerTileY = Mihai.getY() / Mihai.getSize();
            if (gameMap.isCactus(playerTileX, playerTileY)) {
                if (currentLevel == 1) {
                    showLevelStory(1); // Show story after Level 1
                } else if (currentLevel == 2) {
                    showLevelStory(2); // Show story after Level 2
                } else if (currentLevel == 3) {
                    showLevelStory(4); // Show final story after Level 3
                }
                return; // Skip further updates until story is dismissed
            }

            // Update camera position
            camera.update(Mihai);

            // Handle player physics and movement
            if (gameMap != null) {
                // Gravity and ground detection
                if (!Mihai.onGround) {
                    Mihai.move(0, Mihai.gravity++, gameMap);
                }

                int PlayerX = Mihai.getX();
                if (Mihai.getX() % Mihai.getSize() > 8 &&
                        !gameMap.isWalkable(Mihai.getX() / Mihai.getSize() + 1, Mihai.getY() / Mihai.getSize() + 1)) {
                    PlayerX += 32 - Mihai.getX() % Mihai.getSize();
                }

                if (!gameMap.isWalkable(PlayerX / Mihai.getSize(), Mihai.getY() / Mihai.getSize() + 1)) {
                    if (Mihai.gravity >= 0) {
                        Mihai.onGround = true;
                        Mihai.move(0, -Mihai.getY() % Mihai.getSize(), gameMap);
                        Mihai.gravity = 0;
                    }
                } else {
                    Mihai.onGround = false;
                }

                Mihai.isMoving = false;
            }
            if (!Mihai.dead) {
                if (wnd.keys[1]) {
                    Mihai.move(Mihai.speed, 0, gameMap);
                    Mihai.isMoving = true;
                    Mihai.facingRight = true;
                }
                if (wnd.keys[2]) {
                    Mihai.move(-Mihai.speed, 0, gameMap);
                    Mihai.isMoving = true;
                    Mihai.facingRight = false;
                }
                if (wnd.keys[3]) {
                    if (Mihai.onGround) {
                        Mihai.onGround = false;
                        Mihai.gravity = -14;
                        SoundPlayer.playSound("/sounds/jump3.wav");
                    }
                }
                if (wnd.keys[5]) {
                    if (!Mihai.attacking && Mihai.attackCooldown == 0) {
                        Mihai.attacking = true;
                        Mihai.attackCooldown = Mihai.maxAttackCooldown;
                        if (Mihai.facingRight) {
                            Mihai.move(10, 0, gameMap);
                        } else {
                            Mihai.move(-10, 0, gameMap);
                        }
                        SoundPlayer.playSound("/sounds/attack.wav");
                    }
                }
                if (wnd.keys[6]) {
                    if (!Mihai.equipBlindfold && !Mihai.removeBlindfold) {
                        Mihai.blindfoldFrameIndex = 0;
                        if (Mihai.blindfolded) {
                            Mihai.removeBlindfold = true;
                        } else {
                            Mihai.equipBlindfold = true;
                        }
                        SoundPlayer.playSound("/sounds/blindfold.wav");
                    }
                }
                if (wnd.keys[7]) {
                    if (Mihai.onGround && Mihai.attackCooldown == 0) {
                        Mihai.isSliding = true;
                        Mihai.attackCooldown = Mihai.maxAttackCooldown;
                        if (Mihai.facingRight) {
                            Mihai.move(40, 0, gameMap);
                        } else {
                            Mihai.move(-40, 0, gameMap);
                        }
                        SoundPlayer.playSound("/sounds/slide.wav");
                    }
                }
            }

            Mihai.updateWalkAnimation(Mihai.isMoving);
            Mihai.updateJumpAnimation(Mihai.onGround);
            if (gameMap.isFloor(Mihai.getX() / Mihai.getSize(), Mihai.getY() / Mihai.getSize() + 1)) {
                Mihai.health -= 100;
                if (Mihai.health > 0) {
                    Mihai.respawn(Mihai.getX() - 32, 300);
                }
            }
        } else {
            System.out.println("Game paused, skipping normal updates");
        }
        if (Mihai.health <= 0) {
            Mihai.dead = true;
        }
        entityManager.updateAll(gameMap);
        for (Fog f : FogList) {
            f.update(gameMap);
        }
        gameMap.update();
    }

    /*! \fn private void Draw()
        \brief Deseneaza elementele grafice in fereastra coresponzator starilor actualizate ale elementelor.

        Metoda este declarata privat deoarece trebuie apelata doar in metoda run()
     */
    private void Draw() throws IOException {
        try {
            Canvas canvas = wnd.GetCanvas();
            if (canvas == null || !canvas.isDisplayable() || !canvas.isShowing() || !canvasReady) {
                System.out.println("Cannot draw: Canvas not ready");
                return; // Skip drawing if canvas isn't ready
            }
            /// Returnez bufferStrategy pentru canvasul existent
            bs = canvas.getBufferStrategy();
            /// Verific daca buffer strategy a fost construit sau nu
            if (bs == null) {
                /// Se executa doar la primul apel al metodei Draw()
                /// Se construieste triplul buffer
                canvas.createBufferStrategy(3);
                System.out.println("Created new buffer strategy");
                return; // Wait for next frame to draw
            }

            /// Se obtine contextul grafic curent in care se poate desena.
            g = bs.getDrawGraphics();
            /// Se sterge ce era
            g.clearRect(0, 0, wnd.GetWndWidth(), wnd.GetWndHeight());

            if (gameMap != null) {
                gameMap.render(g, camera, Mihai);
            }
            entityManager.renderAll(g, camera);
            for (Fog f : FogList) {
                f.render(g, camera);
            }
            overlay.render(g, camera);
            healthBar.render(g, camera);
            // end operatie de desenare
            /// Se afiseaza pe ecran
            bs.show();

            /// Elibereaza resursele de memorie aferente contextului grafic curent
            g.dispose();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}