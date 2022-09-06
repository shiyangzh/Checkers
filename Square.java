package org.cis120.checkers;

import java.awt.*;

/**
 * A basic game object starting in the upper left corner of the game court. It
 * is displayed as a circle of a specified color.
 */
public class Square implements Comparable<Square> {

    private int width = 100;
    private int height = 100;
    private boolean containsPiece;
    private boolean player1;
    private boolean isKing;

    public Square(boolean containsPiece, boolean player1, boolean isKing) {
        this.containsPiece = containsPiece;
        this.player1 = player1;
        this.isKing = isKing;
    }

    public int getWidth() {
        return this.width;
    }

    public int getHeight() {
        return this.height;
    }

    public boolean getContainsPiece() {
        return this.containsPiece;
    }

    public boolean getPlayer() {
        return this.player1;
    }

    public boolean getIsKing() {
        return this.isKing;
    }

    public void draw(Graphics g, int px, int py) {
        if (!containsPiece) {
            return;
        }
        if (player1) {
            g.setColor(Color.RED);
        } else {
            g.setColor(Color.BLACK);
        }
        g.fillOval(px, py, this.width, this.height);
        if (this.isKing) {
            g.setColor(Color.WHITE);
            g.setFont(g.getFont().deriveFont((float) 40));
            g.drawString("K", px + 37, py + 62);
        }
    }

    public void promote() {
        this.isKing = true;
    }

    public int compareTo(Square p) {
        if (this.containsPiece && !p.containsPiece) {
            return 1;
        } else if (!this.containsPiece && p.containsPiece) {
            return -1;
        } else if (this.player1 && !p.player1) {
            return 1;
        } else if (!this.player1 && p.player1) {
            return -1;
        } else if (this.isKing && !p.isKing) {
            return 1;
        } else if (!this.isKing && p.isKing) {
            return -1;
        }
        return 0;
    }
}