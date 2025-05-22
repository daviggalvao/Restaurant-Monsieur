package database;

public class FirebaseFuncionario{
    private final DatabaseReference funcionarioRef;

    public FirebaseCliente(){
        this.funcionarioRef = FirebaseManager.getDatabase().getReference("funcionario");
    }
    
    public void criarFuncionario(Funcionario funcionario,DatabaseReference.CompletionListener listener){
        String id = funcionarioRef.push().getKey();
        funcionario.setId(id);
        funcionarioRef.child(id).setValue(funcionario,listener);
    }

    public void deletarFuncionario(Funcionario funcionario, DatabaseReference.CompletionListener listener){
        funcionarioRef.child(funcionario.getId()).removeValue(listener);
    }

    public void lerCliente(String funcionarioID, ValueEventListener listener){
        funcionarioRef.child(funcionarioID).addListenerForSingleValueEvent(listener);
    }

    
}
