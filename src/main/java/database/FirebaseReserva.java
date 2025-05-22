package database;

import classes.Reserva;
import com.google.firebase.database.*;

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

    public void lerReserva(Reserva reserva, ValueEventListener listener){
        reservaRef.child(reserva.getId()).addListenerForSingleValueEvent(listener);
    }

    public void lerReservaPorCliente(String cliente, ValueEventListener listener){
        reservaRef.orderByChild("cliente/nome").equalTo(cliente)
        .addListenerForSingleValueEvent(listener);
    }
}
