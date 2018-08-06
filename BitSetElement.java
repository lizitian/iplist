import java.util.BitSet;
class BitSetElement
{
    public BitSet bitset;
    public int start;
    public int value;
    public BitSetElement(BitSet bitset, int start, int value)
    {
        this.bitset = bitset;
        this.start = start;
        this.value = value;
    }
}
