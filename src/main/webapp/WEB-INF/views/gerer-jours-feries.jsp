
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<c:set var="pageTitle" value="Gérer les jours fériés" scope="request"/>
<jsp:include page="/WEB-INF/views/layout.jsp"/>
<div class="card">
    <div class="card-body">
        <h2 class="card-title">Gérer les jours fériés</h2>

        <!-- Formulaire d'ajout -->
        <form action="/jours-feries" method="post">
            <div class="mb-3">
                <label for="dateFerier" class="form-label">Date du jour férié</label>
                <input type="date" class="form-control" id="dateFerier" name="dateFerier" required>
            </div>
            <div class="mb-3">
                <label for="description" class="form-label">Description (facultatif)</label>
                <input type="text" class="form-control" id="description" name="description">
            </div>
            <button type="submit" class="btn btn-primary">Enregistrer</button>
        </form>

        <!-- Messages d'erreur ou de succès -->
        <c:if test="${not empty errorMessage}">
            <div class="alert alert-danger mt-3">${errorMessage}</div>
        </c:if>
        <c:if test="${not empty successMessage}">
            <div class="alert alert-success mt-3">${successMessage}</div>
        </c:if>

        <!-- Liste des jours fériés -->
        <h3 class="card-title mt-4">Liste des jours fériés</h3>
        <div class="table-responsive">
            <table class="table table-hover">
                <thead>
                    <tr>
                        <th>Date</th>
                        <th>Description</th>
                    </tr>
                </thead>
                <tbody>
                    <c:forEach var="jourFerier" items="${joursFeries}">
                        <tr>
                            <td>${jourFerier.dateFerier}</td>
                            <td>${jourFerier.description != null ? jourFerier.description : 'Aucune description'}</td>
                        </tr>
                    </c:forEach>
                    <c:if test="${empty joursFeries}">
                        <tr>
                            <td colspan="2" class="text-center">Aucun jour férié enregistré.</td>
                        </tr>
                    </c:if>
                </tbody>
            </table>
        </div>
    </div>
</div>
