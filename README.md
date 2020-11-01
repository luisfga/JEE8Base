# JEE8Base

Este projeto está dividido entre o backend (domain) e frontend(JSF ou Struts). Há ainda a opção do Angular para o frontend, neste caso é preciso utilizar o módulo rest, que disponibiliza a api necessária para frontends client-side. Utilizando o **TomEE 8.0.4**, com a configuração default, o único ajuste necessário para que tudo funcione é a sessão de email (javax.mail.Session). Basta baixar o servidor e fazer o deploy de um dos módulos **frontend-x**.

### Domain
O módulo domain contém:
- Entidades e configurações da JPA;
- Casos de uso (Register, ConfirmRegistration, Login, Password Recovery e Reset);
- Exceções;
- JdbcRealm para o Apache Shiro.
- Testes unitários.

O módulo domain usa o OpenJPA como implementação da persistência.
    
O módulo domain precisa que dois recursos sejam disponibilizados no servidor:
- applicationDS, datasource do bando de dados;
- applicationMailSession, sessão de email utilizada nos casos de uso ConfirmRegistration e PasswordRecovery.

### Frontend
O módulo (####frontend-jsf) contém páginas xhtml e seus Beans e testes unitários.
O módulo frontend-struts contém páginas jsp e actions e testes unitários.
Ambos têm resource bundle para i18n e configuração do Shiro (shiro.ini).

### Angular + Rest
O módulo frontend-rest contém a api necessária para frontends client-side. O módulo frontend-angular contém um projeto angular.

### Os frontends estão dispostos de maneira que são mutuamente excludentes
Apenas um deles pode estar no servidor, pois todos importam o .jar do módulo domain, que possui ejbs que não podem se repetir.

Os módulos WAR(jsf, struts e rest) possuem um arquivo (WEB-INF/resources.xml) com configuração de datasource para o TomEE. Esse arquivo pode ser excluído caso queira colocar o *resource* direto no servidor. Nesse arquivo está também contido um template para o *resource* da sessão de email.

Os testes unitários utilizam JUnit, Mockito e o ApplicationComposer do OpenEJB (embedded).

O módulo frontend-angular deve ser executado no nodeja. Outra opção é compilar, montar e colocar o diretório gerado (**dist**) num pacote junto com a api rest.

Foi desenvolvido com **TomEE 8.0.4** em um **JDK 1.8** (1.8.0_252). Também foi testado no **WildFly20**. Para o wildfly são necessárias pequenas alterações, como o escopo de algumas dependências (openjpa e hsqldb são padrão no TomEE e estão com o escopo **provided**). O modulo angular foi feito com nodejs v12.


