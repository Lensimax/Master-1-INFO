package Echec;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

//la classe échiquier est une fenêtre (JFrame)
//qui contient un damier lui-même constitué de 8X8 cases
//un label "etiquette" qui indique qui doit jouer
//enfin le booléen tour est vrai lorsque c’est le tour des bleus
// et faux lorsque c’est le tour des rouges
//L’échiquier implémente l’écouteur d’actions pour récupérer
//les évènements sur le menu que l’on va créer
class Echiquier extends JFrame implements ActionListener {
    public Case cases[][];
    private final JPanel damier;
    private final JLabel etiquette;
    private boolean tour;

    Echiquier (){
        setPreferredSize(new Dimension(480,480));
        setBackground(Color.darkGray);
        setSize(getPreferredSize());

        //setLayout permet de choisir la politique de placement
        // des objets graphiques dans un conteneur
        //ici la fenêtre
        //on choisit un FlowLayout : les éléments sont disposés
        // les uns à la suite des autres

        setLayout(new FlowLayout());

        //on choisit de tuer l’application
        //lorsque l’on clique sur la croix setDefaultCloseOperation(EXIT_ON_CLOSE);
        //création d’une barre de menu
        //avec un menu appelé "menu"
        //dans ce menu, on a deux choix
        //"Start" pour commencer une partie
        //"Quitter" pour quitter l’application

        JMenuBar menubar=new JMenuBar();
        JMenu menu=new JMenu("Menu");
        JMenuItem it1=new JMenuItem("Start");

        //Lorsque l’on clique sur l’item Start,
        // on déclenche une action appelée "Start"
        it1.setActionCommand("Start");
        JMenuItem it2=new JMenuItem("Quitter");
        it2.setActionCommand("Quitter");

        //ajout des items au menu
        menu.add(it1);
        menu.add(it2);
        //ajout du menu à la barre de menu

        menubar.add(menu);
        //Les évènements sur les items seront écoutés par l’échiquier
        it1.addActionListener(this);

        it2.addActionListener(this);
        //on affecte la barre de menu à la fenêtre Echiquier
        setJMenuBar(menubar);
        //on crée une zone de dessin "damier"
        // avec une politique de placement
        //en grille 8X8
        //cette zone contiendra les 8X8 cases
        damier=new JPanel(new GridLayout(8,8,0,0));
        damier.setSize(500,500);
        etiquette=new JLabel("Aucune␣partie␣en␣cours");
        //on crée un tableau de cases 8X8
        cases=new Case[8][8];
        //on alterne les couleurs pour obtenir un damier

        //chaque case est ajoutée au damier
        //grâce à la politique de placement des objets
        // les 8 premières cases seront placées sur
        //la première ligne du damier, etc.
        for(int i=0;i<8;i++){
            for(int j=0;j<8;j++){
                if((i+j)%2==0){
                    cases[i][j]=new Case(Color.lightGray ,i,j,null);}
                else{
                    cases[i][j]=new Case(Color.WHITE,i,j,null);}//créée sur le damier
                damier.add(cases[i][j]);
            }
        }
        //on insère le damier dans la fenêtre
        add(damier);
//on insère le label dans la fenêtre
        add(etiquette);
    }

    //fonction qui récupère les actions
//ici les actions correspondent aux évènements
// sur les items du menu
    public void actionPerformed(ActionEvent e){
        //Si on clique sur Quitter alors on tue l’application
        if(e.getActionCommand().equals("Quitter"))
            System.exit(0);
        else // Sinon, on a cliqué sur "Start" et on crée une nouvelle partie
            NouvellePartie();
    }

    public boolean getTour(){ return tour;}

    public void changeTour(){
        //change le booléen tour et met à jour l’étiquette
        tour=!tour;
        if(tour)
            etiquette.setText("Au␣tour␣des␣bleus");
        else
            etiquette.setText("Au␣tour␣des␣rouges");
    }

    private void NouvellePartie(){
        //création d’une nouvelle partie
        //on affiche "Au tour des bleus"
        etiquette.setText("Au␣tour␣des␣bleus");
        // les bleus commencent donc
        //tour est initialisé à vrai
        tour=true;
        //on supprime les pièces de la partie précédente
        for(int i=0;i<8;i++) {
            for(int j=0;j<8;j++){
                cases[i][j].P=null;}
        }
        //on place les pièces

        for(int i=0;i<8;i++)
            cases[1][i].P=new Pion(this,cases[1][i],true);
        for(int i=0;i<8;i++)
            cases[6][i].P=new Pion(this,cases[6][i],false);
        cases[0][4].P=new Roi(this,cases[0][4],true);
        cases[7][4].P=new Roi(this,cases[7][4],false);
        cases[0][0].P=new Tour(this,cases[0][0],true);
        cases[0][7].P=new Tour(this,cases[0][7],true);
        cases[7][0].P=new Tour(this,cases[7][0],false);
        cases[7][7].P=new Tour(this,cases[7][7],false);
        cases[0][2].P=new Fou(this,cases[0][2],true);
        cases[0][5].P=new Fou(this,cases[0][5],true);
        cases[7][2].P=new Fou(this,cases[7][2],false);
        cases[7][5].P=new Fou(this,cases[7][5],false);
        cases[0][1].P=new Cavalier(this,cases[0][1],true);
        cases[0][6].P=new Cavalier(this,cases[0][6],true);
        cases[7][1].P=new Cavalier(this,cases[7][1],false);
        cases[7][6].P=new Cavalier(this,cases[7][6],false);
        cases[0][3].P=new Roi(this,cases[0][3],true);
        cases[7][3].P=new Roi(this,cases[7][3],false);
//une fois toutes les pièces placées, on rafraichit l’affichage
// la partie commence !
        repaint ();
    }
}