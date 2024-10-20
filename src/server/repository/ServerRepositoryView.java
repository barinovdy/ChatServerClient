package server.repository;

public interface ServerRepositoryView {
    void saveLog(String filename, String text);
    String readLog(String filename);
}
