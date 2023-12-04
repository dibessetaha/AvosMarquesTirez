import java.util.Scanner;

public class Main {
    public static void main(String[] args) {
        JeuFlechettes flechettes = new JeuFlechettes() ;
        Scanner scanner = new Scanner(System.in);
        String[] tableauCoordonnees = {
                "9 26", // vitesse et angle
                "8.5 27",
                "9 24",
                "9.6 30",
                "8.5 28",
                "8.3 27",
                "9.2 21",
                "10 25",
                "10.1 24.8",
                "12 20"

        };

        int choixSource = afficherMenu0(scanner);
        switch (choixSource) {
            case 1:
                flechettes.setCoordonnees(tableauCoordonnees);
                break;
            case 2:
                flechettes.setCoordonnees(flechettes.lireCoordonneesDepuisFichier("ressources/coord.txt"));
                break;
            default:
                System.out.println("Choix invalide");
        }

        int choixMenu1 = afficherMenu1(scanner);

        switch (choixMenu1) {
            case 1:
                flechettes.jouerPartie(flechettes.getCoordonnees());
                break;
            case 2:
                flechettes.enregistrerLesPointsTries(flechettes.getCoordonnees());
                break;
            case 3:
                flechettes.calculerEtEstiméeHauteurMaximale(flechettes.getCoordonnees());
                break;
            case 4:
                flechettes.quitter();
                break;
            default:
                System.out.println("Choisissez un nombre entre 1 et 4 !");
        }

    }


    public static int afficherMenu0(Scanner scanner) {
        System.out.println("************** Menu 0***************************");
        System.out.println("*1 – Extraire les coordonnées du tableau      *");
        System.out.println("*2 – Extraire les coordonnées du fichier      *");
        System.out.println("*********************************************");

        System.out.print("Choisissez la source des données : ");
        return scanner.nextInt();
    }

    public static int afficherMenu1(Scanner scanner) {
        System.out.println("************** Menu 1 **************************");
        System.out.println("*1- Jouer une partie                           *");
        System.out.println("*2- Enregistrer les points dans le fichier     *");
        System.out.println("*3- Calculer/Estimer et afficher la hauteur maximale de chaque tir. Afficher l’erreur     *");
        System.out.println("*4- Quitter                                    *");
        System.out.println("***********************************************");

        System.out.print("Choisissez une option : ");
        return scanner.nextInt();
    }







}
