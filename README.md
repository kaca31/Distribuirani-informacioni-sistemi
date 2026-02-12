Bookstore microservices - complete project
Run: docker-compose up --build


## Opis projekta

Ovaj projekat predstavlja **Bookstore mikroservisni sistem** razvijen korišćenjem **Spring Cloud** i kontejnerizovan sa **Docker-om**. Sistem sadrži sledeće mikroservise:

- **Catalog Service** – upravlja knjigama i njihovim podacima.
- **User Service** – upravlja korisnicima.
- **Order Service** – upravlja kreiranjem i čuvanjem porudžbina.
- **Inventory Service** – prati stanje zaliha knjiga.
- **Review Service** – upravlja recenzijama knjiga.
- **Gateway Service** – API Gateway koji preusmerava REST pozive na odgovarajuće servise.
- **Discovery Server (Eureka)** – registruje servise i omogućava dynamic discovery.

API Gateway (8080) – ulazna tačka za sve zahteve, prosleđuje ih odgovarajućim servisima.
Discovery Server / Eureka (8761) – registruje sve mikroservise i omogućava Gateway-u da ih pronađe.
Mikroservisi (8081-8085) – poslovna logika:
Catalog: knjige
User: korisnici
Order: porudžbine
Inventory: stanje zaliha
Review: recenzije
Baze podataka – svaki servis ima svoju PostgreSQL bazu, sve kontejnerizovano.
Komunikacija: Gateway šalje sinhrone HTTP zahteve ka mikroservisima;
                +----------------+
                |  API Gateway   |  (8080)
                +----------------+
                   |   |   |   |   |
   +---------------+   |   |   |   +----------------+
   |                   |   |   |                    |
+---------+         +---------+         +---------+
| Catalog |         | User    |         | Order   |
| Service |         | Service |         | Service |
| 8081    |         | 8082    |         | 8083    |
+---------+         +---------+         +---------+
    |                   |                   |
    |                   |                   |
+---------+         +---------+         +---------+
| Inventory|         | Review  |         | DBs     |
| Service |         | Service |         |         |
| 8084    |         | 8085    |         |         |
+---------+         +---------+         +---------+

      \__________________________________/
                   |
                   v
            +----------------+
            | Discovery      |
            | Server (Eureka)|
            | 8761           |
            +----------------+
