package Project.Rosliny;

import Project.*;

import javax.swing.*;

public class Guarana extends Roslina {
    public Guarana(Swiat swiat, Punkt pozycja, int turaUrodzenia) {
        super(TypOrganizmu.GUARANA, swiat, pozycja, turaUrodzenia, 0, 0);
        setIkona(new ImageIcon("Guarana.png"));
    }

    @Override
    public String TypOrganizmuToString() {
        return "Guarana";
    }

    @Override
    public boolean SpecjalneDzialaniePodczasAtaku(Organizm atakujacy, Organizm ofiara) {
        Punkt tmpPozycja = this.getPozycja();
        getSwiat().UsunOrganizm(this);
        atakujacy.WykonajRuch(tmpPozycja);
        Komentator.DodajKomentarz(atakujacy.OrganizmToSring() + " zjada " + this.OrganizmToSring() + "  i zwieksza swoja sile na " + 3);
        atakujacy.setSila(atakujacy.getSila() + 3);
        return true;
    }
}

