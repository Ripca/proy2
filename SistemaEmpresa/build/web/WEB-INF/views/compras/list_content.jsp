<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ page import="java.util.List" %>
<%@ page import="com.sistemaempresa.models.Compra" %>

<%
    List<Compra> compras = (List<Compra>) request.getAttribute("compras");
%>

<!-- Header de la página -->
<div class="d-flex justify-content-between align-items-center mb-4">
    <div>
        <h2 class="mb-0">Gestión de Compras</h2>
        <p class="text-muted mb-0">Administra todas las compras realizadas</p>
    </div>
    <a href="CompraServlet?action=new" class="btn btn-primary">
        <i class="fas fa-plus"></i> Nueva Compra
    </a>
</div>



<!-- Tabla de compras -->
<div class="card">
    <div class="card-header">
        <h5 class="mb-0">
            <i class="fas fa-list"></i> Lista de Compras
            <span class="badge bg-primary ms-2">
                <%= compras != null ? compras.size() : 0 %> registros
            </span>
        </h5>
    </div>
    <div class="card-body">
        <%
            if (compras != null && !compras.isEmpty()) {
        %>
        <div class="table-responsive">
            <table class="table table-striped table-hover data-table">
                <thead class="table-dark">
                    <tr>
                        <th>ID</th>
                        <th>No. Factura</th>
                        <th>Proveedor</th>
                        <th>Fecha</th>
                        <th>Total</th>
                        <th>Acciones</th>
                    </tr>
                </thead>
                <tbody>
                    <%
                        if (compras != null && !compras.isEmpty()) {
                            for (Compra compra : compras) {
                    %>
                    <tr>
                        <td><%= compra.getIdCompra() %></td>
                        <td>
                            <strong><%= String.valueOf(compra.getNoOrdenCompra()) %></strong>
                        </td>
                        <td><%= compra.getNombreProveedor() != null ? compra.getNombreProveedor() : "Proveedor General" %></td>
                        <td><%= compra.getFechaOrden() != null ? compra.getFechaOrden().toString() : "N/A" %></td>
                        <td>
                            <span class="badge bg-warning text-dark">
                                Q. <%= String.format("%.2f", compra.getTotal()) %>
                            </span>
                        </td>
                        <td>
                            <div class="btn-group" role="group">
                                <a href="CompraServlet?action=edit&id=<%= compra.getIdCompra() %>" 
                                   class="btn btn-warning btn-sm" title="Editar">
                                    <i class="fas fa-edit"></i>
                                </a>
                                <button type="button" class="btn btn-danger btn-sm" 
                                        onclick="confirmDelete('CompraServlet?action=delete&id=<%= compra.getIdCompra() %>', 
                                        '¿Está seguro que desea eliminar la compra No. <%= compra.getNoOrdenCompra() %>?')"
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
            <i class="fas fa-shopping-bag fa-3x text-muted mb-3"></i>
            <h5 class="text-muted">No hay compras registradas</h5>
            <p class="text-muted">Haz clic en "Nueva Compra" para registrar la primera compra.</p>
            <a href="CompraServlet?action=new" class="btn btn-primary">
                <i class="fas fa-plus"></i> Crear Primera Compra
            </a>
        </div>
        <%
            }
        %>
    </div>
</div>
