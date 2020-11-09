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
            <span class="menu-toggle" onclick="toggleMenu()">&#9776;</span>
        </div>

        <table class="content-table">
            <tr>
                <td class="side-menu-td" id="menu-bar">
                    <div class="side-menu">
                        <br>
                        <div class="menu-title"><shiro:principal/></div>
                        <br><br>
                        
                        <!-- Opções apenas para a role ADMIN -->
                        <shiro:hasRole name="Admin">
                            <s:url action="admin" var="adminUrl" />
                            <s:a href="%{adminUrl}" class="menu-item"><s:text name="system.administration"/></s:a><br>
                        </shiro:hasRole>
                            
                        <s:url action="logout" var="logoutUrl"/>
                        <s:a href="%{logoutUrl}" class="menu-item"><s:text name="logout"/></s:a>
                    </div>
                </td>
                <td class="content-td">
                    <div class="content">
                        
                    </div>
                </td>
            </tr>
        </table>

    </body>
</html>