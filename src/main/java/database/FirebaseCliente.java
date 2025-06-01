package database;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.ValueEventListener;
import classes.Cliente;

public class FirebaseCliente{
    private final DatabaseReference clienteRef;

    public FirebaseCliente(){
        this.clienteRef = FirebaseManager.getDatabase().getReference("clientes");
    }
    
    public void criarCliente(Cliente cliente,DatabaseReference.CompletionListener listener){
        String id = clienteRef.push().getKey();
        cliente.setId(id);
        clienteRef.child(id).setValue(cliente,listener);
    }

    public void deletarCliente(Cliente cliente, DatabaseReference.CompletionListener listener){
        clienteRef.child(cliente.getId()).removeValue(listener);
    }

    public void lerCliente(String clienteID, ValueEventListener listener){
        clienteRef.child(clienteID).addListenerForSingleValueEvent(listener);
    }
    public void lerClienteNome(String clienteNome, ValueEventListener listener){
        clienteRef.orderByChild("nome").equalTo(clienteNome)
                .addListenerForSingleValueEvent(listener);
    }
}
