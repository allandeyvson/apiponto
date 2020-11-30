Api para registro/consulta de ponto eletrônico

Gestão de usuários

GET - /usuario
Lista de todos os usuários cadastrados.

GET - /usuario/{id}
Detalha um usuário com o identificador passado como parâmetro.

POST - /usuario
Cria um novo usuário de acordo com os dados informados no corpo da requisição.

```
 {
 
"nome": "Allan",
  
"pis": "987654321"
  
 }
```

Registro de ponto

GET - /ponto

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

POST - /ponto

Registro de ponto de um determinado usuário (Entrada ou Saída).

O identificador do usuário é obtido atráves do Header da requisição (idUsuario)
