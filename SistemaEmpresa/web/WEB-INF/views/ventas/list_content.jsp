<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ page import="java.util.List" %>
<%@ page import="com.sistemaempresa.models.Venta" %>

<%
    List<Venta> ventas = (List<Venta>) request.getAttribute("ventas");
%>

<!-- Header de la página -->
<div class="d-flex justify-content-between align-items-center mb-4">
    <div>
        <h2 class="mb-0">Gestión de Ventas</h2>
        <p class="text-muted mb-0">Administra todas las ventas realizadas</p>
    </div>
    <a href="VentaServlet?action=new" class="btn btn-primary">
        <i class="fas fa-plus"></i> Nueva Venta
    </a>
</div>



<!-- Tabla de ventas -->
<div class="card">
    <div class="card-header">
        <h5 class="mb-0">
            <i class="fas fa-list"></i> Lista de Ventas
            <span class="badge bg-primary ms-2">
                <%= ventas != null ? ventas.size() : 0 %> registros
            </span>
        </h5>
    </div>
    <div class="card-body">
        <%
            if (ventas != null && !ventas.isEmpty()) {
        %>
        <div class="table-responsive">
            <table class="table table-striped table-hover data-table">
                <thead class="table-dark">
                    <tr>
                        <th>ID</th>
                        <th>No. Factura</th>
                        <th>Cliente</th>
                        <th>Fecha</th>
                        <th>Total</th>
                        <th>Acciones</th>
                    </tr>
                </thead>
                <tbody>
                    <%
                        if (ventas != null && !ventas.isEmpty()) {
                            for (Venta venta : ventas) {
                    %>
                    <tr>
                        <td><%= venta.getIdVenta() %></td>
                        <td>
                            <strong><%= venta.getNoFactura() %></strong>
                        </td>
                        <td><%= venta.getNombreCliente() != null ? venta.getNombreCliente() : "Cliente General" %></td>
                        <td><%= venta.getFechaFactura() != null ? venta.getFechaFactura().toString() : "N/A" %></td>
                        <td>
                            <span class="badge bg-success">
                                Q. <%= String.format("%.2f", venta.getTotal()) %>
                            </span>
                        </td>
                        <td>
                            <div class="btn-group" role="group">
                                <a href="VentaServlet?action=edit&id=<%= venta.getIdVenta() %>" 
                                   class="btn btn-warning btn-sm" title="Editar">
                                    <i class="fas fa-edit"></i>
                                </a>
                                <button type="button" class="btn btn-danger btn-sm" 
                                        onclick="confirmDelete('VentaServlet?action=delete&id=<%= venta.getIdVenta() %>', 
                                        '¿Está seguro que desea eliminar la venta No. <%= venta.getNoFactura() %>?')" 
                                        title="Eliminar">
                                    <i class="fas fa-trash"></i>
                                </button>
                            </div>
                        </td>
                    </tr>
                    <%
                            }
                        }
                    %>
                </tbody>
            </table>
        </div>
        <%
            } else {
        %>
        <div class="text-center py-5">
            <i class="fas fa-shopping-cart fa-3x text-muted mb-3"></i>
            <h5 class="text-muted">No hay ventas registradas</h5>
            <p class="text-muted">Haz clic en "Nueva Venta" para registrar la primera venta.</p>
            <a href="VentaServlet?action=new" class="btn btn-primary">
                <i class="fas fa-plus"></i> Crear Primera Venta
            </a>
        </div>
        <%
            }
        %>
    </div>
</div>
