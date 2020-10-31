# JEE8Base

Este projeto está dividido entre o backend (domain) e frontend(JSF ou Struts). Há ainda a opção do angular para o frontend, neste caso é preciso utilizar o módulo rest, que disponibiliza a api necessária para frontends no lado do cliente.

O módulo domain contém:
    - entidades;
    - casos de uso Register, ConfirmRegistration, Login, Password Recovery e Reset;
    - exceções;
    - JdbcRealm para o Apache Shiro.
    - testes unitários.

O módulo domain usa o OpenJPA como implementação da persistência.
O módulo domain precisa que dois recursos sejam disponibilizados no servidor:
    - applicationDS, datasource do bando de dados;
    - applicationMailSession, sessão de email utilizada nos casos de uso ConfirmRegistration e PasswordRecovery.

O módulo frontend-jsf contém páginas xhtml e seus Beans e testes unitários.
O módulo frontend-struts contém páginas jsp e actions e testes unitários.
Ambos têm resource bundle para i18n e configuração do Shiro (shiro.ini).

O módulo frontend-rest contém a api necessária para frontends client-side.
O módulo frontend-angular contém um projeto angular de frontend client-side. Para utilizar esse frontend é preciso usar também o módulo rest.

Os testes unitários utilizam JUnit, Mockito e o ApplicationComposer do OpenEJB (embedded).

Foi desenvolvido com TomEE 8.0.4 em um JDK 1.8 (1.8.0_252)
Também foi testado no WildFly20. Para o wildfly são necessárias algumas configurações específicas, como o escopo de algumas dependências, como openjpa e hsqldb, que são padrão no TomEE, mas não no WildFly.
