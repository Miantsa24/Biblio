<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<!DOCTYPE html>
<html lang="fr">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>Choisir le type de connexion</title>
    <link href="https://cdn.jsdelivr.net/npm/bootstrap@5.3.3/dist/css/bootstrap.min.css" rel="stylesheet">
    <link href="https://cdn.jsdelivr.net/npm/bootstrap-icons@1.10.5/font/bootstrap-icons.css" rel="stylesheet">
    <link href="${pageContext.request.contextPath}/css/styles.css" rel="stylesheet">
</head>
<body>
    <header class="header d-flex align-items-center justify-content-center">
        <div class="logo">
            <h1>Bibliothèque</h1>
        </div>
    </header>
    <main class="container mt-5">
        <div class="row justify-content-center">
            <div class="col-md-6">
                <div class="card">
                    <div class="card-body text-center">
                        <h2 class="card-title mb-4">Choisir votre type de connexion</h2>
                        <div class="d-grid gap-3">
                            <a href="${pageContext.request.contextPath}/admin/login" class="btn btn-primary">
                                <i class="bi bi-shield-lock me-2"></i>Connexion Administrateur
                            </a>
                            <a href="${pageContext.request.contextPath}/adherant/login" class="btn btn-success">
                                <i class="bi bi-person me-2"></i>Connexion Adhérant
                            </a>
                        </div>
                    </div>
                </div>
            </div>
        </div>
    </main>
    <script src="https://cdn.jsdelivr.net/npm/bootstrap@5.3.3/dist/js/bootstrap.bundle.min.js"></script>
</body>
</html>