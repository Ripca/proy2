<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@page import="java.util.List"%>
<%@page import="com.sistemaempresa.models.Cliente"%>
<%@page import="com.sistemaempresa.utils.JSTLAlternative"%>
<!DOCTYPE html>
<html>
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>Sistema Empresa - Clientes</title>
    <link href="https://cdn.jsdelivr.net/npm/bootstrap@5.1.3/dist/css/bootstrap.min.css" rel="stylesheet">
    <link href="https://cdnjs.cloudflare.com/ajax/libs/font-awesome/6.0.0/css/all.min.css" rel="stylesheet">
</head>
<body>
    <!-- Navbar -->
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
                    <h1><i class="fas fa-users"></i> Gestión de Clientes</h1>
                    <a href="ClienteServlet?action=new" class="btn btn-primary">
                        <i class="fas fa-plus"></i> Nuevo Cliente
                    </a>
                </div>

                <!-- Mensajes -->
                <c:if test="${param.success != null}">
                    <div class="alert alert-success alert-dismissible fade show" role="alert">
                        <i class="fas fa-check-circle"></i> ${param.success}
                        <button type="button" class="btn-close" data-bs-dismiss="alert"></button>
                    </div>
                </c:if>
                
                <c:if test="${param.error != null}">
                    <div class="alert alert-danger alert-dismissible fade show" role="alert">
                        <i class="fas fa-exclamation-circle"></i> ${param.error}
                        <button type="button" class="btn-close" data-bs-dismiss="alert"></button>
                    </div>
                </c:if>

                <!-- Búsqueda -->
                <div class="card mb-4">
                    <div class="card-body">
                        <form method="get" action="ClienteServlet">
                            <input type="hidden" name="action" value="search">
                            <div class="row">
                                <div class="col-md-10">
                                    <input type="text" class="form-control" name="termino" 
                                           placeholder="Buscar por nombre, apellido o NIT..." 
                                           value="${termino}">
                                </div>
                                <div class="col-md-2">
                                    <button type="submit" class="btn btn-outline-primary w-100">
                                        <i class="fas fa-search"></i> Buscar
                                    </button>
                                </div>
                            </div>
                        </form>
                    </div>
                </div>

                <!-- Tabla de clientes -->
                <div class="card">
                    <div class="card-header">
                        <h5 class="mb-0">
                            <i class="fas fa-list"></i> Lista de Clientes
                            <span class="badge bg-secondary ms-2">${clientes.size()} registros</span>
                        </h5>
                    </div>
                    <div class="card-body">
                        <c:choose>
                            <c:when test="${empty clientes}">
                                <div class="text-center py-4">
                                    <i class="fas fa-users fa-3x text-muted mb-3"></i>
                                    <h5 class="text-muted">No hay clientes registrados</h5>
                                    <a href="ClienteServlet?action=new" class="btn btn-primary">
                                        <i class="fas fa-plus"></i> Agregar primer cliente
                                    </a>
                                </div>
                            </c:when>
                            <c:otherwise>
                                <div class="table-responsive">
                                    <table class="table table-striped table-hover">
                                        <thead class="table-dark">
                                            <tr>
                                                <th>ID</th>
                                                <th>Nombre Completo</th>
                                                <th>NIT</th>
                                                <th>Género</th>
                                                <th>Teléfono</th>
                                                <th>Email</th>
                                                <th>Fecha Registro</th>
                                                <th>Acciones</th>
                                            </tr>
                                        </thead>
                                        <tbody>
                                            <c:forEach var="cliente" items="${clientes}">
                                                <tr>
                                                    <td>${cliente.idCliente}</td>
                                                    <td>
                                                        <strong>${cliente.nombreCompleto}</strong>
                                                    </td>
                                                    <td>${cliente.nit}</td>
                                                    <td>
                                                        <span class="badge ${cliente.genero ? 'bg-primary' : 'bg-info'}">
                                                            ${cliente.generoTexto}
                                                        </span>
                                                    </td>
                                                    <td>${cliente.telefono}</td>
                                                    <td>${cliente.correoElectronico}</td>
                                                    <td>
                                                        <fmt:formatDate value="${cliente.fechaIngreso}" pattern="dd/MM/yyyy HH:mm"/>
                                                    </td>
                                                    <td>
                                                        <div class="btn-group" role="group">
                                                            <a href="ClienteServlet?action=edit&id=${cliente.idCliente}" 
                                                               class="btn btn-sm btn-outline-warning" title="Editar">
                                                                <i class="fas fa-edit"></i>
                                                            </a>
                                                            <button type="button" class="btn btn-sm btn-outline-danger" 
                                                                    title="Eliminar" onclick="confirmarEliminacion(${cliente.idCliente}, '${cliente.nombreCompleto}')">
                                                                <i class="fas fa-trash"></i>
                                                            </button>
                                                        </div>
                                                    </td>
                                                </tr>
                                            </c:forEach>
                                        </tbody>
                                    </table>
                                </div>
                            </c:otherwise>
                        </c:choose>
                    </div>
                </div>
            </div>
        </div>
    </div>

    <!-- Modal de confirmación -->
    <div class="modal fade" id="confirmModal" tabindex="-1">
        <div class="modal-dialog">
            <div class="modal-content">
                <div class="modal-header">
                    <h5 class="modal-title">Confirmar Eliminación</h5>
                    <button type="button" class="btn-close" data-bs-dismiss="modal"></button>
                </div>
                <div class="modal-body">
                    <p>¿Está seguro que desea eliminar al cliente <strong id="clienteNombre"></strong>?</p>
                    <p class="text-danger"><small>Esta acción no se puede deshacer.</small></p>
                </div>
                <div class="modal-footer">
                    <button type="button" class="btn btn-secondary" data-bs-dismiss="modal">Cancelar</button>
                    <a href="#" id="btnConfirmarEliminar" class="btn btn-danger">
                        <i class="fas fa-trash"></i> Eliminar
                    </a>
                </div>
            </div>
        </div>
    </div>

    <script src="https://cdn.jsdelivr.net/npm/bootstrap@5.1.3/dist/js/bootstrap.bundle.min.js"></script>
    <script>
        function confirmarEliminacion(id, nombre) {
            document.getElementById('clienteNombre').textContent = nombre;
            document.getElementById('btnConfirmarEliminar').href = 'ClienteServlet?action=delete&id=' + id;
            new bootstrap.Modal(document.getElementById('confirmModal')).show();
        }
    </script>
</body>
</html>
