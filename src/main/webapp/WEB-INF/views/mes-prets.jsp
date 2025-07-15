<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<c:set var="pageTitle" value="Mes prêts en cours" scope="request"/>
<jsp:include page="/WEB-INF/views/adherant-layout.jsp"/>
<div class="card">
    <div class="card-body">
        <h2 class="card-title">Mes prêts en cours</h2>
        <c:if test="${not empty successMessage}">
            <div class="alert alert-success">${successMessage}</div>
        </c:if>
        <c:if test="${not empty errorMessage}">
            <div class="alert alert-danger">${errorMessage}</div>
        </c:if>
        <c:if test="${empty pretsEnCours}">
            <p class="error-message">Aucun prêt en cours</p>
        </c:if>
        <c:if test="${not empty pretsEnCours}">
            <div class="table-responsive">
                <table class="table table-bordered table-hover">
                    <thead>
                        <tr>
                            <th>ID du prêt</th>
                            <th>Titre du livre</th>
                            <th>Type de prêt</th>
                            <th>Date du prêt</th>
                            <th>Date de retour prévue</th>
                            <th>Prolongements effectués</th>
                            <th>Action</th>
                        </tr>
                    </thead>
                    <tbody>
                        <c:forEach var="pret" items="${pretsEnCours}">
                            <tr>
                                <td>${pret.id}</td>
                                <td>${pret.exemplaire.livre.titre}</td>
                                <td>${pret.typePret}</td>
                                <td>${pret.datePret}</td>
                                <td>${pret.dateRetourPrevue}</td>
                                <td>${pret.nombreProlongements}</td>
                                <td>
                                    <c:if test="${adherant.quotaRestantProlongement > 0 && pret.nombreProlongements < adherant.typeAdherant.quotaProlongements}">
                                        <c:set var="hasPendingProlongement" value="${prolongementRepository.findByPretIdAndStatut(pret.id, 'EN_ATTENTE').isEmpty()}" />
                                        <c:if test="${hasPendingProlongement}">
                                            <form action="/adherant/prolongements/demander" method="post" style="display:inline;">
                                                <input type="hidden" name="idPret" value="${pret.id}">
                                                <button type="submit" class="btn btn-primary btn-sm">Prolonger</button>
                                            </form>
                                        </c:if>
                                        <c:if test="${!hasPendingProlongement}">
                                            <span class="text-muted">Demande en attente</span>
                                        </c:if>
                                    </c:if>
                                    <c:if test="${adherant.quotaRestantProlongement <= 0 || pret.nombreProlongements >= adherant.typeAdherant.quotaProlongements}">
                                        <span class="text-muted">Prolongement non disponible</span>
                                    </c:if>
                                </td>
                            </tr>
                        </c:forEach>
                    </tbody>
                </table>
            </div>
        </c:if>
    </div>
</div>