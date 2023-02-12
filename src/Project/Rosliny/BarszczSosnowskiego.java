package Project.Rosliny;

import java.util.Random;
import Project.*;

import javax.swing.*;

public class BarszczSosnowskiego extends Roslina {
    public BarszczSosnowskiego(Swiat swiat, Punkt pozycja, int turaUrodzenia) {
        super(TypOrganizmu.BARSZCZ_SOSNOWSKIEGO, swiat, pozycja,
                turaUrodzenia, 10, 0);
        setIkona(new ImageIcon("BarszczSosnowskiego.png"));
        setSzansaRozmnazania(0.05);
    }

    @Override
    public void Akcja() {
        int pozX = getPozycja().getX();
        int pozY = getPozycja().getY();
        LosujPoleDowolne(getPozycja());
        for (int i = 0; i < 4; i++) {
            Organizm tmpOrganizm = null;
            if (i == 0 && !CzyKierunekZablokowany(Kierunek.DOL))
                tmpOrganizm = getSwiat().CoZnajdujeSieNaPolu(new Punkt(pozX, pozY + 1));
            else if (i == 1 && !CzyKierunekZablokowany(Kierunek.GORA))
                tmpOrganizm = getSwiat().CoZnajdujeSieNaPolu(new Punkt(pozX, pozY - 1));
            else if (i == 2 && !CzyKierunekZablokowany(Kierunek.LEWO))
                tmpOrganizm = getSwiat().CoZnajdujeSieNaPolu(new Punkt(pozX - 1, pozY));
            else if (i == 3 && !CzyKierunekZablokowany(Kierunek.PRAWO))
                tmpOrganizm = getSwiat().CoZnajdujeSieNaPolu(new Punkt(pozX + 1, pozY));

            if (tmpOrganizm != null && tmpOrganizm.CzyJestZwierzeciem()) {
                getSwiat().UsunOrganizm(tmpOrganizm);
                Komentator.DodajKomentarz(OrganizmToSring() + " zabija " + tmpOrganizm.OrganizmToSring());
            }
        }
        Random rand = new Random();
        int tmpLosowanie = rand.nextInt(100);
        if (tmpLosowanie < getSzansaRozmnazania() * 100) Rozprzestrzenianie();
    }

    @Override
    public String TypOrganizmuToString() {
        return "Barszcz Sosnowskiego";
    }

    @Override
    public boolean SpecjalneDzialaniePodczasAtaku(Organizm atakujacy, Organizm ofiara) {
        if (atakujacy.getSila() >= 10) {
            getSwiat().UsunOrganizm(this);
            Komentator.DodajKomentarz(atakujacy.OrganizmToSring() + " zjada " + this.OrganizmToSring());
            atakujacy.WykonajRuch(ofiara.getPozycja());
        }
        if (atakujacy.CzyJestZwierzeciem() || atakujacy.getSila() < 10) {
            getSwiat().UsunOrganizm(atakujacy);
            Komentator.DodajKomentarz(this.OrganizmToSring() + " zabija " + atakujacy.OrganizmToSring());
        }
        return true;
    }
}

