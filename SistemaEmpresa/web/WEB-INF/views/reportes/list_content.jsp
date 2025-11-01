<%@ page contentType="text/html;charset=UTF-8" language="java" %>

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
</style>

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

    <div class="col-md-6 col-lg-4 mb-4">
        <div class="card report-card h-100">
            <div class="card-body text-center">
                <div class="report-icon text-info">
                    <i class="fas fa-chart-line"></i>
                </div>
                <h5 class="report-title">Reporte de Ventas</h5>
                <p class="report-description">
                    Análisis detallado de ventas por período, cliente y producto.
                </p>
                <button class="btn btn-info btn-generate" onclick="generarReporte('ventas')">
                    <i class="fas fa-download me-2"></i>Descargar PDF
                </button>
            </div>
        </div>
    </div>

    <div class="col-md-6 col-lg-4 mb-4">
        <div class="card report-card h-100">
            <div class="card-body text-center">
                <div class="report-icon text-warning">
                    <i class="fas fa-shopping-cart"></i>
                </div>
                <h5 class="report-title">Reporte de Compras</h5>
                <p class="report-description">
                    Análisis de compras por período, proveedor y producto.
                </p>
                <button class="btn btn-warning btn-generate" onclick="generarReporte('compras')">
                    <i class="fas fa-download me-2"></i>Descargar PDF
                </button>
            </div>
        </div>
    </div>

    <div class="col-md-6 col-lg-4 mb-4">
        <div class="card report-card h-100">
            <div class="card-body text-center">
                <div class="report-icon text-success">
                    <i class="fas fa-users"></i>
                </div>
                <h5 class="report-title">Reporte de Empleados</h5>
                <p class="report-description">
                    Información completa de todos los empleados registrados.
                </p>
                <button class="btn btn-success btn-generate" onclick="generarReporte('empleados')">
                    <i class="fas fa-download me-2"></i>Descargar PDF
                </button>
            </div>
        </div>
    </div>

    <div class="col-md-6 col-lg-4 mb-4">
        <div class="card report-card h-100">
            <div class="card-body text-center">
                <div class="report-icon text-danger">
                    <i class="fas fa-user-friends"></i>
                </div>
                <h5 class="report-title">Reporte de Clientes</h5>
                <p class="report-description">
                    Listado de clientes con información de contacto y compras.
                </p>
                <button class="btn btn-danger btn-generate" onclick="generarReporte('clientes')">
                    <i class="fas fa-download me-2"></i>Descargar PDF
                </button>
            </div>
        </div>
    </div>
</div>

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

