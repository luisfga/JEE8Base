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
        <span class="navButton" id="openNavButton" onclick="openNav()">&#9776;</span>
        <div id="navbar" class="sidenav">
            <span class="navButton" id="closeNavButton" onclick="closeNav()">&times;</span>
            <div id="navBarOpts" class="navBarOpts">
                <a href="#">Comprar</a>
                <a href="#">Vender</a>
                <a href="#">Promoções</a>
                <a href="#">Sair</a>
            </div>
        </div>
        ${pageContext.request.userPrincipal.name}
        <div class="listas">
            <h3 class="cabecalho">Ofertas</h3>
            <span class="badge-counter">10</span>
            <div class="painelOfertas">
                
            </div>
        </div>

    </body>

    <script>
        function openNav() {
          document.getElementById("navbar").style.width = "250px";
        }

        function closeNav() {
          document.getElementById("navbar").style.width = "0";
        }
    </script>
</html>