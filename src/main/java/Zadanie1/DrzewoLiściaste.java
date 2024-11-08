package Zadanie1;

public class DrzewoLiściaste extends Drzewo {

    public DrzewoLiściaste(String pień, String gałęzie, String liście) {
        super(pień, gałęzie, liście);
    }

    @Override
    void rośnij() {
        System.out.println("Drzewo liściaste rośnie");
    }
}
