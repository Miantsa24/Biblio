
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<c:set var="pageTitle" value="Accueil" scope="request"/>
<jsp:include page="/WEB-INF/views/layout.jsp"/>
<div class="card">
    <div class="card-body">
        <h2 class="card-title">Bienvenue dans le système de gestion de la bibliothèque</h2>
        <p class="card-text">Utilisez le menu à gauche pour gérer les adhérants, les prêts et les retours.</p>
    </div>
</div>

