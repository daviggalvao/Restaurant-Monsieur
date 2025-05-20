import com.google.firebase.database.*;

import classes.Reserva;

import java.util.concurrent.CompletableFuture;
import java.util.ArrayList;
import java.util.List;
package database;

public class FirebaseReserva{
    private final DatabaseReference reservasRef;

    public FirebaseReserva(){
        this.reservasRef = FirebaseManager.getDatabase().getReference("reservas");
    }
    
    public void criar(Reserva reserva){

    }
}
