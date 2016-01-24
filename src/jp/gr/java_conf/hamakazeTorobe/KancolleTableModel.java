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
    final String[] infoArray = { "FileName", "FlagShipName", "FlagShipLevel" };
    String[] fileInfo = new String[this.infoArray.length];
    ArrayList<String> dataList = new ArrayList();
    EquipList equipList = new EquipList();
    ArrayList<KanDevRecord> recordList = new ArrayList();
    DefaultTableModel recordTableModel;
    DefaultComboBoxModel equipCategoryCBM;
    DefaultComboBoxModel equipNameCBM;
    DefaultComboBoxModel equipRareCBM;

    Boolean[] isComboChange = {false,false,false,false};

    int[] comboIndexList = {0,0,0,0};

    Object[] record = {"","","",""};

    public void addData(KanDevRecord record)
    {
        this.recordList.add(record);
        notifyObservers();
    }

    public DefaultComboBoxModel getEquipCategoryCBM(){
        return this.equipCategoryCBM;
    }
    public DefaultComboBoxModel getEquipNameCBM(){
        return this.equipNameCBM;
    }
    public DefaultComboBoxModel getEquipRareCBM(){
        return this.equipRareCBM;
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


    public void comboBoxAction(){
        setChanged();
        notifyObservers();
    }

    public void setSelectData(String cbmName,int selectedIndex){
        for(int i = 0; i < isComboChange.length;i++)isComboChange[i] = false;
        if(cbmName == "isSuccess"){
            comboIndexList[0] = selectedIndex;
            isComboChange[0] = true;
        }
        else if(cbmName == "equipCategory"){
            comboIndexList[1] = selectedIndex;
            isComboChange[1] = true;
        }
        else if(cbmName == "equipName"){
            comboIndexList[2] = selectedIndex;
            isComboChange[2] = true;
        }
        else if(cbmName == "equipRare"){
            comboIndexList[3] = selectedIndex;
            isComboChange[3] = true;
        }
        else{
            System.out.println("comboBoxの指定が不正");
        }

        if(comboIndexList[0] == 0){
            equipCategoryCBM = new DefaultComboBoxModel(equipList.equipCategory);
            equipNameCBM = new DefaultComboBoxModel(convertCategory(comboIndexList[1]));
            equipRareCBM = new DefaultComboBoxModel(equipList.rare);
        }
        else{
            equipCategoryCBM = new DefaultComboBoxModel(equipList.defaultSelection);
            equipNameCBM = new DefaultComboBoxModel(equipList.defaultSelection);
            equipRareCBM = new DefaultComboBoxModel(equipList.defaultSelection);
        }

        setChanged();
        notifyObservers();

    }

    private Object[] convertCategory(int index){
        //改善が必要
        if (index == 1)
            return equipList.syuhou;
        if (index == 2)
            return equipList.taikuu;
        if (index == 3)
            return equipList.hukuhou;
        if (index == 4)
            return equipList.gyorai;
        if (index == 5)
            return equipList.kansaiki;
        if (index == 6)
            return equipList.dentan;
        if (index == 7)
            return equipList.kikan;
        if (index == 8)
            return equipList.houdan;
        if (index == 9)
            return equipList.sonar;
        if (index == 10)
            return equipList.bakurai;
        if (index == 11)
            return equipList.kijuu;
        if (index == 12)
            return equipList.etc;
        else{
            return equipList.defaultSelection;
        }
    }
}