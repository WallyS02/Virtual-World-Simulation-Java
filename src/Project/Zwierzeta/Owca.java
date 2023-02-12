package Project.Zwierzeta;

import Project.*;

import javax.swing.*;

public class Owca extends Zwierze {

    public Owca(Swiat swiat, Punkt pozycja, int turaUrodzenia) {
        super(TypOrganizmu.OWCA, swiat, pozycja, turaUrodzenia, 4, 4);
        this.setZasiegRuchu(1);
        this.setSzansaWykonywaniaRuchu(1);
        setIkona(new ImageIcon("Owca.png"));
    }
    @Override
    public String TypOrganizmuToString() {
        return "Owca";
    }
}