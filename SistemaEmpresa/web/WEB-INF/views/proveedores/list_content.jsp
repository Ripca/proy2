<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ page import="java.util.List" %>
<%@ page import="com.sistemaempresa.models.Proveedor" %>

<%
    List<Proveedor> proveedores = (List<Proveedor>) request.getAttribute("proveedores");
%>

<!-- Header de la página -->
<div class="d-flex justify-content-between align-items-center mb-4">
    <div>
        <h2 class="mb-0">Gestión de Proveedores</h2>
        <p class="text-muted mb-0">Administra la información de todos los proveedores</p>
    </div>
    <a href="ProveedorServlet?action=new" class="btn btn-primary">
        <i class="fas fa-plus"></i> Nuevo Proveedor
    </a>
</div>

<!-- Formulario de búsqueda -->
<div class="card mb-4">
    <div class="card-body">
        <form method="get" action="ProveedorServlet" class="row g-3">
            <input type="hidden" name="action" value="search">
            <div class="col-md-8">
                <input type="text" class="form-control" name="termino" 
                       placeholder="Buscar por nombre, NIT, teléfono..." 
                       value="<%= request.getAttribute("termino") != null ? request.getAttribute("termino") : "" %>">
            </div>
            <div class="col-md-4">
                <button type="submit" class="btn btn-outline-primary me-2">
                    <i class="fas fa-search"></i> Buscar
                </button>
                <a href="ProveedorServlet" class="btn btn-outline-secondary">
                    <i class="fas fa-times"></i> Limpiar
                </a>
            </div>
        </form>
    </div>
</div>

<!-- Tabla de proveedores -->
<div class="card">
    <div class="card-header">
        <h5 class="mb-0">
            <i class="fas fa-list"></i> Lista de Proveedores
            <span class="badge bg-primary ms-2">
                <%= proveedores != null ? proveedores.size() : 0 %> registros
            </span>
        </h5>
    </div>
    <div class="card-body">
        <%
            if (proveedores != null && !proveedores.isEmpty()) {
        %>
        <div class="table-responsive">
            <table class="table table-striped table-hover data-table">
                <thead class="table-dark">
                    <tr>
                        <th>ID</th>
                        <th>Proveedor</th>
                        <th>NIT</th>
                        <th>Teléfono</th>
                        <th>Dirección</th>
                        <th>Fecha Ingreso</th>
                        <th>Acciones</th>
                    </tr>
                </thead>
                <tbody>
                    <%
                        for (Proveedor proveedor : proveedores) {
                    %>
                    <tr>
                        <td><%= proveedor.getIdProveedor() %></td>
                        <td>
                            <strong><%= proveedor.getProveedor() %></strong>
                        </td>
                        <td><%= proveedor.getNit() != null ? proveedor.getNit() : "N/A" %></td>
                        <td><%= proveedor.getTelefono() != null ? proveedor.getTelefono() : "N/A" %></td>
                        <td><%= proveedor.getDireccion() != null ? proveedor.getDireccion() : "N/A" %></td>
                        <td><%= proveedor.getFechaIngreso() != null ? proveedor.getFechaIngreso().toString() : "N/A" %></td>
                        <td>
                            <div class="btn-group" role="group">
                                <a href="ProveedorServlet?action=edit&id=<%= proveedor.getIdProveedor() %>" 
                                   class="btn btn-warning btn-sm" title="Editar">
                                    <i class="fas fa-edit"></i>
                                </a>
                                <button type="button" class="btn btn-danger btn-sm" 
                                        onclick="confirmDelete('ProveedorServlet?action=delete&id=<%= proveedor.getIdProveedor() %>', 
                                        '¿Está seguro que desea eliminar el proveedor <%= proveedor.getProveedor() %>?')" 
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
            <i class="fas fa-truck fa-3x text-muted mb-3"></i>
            <h5 class="text-muted">No hay proveedores registrados</h5>
            <p class="text-muted">Haz clic en "Nuevo Proveedor" para agregar el primer proveedor.</p>
            <a href="ProveedorServlet?action=new" class="btn btn-primary">
                <i class="fas fa-plus"></i> Crear Primer Proveedor
            </a>
        </div>
        <%
            }
        %>
    </div>
</div>
