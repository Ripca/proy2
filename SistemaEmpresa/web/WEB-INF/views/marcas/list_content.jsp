<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ page import="java.util.List" %>
<%@ page import="com.sistemaempresa.models.Marca" %>

<%
    List<Marca> marcas = (List<Marca>) request.getAttribute("marcas");
%>

<!-- Header de la página -->
<div class="d-flex justify-content-between align-items-center mb-4">
    <div>
        <h2 class="mb-0">Gestión de Marcas</h2>
        <p class="text-muted mb-0">Administra las marcas de productos</p>
    </div>
    <a href="MarcaServlet?action=new" class="btn btn-primary">
        <i class="fas fa-plus"></i> Nueva Marca
    </a>
</div>



<!-- Tabla de marcas -->
<div class="card">
    <div class="card-header">
        <h5 class="mb-0">
            <i class="fas fa-list"></i> Lista de Marcas
            <span class="badge bg-primary ms-2">
                <%= marcas != null ? marcas.size() : 0 %> registros
            </span>
        </h5>
    </div>
    <div class="card-body">
        <%
            if (marcas != null && !marcas.isEmpty()) {
        %>
        <div class="table-responsive">
            <table class="table table-striped table-hover data-table">
                <thead class="table-dark">
                    <tr>
                        <th>ID</th>
                        <th>Marca</th>
                        <th>Acciones</th>
                    </tr>
                </thead>
                <tbody>
                    <%
                        if (marcas != null && !marcas.isEmpty()) {
                            for (Marca marca : marcas) {
                    %>
                    <tr>
                        <td><%= marca.getIdMarca() %></td>
                        <td>
                            <strong><%= marca.getMarca() %></strong>
                        </td>
                        <td>
                            <a href="MarcaServlet?action=edit&id=<%= marca.getIdMarca() %>" class="btn btn-sm btn-warning">
                                <i class="fas fa-edit"></i> Editar
                            </a>
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
            <i class="fas fa-tags fa-3x text-muted mb-3"></i>
            <h5 class="text-muted">No hay marcas registradas</h5>
            <p class="text-muted">Haz clic en "Nueva Marca" para agregar la primera marca.</p>
            <a href="MarcaServlet?action=new" class="btn btn-primary">
                <i class="fas fa-plus"></i> Crear Primera Marca
            </a>
        </div>
        <%
            }
        %>
    </div>
</div>
