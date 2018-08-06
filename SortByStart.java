import java.util.Comparator;
class SortByStart implements Comparator<Record>
{
    public int compare(Record a, Record b)
    {
        int high = (a.start >>> 30) - (b.start >>> 30);
        int low = (a.start & 0x3fffffff) - (b.start & 0x3fffffff);
        if(high != 0)
            return high;
        return low;
    }
}
