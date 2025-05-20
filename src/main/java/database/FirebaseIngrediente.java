import classes.Ingrediente;
import classes.Prato;
import com.google.firebase.database.*;
package database;

public class FirebaseIngrediente{
    private final DatabaseReference ingredienteRef;

    public FirebaseIngrediente(){
        this.ingredienteRef = FirebaseManager.getDatabase().getReference("ingredientes");
    }
    
    public void criarIngrediente(Ingrediente ingrediente,DatabaseReference.CompletionListener listener){
        String id = ingredienteRef.push().getKey();
        ingrediente.setId(id);
        ingredienteRef.child(id).setValue(ingrediente,listener);
    }

    public void deletarIngrediente(Ingrediente ingrediente, DatabaseReference.CompletionListener listener){
        ingredienteRef.child(ingrediente.getId()).removeValue(listener);
    }

    public void lerIngrediente(Ingrediente ingrediente, ValueEventListener listener){
        ingredienteRef.child(ingrediente.getId()).addListenerForSingleValueEvent(listener);
    }

    public void lerTodosIngredientes(ValueEventListener listener){
        ingredienteRef.addValueEventListener(listener);
    }

    public void atualizarIngrediente(String id,Ingrediente ingrediente,DatabaseReference.CompletionListener listener){
        ingredienteRef.child(id).setValue(ingrediente, listener);
    }
}
