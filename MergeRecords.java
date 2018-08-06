import java.util.List;
import java.util.ArrayList;
import java.util.BitSet;
import java.util.Queue;
import java.util.LinkedList;
class MergeRecords
{
    public static Record[] merge(Record[] records)
    {
        BitSet[] bitsets = new BitSet[4];
        for(int i = 0; i < 4; i++)
            bitsets[i] = new BitSet(0x40000000);
        for(Record i : records) {
            int high = i.start >>> 30;
            int low = i.start & 0x3fffffff;
            if((low + i.value) > 0x40000000)
                throw new IndexOutOfBoundsException();
            bitsets[high].set(low, low + i.value, true);
        }
        Queue<BitSetElement> queue = new LinkedList<BitSetElement>();
        List<Record> list = new ArrayList<Record>();
        BitSetElement bse;
        Record record;
        for(int i = 0; i < 4; i++)
            queue.offer(new BitSetElement(bitsets[i], i << 30, 0x40000000));
        while((bse = queue.poll()) != null) {
            if(bse.bitset.isEmpty())
                continue;
            if(bse.bitset.cardinality() == bse.value) {
                record = new Record();
                record.cc = "ZZ";
                record.start = bse.start;
                record.value = bse.value;
                list.add(record);
                continue;
            }
            queue.offer(new BitSetElement(bse.bitset.get(0, bse.value >> 1), bse.start, bse.value >> 1));
            queue.offer(new BitSetElement(bse.bitset.get(bse.value >> 1, bse.value), bse.start + (bse.value >> 1), bse.value >> 1));
        }
        return list.toArray(new Record[0]);
    }
}
