package frota;

import java.util.ArrayList;

/**
 * Representa um veiculo da frota e guarda o proprio historico de abastecimentos.
 */
public class Veiculo {
    private final String placa;
    private final String modelo;
    private final double capacidadeTanque;
    private double quilometragemAtual;
    private final ArrayList<Abastecimento> historico = new ArrayList<>();

    public Veiculo(String placa, String modelo, double capacidadeTanque, double quilometragemAtual) {
        this.placa = placa;
        this.modelo = modelo;
        this.capacidadeTanque = capacidadeTanque;
        this.quilometragemAtual = quilometragemAtual;
    }

    public String getPlaca() { return placa; }
    public String getModelo() { return modelo; }
    public double getCapacidadeTanque() { return capacidadeTanque; }
    public double getQuilometragemAtual() { return quilometragemAtual; }
    public ArrayList<Abastecimento> getHistorico() { return new ArrayList<>(historico); }

    /**
     * Registra um abastecimento validando as regras de negocio.
     * @throws IllegalArgumentException com mensagem amigavel se alguma regra for violada.
     */
    public Abastecimento abastecer(double litros, double valorTotal, double quilometragem) {
        if (litros <= 0 || valorTotal <= 0) {
            throw new IllegalArgumentException("Litros e valor pago devem ser maiores que zero.");
        }
        if (litros > capacidadeTanque) {
            throw new IllegalArgumentException(String.format(
                "Litros (%.2f) excedem a capacidade do tanque (%.2f L).", litros, capacidadeTanque));
        }
        if (quilometragem <= quilometragemAtual) {
            throw new IllegalArgumentException(String.format(
                "A quilometragem deve ser maior que a atual (%.1f km).", quilometragemAtual));
        }
        Abastecimento a = new Abastecimento(this, litros, valorTotal, quilometragem);
        historico.add(a);
        quilometragemAtual = quilometragem;
        return a;
    }

    /**
     * Media de consumo (km/l) pelo metodo do tanque cheio:
     * distancia entre o primeiro e o ultimo abastecimento dividida pelos litros
     * abastecidos DEPOIS do primeiro (o primeiro apenas marca o ponto de partida).
     * Retorna -1 se houver menos de 2 abastecimentos.
     */
    public double calcularMediaConsumo() {
        if (historico.size() < 2) {
            return -1;
        }
        double distancia = historico.get(historico.size() - 1).getQuilometragem()
                         - historico.get(0).getQuilometragem();
        double litros = 0;
        for (int i = 1; i < historico.size(); i++) {
            litros += historico.get(i).getLitros();
        }
        return distancia / litros;
    }
}