package Echec;

class Case extends JPanel implements MouseListener{
    private final Color couleur;
    private boolean click;
    private static boolean clicked=false;
    public Piece P;
    public final int x,y;
    private static Case destination=null;
    Case(Color c, int x, int y, Piece P){
// setBackground affecte la couleur de fond
        setBackground(c);
// setPreferredSize et setSize permettent de fixer
// les dimensions de la case
        setPreferredSize(new Dimension(50,50));
        setSize(getPreferredSize());
// les évènements souris sur la case sont écoutés par la case elle­même
        addMouseListener(this);
        couleur=c;
        click=false;
        this.x=x;
        this.y=y;
        this.P=P;
    }
    // cette fonction remet la case à sa couleur d’origine
    public void ResetColor (){
        setBackground(couleur);
    }
    // comme la case écoute les évènements souris sur elle­même
// elle doit implémenter les fonctions suivantes
// lorsqu’on enclenche le clic sur la case
    public void mousePressed(MouseEvent e){
        click=true; // la case se souvient que la souris a cliqué sur elle
        clicked=true;
// on informe les autres cases que le clic est enclenché
        setBackground(Color.green);
// on change la couleur de la case
        destination=this;
// on sauvegarde la référence de la case
//que la souris survole actuellement avec le clic enclenché
    }
    // évènement "sur clic" non utilisé
    public void mouseClicked(MouseEvent e){}
    // lorsqu’on entre dans une case avec le clic enclenché
    public void mouseEntered(MouseEvent e){
        if(!click && clicked) setBackground(Color.cyan); // on change la couleur
        if( clicked) destination=this; // et on met à jour la destination
    }
    // évènement lorsqu’on relâche le clic
// cet évènement est capté par la case qui a subit le clic
// c’est pourquoi on a besoin de la variable destination
// elle permet de savoir où est la souris lorsque le clic est relâché
    public void mouseReleased(MouseEvent e){
// on remet la couleur d’origine de la case
        ResetColor ();
        click=false;
        clicked=false;
        if(destination != null){
// on remet la couleur d’origine de la case destination
            destination.ResetColor();
// si la case d’origine contient une pièce
// on essaie de bouger cette pièce vers la case destination
// ce déplacement réussit si on respecte les contraintes
            if(P!=null){
                if(P.moveTo(destination)){
//code à compléter
                }
            }
        }
    }
    // évènement sur sortie de la souris d’une case
    public void mouseExited(MouseEvent e){
        if(!click) ResetColor();
        destination=null;
    }
    // la méthode paintComponent gère
// l’affichage de la case
    public void paintComponent(Graphics g){
// on affiche d’abord la case
        super.paintComponent(g);
// puis on affiche la pièce
// à l’intérieur
// si la case contient une pièce
        if(P!=null){
            if(P.isBlue())
                g.setColor(Color.blue);
else
            g.setColor(Color.red);
            Font f = new Font("Comic␣sans␣MS",Font.BOLD ,20);
            g.setFont(f);
            g.drawString(P.ToString(),20,30);
        } }
}
­­­­­­­­­­­­­­­­­­­­­­­­­­­­­­­­­­­­­­­­­­­­­­­­­­­­­­­­­­­­­­­­­­­­­­­­­­­­­­­­­­­­­­­­­­­­­­­­­­­­­­­­­­­­­­­­­­­­­­­­­­­
