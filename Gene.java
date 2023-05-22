package gep;

public class Gene implements Cloneable{
    static private int geneLen = 15;
    static private int tailLen = 8;

    private String text;

    /* Constructors */
    public Gene(String str) {
        this.text = str;
        int tail = Param.HEAD_LEN * (Param.FUNC_MAX_PARAM - 1) + 1;
        int gene = Param.HEAD_LEN + tail;
        updateLen(gene, tail);
    }

    /* Private methods */
    private static void updateLen(int gene, int tail) {
        geneLen = gene;
        tailLen = tail;
    }



}
