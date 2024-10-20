package server.repository;

import java.io.FileReader;
import java.io.FileWriter;

public class ServerRepository implements ServerRepositoryView {


    @Override
    public void saveLog(String filename, String text) {
        try (FileWriter writer = new FileWriter(filename, true)){
            writer.write(text);
        } catch (Exception e){
            e.printStackTrace();
        }
    }

    @Override
    public String readLog(String filename) {
        StringBuilder stringBuilder = new StringBuilder();
        try (FileReader reader = new FileReader(filename);){
            int c;
            while ((c = reader.read()) != -1){
                stringBuilder.append((char) c);
            }
            stringBuilder.delete(stringBuilder.length() - 1, stringBuilder.length());
            stringBuilder.append("\n");
            return stringBuilder.toString();
        } catch (Exception e){
            e.printStackTrace();
            return null;
        }
    }
}
