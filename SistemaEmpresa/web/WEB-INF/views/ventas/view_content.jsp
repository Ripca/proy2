<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ page import="com.sistemaempresa.models.*" %>

<%
    Venta venta = (Venta) request.getAttribute("venta");
%>

<!-- Header de la página -->
<div class="d-flex justify-content-between align-items-center mb-4">
    <div>
        <h2 class="mb-0">Detalle de Venta</h2>
        <p class="text-muted mb-0">Información completa de la venta</p>
    </div>
    <div>
        <a href="VentaServlet?action=edit&id=<%= venta.getIdVenta() %>" class="btn btn-warning me-2">
            <i class="fas fa-edit"></i> Editar
        </a>
        <a href="VentaServlet" class="btn btn-secondary">
            <i class="fas fa-arrow-left"></i> Volver a la Lista
        </a>
    </div>
</div>

<div class="row">
    <!-- Información de la Venta -->
    <div class="col-md-6">
        <div class="card">
            <div class="card-header">
                <h5 class="mb-0"><i class="fas fa-info-circle"></i> Información General</h5>
            </div>
            <div class="card-body">
                <table class="table table-borderless">
                    <tr>
                        <td><strong>ID Venta:</strong></td>
                        <td><%= venta.getIdVenta() %></td>
                    </tr>
                    <tr>
                        <td><strong>No. Factura:</strong></td>
                        <td><%= venta.getNoFactura() != null ? venta.getNoFactura() : "N/A" %></td>
                    </tr>
                    <tr>
                        <td><strong>Fecha:</strong></td>
                        <td><%= venta.getFechaFactura() != null ? venta.getFechaFactura().toString() : "N/A" %></td>
                    </tr>
                    <tr>
                        <td><strong>Cliente:</strong></td>
                        <td><%= venta.getNombreCliente() != null ? venta.getNombreCliente() : "Cliente General" %></td>
                    </tr>
                    <tr>
                        <td><strong>Total:</strong></td>
                        <td>
                            <span class="badge bg-success fs-6">
                                Q. <%= String.format("%.2f", venta.getTotal()) %>
                            </span>
                        </td>
                    </tr>
                </table>
            </div>
        </div>
    </div>

    <!-- Resumen -->
    <div class="col-md-6">
        <div class="card">
            <div class="card-header">
                <h5 class="mb-0"><i class="fas fa-chart-bar"></i> Resumen</h5>
            </div>
            <div class="card-body">
                <%
                    int totalProductos = 0;
                    double totalCantidad = 0;
                    if (venta.getDetalles() != null) {
                        totalProductos = venta.getDetalles().size();
                        for (VentaDetalle detalle : venta.getDetalles()) {
                            totalCantidad += detalle.getCantidad();
                        }
                    }
                %>
                <div class="row text-center">
                    <div class="col-6">
                        <div class="border-end">
                            <h4 class="text-primary"><%= totalProductos %></h4>
                            <small class="text-muted">Productos Diferentes</small>
                        </div>
                    </div>
                    <div class="col-6">
                        <h4 class="text-info"><%= (int)totalCantidad %></h4>
                        <small class="text-muted">Cantidad Total</small>
                    </div>
                </div>
            </div>
        </div>
    </div>
</div>

<!-- Detalle de Productos -->
<div class="card mt-4">
    <div class="card-header">
        <h5 class="mb-0"><i class="fas fa-list"></i> Detalle de Productos</h5>
    </div>
    <div class="card-body">
        <%
            if (venta.getDetalles() != null && !venta.getDetalles().isEmpty()) {
        %>
        <div class="table-responsive">
            <table class="table table-striped table-hover">
                <thead class="table-dark">
                    <tr>
                        <th>Producto</th>
                        <th>Cantidad</th>
                        <th>Precio Unitario</th>
                        <th>Subtotal</th>
                    </tr>
                </thead>
                <tbody>
                    <%
                        for (VentaDetalle detalle : venta.getDetalles()) {
                    %>
                    <tr>
                        <td>
                            <strong><%= detalle.getNombreProducto() != null ? detalle.getNombreProducto() : "Producto N/A" %></strong>
                        </td>
                        <td>
                            <span class="badge bg-primary"><%= detalle.getCantidad() %></span>
                        </td>
                        <td>Q. <%= String.format("%.2f", detalle.getPrecio()) %></td>
                        <td>
                            <strong>Q. <%= String.format("%.2f", detalle.getSubtotal()) %></strong>
                        </td>
                    </tr>
                    <%
                        }
                    %>
                </tbody>
                <tfoot class="table-dark">
                    <tr>
                        <th colspan="3" class="text-end">TOTAL:</th>
                        <th>
                            <span class="text-success">
                                Q. <%= String.format("%.2f", venta.getTotal()) %>
                            </span>
                        </th>
                    </tr>
                </tfoot>
            </table>
        </div>
        <%
            } else {
        %>
        <div class="text-center py-4">
            <i class="fas fa-box-open fa-3x text-muted mb-3"></i>
            <h5 class="text-muted">No hay productos en esta venta</h5>
        </div>
        <%
            }
        %>
    </div>
</div>

<!-- Acciones -->
<div class="card mt-4">
    <div class="card-body">
        <div class="d-flex justify-content-between align-items-center">
            <div>
                <h6 class="mb-0">Acciones Disponibles</h6>
                <small class="text-muted">Operaciones que puede realizar con esta venta</small>
            </div>
            <div>
                <a href="VentaServlet?action=edit&id=<%= venta.getIdVenta() %>" class="btn btn-warning">
                    <i class="fas fa-edit"></i> Editar Venta
                </a>
                <button type="button" class="btn btn-danger" 
                        onclick="confirmDelete('VentaServlet?action=delete&id=<%= venta.getIdVenta() %>', 
                        '¿Está seguro que desea eliminar la venta No. <%= venta.getNoFactura() %>?')">
                    <i class="fas fa-trash"></i> Eliminar Venta
                </button>
            </div>
        </div>
    </div>
</div>
