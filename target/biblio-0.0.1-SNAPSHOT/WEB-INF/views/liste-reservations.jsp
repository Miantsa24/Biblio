
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<c:set var="pageTitle" value="Réservations en attente" scope="request"/>
<jsp:include page="/WEB-INF/views/layout.jsp"/>
<div class="card">
    <div class="card-body">
        <h2 class="card-title">Réservations en attente</h2>

        <c:if test="${not empty errorMessage}">
            <div class="alert alert-danger">${errorMessage}</div>
        </c:if>
        <c:if test="${param.success == 'approuver'}">
            <div class="alert alert-success">Réservation approuvée avec succès !</div>
        </c:if>
        <c:if test="${param.success == 'refuser'}">
            <div class="alert alert-success">Réservation refusée avec succès !</div>
        </c:if>

        <div class="table-responsive">
            <table class="table table-hover">
                <thead>
                    <tr>
                        <th>ID Réservation</th>
                        <th>Nom de l'adhérant</th>
                        <th>Titre du livre</th>
                        <th>Exemplaire</th>
                        <th>Date de réservation</th>
                        <th>Actions</th>
                    </tr>
                </thead>
                <tbody>
                    <c:forEach var="reservation" items="${reservationsEnAttente}">
                        <tr>
                            <td>${reservation.id}</td>
                            <td>
                                <c:set var="adherant" value="${adherantRepository.findById(reservation.idAdherant).orElse(null)}" />
                                ${adherant != null ? adherant.nom : 'Non trouvé'} ${adherant != null ? adherant.prenom : ''}
                            </td>
                            <td>
                                <c:set var="exemplaire" value="${exemplaireRepository.findById(reservation.idExemplaire).orElse(null)}" />
                                <c:set var="livre" value="${exemplaire != null ? livreRepository.findById(exemplaire.idLivre).orElse(null) : null}"/>
                                ${livre != null ? livre.titre : 'Non trouvé'}
                            </td>
                            <td>${reservation.idExemplaire}</td>
                            <td>${reservation.dateReservation}</td>
                            <td>
                                <form action="/reservations/approuver" method="post" style="display:inline;">
                                    <input type="hidden" name="idReservation" value="${reservation.id}">
                                    <button type="submit" class="btn btn-success btn-sm">Approuver</button>
                                </form>
                                <form action="/reservations/refuser" method="post" style="display:inline;">
                                    <input type="hidden" name="idReservation" value="${reservation.id}">
                                    <button type="submit" class="btn btn-danger btn-sm">Refuser</button>
                                </form>
                            </td>
                        </tr>
                    </c:forEach>
                    <c:if test="${empty reservationsEnAttente}">
                        <tr>
                            <td colspan="6" class="text-center">Aucune réservation en attente.</td>
                        </tr>
                    </c:if>
                </tbody>
            </table>
        </div>
    </div>
</div>
