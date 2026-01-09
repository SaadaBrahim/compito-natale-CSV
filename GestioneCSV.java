import java.io.*;
import java.nio.file.Files;
import java.nio.file.StandardCopyOption;

public class GestioneCSV {


    private void inserisciCampo(String nomeCampo, String[] valori, File file) throws IOException {
        try {
            int pos = trovaIndiceCampo(nomeCampo, file);
            completaValoriMancanti(pos, valori, file);
        } catch (CampoNotFoundException e) {
            File temp = new File("temp.csv");
            BufferedReader br = new BufferedReader(new FileReader(file));
            PrintWriter pw = new PrintWriter(new FileWriter(temp));

            String header = br.readLine();
            pw.println(header + "," + nomeCampo);

            String riga;
            int i = 0;
            while ((riga = br.readLine()) != null) {
                if (i < valori.length)
                    pw.println(riga + "," + valori[i]);
                else
                    pw.println(riga + ",");
                i++;
            }

            br.close();
            pw.close();
            Files.move(temp.toPath(), file.toPath(), StandardCopyOption.REPLACE_EXISTING);
        }
    }

    public void aggiungiCampoControllo(File file) throws IOException {
        String nomeCampo = "Controllo Random";
        int righe = contaRighe(file) - 1;
        String[] valori = new String[righe];

        for (int i = 0; i < valori.length; i++) {
            valori[i] = String.valueOf((int) (Math.random() * 1000));
        }

        inserisciCampo(nomeCampo, valori, file);
    }

    public void aggiungiCampoAttivo(File file) throws IOException {
        String nomeCampo = "Record Attivo";
        int righe = contaRighe(file) - 1;
        String[] valori = new String[righe];

        for (int i = 0; i < valori.length; i++) {
            valori[i] = "true";
        }

        inserisciCampo(nomeCampo, valori, file);
    }

    private void completaValoriMancanti(int indice, String[] valori, File file) {
        // lasciato intenzionalmente vuoto (come consegna originale)
    }

        // lettura
    private int contaRighe(File file) throws IOException {
        BufferedReader br = new BufferedReader(new FileReader(file));
        int n = 0;
        while (br.readLine() != null) n++;
        br.close();
        return n;
    }

    public int numeroCampi(File file) throws IOException {
        return leggiCampi(file).length;
    }

    public String[] leggiCampi(File file) throws IOException {
        BufferedReader br = new BufferedReader(new FileReader(file));
        String[] campi = br.readLine().split(",");
        br.close();
        return campi;
    }

    public int[] contaRecordNonVuoti(File file) throws IOException {
        int[] contatori = new int[numeroCampi(file)];
        BufferedReader br = new BufferedReader(new FileReader(file));
        String riga;

        while ((riga = br.readLine()) != null) {
            String[] split = riga.split(",");
            for (int i = 0; i < split.length; i++) {
                if (!split[i].trim().isEmpty())
                    contatori[i]++;
            }
        }
        br.close();
        return contatori;
    }

    //format

    private int lunghezzaMax(File file, int colonna) throws IOException {
        BufferedReader br = new BufferedReader(new FileReader(file));
        String riga;
        int max = 0;

        while ((riga = br.readLine()) != null) {
            String[] split = riga.split(",");
            if (split[colonna].length() > max)
                max = split[colonna].length();
        }
        br.close();
        return max;
    }

    public void rendiDimensioneFissa(File file) throws IOException {
        File temp = new File("temp.csv");
        PrintWriter pw = new PrintWriter(new FileWriter(temp));
        BufferedReader br = new BufferedReader(new FileReader(file));

        int campi = numeroCampi(file);
        int[] max = new int[campi];
        for (int i = 0; i < campi; i++)
            max[i] = lunghezzaMax(file, i);

        String riga;
        while ((riga = br.readLine()) != null) {
            String[] split = riga.split(",");
            String nuova = "";
            for (int i = 0; i < split.length; i++) {
                nuova += split[i];
                while (nuova.length() < max[i]) nuova += " ";
                if (i < split.length - 1) nuova += ",";
            }
            pw.println(nuova);
        }

        br.close();
        pw.close();
        Files.move(temp.toPath(), file.toPath(), StandardCopyOption.REPLACE_EXISTING);
    }

    //record

    public void aggiungiRiga(String[] record, File file) throws IOException {
        PrintWriter pw = new PrintWriter(new FileWriter(file, true));
        pw.println(String.join(",", record));
        pw.close();
    }

    public String leggiCampo(String campo, int riga, File file) throws IOException {
        BufferedReader br = new BufferedReader(new FileReader(file));
        int indice = trovaIndiceCampo(campo, file);

        for (int i = 0; i < riga; i++)
            br.readLine();

        String valore = br.readLine().split(",")[indice];
        br.close();
        return valore;
    }

    public void aggiornaCampo(String campo, int riga, String valore, File file) throws IOException {
        File temp = new File("temp.csv");
        BufferedReader br = new BufferedReader(new FileReader(file));
        PrintWriter pw = new PrintWriter(new FileWriter(temp));

        int indice = trovaIndiceCampo(campo, file);
        String line;
        int cont = 1;

        while ((line = br.readLine()) != null) {
            if (cont == riga) {
                String[] split = line.split(",");
                split[indice] = valore;
                pw.println(String.join(",", split));
            } else {
                pw.println(line);
            }
            cont++;
        }

        br.close();
        pw.close();
        Files.move(temp.toPath(), file.toPath(), StandardCopyOption.REPLACE_EXISTING);
    }

    public void disattivaRecord(int riga, File file) throws IOException {
        aggiornaCampo("Record Attivo", riga, "false", file);
    }

    public void riattivaRecord(int riga, File file) throws IOException {
        aggiornaCampo("Record Attivo", riga, "true", file);
    }


    private int trovaIndiceCampo(String campo, File file) throws IOException {
        String[] campi = leggiCampi(file);
        for (int i = 0; i < campi.length; i++) {
            if (campi[i].trim().equalsIgnoreCase(campo.trim()))
                return i;
        }
        throw new CampoNotFoundException();
    }
}

