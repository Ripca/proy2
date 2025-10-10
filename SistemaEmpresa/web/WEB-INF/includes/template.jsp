<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ page import="java.util.List" %>
<%@ page import="com.sistemaempresa.models.Menu" %>

<%
    // Obtener menús del request o cargarlos si no están disponibles
    List<Menu> menusJerarquicos = (List<Menu>) request.getAttribute("menusJerarquicos");
    String pageTitle = (String) request.getAttribute("pageTitle");
    if (pageTitle == null) pageTitle = "Sistema Empresa";
    
    String pageIcon = (String) request.getAttribute("pageIcon");
    if (pageIcon == null) pageIcon = "fas fa-home";
%>

<!DOCTYPE html>
<html lang="es">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title><%= pageTitle %> - Sistema Empresa</title>
    
    <!-- Bootstrap 5 -->
    <link href="https://cdn.jsdelivr.net/npm/bootstrap@5.3.0/dist/css/bootstrap.min.css" rel="stylesheet">
    <!-- Font Awesome 6 -->
    <link href="https://cdnjs.cloudflare.com/ajax/libs/font-awesome/6.0.0/css/all.min.css" rel="stylesheet">
    <!-- DataTables -->
    <link href="https://cdn.datatables.net/1.13.6/css/dataTables.bootstrap5.min.css" rel="stylesheet">
    <!-- SweetAlert2 -->
    <link href="https://cdn.jsdelivr.net/npm/sweetalert2@11/dist/sweetalert2.min.css" rel="stylesheet">
    
    <style>
        body {
            font-family: 'Segoe UI', Tahoma, Geneva, Verdana, sans-serif;
            background-color: #f8f9fa;
        }
        
        #sidebar {
            min-width: 60px;
            max-width: 250px;
            width: 250px;
            background: linear-gradient(180deg, #343a40 0%, #495057 100%);
            color: #fff;
            transition: all 0.3s ease;
            min-height: 100vh;
            overflow-y: auto;
            position: fixed;
            z-index: 1000;
            box-shadow: 2px 0 5px rgba(0,0,0,0.1);
        }
        
        #sidebar.collapsed {
            width: 60px;
            min-width: 60px;
        }
        
        #sidebar.collapsed .sidebar-header h3,
        #sidebar.collapsed .sidebar-header small,
        #sidebar.collapsed ul li a span {
            display: none;
        }
        
        .sidebar-header {
            padding: 1rem;
            background: rgba(0,0,0,0.1);
            border-bottom: 1px solid rgba(255,255,255,0.1);
        }
        
        .sidebar-header h3 {
            margin: 0;
            font-size: 1.1rem;
            font-weight: 600;
        }
        
        #sidebar ul {
            padding: 0;
            list-style: none;
            margin: 0;
        }
        
        #sidebar ul li {
            border-bottom: 1px solid rgba(255,255,255,0.05);
        }
        
        #sidebar ul li a {
            padding: 0.75rem 1rem;
            color: #adb5bd;
            display: block;
            text-decoration: none;
            transition: all 0.3s;
        }
        
        #sidebar ul li a:hover {
            background: rgba(255,255,255,0.1);
            color: #fff;
        }
        
        #sidebar ul li a.active {
            background: rgba(0,123,255,0.2);
            color: #fff;
            border-left: 3px solid #007bff;
        }
        
        #sidebar ul li a i {
            margin-right: 0.5rem;
            width: 20px;
            text-align: center;
        }
        
        #content {
            margin-left: 250px;
            padding: 0;
            min-height: 100vh;
            transition: all 0.3s ease;
        }
        
        #content.expanded {
            margin-left: 60px;
        }
        
        .top-navbar {
            background: #fff;
            padding: 0.75rem 1.5rem;
            border-bottom: 1px solid #dee2e6;
            position: sticky;
            top: 0;
            z-index: 999;
            box-shadow: 0 2px 4px rgba(0,0,0,0.1);
        }
        
        .main-content {
            padding: 1.5rem;
        }
        
        .card {
            border: none;
            box-shadow: 0 0.125rem 0.25rem rgba(0,0,0,0.075);
            border-radius: 0.5rem;
        }
        
        .card-header {
            background: linear-gradient(135deg, #007bff 0%, #0056b3 100%);
            color: white;
            border-radius: 0.5rem 0.5rem 0 0 !important;
            border: none;
        }
        
        .btn {
            border-radius: 0.375rem;
        }
        
        .table {
            margin-bottom: 0;
        }
        
        .page-header {
            margin-bottom: 1.5rem;
            padding-bottom: 1rem;
            border-bottom: 1px solid #dee2e6;
        }
        
        .page-header h1 {
            margin: 0;
            font-size: 1.75rem;
            font-weight: 600;
            color: #495057;
        }
        
        /* Responsive */
        @media (max-width: 768px) {
            #sidebar {
                margin-left: -250px;
            }
            
            #sidebar.show {
                margin-left: 0;
            }
            
            #content {
                margin-left: 0;
            }
            
            #content.expanded {
                margin-left: 0;
            }
        }
    </style>
</head>
<body>
    <!-- Sidebar -->
    <nav id="sidebar">
        <div class="sidebar-header">
            <button type="button" id="sidebarToggle" class="btn btn-link text-white p-0 border-0">
                <i class="fas fa-bars"></i>
            </button>
            <h3 class="d-inline-block ms-2">
                <i class="fas fa-building"></i> 
                <span>Sistema Empresa</span>
            </h3>
        </div>
        
        <ul class="list-unstyled">
            <%
                try {
                    if (menusJerarquicos != null && !menusJerarquicos.isEmpty()) {
                        for (Menu menu : menusJerarquicos) {
                            if (menu.esMenuPrincipal()) { // Menú principal
            %>
            <li>
                <a href="<%= menu.getUrl() %>">
                    <i class="<%= menu.getIcono() %>"></i>
                    <span><%= menu.getNombre() %></span>
                </a>
            </li>
            <%
                            }
                        }
                    } else {
                        // Menús por defecto si no hay en la base de datos
            %>
            <li><a href="DashboardServlet"><i class="fas fa-tachometer-alt"></i> <span>Dashboard</span></a></li>
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
                } catch (Exception e) {
                    // En caso de error, mostrar menús por defecto
            %>
            <li><a href="DashboardServlet"><i class="fas fa-tachometer-alt"></i> <span>Dashboard</span></a></li>
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
    </nav>

    <!-- Contenido Principal -->
    <div id="content">
        <!-- Header superior -->
        <div class="top-navbar">
            <div class="d-flex justify-content-between align-items-center">
                <div class="page-title">
                    <h5 class="mb-0">
                        <i class="<%= pageIcon %>"></i> <%= pageTitle %>
                    </h5>
                </div>
                <div class="d-flex align-items-center">
                    <span class="me-3 text-muted">
                        <i class="fas fa-user"></i> <%= session.getAttribute("nombreCompleto") %>
                    </span>
                    <a href="LoginServlet?action=logout" class="btn btn-outline-danger btn-sm" title="Cerrar Sesión">
                        <i class="fas fa-sign-out-alt"></i>
                    </a>
                </div>
            </div>
        </div>
        
        <!-- Contenido de la página -->
        <div class="main-content">
            <!-- Aquí va el contenido específico de cada página -->
            <%
                String contentPage = request.getParameter("contentPage");
                if (contentPage != null) {
            %>
                <jsp:include page="<%= contentPage %>" />
            <%
                }
            %>
        </div>
    </div>

    <!-- Scripts -->
    <script src="https://cdn.jsdelivr.net/npm/bootstrap@5.3.0/dist/js/bootstrap.bundle.min.js"></script>
    <script src="https://code.jquery.com/jquery-3.7.1.min.js"></script>
    <script src="https://cdn.datatables.net/1.13.6/js/jquery.dataTables.min.js"></script>
    <script src="https://cdn.datatables.net/1.13.6/js/dataTables.bootstrap5.min.js"></script>
    <script src="https://cdn.jsdelivr.net/npm/sweetalert2@11"></script>
    
    <script>
        // Toggle sidebar
        document.getElementById('sidebarToggle').addEventListener('click', function () {
            const sidebar = document.getElementById('sidebar');
            const content = document.getElementById('content');
            
            sidebar.classList.toggle('collapsed');
            content.classList.toggle('expanded');
        });
        
        // Inicializar DataTables
        $(document).ready(function() {
            $('.data-table').DataTable({
                language: {
                    url: 'https://cdn.datatables.net/plug-ins/1.13.6/i18n/es-ES.json'
                },
                responsive: true,
                pageLength: 10,
                order: [[0, 'desc']]
            });
        });
        
        // Función para mostrar alertas con SweetAlert2
        function showAlert(type, title, message) {
            Swal.fire({
                icon: type,
                title: title,
                text: message,
                confirmButtonColor: '#007bff'
            });
        }
        
        // Función para confirmar eliminación
        function confirmDelete(url, message = '¿Está seguro que desea eliminar este registro?') {
            Swal.fire({
                title: 'Confirmar Eliminación',
                text: message,
                icon: 'warning',
                showCancelButton: true,
                confirmButtonColor: '#dc3545',
                cancelButtonColor: '#6c757d',
                confirmButtonText: 'Sí, eliminar',
                cancelButtonText: 'Cancelar'
            }).then((result) => {
                if (result.isConfirmed) {
                    window.location.href = url;
                }
            });
        }
        
        // Mostrar alertas basadas en parámetros URL
        const urlParams = new URLSearchParams(window.location.search);
        const success = urlParams.get('success');
        const error = urlParams.get('error');
        
        if (success) {
            showAlert('success', '¡Éxito!', success);
        }
        if (error) {
            showAlert('error', 'Error', error);
        }
    </script>
</body>
</html>
