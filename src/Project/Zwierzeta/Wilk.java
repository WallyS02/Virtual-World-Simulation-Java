package Project.Zwierzeta;

import Project.*;

import javax.swing.*;

public class Wilk extends Zwierze {
    public Wilk(Swiat swiat, Punkt pozycja, int turaUrodzenia) {
        super(TypOrganizmu.WILK, swiat, pozycja, turaUrodzenia, 9, 5);
        this.setZasiegRuchu(1);
        this.setSzansaWykonywaniaRuchu(1);
        setIkona(new ImageIcon("Wilk.png"));
    }
    @Override
    public String TypOrganizmuToString() {
        return "Wilk";
    }
}