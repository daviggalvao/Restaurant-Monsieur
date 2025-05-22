package database;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.ValueEventListener;
import classes.Ingrediente;

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

    public void lerIngrediente(String ingredienteID, ValueEventListener listener){
        ingredienteRef.child(ingredienteID).addListenerForSingleValueEvent(listener);
    }

    public void lerTodosIngredientes(ValueEventListener listener){
        ingredienteRef.addValueEventListener(listener);
    }

    public void atualizarIngrediente(String id,Ingrediente ingrediente,DatabaseReference.CompletionListener listener){
        ingredienteRef.child(id).setValue(ingrediente, listener);
    }
}
