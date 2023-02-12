package Project;

public class PoleNiedostepne extends Organizm{
    public PoleNiedostepne(TypOrganizmu typOrganizmu, Swiat swiat, Punkt pozycja, int turaUrodzenia, int sila, int inicjatywa) {
        super(typOrganizmu, swiat, pozycja, 0, 0, 0);
    }

    @Override
    public String TypOrganizmuToString() {
        return null;
    }

    @Override
    public void Akcja() {

    }

    @Override
    public void Kolizja(Organizm other) {

    }

    @Override
    public boolean CzyJestZwierzeciem() {
        return false;
    }
}
