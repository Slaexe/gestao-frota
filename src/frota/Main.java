package frota;

import java.util.ArrayList;
import java.util.Scanner;

/**
 * Controla o fluxo do programa e a exibicao do menu no console.
 */
public class Main {
    private static final Scanner sc = new Scanner(System.in);
    private static final ArrayList<Veiculo> frota = new ArrayList<>();
    private static final ArrayList<Abastecimento> abastecimentos = new ArrayList<>();

    public static void main(String[] args) {
        int opcao;
        do {
            exibirMenu();
            opcao = lerInt("Escolha uma opcao: ");
            switch (opcao) {
                case 1: cadastrarVeiculo(); break;
                case 2: listarVeiculos(); break;
                case 3: registrarAbastecimento(); break;
                case 4: mostrarMedia(); break;
                case 5: mostrarHistorico(); break;
                case 6: relatorioGeral(); break;
                case 0: System.out.println("Encerrando... Boa viagem!"); break;
                default: System.out.println("Opcao invalida. Tente novamente.");
            }
        } while (opcao != 0);
        sc.close();
    }

    private static void exibirMenu() {
        System.out.println("\n===== GESTAO DE FROTA E ABASTECIMENTOS =====");
        System.out.println("1 - Cadastrar veiculo");
        System.out.println("2 - Listar veiculos");
        System.out.println("3 - Registrar abastecimento");
        System.out.println("4 - Media de consumo de um veiculo");
        System.out.println("5 - Historico de abastecimentos de um veiculo");
        System.out.println("6 - Relatorio geral da frota");
        System.out.println("0 - Sair");
    }

    // ---------- Funcionalidades ----------

    private static void cadastrarVeiculo() {
        System.out.println("\n--- Cadastro de veiculo ---");
        String placa = lerTexto("Placa: ").toUpperCase();
        if (placa.isEmpty()) { System.out.println("Placa nao pode ser vazia."); return; }
        if (buscarVeiculo(placa) != null) { System.out.println("Ja existe veiculo com essa placa."); return; }
        String modelo = lerTexto("Modelo: ");
        if (modelo.isEmpty()) { System.out.println("Modelo nao pode ser vazio."); return; }
        double tanque = lerDouble("Capacidade do tanque (L): ");
        double km = lerDouble("Quilometragem atual: ");
        if (tanque <= 0 || km < 0) {
            System.out.println("Tanque deve ser maior que zero e quilometragem nao pode ser negativa.");
            return;
        }
        frota.add(new Veiculo(placa, modelo, tanque, km));
        System.out.println("Veiculo cadastrado com sucesso!");
    }

    private static void listarVeiculos() {
        if (frota.isEmpty()) { System.out.println("\nNenhum veiculo cadastrado."); return; }
        System.out.println("\n" + String.format("%-10s %-20s %12s %14s", "PLACA", "MODELO", "TANQUE (L)", "KM ATUAL"));
        System.out.println("-".repeat(60));
        for (Veiculo v : frota) {
            System.out.println(String.format("%-10s %-20s %12.1f %14.1f",
                v.getPlaca(), v.getModelo(), v.getCapacidadeTanque(), v.getQuilometragemAtual()));
        }
    }

    private static void registrarAbastecimento() {
        System.out.println("\n--- Registro de abastecimento ---");
        Veiculo v = buscarVeiculo(lerTexto("Placa do veiculo: "));
        if (v == null) { System.out.println("Veiculo nao encontrado."); return; }
        double litros = lerDouble("Litros abastecidos: ");
        double valor = lerDouble("Valor total pago (R$): ");
        double km = lerDouble("Quilometragem no abastecimento: ");
        try {
            Abastecimento a = v.abastecer(litros, valor, km);
            abastecimentos.add(a);
            System.out.println("Abastecimento registrado com sucesso!");
        } catch (IllegalArgumentException e) {
            System.out.println("Nao foi possivel registrar: " + e.getMessage());
        }
    }

    private static void mostrarMedia() {
        Veiculo v = buscarVeiculo(lerTexto("\nPlaca do veiculo: "));
        if (v == null) { System.out.println("Veiculo nao encontrado."); return; }
        double media = v.calcularMediaConsumo();
        if (media < 0) {
            System.out.println("Sao necessarios ao menos 2 abastecimentos para calcular a media.");
        } else {
            System.out.println(String.format("Media de consumo de %s (%s): %.2f km/l",
                v.getPlaca(), v.getModelo(), media));
        }
    }

    private static void mostrarHistorico() {
        Veiculo v = buscarVeiculo(lerTexto("\nPlaca do veiculo: "));
        if (v == null) { System.out.println("Veiculo nao encontrado."); return; }
        ArrayList<Abastecimento> lista = v.getHistorico();
        if (lista.isEmpty()) { System.out.println("Nenhum abastecimento registrado."); return; }
        System.out.println(String.format("\n%-4s %10s %12s %12s %14s", "#", "LITROS", "VALOR (R$)", "R$/LITRO", "KM"));
        System.out.println("-".repeat(56));
        int i = 1;
        for (Abastecimento a : lista) {
            System.out.println(String.format("%-4d %10.2f %12.2f %12.2f %14.1f",
                i++, a.getLitros(), a.getValorTotal(), a.getPrecoPorLitro(), a.getQuilometragem()));
        }
    }

    private static void relatorioGeral() {
        if (frota.isEmpty()) { System.out.println("\nNenhum veiculo cadastrado."); return; }
        System.out.println("\n" + String.format("%-10s %-18s %8s %10s %12s", "PLACA", "MODELO", "ABAST.", "GASTO (R$)", "MEDIA km/l"));
        System.out.println("-".repeat(62));
        double gastoFrota = 0;
        for (Veiculo v : frota) {
            double gasto = 0;
            for (Abastecimento a : abastecimentos) {
                if (a.getVeiculo() == v) gasto += a.getValorTotal();
            }
            gastoFrota += gasto;
            double media = v.calcularMediaConsumo();
            String mediaTxt = media < 0 ? "n/d" : String.format("%.2f", media);
            System.out.println(String.format("%-10s %-18s %8d %10.2f %12s",
                v.getPlaca(), v.getModelo(), v.getHistorico().size(), gasto, mediaTxt));
        }
        System.out.println("-".repeat(62));
        System.out.println(String.format("Total de abastecimentos: %d | Gasto total da frota: R$ %.2f",
            abastecimentos.size(), gastoFrota));
    }

    // ---------- Auxiliares ----------

    private static Veiculo buscarVeiculo(String placa) {
        for (Veiculo v : frota) {
            if (v.getPlaca().equalsIgnoreCase(placa.trim())) return v;
        }
        return null;
    }

    private static String lerTexto(String msg) {
        System.out.print(msg);
        return sc.nextLine().trim();
    }

    private static int lerInt(String msg) {
        try {
            return Integer.parseInt(lerTexto(msg));
        } catch (NumberFormatException e) {
            return -1; // cai em "opcao invalida"
        }
    }

    /** Le um numero; aceita virgula ou ponto. Retorna -1 se a entrada for invalida. */
    private static double lerDouble(String msg) {
        try {
            return Double.parseDouble(lerTexto(msg).replace(',', '.'));
        } catch (NumberFormatException e) {
            System.out.println("Valor numerico invalido.");
            return -1;
        }
    }
}