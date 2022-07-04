package me.tapeline.quailstudio.project;

import me.tapeline.quailstudio.utils.Fonts;

import javax.swing.*;
import java.awt.*;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.io.PrintWriter;
import java.util.Scanner;

public class Console  {
    private final JTextArea console;
    public Process boundProcess;
    public Scanner inputReader;
    public Scanner errorReader;
    public PrintWriter writer;
    public int lastPutOut = 0;
    public Console(JTextArea area) {
        console = area;
        console.addKeyListener(new KeyAdapter() {
            @Override
            public void keyPressed(KeyEvent e){
                int code = e.getKeyCode();
                if (code == KeyEvent.VK_UP || code == KeyEvent.VK_DOWN){
                    e.consume();
                    return;
                } else if (code == KeyEvent.VK_ENTER) {
                    if (writer == null || boundProcess == null || !boundProcess.isAlive())
                        e.consume();
                    String text = console.getText();
                    text = text.substring(lastPutOut, console.getCaretPosition());
                    text = text.substring(text.lastIndexOf('\n') + 1);
                    /*if(Main.)
                        outputArea.append("\n");*/
                    writer.println(text);
                    writer.flush();
                }
            }
        });
    }

    public void bindToProcess(Process process) {
        boundProcess = process;
        new Thread(()->{
            try {
                inputReader = new Scanner(process.getInputStream());
                errorReader = new Scanner(process.getErrorStream());
                writer = new PrintWriter(process.getOutputStream());
                inputReader.useDelimiter("");
                new Thread(()->{
                    while (boundProcess.isAlive()) {
                        while (errorReader.hasNextLine()) {
                            console.append(errorReader.nextLine() + "\n");
                            console.setCaretPosition(console.getText().length());
                            lastPutOut = console.getCaretPosition();
                        }
                    }
                }).start();
                while (boundProcess.isAlive()) {
                    while (inputReader.hasNext()) {
                        console.append(inputReader.next());
                        console.setCaretPosition(console.getText().length());
                        lastPutOut = console.getCaretPosition();
                    }
                }
            }
            catch(Exception e){
                e.printStackTrace();
            }
        }).start();
    }
}
