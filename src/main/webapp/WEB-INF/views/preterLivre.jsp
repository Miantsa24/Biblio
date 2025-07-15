<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<c:set var="pageTitle" value="Prêter un livre" scope="request"/>
<!-- <c:set var="contentPage" value="/WEB-INF/views/preterLivre.jsp" scope="request"/> -->
<jsp:include page="/WEB-INF/views/layout.jsp"/>
<div class="card">
    <div class="card-body">
        <h2 class="card-title">Prêter un livre</h2>

        <c:if test="${not empty errorMessage}">
            <p class="error-message">${errorMessage}</p>
        </c:if>
        <c:if test="${not empty successMessage}">
            <p class="success-message">${successMessage}</p>
        </c:if>
        <c:if test="${not empty sessionScope.penaltyMessage}">
            <p class="penalty-message">${sessionScope.penaltyMessage}</p>
            <c:remove var="penaltyMessage" scope="session"/>
        </c:if>

        <form action="/prets" method="post">
            <div class="row">
                <div class="col-md-6 mb-3">
                    <div class="form-floating">
                        <input type="number" id="idAdherant" name="idAdherant" class="form-control" placeholder="Référence de l'adhérant" required>
                        <label for="idAdherant">Référence de l'adhérant</label>
                    </div>
                </div>
                <div class="col-md-6 mb-3">
                    <div class="form-floating">
                        <select id="idLivre" name="idLivre" class="form-select" required>
                            <option value="">-- Sélectionner un livre --</option>
                            <c:forEach var="livre" items="${livres}">
                                <option value="${livre.id}">${livre.titre}</option>
                            </c:forEach>
                        </select>
                        <label for="idLivre">Livre</label>
                    </div>
                </div>
            </div>
            <div class="row">
                <div class="col-md-6 mb-3">
                    <div class="form-floating">
                        <select id="typePret" name="typePret" class="form-select" required>
                            <option value="LECTURE_SUR_PLACE">Lecture sur place</option>
                            <option value="A_EMPORTER">À emporter</option>
                        </select>
                        <label for="typePret">Type de prêt</label>
                    </div>
                </div>
                <div class="col-md-6 mb-3">
                    <div class="form-floating">
                        <input type="date" id="datePret" name="datePret" class="form-control" placeholder="Date du prêt" required>
                        <label for="datePret">Date du prêt</label>
                    </div>
                </div>
            </div>
            <div class="row">
                <div class="col-md-6">
                    <button type="submit" class="btn btn-primary w-100">Prêter</button>
                </div>
                <div class="col-md-6">
                    <button type="reset" class="btn btn-secondary w-100">Réinitialiser</button>
                </div>
            </div>
        </form>
    </div>
</div>