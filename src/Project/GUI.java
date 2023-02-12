package Project;

import javax.swing.*;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.util.Objects;

public class GUI implements ActionListener, KeyListener {
    private final JFrame jFrame;
    private final JLabel welcomeLabel;
    private final JMenuItem newGame;
    private final JMenuItem load;
    private final JMenuItem save;
    private final JMenuItem exit;
    private BoardGraphics boardGraphics = null;
    private KomentatorGraphics komentatorGraphics = null;
    private final JPanel mainPanel;
    private final int ODSTEP;
    private Swiat swiat;

    public GUI(String title) {
        Toolkit toolkit = Toolkit.getDefaultToolkit();
        Dimension dimension = toolkit.getScreenSize();
        System.out.println(dimension);
        ODSTEP = dimension.height / 100;


        jFrame = new JFrame(title);
        jFrame.setBounds((dimension.width - 1200) / 2, (dimension.height - 1000) / 2, 1200, 1000);
        jFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        jFrame.setResizable(false);

        welcomeLabel = new JLabel(new ImageIcon("Background.jpg"));
        welcomeLabel.setBounds((dimension.width - 1200) / 5 - 15, (dimension.height - 1000) / 5 + 80, 700, 500);
        jFrame.add(welcomeLabel);

        JMenuBar menuBar = new JMenuBar();
        JMenu menu = new JMenu("Menu");
        newGame = new JMenuItem("New game");
        load = new JMenuItem("Load");
        save = new JMenuItem("Save");
        exit = new JMenuItem("Exit");
        newGame.addActionListener(this);
        load.addActionListener(this);
        save.addActionListener(this);
        exit.addActionListener(this);
        menu.add(newGame);
        menu.add(load);
        menu.add(save);
        menu.add(exit);
        menuBar.add(menu);
        jFrame.setJMenuBar(menuBar);

        mainPanel = new JPanel();
        mainPanel.setBackground(new Color(113, 197, 98));
        mainPanel.setLayout(null);


        jFrame.addKeyListener(this);
        jFrame.add(mainPanel);
        jFrame.setVisible(true);
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        if (e.getSource() == newGame) {
            Komentator.WyczyscKomentarzy();
            String type = JOptionPane.showInputDialog(jFrame, "Wybierz rodzaj planszy", "Krata");
            int sizeX, sizeY;
            if(Objects.equals(type, "Krata")) {
                sizeX = Integer.parseInt(JOptionPane.showInputDialog(jFrame, "Podaj szerokosc swiata", "10"));
                sizeY = Integer.parseInt(JOptionPane.showInputDialog(jFrame, "Podaj wysokosc swiata", "10"));
            }
            else {
                sizeX = Integer.parseInt(JOptionPane.showInputDialog(jFrame, "Podaj szerokosc swiata", "20"));
                sizeY = Integer.parseInt(JOptionPane.showInputDialog(jFrame, "Podaj wysokosc swiata", "20"));
            }
            double zapelnienieSwiatu;
            if(Objects.equals(type, "Krata"))
                zapelnienieSwiatu = Double.parseDouble(JOptionPane.showInputDialog(jFrame, "Podaj zapelnienie swiata(wartosc od 0 do 1)", "0.3"));
            else zapelnienieSwiatu = Double.parseDouble(JOptionPane.showInputDialog(jFrame, "Podaj zapelnienie swiata(wartosc od 0 do 1)", "0.2"));
            swiat = new Swiat(type, sizeX, sizeY, this);
            swiat.GenerujSwiat(zapelnienieSwiatu);
            if (boardGraphics != null)
                mainPanel.remove(boardGraphics);
            if (komentatorGraphics != null)
                mainPanel.remove(komentatorGraphics);
            startGame(type);
        }
        if (e.getSource() == load) {
            Komentator.WyczyscKomentarzy();
            String nameOfFile = JOptionPane.showInputDialog(jFrame, "Podaj nazwe pliku", "zapis");
            swiat = Swiat.OdtworzSwiat(nameOfFile);
            swiat.setSwiatGUI(this);
            String type = swiat.getType();
            if(Objects.equals(type, "Krata"))
                boardGraphics = new BoardGraphicsSquare(swiat);
            else boardGraphics = new BoardGraphicsHex(swiat);
            komentatorGraphics = new KomentatorGraphics();
            if (boardGraphics != null)
                mainPanel.remove(boardGraphics);
            if (komentatorGraphics != null)
                mainPanel.remove(komentatorGraphics);
            startGame(type);
        }
        if (e.getSource() == save) {
            String nameOfFile = JOptionPane.showInputDialog(jFrame, "Podaj nazwe pliku", "zapis");
            swiat.ZapiszSwiat(nameOfFile);
            Komentator.DodajKomentarz("Swiat zostal zapisany");
            komentatorGraphics.odswiezKomentarzy();
        }
        if (e.getSource() == exit) {
            jFrame.dispose();
        }
    }

    @Override
    public void keyPressed(KeyEvent e) {
        if (swiat != null && swiat.isPauza()) {
            int keyCode = e.getKeyCode();
            if (keyCode == KeyEvent.VK_ENTER) {

            }
            else if (swiat.getCzyCzlowiekZyje()) {
                if (keyCode == KeyEvent.VK_W) {
                    swiat.getCzlowiek().setKierunekRuchu(Organizm.Kierunek.GORA);
                }
                else if (keyCode == KeyEvent.VK_S) {
                    swiat.getCzlowiek().setKierunekRuchu(Organizm.Kierunek.DOL);
                }
                else if (keyCode == KeyEvent.VK_A) {
                    swiat.getCzlowiek().setKierunekRuchu(Organizm.Kierunek.LEWO);
                }
                else if (keyCode == KeyEvent.VK_D) {
                    swiat.getCzlowiek().setKierunekRuchu(Organizm.Kierunek.PRAWO);
                }
                else if (keyCode == KeyEvent.VK_R) {
                    if(swiat.getCzlowiek().getCoolDown() == 0) {
                        swiat.getCzlowiek().setSila(swiat.getCzlowiek().getSila() + 10);
                        swiat.getCzlowiek().setCoolDown(swiat.getCzlowiek().getCoolDown() + 15);
                        Komentator.DodajKomentarz("Umiejetnosc 'Magiczny Eliksir' wlaczona (Pozostaly" + " czas trwania wynosi " + (swiat.getCzlowiek().getCoolDown() - 5) + " tur)");
                        komentatorGraphics.odswiezKomentarzy();
                        return;
                    }
                    else if(swiat.getCzlowiek().getCoolDown() > 5) {
                        Komentator.DodajKomentarz("Umiejetnosc 'Magiczny Eliksir' juz wlaczona (Pozostaly" + " czas trwania wynosi " + (swiat.getCzlowiek().getCoolDown() - 5) + " tur)");
                        komentatorGraphics.odswiezKomentarzy();
                        return;
                    }
                    else {
                        Komentator.DodajKomentarz("Umiejetnosc mozna wlaczyc tylko po " + swiat.getCzlowiek().getCoolDown() + " turach");
                        komentatorGraphics.odswiezKomentarzy();
                        return;
                    }
                }
                else {
                    Komentator.DodajKomentarz("\nNieoznaczony symbol, sprobuj ponownie");
                    komentatorGraphics.odswiezKomentarzy();
                    return;
                }
            }
            else if (!swiat.getCzyCzlowiekZyje() && (keyCode == KeyEvent.VK_W || keyCode == KeyEvent.VK_S || keyCode == KeyEvent.VK_A || keyCode == KeyEvent.VK_D || keyCode == KeyEvent.VK_R)) {
                Komentator.DodajKomentarz("Czlowiek umarl, nie mozesz im wiecej sterowac");
                komentatorGraphics.odswiezKomentarzy();
                return;
            }
            else {
                Komentator.DodajKomentarz("\nNieoznaczony symbol, sprobuj ponownie");
                komentatorGraphics.odswiezKomentarzy();
                return;
            }
            Komentator.WyczyscKomentarzy();
            swiat.setPauza(false);
            swiat.WykonajTure();
            odswiezSwiat();
            swiat.setPauza(true);
        }
    }

    @Override
    public void keyTyped(KeyEvent e) {
    }

    @Override
    public void keyReleased(KeyEvent e) {
    }

    private static class PolePlanszy extends JButton {
        private boolean isEmpty;
        private ImageIcon ikona;
        private final int pozX;
        private final int pozY;

        public PolePlanszy(int X, int Y) {
            super();
            ikona = new ImageIcon("Ziemia.png");
            setIcon(ikona);
            isEmpty = true;
            pozX = X;
            pozY = Y;
        }

        public void setEmpty(boolean empty) {
            isEmpty = empty;
        }

        public void setIkona(ImageIcon ikona) {
            this.ikona = ikona;
            setIcon(ikona);
        }

        public int getPozX() {
            return pozX;
        }

        public int getPozY() {
            return pozY;
        }
        public boolean isEmpty() { return isEmpty; }
    }

    private abstract class BoardGraphics extends JPanel {
        private final int sizeX;
        private final int sizeY;
        private final int szerokosc = mainPanel.getHeight() * 5 / 6 + ODSTEP + 110;
        private final int wysokosc = mainPanel.getHeight() * 5 / 6 + ODSTEP + 110;
        private final PolePlanszy[][] polaPlanszy;
        public BoardGraphics(Swiat swiat) {
            super();
            setBounds(mainPanel.getX() + ODSTEP, mainPanel.getY() + ODSTEP, szerokosc, wysokosc);
            setBackground(new Color(255,140,0));
            this.sizeX = swiat.getSizeX();
            this.sizeY = swiat.getSizeY();
            polaPlanszy = new PolePlanszy[this.getSizeX()][this.getSizeY()];
        }
        public int getSizeX() {
            return sizeX;
        }
        public int getSizeY() { return sizeY; }
        public int getSzerokosc() {
            return szerokosc;
        }

        public int getWysokosc() {
            return wysokosc;
        }

        public PolePlanszy[][] getPolaPlanszy() {
            return polaPlanszy;
        }

        public void odswiezPlansze(){
            for (int i = 0; i < this.getSizeY(); i++) {
                for (int j = 0; j < this.getSizeX(); j++) {
                    if(polaPlanszy[i][j] != null) {
                        Organizm tmpOrganizm = swiat.getPlansza()[i][j];
                        if (tmpOrganizm != null) {
                            polaPlanszy[i][j].setEmpty(false);
                            polaPlanszy[i][j].setDisabledIcon(tmpOrganizm.getIkona());
                            polaPlanszy[i][j].setEnabled(false);
                        } else {
                            polaPlanszy[i][j].setEmpty(true);
                            polaPlanszy[i][j].setEnabled(true);
                            polaPlanszy[i][j].setIkona(new ImageIcon("Ziemia.png"));
                        }
                    }
                }
            }
        }
    }

    private class BoardGraphicsSquare extends BoardGraphics {
        public BoardGraphicsSquare(Swiat swiat) {
            super(swiat);
            for (int i = 0; i < this.getSizeY(); i++) {
                for (int j = 0; j < this.getSizeX(); j++) {
                    getPolaPlanszy()[i][j] = new PolePlanszy(j, i);
                    getPolaPlanszy()[i][j].addActionListener(new ActionListener() {
                        @Override
                        public void actionPerformed(ActionEvent e) {
                            if (e.getSource() instanceof PolePlanszy tmpPole) {
                                if (tmpPole.isEmpty()) {
                                    ListaOrganizmow listaOrganizmow = new ListaOrganizmow(tmpPole.getX() + jFrame.getX(), tmpPole.getY() + jFrame.getY(), new Punkt(tmpPole.getPozX(), tmpPole.getPozY()));
                                }
                            }
                        }
                    });
                }
            }
            for (int i = 0; i < this.getSizeY(); i++) {
                for (int j = 0; j < this.getSizeX(); j++) {
                    this.add(getPolaPlanszy()[i][j]);
                }
            }
            this.setLayout(new GridLayout(this.getSizeY(), this.getSizeX()));
        }
    }

    private class BoardGraphicsHex extends BoardGraphics {
        public BoardGraphicsHex(Swiat swiat) {
            super(swiat);
            for(int i = 0; i < this.getSizeX(); i++) {
                getPolaPlanszy()[this.getSizeY() / 2][i] = new PolePlanszy(i, this.getSizeY() / 2);
                getPolaPlanszy()[this.getSizeY() / 2][i].addActionListener(new ActionListener() {
                    @Override
                    public void actionPerformed(ActionEvent e) {
                        if (e.getSource() instanceof PolePlanszy tmpPole) {
                            if (tmpPole.isEmpty()) {
                                ListaOrganizmow listaOrganizmow = new ListaOrganizmow(tmpPole.getX() + jFrame.getX(), tmpPole.getY() + jFrame.getY(), new Punkt(tmpPole.getPozX(), tmpPole.getPozY()));
                            }
                        }
                    }
                });
            }
            int start = 1, end = this.getSizeX() - 2;
            for(int i = this.getSizeY() / 2 - 1; i > 2; i--){
                for(int j = start; j <= end; j++){
                    getPolaPlanszy()[i][j] = new PolePlanszy(j, i);
                    getPolaPlanszy()[i][j].addActionListener(new ActionListener() {
                        @Override
                        public void actionPerformed(ActionEvent e) {
                            if (e.getSource() instanceof PolePlanszy tmpPole) {
                                if (tmpPole.isEmpty()) {
                                    ListaOrganizmow listaOrganizmow = new ListaOrganizmow(tmpPole.getX() + jFrame.getX(), tmpPole.getY() + jFrame.getY(), new Punkt(tmpPole.getPozX(), tmpPole.getPozY()));
                                }
                            }
                        }
                    });
                }
                start++;
                end--;
            }
            start = 1;
            end = this.getSizeX() - 2;
            for(int i = this.getSizeY() / 2 + 1; i < this.getSizeY() - 2; i++){
                for(int j = start; j <= end; j++){
                    getPolaPlanszy()[i][j] = new PolePlanszy(j, i);
                    getPolaPlanszy()[i][j].addActionListener(new ActionListener() {
                        @Override
                        public void actionPerformed(ActionEvent e) {
                            if (e.getSource() instanceof PolePlanszy tmpPole) {
                                if (tmpPole.isEmpty()) {
                                    ListaOrganizmow listaOrganizmow = new ListaOrganizmow(tmpPole.getX() + jFrame.getX(), tmpPole.getY() + jFrame.getY(), new Punkt(tmpPole.getPozX(), tmpPole.getPozY()));
                                }
                            }
                        }
                    });
                }
                start++;
                end--;
            }
            this.setLayout(new GridBagLayout());
            GridBagConstraints settings = new GridBagConstraints();
            settings.weightx = this.getSzerokosc() / this.getSizeX();
            settings.weighty = this.getWysokosc() / this.getSizeY();
            for(int i = 0; i < this.getSizeX(); i++) {
                settings.gridx = i;
                settings.gridy = this.getSizeY() / 2;
                if(this.getPolaPlanszy()[this.getSizeY() / 2][i] != null)
                    this.add(getPolaPlanszy()[this.getSizeY() / 2][i], settings);
            }
            start = 1;
            end = this.getSizeX() - 2;
            for(int i = this.getSizeY() / 2 - 1; i > 2; i--){
                for(int j = start; j <= end; j++){
                    settings.gridx = j;
                    settings.gridy = i;
                    if(this.getPolaPlanszy()[i][j] != null)
                        this.add(getPolaPlanszy()[i][j], settings);
                }
                start++;
                end--;
            }
            start = 1;
            end = this.getSizeX() - 2;
            for(int i = this.getSizeY() / 2 + 1; i < this.getSizeY() - 2; i++){
                for(int j = start; j <= end; j++){
                    settings.gridx = j;
                    settings.gridy = i;
                    if(this.getPolaPlanszy()[i][j] != null)
                        this.add(getPolaPlanszy()[i][j], settings);
                }
                start++;
                end--;
            }
        }
    }

    private class KomentatorGraphics extends JPanel {
        private String tekst;
        private final JTextArea textArea;

        public KomentatorGraphics() {
            super();
            setBounds(boardGraphics.getX() + boardGraphics.getWidth() + ODSTEP, mainPanel.getY() + ODSTEP, mainPanel.getWidth() - boardGraphics.getWidth() - ODSTEP * 3, mainPanel.getHeight() * 5 / 6 + ODSTEP + 110);
            tekst = Komentator.getTekst();
            textArea = new JTextArea(tekst);
            textArea.setEditable(false);
            textArea.setBackground(new Color(255,140,0));
            setLayout(new CardLayout());

            textArea.setLineWrap(true);
            textArea.setWrapStyleWord(true);
            textArea.setMargin(new Insets(5, 5, 5, 5));
            JScrollPane sp = new JScrollPane(textArea);
            add(sp);
        }

        public void odswiezKomentarzy() {
            String instriction = """
                    Autor: Sebastian Kutny 188586
                    WSAD - sterowanie
                    R - aktywacja umiejetnosci
                    Enter - przejscie do nastepnej tury
                    """;
            tekst = instriction + Komentator.getTekst();
            textArea.setText(tekst);
        }
    }

    private class ListaOrganizmow extends JFrame {
        private final Organizm.TypOrganizmu[] typOrganizmuList;
        private final JList jList;

        public ListaOrganizmow(int x, int y, Punkt punkt) {
            super("Lista organizmow");
            setBounds(x, y, 100, 300);
            String[] listaOrganizmow = new String[]{"Barszcz Sosnowskiego", "Guarana", "Mlecz", "Trawa",
                    "Wilcze jagody", "Antylopa", "Lis", "Owca", "Wilk", "Zolw"};
            typOrganizmuList = new Organizm.TypOrganizmu[]{Organizm.TypOrganizmu.BARSZCZ_SOSNOWSKIEGO,
                    Organizm.TypOrganizmu.GUARANA, Organizm.TypOrganizmu.MLECZ, Organizm.TypOrganizmu.TRAWA,
                    Organizm.TypOrganizmu.WILCZE_JAGODY, Organizm.TypOrganizmu.ANTYLOPA,
                    Organizm.TypOrganizmu.LIS,
                    Organizm.TypOrganizmu.OWCA, Organizm.TypOrganizmu.WILK,
                    Organizm.TypOrganizmu.ZOLW
            };

            jList = new JList(listaOrganizmow);
            jList.setVisibleRowCount(listaOrganizmow.length);
            jList.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
            jList.addListSelectionListener(new ListSelectionListener() {
                @Override
                public void valueChanged(ListSelectionEvent e) {
                    Organizm tmpOrganizm = FabrykaObiektow.StworzNowyOrganizm(typOrganizmuList[jList.getSelectedIndex()], swiat, punkt);
                    swiat.DodajOrganizm(tmpOrganizm);
                    Komentator.DodajKomentarz("Stworzono nowy organizm " + tmpOrganizm.OrganizmToSring());
                    odswiezSwiat();
                    dispose();
                }
            });

            JScrollPane sp = new JScrollPane(jList);
            add(sp);

            setVisible(true);
        }
    }

    private void startGame(String type) {
        jFrame.remove(welcomeLabel);
        if(Objects.equals(type, "Krata"))
            boardGraphics = new BoardGraphicsSquare(swiat);
        else boardGraphics = new BoardGraphicsHex(swiat);
        mainPanel.add(boardGraphics);

        komentatorGraphics = new KomentatorGraphics();
        mainPanel.add(komentatorGraphics);

        odswiezSwiat();
    }

    public void odswiezSwiat() {
        boardGraphics.odswiezPlansze();
        komentatorGraphics.odswiezKomentarzy();
        SwingUtilities.updateComponentTreeUI(jFrame);
        jFrame.requestFocusInWindow();
    }

    public Swiat getSwiat() {
        return swiat;
    }
}
