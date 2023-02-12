package Project;

import Project.Rosliny.*;
import Project.Zwierzeta.*;


public class FabrykaObiektow {
    public static Organizm StworzNowyOrganizm(Organizm.TypOrganizmu typOrganizmu, Swiat swiat, Punkt pozycja) {
        return switch (typOrganizmu) {
            case WILK -> new Wilk(swiat, pozycja, swiat.getNumerTury());
            case OWCA -> new Owca(swiat, pozycja, swiat.getNumerTury());
            case LIS -> new Lis(swiat, pozycja, swiat.getNumerTury());
            case ZOLW -> new Zolw(swiat, pozycja, swiat.getNumerTury());
            case ANTYLOPA -> new Antylopa(swiat, pozycja, swiat.getNumerTury());
            case CZLOWIEK -> new Czlowiek(swiat, pozycja, swiat.getNumerTury());
            case TRAWA -> new Trawa(swiat, pozycja, swiat.getNumerTury());
            case MLECZ -> new Mlecz(swiat, pozycja, swiat.getNumerTury());
            case GUARANA -> new Guarana(swiat, pozycja, swiat.getNumerTury());
            case WILCZE_JAGODY -> new WilczeJagody(swiat, pozycja, swiat.getNumerTury());
            case BARSZCZ_SOSNOWSKIEGO -> new BarszczSosnowskiego(swiat, pozycja, swiat.getNumerTury());
            default -> null;
        };
    }
}
