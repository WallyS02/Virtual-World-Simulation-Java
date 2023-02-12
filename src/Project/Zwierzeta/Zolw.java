package Project.Zwierzeta;

import Project.*;

import javax.swing.*;

public class Zolw extends Zwierze {
    public Zolw(Swiat swiat, Punkt pozycja, int turaUrodzenia) {
        super(TypOrganizmu.ZOLW, swiat, pozycja, turaUrodzenia, 2, 1);
        this.setZasiegRuchu(1);
        this.setSzansaWykonywaniaRuchu(0.25);
        setIkona(new ImageIcon("Zolw.png"));
    }
    @Override
    public String TypOrganizmuToString() {
        return "Zolw";
    }

    @Override
    public boolean SpecjalneDzialaniePodczasAtaku(Organizm atakujacy, Organizm ofiara) {
        if (this == ofiara) {
            if (atakujacy.getSila() < 5 && atakujacy.CzyJestZwierzeciem()) {
                Komentator.DodajKomentarz(OrganizmToSring() + " odpiera atak " + atakujacy.OrganizmToSring());
                return true;
            } else return false;
        }
        else {
            if (atakujacy.getSila() >= ofiara.getSila()) return false;
            else {
                if (ofiara.getSila() < 5 && ofiara.CzyJestZwierzeciem()) {
                    Komentator.DodajKomentarz(OrganizmToSring() + " odpiera atak " + ofiara.OrganizmToSring());
                    return true;
                } else return false;
            }
        }
    }
}

