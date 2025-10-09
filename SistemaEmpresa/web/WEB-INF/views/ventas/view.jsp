<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ page import="com.sistemaempresa.models.*" %>
<%@ page import="java.time.format.DateTimeFormatter" %>
<%@ page import="java.text.DecimalFormat" %>

<%
    Venta venta = (Venta) request.getAttribute("venta");
    DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy");
    DecimalFormat df = new DecimalFormat("#,##0.00");
%>

<!DOCTYPE html>
<html lang="es">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>Ver Venta - Sistema Empresa</title>
    <link href="https://cdn.jsdelivr.net/npm/bootstrap@5.3.0/dist/css/bootstrap.min.css" rel="stylesheet">
    <link href="https://cdnjs.cloudflare.com/ajax/libs/font-awesome/6.0.0/css/all.min.css" rel="stylesheet">
</head>
<body>
    <div class="container-fluid">
        <div class="row">
            <div class="col-12">
                <div class="d-flex justify-content-between align-items-center mb-4">
                    <h2><i class="fas fa-eye"></i> Ver Venta</h2>
                    <div>
                        <a href="VentaServlet?action=edit&id=<%= venta.getIdVenta() %>" class="btn btn-warning">
                            <i class="fas fa-edit"></i> Editar
                        </a>
                        <a href="VentaServlet" class="btn btn-secondary">
                            <i class="fas fa-arrow-left"></i> Volver
                        </a>
                    </div>
                </div>

                <!-- Información de la Venta -->
                <div class="card mb-4">
                    <div class="card-header">
                        <h5 class="mb-0"><i class="fas fa-file-invoice"></i> Información de la Venta</h5>
                    </div>
                    <div class="card-body">
                        <div class="row">
                            <div class="col-md-6">
                                <table class="table table-borderless">
                                    <tr>
                                        <th width="40%">No. Factura:</th>
                                        <td><%= venta.getNoFactura() %></td>
                                    </tr>
                                    <tr>
                                        <th>Serie:</th>
                                        <td><%= venta.getSerie() %></td>
                                    </tr>
                                    <tr>
                                        <th>Fecha Factura:</th>
                                        <td><%= venta.getFechaFactura().format(formatter) %></td>
                                    </tr>
                                    <tr>
                                        <th>Fecha Ingreso:</th>
                                        <td><%= venta.getFechaIngreso().format(formatter) %></td>
                                    </tr>
                                </table>
                            </div>
                            <div class="col-md-6">
                                <table class="table table-borderless">
                                    <tr>
                                        <th width="40%">Cliente:</th>
                                        <td><%= venta.getNombreCliente() != null ? venta.getNombreCliente() : "N/A" %></td>
                                    </tr>
                                    <tr>
                                        <th>Empleado:</th>
                                        <td><%= venta.getNombreEmpleado() != null ? venta.getNombreEmpleado() : "N/A" %></td>
                                    </tr>
                                    <tr>
                                        <th>Total:</th>
                                        <td class="h5 text-success">Q. <%= df.format(venta.getTotal()) %></td>
                                    </tr>
                                </table>
                            </div>
                        </div>
                    </div>
                </div>

                <!-- Detalles de la Venta -->
                <div class="card">
                    <div class="card-header">
                        <h5 class="mb-0"><i class="fas fa-list"></i> Detalles de la Venta</h5>
                    </div>
                    <div class="card-body">
                        <%
                            if (venta.tieneDetalles()) {
                        %>
                        <div class="table-responsive">
                            <table class="table table-striped">
                                <thead class="table-dark">
                                    <tr>
                                        <th>Producto</th>
                                        <th>Marca</th>
                                        <th class="text-center">Cantidad</th>
                                        <th class="text-end">Precio Unitario</th>
                                        <th class="text-end">Subtotal</th>
                                    </tr>
                                </thead>
                                <tbody>
                                    <%
                                        for (VentaDetalle detalle : venta.getDetalles()) {
                                    %>
                                    <tr>
                                        <td><%= detalle.getNombreProducto() != null ? detalle.getNombreProducto() : "N/A" %></td>
                                        <td><%= detalle.getMarcaProducto() != null ? detalle.getMarcaProducto() : "N/A" %></td>
                                        <td class="text-center"><%= detalle.getCantidad() %></td>
                                        <td class="text-end">Q. <%= df.format(detalle.getPrecioUnitario()) %></td>
                                        <td class="text-end">Q. <%= df.format(detalle.calcularSubtotal()) %></td>
                                    </tr>
                                    <%
                                        }
                                    %>
                                </tbody>
                                <tfoot class="table-info">
                                    <tr>
                                        <th colspan="4" class="text-end">Total:</th>
                                        <th class="text-end">Q. <%= df.format(venta.getTotal()) %></th>
                                    </tr>
                                </tfoot>
                            </table>
                        </div>
                        <%
                            } else {
                        %>
                        <div class="text-center py-4">
                            <i class="fas fa-box-open fa-3x text-muted mb-3"></i>
                            <h5 class="text-muted">No hay detalles para esta venta</h5>
                        </div>
                        <%
                            }
                        %>
                    </div>
                </div>
            </div>
        </div>
    </div>

    <script src="https://cdn.jsdelivr.net/npm/bootstrap@5.3.0/dist/js/bootstrap.bundle.min.js"></script>
</body>
</html>
