package Project.Rosliny;

import Project.*;

import javax.swing.*;

public class Trawa extends Roslina {
    public Trawa(Swiat swiat, Punkt pozycja, int turaUrodzenia) {
        super(TypOrganizmu.TRAWA, swiat, pozycja, turaUrodzenia, 0, 0);
        setIkona(new ImageIcon("Trawa.png"));
    }

    @Override
    public String TypOrganizmuToString() {
        return "Trawa";
    }
}

