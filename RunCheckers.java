package org.cis120.checkers;

import java.awt.*;
import java.awt.event.*;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;

import javax.swing.*;

public class RunCheckers implements Runnable {
    public void run() {
        // NOTE: the 'final' keyword denotes immutability even for local variables.

        // Top-level frame in which game components live
        final JFrame frame = new JFrame("Checkers");
        frame.setLocation(300, 100);

        // Status panel
        final JPanel status_panel = new JPanel();
        frame.add(status_panel, BorderLayout.SOUTH);
        final JLabel status = new JLabel("Setting up...");
        status_panel.add(status);

        // Game board
        final GameBoard board = new GameBoard(status);
        frame.add(board, BorderLayout.CENTER);

        // Reset button
        final JPanel control_panel = new JPanel();
        frame.add(control_panel, BorderLayout.NORTH);

        // Note here that when we add an action listener to the reset button, we
        // define it as an anonymous inner class that is an instance of
        // ActionListener with its actionPerformed() method overridden. When the
        // button is pressed, actionPerformed() will be called.
        final JButton reset = new JButton("Reset");
        reset.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                board.reset();
            }
        });
        control_panel.add(reset);

        // undo button
        final JButton undo = new JButton("Undo");
        undo.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                board.undo();
            }
        });
        control_panel.add(undo);

        final JButton load = new JButton("Load");
        load.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                board.load();
            }
        });
        control_panel.add(load);

        frame.addWindowListener(new WindowListener() {

            public void windowClosing(WindowEvent we) {
                try {
                    File file = new File("src/main/java/org/cis120/checkers/save.txt");
                    FileWriter w = new FileWriter(file);
                    w.write(board.getCheckers().getCurrentPlayer() + " ");
                    w.write(board.getCheckers().getGameOver() + " ");
                    w.write(board.getCheckers().getNumTurns() + " ");
                    for (int i = 0; i < 8; i++) {
                        for (int j = 0; j < 8; j++) {
                            Square s = board.getCheckers().getCell(i, j);
                            w.write(s.getContainsPiece() + " ");
                            w.write(s.getPlayer() + " ");
                            w.write(s.getIsKing() + " ");
                        }
                    }
                    w.close();
                } catch (IOException e) {
                    return;
                }
            }

            @Override
            public void windowOpened(WindowEvent e) {
                return;

            }

            @Override
            public void windowClosed(WindowEvent e) {
                return;

            }

            @Override
            public void windowIconified(WindowEvent e) {
                return;

            }

            @Override
            public void windowDeiconified(WindowEvent e) {
                return;

            }

            @Override
            public void windowActivated(WindowEvent e) {
                return;

            }

            @Override
            public void windowDeactivated(WindowEvent e) {
                return;

            }
        });

        final JButton instructions = new JButton("Instructions");
        instructions.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent ae) {
                String filePath = "src/main/java/org/cis120/checkers/instructions.txt";
                try {
                    BufferedReader r = new BufferedReader(new FileReader(filePath));
                    String data = "";
                    int lineNumber = 1;
                    while (lineNumber < 10) {
                        data = data + r.readLine() + "\n";
                        lineNumber++;
                    }
                    JOptionPane.showMessageDialog(
                            null, data, "Instructions", JOptionPane.INFORMATION_MESSAGE
                    );
                    r.close();
                } catch (FileNotFoundException e) {
                    throw new IllegalArgumentException();
                } catch (IOException e) {
                    return;
                }

            }
        });
        control_panel.add(instructions);

        // Put the frame on the screen
        frame.pack();
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setVisible(true);

        // Start the game
        board.reset();
    }
}