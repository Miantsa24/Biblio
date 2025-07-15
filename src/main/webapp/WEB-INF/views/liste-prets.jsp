<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<c:set var="pageTitle" value="Prêts en cours" scope="request"/>
<!-- <c:set var="contentPage" value="/WEB-INF/views/liste-prets.jsp" scope="request"/> -->
<jsp:include page="/WEB-INF/views/layout.jsp"/>
<div class="card">
    <div class="card-body">
        <h2 class="card-title">Prêts en cours</h2>
        <c:if test="${empty pretsEnCours}">
            <p class="error-message">Aucun prêt en cours</p>
        </c:if>
        <c:if test="${not empty pretsEnCours}">
            <div class="table-responsive">
                <table class="table table-bordered table-hover">
                    <thead>
                        <tr>
                            <th>ID du prêt</th>
                            <th>Nom de l'adhérant</th>
                            <th>Prénom de l'adhérant</th>
                            <th>Titre du livre</th>
                            <th>Type de prêt</th>
                            <th>Date du prêt</th>
                            <th>Date de retour prévue</th>
                        </tr>
                    </thead>
                    <tbody>
                        <c:forEach var="pret" items="${pretsEnCours}">
                            <tr>
                                <td>${pret.id}</td>
                                <td>${pret.adherant.nom}</td>
                                <td>${pret.adherant.prenom}</td>
                                <td>${pret.exemplaire.livre.titre}</td>
                                <td>${pret.typePret}</td>
                                <td>${pret.datePret}</td>
                                <td>${pret.dateRetourPrevue}</td>
                            </tr>
                        </c:forEach>
                    </tbody>
                </table>
            </div>
        </c:if>
    </div>
</div>