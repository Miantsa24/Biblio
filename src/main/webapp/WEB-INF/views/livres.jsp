<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<c:set var="pageTitle" value="Liste des livres" scope="request"/>
<jsp:include page="/WEB-INF/views/adherant-layout.jsp"/>
<style>
main .section {
  margin-bottom: 40px;
}

main .section h3 {
  font-family: 'Roboto', 'Poppins', sans-serif;
  font-size: 22px;
  font-weight: 600;
  color: #4A6FA5;
  margin-bottom: 15px;
  text-transform: capitalize;
  position: relative;
  display: inline-block;
  padding: 6px 10px;
  background-color: #ffffff;
  border-radius: 5px;
  box-shadow: 0 2px 4px rgba(0, 0, 0, 0.1);
  transition: transform 0.3s ease, color 0.3s ease, box-shadow 0.3s ease;
}

main .section h3:hover {
  color: var(--primary-color);
  transform: translateY(-2px);
  box-shadow: 0 4px 8px rgba(0, 0, 0, 0.15);
}

main h2.page-title {
  font-family: 'Roboto', 'Poppins', sans-serif;
  font-size: 26px;
  font-weight: 700;
  color: #4A6FA5;
  margin-bottom: 20px;
  margin-top: 5px;
}

.card.exemplaire-card {
  background: linear-gradient(145deg, #ffffff, #f6f9ff);
  border: none;
  border-radius: 12px;
  box-shadow: 0 6px 12px rgba(0, 0, 0, 0.15);
  transition: transform 0.3s ease, box-shadow 0.3s ease;
  overflow: hidden;
}

.card.exemplaire-card:hover {
  transform: translateY(-8px);
  box-shadow: 0 8px 16px rgba(0, 0, 0, 0.2);
}

.card.exemplaire-card .card-body {
  padding: 20px;
}

.card.exemplaire-card .card-title {
  color: #4A6FA5;
  font-size: 18px;
  font-weight: 600;
  margin-bottom: 15px;
  text-transform: uppercase;
}

.card.exemplaire-card .card-text {
  font-size: 14px;
  color: #333;
  line-height: 1.8;
}

.card.exemplaire-card .card-text strong {
  color: #4A6FA5;
}

.card.exemplaire-card .badge {
  font-size: 0.9em;
  padding: 8px 12px;
  border-radius: 12px;
  font-weight: 500;
  transition: transform 0.2s ease;
}

.card.exemplaire-card .badge:hover {
  transform: scale(1.1);
}

.badge.bg-success {
  background-color: #28a745;
  color: #fff;
}

.badge.bg-warning {
  background-color: #ffc107;
  color: #333;
}

.badge.bg-info {
  background-color: #17a2b8;
  color: #fff;
}

.badge.bg-primary {
  background-color: #4A6FA5;
  color: #fff;
}

.card.exemplaire-card .btn-primary {
  background-color: #4A6FA5;
  border-color: #4A6FA5;
  font-size: 14px;
  padding: 8px 16px;
  border-radius: 8px;
  display: flex;
  align-items: center;
  gap: 5px;
  transition: background-color 0.3s ease, transform 0.2s ease;
}

.card.exemplaire-card .btn-primary:hover {
  background-color: #3745c8;
  border-color: #3745c8;
  transform: scale(1.05);
}

.card.exemplaire-card .btn-primary i {
  font-size: 16px;
}
</style>
<main class="container mt-3">
    <h2 class="page-title">Liste des livres</h2>
    <c:if test="${not empty errorMessage}">
        <div class="alert alert-danger">${errorMessage}</div>
    </c:if>
    <c:forEach var="livre" items="${livres}">
        <section class="section mb-5">
            <h3 class="mb-3">Exemplaires pour : <c:out value="${livre.titre}"/></h3>
            <div class="row row-cols-1 row-cols-md-3 g-4">
                <c:forEach var="exemplaire" items="${livre.exemplaires}">
                    <div class="col">
                        <div class="card exemplaire-card h-100">
                            <div class="card-body">
                                <h5 class="card-title"><c:out value="${livre.titre}"/></h5>
                                <p class="card-text">
                                    <strong>Auteur :</strong> <c:out value="${livre.auteur}"/><br>
                                    <strong>ISBN :</strong> <c:out value="${livre.isbn}"/><br>
                                    <strong>Âge minimum :</strong> <c:out value="${livre.ageMinimum}"/><br>
                                    <strong>Exemplaire ID :</strong> <c:out value="${exemplaire.id}"/><br>
                                    <strong>Statut :</strong> 
                                    <c:choose>
                                        <c:when test="${exemplaire.statut == 'DISPONIBLE'}">
                                            <span class="badge bg-success">Disponible</span>
                                        </c:when>
                                        <c:when test="${exemplairePrets.contains(exemplaire.id)}">
                                            <span class="badge bg-warning">Emprunté par vous</span>
                                        </c:when>
                                        <c:when test="${exemplaireReservationsHonorees.contains(exemplaire.id)}">
                                            <span class="badge bg-primary">Réservé par vous</span>
                                        </c:when>
                                        <c:when test="${exemplaire.statut == 'EMPRUNTE'}">
                                            <span class="badge bg-warning">Emprunté</span>
                                        </c:when>
                                        <c:when test="${exemplaire.statut == 'RESERVE'}">
                                            <span class="badge bg-info">Réservé</span>
                                        </c:when>
                                    </c:choose>
                                </p>
                                <c:if test="${(exemplaire.statut == 'EMPRUNTE' || exemplaire.statut == 'RESERVE') && !exemplairePrets.contains(exemplaire.id) && !exemplaireReservations.contains(exemplaire.id)}">
                                    <a href="/adherant/reservations?idExemplaire=${exemplaire.id}" class="btn btn-primary btn-sm">
                                        <i class="bi bi-bookmark-plus"></i> Réserver
                                    </a>
                                </c:if>
                            </div>
                        </div>
                    </div>
                </c:forEach>
            </div>
        </section>
    </c:forEach>
</main>