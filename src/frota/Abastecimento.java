package frota;

/**
 * Representa um abastecimento associado a um veiculo especifico.
 */
public class Abastecimento {
    private final Veiculo veiculo;
    private final double litros;
    private final double valorTotal;
    private final double quilometragem;

    public Abastecimento(Veiculo veiculo, double litros, double valorTotal, double quilometragem) {
        this.veiculo = veiculo;
        this.litros = litros;
        this.valorTotal = valorTotal;
        this.quilometragem = quilometragem;
    }

    public Veiculo getVeiculo() { return veiculo; }
    public double getLitros() { return litros; }
    public double getValorTotal() { return valorTotal; }
    public double getQuilometragem() { return quilometragem; }

    public double getPrecoPorLitro() {
        return valorTotal / litros;
    }
}