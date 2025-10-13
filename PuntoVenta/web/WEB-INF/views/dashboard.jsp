<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<!DOCTYPE html>
<html lang="es">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>Dashboard - PuntoVenta</title>
    
    <!-- Bootstrap 5 CSS -->
    <link href="https://cdn.jsdelivr.net/npm/bootstrap@5.3.0/dist/css/bootstrap.min.css" rel="stylesheet">
    <!-- Font Awesome -->
    <link href="https://cdnjs.cloudflare.com/ajax/libs/font-awesome/6.4.0/css/all.min.css" rel="stylesheet">
    <!-- Chart.js -->
    <script src="https://cdn.jsdelivr.net/npm/chart.js"></script>
    
    <style>
        body {
            background-color: #f8f9fa;
        }
        
        .navbar-brand {
            font-weight: bold;
            color: #495057 !important;
        }
        
        .sidebar {
            min-height: calc(100vh - 56px);
            background: linear-gradient(135deg, #667eea 0%, #764ba2 100%);
            color: white;
        }
        
        .sidebar .nav-link {
            color: rgba(255, 255, 255, 0.8);
            padding: 0.75rem 1rem;
            border-radius: 0.375rem;
            margin: 0.25rem 0;
            transition: all 0.3s ease;
        }
        
        .sidebar .nav-link:hover,
        .sidebar .nav-link.active {
            color: white;
            background-color: rgba(255, 255, 255, 0.1);
        }
        
        .card {
            border: none;
            border-radius: 15px;
            box-shadow: 0 0.125rem 0.25rem rgba(0, 0, 0, 0.075);
            transition: transform 0.2s ease-in-out;
        }
        
        .card:hover {
            transform: translateY(-2px);
            box-shadow: 0 0.5rem 1rem rgba(0, 0, 0, 0.15);
        }
        
        .stat-card {
            background: linear-gradient(135deg, #667eea 0%, #764ba2 100%);
            color: white;
        }
        
        .stat-card .card-body {
            padding: 1.5rem;
        }
        
        .stat-number {
            font-size: 2rem;
            font-weight: bold;
            margin-bottom: 0.5rem;
        }
        
        .stat-label {
            font-size: 0.9rem;
            opacity: 0.9;
        }
        
        .chart-container {
            position: relative;
            height: 300px;
        }
        
        .table-responsive {
            border-radius: 10px;
            overflow: hidden;
        }
        
        .badge-stock-bajo {
            background-color: #dc3545;
        }
        
        .badge-stock-normal {
            background-color: #28a745;
        }
        
        .carousel-item img {
            height: 200px;
            object-fit: cover;
            border-radius: 10px;
        }
        
        .notification-badge {
            position: absolute;
            top: -5px;
            right: -5px;
            background-color: #dc3545;
            color: white;
            border-radius: 50%;
            width: 20px;
            height: 20px;
            font-size: 0.75rem;
            display: flex;
            align-items: center;
            justify-content: center;
        }
    </style>
</head>
<body>
    <!-- Navbar -->
    <nav class="navbar navbar-expand-lg navbar-light bg-white shadow-sm">
        <div class="container-fluid">
            <a class="navbar-brand" href="${pageContext.request.contextPath}/dashboard">
                <i class="fas fa-cash-register me-2"></i>PuntoVenta
            </a>
            
            <div class="navbar-nav ms-auto">
                <div class="nav-item dropdown">
                    <a class="nav-link dropdown-toggle" href="#" id="userDropdown" role="button" 
                       data-bs-toggle="dropdown" aria-expanded="false">
                        <i class="fas fa-user-circle me-1"></i>
                        ${usuarioActual.nombreEmpleado}
                        <span class="badge bg-primary ms-1">${usuarioActual.rol}</span>
                    </a>
                    <ul class="dropdown-menu dropdown-menu-end">
                        <li><a class="dropdown-item" href="#"><i class="fas fa-user me-2"></i>Perfil</a></li>
                        <li><a class="dropdown-item" href="#"><i class="fas fa-cog me-2"></i>Configuración</a></li>
                        <li><hr class="dropdown-divider"></li>
                        <li><a class="dropdown-item" href="${pageContext.request.contextPath}/logout">
                            <i class="fas fa-sign-out-alt me-2"></i>Cerrar Sesión</a></li>
                    </ul>
                </div>
            </div>
        </div>
    </nav>

    <div class="container-fluid">
        <div class="row">
            <!-- Sidebar -->
            <nav class="col-md-3 col-lg-2 d-md-block sidebar collapse">
                <div class="position-sticky pt-3">
                    <ul class="nav flex-column">
                        <li class="nav-item">
                            <a class="nav-link active" href="${pageContext.request.contextPath}/dashboard">
                                <i class="fas fa-tachometer-alt me-2"></i>Dashboard
                            </a>
                        </li>
                        
                        <c:if test="${usuarioActual.puedeVender()}">
                        <li class="nav-item">
                            <a class="nav-link" href="${pageContext.request.contextPath}/ventas">
                                <i class="fas fa-shopping-cart me-2"></i>Ventas
                                <c:if test="${not empty productosStockBajo}">
                                    <span class="notification-badge">${fn:length(productosStockBajo)}</span>
                                </c:if>
                            </a>
                        </li>
                        </c:if>
                        
                        <c:if test="${usuarioActual.puedeComprar()}">
                        <li class="nav-item">
                            <a class="nav-link" href="${pageContext.request.contextPath}/compras">
                                <i class="fas fa-truck me-2"></i>Compras
                                <c:if test="${not empty comprasPendientes}">
                                    <span class="notification-badge">${fn:length(comprasPendientes)}</span>
                                </c:if>
                            </a>
                        </li>
                        </c:if>
                        
                        <li class="nav-item">
                            <a class="nav-link" href="${pageContext.request.contextPath}/productos">
                                <i class="fas fa-box me-2"></i>Productos
                            </a>
                        </li>
                        
                        <li class="nav-item">
                            <a class="nav-link" href="${pageContext.request.contextPath}/clientes">
                                <i class="fas fa-users me-2"></i>Clientes
                            </a>
                        </li>
                        
                        <li class="nav-item">
                            <a class="nav-link" href="${pageContext.request.contextPath}/proveedores">
                                <i class="fas fa-industry me-2"></i>Proveedores
                            </a>
                        </li>
                        
                        <c:if test="${usuarioActual.tienePermisosAdmin()}">
                        <li class="nav-item">
                            <a class="nav-link" href="${pageContext.request.contextPath}/empleados">
                                <i class="fas fa-id-badge me-2"></i>Empleados
                            </a>
                        </li>
                        
                        <li class="nav-item">
                            <a class="nav-link" href="${pageContext.request.contextPath}/usuarios">
                                <i class="fas fa-user-cog me-2"></i>Usuarios
                            </a>
                        </li>
                        
                        <li class="nav-item">
                            <a class="nav-link" href="${pageContext.request.contextPath}/reportes">
                                <i class="fas fa-chart-bar me-2"></i>Reportes
                            </a>
                        </li>
                        </c:if>
                    </ul>
                </div>
            </nav>

            <!-- Main content -->
            <main class="col-md-9 ms-sm-auto col-lg-10 px-md-4">
                <div class="d-flex justify-content-between flex-wrap flex-md-nowrap align-items-center pt-3 pb-2 mb-3 border-bottom">
                    <h1 class="h2">Dashboard</h1>
                    <div class="btn-toolbar mb-2 mb-md-0">
                        <div class="btn-group me-2">
                            <button type="button" class="btn btn-sm btn-outline-secondary" onclick="refreshStats()">
                                <i class="fas fa-sync-alt"></i> Actualizar
                            </button>
                        </div>
                    </div>
                </div>

                <!-- Estadísticas principales -->
                <div class="row mb-4">
                    <div class="col-xl-3 col-md-6 mb-4">
                        <div class="card stat-card">
                            <div class="card-body">
                                <div class="d-flex justify-content-between">
                                    <div>
                                        <div class="stat-number">${totalProductos}</div>
                                        <div class="stat-label">Productos</div>
                                    </div>
                                    <div class="align-self-center">
                                        <i class="fas fa-box fa-2x opacity-75"></i>
                                    </div>
                                </div>
                            </div>
                        </div>
                    </div>
                    
                    <div class="col-xl-3 col-md-6 mb-4">
                        <div class="card stat-card">
                            <div class="card-body">
                                <div class="d-flex justify-content-between">
                                    <div>
                                        <div class="stat-number">${totalClientes}</div>
                                        <div class="stat-label">Clientes</div>
                                    </div>
                                    <div class="align-self-center">
                                        <i class="fas fa-users fa-2x opacity-75"></i>
                                    </div>
                                </div>
                            </div>
                        </div>
                    </div>
                    
                    <div class="col-xl-3 col-md-6 mb-4">
                        <div class="card stat-card">
                            <div class="card-body">
                                <div class="d-flex justify-content-between">
                                    <div>
                                        <div class="stat-number">${totalVentas}</div>
                                        <div class="stat-label">Ventas</div>
                                    </div>
                                    <div class="align-self-center">
                                        <i class="fas fa-shopping-cart fa-2x opacity-75"></i>
                                    </div>
                                </div>
                            </div>
                        </div>
                    </div>
                    
                    <div class="col-xl-3 col-md-6 mb-4">
                        <div class="card stat-card">
                            <div class="card-body">
                                <div class="d-flex justify-content-between">
                                    <div>
                                        <div class="stat-number">
                                            <fmt:formatNumber value="${ventasDelDia}" type="currency" currencySymbol="Q"/>
                                        </div>
                                        <div class="stat-label">Ventas Hoy</div>
                                    </div>
                                    <div class="align-self-center">
                                        <i class="fas fa-dollar-sign fa-2x opacity-75"></i>
                                    </div>
                                </div>
                            </div>
                        </div>
                    </div>
                </div>

                <!-- Carrusel de imágenes -->
                <c:if test="${not empty imagenesCarrusel}">
                <div class="row mb-4">
                    <div class="col-12">
                        <div class="card">
                            <div class="card-body">
                                <div id="dashboardCarousel" class="carousel slide" data-bs-ride="carousel">
                                    <div class="carousel-inner">
                                        <c:forEach var="imagen" items="${imagenesCarrusel}" varStatus="status">
                                        <div class="carousel-item ${status.first ? 'active' : ''}">
                                            <img src="${imagen.imagenUrl}" class="d-block w-100" alt="${imagen.titulo}">
                                            <div class="carousel-caption d-none d-md-block">
                                                <h5>${imagen.titulo}</h5>
                                                <p>${imagen.descripcion}</p>
                                            </div>
                                        </div>
                                        </c:forEach>
                                    </div>
                                    <button class="carousel-control-prev" type="button" data-bs-target="#dashboardCarousel" data-bs-slide="prev">
                                        <span class="carousel-control-prev-icon"></span>
                                    </button>
                                    <button class="carousel-control-next" type="button" data-bs-target="#dashboardCarousel" data-bs-slide="next">
                                        <span class="carousel-control-next-icon"></span>
                                    </button>
                                </div>
                            </div>
                        </div>
                    </div>
                </div>
                </c:if>

                <div class="row">
                    <!-- Productos con stock bajo -->
                    <div class="col-lg-6 mb-4">
                        <div class="card">
                            <div class="card-header d-flex justify-content-between align-items-center">
                                <h5 class="mb-0">
                                    <i class="fas fa-exclamation-triangle text-warning me-2"></i>
                                    Productos con Stock Bajo
                                </h5>
                                <span class="badge bg-warning">${fn:length(productosStockBajo)}</span>
                            </div>
                            <div class="card-body">
                                <c:choose>
                                    <c:when test="${not empty productosStockBajo}">
                                        <div class="table-responsive">
                                            <table class="table table-sm">
                                                <thead>
                                                    <tr>
                                                        <th>Producto</th>
                                                        <th>Existencia</th>
                                                        <th>Mínimo</th>
                                                    </tr>
                                                </thead>
                                                <tbody>
                                                    <c:forEach var="producto" items="${productosStockBajo}" end="4">
                                                    <tr>
                                                        <td>${producto.producto}</td>
                                                        <td>
                                                            <span class="badge badge-stock-bajo">${producto.existencia}</span>
                                                        </td>
                                                        <td>${producto.stockMinimo}</td>
                                                    </tr>
                                                    </c:forEach>
                                                </tbody>
                                            </table>
                                        </div>
                                        <c:if test="${fn:length(productosStockBajo) > 5}">
                                            <div class="text-center">
                                                <a href="${pageContext.request.contextPath}/productos?action=stockBajo" 
                                                   class="btn btn-sm btn-outline-warning">
                                                    Ver todos (${fn:length(productosStockBajo)})
                                                </a>
                                            </div>
                                        </c:if>
                                    </c:when>
                                    <c:otherwise>
                                        <div class="text-center text-muted">
                                            <i class="fas fa-check-circle fa-3x mb-3"></i>
                                            <p>Todos los productos tienen stock suficiente</p>
                                        </div>
                                    </c:otherwise>
                                </c:choose>
                            </div>
                        </div>
                    </div>

                    <!-- Top clientes -->
                    <div class="col-lg-6 mb-4">
                        <div class="card">
                            <div class="card-header">
                                <h5 class="mb-0">
                                    <i class="fas fa-star text-warning me-2"></i>
                                    Top Clientes
                                </h5>
                            </div>
                            <div class="card-body">
                                <c:choose>
                                    <c:when test="${not empty topClientes}">
                                        <div class="table-responsive">
                                            <table class="table table-sm">
                                                <thead>
                                                    <tr>
                                                        <th>Cliente</th>
                                                        <th>Compras</th>
                                                    </tr>
                                                </thead>
                                                <tbody>
                                                    <c:forEach var="cliente" items="${topClientes}">
                                                    <tr>
                                                        <td>${cliente.nombres} ${cliente.apellidos}</td>
                                                        <td>
                                                            <span class="badge bg-primary">${cliente.totalCompras}</span>
                                                        </td>
                                                    </tr>
                                                    </c:forEach>
                                                </tbody>
                                            </table>
                                        </div>
                                    </c:when>
                                    <c:otherwise>
                                        <div class="text-center text-muted">
                                            <i class="fas fa-users fa-3x mb-3"></i>
                                            <p>No hay datos de clientes disponibles</p>
                                        </div>
                                    </c:otherwise>
                                </c:choose>
                            </div>
                        </div>
                    </div>
                </div>

                <!-- Compras pendientes -->
                <c:if test="${not empty comprasPendientes}">
                <div class="row">
                    <div class="col-12">
                        <div class="card">
                            <div class="card-header d-flex justify-content-between align-items-center">
                                <h5 class="mb-0">
                                    <i class="fas fa-clock text-info me-2"></i>
                                    Compras Pendientes
                                </h5>
                                <span class="badge bg-info">${fn:length(comprasPendientes)}</span>
                            </div>
                            <div class="card-body">
                                <div class="table-responsive">
                                    <table class="table">
                                        <thead>
                                            <tr>
                                                <th>Orden #</th>
                                                <th>Proveedor</th>
                                                <th>Fecha Orden</th>
                                                <th>Total</th>
                                                <th>Acciones</th>
                                            </tr>
                                        </thead>
                                        <tbody>
                                            <c:forEach var="compra" items="${comprasPendientes}" end="4">
                                            <tr>
                                                <td>${compra.noOrdenCompra}</td>
                                                <td>${compra.nombreProveedor}</td>
                                                <td>
                                                    <fmt:formatDate value="${compra.fechaOrden}" pattern="dd/MM/yyyy"/>
                                                </td>
                                                <td>
                                                    <fmt:formatNumber value="${compra.total}" type="currency" currencySymbol="Q"/>
                                                </td>
                                                <td>
                                                    <a href="${pageContext.request.contextPath}/compras?action=view&id=${compra.idCompra}" 
                                                       class="btn btn-sm btn-outline-primary">
                                                        <i class="fas fa-eye"></i>
                                                    </a>
                                                </td>
                                            </tr>
                                            </c:forEach>
                                        </tbody>
                                    </table>
                                </div>
                            </div>
                        </div>
                    </div>
                </div>
                </c:if>
            </main>
        </div>
    </div>

    <!-- Scripts -->
    <script src="https://cdn.jsdelivr.net/npm/bootstrap@5.3.0/dist/js/bootstrap.bundle.min.js"></script>
    <script src="https://code.jquery.com/jquery-3.7.0.min.js"></script>
    
    <script>
        // Función para actualizar estadísticas
        function refreshStats() {
            $.ajax({
                url: '${pageContext.request.contextPath}/dashboard',
                method: 'POST',
                data: { action: 'getStats' },
                dataType: 'json'
            })
            .done(function(data) {
                // Actualizar números en las tarjetas
                $('.stat-number').each(function(index) {
                    const values = [data.totalProductos, data.totalClientes, data.totalVentas, 'Q' + data.ventasDelDia.toFixed(2)];
                    $(this).text(values[index]);
                });
            })
            .fail(function() {
                console.error('Error al actualizar estadísticas');
            });
        }
        
        // Auto-refresh cada 5 minutos
        setInterval(refreshStats, 300000);
        
        // Animaciones de entrada
        $(document).ready(function() {
            $('.card').hide().each(function(index) {
                $(this).delay(index * 100).fadeIn(500);
            });
        });
    </script>
</body>
</html>
