## Project
[![Contributors][contributors-shield]][contributors-url]
[![Forks][forks-shield]][forks-url]
[![Stargazers][stars-shield]][stars-url]
[![Issues][issues-shield]][issues-url]
[![MIT License][license-shield]][license-url]
[![LinkedIn][linkedin-shield]][linkedin-url]

## Social
![YouTube](https://img.shields.io/youtube/channel/views/UCPOdfYoz_hTNrngfouB24jQ)
![Tweeter](https://img.shields.io/twitter/follow/luizcostatech?style=social)

## Build status
![Build status](https://github.com/luizgustavocosta/progamacao-reativa-em-java-com-spring/workflows/Java%20CI%20with%20Maven/badge.svg)

[![Quality Gate Status](https://sonarcloud.io/api/project_badges/measure?project=luizgustavocosta_progamacao-reativa-em-java-com-spring&metric=alert_status)](https://sonarcloud.io/dashboard?id=luizgustavocosta_progamacao-reativa-em-java-com-spring)

# progamacao-reativa-em-java-com-spring
Programação reativa com Spring H2 MongoDB

[Apresentação](https://github.com/luizgustavocosta/progamacao-reativa-em-java-com-spring/blob/main/static/Programacao_reativa_em_Java_LuizCosta.pdf) 

## Diagrama de instalação
<kbd>
    <img src="https://raw.githubusercontent.com/luizgustavocosta/progamacao-reativa-em-java-com-spring/main/static/App.png">
</kbd>

### Swagger
| Serviço      | Endpoint |
| ----------- | ----------- |
| Review      | http://localhost:8080/swagger-ui.html|
| Customer   | http://localhost:8081/swagger-ui.html|
| Movie   | http://localhost:8083/swagger-ui.html|

### Review
| Serviço      | Endpoint |Observação | 
| ----------- | ----------- |-----------|
| /api/v1/reviews      | http://localhost:8080/swagger-ui.html|
| /api/v1/reviews/stream      | http://localhost:8080/api/v1/reviews/stream|Irá falhar, pelo uso del @Tailable|
| /api/v1/reviews/search?name=Name      | curl -X 'GET' 'http://localhost:8080/api/v1/reviews/search?name=Barcelona'-H 'accept: text/event-stream'|

### Customer
|Método| Endpoint      | Exemplo |Observação |
|-----------| ----------- | ----------- |-----------|
|Post| /customers      |curl -X 'POST' 'http://127.0.0.1:8081/customers' -H 'accept: \*/*' -H 'Content-Type: application/json' -d '{"name": "string","middleName": "string","lastName": "string","becameCustomer": "2021-09-23"}' |
|Put| /customers      |curl -X 'PUT' 'http://127.0.0.1:8081/customers' -H 'accept: \*/*' -H 'Content-Type: application/json' -d '{"id": 0,"name": "string","middleName": "string","lastName": "string","becameCustomer": "2021-09-23"}'||
|Get| /customers      | curl -X 'GET' 'http://127.0.0.1:8081/customers' -H 'accept: \*/*'||
|Get| /customers/stream      | curl -X 'GET' 'http://127.0.0.1:8081/customers/stream' -H 'accept: \*/*'||
|Get| /customers/{id}      | curl -X 'GET' 'http://127.0.0.1:8081/customers/3' -H 'accept: \*/*'||
|Delete| /customers/{id} | curl -X 'DELETE' 'http://127.0.0.1:8081/customers/1' -H 'accept: \*/*'||

## Movie
|Método| Endpoint      | Exemplo |Observação |
|-----------| ----------- | ----------- |-----------|
|Get| /api/v1/movies      |curl -X 'GET' 'http://127.0.0.1:8083/api/v1/movies' -H 'accept: \*/*'||
|Get| /api/v1/movies/{id}  |curl -X 'GET' 'http://127.0.0.1:8083/api/v1/movies/3' -H 'accept: \*/*'||
|Get| /api/v1/movies/stream |curl -X 'GET' 'http://127.0.0.1:8083/api/v1/movies/stream' ||
|Get| /api/v1/movies/search  |curl -X 'GET' 'http://127.0.0.1:8083/api/v1/movies/search?name=John' -H 'accept: text/event-stream'||

### Save a document and watch the live stream

- Usando Google Chrome, faça uma request para o endpoint `/api/v1/movies/stream`
- Conecte com seu MongoDB e execute o comando abaixo.

```
db.movies.insertOne({"id":"61450f16538a1ad9f23c84AD","name":"Reactive John Wick?","director":"Morpheus","duration":2021})
```
- O dado acima será salvo, e mostrado imediatamente no seu browser.

### Referências
* [Markdown table generator](https://www.tablesgenerator.com/markdown_tables)
* [Shields](https://shields.io)

[contributors-shield]: https://img.shields.io/github/contributors/luizgustavocosta/progamacao-reativa-em-java-com-spring.svg?style=plastic
[contributors-url]: https://github.com/luizgustavocosta/progamacao-reativa-em-java-com-spring/graphs/contributors
[forks-shield]: https://img.shields.io/github/forks/luizgustavocosta/progamacao-reativa-em-java-com-spring.svg?style=plastic
[forks-url]: https://github.com/luizgustavocosta/progamacao-reativa-em-java-com-spring/network/members
[stars-shield]: https://img.shields.io/github/stars/luizgustavocosta/progamacao-reativa-em-java-com-spring.svg?style=plastic
[stars-url]: https://github.com/luizgustavocosta/progamacao-reativa-em-java-com-spring/stargazers
[issues-shield]: https://img.shields.io/github/issues/luizgustavocosta/progamacao-reativa-em-java-com-spring.svg?style=plastic
[issues-url]: https://github.com/luizgustavocosta/progamacao-reativa-em-java-com-spring/issues
[license-shield]: https://img.shields.io/github/license/luizgustavocosta/progamacao-reativa-em-java-com-spring.svg?style=plastic
[license-url]: https://github.com/luizgustavocosta/progamacao-reativa-em-java-com-spring/blob/master/LICENSE
[linkedin-shield]: https://img.shields.io/badge/-LinkedIn-black.svg?style=plastic&logo=linkedin&colorB=555
[linkedin-url]: https://www.linkedin.com/in/luiz-gustavo-oliveira-costa-8989776/