package jp.gr.java_conf.hamakazeTorobe;
import java.awt.Frame;
import java.awt.event.ActionEvent;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.io.File;
import java.util.Observer;
import javax.swing.*;

/**
 * Created by Tomoki on 2016/01/24.
 */
public class KancolleTableController
        extends WindowAdapter
{
    private KancolleTableModel model;
    private String logFilename = "";
    private String lastFilename = "";
    Action addButtonAction;
    Action clearButtonAction;
    Action readButtonAction;
    Action writeButtonAction;
    Action exitButtonAction;

    Action filenameFieldAction;
    Action flagshipNameFieldAction;
    Action flagshipLvFieldAction;

    Action kaihatsuSuccessComboAction;
    Action equipCategoryComboAction;
    Action equipNamecomboAction;
    Action equipRareComboAction;

    public KancolleTableController(final KancolleTableModel model)
    {
        this.model = model;

        this.addButtonAction = new AbstractAction("追加")
        {
            public void actionPerformed(ActionEvent e) {
                model.addData();
            }
        };
        this.clearButtonAction = new AbstractAction("クリア")
        {
            public void actionPerformed(ActionEvent e) {}
        };
        this.readButtonAction = new AbstractAction("read")
        {
            public void actionPerformed(ActionEvent e)
            {
                JFileChooser fileChooser = new JFileChooser(".");
                fileChooser.setFileSelectionMode(0);
                fileChooser.setDialogTitle("ファイルを選択して下さい");

                int ret = fileChooser.showOpenDialog(null);
                if (ret != 0) {
                    return;
                }
                KancolleTableController.this.logFilename = fileChooser.getSelectedFile().getName();
                KancolleTableController.this.lastFilename = KancolleTableController.this.logFilename;
                model.fileRead(KancolleTableController.this.logFilename);
            }
        };
        this.writeButtonAction = new AbstractAction("Save")
        {
            public void actionPerformed(ActionEvent e) {}
        };
        this.exitButtonAction = new AbstractAction("Exit")
        {
            public void actionPerformed(ActionEvent e)
            {
                System.exit(0);
            }
        };
        this.filenameFieldAction = new AbstractAction()
        {
            public void actionPerformed(ActionEvent e) {
                model.setFilename(((JTextField)e.getSource()).getText());
            }
        };

        this.flagshipNameFieldAction = new AbstractAction() {
            @Override
            public void actionPerformed(ActionEvent e) {
                model.setFlagShipName(((JTextField)e.getSource()).getText());
            }
        };
        this.flagshipLvFieldAction = new AbstractAction() {
            @Override
            public void actionPerformed(ActionEvent e) {
                model.setFlagShipLv(((JTextField)e.getSource()).getText());
            }
        };

        this.kaihatsuSuccessComboAction = new AbstractAction()
        {
            public void actionPerformed(ActionEvent e) {
                //System.out.println(((JComboBox)e.getSource()).getSelectedIndex());
                model.setSelectData("isSuccess",((JComboBox)e.getSource()).getSelectedIndex());
                //model.comboBoxAction();
            }
        };
        this.equipCategoryComboAction = new AbstractAction()
        {
            public void actionPerformed(ActionEvent e) {
                model.setSelectData("equipCategory",((JComboBox)e.getSource()).getSelectedIndex());
                //model.comboBoxAction();
            }
        };
        this.equipNamecomboAction = new AbstractAction()
        {
            public void actionPerformed(ActionEvent e) {
                model.setSelectData("equipName",((JComboBox)e.getSource()).getSelectedIndex());
                //model.comboBoxAction();
            }
        };
        this.equipRareComboAction = new AbstractAction()
        {
            public void actionPerformed(ActionEvent e) {
                model.setSelectData("equipRare",((JComboBox)e.getSource()).getSelectedIndex());
                //model.comboBoxAction();
            }
        };
    }

    public void windowOpened(WindowEvent windowEvent)
    {
        this.model.addObserver((Observer)windowEvent.getSource());
    }

    public void windowClosing(WindowEvent windowEvent)
    {
        this.model.deleteObserver((Observer)windowEvent.getSource());

        ((Frame)windowEvent.getSource()).dispose();
        if (this.model.countObservers() == 0) {
            System.exit(0);
        }
    }
}
