package com.university.finance;

import com.university.finance.model.*;
import com.university.finance.pattern.factory.UserFactory;
import com.university.finance.pattern.factory.AccountFactory;
import com.university.finance.pattern.observer.AuditLogger;
import com.university.finance.pattern.observer.NotificationService;
import com.university.finance.service.BankingService;

import java.util.List;
import java.util.Scanner;

public class Main {
    private static BankingService bankingService = new BankingService();
    private static Scanner scanner = new Scanner(System.in);
    private static UserFactory userFactory = new UserFactory();
    private static AccountFactory accountFactory = new AccountFactory();

    public static void main(String[] args) {
        // Configurer les observateurs
        bankingService.getNotifier().addObserver(new AuditLogger());
        bankingService.getNotifier().addObserver(new NotificationService());

        System.out.println("=== Système Bancaire Refactoré ===");
        System.out.println("Bienvenue dans l'application bancaire!");

        boolean running = true;
        User currentUser = null;

        while (running) {
            if (currentUser == null) {
                currentUser = loginMenu();
                if (currentUser == null) {
                    System.out.println("Voulez-vous quitter? (o/n)");
                    if (scanner.next().equalsIgnoreCase("o")) {
                        running = false;
                    }
                }
            } else {
                running = mainMenu(currentUser);
            }
        }

        scanner.close();
        System.out.println("Merci d'avoir utilisé notre système bancaire!");
    }

    private static User loginMenu() {
        System.out.println("\n=== MENU CONNEXION ===");
        System.out.println("1. Se connecter");
        System.out.println("2. Créer un compte");
        System.out.println("3. Quitter");
        System.out.print("Choix: ");

        int choice = scanner.nextInt();
        scanner.nextLine(); // Consommer la nouvelle ligne

        switch (choice) {
            case 1:
                return login();
            case 2:
                return createUser();
            case 3:
                return null;
            default:
                System.out.println("Choix invalide!");
                return null;
        }
    }

    private static User login() {
        System.out.print("Nom d'utilisateur: ");
        String username = scanner.nextLine();
        System.out.print("Mot de passe: ");
        String password = scanner.nextLine();

        User user = bankingService.authenticateUser(username, password);
        if (user != null) {
            System.out.println("Connexion réussie! Bienvenue " + username);
            return user;
        } else {
            System.out.println("Identifiants incorrects!");
            return null;
        }
    }

    private static User createUser() {
        System.out.print("Nom d'utilisateur: ");
        String username = scanner.nextLine();
        System.out.print("Mot de passe: ");
        String password = scanner.nextLine();
        System.out.print("Type (STANDARD/PREMIUM): ");
        String type = scanner.nextLine();

        User user = userFactory.createUser(username, password, type);
        bankingService.createUser(username, password,
                type.equalsIgnoreCase("PREMIUM") ? UserType.PREMIUM : UserType.STANDARD);

        System.out.println("Utilisateur créé avec succès!");
        return user;
    }

    private static boolean mainMenu(User user) {
        System.out.println("\n=== MENU PRINCIPAL ===");
        System.out.println("Utilisateur: " + user.getUsername());
        System.out.println("1. Afficher mes comptes");
        System.out.println("2. Créer un compte");
        System.out.println("3. Effectuer un dépôt");
        System.out.println("4. Effectuer un retrait");
        System.out.println("5. Effectuer un transfert");
        System.out.println("6. Voir l'historique");
        System.out.println("7. Se déconnecter");
        System.out.println("0. Quitter l'application");
        System.out.print("Choix: ");

        int choice = scanner.nextInt();
        scanner.nextLine(); // Consommer la nouvelle ligne

        switch (choice) {
            case 1:
                displayUserAccounts(user);
                break;
            case 2:
                createAccount(user);
                break;
            case 3:
                performDeposit(user);
                break;
            case 4:
                performWithdrawal(user);
                break;
            case 5:
                performTransfer(user);
                break;
            case 6:
                displayTransactionHistory(user);
                break;
            case 7:
                System.out.println("Déconnexion...");
                return true; // Retourne true pour continuer mais déconnecter
            case 0:
                return false; // Retourne false pour quitter
            default:
                System.out.println("Choix invalide!");
        }

        return true;
    }

    private static void displayUserAccounts(User user) {
        List<Account> accounts = bankingService.getUserAccounts(user.getUserId());
        if (accounts.isEmpty()) {
            System.out.println("Vous n'avez aucun compte.");
        } else {
            System.out.println("\nVos comptes:");
            for (Account account : accounts) {
                System.out.printf("ID: %s | Solde: %.2f € | Type: %s%n",
                        account.getAccountId(),
                        account.getBalance(),
                        account.getType());
            }
        }
    }

    private static void createAccount(User user) {
        System.out.print("Type de compte (CHECKING/SAVINGS): ");
        String type = scanner.nextLine();
        System.out.print("Dépôt initial: ");
        double initialDeposit = scanner.nextDouble();
        scanner.nextLine(); // Consommer la nouvelle ligne

        Account account = accountFactory.createAccount(user.getUserId(), initialDeposit, type);
        bankingService.createAccount(user.getUserId(), initialDeposit,
                type.equalsIgnoreCase("SAVINGS") ? AccountType.SAVINGS : AccountType.CHECKING);

        System.out.println("Compte créé avec succès! ID: " + account.getAccountId());
    }

    private static void performDeposit(User user) {
        displayUserAccounts(user);
        System.out.print("ID du compte pour le dépôt: ");
        String accountId = scanner.nextLine();
        System.out.print("Montant à déposer: ");
        double amount = scanner.nextDouble();
        scanner.nextLine(); // Consommer la nouvelle ligne

        try {
            bankingService.deposit(accountId, amount);
            System.out.println("Dépôt effectué avec succès!");
        } catch (Exception e) {
            System.out.println("Erreur: " + e.getMessage());
        }
    }

    private static void performWithdrawal(User user) {
        displayUserAccounts(user);
        System.out.print("ID du compte pour le retrait: ");
        String accountId = scanner.nextLine();
        System.out.print("Montant à retirer: ");
        double amount = scanner.nextDouble();
        scanner.nextLine(); // Consommer la nouvelle ligne

        try {
            bankingService.withdraw(accountId, amount);
            System.out.println("Retrait effectué avec succès!");
        } catch (Exception e) {
            System.out.println("Erreur: " + e.getMessage());
        }
    }

    private static void performTransfer(User user) {
        displayUserAccounts(user);
        System.out.print("ID du compte source: ");
        String fromAccountId = scanner.nextLine();
        System.out.print("ID du compte destination: ");
        String toAccountId = scanner.nextLine();
        System.out.print("Montant à transférer: ");
        double amount = scanner.nextDouble();
        scanner.nextLine(); // Consommer la nouvelle ligne

        try {
            bankingService.transfer(fromAccountId, toAccountId, amount);
            System.out.println("Transfert effectué avec succès!");
        } catch (Exception e) {
            System.out.println("Erreur: " + e.getMessage());
        }
    }

    private static void displayTransactionHistory(User user) {
        List<Transaction> transactions = bankingService.getUserTransactionHistory(user.getUserId());
        if (transactions.isEmpty()) {
            System.out.println("Aucune transaction trouvée.");
        } else {
            System.out.println("\nHistorique des transactions:");
            for (Transaction transaction : transactions) {
                System.out.printf("[%s] %s: %.2f € | %s%n",
                        transaction.getTimestamp(),
                        transaction.getType(),
                        transaction.getAmount(),
                        transaction.getDescription());
            }
        }
    }
}