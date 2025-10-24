<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ page import="com.sistemaempresa.models.Menu" %>
<%@ page import="java.util.List" %>
<%@ page import="com.sistemaempresa.dao.MenuDAO" %>

<%
    // Obtener el menú estructurado jerárquico
    MenuDAO menuDAO = new MenuDAO();
    List<Menu> menusJerarquicos = menuDAO.obtenerMenusJerarquicos();
%>

<!-- Menú Dashboard siempre visible -->
<ul class="list-unstyled">
    <li>
        <a href="DashboardServlet">
            <i class="fas fa-tachometer-alt"></i>
            <span>Dashboard</span>
        </a>
    </li>

    <%
        if (menusJerarquicos != null && !menusJerarquicos.isEmpty()) {
            for (Menu menu : menusJerarquicos) {
    %>
        <li class="menu-item">
            <% if (menu.tieneSubmenus()) { %>
                <!-- Elemento padre con submenú -->
                <a href="#submenu<%= menu.getIdMenu() %>" data-bs-toggle="collapse"
                   class="menu-link collapsed" aria-expanded="false">
                    <i class="<%= menu.getIcono() %>"></i>
                    <span><%= menu.getNombre() %></span>
                    <i class="fas fa-chevron-down ms-auto"></i>
                </a>

                <div class="collapse" id="submenu<%= menu.getIdMenu() %>">
                    <ul class="list-unstyled submenu">
                        <%
                            for (Menu submenu : menu.getSubmenus()) {
                        %>
                            <li>
                                <% if (submenu.tieneSubmenus()) { %>
                                    <!-- Submenú de segundo nivel con más hijos -->
                                    <a href="#submenu<%= submenu.getIdMenu() %>" data-bs-toggle="collapse"
                                       class="submenu-link collapsed" aria-expanded="false">
                                        <i class="<%= submenu.getIcono() %>"></i>
                                        <span><%= submenu.getNombre() %></span>
                                        <i class="fas fa-chevron-down ms-auto"></i>
                                    </a>

                                    <div class="collapse" id="submenu<%= submenu.getIdMenu() %>">
                                        <ul class="list-unstyled sub-submenu">
                                            <%
                                                for (Menu subsubmenu : submenu.getSubmenus()) {
                                            %>
                                                <li>
                                                    <% if (subsubmenu.tieneSubmenus()) { %>
                                                        <!-- Tercer nivel con más hijos -->
                                                        <a href="#submenu<%= subsubmenu.getIdMenu() %>" data-bs-toggle="collapse"
                                                           class="sub-submenu-link collapsed" aria-expanded="false">
                                                            <i class="<%= subsubmenu.getIcono() %>"></i>
                                                            <span><%= subsubmenu.getNombre() %></span>
                                                            <i class="fas fa-chevron-down ms-auto"></i>
                                                        </a>

                                                        <div class="collapse" id="submenu<%= subsubmenu.getIdMenu() %>">
                                                            <ul class="list-unstyled sub-sub-submenu">
                                                                <%
                                                                    for (Menu item : subsubmenu.getSubmenus()) {
                                                                %>
                                                                    <li>
                                                                        <a href="<%= item.getUrl() %>" class="sub-sub-submenu-link">
                                                                            <i class="<%= item.getIcono() %>"></i>
                                                                            <span><%= item.getNombre() %></span>
                                                                        </a>
                                                                    </li>
                                                                <%
                                                                    }
                                                                %>
                                                            </ul>
                                                        </div>
                                                    <% } else { %>
                                                        <!-- Elemento hoja de tercer nivel -->
                                                        <a href="<%= subsubmenu.getUrl() %>" class="sub-submenu-link">
                                                            <i class="<%= subsubmenu.getIcono() %>"></i>
                                                            <span><%= subsubmenu.getNombre() %></span>
                                                        </a>
                                                    <% } %>
                                                </li>
                                            <%
                                                }
                                            %>
                                        </ul>
                                    </div>
                                <% } else { %>
                                    <!-- Elemento hijo simple de segundo nivel -->
                                    <a href="<%= submenu.getUrl() %>" class="submenu-link">
                                        <i class="<%= submenu.getIcono() %>"></i>
                                        <span><%= submenu.getNombre() %></span>
                                    </a>
                                <% } %>
                            </li>
                        <%
                            }
                        %>
                    </ul>
                </div>
            <% } else { %>
                <!-- Elemento simple sin hijos -->
                <a href="<%= menu.getUrl() %>" class="menu-link">
                    <i class="<%= menu.getIcono() %>"></i>
                    <span><%= menu.getNombre() %></span>
                </a>
            <% } %>
        </li>
    <%
            }
        } else {
            // Menús por defecto si no hay en la base de datos
    %>
        <li><a href="ClienteServlet"><i class="fas fa-users"></i> <span>Clientes</span></a></li>
        <li><a href="EmpleadoServlet"><i class="fas fa-user-tie"></i> <span>Empleados</span></a></li>
        <li><a href="PuestoServlet"><i class="fas fa-briefcase"></i> <span>Puestos</span></a></li>
        <li><a href="ProductoServlet"><i class="fas fa-box"></i> <span>Productos</span></a></li>
        <li><a href="MarcaServlet"><i class="fas fa-tags"></i> <span>Marcas</span></a></li>
        <li><a href="ProveedorServlet"><i class="fas fa-truck"></i> <span>Proveedores</span></a></li>
        <li><a href="VentaServlet"><i class="fas fa-shopping-cart"></i> <span>Ventas</span></a></li>
        <li><a href="CompraServlet"><i class="fas fa-shopping-bag"></i> <span>Compras</span></a></li>
    <%
        }
    %>
</ul>

<!-- CSS específico para el menú del sidebar con soporte multinivel -->
<style>
.menu-item {
    margin-bottom: 0.25rem;
}

.menu-link, .submenu-link, .sub-submenu-link, .sub-sub-submenu-link {
    display: flex;
    align-items: center;
    padding: 0.75rem 1rem;
    color: #adb5bd;
    text-decoration: none;
    border-radius: 0.375rem;
    transition: all 0.3s ease;
    margin-bottom: 0.125rem;
}

.menu-link:hover, .submenu-link:hover, .sub-submenu-link:hover, .sub-sub-submenu-link:hover {
    color: #fff;
    background-color: rgba(255, 255, 255, 0.1);
    text-decoration: none;
}

.menu-link.active, .submenu-link.active, .sub-submenu-link.active, .sub-sub-submenu-link.active {
    color: #fff;
    background-color: #007bff;
}

.submenu {
    padding-left: 1rem;
    background-color: rgba(0, 0, 0, 0.1);
    border-radius: 0.375rem;
    margin-top: 0.25rem;
}

.submenu-link {
    padding: 0.5rem 1rem;
    font-size: 0.9rem;
}

.sub-submenu {
    padding-left: 1rem;
    background-color: rgba(0, 0, 0, 0.15);
    border-radius: 0.375rem;
    margin-top: 0.25rem;
}

.sub-submenu-link {
    padding: 0.4rem 1rem;
    font-size: 0.85rem;
}

.sub-sub-submenu {
    padding-left: 1rem;
    background-color: rgba(0, 0, 0, 0.2);
    border-radius: 0.375rem;
    margin-top: 0.25rem;
}

.sub-sub-submenu-link {
    padding: 0.35rem 1rem;
    font-size: 0.8rem;
}

.menu-link i, .submenu-link i, .sub-submenu-link i, .sub-sub-submenu-link i {
    width: 1.25rem;
    margin-right: 0.75rem;
    text-align: center;
}

.fa-chevron-down {
    transition: transform 0.3s ease;
    font-size: 0.75rem;
}

.menu-link[aria-expanded="true"] .fa-chevron-down,
.submenu-link[aria-expanded="true"] .fa-chevron-down,
.sub-submenu-link[aria-expanded="true"] .fa-chevron-down {
    transform: rotate(180deg);
}

/* Responsive */
@media (max-width: 768px) {
    .menu-link, .submenu-link, .sub-submenu-link, .sub-sub-submenu-link {
        padding: 0.5rem;
    }

    .submenu, .sub-submenu, .sub-sub-submenu {
        padding-left: 0.5rem;
    }
}
</style>

<!-- JavaScript para manejar el estado activo del menú en estructura multinivel -->
<script>
document.addEventListener('DOMContentLoaded', function() {
    // Obtener la URL actual
    const currentPath = window.location.pathname;
    const currentSearch = window.location.search;
    const currentUrl = currentPath + currentSearch;

    // Buscar el enlace activo en todos los niveles
    const menuLinks = document.querySelectorAll('.menu-link, .submenu-link, .sub-submenu-link, .sub-sub-submenu-link');

    menuLinks.forEach(function(link) {
        const href = link.getAttribute('href');
        if (href && href !== '#' && (currentUrl.includes(href) || currentPath.includes(href))) {
            link.classList.add('active');

            // Expandir todos los menús padres recursivamente
            let parent = link.closest('.collapse');
            while (parent) {
                parent.classList.add('show');
                const toggle = document.querySelector(`[href="#${parent.id}"]`);
                if (toggle) {
                    toggle.classList.remove('collapsed');
                    toggle.setAttribute('aria-expanded', 'true');
                }
                parent = parent.parentElement.closest('.collapse');
            }
        }
    });
});
</script>
