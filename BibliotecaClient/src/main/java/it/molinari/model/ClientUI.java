package it.molinari.model;
import java.util.Scanner;
import it.molinari.service.ClientManager;

public class ClientUI {

    private ClientManager manager = new ClientManager();
    private Scanner input = new Scanner(System.in);

    public void start() {
    	
    	int scelta = 0;
        do {
            System.out.println("\n--- MENU ---");
            System.out.println("1) Login");
            System.out.println("2) Registrazione");
            System.out.println("0) Esci");
            System.out.print("Scelta: ");

            scelta = Integer.parseInt(input.nextLine());

            switch (scelta) {

                case 1:
                	doLogin();
                	break;
                case 2:
                	doRegistration();
                	break;
                case 0:
                    System.out.println("Uscita...");
                    manager.close();
                    return;
                default:
                	System.out.println("Scelta non valida.");
            }
        } while (scelta == 0);
    }
    private void menuUser() {
    	
    	int scelta = 0;
    	
        do {
        	
            System.out.println("\n=== MENU UTENTE ===");
            System.out.println("1) Visualizza Libri");
            System.out.println("2) Visualizza CD");
            System.out.println("3) Effettua prestito");
            System.out.println("4) Restituzione item");
            System.out.println("0) Logout");
            System.out.print("Scelta: ");

            scelta = Integer.parseInt(input.nextLine());

            switch (scelta) {
                case 1:
                	System.out.println("WIP");
                	break;
                case 2:
                	System.out.println("WIP");
                	break;
                case 3:
                	System.out.println("WIP");
                	break;
                case 4:
                	System.out.println("WIP");
                	break;
                case 0:
                    System.out.println("Logout effettuato.");
                    manager.close();
                    return;
                default:
                	System.out.println("Scelta non valida.");
                	break;
            } 
        } while (scelta != 0);
    }

    private void menuAdmin() {
    	
    	int scelta = 0;
    	
        do {
            System.out.println("\n=== MENU ADMIN ===");
            System.out.println("1) Aggiungi Elemento");
            System.out.println("2) Rimuovi Elemento");
            System.out.println("3) Verifica Prestiti");
            System.out.println("4) Elementi in magazzino");
            System.out.println("5) Lista utenti");
            System.out.println("0) Logout");
            System.out.print("Scelta: ");

            scelta = Integer.parseInt(input.nextLine());

            switch (scelta) {
	            case 1:
	            	System.out.println("WIP");
	            	break;
	            case 2:
	            	System.out.println("WIP");
	            	break;
	            case 3:
	            	System.out.println("WIP");
	            	break;
	            case 4:
	            	System.out.println("WIP");
	            	break;
	            case 5:
	            	System.out.println("WIP");
	            	break;
                case 0:
                    System.out.println("Logout effettuato.");
                    manager.close();
                    return;
                default:
                	System.out.println("Scelta non valida.");
            }
        } while (scelta != 0);
    }
    private void doLogin() {
        System.out.print("Username: ");
        String user = input.nextLine();

        System.out.print("Password: ");
        String pass = input.nextLine();

        if (manager.login(user, pass)) {
            System.out.println("Login riuscito!");
            System.out.println("Token: " + manager.getToken());
            System.out.println("Admin: " + manager.isAdmin());
            
            if (manager.isAdmin()) {
            	menuAdmin();
            } else {
            	menuUser();
            }
        } else {
            System.out.println("Login fallito.");
        }
    }

    private void doRegistration() {
        System.out.print("Nome: ");
        String nome = input.nextLine();

        System.out.print("Cognome: ");
        String cognome = input.nextLine();

        System.out.print("Username: ");
        String user = input.nextLine();

        System.out.print("Password: ");
        String pass = input.nextLine();

        if (manager.register(nome, cognome, user, pass)) {
            System.out.println("Registrazione completata!");
            if (manager.isAdmin()) {
            	menuAdmin();
            } else {
            	menuUser();
            }
        } else {
            System.out.println("Errore nella registrazione.");
        }
    }
}