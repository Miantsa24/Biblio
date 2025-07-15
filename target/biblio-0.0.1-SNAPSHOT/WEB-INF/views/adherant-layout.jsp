<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<!DOCTYPE html>
<html lang="fr">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>${pageTitle}</title>
    <link href="https://cdn.jsdelivr.net/npm/bootstrap@5.3.3/dist/css/bootstrap.min.css" rel="stylesheet">
    <link href="https://cdn.jsdelivr.net/npm/bootstrap-icons@1.10.5/font/bootstrap-icons.css" rel="stylesheet">
    <link href="${pageContext.request.contextPath}/css/styles.css" rel="stylesheet">
</head>
<body>
    <header class="header d-flex align-items-center">
        <i class="bi bi-list toggle-sidebar-btn"></i>
        <div class="logo">
            <h1>Bibliothèque</h1>
        </div>
    </header>
    <aside class="sidebar">
        <ul class="sidebar-nav">
            <li class="nav-item">
                <a class="nav-link" href="${pageContext.request.contextPath}/adherant/index">
                    <i class="bi bi-house ici"></i>
                    <span>Accueil</span>
                </a>
            </li>
            <li class="nav-item">
                <a class="nav-link" href="${pageContext.request.contextPath}/adherant/livres">
                    <i class="bi bi-book ici"></i>
                    <span>Liste des livres</span>
                </a>
            </li>
            <li class="nav-item">
                <a class="nav-link" href="${pageContext.request.contextPath}/adherant/prets">
                    <i class="bi bi-list-check ici"></i>
                    <span>Prêts en cours</span>
                </a>
            </li>
            <li class="nav-item">
                <a class="nav-link" href="${pageContext.request.contextPath}/adherant/reservations">
                    <i class="bi bi-bookmark-check ici"></i>
                    <span>Réservations</span>
                </a>
            </li>
            <li class="nav-item">
                <a class="nav-link" href="${pageContext.request.contextPath}/adherant/liste-reservations">
                    <i class="bi bi-list-ul ici"></i>
                    <span>Liste des réservations</span>
                </a>
            </li>
            <li class="nav-item">
                <a class="nav-link" href="${pageContext.request.contextPath}/adherant/logout">
                    <i class="bi bi-box-arrow-right ici"></i>
                    <span>Déconnexion</span>
                </a>
            </li>
        </ul>
    </aside>
    <main id="main">
        <!-- Contenu spécifique inséré par chaque JSP -->
    </main>
    <script src="https://cdn.jsdelivr.net/npm/bootstrap@5.3.3/dist/js/bootstrap.bundle.min.js"></script>
    <script>
        document.addEventListener('DOMContentLoaded', function() {
            const toggleBtn = document.querySelector('.toggle-sidebar-btn');
            const body = document.body;
            
            toggleBtn.addEventListener('click', function() {
                body.classList.toggle('toggle-sidebar');
                if (body.classList.contains('toggle-sidebar')) {
                    localStorage.setItem('sidebarState', 'collapsed');
                } else {
                    localStorage.setItem('sidebarState', 'expanded');
                }
            });

            if (localStorage.getItem('sidebarState') === 'collapsed') {
                body.classList.add('toggle-sidebar');
            }
        });
    </script>
</body>
</html>