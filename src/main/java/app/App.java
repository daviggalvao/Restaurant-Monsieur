package app;
import classes.*;
import database.*;
public class App{
    public static void main( String[] args )
    {
        String Roberto = "Roberto";
        String pix = "pix";
        float ola = 10.0f;
        Pagamento hello = new Pagamento(ola,Roberto,pix,10);
        System.out.println("hello.getNome(): " + hello.getTipo());
    }
}
