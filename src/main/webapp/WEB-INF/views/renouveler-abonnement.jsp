<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<c:set var="pageTitle" value="Renouveler un abonnement" scope="request"/>
<jsp:include page="/WEB-INF/views/layout.jsp"/>
<div class="container mt-4">
    <div class="card">
        <div class="card-body">
            <h2 class="card-title">Renouveler un abonnement</h2>
            
            <c:if test="${not empty errorMessage}">
                <div class="alert alert-danger">${errorMessage}</div>
            </c:if>
            <c:if test="${not empty successMessage}">
                <div class="alert alert-success">${successMessage}</div>
            </c:if>

            <form action="/renouveler" method="post">
                <div class="mb-3">
                    <label for="idAdherant" class="form-label">ID de l'adhérant</label>
                    <input type="number" class="form-control" id="idAdherant" name="idAdherant" required>
                </div>
                <div class="mb-3">
                    <label for="dateDebutAbonnement" class="form-label">Nouvelle date de début d'abonnement</label>
                    <input type="date" class="form-control" id="dateDebutAbonnement" name="dateDebutAbonnement" required>
                </div>
                <button type="submit" class="btn btn-primary">Renouveler</button>
                <a href="/adherants" class="btn btn-secondary">Annuler</a>
            </form>
        </div>
    </div>
</div>