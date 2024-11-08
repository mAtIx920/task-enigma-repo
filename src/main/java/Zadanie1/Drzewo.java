package Zadanie1;

public abstract class Drzewo {

    private String pień;
    private String gałęzie;
    private String liście;

    public Drzewo(String pień, String gałęzie, String liście) {
        this.pień = pień;
        this.gałęzie = gałęzie;
        this.liście = liście;
    }

    abstract void rośnij();
}
