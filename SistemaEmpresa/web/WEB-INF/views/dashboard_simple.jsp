<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@page import="java.util.List"%>
<%@page import="com.sistemaempresa.models.CarruselImagen"%>
<%@page import="com.sistemaempresa.models.Menu"%>
<!DOCTYPE html>
<html lang="es">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>Dashboard - Sistema Empresa</title>
    <link href="https://cdn.jsdelivr.net/npm/bootstrap@5.3.0/dist/css/bootstrap.min.css" rel="stylesheet">
    <link href="https://cdnjs.cloudflare.com/ajax/libs/font-awesome/6.4.0/css/all.min.css" rel="stylesheet">
    <style>
        .wrapper {
            display: flex;
            width: 100%;
            align-items: stretch;
        }

        #sidebar {
            min-width: 60px;
            max-width: 250px;
            width: 250px;
            background: #343a40;
            color: #fff;
            transition: all 0.3s;
            min-height: 100vh;
            overflow-y: auto;
            position: fixed;
            z-index: 1000;
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

        #sidebar.collapsed ul li a {
            text-align: center;
            padding: 10px;
        }

        #sidebar .sidebar-header {
            padding: 20px;
            background: #495057;
        }

        #sidebar ul.components {
            padding: 20px 0;
            border-bottom: 1px solid #47748b;
        }

        #sidebar ul p {
            color: #fff;
            padding: 10px;
        }

        #sidebar ul li a {
            padding: 10px 20px;
            font-size: 1.1em;
            display: block;
            color: #fff;
            text-decoration: none;
            transition: all 0.3s;
        }

        #sidebar ul li a:hover {
            color: #343a40;
            background: #fff;
        }

        #sidebar ul li.active > a {
            color: #fff;
            background: #6c757d;
        }

        #content {
            margin-left: 250px;
            padding: 0;
            min-height: 100vh;
            transition: all 0.3s;
        }

        #content.expanded {
            margin-left: 60px;
        }

        .top-navbar {
            background: #f8f9fa;
            padding: 1rem 1.5rem;
            border-bottom: 1px solid #dee2e6;
            position: sticky;
            top: 0;
            z-index: 999;
        }

        .top-navbar {
            background: #fff;
            border-bottom: 1px solid #dee2e6;
            padding: 10px 20px;
            position: sticky;
            top: 0;
            z-index: 999;
        }

        .card-stats {
            transition: transform 0.2s;
        }
        .card-stats:hover {
            transform: translateY(-5px);
        }

        .carousel-item img {
            height: 400px;
            object-fit: cover;
        }

        @media (max-width: 768px) {
            #sidebar {
                margin-left: -250px;
            }
            #sidebar.mobile-show {
                margin-left: 0;
            }
            #content {
                margin-left: 0;
            }
        }
    </style>
</head>
<body>
    <div class="wrapper">
        <!-- Sidebar -->
        <nav id="sidebar">
            <div class="sidebar-header">
                <button type="button" id="sidebarToggle" class="btn btn-link text-white p-2">
                    <i class="fas fa-bars"></i>
                </button>
                <h3 class="d-inline-block ms-2"><i class="fas fa-building"></i> <span>Sistema Empresa</span></h3>
            </div>

            <ul class="list-unstyled components">
                <%
                    List<Menu> menusJerarquicos = (List<Menu>) request.getAttribute("menusJerarquicos");
                    if (menusJerarquicos != null && !menusJerarquicos.isEmpty()) {
                        for (Menu menu : menusJerarquicos) {
                            if (menu.tieneSubmenus()) {
                %>
                <!-- Menú con submenús -->
                <li>
                    <a href="#<%= menu.getNombre().toLowerCase().replaceAll("\\s+", "") %>Submenu" data-bs-toggle="collapse" aria-expanded="false" class="dropdown-toggle">
                        <i class="<%= menu.getIcono() %>"></i> <span><%= menu.getNombre() %></span>
                    </a>
                    <ul class="collapse list-unstyled" id="<%= menu.getNombre().toLowerCase().replaceAll("\\s+", "") %>Submenu">
                        <%
                            for (Menu submenu : menu.getSubmenus()) {
                        %>
                        <li>
                            <a href="<%= submenu.getUrl() %>">
                                <i class="<%= submenu.getIcono() %>"></i> <span><%= submenu.getNombre() %></span>
                            </a>
                        </li>
                        <%
                            }
                        %>
                    </ul>
                </li>
                <%
                            } else {
                %>
                <!-- Menú simple -->
                <li>
                    <a href="<%= menu.getUrl() %>">
                        <i class="<%= menu.getIcono() %>"></i> <span><%= menu.getNombre() %></span>
                    </a>
                </li>
                <%
                            }
                        }
                    } else {
                %>
                <!-- Menús por defecto si no hay datos dinámicos -->
                <li><a href="DashboardServlet"><i class="fas fa-tachometer-alt"></i> <span>Dashboard</span></a></li>
                <li><a href="ClienteServlet"><i class="fas fa-users"></i> <span>Clientes</span></a></li>
                <li><a href="EmpleadoServlet"><i class="fas fa-user-tie"></i> <span>Empleados</span></a></li>
                <li><a href="PuestoServlet"><i class="fas fa-briefcase"></i> <span>Puestos</span></a></li>
                <li><a href="ProductoServlet"><i class="fas fa-box"></i> <span>Productos</span></a></li>
                <li><a href="MarcaServlet"><i class="fas fa-tags"></i> <span>Marcas</span></a></li>
                <li><a href="ProveedorServlet"><i class="fas fa-truck"></i> <span>Proveedores</span></a></li>
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
                    <div>
                        <!-- Espacio para breadcrumbs o título si es necesario -->
                    </div>
                    <div class="d-flex align-items-center">
                        <span class="me-3">
                            <i class="fas fa-user"></i> <%= session.getAttribute("nombreCompleto") %>
                        </span>
                        <a href="LoginServlet?action=logout" class="btn btn-outline-danger btn-sm">
                            <i class="fas fa-sign-out-alt"></i> Cerrar Sesión
                        </a>
                    </div>
                </div>
            </div>

            <!-- Contenido de la página -->
            <div class="p-4">

            <!-- Encabezado -->
            <div class="row mb-4">
                <div class="col-12">
                    <h1 class="h3 mb-0">
                        <i class="fas fa-tachometer-alt"></i> Dashboard
                    </h1>
                    <p class="text-muted">Bienvenido al Sistema de Gestión Empresarial</p>
                </div>
            </div>

            <!-- Tarjetas de Resumen -->
            <div class="row mb-4">
                <div class="col-xl-3 col-md-6 mb-4">
                    <div class="card border-start border-primary border-4 shadow h-100 py-2 card-stats">
                        <div class="card-body">
                            <div class="row no-gutters align-items-center">
                                <div class="col mr-2">
                                    <div class="text-xs fw-bold text-primary text-uppercase mb-1">
                                        Clientes
                                    </div>
                                    <div class="h5 mb-0 fw-bold text-gray-800"><%= request.getAttribute("totalClientes") != null ? request.getAttribute("totalClientes") : 0 %></div>
                                </div>
                                <div class="col-auto">
                                    <i class="fas fa-users fa-2x text-muted"></i>
                                </div>
                            </div>
                        </div>
                    </div>
                </div>

                <div class="col-xl-3 col-md-6 mb-4">
                    <div class="card border-start border-success border-4 shadow h-100 py-2 card-stats">
                        <div class="card-body">
                            <div class="row no-gutters align-items-center">
                                <div class="col mr-2">
                                    <div class="text-xs fw-bold text-success text-uppercase mb-1">
                                        Productos
                                    </div>
                                    <div class="h5 mb-0 fw-bold text-gray-800"><%= request.getAttribute("totalProductos") != null ? request.getAttribute("totalProductos") : 0 %></div>
                                </div>
                                <div class="col-auto">
                                    <i class="fas fa-box fa-2x text-muted"></i>
                                </div>
                            </div>
                        </div>
                    </div>
                </div>

                <div class="col-xl-3 col-md-6 mb-4">
                    <div class="card border-start border-info border-4 shadow h-100 py-2 card-stats">
                        <div class="card-body">
                            <div class="row no-gutters align-items-center">
                                <div class="col mr-2">
                                    <div class="text-xs fw-bold text-info text-uppercase mb-1">
                                        Empleados
                                    </div>
                                    <div class="h5 mb-0 fw-bold text-gray-800"><%= request.getAttribute("totalEmpleados") != null ? request.getAttribute("totalEmpleados") : 0 %></div>
                                </div>
                                <div class="col-auto">
                                    <i class="fas fa-user-tie fa-2x text-muted"></i>
                                </div>
                            </div>
                        </div>
                    </div>
                </div>

                <div class="col-xl-3 col-md-6 mb-4">
                    <div class="card border-start border-warning border-4 shadow h-100 py-2 card-stats">
                        <div class="card-body">
                            <div class="row no-gutters align-items-center">
                                <div class="col mr-2">
                                    <div class="text-xs fw-bold text-warning text-uppercase mb-1">
                                        Proveedores
                                    </div>
                                    <div class="h5 mb-0 fw-bold text-gray-800"><%= request.getAttribute("totalProveedores") != null ? request.getAttribute("totalProveedores") : 0 %></div>
                                </div>
                                <div class="col-auto">
                                    <i class="fas fa-truck fa-2x text-muted"></i>
                                </div>
                            </div>
                        </div>
                    </div>
                </div>
            </div>

            <!-- Accesos Rápidos -->
            <div class="row">
                <div class="col-12">
                    <div class="card shadow">
                        <div class="card-header py-3">
                            <h6 class="m-0 fw-bold text-primary">
                                <i class="fas fa-rocket"></i> Accesos Rápidos
                            </h6>
                        </div>
                        <div class="card-body">
                            <div class="row">
                                <div class="col-md-3 mb-3">
                                    <a href="ClienteServlet?action=new" class="btn btn-outline-primary w-100 py-3">
                                        <i class="fas fa-user-plus fa-2x d-block mb-2"></i>
                                        Nuevo Cliente
                                    </a>
                                </div>
                                <div class="col-md-3 mb-3">
                                    <a href="ProductoServlet?action=new" class="btn btn-outline-success w-100 py-3">
                                        <i class="fas fa-plus-square fa-2x d-block mb-2"></i>
                                        Nuevo Producto
                                    </a>
                                </div>
                                <div class="col-md-3 mb-3">
                                    <a href="EmpleadoServlet?action=new" class="btn btn-outline-info w-100 py-3">
                                        <i class="fas fa-user-tie fa-2x d-block mb-2"></i>
                                        Nuevo Empleado
                                    </a>
                                </div>
                                <div class="col-md-3 mb-3">
                                    <a href="ProveedorServlet?action=new" class="btn btn-outline-warning w-100 py-3">
                                        <i class="fas fa-truck fa-2x d-block mb-2"></i>
                                        Nuevo Proveedor
                                    </a>
                                </div>
                            </div>
                        </div>
                    </div>
                </div>
            </div>

            <!-- Carrusel de Imágenes -->
            <%
                List<CarruselImagen> imagenesCarrusel = (List<CarruselImagen>) request.getAttribute("imagenesCarrusel");
                if (imagenesCarrusel != null && !imagenesCarrusel.isEmpty()) {
            %>
            <div class="row mt-4">
                <div class="col-12">
                    <div id="carouselDashboard" class="carousel slide" data-bs-ride="carousel">
                        <div class="carousel-indicators">
                            <%
                                for (int i = 0; i < imagenesCarrusel.size(); i++) {
                            %>
                            <button type="button" data-bs-target="#carouselDashboard" data-bs-slide-to="<%= i %>"
                                    <%= i == 0 ? "class=\"active\"" : "" %>></button>
                            <%
                                }
                            %>
                        </div>
                        <div class="carousel-inner">
                            <%
                                for (int i = 0; i < imagenesCarrusel.size(); i++) {
                                    CarruselImagen imagen = imagenesCarrusel.get(i);
                            %>
                            <div class="carousel-item <%= i == 0 ? "active" : "" %>">
                                <img src="<%= imagen.getUrlImagen() %>" class="d-block w-100" alt="<%= imagen.getTitulo() %>">
                                <div class="carousel-caption d-none d-md-block">
                                    <h5><%= imagen.getTitulo() %></h5>
                                    <% if (imagen.tieneDescripcion()) { %>
                                        <p><%= imagen.getDescripcion() %></p>
                                    <% } %>
                                </div>
                            </div>
                            <%
                                }
                            %>
                        </div>
                        <button class="carousel-control-prev" type="button" data-bs-target="#carouselDashboard" data-bs-slide="prev">
                            <span class="carousel-control-prev-icon"></span>
                            <span class="visually-hidden">Anterior</span>
                        </button>
                        <button class="carousel-control-next" type="button" data-bs-target="#carouselDashboard" data-bs-slide="next">
                            <span class="carousel-control-next-icon"></span>
                            <span class="visually-hidden">Siguiente</span>
                        </button>
                    </div>
                </div>
            </div>
            <%
                }
            %>
        </div>
    </div>

    <script src="https://cdn.jsdelivr.net/npm/bootstrap@5.3.0/dist/js/bootstrap.bundle.min.js"></script>
    <script>
        // Toggle sidebar
        document.getElementById('sidebarToggle').addEventListener('click', function () {
            const sidebar = document.getElementById('sidebar');
            const content = document.getElementById('content');

            sidebar.classList.toggle('collapsed');
            content.classList.toggle('expanded');
        });

        // Auto-start carousel
        document.addEventListener('DOMContentLoaded', function() {
            var carousel = document.getElementById('carouselDashboard');
            if (carousel) {
                var bsCarousel = new bootstrap.Carousel(carousel, {
                    interval: 5000,
                    wrap: true
                });
            }
        });
    </script>
</body>
</html>
