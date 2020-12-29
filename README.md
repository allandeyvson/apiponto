#### Api para registro/consulta de ponto eletrônico

- A api possui autenticação via httpbasic, estando suas credenciais de acesso configuradas no application.properties.

- Possui documentação utilizando Swagger, a qual pode ser acessada via /swagger-ui/

- Abaixo temos um breve detalhamento dos endpoints  que compõem o serviço.


------------

###### Gestão de usuários

`GET - /api/usuario` <br>
Lista de todos os usuários cadastrados.

`GET - /api/usuario/{id}` <br>
Detalha um usuário com o identificador passado como parâmetro.

`POST - /api/usuario` <br>
Cria um novo usuário de acordo com os dados informados no corpo da requisição, os quais estão exemplificados abaixo.

```json
 {"nome": "Allan", "pis": "987654321"}
```

------------

###### Registro de ponto

`POST - /api/ponto` <br>
Registra de ponto de um determinado usuário (Entrada ou Saída). Onde o identificador do usuário é obtido atráves do header da requisição (idUsuario).


`GET - /api/ponto` <br>
Lista as batidas de ponto de um determinado usuário, agrupadas por dia, bem como a quantidade de horas trabalhadas naquele dia. O identificador do usuário é obtido atráves do Header da requisição (idUsuario). Abaixo temos um exemplo de retorno da api

```json
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