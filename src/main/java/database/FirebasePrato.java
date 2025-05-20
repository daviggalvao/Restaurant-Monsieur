import classes.Ingrediente;
import classes.Prato;
import com.google.firebase.database.*;
package database;

public class FirebasePrato{
    private final DatabaseReference pratoRef;

    public FirebasePrato(){
        this.pratoRef = FirebaseManager.getDatabase().getReference("pratos");
    }
    
    public void criarPrato(Prato prato,DatabaseReference.CompletionListener listener){
        String id = pratoRef.push().getKey();
        prato.setId(id);
        pratoRef.child(id).setValue(prato,listener);
    }

    public void deletarPrato(Prato prato, DatabaseReference.CompletionListener listener){
        pratoRef.child(prato.getId()).removeValue(listener);
    }

    public void lerPrato(String idPrato, ValueEventListener listener){
        pratoRef.child(idPrato).addListenerForSingleValueEvent(listener);
    }

    public void lerTodosPratos(ValueEventListener listener){
        pratoRef.addValueEventListener(listener);
    }
    
    public void atualizarPrato(String id,Prato prato,DatabaseReference.CompletionListener listener){
        pratoRef.child(id).setValue(prato, listener);
    }
}
