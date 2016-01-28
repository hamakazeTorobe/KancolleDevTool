package jp.gr.java_conf.hamakazeTorobe;

import javax.swing.JFrame;

/**
 * Created by Tomoki on 2016/01/24.
 */
public class Main
{
    public static void main(String[] args)
    {
        JFrame frame = new KancolleTableView("リファクタリングやりました", new KancolleTableController(new KancolleTableModel()));
        frame.setDefaultCloseOperation(3);
        frame.setSize(900, 600);
        frame.setVisible(true);
    }
}