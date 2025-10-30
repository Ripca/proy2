<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ page import="com.sistemaempresa.models.MenuItem" %>
<%@ page import="com.sistemaempresa.models.Usuario" %>
<%@ page import="java.util.List" %>
<%@ page import="com.sistemaempresa.dao.MenuItemDAO" %>

<%
    // Obtener el usuario de la sesión
    Usuario usuarioSesion = (Usuario) session.getAttribute("usuario");
    List<MenuItem> menuItems = new ArrayList<>();

    if (usuarioSesion != null) {
        // Obtener el menú estructurado filtrado por rol del usuario
        MenuItemDAO menuDAO = new MenuItemDAO();
        menuItems = menuDAO.obtenerMenuEstructuradoPorRol(usuarioSesion.getIdUsuario());
    }

    request.setAttribute("menuItems", menuItems);
%>

<!-- Menú Dinámico con Estructura de Árbol -->
<nav class="navbar navbar-expand-lg navbar-dark bg-primary">
    <div class="container-fluid">
        <a class="navbar-brand" href="DashboardServlet">
            <i class="fas fa-building"></i> Sistema Empresa
        </a>
        
        <button class="navbar-toggler" type="button" data-bs-toggle="collapse" data-bs-target="#navbarNav">
            <span class="navbar-toggler-icon"></span>
        </button>
        
        <div class="collapse navbar-collapse" id="navbarNav">
            <ul class="navbar-nav me-auto">
                <%
                    if (menuItems != null) {
                        for (MenuItem item : menuItems) {
                %>
                    <li class="nav-item dropdown">
                        <% if (item.tieneHijos()) { %>
                            <!-- Elemento padre con submenú -->
                            <a class="nav-link dropdown-toggle" href="#" role="button" 
                               data-bs-toggle="dropdown" aria-expanded="false">
                                <i class="<%= item.getIcono() %>"></i> <%= item.getTitulo() %>
                            </a>
                            <ul class="dropdown-menu">
                                <%
                                    for (MenuItem hijo : item.getHijos()) {
                                %>
                                    <% if (hijo.tieneHijos()) { %>
                                        <!-- Submenú de segundo nivel -->
                                        <li class="dropdown-submenu">
                                            <a class="dropdown-item dropdown-toggle" href="#">
                                                <i class="<%= hijo.getIcono() %>"></i> <%= hijo.getTitulo() %>
                                            </a>
                                            <ul class="dropdown-menu">
                                                <%
                                                    for (MenuItem nieto : hijo.getHijos()) {
                                                %>
                                                    <li>
                                                        <a class="dropdown-item" href="<%= nieto.getUrl() %>">
                                                            <i class="<%= nieto.getIcono() %>"></i> <%= nieto.getTitulo() %>
                                                        </a>
                                                    </li>
                                                <%
                                                    }
                                                %>
                                            </ul>
                                        </li>
                                    <% } else { %>
                                        <!-- Elemento hijo simple -->
                                        <li>
                                            <a class="dropdown-item" href="<%= hijo.getUrl() %>">
                                                <i class="<%= hijo.getIcono() %>"></i> <%= hijo.getTitulo() %>
                                            </a>
                                        </li>
                                    <% } %>
                                <%
                                    }
                                %>
                            </ul>
                        <% } else { %>
                            <!-- Elemento simple sin hijos -->
                            <a class="nav-link" href="<%= item.getUrl() %>">
                                <i class="<%= item.getIcono() %>"></i> <%= item.getTitulo() %>
                            </a>
                        <% } %>
                    </li>
                <%
                        }
                    }
                %>
            </ul>
            
            <!-- Menú de usuario -->
            <ul class="navbar-nav">
                <li class="nav-item dropdown">
                    <a class="nav-link dropdown-toggle" href="#" role="button" 
                       data-bs-toggle="dropdown" aria-expanded="false">
                        <i class="fas fa-user"></i> 
                        <%= session.getAttribute("nombreUsuario") != null ? 
                            session.getAttribute("nombreUsuario") : "Usuario" %>
                    </a>
                    <ul class="dropdown-menu dropdown-menu-end">
                        <li><a class="dropdown-item" href="UsuarioServlet?action=profile">
                            <i class="fas fa-user-cog"></i> Mi Perfil
                        </a></li>
                        <li><a class="dropdown-item" href="UsuarioServlet">
                            <i class="fas fa-users-cog"></i> Gestión de Usuarios
                        </a></li>
                        <li><hr class="dropdown-divider"></li>
                        <li><a class="dropdown-item" href="LoginServlet?action=logout">
                            <i class="fas fa-sign-out-alt"></i> Cerrar Sesión
                        </a></li>
                    </ul>
                </li>
            </ul>
        </div>
    </div>
</nav>

<!-- CSS para submenús multinivel -->
<style>
.dropdown-submenu {
    position: relative;
}

.dropdown-submenu .dropdown-menu {
    top: 0;
    left: 100%;
    margin-top: -1px;
}

.dropdown-submenu:hover .dropdown-menu {
    display: block;
}

.dropdown-item:hover {
    background-color: #f8f9fa;
}

.dropdown-toggle::after {
    border-top: 0.3em solid;
    border-right: 0.3em solid transparent;
    border-bottom: 0;
    border-left: 0.3em solid transparent;
}

.dropdown-submenu .dropdown-toggle::after {
    border-top: 0.3em solid transparent;
    border-right: 0;
    border-bottom: 0.3em solid transparent;
    border-left: 0.3em solid;
}
</style>

<!-- JavaScript para manejar submenús -->
<script>
document.addEventListener('DOMContentLoaded', function() {
    // Manejar submenús en dispositivos móviles
    const dropdownSubmenus = document.querySelectorAll('.dropdown-submenu');
    
    dropdownSubmenus.forEach(function(submenu) {
        submenu.addEventListener('click', function(e) {
            e.stopPropagation();
            
            const dropdownMenu = this.querySelector('.dropdown-menu');
            if (dropdownMenu) {
                dropdownMenu.style.display = 
                    dropdownMenu.style.display === 'block' ? 'none' : 'block';
            }
        });
    });
    
    // Cerrar submenús al hacer clic fuera
    document.addEventListener('click', function() {
        dropdownSubmenus.forEach(function(submenu) {
            const dropdownMenu = submenu.querySelector('.dropdown-menu');
            if (dropdownMenu) {
                dropdownMenu.style.display = 'none';
            }
        });
    });
});
</script>
