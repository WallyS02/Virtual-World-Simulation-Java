package Project.Rosliny;

import java.util.Random;
import Project.*;

import javax.swing.*;

public class Mlecz extends Roslina {
    private static final int ILE_PROB = 3;

    public Mlecz(Swiat swiat, Punkt pozycja, int turaUrodzenia) {
        super(TypOrganizmu.MLECZ, swiat, pozycja, turaUrodzenia, 0, 0);
        setIkona(new ImageIcon("Mlecz.png"));
    }

    @Override
    public void Akcja() {
        Random rand = new Random();
        for (int i = 0; i < ILE_PROB; i++) {
            int tmpLosowanie = rand.nextInt(100);
            if (tmpLosowanie < getSzansaRozmnazania()) Rozprzestrzenianie();
        }
    }

    @Override
    public String TypOrganizmuToString() {
        return "Mlecz";
    }
}

