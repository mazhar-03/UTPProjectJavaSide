import javax.swing.*;

public class Main {
    static {
        System.loadLibrary("libgameLogic");
    }

    public static void main(String[] args) {
        System.out.println("Fetching board state and managing moves with JNI!");
        SwingUtilities.invokeLater(CheckersGUI::new);
    }

    // Fetching the current board state from C++
    public static native int[][] getBoardState();

    // For moving a piece on the board
    public static native boolean movePiece(int startX, int startY, int endX, int endY);
}
