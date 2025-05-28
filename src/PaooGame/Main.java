package PaooGame;

import java.io.IOException;

public class Main
{
    public static void main(String[] args) throws IOException {
        Game paooGame = new Game("Blind Hope", 800, 480);
        paooGame.StartGame();
    }
}
