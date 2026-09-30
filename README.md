# Gestão de Frota e Abastecimentos

Aplicação de console em Java (POO) para controlar uma frota de veículos e acompanhar quanto cada um consome.

## A história
Quem cuida de uma frota costuma anotar abastecimentos em papel e descobrir tarde demais que um veículo está gastando mais do que deveria. Este sistema transforma essas anotações em informação: quanto cada veículo faz por litro e quanto a frota gasta.

## Funcionalidades
- Cadastro e listagem de veículos (placa, modelo, tanque, km atual)
- Registro de abastecimentos (litros, valor, km) ligado a um veículo
- Média de consumo (km/l) por veículo
- Histórico de abastecimentos e relatório geral da frota
- Menu em loop até a opção "Sair"

## Regras de negócio
- Placa única; valores negativos ou inválidos são recusados
- Litros não podem exceder a capacidade do tanque
- A km do abastecimento deve ser maior que a km atual do veículo
- Média km/l = (km do último - km do primeiro abastecimento) / litros abastecidos depois do primeiro. Exige ao menos 2 abastecimentos.

## Estrutura
src/frota/
- Veiculo.java: dados do veículo, regras e média de consumo
- Abastecimento.java: transação ligada a um Veiculo
- Main.java: menu, leitura via Scanner e relatórios

Os dados ficam apenas em memória (ArrayList).

## Como executar
No Eclipse: abra Main.java, clique com o botão direito > Run As > Java Application, e digite as opções no console.

## Exemplo
Cadastre ABC1D23 (tanque 50, km 10000). Abasteça 30 L a km 10100 e 40 L a km 10500.
Média = (10500 - 10100) / 40 = 10,00 km/l.