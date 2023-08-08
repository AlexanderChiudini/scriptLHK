package scriptLHK;

import java.io.*;
import java.util.regex.*;

public class TerminalExecutor {
    public static void main(String[] args) {
    	String directory = new File("").getAbsolutePath(); 
    	
        File folder = new File("./tsp");
        File[] listOfFiles = folder.listFiles();

        if (listOfFiles == null) {
            System.out.println("Nenhum arquivo encontrado na pasta especificada.");
            return;
        }

        try (BufferedWriter writer = new BufferedWriter(new FileWriter("output_table.txt"))) {
            writer.write("Instância\tRepetição\tSeed\tResultado\n");

            for (File file : listOfFiles) {
                if (file.isFile()) {
                    for (int i = 0; i < 10; i++) {
                    	File filePY = new File(directory + "/lkh/lkhcli.py").getAbsoluteFile();
                    	filePY.setExecutable(true);

                        executeCommand("python3 " + directory + "/lkh/lkhcli.py --instance " + file.getAbsolutePath() + " >> _out.txt");
                        String costMinValue = extractCostMin("_out.txt");
                        writer.write(file.getName() + "\t" + i + "\t" + "" + "\t" + costMinValue + "\n");
                    }
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static void executeCommand(String command) {
        try {
            Process process = Runtime.getRuntime().exec(command);
            process.waitFor();
            
            BufferedReader errorReader = new BufferedReader(new InputStreamReader(process.getErrorStream()));
            String line;
            while ((line = errorReader.readLine()) != null) {
                System.out.println(line);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static String extractCostMin(String filePath) {
        String costMinValue = "";
        Pattern pattern = Pattern.compile("Cost.min\\s*=\\s*(\\d+)");

        try (BufferedReader reader = new BufferedReader(new FileReader(filePath))) {
            String line;
            while ((line = reader.readLine()) != null) {
                Matcher matcher = pattern.matcher(line);
                if (matcher.find()) {
                    costMinValue = matcher.group(1);
                    break;
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

        return costMinValue;
    }
}
