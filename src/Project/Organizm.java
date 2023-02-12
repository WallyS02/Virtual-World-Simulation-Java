package Project;

import javax.swing.*;
import java.util.Random;

public abstract class Organizm {
    public enum TypOrganizmu {
        CZLOWIEK,
        WILK,
        OWCA,
        LIS,
        ZOLW,
        ANTYLOPA,
        TRAWA,
        MLECZ,
        GUARANA,
        WILCZE_JAGODY,
        BARSZCZ_SOSNOWSKIEGO,
        POLE_NIEDOSTEPNE
    }

    public enum Kierunek {
        LEWO(0),
        PRAWO(1),
        GORA(2),
        DOL(3),
        BRAK_KIERUNKU(4);

        private final int value;

        Kierunek(int value) {
            this.value = value;
        }

        public int getValue() {
            return value;
        }
    }

    private int sila;
    private final int inicjatywa;
    private int turaUrodzenia;
    private ImageIcon ikona;
    private boolean czyUmarl;
    private final boolean[] kierunek;
    private boolean czyRozmnazalSie;
    private Swiat swiat;
    private Punkt pozycja;
    private final TypOrganizmu typOrganizmu;
    private double szansaRozmnazania;
    private static final int LICZBA_ROZNYCH_GATUNKOW = 11;

    public abstract String TypOrganizmuToString();

    public abstract void Akcja();

    public abstract void Kolizja(Organizm other);

    public abstract boolean CzyJestZwierzeciem();

    public Organizm(TypOrganizmu typOrganizmu, Swiat swiat, Punkt pozycja, int turaUrodzenia, int sila, int inicjatywa) {
        this.typOrganizmu = typOrganizmu;
        this.swiat = swiat;
        this.pozycja = pozycja;
        this.turaUrodzenia = turaUrodzenia;
        this.sila = sila;
        this.inicjatywa = inicjatywa;
        czyUmarl = false;
        kierunek = new boolean[]{true, true, true, true};
    }

    public boolean SpecjalneDzialaniePodczasAtaku(Organizm atakujacy, Organizm ofiara) {
        return false;
    }

    public String OrganizmToSring() {
        return (TypOrganizmuToString() + " x[" + pozycja.getX() + "] y["
                + pozycja.getY() + "] sila[" + sila + "]");
    }

    public void WykonajRuch(Punkt przyszlaPozycja) {
        int x = przyszlaPozycja.getX();
        int y = przyszlaPozycja.getY();
        swiat.getPlansza()[pozycja.getY()][pozycja.getX()] = null;
        swiat.getPlansza()[y][x] = this;
        pozycja.setX(x);
        pozycja.setY(y);
    }

    static TypOrganizmu LosujTyp() {
        Random rand = new Random();
        int tmp = rand.nextInt(LICZBA_ROZNYCH_GATUNKOW - 1);
        if (tmp == 0) return TypOrganizmu.ANTYLOPA;
        if (tmp == 1) return TypOrganizmu.BARSZCZ_SOSNOWSKIEGO;
        if (tmp == 2) return TypOrganizmu.GUARANA;
        if (tmp == 3) return TypOrganizmu.LIS;
        if (tmp == 4) return TypOrganizmu.MLECZ;
        if (tmp == 5) return TypOrganizmu.OWCA;
        if (tmp == 6) return TypOrganizmu.TRAWA;
        if (tmp == 7) return TypOrganizmu.WILCZE_JAGODY;
        if (tmp == 8) return TypOrganizmu.WILK;
        else return TypOrganizmu.ZOLW;
    }
    public Punkt LosujPoleDowolne(Punkt pozycja) {
        OdblokujWszystkieKierunki();
        int pozX = pozycja.getX();
        int pozY = pozycja.getY();
        int sizeX = swiat.getSizeX();
        int sizeY = swiat.getSizeY();
        int ileKierunkowMozliwych = 0;

        if (pozX == 0 || getSwiat().getPlansza()[pozY][pozX - 1] instanceof PoleNiedostepne) ZablokujKierunek(Kierunek.LEWO);
        else ileKierunkowMozliwych++;
        if (pozX == sizeX - 1 || getSwiat().getPlansza()[pozY][pozX + 1] instanceof PoleNiedostepne) ZablokujKierunek(Kierunek.PRAWO);
        else ileKierunkowMozliwych++;
        if (pozY == 0 || getSwiat().getPlansza()[pozY - 1][pozX] instanceof PoleNiedostepne) ZablokujKierunek(Kierunek.GORA);
        else ileKierunkowMozliwych++;
        if (pozY == sizeY - 1 || getSwiat().getPlansza()[pozY + 1][pozX] instanceof PoleNiedostepne) ZablokujKierunek(Kierunek.DOL);
        else ileKierunkowMozliwych++;

        if (ileKierunkowMozliwych == 0) return pozycja;
        while (true) {
            Random rand = new Random();
            int upperbound = 100;
            int tmpLosowanie = rand.nextInt(upperbound);
            if (tmpLosowanie < 25 && !CzyKierunekZablokowany(Kierunek.LEWO))
                return new Punkt(pozX - 1, pozY);
            else if (tmpLosowanie >= 25 && tmpLosowanie < 50 && !CzyKierunekZablokowany(Kierunek.PRAWO))
                return new Punkt(pozX + 1, pozY);
            else if (tmpLosowanie >= 50 && tmpLosowanie < 75 && !CzyKierunekZablokowany(Kierunek.GORA))
                return new Punkt(pozX, pozY - 1);
            else if (tmpLosowanie >= 75 && !CzyKierunekZablokowany(Kierunek.DOL))
                return new Punkt(pozX, pozY + 1);
        }
    }
    public Punkt LosujPoleNiezajete(Punkt pozycja) {
        OdblokujWszystkieKierunki();
        int pozX = pozycja.getX();
        int pozY = pozycja.getY();
        int sizeX = swiat.getSizeX();
        int sizeY = swiat.getSizeY();
        int ileKierunkowMozliwych = 0;

        if (pozX == 0 || getSwiat().getPlansza()[pozY][pozX - 1] instanceof PoleNiedostepne) ZablokujKierunek(Kierunek.LEWO);
        else {
            if (!swiat.CzyPoleJestZajete(new Punkt(pozX - 1, pozY))) ileKierunkowMozliwych++;
            else ZablokujKierunek(Kierunek.LEWO);
        }

        if (pozX == sizeX - 1 || getSwiat().getPlansza()[pozY][pozX + 1] instanceof PoleNiedostepne) ZablokujKierunek(Kierunek.PRAWO);
        else {
            if (!swiat.CzyPoleJestZajete(new Punkt(pozX + 1, pozY))) ileKierunkowMozliwych++;
            else ZablokujKierunek(Kierunek.PRAWO);
        }

        if (pozY == 0 || getSwiat().getPlansza()[pozY - 1][pozX] instanceof PoleNiedostepne) ZablokujKierunek(Kierunek.GORA);
        else {
            if (!swiat.CzyPoleJestZajete(new Punkt(pozX, pozY - 1))) ileKierunkowMozliwych++;
            else ZablokujKierunek(Kierunek.GORA);
        }

        if (pozY == sizeY - 1 || getSwiat().getPlansza()[pozY + 1][pozX] instanceof PoleNiedostepne) ZablokujKierunek(Kierunek.DOL);
        else {
            if (!swiat.CzyPoleJestZajete(new Punkt(pozX, pozY + 1))) ileKierunkowMozliwych++;
            else ZablokujKierunek(Kierunek.DOL);
        }

        if (ileKierunkowMozliwych == 0) return new Punkt(pozX, pozY);
        while (true) {
            Random rand = new Random();
            int upperbound = 100;
            int tmpLosowanie = rand.nextInt(upperbound);
            if (tmpLosowanie < 25 && !CzyKierunekZablokowany(Kierunek.LEWO))
                return new Punkt(pozX - 1, pozY);
            else if (tmpLosowanie >= 25 && tmpLosowanie < 50 && !CzyKierunekZablokowany(Kierunek.PRAWO))
                return new Punkt(pozX + 1, pozY);
            else if (tmpLosowanie >= 50 && tmpLosowanie < 75 && !CzyKierunekZablokowany(Kierunek.GORA))
                return new Punkt(pozX, pozY - 1);
            else if (tmpLosowanie >= 75 && !CzyKierunekZablokowany(Kierunek.DOL))
                return new Punkt(pozX, pozY + 1);
        }
    }

    protected void ZablokujKierunek(Kierunek kierunek) {
        this.kierunek[kierunek.getValue()] = false;
    }

    protected void OdblokujKierunek(Kierunek kierunek) {
        this.kierunek[kierunek.getValue()] = true;
    }

    protected void OdblokujWszystkieKierunki() {
        OdblokujKierunek(Kierunek.LEWO);
        OdblokujKierunek(Kierunek.PRAWO);
        OdblokujKierunek(Kierunek.GORA);
        OdblokujKierunek(Kierunek.DOL);
    }

    protected boolean CzyKierunekZablokowany(Kierunek kierunek) {
        return !(this.kierunek[kierunek.getValue()]);
    }

    public int getSila() {
        return sila;
    }

    public int getInicjatywa() {
        return inicjatywa;
    }

    public int getTuraUrodzenia() {
        return turaUrodzenia;
    }

    public boolean getCzyUmarl() {
        return czyUmarl;
    }

    public boolean getCzyRozmnazalSie() {
        return czyRozmnazalSie;
    }

    public Swiat getSwiat() {
        return swiat;
    }

    public Punkt getPozycja() {
        return pozycja;
    }

    public TypOrganizmu getTypOrganizmu() {
        return typOrganizmu;
    }

    public void setSila(int sila) {
        this.sila = sila;
    }

    public void setTuraUrodzenia(int turaUrodzenia) {
        this.turaUrodzenia = turaUrodzenia;
    }

    public void setCzyUmarl(boolean czyUmarl) {
        this.czyUmarl = czyUmarl;
    }

    public void setCzyRozmnazalSie(boolean czyRozmnazalSie) {
        this.czyRozmnazalSie = czyRozmnazalSie;
    }

    public void setSwiat(Swiat swiat) {
        this.swiat = swiat;
    }

    public void setPozycja(Punkt pozycja) {
        this.pozycja = pozycja;
    }

    public ImageIcon getIkona() {
        return ikona;
    }

    public void setIkona(ImageIcon ikona) {
        this.ikona = ikona;
    }

    public double getSzansaRozmnazania() {
        return szansaRozmnazania;
    }

    public void setSzansaRozmnazania(double szansaRozmnazania) {
        this.szansaRozmnazania = szansaRozmnazania;
    }
}
