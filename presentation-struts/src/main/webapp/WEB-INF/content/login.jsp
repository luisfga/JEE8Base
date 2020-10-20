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
            <h1 class="h1Center"><s:text name="header.login"/></h1>
            <s:actionmessage/>
            <s:actionerror escape="false"/>
            <s:form action="login" labelposition="top" class="wwFormTable">
                <s:textfield key="email" cssClass="inputField" errorPosition="bottom" />
                <s:password key="password" cssClass="inputField" errorPosition="bottom"/>
                <s:submit key="login" name="" cssClass="button customFormButton"/>
            </s:form>
            <div class="customExtraFooter">
                <div class="plainText"><s:text name="question.dont.have.account"/></div>
                <br/>
                <s:url var="urlRegisterInput" action="registerInput"/>
                <s:a href="%{urlRegisterInput}" cssClass="button"><s:text name="new.account"/></s:a>
                
                <br/><br/><br/><br/>
                <s:url var="urlPasswordRecoverInput" action="passwordRecoverInput"/>
                <s:a href="%{urlPasswordRecoverInput}" class="undecLink tinyText"><s:text name="question.forgot.password"/></s:a>
            </div>

        </div>
    </body>
</html>