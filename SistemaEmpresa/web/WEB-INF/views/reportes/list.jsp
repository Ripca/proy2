<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<!DOCTYPE html>
<html lang="es">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>Reportes - Sistema Empresa</title>
    <link href="https://cdn.jsdelivr.net/npm/bootstrap@5.3.0/dist/css/bootstrap.min.css" rel="stylesheet">
    <link href="https://cdnjs.cloudflare.com/ajax/libs/font-awesome/6.0.0/css/all.min.css" rel="stylesheet">
    <link href="https://cdn.jsdelivr.net/npm/sweetalert2@11/dist/sweetalert2.min.css" rel="stylesheet">
    
    <style>
        .report-card {
            transition: all 0.3s ease;
            border: none;
            box-shadow: 0 2px 8px rgba(0,0,0,0.1);
        }
        
        .report-card:hover {
            transform: translateY(-5px);
            box-shadow: 0 8px 16px rgba(0,0,0,0.15);
        }
        
        .report-icon {
            font-size: 2.5rem;
            margin-bottom: 15px;
        }
        
        .report-title {
            font-size: 1.1rem;
            font-weight: 600;
            margin-bottom: 10px;
            color: #333;
        }
        
        .report-description {
            font-size: 0.9rem;
            color: #666;
            margin-bottom: 15px;
        }
        
        .btn-generate {
            width: 100%;
            border-radius: 6px;
            font-weight: 600;
            transition: all 0.3s ease;
        }
        
        .page-header {
            margin-bottom: 2rem;
            padding-bottom: 1rem;
            border-bottom: 2px solid #e0e0e0;
        }
        
        .page-header h1 {
            color: #333;
            font-weight: 700;
        }
        
        .page-header p {
            color: #666;
            margin: 0;
        }
    </style>
</head>
<body>
    <div class="container-fluid p-4">
        <!-- Header -->
        <div class="page-header">
            <h1>
                <i class="fas fa-chart-bar me-2"></i>Reportes
            </h1>
            <p>Genera reportes de tu negocio</p>
        </div>
        
        <!-- Reportes disponibles -->
        <div class="row">
            <!-- Reporte de Inventario de Productos - FUNCIONAL -->
            <div class="col-md-6 col-lg-4 mb-4">
                <div class="card report-card h-100">
                    <div class="card-body text-center">
                        <div class="report-icon text-primary">
                            <i class="fas fa-boxes"></i>
                        </div>
                        <h5 class="report-title">Inventario de Productos</h5>
                        <p class="report-description">
                            Reporte detallado del inventario actual de productos con cantidades y valores.
                        </p>
                        <button class="btn btn-primary btn-generate" onclick="generarReporte('inventario')">
                            <i class="fas fa-download me-2"></i>Descargar PDF
                        </button>
                    </div>
                </div>
            </div>

            <!-- Reporte de Ventas - EN DESARROLLO -->
            <div class="col-md-6 col-lg-4 mb-4">
                <div class="card report-card h-100 opacity-75">
                    <div class="card-body text-center">
                        <div class="report-icon text-info">
                            <i class="fas fa-chart-line"></i>
                        </div>
                        <h5 class="report-title">Reporte de Ventas</h5>
                        <p class="report-description">
                            Análisis detallado de ventas por período, cliente y producto.
                        </p>
                        <button class="btn btn-info btn-generate" disabled>
                            <i class="fas fa-clock me-2"></i>En Desarrollo
                        </button>
                    </div>
                </div>
            </div>

            <!-- Reporte de Compras - EN DESARROLLO -->
            <div class="col-md-6 col-lg-4 mb-4">
                <div class="card report-card h-100 opacity-75">
                    <div class="card-body text-center">
                        <div class="report-icon text-warning">
                            <i class="fas fa-shopping-cart"></i>
                        </div>
                        <h5 class="report-title">Reporte de Compras</h5>
                        <p class="report-description">
                            Análisis de compras por período, proveedor y producto.
                        </p>
                        <button class="btn btn-warning btn-generate" disabled>
                            <i class="fas fa-clock me-2"></i>En Desarrollo
                        </button>
                    </div>
                </div>
            </div>

            <!-- Reporte de Empleados - EN DESARROLLO -->
            <div class="col-md-6 col-lg-4 mb-4">
                <div class="card report-card h-100 opacity-75">
                    <div class="card-body text-center">
                        <div class="report-icon text-success">
                            <i class="fas fa-users"></i>
                        </div>
                        <h5 class="report-title">Reporte de Empleados</h5>
                        <p class="report-description">
                            Información completa de todos los empleados registrados.
                        </p>
                        <button class="btn btn-success btn-generate" disabled>
                            <i class="fas fa-clock me-2"></i>En Desarrollo
                        </button>
                    </div>
                </div>
            </div>

            <!-- Reporte de Clientes - EN DESARROLLO -->
            <div class="col-md-6 col-lg-4 mb-4">
                <div class="card report-card h-100 opacity-75">
                    <div class="card-body text-center">
                        <div class="report-icon text-danger">
                            <i class="fas fa-user-friends"></i>
                        </div>
                        <h5 class="report-title">Reporte de Clientes</h5>
                        <p class="report-description">
                            Listado de clientes con información de contacto y compras.
                        </p>
                        <button class="btn btn-danger btn-generate" disabled>
                            <i class="fas fa-clock me-2"></i>En Desarrollo
                        </button>
                    </div>
                </div>
            </div>
        </div>
    </div>



    <script src="https://cdn.jsdelivr.net/npm/bootstrap@5.3.0/dist/js/bootstrap.bundle.min.js"></script>
    <script src="https://cdn.jsdelivr.net/npm/sweetalert2@11"></script>

    <script>
        function generarReporte(tipo) {
            Swal.fire({
                title: 'Generando Reporte',
                html: 'Por favor espere...',
                allowOutsideClick: false,
                didOpen: (modal) => {
                    Swal.showLoading();
                }
            });

            // Redirigir a ReporteServlet con el tipo de reporte
            window.location.href = 'jasper-report?tipo=' + tipo;

            // Cerrar el modal después de 2 segundos
            setTimeout(() => {
                Swal.close();
            }, 2000);
        }
    </script>
</body>
</html>

