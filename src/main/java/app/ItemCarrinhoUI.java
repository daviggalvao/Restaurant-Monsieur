package app; // Ou com.seuprojeto.ui, etc.

import classes.Prato; // Importa sua classe Prato
import javafx.beans.property.IntegerProperty;
import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleObjectProperty;

public class ItemCarrinhoUI {
    private final ObjectProperty<Prato> prato = new SimpleObjectProperty<>();
    // Quantidade DESTE item NO CARRINHO da UI
    private final IntegerProperty quantidadeNoCarrinho = new SimpleIntegerProperty();

    public ItemCarrinhoUI(Prato prato, int quantidadeNoCarrinho) {
        setPrato(prato);
        setQuantidadeNoCarrinho(quantidadeNoCarrinho);
    }

    public Prato getPrato() { return prato.get(); }
    public void setPrato(Prato value) { prato.set(value); }
    public ObjectProperty<Prato> pratoProperty() { return prato; }

    public int getQuantidadeNoCarrinho() { return quantidadeNoCarrinho.get(); }
    public void setQuantidadeNoCarrinho(int value) {
        if (value >= 0) {
            quantidadeNoCarrinho.set(value);
        }
    }
    public IntegerProperty quantidadeNoCarrinhoProperty() { return quantidadeNoCarrinho; }

    public double getSubtotal() {
        return getPrato() != null ? getPrato().getPreco() * getQuantidadeNoCarrinho() : 0;
    }

    @Override
    public String toString() {
        if (getPrato() == null) return "";
        return String.format("%s (Qtd: %d) - R$ %.2f",
                getPrato().getNome(), getQuantidadeNoCarrinho(), getSubtotal());
    }
}