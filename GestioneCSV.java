import java.io.*;

public class GestioneCSV {

    private String[][] dati;
    private int righe;
    private int colonne;
    private final int MAX_RIGHE = 500;
    private final int MAX_COLONNE = 15;

    public GestioneCSV() {
        dati = new String[MAX_RIGHE][MAX_COLONNE];
        righe = 0;
        colonne = 0;
    }

    public void leggiFile() {
        try (BufferedReader br = new BufferedReader(new FileReader("src/saada.csv"))) {
            String riga;

            // conto colonne
            riga = br.readLine();
            if (riga != null) {
                colonne = riga.split("\t").length;
            }

            // leggo dati
            while ((riga = br.readLine()) != null && righe < MAX_RIGHE) {
                String[] campi = riga.split("\t");
                
                // creo record con metodo
                aggiungiRecord(campi);
            }
        } catch (IOException e) {
            System.out.println("Errore nella lettura del file: " + e.getMessage());
        }
    }

    public void aggiungiRecord(String[] record) {
        if (righe < MAX_RIGHE) {
            // copio dati record
            for (int i = 0; i < record.length; i++) {
                dati[righe][i] = record[i];
            }

            // aggiungo due campi extra
            // valore tra 10 e 20
            dati[righe][colonne] = "" + (10 + (int) (Math.random() * 11));
            dati[righe][colonne + 1] = "0";
            righe++;
        } else {
            System.out.println("finito MAX_RIGHE");
        }
    }

    public int contaCampi() {
        return colonne + 2;
    }

    public int lunghezzaMassimaRecord() {
        int max = 0;
        for (int i = 0; i < righe; i++) {
            int len = 0;
            for (int j = 0; j < contaCampi(); j++) {
                if (dati[i][j] != null) {
                    len += dati[i][j].length();
                }
            }
            if (len > max) {
                max = len;
            }
        }
        return max;
    }

    // inserire in ogni record un numero di spazi necessari a rendere fissa la dimensione di tutti i record, senza perdere informazioni. 

}
