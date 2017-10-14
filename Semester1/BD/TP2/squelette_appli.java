import java.sql.*;
import java.util.ArrayList;

public class squelette_appli {
	
	static final String CONN_URL = "jdbc:oracle:thin:@im2ag-oracle.e.ujf-grenoble.fr:1521:im2ag";
	
	static final String USER = "isnelm";
	static final String PASSWD = "Maxime26";

	static Connection conn; 
	
	
    public static void main(String args[]) {

        try {


            // Enregistrement du driver Oracle
            System.out.print("Loading Oracle driver... ");
            DriverManager.registerDriver(new oracle.jdbc.driver.OracleDriver());  	    System.out.println("loaded");

            // Etablissement de la connection
            System.out.print("Connecting to the database... ");
            conn = DriverManager.getConnection(CONN_URL,USER,PASSWD);
            System.out.println("connected");

            // Desactivation de l'autocommit
            conn.setAutoCommit(false);
            System.out.println("Autocommit disabled");

            // code métier de la fonctionnalité

            changerFonctionCage();

            // Liberation des ressources et fermeture de la connexion...
            // A COMPLETER
            conn.close();

            System.out.println("bye.");


  	    // traitement d'exception
          } catch (SQLException e) {
              System.err.println("failed");
              System.out.println("Affichage de la pile d'erreur");
  	          e.printStackTrace(System.err);
              System.out.println("Affichage du message d'erreur");
              System.out.println(e.getMessage());
              System.out.println("Affichage du code d'erreur");
  	          System.out.println(e.getErrorCode());	    

          }
     }
    
    public static void changerFonctionCage(){
    	try {
    		String fonction;
        	int numCage;
        	int rs;
        	
        	do {
            	numCage = LectureClavier.lireEntier("Entrer le numéro de la cage");
        	} while(numCage < 0);
        	
        	System.out.println("Entrer la nouvelle fonction de la cage");
        	fonction = LectureClavier.lireChaine();
        	

    		PreparedStatement pstmt = conn.prepareStatement("UPDATE LesCages SET fonction = ? WHERE noCage = ?");
    			
    		pstmt.setInt(2, numCage); //  includes  type  checking
    		pstmt.setString(1, fonction);

        	
    		rs = pstmt.executeUpdate();
    		
    		conn.commit();

    	} catch(SQLException esql){
    		System.err.println("Erreur execution update " + esql);
    	} catch(Exception e){
    		System.err.println("Erreur changerFonctionCage");
    	}
    
    	
    	
    }
    
    public static void ajouterAnimal(){
    	
    	try {
    		String animal;
        	ResultSet rs;
        	int numCage = -1;
        	
        	System.out.println("Entrer le type de l'animal");
        	animal = LectureClavier.lireChaine();
        	
        	PreparedStatement pstmt = conn.prepareStatement("SELECT * From LesCages WHERE fonction = ?");
    		
    		pstmt.setString(1, animal);

    		rs = pstmt.executeQuery();
    		
    		ArrayList<Integer> tabnumCage = new ArrayList<Integer>();
    		
    		while(rs.next()){
    			System.out.println("Numero cage: "+ rs.getInt("noCage") +" Numero allée: "+ rs.getInt("noAllee"));
    			tabnumCage.add(rs.getInt("noCage"));
    		}
    		

    		boolean trouve = false;
    		
    		while(!trouve){
    			numCage = LectureClavier.lireEntier("Choisir un numéro de cage");
        		
        		
        		for(int i: tabnumCage){
        			if(i == numCage){
        				trouve = true;
        				break;
        			}   				
        		}
    		}
    		
    		String nom;
    		String sexe;
    		String type;
    		String pays;
    		int annee_nais;
    		int nb_maladies;
    		
    		System.out.println("Entrer le nom de l'animal");
    		nom = LectureClavier.lireChaine();
    		
    		System.out.println("Entrer le sexe de l'animal");
    		sexe = LectureClavier.lireChaine();
    		
    		System.out.println("Entrer le type de l'animal");
    		type = LectureClavier.lireChaine();
    		
    		System.out.println("Entrer le pays d'origine de l'animal");
    		pays = LectureClavier.lireChaine();
    		
    		do {
        		annee_nais = LectureClavier.lireEntier("Entrer l'année de naissance de l'animal");
    		} while(annee_nais < 1900);
    		
    		do {
        		nb_maladies = LectureClavier.lireEntier("Entrer le nombre de maladie(s) de l'animal");
    		} while(nb_maladies < 0);
    		
    		PreparedStatement insert = conn.prepareStatement("INSERT INTO LesAnimaux VALUES (?, ?, ?, ?, ?, ?, ?, ?)");
    		
    		insert.setString(1, nom);
    		insert.setString(2, sexe);
    		insert.setString(3, type);
    		insert.setString(4, animal);
    		insert.setString(5, pays);
    		insert.setInt(6, annee_nais);
    		insert.setInt(7, numCage);
    		insert.setInt(8, nb_maladies);
    		
    		int res = insert.executeUpdate();
    		
    		if(res == 0){
    			System.err.println("Insertion fail");
    		} else if(res == 1){
    			System.out.println("Insertion done");
    		}
    		
    		conn.commit();
    		
    		
    		
	    } catch(SQLException esql){
			System.err.println("Erreur execution update " + esql);
		} catch(Exception e){
			System.err.println("Erreur changerFonctionCage");
		}
	
    }

}