package Project.Rosliny;

import java.util.Random;
import Project.*;

import javax.swing.*;

public class WilczeJagody extends Roslina {
    public WilczeJagody(Swiat swiat, Punkt pozycja, int turaUrodzenia) {
        super(TypOrganizmu.WILCZE_JAGODY, swiat, pozycja, turaUrodzenia, 99, 0);
        setIkona(new ImageIcon("WilczeJagody.png"));
        setSzansaRozmnazania(0.05);
    }


    @Override
    public void Akcja() {
        Random rand = new Random();
        int upperbound = 100;
        int tmpLosowanie = rand.nextInt(upperbound);
        if (tmpLosowanie < getSzansaRozmnazania() * 100) Rozprzestrzenianie();
    }

    @Override
    public String TypOrganizmuToString() {
        return "Wilcze jagody";
    }

    @Override
    public boolean SpecjalneDzialaniePodczasAtaku(Organizm atakujacy, Organizm ofiara) {
        Komentator.DodajKomentarz(atakujacy.OrganizmToSring() + " zjada " + this.OrganizmToSring());
        if (atakujacy.getSila() >= 99) {
            getSwiat().UsunOrganizm(this);
            Komentator.DodajKomentarz(atakujacy.OrganizmToSring() + " niszczy krzak wilczej jagody");
        }
        if (atakujacy.CzyJestZwierzeciem()) {
            getSwiat().UsunOrganizm(atakujacy);
            Komentator.DodajKomentarz(atakujacy.OrganizmToSring() + " ginie od wilczej jagody");
        }
        return true;
    }
}