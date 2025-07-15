<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<c:set var="pageTitle" value="Pénalités en cours" scope="request"/>
<jsp:include page="/WEB-INF/views/layout.jsp"/>
<div class="card">
    <div class="card-body">
        <h2 class="card-title">Pénalités en cours</h2>

        <!-- Liste des pénalités en cours -->
        <div class="table-responsive">
            <table class="table table-hover">
                <thead>
                    <tr>
                        <th>Nom de l'adhérant</th>
                        <th>Type de pénalité</th>
                        <th>Date de retour prévue</th>
                        <th>Date de retour réelle</th>
                        <th>Nombre de jours</th>
                        <th>Date de fin de pénalité</th>
                    </tr>
                </thead>
                <tbody>
                    <c:forEach var="penalite" items="${penalites}">
                        <tr>
                            <td>${penalite.adherant.nom} ${penalite.adherant.prenom}</td>
                            <td>${penalite.typePenalite}</td>
                            <td>${penalite.pret.dateRetourPrevue}</td>
                            <td>${penalite.pret.dateRetourReelle}</td>
                            <td>${penalite.nombreJours}</td>
                            <td>${penalite.dateFinPenalite}</td>
                        </tr>
                    </c:forEach>
                    <c:if test="${empty penalites}">
                        <tr>
                            <td colspan="6" class="text-center">Aucune pénalité en cours.</td>
                        </tr>
                    </c:if>
                </tbody>
            </table>
        </div>
    </div>
</div>