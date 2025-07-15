
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<c:set var="pageTitle" value="Ajouter un adhérant" scope="request"/>
<jsp:include page="/WEB-INF/views/layout.jsp"/>
<div class="card">
    <div class="card-body">
        <h2 class="card-title">Ajouter un nouvel adhérant</h2>
        
        <c:if test="${not empty errorMessage}">
            <div class="alert alert-danger">${errorMessage}</div>
        </c:if>
        <c:if test="${not empty successMessage}">
            <div class="alert alert-success">${successMessage}</div>
        </c:if>
        
        <form action="/ajouter" method="post">
            <div class="row">
                <div class="col-md-6 mb-3">
                    <div class="form-floating">
                        <input type="text" name="nom" class="form-control" id="nom" placeholder="Nom" required>
                        <label for="nom">Nom</label>
                    </div>
                </div>
                <div class="col-md-6 mb-3">
                    <div class="form-floating">
                        <input type="text" name="prenom" class="form-control" id="prenom" placeholder="Prénom" required>
                        <label for="prenom">Prénom</label>
                    </div>
                </div>
            </div>
            <div class="row">
                <div class="col-md-6 mb-3">
                    <div class="form-floating">
                        <input type="date" name="dateNaissance" class="form-control" id="dateNaissance" placeholder="Date de naissance" required>
                        <label for="dateNaissance">Date de naissance</label>
                    </div>
                </div>
                <div class="col-md-6 mb-3">
                    <div class="form-floating">
                        <input type="email" name="email" class="form-control" id="email" placeholder="Email" required>
                        <label for="email">Email</label>
                    </div>
                </div>
            </div>
            <div class="row">
                <div class="col-md-6 mb-3">
                    <div class="form-floating">
                        <input type="password" name="motDePasse" class="form-control" id="motDePasse" placeholder="Mot de passe" required>
                        <label for="motDePasse">Mot de passe</label>
                    </div>
                </div>
                <div class="col-md-6 mb-3">
                    <div class="form-floating">
                        <select name="nomTypeAdherant" id="typeAdherant" class="form-select" required>
                            <option value="" disabled selected>Sélectionnez un type</option>
                            <c:forEach var="type" items="${typesAdherant}">
                                <option value="${type.nomType}">${type.nomType}</option>
                            </c:forEach>
                        </select>
                        <label for="typeAdherant">Type d'adhérant</label>
                        <c:if test="${empty typesAdherant}">
                            <div class="text-danger mt-2">Aucun type d'adhérant disponible. Veuillez contacter l'administrateur.</div>
                        </c:if>
                    </div>
                </div>
            </div>
            <div class="mb-3">
                <div class="form-floating">
                    <input type="date" name="dateDebutAbonnement" value="${now}" class="form-control" id="dateDebutAbonnement" placeholder="Date de début d'abonnement" required>
                    <label for="dateDebutAbonnement">Date de début d'abonnement</label>
                </div>
            </div>
            <div class="row">
                <div class="col-md-6">
                    <button type="submit" class="btn btn-primary w-100">Ajouter</button>
                </div>
                <div class="col-md-6">
                    <button type="reset" class="btn btn-secondary w-100">Réinitialiser</button>
                </div>
            </div>
        </form>
    </div>
</div>
