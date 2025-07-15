<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="jakarta.tags.core" %>
<c:set var="pageTitle" value="Rendre un livre" scope="request"/>
<!-- <c:set var="contentPage" value="/WEB-INF/views/rendreLivre.jsp" scope="request"/> -->
<jsp:include page="/WEB-INF/views/layout.jsp"/>
<div class="card">
    <div class="card-body">
        <h2 class="card-title">Rendre un livre</h2>

        <c:if test="${not empty errorMessage}">
            <div class="error-message">${errorMessage}</div>
        </c:if>
        <c:if test="${not empty penaltyMessage}">
            <c:choose>
                <c:when test="${penaltyMessage.contains('pénalisé')}">
                    <div class="penalty-message">${penaltyMessage}</div>
                </c:when>
                <c:otherwise>
                    <div class="success-message">${penaltyMessage}</div>
                </c:otherwise>
            </c:choose>
        </c:if>
        <c:if test="${param.success == 'true'}">
            <div class="success-message">Livre rendu avec succès !</div>
        </c:if>

        <form action="/retours" method="post">
            <div class="row">
                <div class="col-md-6 mb-3">
                    <div class="form-floating">
                        <input type="text" id="idAdherant" name="idAdherant" class="form-control" placeholder="ID de l'adhérant" required>
                        <label for="idAdherant">ID de l'adhérant</label>
                    </div>
                </div>
                <div class="col-md-6 mb-3">
                    <div class="form-floating">
                        <input type="text" id="idExemplaire" name="idExemplaire" class="form-control" placeholder="ID de l'exemplaire" required>
                        <label for="idExemplaire">ID de l'exemplaire</label>
                    </div>
                </div>
            </div>
            <div class="mb-3">
                <div class="form-floating">
                    <input type="date" id="dateRetourReelle" name="dateRetourReelle" class="form-control" placeholder="Date de retour réelle" required>
                    <label for="dateRetourReelle">Date de retour réelle</label>
                </div>
            </div>
            <div class="row">
                <div class="col-md-6">
                    <button type="submit" class="btn btn-primary w-100">Rendre</button>
                </div>
                <div class="col-md-6">
                    <button type="reset" class="btn btn-secondary w-100">Réinitialiser</button>
                </div>
            </div>
        </form>
    </div>
</div>