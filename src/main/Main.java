package main;

import javax.swing.*;

public class Main {
    static{
        System.loadLibrary("libgameLogic");
    }

    public static void main(String[] args) {
//        SwingUtilities.invokeLater(main.CheckersGUI::new);
    }
    public static native int[][] getBoardState();
    public static native boolean movePiece(int startX, int startY, int endX, int endY);
    public static native int checkEndgame();
    public static native int getTileState(int row, int col);
}
