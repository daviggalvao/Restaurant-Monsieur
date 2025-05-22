package database;

import com.google.auth.oauth2.GoogleCredentials;
import com.google.firebase.FirebaseApp;
import com.google.firebase.FirebaseOptions;
import com.google.firebase.database.*;
import java.io.FileInputStream;
import java.io.IOException;

public class FirebaseManager {
    private static FirebaseDatabase database;

    public static void inicializarFirebase() {
        try {

            File arquivoCredenciais = new File("serviceAccountKey.json");
            FileInputStream serviceAccount = new FileInputStream(arquivoCredenciais);

            FirebaseOptions options = new FirebaseOptions.Builder()
            .setCredentials(GoogleCredentials.fromStream(serviceAccount))
            .setDatabaseUrl("https://restaurante-monsieur-33b14-default-rtdb.firebaseio.com")
            .build();

            FirebaseApp.initializeApp(options);
                
            database = FirebaseDatabase.getInstance();
            System.out.println("Conexão com Firebase estabelecida com sucesso!");
        } catch (IOException e) {
            System.err.println("Erro ao inicializar Firebase: " + e.getMessage());
            e.printStackTrace();
        }
    }

    public static FirebaseDatabase getDatabase() {
        if (database == null) {
            inicializarFirebase();
        }
        return database;
    }
}

