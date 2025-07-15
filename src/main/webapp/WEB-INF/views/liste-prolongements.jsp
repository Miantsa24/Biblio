<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<c:set var="pageTitle" value="Prolongements en attente" scope="request"/>
<jsp:include page="/WEB-INF/views/layout.jsp"/>
<div class="card">
    <div class="card-body">
        <h2 class="card-title">Prolongements en attente</h2>

        <c:if test="${not empty errorMessage}">
            <div class="alert alert-danger">${errorMessage}</div>
        </c:if>
        <c:if test="${param.success == 'approuver'}">
            <div class="alert alert-success">Prolongement approuvé avec succès !</div>
        </c:if>
        <c:if test="${param.success == 'refuser'}">
            <div class="alert alert-success">Prolongement refusé avec succès !</div>
        </c:if>

        <div class="table-responsive">
            <table class="table table-hover">
                <thead>
                    <tr>
                        <th>ID Prolongement</th>
                        <th>Nom de l'adhérant</th>
                        <th>Titre du livre</th>
                        <th>Exemplaire</th>
                        <th>Date de la demande</th>
                        <th>Nouvelle date de retour</th>
                        <th>Actions</th>
                    </tr>
                </thead>
                <tbody>
                    <c:forEach var="prolongement" items="${prolongementsEnAttente}">
                        <tr>
                            <td>${prolongement.id}</td>
                            <td>
                                <c:set var="adherant" value="${adherantRepository.findById(prolongement.adherant.idAdherant).orElse(null)}" />
                                ${adherant != null ? adherant.nom : 'Non trouvé'} ${adherant != null ? adherant.prenom : ''}
                            </td>
                            <td>
                                <c:set var="exemplaire" value="${exemplaireRepository.findById(prolongement.exemplaire.id).orElse(null)}" />
                                <c:set var="livre" value="${exemplaire != null ? livreRepository.findById(exemplaire.idLivre).orElse(null) : null}"/>
                                ${livre != null ? livre.titre : 'Non trouvé'}
                            </td>
                            <td>${prolongement.exemplaire.id}</td>
                            <td>${prolongement.dateDemande}</td>
                            <td>${prolongement.nouvelleDateRetourPrevue}</td>
                            <td>
                                <form action="/prolongements/approuver" method="post" style="display:inline;">
                                    <input type="hidden" name="idProlongement" value="${prolongement.id}">
                                    <button type="submit" class="btn btn-success btn-sm">Approuver</button>
                                </form>
                                <form action="/prolongements/refuser" method="post" style="display:inline;">
                                    <input type="hidden" name="idProlongement" value="${prolongement.id}">
                                    <button type="submit" class="btn btn-danger btn-sm">Refuser</button>
                                </form>
                            </td>
                        </tr>
                    </c:forEach>
                    <c:if test="${empty prolongementsEnAttente}">
                        <tr>
                            <td colspan="7" class="text-center">Aucune demande de prolongement en attente.</td>
                        </tr>
                    </c:if>
                </tbody>
            </table>
        </div>
    </div>
</div>