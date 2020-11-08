<!DOCTYPE html>
<%@ taglib prefix="s" uri="/struts-tags" %>
<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<s:set var="rootPath">${pageContext.request.contextPath}</s:set>
<html>
    <head>
        <meta name="viewport" content="width=device-width, initial-scale=1.0" charset="UTF-8">
        <title><s:text name="main.title"/></title>
        <link rel="stylesheet" href="${rootPath}/css/main.css"/>
        <link rel="stylesheet" href="${rootPath}/css/dashboard.css"/>
        <script type="text/javascript" src="${rootPath}/javascript/main.js" defer></script>
    </head>

    <body>
        <table class="table">
            <tr>
                <td colspan="2" class="top-row">
                    <span>${pageContext.request.userPrincipal.name}</span>
                    <s:url var="urlLogout" action="logout"/>
                    <s:a href="%{urlLogout}" class="button">Logout</s:a> 
                </td>
            </tr>
            <tr>
                <td class="sec-row-side-menu">
                    </br>
                    <a href="" class="menu-item">Opção 1</a></br>
                    <a href="" class="menu-item">Opção 2</a></br>
                </td>
                <td class="sec-row-content">
                    <table class="content-table">
                        <tr>
                            <!--Painel "ROLES" da parte superior - Painel 1-->
                            <td class="content-tb-row1-col1">
                                Roles - add or remove system's roles
                                <table class="tb">
                                    <caption class="tb-caption"><s:text name="roles"/></caption>
                                    <s:iterator value="roles">
                                    <tr><td><s:property value="roleName"/></td></tr>
                                    </s:iterator>
                                </table>
                            </td>
                            <!--Painel "PERMISSIONS" da parte superior - Painel 2-->
                            <td class="content-tb-row1-col2">
                                <table class="tb">
                                    <caption class="tb-caption"><s:text name="permissions"/></caption>
                                    <thead>
                                        <tr>
                                            <td>Módulo</td>
                                            <td>Action</td>
                                        </tr>
                                    </thead>
                                    <tr>
                                        <td>Módulo1</td>
                                        <td>Create</td>
                                    </tr>
                                </table>
                            </td>
                            <!--Painel "TERCEITO" da parte superior - Painel 3-->
                            <td class="content-tb-row1-col3">
                                <table class="tb">
                                    <caption class="tb-caption">Roles&Permissions</caption>
                                    <tr><td>temp data</td></tr>
                                </table>
                            </td>
                        </tr>
                        
                        <!--Painel inferior da área principal-->
                        <tr>
                            <td colspan="2" class="content-tb-bottom">
                                <table class="tb">
                                    <caption class="tb-caption"><s:text name="users"/></caption>
                                    <thead>
                                        <tr>
                                            <td class="tb-users-head"><s:text name="birthday"/></td>
                                            <td class="tb-users-head"><s:text name="userName"/></td>
                                            <td class="tb-users-head"><s:text name="email"/></td>
                                            <td class="tb-users-head"><s:text name="roles"/></td>
                                        </tr>                                        
                                    </thead>
                                    <tbody>
                                        <tr>
                                            <td class="tb-users-col-filters"><input type="text"/></td>
                                            <td class="tb-users-col-filters"><input type="text"/></td>
                                            <td class="tb-users-col-filters"><input type="text"/></td>
                                            <td class="tb-users-col-filters"><input type="text"/></td>
                                        </tr>
                                        <s:iterator value="users">
                                        <tr>
                                            <td><s:property value="birthday"/></td>
                                            <td><s:property value="userName"/></td>
                                            <td><s:property value="email"/></td>
                                            <td><s:iterator value="roles"><s:property value="roleName"/></br></s:iterator></td>
                                        </tr>
                                        </s:iterator>
                                        </tbody>
                                </table>
                            </td>
                        </tr>
                    </table>
                </td>
            </tr>
        </table>
            
    </body>

    <script>

    </script>
</html>