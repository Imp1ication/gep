package gep;

public class Gene implements Cloneable{
    private String text;

    /* Constructors */
    public Gene() { this.initialize(); }
    public Gene(String str) { this.text = str; }

    /* Setters and getters */
    public String getFragment(int start, int end) { 
        // get substring from start to end-1
        return this.text.substring(start, end);
    }
    public String setFragment(int start, int end, String fragment) { 
        // set substring from start to end-1
        this.text = this.text.substring(0, start) + fragment + this.text.substring(end);
        return this.text;
    }


    /* Public methods */
    public void initialize() {
        this.text = "";
        text += Param.getRandomFunc();

        // random head
        for (int i = 0; i < Param.HEAD_LEN - 1; i++) {
            text += Param.getRandomElem();
        }

        // random tail
        for (int i = 0; i < Param.TAIL_LEN; i++) {
            text += Param.getRandomTerm();
        }

        // random dc
        for (int i = 0; i < Param.TAIL_LEN; i++) {
            text += Param.getRandomDc();
        }
    }
        


    /* Private methods */

    @Override
    public String toString() { return this.text; }

    @Override
    protected Gene clone() throws CloneNotSupportedException {
        return (Gene)super.clone();
    }


}
