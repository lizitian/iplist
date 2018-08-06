import java.util.List;
import java.util.ArrayList;
class RecordFilter
{
    public static Record[] notCN(Record[] records)
    {
        List<Record> list = new ArrayList<Record>();
        for(Record i : records)
            if(!i.cc.equals("CN"))
                list.add(i);
        return list.toArray(new Record[0]);
    }
}
