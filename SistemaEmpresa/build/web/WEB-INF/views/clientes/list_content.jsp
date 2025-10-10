<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ page import="java.util.List" %>
<%@ page import="com.sistemaempresa.models.Cliente" %>

<%
    List<Cliente> clientes = (List<Cliente>) request.getAttribute("clientes");
%>

<!-- Header de la página -->
<div class="d-flex justify-content-between align-items-center mb-4">
    <div>
        <h2 class="mb-0">Gestión de Clientes</h2>
        <p class="text-muted mb-0">Administra la información de todos los clientes</p>
    </div>
    <a href="ClienteServlet?action=new" class="btn btn-primary">
        <i class="fas fa-plus"></i> Nuevo Cliente
    </a>
</div>



<!-- Tabla de clientes -->
<div class="card">
    <div class="card-header">
        <h5 class="mb-0">
            <i class="fas fa-list"></i> Lista de Clientes
            <span class="badge bg-primary ms-2">
                <%= clientes != null ? clientes.size() : 0 %> registros
            </span>
        </h5>
    </div>
    <div class="card-body">
        <%
            if (clientes != null && !clientes.isEmpty()) {
        %>
        <div class="table-responsive">
            <table class="table table-striped table-hover data-table">
                <thead class="table-dark">
                    <tr>
                        <th>ID</th>
                        <th>Cliente</th>
                        <th>NIT</th>
                        <th>Teléfono</th>
                        <th>Email</th>
                        <th>Fecha Ingreso</th>
                        <th>Acciones</th>
                    </tr>
                </thead>
                <tbody>
                    <%
                        for (Cliente cliente : clientes) {
                    %>
                    <tr>
                        <td><%= cliente.getIdCliente() %></td>
                        <td>
                            <strong><%= cliente.getNombreCompleto() %></strong>
                        </td>
                        <td><%= cliente.getNit() != null ? cliente.getNit() : "N/A" %></td>
                        <td><%= cliente.getTelefono() != null ? cliente.getTelefono() : "N/A" %></td>
                        <td><%= cliente.getCorreoElectronico() != null ? cliente.getCorreoElectronico() : "N/A" %></td>
                        <td><%= cliente.getFechaIngreso() != null ? cliente.getFechaIngreso().toString() : "N/A" %></td>
                        <td>
                            <div class="btn-group" role="group">
                                <a href="ClienteServlet?action=edit&id=<%= cliente.getIdCliente() %>" 
                                   class="btn btn-warning btn-sm" title="Editar">
                                    <i class="fas fa-edit"></i>
                                </a>
                                <button type="button" class="btn btn-danger btn-sm" 
                                        onclick="confirmDelete('ClienteServlet?action=delete&id=<%= cliente.getIdCliente() %>', 
                                        '¿Está seguro que desea eliminar el cliente <%= cliente.getNombreCompleto() %>?')"
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
            <i class="fas fa-users fa-3x text-muted mb-3"></i>
            <h5 class="text-muted">No hay clientes registrados</h5>
            <p class="text-muted">Haz clic en "Nuevo Cliente" para agregar el primer cliente.</p>
            <a href="ClienteServlet?action=new" class="btn btn-primary">
                <i class="fas fa-plus"></i> Crear Primer Cliente
            </a>
        </div>
        <%
            }
        %>
    </div>
</div>
