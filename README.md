# JEE8Demo

Este projeto está dividido entre o backend (domain) e frontend(JSF, Struts ou Rest+Angular). Utilizando o <a href="https://tomee.apache.org/download-ng.html">**TomEE 8.0.4**</a>, com a configuração padrão, o único ajuste necessário para que tudo funcione é a sessão de email (javax.mail.Session).

### Domain
O módulo <a href="https://github.com/luisfga/JEE8Demo/tree/master/domain">domain</a> contém:
- Entidades e configurações JPA;
- Casos de uso (Registro, Confirmação de Registro, Login, Recuperação e Reset de Senha);
- Exceções;
- JdbcRealm para o Apache Shiro.
- Testes unitários.

O módulo domain usa o OpenJPA como implementação da persistência.
    
O módulo domain precisa que um recurso seja disponibilizado no servidor:
- applicationMailSession, sessão de email utilizada nos casos de uso ConfirmRegistration e PasswordRecovery.

### Frontend JSF
O módulo <a href="https://github.com/luisfga/JEE8Demo/tree/master/frontend-jsf">frontend-jsf</a> contém páginas xhtml, seus Beans, testes unitários, resource bundle para i18n e configuração do Shiro (shiro.ini).

### Frontend Struts
O módulo <a href="https://github.com/luisfga/JEE8Demo/tree/master/frontend-struts">frontend-struts</a> contém páginas jsp e actions, testes unitários, resource bundle para i18n e configuração do Shiro (shiro.ini). * *No branch '<a href="https://github.com/luisfga/JEE8Demo/tree/user-management">user-management</a>' esta versão possui também, no dashboard (tela após o login), frontend para administração de Roles e Permissões e suas associações, cheio de javascript, drag&drop e mais. =)*

### Angular + Rest
O módulo <a href="https://github.com/luisfga/JEE8Demo/tree/master/frontend-rest">frontend-rest</a> contém a api necessária para frontends client-side. O módulo <a href="https://github.com/luisfga/JEE8Demo/tree/master/frontend-angular">frontend-angular</a> contém um projeto angular. * *Por enquanto, há apenas a funcionalidade de Login, com exemplo de XmlHttpRequest e Json Web Token (JWT).*

#### Notas

*Os módulos WAR(jsf, struts e rest) possuem um arquivo (WEB-INF/resources.xml) com configuração de datasource para o TomEE. Esse arquivo pode ser excluído caso queira colocar o *resource* direto no servidor. Nesse arquivo há também um template para o *resource* da sessão de email.*

*Os testes unitários utilizam **JUnit**, **Mockito** e o **ApplicationComposer** do OpenEJB (embedded).*

*O módulo frontend-angular deve ser executado no nodejs. Outra opção é compilar, montar e colocar o diretório gerado (dist) num pacote junto com a api rest.*

*Foi desenvolvido/testado com **TomEE 8.0.4** em um **JDK 1.8** (1.8.0_252). Também foi testado no **WildFly20**. Para o wildfly são necessárias pequenas alterações, como o escopo de algumas dependências (openjpa e hsqldb são padrão no TomEE e estão com o escopo* provided *). O modulo angular foi feito com nodejs v12.*

#### Casos de uso

*Os casos de uso não utilizam a interface @Remote, mas apenas a @LocalBean, gerada automaticamente pelo OpenEJB. Caso seja necessário, basta fazer com que eles implementem interfaces de um jar separado e utilizar esse jar também em clientes remotos.*

##### Registrar
O usuário cadastra email, senha, nome e data de nascimento. A senha tem um campo para confirmação. O sistema grava os dados com o password codificado (hashing) e status NEW. O sistema utiliza o passwordService do Apache Shiro para codificar e verificar senhas. O sistema envia email para que o usuário confirme. O email enviado no link está codificado em Base64.
    
##### Confirmar registro
O usuário clica no link enviado por email e o sistema atualiza o status para OK.
    
##### Login
O usuário utiliza email e senha para logar. O sistema usa uma implementação própria do JdbcRealm do Apache Shiro para buscar os dados de autenticação no banco de dados. O passwordService verifica se o password confere com o que está salvo (codificado) no banco. 

##### Recuperação de password
O sistema pede data de nascimento e email antes de enviar um email com link para o funcionalidade "reset de senha". O sistema "abre uma janela" de 7 minutos para que a operação seja executada.
    
##### Reset de password
O usuário clica no link enviado para o email. O sistema valida se está dentro do limite de 7 minutos. Se sim, encaminha o usuário para a tela onde ele poderá cadastrar a nova senha. O campo email é mostrado 'read-only' e a senha tem um campo para confirmação.
