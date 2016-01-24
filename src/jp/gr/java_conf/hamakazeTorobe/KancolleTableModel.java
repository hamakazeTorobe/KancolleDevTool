package jp.gr.java_conf.hamakazeTorobe;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Observable;
import javax.swing.DefaultComboBoxModel;
import javax.swing.table.DefaultTableModel;

/**
 * Created by Tomoki on 2016/01/24.
 */
public class KancolleTableModel
        extends Observable
{
    private final String[] infoArray = { "FileName", "FlagShipName", "FlagShipLevel" };
    String[] fileInfo = new String[this.infoArray.length];
    ArrayList<String> dataList = new ArrayList();
    EquipList equipList;
    ArrayList<KanDevRecord> recordList = new ArrayList();
    DefaultTableModel recordTableModel;
    DefaultComboBoxModel equipCategoryCBM;
    DefaultComboBoxModel equipNameCBM;

    public void addData(KanDevRecord record)
    {
        this.recordList.add(record);
        notifyObservers();
    }

    public ArrayList<KanDevRecord> getAllRecord()
    {
        return this.recordList;
    }

    public void fileRead(String filename)
    {
        try
        {
            this.recordList.clear();
            BufferedReader in = new BufferedReader(new FileReader(filename));
            for (int i = 0; i < 3; i++)
            {
                String line;
                if ((line = in.readLine()) != null) {
                    this.fileInfo[i] = line;
                }
            }
            String line;
            while ((line = in.readLine()) != null)
            {
                this.dataList.add(line);

                String[] splitRecord = line.split(",");
                this.recordList.add(new KanDevRecord(splitRecord[1], splitRecord[2], splitRecord[3], splitRecord[4]));
            }
            in.close();
        }
        catch (FileNotFoundException e) {}catch (IOException e) {}catch (ArrayIndexOutOfBoundsException e) {}
        setChanged();
        notifyObservers();
    }

    public String[] getFileInfo()
    {
        return this.fileInfo;
    }

    public ArrayList<String> getDataList()
    {
        return this.dataList;
    }
}