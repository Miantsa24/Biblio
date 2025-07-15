<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>
<c:set var="pageTitle" value="Liste des adhérants" scope="request"/>
<jsp:include page="/WEB-INF/views/layout.jsp"/>
<div class="card">
    <div class="card-body">
        <h2 class="card-title">Liste des adhérants inscrits</h2>
        
        <c:if test="${not empty errorMessage}">
            <div class="alert alert-danger">${errorMessage}</div>
        </c:if>
        
        <div class="table-responsive">
            <table class="table table-hover">
                <thead>
                    <tr>
                        <th>Nom de l'adhérant</th>
                        <th>Date de naissance</th>
                        <th>Email</th>
                        <th>Type d'adhérant</th>
                        <th>Quota restant</th>
                        <th>Quota emprunts</th>
                        <th>Quota réservations</th>
                        <th>Quota prolongements</th>
                        <th>Date de début d'abonnement</th>
                        <th>Date de fin d'abonnement</th>
                    </tr>
                </thead>
                <tbody>
                    <c:forEach var="adherant" items="${adherants}">
                        <c:set var="latestAbonnement" value="${null}"/>
                        <c:forEach var="abonnement" items="${adherant.abonnements}">
                            <c:if test="${empty latestAbonnement or abonnement.dateDebut gt latestAbonnement.dateDebut}">
                                <c:set var="latestAbonnement" value="${abonnement}"/>
                            </c:if>
                        </c:forEach>
                        <tr>
                            <td>${adherant.nom} ${adherant.prenom}</td>
                            <td>${adherant.dateNaissance}</td>
                            <td>${adherant.email}</td>
                            <td>${adherant.typeAdherant.nomType}</td>
                            <td>${adherant.quotaRestant}</td>
                            <td>${adherant.typeAdherant.quotaEmprunts}</td>
                            <td>${adherant.typeAdherant.quotaReservations}</td>
                            <td>${adherant.typeAdherant.quotaProlongements}</td>
                            <td>${latestAbonnement.dateDebut}</td>
                            <td>${latestAbonnement.dateFin}</td>
                        </tr>
                    </c:forEach>
                    <c:if test="${empty adherants}">
                        <tr>
                            <td colspan="10" class="text-center">Aucun adhérant avec abonnement valide trouvé.</td>
                        </tr>
                    </c:if>
                </tbody>
            </table>
        </div>
    </div>
</div>