package PaooGame;

import PaooGame.GameWindow.GameWindow;

public class Main
{
    public static void main(String[] args)
    {
        Game paooGame = new Game("PaooGame", 800, 640);
        paooGame.StartGame();
    }
}
