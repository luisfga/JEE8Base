# JEE8Demo

Este projeto está dividido entre o backend (domain) e frontend(JSF ou Struts). Há ainda a opção do Angular para o frontend, neste caso é preciso utilizar o módulo rest, que disponibiliza a api necessária para frontends client-side. Utilizando o <a href="https://tomee.apache.org/download-ng.html">**TomEE 8.0.4**</a>, com a configuração default, o único ajuste necessário para que tudo funcione é a sessão de email (javax.mail.Session). Basta baixar o servidor, fazer o deploy de um dos módulos **frontend** e configurar a sessão de email (há um template em WEB-INF/resources.xml).

### Domain
O módulo <a href="https://github.com/luisfga/JEE8Demo/tree/master/domain">domain</a> contém:
- Entidades e configurações da JPA;
- Casos de uso (Register, ConfirmRegistration, Login, Password Recovery e Reset);
- Exceções;
- JdbcRealm para o Apache Shiro.
- Testes unitários.

O módulo domain usa o OpenJPA como implementação da persistência.
    
O módulo domain precisa que dois recursos sejam disponibilizados no servidor:
- applicationDS, datasource do bando de dados;
- applicationMailSession, sessão de email utilizada nos casos de uso ConfirmRegistration e PasswordRecovery.

### Frontend JSF
O módulo <a href="https://github.com/luisfga/JEE8Demo/tree/master/frontend-jsf">frontend-jsf</a> contém páginas xhtml, seus Beans, testes unitários, resource bundle para i18n e configuração do Shiro (shiro.ini).

### Frontend Struts
O módulo <a href="https://github.com/luisfga/JEE8Demo/tree/master/frontend-struts">frontend-struts</a> contém páginas jsp e actions, testes unitários, resource bundle para i18n e configuração do Shiro (shiro.ini). * *No branch 'user-management' esta versão possui também, no dashboard (tela após o login), frontend para administração de Roles e Permissões e suas associações, cheio de javascript, drag&drop e mais. =)*

### Angular + Rest
O módulo <a href="https://github.com/luisfga/JEE8Demo/tree/master/frontend-rest">frontend-rest</a> contém a api necessária para frontends client-side. O módulo <a href="https://github.com/luisfga/JEE8Demo/tree/master/frontend-angular">frontend-angular</a> contém um projeto angular.
* *Por enquanto, há apenas a funcionalidade de Login, com exemplo de ajax com XmlHttpRequest e Json Web Token (JWT).*

### Os frontends são mutuamente excludentes
Apenas um deles pode estar no servidor, pois todos importam o .jar do módulo domain, que possui ejbs que não podem se repetir.

Os módulos WAR(jsf, struts e rest) possuem um arquivo (WEB-INF/resources.xml) com configuração de datasource para o TomEE. Esse arquivo pode ser excluído caso queira colocar o *resource* direto no servidor. Nesse arquivo está também contido um template para o *resource* da sessão de email.

Os testes unitários utilizam JUnit, Mockito e o ApplicationComposer do OpenEJB (embedded).

O módulo frontend-angular deve ser executado no nodeja. Outra opção é compilar, montar e colocar o diretório gerado (**dist**) num pacote junto com a api rest.

Foi desenvolvido com **TomEE 8.0.4** em um **JDK 1.8** (1.8.0_252). Também foi testado no **WildFly20**. Para o wildfly são necessárias pequenas alterações, como o escopo de algumas dependências (openjpa e hsqldb são padrão no TomEE e estão com o escopo **provided**). O modulo angular foi feito com nodejs v12.

#### Casos de uso

*Os casos de uso não utilizam interface @Remote, mas apenas o @LocalBean gerado automaticamente. Em caso de necessidade basta fazer com que eles implementem suas interfaces de um jar separado e utilizar esse jar tanto no 'domain' quanto nos frontends.*

##### Registrar
O usuário cadastra email, senha, nome e data de nascimento. A senha tem um campo para confirmação. O sistema grava os dados com o password codificado (hashing) e status NEW. O sistema utiliza o passwordService do Apache Shiro para codificar e verificar senhas. O sistema envia email para que o usuário confirme. O email enviado no link está codificado em Base64.
    
##### Confirmar registro
O usuário clica no link enviado por email e o sistema atualizado o status para OK.
    
##### Login
O usuário utiliza email e senha para logar. O sistema usa uma implementação própria do JdbcRealm do Apache Shiro para buscar os dados de autenticação no banco de dados. O passwordService verifica se o password confere com o que está salvo (hashed) no banco. 

OBS: o sistema foi feito pra transmissão via HTTPS, i.e. com TLS, por isso não codifica senhas no cliente antes de transmiti-las, o que é desnecessário no caso. Se por ventura se queira usar HTTP ao invés de HTTPS basta codificar a informações necessárias (basicamente senhas) no client-side. Nos frontends Struts ou JSF é preciso usar javascript. No angular, tranquilo, é tudo javascript mesmo. Mas optou-se por não fazê-lo pois a idéia é utilizar HTTPS.
    
##### Recuperação de password
O sistema pede data de nascimento e email antes de enviar um email com link para o funcionalidade "reset de senha". O sistema "abre uma janela" de 7 minutos para que a operação seja executada.
    
##### Reset de password
O usuário clica no link enviado para o email. O sistema valida se está dentro do limite de 7 minutos. Se sim, retorna para tela onde o usuário poderá digitar uma nova senha. O campo email é mostrado 'read-only' e a senha tem um campo para confirmação.
