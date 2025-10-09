<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ page import="java.util.List" %>
<%@ page import="com.sistemaempresa.models.Venta" %>
<%@ page import="java.time.format.DateTimeFormatter" %>
<%@ page import="java.text.DecimalFormat" %>

<%
    List<Venta> ventas = (List<Venta>) request.getAttribute("ventas");
    DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy");
    DecimalFormat df = new DecimalFormat("#,##0.00");
%>

<!DOCTYPE html>
<html lang="es">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>Gestión de Ventas - Sistema Empresa</title>
    <link href="https://cdn.jsdelivr.net/npm/bootstrap@5.3.0/dist/css/bootstrap.min.css" rel="stylesheet">
    <link href="https://cdnjs.cloudflare.com/ajax/libs/font-awesome/6.0.0/css/all.min.css" rel="stylesheet">
</head>
<body>
    <div class="container-fluid">
        <div class="row">
            <div class="col-12">
                <div class="d-flex justify-content-between align-items-center mb-4">
                    <h2><i class="fas fa-shopping-cart"></i> Gestión de Ventas</h2>
                    <a href="VentaServlet?action=new" class="btn btn-primary">
                        <i class="fas fa-plus"></i> Nueva Venta
                    </a>
                </div>

                <!-- Mensajes de éxito/error -->
                <%
                    String success = request.getParameter("success");
                    String error = request.getParameter("error");
                    if (success != null) {
                %>
                <div class="alert alert-success alert-dismissible fade show" role="alert">
                    <i class="fas fa-check-circle"></i> <%= success %>
                    <button type="button" class="btn-close" data-bs-dismiss="alert"></button>
                </div>
                <%
                    }
                    if (error != null) {
                %>
                <div class="alert alert-danger alert-dismissible fade show" role="alert">
                    <i class="fas fa-exclamation-circle"></i> <%= error %>
                    <button type="button" class="btn-close" data-bs-dismiss="alert"></button>
                </div>
                <%
                    }
                %>

                <!-- Tabla de ventas -->
                <div class="card">
                    <div class="card-header">
                        <h5 class="mb-0"><i class="fas fa-list"></i> Lista de Ventas</h5>
                    </div>
                    <div class="card-body">
                        <%
                            if (ventas != null && !ventas.isEmpty()) {
                        %>
                        <div class="table-responsive">
                            <table class="table table-striped table-hover">
                                <thead class="table-dark">
                                    <tr>
                                        <th>No. Factura</th>
                                        <th>Serie</th>
                                        <th>Fecha Factura</th>
                                        <th>Cliente</th>
                                        <th>Empleado</th>
                                        <th>Total</th>
                                        <th>Fecha Ingreso</th>
                                        <th>Acciones</th>
                                    </tr>
                                </thead>
                                <tbody>
                                    <%
                                        for (Venta venta : ventas) {
                                    %>
                                    <tr>
                                        <td><%= venta.getNoFactura() %></td>
                                        <td><%= venta.getSerie() %></td>
                                        <td><%= venta.getFechaFactura().format(formatter) %></td>
                                        <td><%= venta.getNombreCliente() != null ? venta.getNombreCliente() : "N/A" %></td>
                                        <td><%= venta.getNombreEmpleado() != null ? venta.getNombreEmpleado() : "N/A" %></td>
                                        <td>Q. <%= df.format(venta.getTotal()) %></td>
                                        <td><%= venta.getFechaIngreso().format(formatter) %></td>
                                        <td>
                                            <div class="btn-group" role="group">
                                                <a href="VentaServlet?action=view&id=<%= venta.getIdVenta() %>" 
                                                   class="btn btn-info btn-sm" title="Ver">
                                                    <i class="fas fa-eye"></i>
                                                </a>
                                                <a href="VentaServlet?action=edit&id=<%= venta.getIdVenta() %>" 
                                                   class="btn btn-warning btn-sm" title="Editar">
                                                    <i class="fas fa-edit"></i>
                                                </a>
                                                <button type="button" class="btn btn-danger btn-sm" 
                                                        onclick="confirmarEliminacion(<%= venta.getIdVenta() %>)" title="Eliminar">
                                                    <i class="fas fa-trash"></i>
                                                </button>
                                            </div>
                                        </td>
                                    </tr>
                                    <%
                                        }
                                    %>
                                </tbody>
                            </table>
                        </div>
                        <%
                            } else {
                        %>
                        <div class="text-center py-4">
                            <i class="fas fa-shopping-cart fa-3x text-muted mb-3"></i>
                            <h5 class="text-muted">No hay ventas registradas</h5>
                            <p class="text-muted">Haz clic en "Nueva Venta" para agregar la primera venta.</p>
                        </div>
                        <%
                            }
                        %>
                    </div>
                </div>
            </div>
        </div>
    </div>

    <!-- Modal de confirmación para eliminar -->
    <div class="modal fade" id="modalEliminar" tabindex="-1">
        <div class="modal-dialog">
            <div class="modal-content">
                <div class="modal-header">
                    <h5 class="modal-title">Confirmar Eliminación</h5>
                    <button type="button" class="btn-close" data-bs-dismiss="modal"></button>
                </div>
                <div class="modal-body">
                    ¿Está seguro que desea eliminar esta venta? Esta acción no se puede deshacer.
                </div>
                <div class="modal-footer">
                    <button type="button" class="btn btn-secondary" data-bs-dismiss="modal">Cancelar</button>
                    <a href="#" id="btnConfirmarEliminar" class="btn btn-danger">Eliminar</a>
                </div>
            </div>
        </div>
    </div>

    <script src="https://cdn.jsdelivr.net/npm/bootstrap@5.3.0/dist/js/bootstrap.bundle.min.js"></script>
    <script>
        function confirmarEliminacion(idVenta) {
            document.getElementById('btnConfirmarEliminar').href = 'VentaServlet?action=delete&id=' + idVenta;
            new bootstrap.Modal(document.getElementById('modalEliminar')).show();
        }
    </script>
</body>
</html>
