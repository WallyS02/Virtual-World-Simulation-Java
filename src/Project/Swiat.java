package Project;

import java.io.IOException;
import java.util.*;
import java.io.File;
import java.io.PrintWriter;

import Project.Rosliny.Trawa;
import Project.Zwierzeta.Czlowiek;

public class Swiat {
    private final int sizeX;
    private final int sizeY;
    private final String type;
    private int numerTury;
    private Organizm[][] plansza;
    private boolean czyCzlowiekZyje;
    private boolean czyJestKoniecGry;
    private boolean pauza;
    private final ArrayList<Organizm> organizmy;
    private Czlowiek czlowiek;
    private GUI swiatGUI;

    public Swiat(GUI swiatGUI) {
        this.type = "Krata";
        this.sizeX = 0;
        this.sizeY = 0;
        numerTury = 0;
        czyCzlowiekZyje = true;
        czyJestKoniecGry = false;
        pauza = true;
        organizmy = new ArrayList<>();
        this.swiatGUI = swiatGUI;
    }

    public Swiat(String type, int sizeX, int sizeY, GUI swiatGUI) {
        this.type = type;
        this.sizeX = sizeX;
        this.sizeY = sizeY;
        numerTury = 0;
        czyCzlowiekZyje = true;
        czyJestKoniecGry = false;
        pauza = true;
        plansza = new Organizm[sizeY][sizeX];
        if(Objects.equals(type, "Hex")) {
            for (int i = 0; i < this.getSizeX(); i++) {
                Punkt p = new Punkt(i, sizeY / 2);
                plansza[sizeY / 2][i] = new Trawa(this, p, 0);
            }
            int start = 1, end = this.getSizeX() - 2;
            for (int i = this.getSizeY() / 2 - 1; i > 2; i--) {
                for (int j = start; j <= end; j++) {
                    Punkt p = new Punkt(j, i);
                    plansza[i][j] = new Trawa(this, p, 0);
                }
                start++;
                end--;
            }
            start = 1;
            end = this.getSizeX() - 2;
            for (int i = this.getSizeY() / 2 + 1; i < this.getSizeY() - 2; i++) {
                for (int j = start; j <= end; j++) {
                    Punkt p = new Punkt(j, i);
                    plansza[i][j] = new Trawa(this, p, 0);
                }
                start++;
                end--;
            }
            for (int i = 0; i < sizeY; i++) {
                for (int j = 0; j < sizeX; j++) {
                    if (plansza[i][j] == null) {
                        Punkt p = new Punkt(j, i);
                        plansza[i][j] = new PoleNiedostepne(Organizm.TypOrganizmu.POLE_NIEDOSTEPNE, this, p, 0, 0, 0);
                    }
                }
            }
            for (int i = 0; i < sizeY; i++) {
                for (int j = 0; j < sizeX; j++) {
                    if (!(plansza[i][j] instanceof PoleNiedostepne)) {
                        plansza[i][j] = null;
                    }
                }
            }
        }
        else {
            for (int i = 0; i < sizeY; i++) {
                for (int j = 0; j < sizeX; j++) {
                    plansza[i][j] = null;
                }
            }
        }
        organizmy = new ArrayList<>();
        this.swiatGUI = swiatGUI;
    }


    public void ZapiszSwiat(String nameOfFile) {
        try {
            nameOfFile += ".txt";
            File file = new File(nameOfFile);
            file.createNewFile();

            PrintWriter pw = new PrintWriter(file);
            pw.print(type + " ");
            pw.print(sizeX + " ");
            pw.print(sizeY + " ");
            pw.print(numerTury + " ");
            pw.print(czyCzlowiekZyje + " ");
            pw.print(czyJestKoniecGry + "\n");
            for (Organizm organizm : organizmy) {
                pw.print(organizm.getTypOrganizmu() + " ");
                pw.print(organizm.getPozycja().getX() + " ");
                pw.print(organizm.getPozycja().getY() + " ");
                pw.print(organizm.getSila() + " ");
                pw.print(organizm.getTuraUrodzenia() + " ");
                pw.print(organizm.getCzyUmarl());
                if (organizm.getTypOrganizmu() == Organizm.TypOrganizmu.CZLOWIEK) {
                    pw.print(" " + czlowiek.getCoolDown());
                }
                pw.println();
            }
            pw.close();
        } catch (IOException e) {
            System.out.println("Error: " + e);
        }
    }

    public static Swiat OdtworzSwiat(String nameOfFile) {
        try {
            nameOfFile += ".txt";
            File file = new File(nameOfFile);

            Scanner scanner = new Scanner(file);
            String line = scanner.nextLine();
            String[] properties = line.split(" ");
            String type = properties[0];
            int sizeX = Integer.parseInt(properties[1]);
            int sizeY = Integer.parseInt(properties[2]);
            Swiat tmpSwiat = new Swiat(type, sizeX, sizeY, null);
            tmpSwiat.numerTury = Integer.parseInt(properties[3]);
            tmpSwiat.czyCzlowiekZyje = Boolean.parseBoolean(properties[4]);
            tmpSwiat.czyJestKoniecGry = Boolean.parseBoolean(properties[5]);
            tmpSwiat.czlowiek = null;

            while (scanner.hasNextLine()) {
                line = scanner.nextLine();
                properties = line.split(" ");
                Organizm.TypOrganizmu typOrganizmu = Organizm.TypOrganizmu.valueOf(properties[0]);
                int x = Integer.parseInt(properties[1]);
                int y = Integer.parseInt(properties[2]);

                Organizm tmpOrganizm = FabrykaObiektow.StworzNowyOrganizm(typOrganizmu, tmpSwiat, new Punkt(x, y));
                int sila = Integer.parseInt(properties[3]);
                tmpOrganizm.setSila(sila);
                int turaUrodzenia = Integer.parseInt(properties[4]);
                tmpOrganizm.setTuraUrodzenia(turaUrodzenia);
                boolean czyUmarl = Boolean.parseBoolean(properties[5]);
                tmpOrganizm.setCzyUmarl(czyUmarl);

                if (typOrganizmu == Organizm.TypOrganizmu.CZLOWIEK) {
                    tmpSwiat.czlowiek = (Czlowiek) tmpOrganizm;
                    int cooldown = Integer.parseInt(properties[6]);
                    tmpSwiat.czlowiek.setCoolDown(cooldown);
                }
                tmpSwiat.DodajOrganizm(tmpOrganizm);
            }
            scanner.close();
            return tmpSwiat;
        } catch (
                IOException e) {
            System.out.println("Error: " + e);
        }
        return null;
    }

    public void GenerujSwiat(double zapelnienieSwiatu) {
        int liczbaOrganizmow = (int) Math.floor(sizeX * sizeY * zapelnienieSwiatu);
        Punkt pozycja = WylosujWolnePole();
        Organizm tmpOrganizm = FabrykaObiektow.StworzNowyOrganizm(Organizm.TypOrganizmu.CZLOWIEK, this, pozycja);
        DodajOrganizm(tmpOrganizm);
        czlowiek = (Czlowiek) tmpOrganizm;
        for (int i = 0; i < liczbaOrganizmow - 1; i++) {
            pozycja = WylosujWolnePole();
            if (pozycja != new Punkt(-1, -1)) {
                DodajOrganizm(FabrykaObiektow.StworzNowyOrganizm(Organizm.LosujTyp(), this, pozycja));
            } else return;
        }
    }

    public void WykonajTure() {
        if (czyJestKoniecGry) return;
        numerTury++;
        Komentator.DodajKomentarz("\nTURA " + numerTury);
        SortujOrganizmy();
        if(czlowiek != null) {
            if (czlowiek.getCoolDown() > 5)
                czlowiek.setSila(czlowiek.getSila() - 1);
            if (czlowiek.getCoolDown() > 0)
                czlowiek.setCoolDown(czlowiek.getCoolDown() - 1);
        }
        for (int i = 0; i < organizmy.size(); i++) {
            if (organizmy.get(i).getTuraUrodzenia() != numerTury && !organizmy.get(i).getCzyUmarl()) {
                organizmy.get(i).Akcja();
            }
        }
        for (int i = 0; i < organizmy.size(); i++) {
            if (organizmy.get(i).getCzyUmarl()) {
                organizmy.remove(i);
                i--;
            }
        }
        for (Organizm organizm : organizmy) {
            organizm.setCzyRozmnazalSie(false);
        }
    }

    private void SortujOrganizmy() {
        organizmy.sort((o1, o2) -> {
            if (o1.getInicjatywa() != o2.getInicjatywa())
                return Integer.compare(o2.getInicjatywa(), o1.getInicjatywa());
            else
                return Integer.compare(o1.getTuraUrodzenia(), o2.getTuraUrodzenia());
        });
    }

    public Punkt WylosujWolnePole() {
        Random rand = new Random();
        for (int i = 0; i < sizeY; i++) {
            for (int j = 0; j < sizeX; j++) {
                if (plansza[i][j] == null && !(plansza[i][j] instanceof PoleNiedostepne)) {
                    while (true) {
                        int x = rand.nextInt(sizeX);
                        int y = rand.nextInt(sizeY);
                        if (plansza[y][x] == null && !(plansza[y][x] instanceof PoleNiedostepne)) return new Punkt(x, y);
                    }
                }
            }
        }
        return new Punkt(-1, -1);
    }

    public boolean CzyPoleJestZajete(Punkt pole) {
        return plansza[pole.getY()][pole.getX()] != null;
    }

    public Organizm CoZnajdujeSieNaPolu(Punkt pole) {
        return plansza[pole.getY()][pole.getX()];
    }

    public void DodajOrganizm(Organizm organizm) {
        organizmy.add(organizm);
        plansza[organizm.getPozycja().getY()][organizm.getPozycja().getX()] = organizm;
    }

    public void UsunOrganizm(Organizm organizm) {
        plansza[organizm.getPozycja().getY()][organizm.getPozycja().getX()] = null;
        organizm.setCzyUmarl(true);
        if (organizm.getTypOrganizmu() == Organizm.TypOrganizmu.CZLOWIEK) {
            czyCzlowiekZyje = false;
            czlowiek = null;
        }
    }

    public int getSizeX() {
        return sizeX;
    }

    public int getSizeY() {
        return sizeY;
    }

    public int getNumerTury() {
        return numerTury;
    }

    public Organizm[][] getPlansza() {
        return plansza;
    }

    public boolean getCzyCzlowiekZyje() {
        return czyCzlowiekZyje;
    }

    public Czlowiek getCzlowiek() {
        return czlowiek;
    }

    public boolean isPauza() {
        return pauza;
    }

    public void setPauza(boolean pauza) {
        this.pauza = pauza;
    }

    public void setSwiatGUI(GUI swiatGUI) {
        this.swiatGUI = swiatGUI;
    }
    public String getType() {return type;}
}
