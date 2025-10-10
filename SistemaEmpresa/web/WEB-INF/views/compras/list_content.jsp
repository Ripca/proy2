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

<!-- Formulario de búsqueda -->
<div class="card mb-4">
    <div class="card-body">
        <form method="get" action="CompraServlet" class="row g-3">
            <input type="hidden" name="action" value="search">
            <div class="col-md-8">
                <input type="text" class="form-control" name="termino" 
                       placeholder="Buscar por proveedor, número de factura..." 
                       value="<%= request.getAttribute("termino") != null ? request.getAttribute("termino") : "" %>">
            </div>
            <div class="col-md-4">
                <button type="submit" class="btn btn-outline-primary me-2">
                    <i class="fas fa-search"></i> Buscar
                </button>
                <a href="CompraServlet" class="btn btn-outline-secondary">
                    <i class="fas fa-times"></i> Limpiar
                </a>
            </div>
        </form>
    </div>
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
                        for (Compra compra : compras) {
                    %>
                    <tr>
                        <td><%= compra.getIdCompra() %></td>
                        <td>
                            <strong><%= compra.getNoFactura() != null ? compra.getNoFactura() : "N/A" %></strong>
                        </td>
                        <td><%= compra.getNombreProveedor() != null ? compra.getNombreProveedor() : "Proveedor General" %></td>
                        <td><%= compra.getFecha() != null ? compra.getFecha().toString() : "N/A" %></td>
                        <td>
                            <span class="badge bg-warning text-dark">
                                Q. <%= String.format("%.2f", compra.getTotal() != null ? compra.getTotal() : 0.0) %>
                            </span>
                        </td>
                        <td>
                            <div class="btn-group" role="group">
                                <a href="CompraServlet?action=view&id=<%= compra.getIdCompra() %>" 
                                   class="btn btn-info btn-sm" title="Ver Detalle">
                                    <i class="fas fa-eye"></i>
                                </a>
                                <a href="CompraServlet?action=edit&id=<%= compra.getIdCompra() %>" 
                                   class="btn btn-warning btn-sm" title="Editar">
                                    <i class="fas fa-edit"></i>
                                </a>
                                <button type="button" class="btn btn-danger btn-sm" 
                                        onclick="confirmDelete('CompraServlet?action=delete&id=<%= compra.getIdCompra() %>', 
                                        '¿Está seguro que desea eliminar la compra No. <%= compra.getNoFactura() %>?')" 
                                        title="Eliminar">
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
