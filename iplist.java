import java.util.Arrays;
class iplist
{
    public static void main(String[] args)
    {
        Record[] records = GetRecords.get();
        records = RecordFilter.notCN(records);
        records = MergeRecords.merge(records);
        Arrays.sort(records, new SortByStart());
        for(Record i : records)
            System.out.println(printIP(i.start) + "/" + (32 - log2(i.value)));
    }
    private static String printIP(int ip) {
        return ((ip >>> 24) & 0xff) + "." + ((ip >>> 16) & 0xff) + "." + ((ip >>> 8) & 0xff) + "." + (ip & 0xff);
    }
    private static int log2(int number)
    {
        int result = 0;
        while((1 << result) != number)
            if(result == 31)
                throw new IndexOutOfBoundsException();
            else
                result++;
        return result;
    }
}
