# JEE8Base

Um projeto POM multi-módulo que serve de base para as camadas de um projeto JEE8 com a lógica dos casos de uso Register, Login e Password Recovery.

A camada Domain lida com entidades e as regras de negócio.
A camada Presentation, demonstrando desacoplamento da camada Domain, está dividida em as opções mutuamente exclusivas para a apresentação: JSF, Struts and Angular(#TODO).
Apache Shiro está configurado na camada Presentation.

Este é apenas um primeiro commit. Há coisas a melhorar, mas está funcionando.
Foi desenvolvido com TomEE 8.0.4 em um JDK 1.8.
Foi testado com sucesso no WildFly20, apenas precisa de algumas alterações específicas de configuração como jndi e escopo de algumas dependências, como openjpa e hsqldb, que são padrão no TomEE, mas não no WildFly.
