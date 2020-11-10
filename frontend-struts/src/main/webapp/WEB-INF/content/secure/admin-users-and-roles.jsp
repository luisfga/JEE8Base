<%@ taglib prefix="s" uri="/struts-tags" %>
<%@ taglib prefix="shiro" uri="http://shiro.apache.org/tags" %>
<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<s:set var="rootPath">${pageContext.request.contextPath}</s:set>
<html>
    <head>
        <meta name="viewport" content="width=device-width, initial-scale=1.0" charset="UTF-8">
        <title><s:text name="main.title"/></title>
        <link rel="stylesheet" href="${rootPath}/css/main.css"/>
        <link rel="stylesheet" href="${rootPath}/css/dashboard.css"/>
        <script>
            function toggleMenu() {
                console.log("Display = " + document.getElementById("menu-bar").style.display);
                if (document.getElementById("menu-bar").style.display === ""){
                    console.log("Agora -> none");
                    document.getElementById("menu-bar").style.display = "none";
                } else {
                    console.log("Agora -> mostrar");
                    document.getElementById("menu-bar").style.display = "";
                }
            }

            function closeNav() {
              document.getElementById("menu-bar").style.display = "none";
            }
        </script>
    </head>

    <body>
        <div class="top-row">
            <span class="user-principal"><shiro:principal/></span>
            <span class="menu-toggle" onclick="toggleMenu()">&#9776;</span>
        </div>

        <table class="content-table">
            <tr>
                <td class="side-menu-td" id="menu-bar">
                    <div class="side-menu">
                        <br>
                        
                        <br><br>
                        
                        <!-- Opções apenas para a role ADMIN -->
                        <shiro:hasRole name="Admin">
                            <div class="menu-section-title"><s:text name="system.administration"/></div>
                            <s:url action="admin-roles-and-permissions" var="adminUrl" />
                            <s:a href="%{adminUrl}" class="menu-item"><s:text name="roles.and.permissions"/></s:a><br>
                            
                            <s:url action="admin-users-and-roles" var="adminUrl" />
                            <s:a href="%{adminUrl}" class="menu-item"><s:text name="users.and.roles"/></s:a><br>
                        </shiro:hasRole>
                        
                        <s:url action="logout" var="logoutUrl"/>
                        <s:a href="%{logoutUrl}" class="logout-menu-item"><s:text name="logout"/></s:a>  
                    </div>
                </td>
                <td class="content-td">
                    <div class="content">
                        
                        <!-- ROLES -->
                        <div id="roleMessage" class="info-msg"></div>
                        <div class="tb">
                            <div class="tb-caption">
                                <div class="cap-title"><s:text name="roles"/></div>
                            </div>
                            <div id="rolesContainer" class="tag-container">
                                <s:iterator value="roles">
                                <span id="role-<s:property value="roleName"/>" 
                                      class="item-tag" 
                                      draggable="true" 
                                      ondragstart="onDragStart(event);"><s:property value="roleName"/></span>
                                </s:iterator>
                            </div>
                        </div>

                        <!--TABELA USUÁRIOS-->
                        <div class="tb">
                            <div class="tb-caption">
                                <div class="cap-title"><s:text name="users"/></div>
                            </div>
                            <div id="users-search" class="input-group">
                                <s:label key="email" theme="simple"/>
                                <s:textfield key="email" theme="simple"/>
                                <s:url var="urlSearch" action="searchUser"/>
                                <s:a href="%{urlSearch}" class="action-button">Buscar</s:a>                                 
                            </div>
                        </div>

                    </div>
                </td>
            </tr>
        </table>

    </body>
    
    <script>
    /*XXXXXXXXXXXXXXXXXXX
      Grupos & Permissões
    XXXXXXXXXXXXXXXXXXXXX*/
    function onRoleSelectionDrop(event) {
        event.preventDefault();//cancel forward trickery

    }
    
    /*XXXXXXXXXXXXXXXXXXXXXXXX
    Generic Drag&Drop Handlers
    XXXXXXXXXXXXXXXXXXXXXXXXXX*/
    function reset(){

    }
    function onDragStart(event) {
        event.dataTransfer.setData('text/plain', event.target.id);
    }
    function onDragOver(event) {
        event.preventDefault();
    }
    function onDragEnd(event) {
        event.preventDefault();
    }

    </script>
</html>