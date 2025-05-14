package PaooGame;

import PaooGame.GameWindow.GameWindow;
import PaooGame.Graphics.Assets;
import PaooGame.Graphics.HealthBar;
import PaooGame.Tiles.Tile;
import PaooGame.Tiles.TileFactory;

import javax.swing.*;
import java.awt.*;
import java.awt.event.KeyEvent;
import java.awt.image.BufferStrategy;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;

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
    private final GameWindow      wnd;        /*!< Fereastra in care se va desena tabla jocului*/
    private boolean         runState;   /*!< Flag ce starea firului de executie.*/
    private Thread          gameThread; /*!< Referinta catre thread-ul de update si draw al ferestrei*/
    private BufferStrategy  bs;         /*!< Referinta catre un mecanism cu care se organizeaza memoria complexa pentru un canvas.*/
    private GameMap gameMap;
    private Graphics        g;          /*!< Referinta catre un context grafic.*/
    private final Player Mihai = new Player(200,200);
    private HealthBar healthBar = new HealthBar(Mihai);
    private int tileSize = 32;

//    private ArrayList<Enemy> Eagles = new ArrayList<Enemy>(
//            new Enemy(22*tileSize,14*tileSize, Mihai, "Eagle");
//    );
    private ArrayList<Enemy> Eagles = new ArrayList<>(
            Arrays.asList(
                    new Enemy(22 * tileSize, 14 * tileSize, Mihai, "Eagle", true),
                    new Enemy(30 * tileSize, 12 * tileSize, Mihai, "Eagle", false),
                    new Enemy(45 * tileSize, 17 * tileSize, Mihai, "Eagle", true),
                    new Enemy(63 * tileSize, 24 * tileSize, Mihai, "Eagle", false),
                    new Enemy(73 * tileSize, 29 * tileSize, Mihai, "Eagle", false),
                    new Enemy(80 * tileSize, 25 * tileSize, Mihai, "Eagle", false),
                    new Enemy(84 * tileSize, 23 * tileSize, Mihai, "Eagle", false)
            )
    );

    private Camera camera = new Camera(800,480);
    EntityManager entityManager = new EntityManager();

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
    }

    private void InitGame() throws IOException {
//        wnd = new GameWindow(title, width, height);
//        wnd.BuildGameWindow();
//        /// Este construita fereastra grafica.
//        wnd.showMenu();
        /// Se incarca toate elementele grafice (dale)
        Assets.Init();
        setupMenuButtons();

        TileFactory tileFactory = new TileFactory();
        try {
            gameMap = new GameMap("src/PaooGame/Level1MapNEW.txt", tileFactory);
        } catch (IOException e){
            System.err.println("Failed to load game map :"+ e.getMessage());
            JOptionPane.showMessageDialog(wnd.GetCanvas(),"Failed to load game map","Error",JOptionPane.ERROR_MESSAGE);
            System.exit(1);
        }

        entityManager.addEntity(Mihai);
        for(int i=0; i<Eagles.size(); i++){
            entityManager.addEntity(Eagles.get(i));
        }
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
        }catch (IOException e){
            e.printStackTrace();
            return;
        }
        long oldTime = System.nanoTime();   /*!< Retine timpul in nanosecunde aferent frame-ului anterior.*/
        long curentTime;                    /*!< Retine timpul curent de executie.*/

            /// Apelul functiilor Update() & Draw() trebuie realizat la fiecare 16.7 ms
            /// sau mai bine spus de 60 ori pe secunda.

        final int framesPerSecond   = 60; /*!< Constanta intreaga initializata cu numarul de frame-uri pe secunda.*/
        final double timeFrame      = 1000000000 / framesPerSecond; /*!< Durata unui frame in nanosecunde.*/

            /// Atat timp timp cat threadul este pornit Update() & Draw()
        while (runState == true)
        {
                /// Se obtine timpul curent
            curentTime = System.nanoTime();
                /// Daca diferenta de timp dintre curentTime si oldTime mai mare decat 16.6 ms
            if((curentTime - oldTime) > timeFrame)
            {
                if(!wnd.isMenuShowing())
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


    /*! \fn private void init()
    \brief  Metoda construieste fereastra jocului, initializeaza aseturile, listenerul de tastatura etc.

    Fereastra jocului va fi construita prin apelul functiei BuildGameWindow();
    Sunt construite elementele grafice (assets): dale, player, elemente active si pasive.

 */

    private void setupMenuButtons()
    {
//        for(Component comp : wnd.getMenuPanel().getComponents())
//        {
//            if(comp instanceof JButton)
//            {
//                JButton button = (JButton) comp;
//                button.addActionListener(e -> handleButtonAction(button.getText()));
//            }
//        }
        wnd.getMenu().addActionListenerToButton("New Game", e -> handleButtonAction("New Game"));
        wnd.getMenu().addActionListenerToButton("Load Game", e -> handleButtonAction("Load Game"));
        wnd.getMenu().addActionListenerToButton("Exit", e -> handleButtonAction("Exit"));
        wnd.getMenu().addSettingsActionListener(e -> {
            System.out.println("Settings button clicked");
            // Add your settings functionality here
        });
    }

    private void handleButtonAction(String buttonText)
    {
        switch (buttonText)
        {
            case "New Game":
                wnd.hideMenu();
                break;
            case "Load Game":
                System.out.println("Urmeaza de implimentat load-ul");
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
        if(runState == false)
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
        if(runState == true)
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
            catch(InterruptedException ex)
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

    /*! \fn private void Update()
        \brief Actualizeaza starea elementelor din joc.

        Metoda este declarata privat deoarece trebuie apelata doar in metoda run()
     */
    private void Update()
    {
        camera.update(Mihai);

        if(gameMap != null){
            if(!Mihai.onGround){
                Mihai.move(0,Mihai.gravity++, gameMap);
            }
            int PlayerX = Mihai.getX();
            if(Mihai.getX()% Mihai.getSize() > 8 && !gameMap.isWalkable(Mihai.getX()/Mihai.getSize() + 1, Mihai.getY()/Mihai.getSize() + 1)){
                PlayerX += 32;
            }
            if(!gameMap.isWalkable(PlayerX/Mihai.getSize(), Mihai.getY()/Mihai.getSize() + 1)){
                Mihai.onGround = true;
                Mihai.move(0, -Mihai.getY() % Mihai.getSize(), gameMap);
                Mihai.gravity = 0;
            }
            else{
                Mihai.onGround = false;
            }
//            if(!gameMap.isWalkable(Mihai.getX()/Mihai.getSize() +1, Mihai.getY()/Mihai.getSize() + 1)){
//                if(Mihai.getX() % Mihai.getSize() >=30){
//                    Mihai.onGround = true;
//                    Mihai.move(0, -Mihai.getY() % Mihai.getSize(), gameMap);
//                    Mihai.gravity = 0;
//
//                }
//            }

            Mihai.isMoving = false;
        }
        if(!Mihai.dead){
            if(wnd.keys[1]){
                Mihai.move(Mihai.speed, 0, gameMap);
                Mihai.isMoving = true;
                Mihai.facingRight = true;
            }
            if(wnd.keys[2]){
                Mihai.move(-Mihai.speed, 0, gameMap);
                Mihai.isMoving = true;
                Mihai.facingRight = false;
            }
            if(wnd.keys[3]){
                if(Mihai.onGround){
                    Mihai.onGround = false;
                    Mihai.gravity = -14;

                }
            }
            if(wnd.keys[5]){
                if(!Mihai.attacking){
                    Mihai.attacking = true;
                    if(Mihai.facingRight){
                        Mihai.move(10, 0, gameMap);
                    }
                    else{
                        Mihai.move(-10,0,gameMap);
                    }
                }
            }
        }

        Mihai.updateWalkAnimation(Mihai.isMoving);
        Mihai.updateJumpAnimation(Mihai.onGround);
        if(gameMap.isFloor(Mihai.getX()/Mihai.getSize(), Mihai.getY()/Mihai.getSize() + 1)){
                Mihai.respawn(200, 100);
        }
        if(Mihai.health == 0){
            Mihai.dead = true;
        }
        entityManager.updateAll(gameMap);

    }

    /*! \fn private void Draw()
        \brief Deseneaza elementele grafice in fereastra coresponzator starilor actualizate ale elementelor.

        Metoda este declarata privat deoarece trebuie apelata doar in metoda run()
     */
    private void Draw() throws IOException {
        try {
            Canvas canvas = wnd.GetCanvas();
            if (canvas == null) {
                return;
            }
            /// Returnez bufferStrategy pentru canvasul existent
            bs = canvas.getBufferStrategy();
            /// Verific daca buffer strategy a fost construit sau nu
            if (bs == null) {
                /// Se executa doar la primul apel al metodei Draw()
                /// Se construieste tripul buffer
                canvas.createBufferStrategy(3);
                return;

            }

            /// Se obtine contextul grafic curent in care se poate desena.
            g = bs.getDrawGraphics();
            /// Se sterge ce era
            g.clearRect(0, 0, wnd.GetWndWidth(), wnd.GetWndHeight());

            /// operatie de desenare
            // ...............
//            Tile.grassTile.Draw(g, 0 * Tile.TILE_WIDTH, 0);
//            Tile.soilTile.Draw(g, 1 * Tile.TILE_WIDTH, 0);
//            Tile.waterTile.Draw(g, 2 * Tile.TILE_WIDTH, 0);
//            Tile.mountainTile.Draw(g, 3 * Tile.TILE_WIDTH, 0);
//            Tile.treeTile.Draw(g, 4 * Tile.TILE_WIDTH, 0);
//
//            g.drawRect(1 * Tile.TILE_WIDTH, 1 * Tile.TILE_HEIGHT, Tile.TILE_WIDTH, Tile.TILE_HEIGHT);

//        Mihai.addNativeKeyListener();
//        if (keyPressed == KeyEvent.VK_RIGHT) {
////            if (gameMap.isWalkable(M.getX() + 1, player.getY())) {
//                Mihai.move(1, 0,gameMap);
////            }
//        }

//        TileFactory tileFactory = new TileFactory();
//        gameMap = new GameMap("src/PaooGame/Level1.txt", tileFactory);
//        gameMap = new GameMap("src/PaooGame/map.txt", tileFactory);
            if (gameMap != null) {
                gameMap.render(g, camera);
            }
//            Mihai.render(g, camera);
            healthBar.render(g,camera);
            entityManager.renderAll(g,camera);

            // end operatie de desenare
            /// Se afiseaza pe ecran
            bs.show();

            /// Elibereaza resursele de memorie aferente contextului grafic curent (zonele de memorie ocupate de
            /// elementele grafice ce au fost desenate pe canvas).
            g.dispose();
        } catch (Exception e)
        {
            e.printStackTrace();
        }
    }
}

