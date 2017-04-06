/**
 * Created by NamNguyen on 4/5/17.
 */
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.Scanner;
import java.util.concurrent.Semaphore;

import javax.swing.*;

public class MoleGameGUI extends JFrame{
    Container pane = getContentPane();
    JLabel label;

    public MoleGameGUI(){
        pane.setLayout(new GridLayout(MoleGame.rowSize + 1, MoleGame.columnSize));
        for(int i = 0; i < MoleGame.totalMoles; i++){
            final int index = i;
            JButton button = new JButton(Integer.toString(i));
            button.addActionListener(new ActionListener() {
                @Override
                public void actionPerformed(ActionEvent e) {
                    if(button.getText().equals("")){
                        MoleGame.miss++;
                        System.out.println("Miss");
                        label.setText("Hit: " + MoleGame.hit + " Miss: " + MoleGame.miss);
                    }
                    else {
                        MoleGame.hit++;
                        System.out.println("Hit");
                        MoleGame.board.set(index, "");
                        label.setText("Hit: " + MoleGame.hit + " Miss: " + MoleGame.miss);
                    }
                    button.setText("");
                }
            });
            pane.add(button);
        }
        label = new JLabel("Hit: " + MoleGame.hit + " Miss: " + MoleGame.miss);
        JButton quit = new JButton("Quit");
        quit.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                MoleGame.playing = false;
            }
        });
        pane.add(label);
        pane.add(quit);
    }

    public static void main(String [] args){
        MoleGame.rowSize = Integer.parseInt(args[0]);
        MoleGame.columnSize = Integer.parseInt(args[1]);
        MoleGame.max_available = Integer.parseInt(args[2]);
        MoleGame.hideLower = Integer.parseInt(args[3]);
        MoleGame.hideUpper = Integer.parseInt(args[4]);
        MoleGame.popUpLower = Integer.parseInt(args[5]);
        MoleGame.popUpUpper = Integer.parseInt(args[6]);

        MoleGame.totalMoles = MoleGame.rowSize * MoleGame.columnSize;
        MoleGame.sem = new Semaphore(MoleGame.max_available);

        MoleGameGUI gui = new MoleGameGUI();
        gui.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        gui.pack();
        gui.setVisible(true);

        //Initialize empty board
        for (int i = 0; i < MoleGame.totalMoles; i++) {
            MoleGame.board.add("");
        }

        //Create mole threads
        char letter = 'a';
        for(int i = 0; i < MoleGame.totalMoles; i++){
            Thread t = new MoleGame(i, letter);
            t.start();
            letter++;
        }

        JButton button;
        while (true){
            for(int i = 0; i < MoleGame.totalMoles; i++) {
                button = (JButton) gui.pane.getComponent(i);
                button.setText(MoleGame.board.get(i));
            }
        }
    }
}
