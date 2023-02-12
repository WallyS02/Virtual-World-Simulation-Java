package Project.Zwierzeta;

import Project.*;

import javax.swing.*;

public class Czlowiek extends Zwierze {
    private Kierunek kierunekRuchu;
    private int coolDown;

    public Czlowiek(Swiat swiat, Punkt pozycja, int turaUrodzenia) {
        super(TypOrganizmu.CZLOWIEK, swiat, pozycja, turaUrodzenia, 5, 4);
        this.setZasiegRuchu(1);
        this.setSzansaWykonywaniaRuchu(1);
        kierunekRuchu = Kierunek.BRAK_KIERUNKU;
        setIkona(new ImageIcon("Czlowiek.png"));
        coolDown = 0;
    }

    @Override
    protected Punkt ZaplanujRuch() {
        int x = getPozycja().getX();
        int y = getPozycja().getY();
        LosujPoleDowolne(getPozycja());
        if (kierunekRuchu == Kierunek.BRAK_KIERUNKU ||
                CzyKierunekZablokowany(kierunekRuchu)) return getPozycja();
        else {
            if (kierunekRuchu == Kierunek.DOL) return new Punkt(x, y + 1);
            if (kierunekRuchu == Kierunek.GORA) return new Punkt(x, y - 1);
            if (kierunekRuchu == Kierunek.LEWO) return new Punkt(x - 1, y);
            if (kierunekRuchu == Kierunek.PRAWO) return new Punkt(x + 1, y);
            else return new Punkt(x, y);
        }
    }

    @Override
    public void Akcja() {
        for (int i = 0; i < getZasiegRuchu(); i++) {
            Punkt przyszlaPozycja = ZaplanujRuch();

            if (getSwiat().CzyPoleJestZajete(przyszlaPozycja)
                    && getSwiat().CoZnajdujeSieNaPolu(przyszlaPozycja) != this) {
                Kolizja(getSwiat().CoZnajdujeSieNaPolu(przyszlaPozycja));
                break;
            } else if (getSwiat().CoZnajdujeSieNaPolu(przyszlaPozycja) != this) WykonajRuch(przyszlaPozycja);
        }
        kierunekRuchu = Kierunek.BRAK_KIERUNKU;
    }

    @Override
    public String TypOrganizmuToString() {
        return "Czlowiek";
    }

    public void setKierunekRuchu(Kierunek kierunekRuchu) {
        this.kierunekRuchu = kierunekRuchu;
    }

    public int getCoolDown() {
        return coolDown;
    }

    public void setCoolDown(int coolDown) {
        this.coolDown = coolDown;
    }
}
