import java.io.*;
import java.util.Scanner;

public class Main {

    public static void main(String[] args) throws IOException {
        File file = new File("saada.csv");
        menu(file);
    }

    public static void menu(File file) throws IOException {
        GestioneCSV csv = new GestioneCSV();
        Scanner sc = new Scanner(System.in);
        int scelta;

        do {
            System.out.println("1) Aggiungi campo controllo random");
            System.out.println("2) Aggiungi campo record attivo");
            System.out.println("3) Numero campi");
            System.out.println("4) Conteggio valori non vuoti");
            System.out.println("5) Dimensione fissa");
            System.out.println("6) Aggiungi record");
            System.out.println("7) Leggi campo");
            System.out.println("8) Modifica record");
            System.out.println("9) Disattiva record");
            System.out.println("10) Riattiva record");
            System.out.println("0) Esci");

            scelta = Integer.parseInt(sc.nextLine());

            switch (scelta) {
                case 1 -> csv.aggiungiCampoControllo(file);
                case 2 -> csv.aggiungiCampoAttivo(file);
                case 3 -> System.out.println("Campi: " + csv.numeroCampi(file));
                case 4 -> {
                    int[] c = csv.contaRecordNonVuoti(file);
                    for (int i : c) System.out.print(i + " ");
                    System.out.println();
                }
                case 5 -> csv.rendiDimensioneFissa(file);
                case 6 -> {
                    String[] nuovo = new String[csv.numeroCampi(file)];
                    for (int i = 0; i < nuovo.length; i++) nuovo[i] = "0";
                    csv.aggiungiRiga(nuovo, file);
                }
                case 7 -> {
                    System.out.print("Campo: ");
                    String campo = sc.nextLine();
                    System.out.print("Riga: ");
                    int riga = Integer.parseInt(sc.nextLine());
                    System.out.println(csv.leggiCampo(campo, riga, file));
                }
                case 8 -> {
                    System.out.print("Campo: ");
                    String campo = sc.nextLine();
                    System.out.print("Riga: ");
                    int riga = Integer.parseInt(sc.nextLine());
                    System.out.print("Nuovo valore: ");
                    String valore = sc.nextLine();
                    csv.aggiornaCampo(campo, riga, valore, file);
                }
                case 9 -> csv.disattivaRecord(Integer.parseInt(sc.nextLine()), file);
                case 10 -> csv.riattivaRecord(Integer.parseInt(sc.nextLine()), file);
            }

        } while (scelta != 0);
    }
}
