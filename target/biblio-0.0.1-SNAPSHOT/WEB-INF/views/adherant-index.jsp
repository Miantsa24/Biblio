<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<c:set var="pageTitle" value="Accueil Adhérant" scope="request"/>
<jsp:include page="/WEB-INF/views/adherant-layout.jsp"/>
<div class="card">
    <div class="card-body">
        <h2 class="card-title">Bienvenue, <c:out value="${sessionScope.adherantNom}"/> !</h2>
        <p class="card-text">Utilisez le menu à gauche pour consulter les livres, vos prêts en cours ou vos réservations.</p>
    </div>
</div>