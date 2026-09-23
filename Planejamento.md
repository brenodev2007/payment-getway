## Semana 1 — Fundação e modelagem

[X] Dia 1-2: Setup do projeto (Spring Boot, banco, estrutura de pacotes). Modelagem das entidades: Transaction, Merchant, Account (saldo/limite simulado). Definir a máquina de estados da transação no papel antes de codar.

[] Dia 3-4: Endpoint de criação de transação com as regras de aprovação/recusa (saldo insuficiente, limite excedido, etc). Implementar a máquina de estados como enum + validação de transições permitidas (não deixar pular de PENDING direto pra REFUNDED, por exemplo).

[] Dia 5: Endpoint de consulta de status e endpoint de reembolso (parcial e total). Testes unitários das regras de negócio.

[] Dia 6-7 (fim de semana): Implementar idempotência — header Idempotency-Key, tabela pra guardar a chave + resultado da primeira execução, retornar o mesmo resultado se a chave repetir. Escrever teste simulando duas requisições concorrentes com a mesma chave (usar CompletableFuture ou threads pra provar que não duplica).

## Semana 2 — Assincronia e resiliência

[] Dia 8-9: Sistema de webhook: quando a transação muda de estado, disparar notificação HTTP pro "lojista" (pode ser um endpoint mock seu mesmo). Implementar de forma assíncrona (@Async ou fila simples).

[] Dia 10-11: Adicionar retry com backoff exponencial nas chamadas de webhook (o lojista pode estar fora do ar). Usar Resilience4j pra isso — já deixa o projeto com uma lib de mercado no currículo.

[] Dia 12: Logs estruturados e auditoria — toda mudança de estado da transação vira um registro imutável (event sourcing simplificado, nem precisa ser o padrão completo, só um histórico append-only).

[] Dia 13-14 (fim de semana): Testes de integração ponta a ponta (Testcontainers com banco real), documentação da API (OpenAPI/Swagger), README explicando as decisões de arquitetura — isso é o que recrutador sênior realmente lê.

## Semana 3 — Extensão (opcional, Nível 2)

[] Dia 15-17: Migrar a notificação de webhook pra outbox pattern com RabbitMQ ou Kafka: gravar evento na mesma transação do banco, worker separado publica.

[] Dia 18-19: Circuit breaker simulando timeout de "banco emissor" externo, com fallback controlado.

[] Dia 20-21: Dashboard simples (ou métricas via Actuator + Prometheus) mostrando taxa de aprovação e transações presas em PROCESSING.
