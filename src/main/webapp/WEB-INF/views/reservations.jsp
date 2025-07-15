<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<c:set var="pageTitle" value="Faire une réservation" scope="request"/>
<jsp:include page="/WEB-INF/views/adherant-layout.jsp"/>
<style>
main .card {
  background: linear-gradient(145deg, #ffffff, #f6f9ff);
  border: none;
  border-radius: 12px;
  box-shadow: 0 6px 12px rgba(0, 0, 0, 0.15);
  padding: 20px;
  max-width: 800px;
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

main .form-floating label {
  color: #4A6FA5;
  font-size: 14px;
}

main .form-control[readonly] {
  background-color: #e9ecef;
}

main .btn-primary {
  background-color: #4A6FA5;
  border-color: #4A6FA5;
  font-size: 14px;
  padding: 10px;
  border-radius: 8px;
  transition: background-color 0.3s ease, transform 0.2s ease;
}

main .btn-primary:hover {
  background-color: #3745c8;
  border-color: #3745c8;
  transform: scale(1.05);
}

main .btn-secondary {
  background-color: #6c757d;
  border-color: #6c757d;
  font-size: 14px;
  padding: 10px;
  border-radius: 8px;
}

main .btn-secondary:hover {
  background-color: #5a6268;
  border-color: #5a6268;
}

main .alert-success {
  background-color: #d4edda;
  color: #155724;
  border-radius: 8px;
  margin-bottom: 20px;
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
            <h2 class="card-title">Faire une réservation</h2>

            <c:if test="${not empty errorMessage}">
                <div class="alert alert-danger">${errorMessage}</div>
            </c:if>
            <c:if test="${not empty successMessage}">
                <div class="alert alert-success">${successMessage}</div>
            </c:if>

            <form action="/adherant/reservations" method="post">
                <div class="row">
                    <div class="col-md-6 mb-3">
                        <div class="form-floating">
                            <input type="number" id="idAdherant" name="idAdherant" class="form-control" value="${sessionScope.adherant.idAdherant}" readonly required>
                            <label for="idAdherant">Référence de l'adhérant</label>
                        </div>
                    </div>
                    <div class="col-md-6 mb-3">
                        <div class="form-floating">
                            <input type="number" id="idExemplaire" name="idExemplaire" class="form-control" value="${param.idExemplaire}" placeholder="Référence de l'exemplaire" required>
                            <label for="idExemplaire">Référence de l'exemplaire</label>
                        </div>
                    </div>
                </div>
                <div class="row">
                    <div class="col-md-6 mb-3">
                        <div class="form-floating">
                            <input type="date" id="dateReservation" name="dateReservation" class="form-control" placeholder="Date de réservation" required>
                            <label for="dateReservation">Date de réservation</label>
                        </div>
                    </div>
                </div>
                <div class="row">
                    <div class="col-md-6">
                        <button type="submit" class="btn btn-primary w-100">Réserver</button>
                    </div>
                    <div class="col-md-6">
                        <button type="reset" class="btn btn-secondary w-100">Réinitialiser</button>
                    </div>
                </div>
            </form>
        </div>
    </div>
</main>