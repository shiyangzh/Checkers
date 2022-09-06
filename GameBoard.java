package org.cis120.checkers;

import java.awt.*;
import java.awt.event.*;
import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.Map;
import java.util.TreeMap;

import javax.swing.*;

@SuppressWarnings("serial")
public class GameBoard extends JPanel {

    private Checkers checkers;
    private JLabel status;
    private Point currentPoint;
    private Map<Integer, Square[][]> previousBoards;

    public static final int BOARD_WIDTH = 800;
    public static final int BOARD_HEIGHT = 800;

    public GameBoard(JLabel statusInit) {

        setBorder(BorderFactory.createLineBorder(Color.BLACK));

        setFocusable(true);

        checkers = new Checkers();
        status = statusInit;

        previousBoards = new TreeMap<Integer, Square[][]>();

        addMouseListener(new MouseAdapter() {
            public void mousePressed(MouseEvent e) {
                currentPoint = e.getPoint();
                currentPoint.x = currentPoint.x / 100;
                currentPoint.y = currentPoint.y / 100;
            }

            public void mouseReleased(MouseEvent e) {
                Point p = e.getPoint();
                p.x = p.x / 100;
                p.y = p.y / 100;

                if (checkers.playTurn(currentPoint, p)) {
                    previousBoards.put(checkers.getNumTurns(), checkers.getBoard());
                }
                updateStatus();
                repaint();
            }
        });
    }

    public void reset() {
        checkers.reset();
        previousBoards.clear();
        status.setText("Player 1's Turn");
        repaint();
        requestFocusInWindow();
    }

    public void undo() {
        if (!previousBoards.containsKey(checkers.getNumTurns())) {
            return;
        } else if (checkers.getNumTurns() > 1) {
            checkers.undo(previousBoards.get(checkers.getNumTurns() - 1));
            updateStatus();
            repaint();
            requestFocusInWindow();
        } else if (checkers.getNumTurns() == 1) {
            reset();
        }
    }

    public void load() {
        String filePath = "src/main/java/org/cis120/checkers/save.txt";
        try {
            BufferedReader r = new BufferedReader(new FileReader(filePath));
            String data = r.readLine();
            String[] words = data.split(" ");
            checkers.load(words);
            r.close();
        } catch (FileNotFoundException e) {
            throw new IllegalArgumentException();
        } catch (IOException e) {
            return;
        }
        updateStatus();
        repaint();
        requestFocusInWindow();
    }

    /**
     * Updates the JLabel to reflect the current state of the game.
     */
    private void updateStatus() {
        if (checkers.getCurrentPlayer()) {
            status.setText("Player 1's Turn");
        } else {
            status.setText("Player 2's Turn");
        }

        int winner = checkers.checkWinner();
        if (winner == 1) {
            status.setText("Player 1 wins!!!");
        } else if (winner == 2) {
            status.setText("Player 2 wins!!!");
        }
    }

    @Override
    public void paintComponent(Graphics g) {
        super.paintComponent(g);
        checkers.drawBoard(g);
    }

    @Override
    public Dimension getPreferredSize() {
        return new Dimension(BOARD_WIDTH, BOARD_HEIGHT);
    }

    public Checkers getCheckers() {
        Checkers copy = new Checkers();
        copy = this.checkers;
        return copy;
    }
}
