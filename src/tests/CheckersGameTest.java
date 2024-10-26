package tests;

import main.Main;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

public class CheckersGameTest {
    @Test
    void testBoardInitialization() {
        for (int row = 0; row < 8; row++) {
            for (int col = 0; col < 8; col++) {
                int tileState = Main.getTileState(row, col);
                if (row < 3 && (row + col) % 2 != 0) {
                    assertEquals(1, tileState, "Expected Player 1 piece at (" + row + "," + col + ")");
                } else if (row > 4 && (row + col) % 2 != 0) {
                    assertEquals(-1, tileState, "Expected Player 2 piece at (" + row + "," + col + ")");
                } else {
                    assertEquals(0, tileState, "Expected empty tile at (" + row + "," + col + ")");
                }
            }
        }
    }

    @Test
    void testValidMove() {
        int startX = 2, startY = 1;
        int endX = 3, endY = 2;

        boolean moveResult = Main.movePiece(startX, startY, endX, endY);
        assertTrue(moveResult, "Expected move to be valid from (" + startX + "," + startY + ") to (" + endX + "," + endY + ")");

        assertEquals(1, Main.getTileState(endX, endY), "Expected Player 1 piece at new position (" + endX + "," + endY + ")");
        assertEquals(0, Main.getTileState(startX, startY), "Expected old position (" + startX + "," + startY + ") to be empty");
    }

    @Test
    void testCaptureMechanics() {
        // Setup capture by moving pieces step-by-step
        Main.movePiece(2, 1, 3, 2); // Player 1 moves
        Main.movePiece(5, 4, 4, 3); // Player 2 moves into capture range

        boolean captureResult = Main.movePiece(3, 2, 5, 4); // Player 1 captures Player 2
        assertTrue(captureResult, "Expected capture to be valid from (3,2) to (5,4)");

        assertEquals(1, Main.getTileState(5, 4), "Expected Player 1 piece at new position (5,4)");
        assertEquals(0, Main.getTileState(4, 3), "Expected captured position (4,3) to be empty");
    }

    @Test
    void testPromotePlayer1PieceToKing() {
        //player1's REGULAR PIECE BECOMING KING PIECE
        Main.movePiece(2,1,3,2);
        Main.movePiece(5,4,4,3);
        Main.movePiece(3,2,5,4);
        Main.movePiece(5,2,4,1);
        Main.movePiece(1,0,2,1);
        Main.movePiece(6,1,5,2);
        Main.movePiece(2,1,3,0);
        Main.movePiece(7,2,6,1);
        Main.movePiece(5,4,7,2);

        int player1pieceState = Main.getTileState(7, 2);
        assertEquals(2, player1pieceState, "Expected Player 1 piece at (7,2) to be promoted to king");
    }
    @Test
    void testPromotePlayer2PiecesToKing(){
        //player2's REGULAR PIECE BECOMING KING PIECE
        Main.movePiece(2,1,3,2);
        Main.movePiece(5,4,4,3);
        Main.movePiece(2,3,3,4);
        Main.movePiece(4,3,2,1);
        Main.movePiece(1,4,2,3);
        Main.movePiece(5,0,4,1);
        Main.movePiece(0,3,1,4);
        Main.movePiece(2,1,0,3);

        int player2pieceState = Main.getTileState(0, 3);
        assertEquals(-2, player2pieceState, "Expected Player 2 piece at (0,3) to be promoted to king");
    }

    @Test
    void testEndgameCondition() {
        int endgameStatus = Main.checkEndgame();
        assertEquals(0, endgameStatus, "Expected game to be ongoing at the start");
    }

    @Test
    void testPlayer1WinsAlternateSequence() {
        // Starting position, Player 1 moves first
        Main.movePiece(2,1,3,2);
        Main.movePiece(5,2,4,3);
        Main.movePiece(2,3,3,4);
        Main.movePiece(5,0,4,1);
        Main.movePiece(1,2,2,3);
        Main.movePiece(6,3,5,2);
        Main.movePiece(3,2,5,0);
        Main.movePiece(7,2,6,3);
        Main.movePiece(5,0,7,2);
        Main.movePiece(4,3,3,2);
        Main.movePiece(2,5,3,6);
        Main.movePiece(5,4,4,3);
        Main.movePiece(7,2,5,4);
        Main.movePiece(5,2,4,1);
        Main.movePiece(3,4,5,2);
        Main.movePiece(7,0,6,1);
        Main.movePiece(5,2,7,0);
        Main.movePiece(5,6,4,5);
        Main.movePiece(5,4,6,3);
        Main.movePiece(6,7,5,6);
        Main.movePiece(3,6,5,4);
        Main.movePiece(6,5,4,3);
        Main.movePiece(2,7,3,6);
        Main.movePiece(4,1,3,0);
        Main.movePiece(2,3,4,1);
        Main.movePiece(3,0,2,1);
        Main.movePiece(1,0,3,2);
        Main.movePiece(3,2,5,4);//2nd jump
        Main.movePiece(7,6,6,7);
        Main.movePiece(6,3,5,2);
        Main.movePiece(7,4,6,5);
        Main.movePiece(5,4,7,6);
        Main.movePiece(5,6,4,5);
        Main.movePiece(3,6,5,4);
        Main.movePiece(6,7,5,6);
        Main.movePiece(1,6,2,5);
        Main.movePiece(5,6,4,5);
        Main.movePiece(7,0,6,1);
        Main.movePiece(4,5,3,4);
        Main.movePiece(2,5,4,3);

        // Verify endgame condition
        int endgameStatus = Main.checkEndgame();
        assertEquals(1, endgameStatus, "Expected Player 1 (white) to win the game");
    }
    @Test
    void player2WinsAllAlternateSequence(){
        //starting position player1 moves first (black)
        Main.movePiece(2,1,3,2);
        Main.movePiece(5,2,4,3);
        Main.movePiece(2,3,3,4);
        Main.movePiece(4,3,2,1);
        Main.movePiece(1,4,2,3);
        Main.movePiece(5,6,4,5);
        Main.movePiece(0,3,1,4);
        Main.movePiece(2,1,0,3);
        Main.movePiece(2,3,3,2);
        Main.movePiece(5,4,4,3);
        Main.movePiece(0,1,1,2);
        Main.movePiece(0,3,2,1);
        Main.movePiece(2,7,3,6);
        Main.movePiece(4,5,2,7);
        Main.movePiece(3,2,5,4);
        Main.movePiece(6,5,4,3);
        Main.movePiece(3,4,5,2);
        Main.movePiece(6,1,4,3);
        Main.movePiece(2,5,3,4);
        Main.movePiece(4,3,2,5);
        Main.movePiece(2,5,0,3);//2nd jump
        Main.movePiece(1,0,3,2);
        Main.movePiece(5,0,4,1);
        Main.movePiece(0,5,1,4);
        Main.movePiece(0,3,2,5);
        Main.movePiece(1,6,3,4);
        Main.movePiece(4,1,2,3);
        Main.movePiece(0,7,1,6);
        Main.movePiece(2,7,0,5);
        Main.movePiece(3,4,4,3);
        Main.movePiece(7,0,6,1);
        Main.movePiece(4,3,5,2);
        Main.movePiece(6,1,4,3);

        int endgameStatus = Main.checkEndgame();


        assertEquals(-1, endgameStatus, "Expected Player 2 (black) to win the game");
    }
}
