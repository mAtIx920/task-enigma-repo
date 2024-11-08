package Zadanie1;

public class DrzewoIglaste extends Drzewo {

    public DrzewoIglaste(String pień, String gałęzie, String liście) {
        super(pień, gałęzie, liście);
    }

    @Override
    void rośnij() {
        System.out.println("Drzewo iglaste rośnie");
    }
}
