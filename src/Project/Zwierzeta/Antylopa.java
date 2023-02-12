package Project.Zwierzeta;

import java.util.Random;
import Project.*;

import javax.swing.*;

public class Antylopa extends Zwierze {
    public Antylopa(Swiat swiat, Punkt pozycja, int turaUrodzenia) {
        super(TypOrganizmu.ANTYLOPA, swiat, pozycja, turaUrodzenia, 4, 4);
        this.setZasiegRuchu(2);
        this.setSzansaWykonywaniaRuchu(1);
        setIkona(new ImageIcon("Antylopa.png"));
    }

    @Override
    public String TypOrganizmuToString() {
        return "Antylopa";
    }

    @Override
    public boolean SpecjalneDzialaniePodczasAtaku(Organizm atakujacy, Organizm ofiara) {
        Random rand = new Random();
        int tmpLosowanie = rand.nextInt(100);
        if (tmpLosowanie < 50) {
            if (this == atakujacy) {
                Komentator.DodajKomentarz(OrganizmToSring() + " ucieka od " + ofiara.OrganizmToSring());
                Punkt tmpPozycja = LosujPoleNiezajete(ofiara.getPozycja());
                if (!tmpPozycja.equals(ofiara.getPozycja()))
                    WykonajRuch(tmpPozycja);
            } else if (this == ofiara) {
                Komentator.DodajKomentarz(OrganizmToSring() + " ucieka od " + atakujacy.OrganizmToSring());
                Punkt tmpPozycja = this.getPozycja();
                WykonajRuch(LosujPoleNiezajete(this.getPozycja()));
                if (getPozycja().equals(tmpPozycja)) {
                    getSwiat().UsunOrganizm(this);
                    Komentator.DodajKomentarz(atakujacy.OrganizmToSring() + " zabija " + OrganizmToSring());
                }
                atakujacy.WykonajRuch(tmpPozycja);
            }
            return true;
        } else return false;
    }
}
