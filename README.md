Api para registro/consulta de ponto eletrônico

A api possui autenticação via httpbasic, estando suas credenciais de acesso configuradas no application.properties.

Abaixo temos um breve detalhamento dos endpoints  que compõem o serviço.

Gestão de usuários

GET - /api/usuario
Lista de todos os usuários cadastrados.

GET - /api/usuario/{id}
Detalha um usuário com o identificador passado como parâmetro.

POST - /api/usuario
Cria um novo usuário de acordo com os dados informados no corpo da requisição.

```
 {
 
"nome": "Allan",
  
"pis": "987654321"
  
 }
```

Registro de ponto

GET - /api/ponto

Listagem agrupada por dia de todos registros de ponto de um usuario, bem como a quantidade de horas trabalhadas.
O identificador do usuário é obtido atráves do Header da requisição (idUsuario).

```
[
    {
        "data": "30/11/2020",
        "horasTrabalhadas": "00:02:34",
        "pontos": [
            {
                "horario": "17:43:29",
                "data": "30/11/2020",
                "tipo": "ENTRADA"
            },
            {
                "horario": "17:46:04",
                "data": "30/11/2020",
                "tipo": "SAIDA"
            }
        ]
    }
]
```

POST - /api/ponto

Registro de ponto de um determinado usuário (Entrada ou Saída).

O identificador do usuário é obtido atráves do Header da requisição (idUsuario)
