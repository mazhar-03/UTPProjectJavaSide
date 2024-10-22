package main;

public class Main {
    static{
        System.loadLibrary("libgameLogic");
    }

    public static void main(String[] args) {
        System.out.println("FETCHING!!!");
    }
    public static native int[][] getBoardState();
    public static native boolean movePiece(int startX, int startY, int endX, int endY);
}
