package it.molinari.model;
import java.util.List;
import java.util.Scanner;
import it.molinari.service.ClientManager;

public class ClientUI {

    private Scanner input = new Scanner(System.in);
    private ClientManager manager = new ClientManager();

    public void start() {
        while (true) {
            System.out.println("\n=== MENU PRINCIPALE ===");
            System.out.println("1) Login");
            System.out.println("2) Registrazione");
            System.out.println("0) Esci");

            System.out.print("Scelta: ");
            String scelta = input.nextLine();

            switch (scelta) {
                case "1":
                	login();
                	break;
                case "2":
                	registrazione();
                	break;
                case "0":
                    manager.close();
                    return;
                default:
                	System.out.println("Scelta non valida.");
                	break;
            }
        }
    }

    private void login() {
        System.out.print("Username: ");
        String user = input.nextLine();

        System.out.print("Password: ");
        String pass = input.nextLine();

        if (manager.login(user, pass)) {
            System.out.println("Login effettuato.");
            if (manager.isAdmin()) menuAdmin();
            else menuUser();
        } else {
            System.out.println("Credenziali non valide.");
        }
    }

    private void registrazione() {
        System.out.print("Nome: ");
        String nome = input.nextLine();

        System.out.print("Cognome: ");
        String cognome = input.nextLine();

        System.out.print("Username: ");
        String user = input.nextLine();

        System.out.print("Password: ");
        String pass = input.nextLine();

        if (manager.register(nome, cognome, user, pass)) {
            System.out.println("Registrazione completata.");
        } else {
            System.out.println("Errore nella registrazione.");
        }
    }

    private void menuUser() {
        while (true) {
            System.out.println("\n=== MENU UTENTE ===");
            System.out.println("1) Visualizza magazzino");
            System.out.println("2) Cerca item");
            System.out.println("3) Effettua prestito");
            System.out.println("4) Restituisci item");
            System.out.println("5) Visualizza prestiti");
            System.out.println("0) Logout");

            System.out.print("Scelta: ");
            String scelta = input.nextLine();

            switch (scelta) {
                case "1":
                	visualizzaMagazzino();
                	break;
                case "2":
                	cercaItem();
                	break;
                case "3":
                	effettuaPrestito();
                	break;
                case "4":
                	restituisci();
                	break;
                case "5":
                	visualizzaPrestitiUtente();
                	break;
                case "0":
                	return;
                default:
                	System.out.println("Scelta non valida.");
                	break;
            }
        }
    }

    private void menuAdmin() {
        while (true) {
            System.out.println("\n=== MENU ADMIN ===");
            System.out.println("1) Visualizza magazzino");
            System.out.println("2) Cerca item");
            System.out.println("3) Aggiungi item");
            System.out.println("4) Rimuovi item");
            System.out.println("5) Effettua prestito");
            System.out.println("6) Restituisci item");
            System.out.println("7) Visualizza prestiti utente");
            System.out.println("0) Logout");

            System.out.print("Scelta: ");
            String scelta = input.nextLine();

            switch (scelta) {
                case "1":
                	visualizzaMagazzino();
                	break;
                case "2":
                	cercaItem();
                	break;
                case "3":
                	aggiungiItem();
                	break;
                case "4":
                	rimuoviItem();
                	break;
                case "5":
                	effettuaPrestito();
                	break;
                case "6":
                	restituisci();
                	break;
                case "7":
                	visualizzaPrestitiUtente();
                	break;
                case "0":
                	return;
                default:
                	System.out.println("Scelta non valida.");
                	break;
            }
        }
    }

    private void visualizzaMagazzino() {
        List<Item> lista = manager.getItems();

        if (lista == null || lista.isEmpty()) {
            System.out.println("Magazzino vuoto.");
            return;
        }

        System.out.println("\n=== MAGAZZINO ===");
        for (Item i : lista) {
            System.out.println("ID: " + i.getId());
            System.out.println("Nome: " + i.getNome());
            System.out.println("Autore: " + i.getAutore());
            System.out.println("Tipologia: " + i.getTipologia());
            System.out.println("Quantità: " + i.getQuanti());
            System.out.println("------------------------");
        }
    }

    private void cercaItem() {
        System.out.println("\nModalità di ricerca:");
        System.out.println("1) Per titolo/nome");
        System.out.println("2) Per tipologia");
        System.out.println("3) Per codice (ID)");
        System.out.print("Scelta: ");

        int modalita = Integer.parseInt(input.nextLine());

        System.out.print("Valore da cercare: ");
        String valore = input.nextLine();

        List<Item> risultati = manager.cerca(valore, modalita);

        if (risultati == null || risultati.isEmpty()) {
            System.out.println("Nessun item trovato.");
            return;
        }

        System.out.println("\n=== RISULTATI ===");
        for (Item i : risultati) {
            System.out.println("ID: " + i.getId());
            System.out.println("Nome: " + i.getNome());
            System.out.println("Autore: " + i.getAutore());
            System.out.println("Tipologia: " + i.getTipologia());
            System.out.println("Quantità: " + i.getQuanti());
            System.out.println("------------------------");
        }
    }

    private void effettuaPrestito() {
        visualizzaMagazzino();

        System.out.print("ID dell’item da prendere in prestito: ");
        int id = Integer.parseInt(input.nextLine());

        System.out.print("Quantità: ");
        int quantita = Integer.parseInt(input.nextLine());

        List<Item> items = manager.getItems();
        Item selezionato = null;

        for (Item i : items) {
            if (i.getId() == id) {
                selezionato = i;
                break;
            }
        }

        if (selezionato == null) {
            System.out.println("ID non valido.");
            return;
        }

        if (manager.effettuaPrestito(selezionato, quantita)) {
            System.out.println("Prestito effettuato.");
        } else {
            System.out.println("Errore nel prestito.");
        }
    }

    private void restituisci() {
        System.out.print("ID dell’item da restituire: ");
        int id = Integer.parseInt(input.nextLine());

        System.out.print("Quantità: ");
        int quantita = Integer.parseInt(input.nextLine());

        if (manager.restituisciItem(id, quantita)) {
            System.out.println("Restituzione completata.");
        } else {
            System.out.println("Errore nella restituzione.");
        }
    }

    private void visualizzaPrestitiUtente() {
        var lista = manager.getPrestitiUtente();

        if (lista == null || lista.isEmpty()) {
            System.out.println("Nessun prestito attivo.");
            return;
        }

        System.out.println("\n=== PRESTITI UTENTE ===");
        for (var p : lista) {
            System.out.println("ID Prestito: " + p.getId());
            System.out.println("Item: " + (p.getItem() != null ? p.getItem().getNome() : "??"));
            System.out.println("Quantità: " + p.getQuanti());
            System.out.println("Inizio: " + p.getInizioPrestito());
            System.out.println("Fine: " + p.getFinePrestito());
            System.out.println("-----------------------");
        }
    }

    private void aggiungiItem() {
        System.out.print("Tipo (libro/rivista/cd): ");
        String tipo = input.nextLine().trim().toLowerCase();

        System.out.print("Nome: ");
        String nome = input.nextLine();

        System.out.print("Autore: ");
        String autore = input.nextLine();

        System.out.print("Quantità: ");
        int quanti = Integer.parseInt(input.nextLine());

        Item nuovo = null;

        if (tipo.equals("libro")) {
            System.out.print("ISBN: ");
            String isbn = input.nextLine();

            System.out.print("Genere: ");
            String genere = input.nextLine();

            nuovo = new Libro(nome, autore, isbn, genere, quanti);

        }
        else if (tipo.equals("rivista")) {
            System.out.print("Edizione: ");
            String ed = input.nextLine();

            System.out.print("Periodicità: ");
            String per = input.nextLine();

            nuovo = new Rivista(nome, autore, ed, per, "Rivista", quanti);
        }
        else if (tipo.equals("cd")) {
            System.out.print("Numero tracce: ");
            int tr = Integer.parseInt(input.nextLine());

            System.out.print("Durata: ");
            int dur = Integer.parseInt(input.nextLine());

            nuovo = new Cd(nome, autore, tr, dur, "Cd", quanti);
        }
        else {
            System.out.println("Tipo non valido.");
            return;
        }

        if (manager.aggiungiItem(nuovo)) {
            System.out.println("Item aggiunto.");
        } else {
            System.out.println("Errore nell’aggiunta.");
        }
    }

    private void rimuoviItem() {
        System.out.print("Tipo (libro/rivista/cd): ");
        String tipo = input.nextLine().trim().toLowerCase();

        System.out.print("Nome: ");
        String nome = input.nextLine();

        System.out.print("Autore: ");
        String autore = input.nextLine();

        System.out.print("Quantità da rimuovere: ");
        int quanti = Integer.parseInt(input.nextLine());

        Item item = null;

        if (tipo.equals("libro")) {
            System.out.print("ISBN: ");
            String isbn = input.nextLine();

            System.out.print("Genere: ");
            String genere = input.nextLine();

            item = new Libro(nome, autore, isbn, genere, quanti);
        }
        else if (tipo.equals("rivista")) {
            System.out.print("Edizione: ");
            String ed = input.nextLine();

            System.out.print("Periodicità: ");
            String per = input.nextLine();

            item = new Rivista(nome, autore, ed, per, "Rivista", quanti);
        }
        else if (tipo.equals("cd")) {
            System.out.print("Numero tracce: ");
            int tr = Integer.parseInt(input.nextLine());

            System.out.print("Durata: ");
            int dur = Integer.parseInt(input.nextLine());

            item = new Cd(nome, autore, tr, dur, "Cd", quanti);
        }
        else {
            System.out.println("Tipo non valido.");
            return;
        }

        if (manager.rimuoviItem(item)) {
            System.out.println("Item rimosso.");
        } else {
            System.out.println("Errore nella rimozione.");
        }
    }
}