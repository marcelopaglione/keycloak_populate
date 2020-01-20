import entity.Cliente;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class Run {

    public ExecutorService pool = Executors.newFixedThreadPool(5);
    public Boolean cleanAll = true;

    public static void main(String[] args) {
        new Run().createClients();
    }

    public void createClients() {

        Cliente.getClientNames().forEach(clientName -> {
            Cliente cliente = new Cliente( clientName, null, cleanAll);
            pool.execute(cliente);
        });

    }
}
