<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<c:set var="pageTitle" value="Mes réservations" scope="request"/>
<jsp:include page="/WEB-INF/views/adherant-layout.jsp"/>
<style>
main .card {
  background: linear-gradient(145deg, #ffffff, #f6f9ff);
  border: none;
  border-radius: 12px;
  box-shadow: 0 6px 12px rgba(0, 0, 0, 0.15);
  padding: 20px;
  max-width: 1000px;
  margin: 20px auto;
}

main .card-title {
  font-family: 'Roboto', 'Poppins', sans-serif;
  font-size: 26px;
  font-weight: 700;
  color: #4A6FA5;
  margin-bottom: 20px;
  text-align: center;
}

main .table th, main .table td {
  font-family: 'Roboto', 'Poppins', sans-serif;
  font-size: 14px;
  color: #333;
  vertical-align: middle;
}

main .table th {
  color: #fdfefe;
  font-weight: 600;
}

main .badge {
  font-size: 0.9em;
  padding: 8px 12px;
  border-radius: 12px;
  font-weight: 500;
}

main .badge.bg-success {
  background-color: #28a745;
  color: #fff;
}

main .badge.bg-warning {
  background-color: #ffc107;
  color: #333;
}

main .badge.bg-info {
  background-color: #17a2b8;
  color: #fff;
}

main .badge.bg-danger {
  background-color: #dc3545;
  color: #fff;
}

main .alert-danger {
  background-color: #f8d7da;
  color: #721c24;
  border-radius: 8px;
  margin-bottom: 20px;
}
</style>
<main class="container mt-3">
    <div class="card">
        <div class="card-body">
            <h2 class="card-title">Mes réservations</h2>

            <c:if test="${not empty errorMessage}">
                <div class="alert alert-danger">${errorMessage}</div>
            </c:if>

            <div class="table-responsive">
                <table class="table table-hover">
                    <thead>
                        <tr>
                            <th>ID Réservation</th>
                            <th>Titre du livre</th>
                            <th>Exemplaire</th>
                            <th>Date de réservation</th>
                            <th>Statut</th>
                        </tr>
                    </thead>
                    <tbody>
                        <c:forEach var="reservation" items="${reservations}">
                            <tr>
                                <td>${reservation.id}</td>
                                <td>
                                    <c:choose>
                                        <c:when test="${reservation.exemplaire != null && reservation.exemplaire.livre != null}">
                                            ${reservation.exemplaire.livre.titre}
                                        </c:when>
                                        <c:otherwise>
                                            Non trouvé
                                        </c:otherwise>
                                    </c:choose>
                                </td>
                                <td>${reservation.idExemplaire}</td>
                                <td>${reservation.dateReservation}</td>
                                <td>
                                    <c:choose>
                                        <c:when test="${reservation.statut == 'EN_ATTENTE'}">
                                            <span class="badge bg-info">En attente</span>
                                        </c:when>
                                        <c:when test="${reservation.statut == 'HONOREE'}">
                                            <span class="badge bg-success">Honorée</span>
                                        </c:when>
                                        <c:when test="${reservation.statut == 'ANNULEE'}">
                                            <span class="badge bg-danger">Annulée</span>
                                        </c:when>
                                        <c:when test="${reservation.statut == 'TERMINEE'}">
                                            <span class="badge bg-success">Terminée</span>
                                        </c:when>
                                    </c:choose>
                                </td>
                            </tr>
                        </c:forEach>
                        <c:if test="${empty reservations}">
                            <tr>
                                <td colspan="5" class="text-center">Aucune réservation trouvée.</td>
                            </tr>
                        </c:if>
                    </tbody>
                </table>
            </div>
        </div>
    </div>
</main>