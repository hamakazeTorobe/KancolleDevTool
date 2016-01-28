package jp.gr.java_conf.hamakazeTorobe;
import java.awt.BorderLayout;
import java.awt.Dimension;
import java.io.PrintStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Observable;
import java.util.Observer;
import java.util.TreeSet;
import javax.swing.BoxLayout;
import javax.swing.DefaultComboBoxModel;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.table.DefaultTableModel;

/**
 * Created by Tomoki on 2016/01/24.
 */

public class KancolleTableView
        extends JFrame
        implements Observer
{
    private static final Object[] columnNames = { "試行回数", "結果", "種類", "装備", "レア度" };
    private  final Object[] equipCategory = {
            "装備の種類",
            "主砲", //1
            "主砲(対空砲)", //2
            "副砲", //3
            "魚雷", //4
            "艦載機", //5
            "電探", //6
            "機関", //7
            "砲弾", //8
            "ソナー", //9
            "爆雷", //10
            "対空機銃", //11
            "その他", //12
    };
    private final Object[] DevSuccess = { "成功", "失敗" };
    private final Object[] Rare = { "レア度", "1", "2", "3", "4", "5" };
    private static JPanel panel;
    private static int columnV = columnNames.length;
    private static ArrayList<String> lineList = new ArrayList();
    private static String[][] data;
    private static JButton addButton;
    private static JButton readButton;
    private static JButton writeButton;
    private static JTable table;
    private static DefaultTableModel model;
    private static JComboBox kaihatsuSuccessCombo;
    private static JComboBox equipCategoryCombo;
    private static JComboBox equipNameCombo;
    private static JComboBox rareCombo;
    private static DefaultComboBoxModel cbm0;
    private static DefaultComboBoxModel cbm1;
    private static DefaultComboBoxModel cbm2;
    private static DefaultComboBoxModel cbm3;
    private static JTextField filenameField;
    private static JTextField flagshipField;
    private static JTextField flagshipLvField;
    private static String filename = "null";
    private static String[] firstLine = new String[3];
    private static TreeSet set = new TreeSet();
    private static HashMap<String, Float> failureProb;
    private static HashMap<String, Float> categoryProb;
    private static HashMap<String, Float> equipProb;
    private static HashMap<String, Float> rareProb;
    private static ArrayList<String> failureProbKeyList;
    private static ArrayList<String> categoryProbKeyList;
    private static ArrayList<String> equipProbKeyList;
    private static ArrayList<String> rareProbKeyList;
    private static JTextArea dataText;
    private static JButton clear;

    public KancolleTableView(String title, KancolleTableController controller)
    {
        super(title);
        ImageIcon icon = new ImageIcon("pengin2.png");
        setIconImage(icon.getImage());

        model = new DefaultTableModel(columnNames, 0);
        table = new JTable(model);

        failureProb = new HashMap();
        categoryProb = new HashMap();
        equipProb = new HashMap();
        rareProb = new HashMap();

        failureProbKeyList = new ArrayList();
        categoryProbKeyList = new ArrayList();
        equipProbKeyList = new ArrayList();
        rareProbKeyList = new ArrayList();

        JScrollPane scroll = new JScrollPane(table);
        panel = (JPanel)getContentPane();
        panel.setLayout(new BorderLayout());

        JPanel panel2 = new JPanel();
        panel2.setLayout(new BoxLayout(panel2, 0));
        addButton = new JButton(controller.addButtonAction);
        readButton = new JButton(controller.readButtonAction);
        writeButton = new JButton(controller.writeButtonAction);

        Object[] def = { "-" };
        cbm0 = new DefaultComboBoxModel(this.DevSuccess);
        cbm1 = new DefaultComboBoxModel(this.equipCategory);
        cbm2 = new DefaultComboBoxModel(def);
        cbm3 = new DefaultComboBoxModel(def);

        kaihatsuSuccessCombo = new JComboBox();
        kaihatsuSuccessCombo.setAction(controller.kaihatsuSuccessComboAction);
        kaihatsuSuccessCombo.setModel(cbm0);

        equipCategoryCombo = new JComboBox();
        equipCategoryCombo.setAction(controller.equipCategoryComboAction);
        equipCategoryCombo.setModel(cbm1);

        equipNameCombo = new JComboBox();
        equipNameCombo.setAction(controller.equipNamecomboAction);
        equipNameCombo.setModel(cbm2);
        rareCombo = new JComboBox();
        rareCombo.setAction(controller.equipRareComboAction);
        rareCombo.setModel(cbm3);

        JPanel panel3 = new JPanel();
        panel3.setLayout(new BorderLayout());

        dataText = new JTextArea();
        dataText.setEditable(false);

        JScrollPane scroll2 = new JScrollPane(dataText);

        kaihatsuSuccessCombo.setPreferredSize(new Dimension(1, 10));
        equipCategoryCombo.setPreferredSize(new Dimension(50, 10));
        equipNameCombo.setPreferredSize(new Dimension(80, 10));
        rareCombo.setPreferredSize(new Dimension(1, 10));

        clear = new JButton(controller.clearButtonAction);
        panel2.add(clear);
        panel2.add(addButton);
        panel2.add(kaihatsuSuccessCombo);
        panel2.add(equipCategoryCombo);
        panel2.add(equipNameCombo);
        panel2.add(rareCombo);

        JPanel filePanel = new JPanel();
        filePanel.setLayout(new BoxLayout(filePanel, 0));
        JLabel fileLabel = new JLabel("レシピ名");
        filenameField = new JTextField();
        filenameField.addActionListener(controller.filenameFieldAction);
        JLabel flagshipName = new JLabel("旗艦");
        flagshipField = new JTextField();
        flagshipField.addActionListener(controller.flagshipNameFieldAction);
        JLabel flagshipLv = new JLabel("旗艦Lv");
        flagshipLvField = new JTextField();
        flagshipLvField.addActionListener(controller.flagshipLvFieldAction);

        filePanel.add(fileLabel);
        filePanel.add(filenameField);
        filePanel.add(flagshipName);
        filePanel.add(flagshipField);
        filePanel.add(flagshipLv);
        filePanel.add(flagshipLvField);
        filePanel.add(clear);

        JPanel northPanel = new JPanel();

        northPanel.setLayout(new BoxLayout(northPanel, 1));

        northPanel.add(filePanel);
        northPanel.add(panel2);

        panel3.add(northPanel, "North");
        panel3.add(scroll, "Center");
        panel.setLayout(new BorderLayout());
        panel.add(panel3, "Center");
        scroll2.setPreferredSize(new Dimension(200, 100));
        panel.add(scroll2, "East");

        JMenuBar menuBar = new JMenuBar();
        setJMenuBar(menuBar);
        JMenu fileMenu = new JMenu("File");
        menuBar.add(fileMenu);

        JMenuItem item = new JMenuItem(controller.readButtonAction);
        fileMenu.add(item);
        item = new JMenuItem(controller.writeButtonAction);
        fileMenu.add(item);
        fileMenu.addSeparator();
        item = new JMenuItem(controller.exitButtonAction);
        fileMenu.add(item);

        addWindowListener(controller);
    }

    public void update(Observable o, Object arg)
    {
        //System.out.println("*start update*");
        updateFileInfo(o,arg);
        updateAllCombobox(o,arg);
        updateTable(((KancolleTableModel)o).getRecordTableModel());

    }

    private void updateTable(DefaultTableModel tableModel)
    {
        table.setModel(tableModel);
        table.clearSelection();
    }

    private void calcUpdate(Object[] calcedData)
    {
        dataText.setText("");
        dataText.append("------レア度------\n");
        dataText.append(calcedData[0].toString());
        dataText.append("\n------種類------\n");
        dataText.append(calcedData[1].toString());
        dataText.append("\n------装備名------\n");
        dataText.append(calcedData[2].toString());
    }

    private void updateFileInfo(Observable o, Object arg) {
        String[] fileInfo = ((KancolleTableModel)o).getFileInfo();
        for (String a : fileInfo) {
            System.out.println(a);
        }
        filenameField.setText(fileInfo[0]);
        flagshipField.setText(fileInfo[1]);
        flagshipLvField.setText(fileInfo[2]);
        for (int i = 0; i < ((KancolleTableModel)o).getAllRecord().size(); i++) {
            System.out.println(((KancolleTableModel)o).getAllRecord().get(i));
        }
    }

    private void updateAllCombobox(Observable o,Object arg){
        //変更があったもの以外のコンボボックスを更新
        if(((KancolleTableModel)o).isComboChange[0]){ //開発成否の更新がある場合
            updateEquipCatgoryCombo(o,arg);
            updateEquipNameCombo(o,arg);
            updateRareCombo(o,arg);
        }
        if(((KancolleTableModel)o).isComboChange[1]){ //カテゴリ名の更新がある場合
            updateEquipNameCombo(o,arg);
            updateRareCombo(o,arg);
        }
        if(((KancolleTableModel)o).isComboChange[2]){ //装備名の更新がある場合
            updateRareCombo(o,arg);
        }
    }

    private void updateEquipCatgoryCombo(Observable o, Object arg){
        equipCategoryCombo.setModel(((KancolleTableModel)o).getEquipCategoryCBM());
    }
    private void updateEquipNameCombo(Observable o, Object arg){
        equipNameCombo.setModel(((KancolleTableModel)o).getEquipNameCBM());
    }
    private void updateRareCombo(Observable o, Object arg) {
        rareCombo.setModel(((KancolleTableModel)o).getEquipRareCBM());
    }
}