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
            <!-- Reporte de Inventario de Productos -->
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
            
            <!-- Reporte de Productos -->
            <div class="col-md-6 col-lg-4 mb-4">
                <div class="card report-card h-100">
                    <div class="card-body text-center">
                        <div class="report-icon text-info">
                            <i class="fas fa-box"></i>
                        </div>
                        <h5 class="report-title">Reporte de Productos</h5>
                        <p class="report-description">
                            Listado completo de productos con detalles y existencias.
                        </p>
                        <button class="btn btn-info btn-generate" onclick="generarReporte('productos')">
                            <i class="fas fa-download me-2"></i>Descargar PDF
                        </button>
                    </div>
                </div>
            </div>

            <!-- Reporte de Empleados -->
            <div class="col-md-6 col-lg-4 mb-4">
                <div class="card report-card h-100">
                    <div class="card-body text-center">
                        <div class="report-icon text-warning">
                            <i class="fas fa-users"></i>
                        </div>
                        <h5 class="report-title">Reporte de Empleados</h5>
                        <p class="report-description">
                            Información completa de todos los empleados registrados.
                        </p>
                        <button class="btn btn-warning btn-generate" onclick="generarReporte('empleados')">
                            <i class="fas fa-download me-2"></i>Descargar PDF
                        </button>
                    </div>
                </div>
            </div>

            <!-- Reporte de Clientes -->
            <div class="col-md-6 col-lg-4 mb-4">
                <div class="card report-card h-100">
                    <div class="card-body text-center">
                        <div class="report-icon text-success">
                            <i class="fas fa-user-friends"></i>
                        </div>
                        <h5 class="report-title">Reporte de Clientes</h5>
                        <p class="report-description">
                            Listado de clientes con información de contacto.
                        </p>
                        <button class="btn btn-success btn-generate" onclick="generarReporte('clientes')">
                            <i class="fas fa-download me-2"></i>Descargar PDF
                        </button>
                    </div>
                </div>
            </div>

            <!-- Factura de Venta -->
            <div class="col-md-6 col-lg-4 mb-4">
                <div class="card report-card h-100">
                    <div class="card-body text-center">
                        <div class="report-icon text-primary">
                            <i class="fas fa-receipt"></i>
                        </div>
                        <h5 class="report-title">Factura de Venta</h5>
                        <p class="report-description">
                            Genera factura de una venta específica.
                        </p>
                        <button class="btn btn-primary btn-generate" onclick="abrirModalVentas()">
                            <i class="fas fa-file-pdf me-2"></i>Generar Factura
                        </button>
                    </div>
                </div>
            </div>

            <!-- Factura de Compra -->
            <div class="col-md-6 col-lg-4 mb-4">
                <div class="card report-card h-100">
                    <div class="card-body text-center">
                        <div class="report-icon text-danger">
                            <i class="fas fa-file-invoice"></i>
                        </div>
                        <h5 class="report-title">Factura de Compra</h5>
                        <p class="report-description">
                            Genera factura de una compra específica.
                        </p>
                        <button class="btn btn-danger btn-generate" onclick="abrirModalCompras()">
                            <i class="fas fa-file-pdf me-2"></i>Generar Factura
                        </button>
                    </div>
                </div>
            </div>
        </div>
    </div>

    <!-- Modal para seleccionar Venta -->
    <div class="modal fade" id="modalVentas" tabindex="-1">
        <div class="modal-dialog modal-lg">
            <div class="modal-content">
                <div class="modal-header">
                    <h5 class="modal-title">
                        <i class="fas fa-shopping-cart me-2"></i>Seleccionar Venta
                    </h5>
                    <button type="button" class="btn-close" data-bs-dismiss="modal"></button>
                </div>
                <div class="modal-body">
                    <div class="table-responsive">
                        <table class="table table-hover" id="tablaVentas">
                            <thead class="table-dark">
                                <tr>
                                    <th>No. Factura</th>
                                    <th>Cliente</th>
                                    <th>Fecha</th>
                                    <th>Total</th>
                                    <th>Acción</th>
                                </tr>
                            </thead>
                            <tbody id="bodyVentas">
                                <!-- Se cargará dinámicamente -->
                            </tbody>
                        </table>
                    </div>
                </div>
            </div>
        </div>
    </div>

    <!-- Modal para seleccionar Compra -->
    <div class="modal fade" id="modalCompras" tabindex="-1">
        <div class="modal-dialog modal-lg">
            <div class="modal-content">
                <div class="modal-header">
                    <h5 class="modal-title">
                        <i class="fas fa-shopping-bag me-2"></i>Seleccionar Compra
                    </h5>
                    <button type="button" class="btn-close" data-bs-dismiss="modal"></button>
                </div>
                <div class="modal-body">
                    <div class="table-responsive">
                        <table class="table table-hover" id="tablaCompras">
                            <thead class="table-dark">
                                <tr>
                                    <th>No. Orden</th>
                                    <th>Proveedor</th>
                                    <th>Fecha</th>
                                    <th>Total</th>
                                    <th>Acción</th>
                                </tr>
                            </thead>
                            <tbody id="bodyCompras">
                                <!-- Se cargará dinámicamente -->
                            </tbody>
                        </table>
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
            window.location.href = 'ReporteServlet?tipo=' + tipo;

            // Cerrar el modal después de 2 segundos
            setTimeout(() => {
                Swal.close();
            }, 2000);
        }

        function abrirModalVentas() {
            // TODO: Cargar ventas desde el servidor
            // fetch('VentaServlet?action=obtenerTodos')
            //     .then(response => response.json())
            //     .then(data => {
            //         let html = '';
            //         data.forEach(venta => {
            //             html += `<tr>
            //                 <td>${venta.noFactura}</td>
            //                 <td>${venta.nombreCliente}</td>
            //                 <td>${venta.fechaFactura}</td>
            //                 <td>Q. ${venta.total.toFixed(2)}</td>
            //                 <td>
            //                     <button class="btn btn-sm btn-primary" onclick="generarFacturaVenta(${venta.idVenta})">
            //                         <i class="fas fa-file-pdf"></i> Generar
            //                     </button>
            //                 </td>
            //             </tr>`;
            //         });
            //         document.getElementById('bodyVentas').innerHTML = html;
            //     });

            const modal = new bootstrap.Modal(document.getElementById('modalVentas'));
            modal.show();
        }

        function abrirModalCompras() {
            // TODO: Cargar compras desde el servidor
            // fetch('CompraServlet?action=obtenerTodos')
            //     .then(response => response.json())
            //     .then(data => {
            //         let html = '';
            //         data.forEach(compra => {
            //             html += `<tr>
            //                 <td>${compra.noOrdenCompra}</td>
            //                 <td>${compra.nombreProveedor}</td>
            //                 <td>${compra.fechaOrden}</td>
            //                 <td>Q. ${compra.total.toFixed(2)}</td>
            //                 <td>
            //                     <button class="btn btn-sm btn-danger" onclick="generarFacturaCompra(${compra.idCompra})">
            //                         <i class="fas fa-file-pdf"></i> Generar
            //                     </button>
            //                 </td>
            //             </tr>`;
            //         });
            //         document.getElementById('bodyCompras').innerHTML = html;
            //     });

            const modal = new bootstrap.Modal(document.getElementById('modalCompras'));
            modal.show();
        }

        function generarFacturaVenta(idVenta) {
            // TODO: Generar factura de venta
            // window.location.href = 'ReporteServlet?tipo=factura_venta&idVenta=' + idVenta;
            alert('Funcionalidad de factura de venta en desarrollo');
        }

        function generarFacturaCompra(idCompra) {
            // TODO: Generar factura de compra
            // window.location.href = 'ReporteServlet?tipo=factura_compra&idCompra=' + idCompra;
            alert('Funcionalidad de factura de compra en desarrollo');
        }
    </script>
</body>
</html>

