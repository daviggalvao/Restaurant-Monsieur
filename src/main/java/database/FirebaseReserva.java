package database;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.ValueEventListener;
import classes.Reserva;

public class FirebaseReserva{
    private final DatabaseReference reservaRef;

    public FirebaseReserva(){
        this.reservaRef = FirebaseManager.getDatabase().getReference("reservas");
    }
    
    public void criarReserva(Reserva reserva,DatabaseReference.CompletionListener listener){
        String id = reservaRef.push().getKey();
        reserva.setId(id);
        reservaRef.child(id).setValue(reserva,listener);
    }

    public void deletarReserva(Reserva reserva, DatabaseReference.CompletionListener listener){
        reservaRef.child(reserva.getId()).removeValue(listener);
    }

    public void lerReserva(String reservaID, ValueEventListener listener){
        reservaRef.child(reservaID).addListenerForSingleValueEvent(listener);
    }

    public void lerReservaPorCliente(String cliente, ValueEventListener listener){
        reservaRef.orderByChild("cliente/nome").equalTo(cliente)
        .addListenerForSingleValueEvent(listener);
    }
}
