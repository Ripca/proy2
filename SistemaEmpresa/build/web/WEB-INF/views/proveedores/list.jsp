<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@page import="java.util.List"%>
<%@page import="com.sistemaempresa.models.Proveedor"%>
<!DOCTYPE html>
<html lang="es">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>Gestión de Proveedores - Sistema Empresa</title>
    <link href="https://cdn.jsdelivr.net/npm/bootstrap@5.3.0/dist/css/bootstrap.min.css" rel="stylesheet">
    <link href="https://cdnjs.cloudflare.com/ajax/libs/font-awesome/6.4.0/css/all.min.css" rel="stylesheet">
</head>
<body class="bg-light">
    <!-- Navegación -->
    <nav class="navbar navbar-expand-lg navbar-dark bg-dark">
        <div class="container-fluid">
            <a class="navbar-brand" href="DashboardServlet">
                <i class="fas fa-building"></i> Sistema Empresa
            </a>
            <div class="navbar-nav ms-auto">
                <a class="nav-link" href="DashboardServlet">
                    <i class="fas fa-home"></i> Dashboard
                </a>
                <a class="nav-link" href="LoginServlet?action=logout">
                    <i class="fas fa-sign-out-alt"></i> Cerrar Sesión
                </a>
            </div>
        </div>
    </nav>

    <div class="container-fluid mt-4">
        <div class="row">
            <div class="col-12">
                <!-- Header -->
                <div class="d-flex justify-content-between align-items-center mb-4">
                    <h1><i class="fas fa-truck"></i> Gestión de Proveedores</h1>
                    <a href="ProveedorServlet?action=new" class="btn btn-primary">
                        <i class="fas fa-plus"></i> Nuevo Proveedor
                    </a>
                </div>

                <!-- Mensajes -->
                <% if (request.getParameter("success") != null) { %>
                    <div class="alert alert-success alert-dismissible fade show" role="alert">
                        <i class="fas fa-check-circle"></i> <%= request.getParameter("success") %>
                        <button type="button" class="btn-close" data-bs-dismiss="alert"></button>
                    </div>
                <% } %>
                
                <% if (request.getParameter("error") != null) { %>
                    <div class="alert alert-danger alert-dismissible fade show" role="alert">
                        <i class="fas fa-exclamation-circle"></i> <%= request.getParameter("error") %>
                        <button type="button" class="btn-close" data-bs-dismiss="alert"></button>
                    </div>
                <% } %>

                <!-- Búsqueda -->
                <div class="card mb-4">
                    <div class="card-body">
                        <form method="GET" action="ProveedorServlet" class="row g-3">
                            <input type="hidden" name="action" value="search">
                            <div class="col-md-10">
                                <input type="text" class="form-control" name="termino" 
                                       placeholder="Buscar por nombre o NIT..." 
                                       value="<%= request.getParameter("termino") != null ? request.getParameter("termino") : "" %>">
                            </div>
                            <div class="col-md-2">
                                <button type="submit" class="btn btn-outline-primary w-100">
                                    <i class="fas fa-search"></i> Buscar
                                </button>
                            </div>
                        </form>
                    </div>
                </div>

                <!-- Tabla de Proveedores -->
                <div class="card">
                    <div class="card-header">
                        <h5 class="mb-0">Lista de Proveedores</h5>
                    </div>
                    <div class="card-body">
                        <%
                            List<Proveedor> proveedores = (List<Proveedor>) request.getAttribute("proveedores");
                            if (proveedores != null && !proveedores.isEmpty()) {
                        %>
                        <div class="table-responsive">
                            <table class="table table-striped table-hover">
                                <thead class="table-dark">
                                    <tr>
                                        <th>ID</th>
                                        <th>Proveedor</th>
                                        <th>NIT</th>
                                        <th>Dirección</th>
                                        <th>Teléfono</th>
                                        <th>Acciones</th>
                                    </tr>
                                </thead>
                                <tbody>
                                    <%
                                        for (Proveedor proveedor : proveedores) {
                                    %>
                                    <tr>
                                        <td><%= proveedor.getIdProveedor() %></td>
                                        <td><strong><%= proveedor.getProveedor() %></strong></td>
                                        <td><%= proveedor.getNit() %></td>
                                        <td><%= proveedor.getDireccion() != null ? proveedor.getDireccion() : "" %></td>
                                        <td><%= proveedor.getTelefono() != null ? proveedor.getTelefono() : "" %></td>
                                        <td>
                                            <div class="btn-group" role="group">
                                                <a href="ProveedorServlet?action=edit&id=<%= proveedor.getIdProveedor() %>" 
                                                   class="btn btn-sm btn-outline-primary" title="Editar">
                                                    <i class="fas fa-edit"></i>
                                                </a>
                                                <button type="button" class="btn btn-sm btn-outline-danger" 
                                                        onclick="confirmarEliminacion(<%= proveedor.getIdProveedor() %>, '<%= proveedor.getProveedor() %>')" 
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
                        <div class="text-center py-4">
                            <i class="fas fa-truck fa-3x text-muted mb-3"></i>
                            <h5 class="text-muted">No hay proveedores registrados</h5>
                            <p class="text-muted">Comience agregando un nuevo proveedor</p>
                            <a href="ProveedorServlet?action=new" class="btn btn-primary">
                                <i class="fas fa-plus"></i> Agregar Primer Proveedor
                            </a>
                        </div>
                        <%
                            }
                        %>
                    </div>
                </div>
            </div>
        </div>
    </div>

    <!-- Modal de Confirmación -->
    <div class="modal fade" id="deleteModal" tabindex="-1">
        <div class="modal-dialog">
            <div class="modal-content">
                <div class="modal-header">
                    <h5 class="modal-title">Confirmar Eliminación</h5>
                    <button type="button" class="btn-close" data-bs-dismiss="modal"></button>
                </div>
                <div class="modal-body">
                    <p>¿Está seguro que desea eliminar al proveedor <strong id="proveedorNombre"></strong>?</p>
                    <p class="text-danger"><small>Esta acción no se puede deshacer.</small></p>
                </div>
                <div class="modal-footer">
                    <button type="button" class="btn btn-secondary" data-bs-dismiss="modal">Cancelar</button>
                    <a href="#" id="confirmarEliminar" class="btn btn-danger">Eliminar</a>
                </div>
            </div>
        </div>
    </div>

    <script src="https://cdn.jsdelivr.net/npm/bootstrap@5.3.0/dist/js/bootstrap.bundle.min.js"></script>
    <script>
        function confirmarEliminacion(id, nombre) {
            document.getElementById('proveedorNombre').textContent = nombre;
            document.getElementById('confirmarEliminar').href = 'ProveedorServlet?action=delete&id=' + id;
            new bootstrap.Modal(document.getElementById('deleteModal')).show();
        }
    </script>
</body>
</html>
