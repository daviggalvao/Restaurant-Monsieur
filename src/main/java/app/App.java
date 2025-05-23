package app;
import classes.*;
import database.*;
public class App{
    public static void main( String[] args )
    {
        String Roberto = "Roberto";
        String pix = "pix";
        Pagamento hello = new Pagamento(10.f,Roberto,pix,10);
        System.out.println("hello.getNome(): " + hello.getPreco());
    }
}
