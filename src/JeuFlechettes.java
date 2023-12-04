import java.io.*;
import java.text.DecimalFormat;

public class JeuFlechettes {

    private String[] coordonnees ;

    private double[] angles ;

    private double[] vitesses ;

    private int[] pointage = {100,50,25,0} ;

    public String[] getCoordonnees() {
        return coordonnees;
    }

    public void setCoordonnees(String[] coordonnees) {
        this.coordonnees = coordonnees;
    }


    public String[] lireCoordonneesDepuisFichier(String nomFichier) {
        try {
            BufferedReader reader = new BufferedReader(new FileReader("ressources/" + nomFichier));
            String line;
            int i = 0;
            int taille = calculerLesLignesDuFichier(nomFichier) ;
            coordonnees = new String[taille];
            while ((line = reader.readLine()) != null) {
                coordonnees[i] = line;
                i++;
            }
            reader.close();
        } catch (IOException e) {
            e.printStackTrace();
            return new String[0];
        }
        return coordonnees;
    }


    public  void jouerPartie(String[] coordonnees) {
        for (int i = 0; i < coordonnees.length; i++) {
            String[] parts = coordonnees[i].split(" ");
            double vitesse = Double.parseDouble(parts[0]);
            double angle = Double.parseDouble(parts[1]);
            double tImpact = calculerTempsImpact(vitesse, angle);
            double position = calculerPosition(vitesse, angle, tImpact);
            double pointage = calculerPointage(position);
            System.out.println("A Vos Marques Tirez ...... " + (i + 1) + ":");
            System.out.println("Temps d'impact : " + tImpact);
            System.out.println("Position de la fléchette : " + position);
            System.out.println("Pointage : " + pointage);
        }
    }


    public  void enregistrerLesPoints(String[] coordonnees) {
        double[][] coordonees = convertirEnTableauDeDoubles(coordonnees) ;
        try {
            BufferedWriter writer = new BufferedWriter(new FileWriter("ressources/points.txt"));
            for (int i = 0 ; i<coordonees.length ; i++) {
                double angle = coordonees[i][0];
                double vitesse = coordonees[i][1];
                double tImpact = calculerTempsImpact(vitesse, angle);
                double position = calculerPosition(vitesse, angle, tImpact);
                int pointage = calculerPointage(position);
                writer.write("\n" + (int)(i+1) + " " + pointage);

            }
            writer.close();
            afficherFichier("ressources/points.txt");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public double[][] convertirEnTableauDeDoubles(String[] coordonnees) {
        double[][] result = new double[coordonnees.length][2];
        for (int i = 0; i < coordonnees.length; i++) {
            String[] parts = coordonnees[i].split(" ");
            double angle = Double.parseDouble(parts[0]);
            double vitesse = Double.parseDouble(parts[1]);
            result[i][0] = angle;
            result[i][1] = vitesse;
        }
        return result;
    }



    public  void calculerEtEstiméeHauteurMaximale(String[] coordonnees) {
        DecimalFormat decimalFormat = new DecimalFormat("0.###############");
        double hauteurInitial = 1.50 ;
        for (String coordonnee : coordonnees) {
            String[] parts = coordonnee.split(" ");
            double vitesse = Double.parseDouble(parts[0]);
            double angle = Double.parseDouble(parts[1]) ;

            double hauteurMaxC = calculerHauteurMaximale(vitesse, angle);
            double hauteurMaxE = estimerHauteurMaximale(vitesse,angle,hauteurInitial) ;
            double erreur = Math.abs(hauteurMaxE - hauteurMaxC);; // Erreur par rapport à la hauteur initiale

            String hauteurMaxCFormatted = decimalFormat.format(hauteurMaxC);
            String hauteurMaxEFormatted = decimalFormat.format(hauteurMaxE);

            System.out.println("********************************************");
            System.out.println("Hauteur calculee maximale de la trajectoire : " + hauteurMaxCFormatted);
            System.out.println("Hauteur estimée maximale de la trajectoire : " + hauteurMaxEFormatted);
            System.out.println("Erreur par rapport à la hauteur initiale : " + erreur);
        }
    }



    public  void quitter() {
        System.out.println("Merci d'avoir utilisé le simulateur de jeu de fléchettes. Au revoir !");
        System.exit(0);
    }



    public  double calculerHauteurMaximale(double vitesse, double angle) {
        double g = 9.8; // Accélération gravitationnelle
        double thetaRadians = Math.toRadians(angle); // Conversion de degrés à radians
        // Formule pour calculer la hauteur maximale
        double hauteurMaximale = (Math.pow(vitesse, 2) * Math.pow(Math.sin(thetaRadians), 2)) / (2 * g);

        return hauteurMaximale;
    }

    public double estimerHauteurMaximale(double vitesseInitiale, double angleTir, double hauteurInitiale) {
        double g = 9.8; // Accélération gravitationnelle
        double thetaRadians = Math.toRadians(angleTir); // Conversion de degrés à radians
        double t = 0.0; // Temps initial
        double increment = 0.1; // Pas d'incrémentation du temps
        double hauteurEstimee;
        // Boucle jusqu'à ce que yn+1 < yn ou t >= timpact
        while (true) {
            hauteurEstimee = -0.5 * g * Math.pow(t, 2) + vitesseInitiale * Math.sin(thetaRadians) * t + hauteurInitiale;
            // Calcul de la hauteur maximale en utilisant la fonction précédente
            double hauteurCalculee = calculerHauteurMaximale(vitesseInitiale, angleTir);
            // Condition d'arrêt
            if (hauteurEstimee < hauteurCalculee || t >= hauteurCalculee / (vitesseInitiale * Math.cos(thetaRadians))) {
                break;
            }
            // Incrémentation du temps
            t += increment;
        }
        return hauteurEstimee ;
    }





    public  double calculerTempsImpact(double vitesse, double angle) {
        return 2.40 / (vitesse * Math.cos(Math.toRadians(angle)));
    }

    public  double calculerPosition(double vitesse, double angle, double tImpact) {
        return -0.5 * 9.8 * Math.pow(tImpact, 2) + (vitesse * Math.sin(Math.toRadians(angle))) * tImpact + 1.5;
    }

    public  int calculerPointage(double position) {
            double hauteurCentreCible = 2.20;
            double rayonBoutonCentral = 0.03;
            double largeurAnneau = 0.02;
            double distanceVerticale = Math.abs(position - hauteurCentreCible);

            if (distanceVerticale <= rayonBoutonCentral) {
                return pointage[0];
            } else if (distanceVerticale <= rayonBoutonCentral + largeurAnneau) {
                return pointage[1];
            } else if (distanceVerticale <= rayonBoutonCentral + 2 * largeurAnneau) {
                return pointage[2];
            } else {
                return pointage[3];
            }
        }

    public void afficherFichier(String file) {
        try {
            BufferedReader reader = new BufferedReader(new FileReader(file));
            String line;
            int i = 0;
            while ((line = reader.readLine()) != null) {
                System.out.println(line);
            }
            reader.close();
        } catch (IOException e) {
            e.printStackTrace();
        }


    }

    public void afficherTableau(Object[] tab, int lenght){

        for (int i=0;i<lenght;i++){
            System.out.println(tab[i+1]);
        }
    }

    public int calculerLesLignesDuFichier(String filePath) throws IOException {
        BufferedReader reader = new BufferedReader(new FileReader(filePath));
        int lineCount = 0;
        try {
            while (reader.readLine() != null) {
                lineCount++;
            }
        } finally {
            reader.close();
        }

        return lineCount;
    }




}

