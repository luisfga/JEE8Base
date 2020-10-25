# JEE8Base

Um projeto dividido em módulos que podem servir de base para as camadas de um projeto JEE8. Contém lógica implementada para os casos de uso Register, Login e Password Recovery(com envio de email e reset de senha).

A camada Domain lida com entidades e as regras de negócio.

A camada Presentation está dividida nas opções mutuamente exclusivas para a apresentação: JSF, Struts ou Angular(#TODO).

Este projeto usa o Apache Shiro para segurança e ele está configurado na camada apresentação.

Os testes unitários utilizam JUnit, Mockito e o ApplicationComposer do OpenEJB (embedded - só pra testes mesmo)

Foi desenvolvido com TomEE 8.0.4 em um JDK 1.8 e também testado com sucesso no WildFly20, apenas precisa de algumas alterações específicas de configuração como jndi e escopo de algumas dependências, como openjpa e hsqldb, que são padrão no TomEE, mas não no WildFly. Além disso para o wildfly é preciso configurar o DataSource no servidor.
