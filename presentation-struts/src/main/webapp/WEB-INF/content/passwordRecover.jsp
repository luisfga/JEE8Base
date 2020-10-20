<!DOCTYPE html>
<%@ taglib prefix="s" uri="/struts-tags" %>
<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<s:set var="rootPath">${pageContext.request.contextPath}</s:set>
<html>
    <head>
        <meta name="viewport" content="width=device-width, initial-scale=1.0" charset="UTF-8">
        <title><s:text name="main.title"/></title>
        <link rel="stylesheet" href="${rootPath}/css/main.css"/>
        <link rel="stylesheet" href="${rootPath}/css/custom_struts2_css_xhtml_theme.css"/>
    </head>
    <body style="background-image: url('${rootPath}/images/light-blue-wave-background.jpg');">
        <div class="languageFlags">
            <s:a href="?request_locale=pt_BR"><img src="${rootPath}/images/brazil-flag.png" class="langflag"/></s:a>
            <s:a href="?request_locale=en"><img src="${rootPath}/images/usa-flag.png" class="langflag"/></s:a>
        </div>
        <div class="inputPanelOffLogin">
            <h1 class="h1Center"><s:text name="header.password.recovery"/></h1>
            <h4 ><s:text name="header.orientations"/></h4>
            <p class="plainText"><s:text name="orientation.text.recover"/></p>
            <br/>
            <s:actionmessage escape="false"/>
            <s:actionerror escape="false"/>
            <s:form action="passwordRecover" labelposition="top" class="wwFormTable">
                <s:textfield key="birthday" type="date" cssClass="inputField" errorPosition="bottom" requiredLabel="true"/>
                <s:textfield key="email" cssClass="inputField" errorPosition="bottom" requiredLabel="true"/>
                <s:token/>
                <s:submit key="recover" name="" cssClass="button customFormButton"/>
            </s:form>
        </div>
    </body>
</html>