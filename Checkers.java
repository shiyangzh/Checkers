package org.cis120.checkers;

import java.awt.*;

public class Checkers {

    private Square[][] board;
    private Square pieceToMove;
    private boolean player1Turn;
    private boolean gameOver;
    private int numTurns;

    public Checkers() {
        reset();
    }

    public boolean playTurn(Point currentPoint, Point pointToMove) {
        // if the player did not click on a piece
        if (!board[currentPoint.x][currentPoint.y].getContainsPiece()) {
            return false;
        }

        pieceToMove = board[currentPoint.x][currentPoint.y];
        boolean player1Piece = pieceToMove.getPlayer();
        Square capturedPiece = board[pointToMove.x][pointToMove.y];

        // if the game is over
        if (gameOver) {
            return false;
        }

        if (player1Turn && player1Piece) {
            if (!capturedPiece.getContainsPiece()) {
                if (!validMove(currentPoint, pointToMove, player1Piece, pieceToMove.getIsKing())) {
                    // if the player made an illegal move
                    return false;
                }
                // move the piece if there is no piece in the way
                board[currentPoint.x][currentPoint.y] = new Square(false, true, false);
                board[pointToMove.x][pointToMove.y] = pieceToMove;
                // if a piece reaches the end, promote it to a king
                if (pointToMove.y == 0) {
                    board[pointToMove.x][pointToMove.y].promote();
                }
                player1Turn = !player1Turn;
                numTurns++;
                return true;
            } else {
                Point newPoint = new Point(
                        currentPoint.x + 2 * (pointToMove.x - currentPoint.x),
                        currentPoint.y + 2 * (pointToMove.y - currentPoint.y)
                );
                /*
                 * if there is a piece, check that it can be captured, capture it
                 * and move it ahead of that piece
                 */
                if (validMove(pointToMove, newPoint, player1Piece, pieceToMove.getIsKing()) &&
                        capturedPiece.getPlayer() != player1Piece) {
                    board[currentPoint.x][currentPoint.y] = new Square(false, true, false);
                    board[pointToMove.x][pointToMove.y] = new Square(false, true, false);
                    board[newPoint.x][newPoint.y] = pieceToMove;
                    // if a piece reaches the end, promote it to a king
                    if (newPoint.y == 0) {
                        board[newPoint.x][newPoint.y].promote();
                    }
                    if (checkWinner() == 0) {
                        player1Turn = !player1Turn;
                    }
                    numTurns++;
                    return true;
                }
            }
            numTurns++;
        }

        if (!player1Turn && !player1Piece) {
            if (!capturedPiece.getContainsPiece()) {
                if (!validMove(currentPoint, pointToMove, player1Piece, pieceToMove.getIsKing())) {
                    return false;
                }
                board[currentPoint.x][currentPoint.y] = new Square(false, true, false);
                board[pointToMove.x][pointToMove.y] = pieceToMove;
                if (pointToMove.y == 7) {
                    board[pointToMove.x][pointToMove.y].promote();
                }
                player1Turn = !player1Turn;
                numTurns++;
                return true;
            } else {
                Point newPoint = new Point(
                        currentPoint.x + 2 * (pointToMove.x - currentPoint.x),
                        currentPoint.y + 2 * (pointToMove.y - currentPoint.y)
                );
                if (validMove(pointToMove, newPoint, player1Piece, pieceToMove.getIsKing()) &&
                        capturedPiece.getPlayer() != player1Piece) {
                    board[currentPoint.x][currentPoint.y] = new Square(false, true, false);
                    board[pointToMove.x][pointToMove.y] = new Square(false, true, false);
                    board[newPoint.x][newPoint.y] = pieceToMove;
                    if (newPoint.y == 7) {
                        board[newPoint.x][newPoint.y].promote();
                    }
                    if (checkWinner() == 0) {
                        player1Turn = !player1Turn;
                    }
                    numTurns++;
                    return true;
                }
            }
        }

        return false;
    }

    public int checkWinner() {
        boolean p1CanMove = false;
        boolean p2CanMove = false;

        for (int i = 0; i < board.length; i++) {
            for (int j = 0; j < board[i].length; j++) {
                if (i % 2 != j % 2) {
                    if (board[i][j].getContainsPiece()) {
                        if (board[i][j].getPlayer()) {
                            p1CanMove = p1CanMove || canMove(i, j);
                        } else {
                            p2CanMove = p2CanMove || canMove(i, j);
                        }
                    }
                }

            }
        }

        if (!p2CanMove) {
            gameOver = true;
            return 1;
        } else if (!p1CanMove) {
            gameOver = true;
            return 2;
        }

        return 0;
    }

    public boolean canMove(int i, int j) {
        /*
         * checks that a piece is able to move, used in checkWinner(). In the case that
         * a player has pieces but cannot move them, they will automatically lose.
         */
        Point p = new Point(i, j);
        Point p1 = new Point(i + 1, j + 1);
        Point p2 = new Point(i + 1, j - 1);
        Point p3 = new Point(i - 1, j + 1);
        Point p4 = new Point(i - 1, j - 1);

        boolean p1Piece = board[i][j].getPlayer();
        boolean isKing = board[i][j].getIsKing();
        if (validMove(p, p1, p1Piece, isKing) || validMove(p, p2, p1Piece, isKing) ||
                validMove(p, p3, p1Piece, isKing) || validMove(p, p4, p1Piece, isKing)) {
            return true;
        }
        return false;
    }

    public boolean getCurrentPlayer() {
        return player1Turn;
    }

    public int getNumTurns() {
        return this.numTurns;
    }

    public boolean getGameOver() {
        return this.gameOver;
    }

    public Square getCell(int r, int c) {
        boolean x = board[r][c].getContainsPiece();
        boolean y = board[r][c].getPlayer();
        boolean z = board[r][c].getIsKing();

        Square copy = new Square(x, y, z);
        return copy;
    }

    public Square[][] getBoard() {
        Square[][] copy = new Square[8][8];
        for (int i = 0; i < copy.length; i++) {
            for (int j = 0; j < copy[i].length; j++) {
                copy[i][j] = board[i][j];
            }
        }
        return copy;
    }

    public void setCurrentPlayer(boolean player1) {
        this.player1Turn = player1;
    }

    public void setGameOver(boolean gameOver) {
        this.gameOver = gameOver;
    }

    public void setNumTurns(int num) {
        this.numTurns = num;
    }

    public void setBoard(Square[][] board) {
        this.board = board;
    }

    public boolean validMove(
            Point currentPoint, Point pointToMove,
            boolean player1Piece, boolean isKing
    ) {
        if (!(pointToMove.x < 8 && pointToMove.x >= 0 && pointToMove.y < 8 && pointToMove.y >= 0)) {
            // check that the point to move to is in the board
            return false;

        } else if (pointToMove.x % 2 == pointToMove.y % 2) {
            // check that the piece is moving diagonally
            return false;

        } else if ((Math.abs(currentPoint.x - pointToMove.x) > 1) ||
                (Math.abs(currentPoint.y - pointToMove.y) > 1)) {
            // check that the piece is moving 1 diagonal space (2 if capturing)
            return false;

        } else if (board[pointToMove.x][pointToMove.y].getContainsPiece()) {
            // check if square is occupied
            return false;

        } else if (player1Piece && !isKing && currentPoint.y < pointToMove.y) {
            // if the piece belongs to player 1 and is not a king, check that it moves
            // forward
            return false;

        } else if (!player1Piece && !isKing && currentPoint.y > pointToMove.y) {
            // if the piece belongs to player 2 and is not a king, check that it moves
            // forward
            return false;

        } else {
            // check that the player is not moving to the same position
            return !(currentPoint.x == pointToMove.x && currentPoint.y == pointToMove.y);
        }
    }

    public void reset() {
        board = new Square[8][8];
        player1Turn = true;
        gameOver = false;
        numTurns = 0;

        for (int i = 0; i < board.length; i++) {
            for (int j = 0; j < board[i].length; j++) {
                board[i][j] = new Square(false, true, false);
            }
        }

        for (int i = 0; i < board.length; i++) {
            for (int j = 5; j < board[i].length; j++) {
                if (i % 2 != j % 2) {
                    board[i][j] = new Square(true, true, false);
                }
            }
        }

        for (int i = 0; i < 8; i++) {
            for (int j = 0; j < 3; j++) {
                if (i % 2 != j % 2) {
                    board[i][j] = new Square(true, false, false);
                }
            }
        }
    }

    public void undo(Square[][] board) {
        setBoard(board);
        player1Turn = !player1Turn;
        numTurns--;
    }

    public void load(String[] data) {
        setCurrentPlayer(Boolean.valueOf(data[0]));
        setGameOver(Boolean.valueOf(data[1]));
        setNumTurns(Integer.parseInt(data[2]));

        Square[][] oldBoard = new Square[8][8];
        int index = 3;
        for (int i = 0; i < oldBoard.length; i++) {
            for (int j = 0; j < oldBoard[i].length; j++) {
                oldBoard[i][j] = new Square(
                        Boolean.valueOf(data[index]),
                        Boolean.valueOf(data[index + 1]),
                        Boolean.valueOf(data[index + 2])
                );
                index += 3;
            }
        }
        setBoard(oldBoard);
    }

    public void drawBoard(Graphics g) {
        for (int i = 0; i < board.length; i++) {
            for (int j = 0; j < board[i].length; j++) {
                if (i % 2 != j % 2) {
                    g.setColor(Color.DARK_GRAY);
                    g.fillRect(i * 100, j * 100, 100, 100);
                    board[i][j].draw(g, i * 100, j * 100);
                }

            }
        }
    }
}
