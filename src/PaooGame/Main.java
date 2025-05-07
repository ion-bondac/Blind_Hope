package PaooGame;

import PaooGame.GameWindow.GameWindow;

import java.io.IOException;

public class Main
{
    public static void main(String[] args) throws IOException {
        Game paooGame = new Game("PaooGame", 800, 480);
        paooGame.StartGame();
    }
}
//nu merge pentru ca o ia din coltul stanga sus si acolo e doar empty