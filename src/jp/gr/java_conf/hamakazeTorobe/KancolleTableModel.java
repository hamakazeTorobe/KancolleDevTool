package jp.gr.java_conf.hamakazeTorobe;

import java.io.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Observable;
import java.util.TreeSet;
import javax.swing.*;
import javax.swing.table.DefaultTableModel;

/**
 * Created by Tomoki on 2016/01/24.
 */
public class KancolleTableModel
        extends Observable
{
    private final String[] infoArray = { "FileName", "FlagShipName", "FlagShipLv" };
    private final String[] columnNames = {"試行回数","結果","種類","装備","レア度"};
    private final Object[] DevSuccess = { "成功", "失敗" };

    String[] fileInfo = new String[this.infoArray.length];
    ArrayList<String> dataList = new ArrayList();
    EquipList equipList = new EquipList();
    ArrayList<KanDevRecord> recordList = new ArrayList();
    DefaultTableModel recordTableModel;
    DefaultComboBoxModel isSuccessCBM;
    DefaultComboBoxModel equipCategoryCBM;
    DefaultComboBoxModel equipNameCBM;
    DefaultComboBoxModel equipRareCBM;

    Boolean[] isComboChange = {false,false,false,false};

    int[] comboIndexList = {0,0,0,0};

    Object[] record = {"成功","-","-","-"};

    /*----------------------------------------*/
    TreeSet set = new TreeSet();
    HashMap<String, Float> failureProb, categoryProb, equipProb, rareProb;
    ArrayList<String> failureProbKeyList, categoryProbKeyList, equipProbKeyList, rareProbKeyList;
    JTextArea dataText;
    /*----------------------------------------*/

    public KancolleTableModel(){
        isSuccessCBM = new DefaultComboBoxModel(DevSuccess);
        equipCategoryCBM  = new DefaultComboBoxModel(equipList.equipCategory);
        equipNameCBM = new DefaultComboBoxModel(equipList.defaultSelection);
        equipRareCBM = new DefaultComboBoxModel(equipList.defaultSelection);

        recordTableModel = new DefaultTableModel();

        /*----------------------------------------*/
        failureProb = new HashMap<String, Float>();
        categoryProb = new HashMap<String, Float>();
        equipProb = new HashMap<String, Float>();
        rareProb = new HashMap<String, Float>();

        failureProbKeyList = new ArrayList<String>();
        categoryProbKeyList = new ArrayList<String>();
        equipProbKeyList = new ArrayList<String>();
        rareProbKeyList = new ArrayList<String>();

        /*----------------------------------------*/
        setChanged();
        notifyObservers();
    }

    public void addData()
    {
        this.recordList.add(
                new KanDevRecord(
                        record[0].toString(),
                        record[1].toString(),
                        record[2].toString(),
                        record[3].toString())
        );
        setTableData(recordList);
        setChanged();
        notifyObservers();
    }

    public void setFilename(String filename){
        fileInfo[0] = filename;
    }
    public void setFlagShipName(String shipname){
        fileInfo[1] = shipname;
    }
    public void setFlagShipLv(String shipLv){
        fileInfo[2] = shipLv;
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
        setTableData(recordList);

        setChanged();
        notifyObservers();
    }

    public void fileWrite(String filename){
        try {
            PrintWriter writer = new PrintWriter(new BufferedWriter(new FileWriter(filename)));
            String lineW;

            for (int i = 0; i < 3; i++) {//開発レシピ＆旗艦データ書き出し
                writer.println(fileInfo[i]);
            }

            for (int i = 0; i < recordTableModel.getRowCount(); i++) {
                lineW = "";
                for (int j = 0; j < columnNames.length; j++) {

                    if (recordTableModel.getValueAt(i, j) != null)
                        lineW += recordTableModel.getValueAt(i, j);
                    else
                        lineW = "";

                    if (j != columnNames.length - 1)
                        lineW += ",";
                }
                writer.println(lineW);
            }
            writer.close();
        } catch (IOException e) {
            System.out.println(e);
        } catch (ArrayIndexOutOfBoundsException e) {
            System.out.println(e + ":::write");
        }
    }

    public String[] getFileInfo()
    {
        return this.fileInfo;
    }

    public void setSelectData(String cbmName,int selectedIndex){
        for(int i = 0; i < isComboChange.length;i++)isComboChange[i] = false;
        if(cbmName == "isSuccess"){
            comboIndexList[0] = selectedIndex;
            record[0] = DevSuccess[selectedIndex];
            if(selectedIndex == 1) {
                record[1] = "-";
                record[2] = "-";
                record[3] = "-";
            }else{

                record[1] = equipCategoryCBM.getIndexOf(0);
                record[2] = "-";
                record[3] = "-";
            }

           isComboChange[0] = true;
        }
        else if(cbmName == "equipCategory"){
            comboIndexList[1] = selectedIndex;
            record[1] = equipCategoryCBM.getElementAt(selectedIndex);
            record[2] = "-";
            record[3] = "-";
            isComboChange[1] = true;
        }
        else if(cbmName == "equipName"){
            comboIndexList[2] = selectedIndex;
            record[2] = equipNameCBM.getElementAt(selectedIndex);
            record[3] = "-";
            isComboChange[2] = true;
        }
        else if(cbmName == "equipRare"){
            comboIndexList[3] = selectedIndex;
            record[3] = equipRareCBM.getElementAt(selectedIndex);
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

    private void setTableData(ArrayList<KanDevRecord> recList) {

        String[][] tableData = new String[recList.size()][columnNames.length];
        for (int i = 0; i < recList.size(); i++) {
            for (int j = 0; j < columnNames.length - 1; j++) {
                if (j == 0) tableData[i][0] = Integer.toString(i + 1); //試行回数カウント
                tableData[i][j + 1] = recList.get(i).getRecord()[j].toString();//のこり4つのデータ追加
            }
        }
        recordTableModel = new DefaultTableModel(tableData, columnNames);
        for(int i = 0; i < columnNames.length;i++) {
            setDataAnalysis(i);
        }

    }
    public DefaultTableModel getRecordTableModel(){
        return this.recordTableModel;
    }

    public void clearRecord(){
        //record = new Object[]{"-","-","-","-"};
        fileInfo = new String[]{"","",""};
        recordList.clear();
        dataList.clear();
        recordTableModel = new DefaultTableModel();

        for(int i = 0; i < columnNames.length;i++) {
            setDataAnalysis(i);
        }

        setChanged();
        notifyObservers();
    }

    public void setDataAnalysis(int columnNum){
        for (int i = 0; i < recordList.size(); i++) {
            set.add(recordList.get(i).getIndexObject(columnNum));
        }
        int Ssize = set.size();
        int[] setCount = new int[Ssize];
        String[] dataName = new String[Ssize];
        for (int j = 0; j < Ssize; j++) {
            dataName[j] = (String) set.first();
            for (int k = 0; k < recordList.size(); k++) {
                if (recordList.get(k).getIndexObject(columnNum).equals(dataName[j])) {
                    setCount[j]++;
                }
            }
            set.pollFirst();
        }
        switch (columnNum) {
            case 1://成功失敗
                failureProbKeyList.clear();
                failureProb.clear();
                for (int i = 0; i < dataName.length; i++) {
                    if (dataName[i].contains("-"))
                        continue;
                    failureProbKeyList.add(dataName[i]);
                    Float prob = (float) 100 * ((float) setCount[i] / (float) recordList.size());
                    failureProb.put(dataName[i], prob);
                }
                break;
            case 2://装備の種類
                categoryProbKeyList.clear();
                categoryProb.clear();
                for (int i = 0; i < dataName.length; i++) {
                    if (dataName[i].contains("-"))
                        continue;
                    categoryProbKeyList.add(dataName[i]);
                    //System.out.println(categoryProbKeyList);
                    Float prob = (float) 100 * ((float) setCount[i] / (float) recordList.size());
                    categoryProb.put(dataName[i], prob);
                }
                break;
            case 3://装備名
                equipProbKeyList.clear();
                equipProb.clear();
                for (int i = 0; i < dataName.length; i++) {
                    if (dataName[i].contains("-"))
                        continue;
                    equipProbKeyList.add(dataName[i]);
                    Float count = (float) setCount[i];
                    equipProb.put(dataName[i], count);
                }
                break;

            case 4://レア度
                rareProbKeyList.clear();
                rareProb.clear();
                for (int i = 0; i < dataName.length; i++) {
                    if (dataName[i].contains("-")) {
                        dataName[i] = "失敗";
                    }
                    rareProbKeyList.add(dataName[i]);
                    Float prob = (float) 100 * ((float) setCount[i] / (float) recordList.size());
                    rareProb.put(dataName[i], prob);
                }
                break;
        }
    }
}