<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@page import="com.sistemaempresa.utils.JSTLAlternative"%>
<!DOCTYPE html>
<html>
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>Sistema Empresa - Dashboard</title>
    <link href="https://cdn.jsdelivr.net/npm/bootstrap@5.1.3/dist/css/bootstrap.min.css" rel="stylesheet">
    <link href="https://cdnjs.cloudflare.com/ajax/libs/font-awesome/6.0.0/css/all.min.css" rel="stylesheet">
</head>
<body>
    <!-- Navbar -->
    <nav class="navbar navbar-expand-lg navbar-dark bg-dark">
        <div class="container-fluid">
            <a class="navbar-brand" href="DashboardServlet">
                <i class="fas fa-building"></i> Sistema Empresa
            </a>
            
            <button class="navbar-toggler" type="button" data-bs-toggle="collapse" data-bs-target="#navbarNav">
                <span class="navbar-toggler-icon"></span>
            </button>
            
            <div class="collapse navbar-collapse" id="navbarNav">
                <ul class="navbar-nav me-auto">
                    <!-- Menú Productos -->
                    <li class="nav-item dropdown">
                        <a class="nav-link dropdown-toggle" href="#" id="productosDropdown" role="button" data-bs-toggle="dropdown">
                            <i class="fas fa-box"></i> Productos
                        </a>
                        <ul class="dropdown-menu">
                            <li><a class="dropdown-item" href="MarcaServlet">Marcas</a></li>
                        </ul>
                    </li>
                    
                    <!-- Menú Ventas -->
                    <li class="nav-item dropdown">
                        <a class="nav-link dropdown-toggle" href="#" id="ventasDropdown" role="button" data-bs-toggle="dropdown">
                            <i class="fas fa-shopping-cart"></i> Ventas
                        </a>
                        <ul class="dropdown-menu">
                            <li><a class="dropdown-item" href="ClienteServlet">Clientes</a></li>
                            <li><a class="dropdown-item" href="EmpleadoServlet">Empleados</a></li>
                            <li><a class="dropdown-item" href="PuestoServlet">Puestos</a></li>
                        </ul>
                    </li>
                    
                    <!-- Menú Compras -->
                    <li class="nav-item dropdown">
                        <a class="nav-link dropdown-toggle" href="#" id="comprasDropdown" role="button" data-bs-toggle="dropdown">
                            <i class="fas fa-truck"></i> Compras
                        </a>
                        <ul class="dropdown-menu">
                            <li><a class="dropdown-item" href="ProveedorServlet">Proveedores</a></li>
                        </ul>
                    </li>
                    
                    <!-- Menú Reportes -->
                    <li class="nav-item dropdown">
                        <a class="nav-link dropdown-toggle" href="#" id="reportesDropdown" role="button" data-bs-toggle="dropdown">
                            <i class="fas fa-chart-bar"></i> Reportes
                        </a>
                        <ul class="dropdown-menu">
                            <li><a class="dropdown-item" href="#" onclick="alert('Funcionalidad en desarrollo')">Reporte de Ventas</a></li>
                            <li><a class="dropdown-item" href="#" onclick="alert('Funcionalidad en desarrollo')">Reporte de Compras</a></li>
                            <li><a class="dropdown-item" href="#" onclick="alert('Funcionalidad en desarrollo')">Reporte de Inventario</a></li>
                        </ul>
                    </li>
                </ul>
                
                <ul class="navbar-nav">
                    <li class="nav-item dropdown">
                        <a class="nav-link dropdown-toggle" href="#" id="userDropdown" role="button" data-bs-toggle="dropdown">
                            <i class="fas fa-user"></i> <%= session.getAttribute("nombreCompleto") != null ? session.getAttribute("nombreCompleto") : "Usuario" %>
                        </a>
                        <ul class="dropdown-menu">
                            <li><a class="dropdown-item" href="LoginServlet?action=logout">
                                <i class="fas fa-sign-out-alt"></i> Cerrar Sesión
                            </a></li>
                        </ul>
                    </li>
                </ul>
            </div>
        </div>
    </nav>

    <!-- Contenido Principal -->
    <div class="container-fluid mt-4">
        <div class="row">
            <div class="col-12">
                <h1 class="mb-4">
                    <i class="fas fa-tachometer-alt"></i> Dashboard
                </h1>
                
                <!-- Cards de resumen -->
                <div class="row">
                    <div class="col-md-3 mb-4">
                        <div class="card bg-primary text-white">
                            <div class="card-body">
                                <div class="d-flex justify-content-between">
                                    <div>
                                        <h5 class="card-title">Clientes</h5>
                                        <h2 class="mb-0">--</h2>
                                    </div>
                                    <div class="align-self-center">
                                        <i class="fas fa-users fa-2x"></i>
                                    </div>
                                </div>
                            </div>
                            <div class="card-footer">
                                <a href="ClienteServlet" class="text-white text-decoration-none">
                                    Ver todos <i class="fas fa-arrow-right"></i>
                                </a>
                            </div>
                        </div>
                    </div>
                    
                    <div class="col-md-3 mb-4">
                        <div class="card bg-success text-white">
                            <div class="card-body">
                                <div class="d-flex justify-content-between">
                                    <div>
                                        <h5 class="card-title">Productos</h5>
                                        <h2 class="mb-0">--</h2>
                                    </div>
                                    <div class="align-self-center">
                                        <i class="fas fa-box fa-2x"></i>
                                    </div>
                                </div>
                            </div>
                            <div class="card-footer">
                                <a href="ProductoServlet" class="text-white text-decoration-none">
                                    Ver todos <i class="fas fa-arrow-right"></i>
                                </a>
                            </div>
                        </div>
                    </div>
                    
                    <div class="col-md-3 mb-4">
                        <div class="card bg-warning text-white">
                            <div class="card-body">
                                <div class="d-flex justify-content-between">
                                    <div>
                                        <h5 class="card-title">Empleados</h5>
                                        <h2 class="mb-0">--</h2>
                                    </div>
                                    <div class="align-self-center">
                                        <i class="fas fa-user-tie fa-2x"></i>
                                    </div>
                                </div>
                            </div>
                            <div class="card-footer">
                                <a href="EmpleadoServlet" class="text-white text-decoration-none">
                                    Ver todos <i class="fas fa-arrow-right"></i>
                                </a>
                            </div>
                        </div>
                    </div>
                    
                    <div class="col-md-3 mb-4">
                        <div class="card bg-info text-white">
                            <div class="card-body">
                                <div class="d-flex justify-content-between">
                                    <div>
                                        <h5 class="card-title">Proveedores</h5>
                                        <h2 class="mb-0">--</h2>
                                    </div>
                                    <div class="align-self-center">
                                        <i class="fas fa-truck fa-2x"></i>
                                    </div>
                                </div>
                            </div>
                            <div class="card-footer">
                                <a href="ProveedorServlet" class="text-white text-decoration-none">
                                    Ver todos <i class="fas fa-arrow-right"></i>
                                </a>
                            </div>
                        </div>
                    </div>
                </div>

                <!-- Accesos rápidos -->
                <div class="row">
                    <div class="col-12">
                        <div class="card">
                            <div class="card-header">
                                <h5 class="mb-0">
                                    <i class="fas fa-bolt"></i> Accesos Rápidos
                                </h5>
                            </div>
                            
                            
                            <div class="card-body">
                                <div class="row">
                                    <div class="col-md-2 mb-3">
                                        <a href="ClienteServlet?action=new" class="btn btn-outline-primary w-100">
                                            <i class="fas fa-user-plus"></i><br>
                                            Nuevo Cliente
                                        </a>
                                    </div>
                                    <div class="col-md-2 mb-3">
                                        <a href="ProductoServlet?action=new" class="btn btn-outline-success w-100">
                                            <i class="fas fa-plus-square"></i><br>
                                            Nuevo Producto
                                        </a>
                                    </div>
                                    <div class="col-md-2 mb-3">
                                        <a href="EmpleadoServlet?action=new" class="btn btn-outline-warning w-100">
                                            <i class="fas fa-user-tie"></i><br>
                                            Nuevo Empleado
                                        </a>
                                    </div>
                                    <div class="col-md-2 mb-3">
                                        <a href="VentaServlet?action=new" class="btn btn-outline-info w-100">
                                            <i class="fas fa-shopping-cart"></i><br>
                                            Nueva Venta
                                        </a>
                                    </div>
                                    <div class="col-md-2 mb-3">
                                        <a href="CompraServlet?action=new" class="btn btn-outline-secondary w-100">
                                            <i class="fas fa-truck"></i><br>
                                            Nueva Compra
                                        </a>
                                    </div>
                                    <div class="col-md-2 mb-3">
                                        <a href="ProveedorServlet?action=new" class="btn btn-outline-dark w-100">
                                            <i class="fas fa-building"></i><br>
                                            Nuevo Proveedor
                                        </a>
                                    </div>
                                </div>
                            </div>
                        </div>
                    </div>
                </div>
            </div>
        </div>
    </div>

    <script src="https://cdn.jsdelivr.net/npm/bootstrap@5.1.3/dist/js/bootstrap.bundle.min.js"></script>
</body>
</html>
