package jp.gr.java_conf.hamakazeTorobe;

/**
 * Created by Tomoki on 2016/01/24.
 */
public class KanDevRecord
{
    String isSuccess;
    String name;
    String category;
    String rare;

    public KanDevRecord(String isSuccess, String name, String category, String rare)
    {
        this.isSuccess = isSuccess;
        this.name = name;
        this.category = category;
        this.rare = rare;
    }

    public String getIsSuccess()
    {
        return this.isSuccess;
    }

    public String getName()
    {
        return this.name;
    }

    public String getCategory()
    {
        return this.category;
    }

    public String getRare()
    {
        return this.rare;
    }

    public Object[] getRecord()
    {
        Object[] record = new Object[4];
        record[0] = this.isSuccess;
        record[1] = this.name;
        record[2] = this.category;
        record[3] = this.rare;

        return record;
    }

    public String toString()
    {
        return "record[" + this.isSuccess + "," + this.name + "," + this.category + "," + this.rare + "]";
    }
}
