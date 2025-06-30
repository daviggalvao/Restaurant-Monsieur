package classes;

public enum PedidoStatus {
    RECEBIDO("Recebido"),
    EM_PREPARO("Em Preparo"),
    SAIU_PARA_ENTREGA("Saiu para Entrega"),
    CONCLUIDO("Concluído"),
    CANCELADO("Cancelado");

    private final String descricao;

    PedidoStatus(String descricao) {
        this.descricao = descricao;
    }

    @Override
    public String toString() {
        return descricao;
    }
}