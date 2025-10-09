<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@page import="com.sistemaempresa.models.Empleado"%>
<%@page import="com.sistemaempresa.models.Puesto"%>
<%@page import="java.util.List"%>
<!DOCTYPE html>
<html lang="es">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>Sistema Empresa - <%= request.getAttribute("empleado") != null ? "Editar" : "Nuevo" %> Empleado</title>
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
                <a class="nav-link" href="EmpleadoServlet">
                    <i class="fas fa-user-tie"></i> Empleados
                </a>
                <a class="nav-link" href="LoginServlet?action=logout">
                    <i class="fas fa-sign-out-alt"></i> Cerrar Sesión
                </a>
            </div>
        </div>
    </nav>

    <div class="container mt-4">
        <div class="row justify-content-center">
            <div class="col-md-10">
                <%
                    Empleado empleado = (Empleado) request.getAttribute("empleado");
                    List<Puesto> puestos = (List<Puesto>) request.getAttribute("puestos");
                    boolean esEdicion = empleado != null;
                %>
                
                <!-- Header -->
                <div class="d-flex justify-content-between align-items-center mb-4">
                    <h1>
                        <i class="fas fa-user-tie"></i> 
                        <%= esEdicion ? "Editar Empleado" : "Nuevo Empleado" %>
                    </h1>
                    <a href="EmpleadoServlet" class="btn btn-secondary">
                        <i class="fas fa-arrow-left"></i> Volver
                    </a>
                </div>

                <!-- Mensajes -->
                <% if (request.getParameter("error") != null) { %>
                    <div class="alert alert-danger alert-dismissible fade show" role="alert">
                        <i class="fas fa-exclamation-circle"></i> <%= request.getParameter("error") %>
                        <button type="button" class="btn-close" data-bs-dismiss="alert"></button>
                    </div>
                <% } %>

                <!-- Formulario -->
                <div class="card">
                    <div class="card-header">
                        <h5 class="mb-0">
                            <i class="fas fa-<%= esEdicion ? "edit" : "plus" %>"></i>
                            Datos del Empleado
                        </h5>
                    </div>
                    <div class="card-body">
                        <form method="POST" action="EmpleadoServlet" id="empleadoForm">
                            <input type="hidden" name="action" value="<%= esEdicion ? "update" : "save" %>">
                            <% if (esEdicion) { %>
                                <input type="hidden" name="idEmpleado" value="<%= empleado.getIdEmpleado() %>">
                            <% } %>
                            
                            <div class="row">
                                <div class="col-md-6">
                                    <div class="mb-3">
                                        <label for="nombres" class="form-label">
                                            <i class="fas fa-user"></i> Nombres *
                                        </label>
                                        <input type="text" 
                                               class="form-control" 
                                               id="nombres" 
                                               name="nombres" 
                                               value="<%= esEdicion ? empleado.getNombres() : "" %>"
                                               required 
                                               maxlength="60"
                                               placeholder="Nombres del empleado">
                                    </div>
                                </div>
                                
                                <div class="col-md-6">
                                    <div class="mb-3">
                                        <label for="apellidos" class="form-label">
                                            <i class="fas fa-user"></i> Apellidos *
                                        </label>
                                        <input type="text" 
                                               class="form-control" 
                                               id="apellidos" 
                                               name="apellidos" 
                                               value="<%= esEdicion ? empleado.getApellidos() : "" %>"
                                               required 
                                               maxlength="60"
                                               placeholder="Apellidos del empleado">
                                    </div>
                                </div>
                            </div>

                            <div class="row">
                                <div class="col-md-6">
                                    <div class="mb-3">
                                        <label for="dpi" class="form-label">
                                            <i class="fas fa-id-card"></i> DPI *
                                        </label>
                                        <input type="text" 
                                               class="form-control" 
                                               id="dpi" 
                                               name="dpi" 
                                               value="<%= esEdicion ? empleado.getDpi() : "" %>"
                                               required 
                                               maxlength="13"
                                               placeholder="Número de DPI">
                                    </div>
                                </div>
                                
                                <div class="col-md-6">
                                    <div class="mb-3">
                                        <label for="idPuesto" class="form-label">
                                            <i class="fas fa-briefcase"></i> Puesto *
                                        </label>
                                        <select class="form-select" id="idPuesto" name="idPuesto" required>
                                            <option value="">Seleccione un puesto</option>
                                            <%
                                                if (puestos != null) {
                                                    for (Puesto puesto : puestos) {
                                            %>
                                            <option value="<%= puesto.getIdPuesto() %>" 
                                                    <%= esEdicion && empleado.getIdPuesto() == puesto.getIdPuesto() ? "selected" : "" %>>
                                                <%= puesto.getPuesto() %>
                                            </option>
                                            <%
                                                    }
                                                }
                                            %>
                                        </select>
                                    </div>
                                </div>
                            </div>

                            <div class="row">
                                <div class="col-md-6">
                                    <div class="mb-3">
                                        <label for="fechaNacimiento" class="form-label">
                                            <i class="fas fa-calendar"></i> Fecha de Nacimiento *
                                        </label>
                                        <input type="date" 
                                               class="form-control" 
                                               id="fechaNacimiento" 
                                               name="fechaNacimiento" 
                                               value="<%= esEdicion ? empleado.getFechaNacimiento() : "" %>"
                                               required>
                                    </div>
                                </div>
                                
                                <div class="col-md-6">
                                    <div class="mb-3">
                                        <label for="fechaInicioLabores" class="form-label">
                                            <i class="fas fa-calendar-check"></i> Fecha Inicio Labores *
                                        </label>
                                        <input type="date" 
                                               class="form-control" 
                                               id="fechaInicioLabores" 
                                               name="fechaInicioLabores" 
                                               value="<%= esEdicion ? empleado.getFechaInicioLabores() : "" %>"
                                               required>
                                    </div>
                                </div>
                            </div>

                            <div class="mb-3">
                                <label for="telefono" class="form-label">
                                    <i class="fas fa-phone"></i> Teléfono
                                </label>
                                <input type="tel" 
                                       class="form-control" 
                                       id="telefono" 
                                       name="telefono" 
                                       value="<%= esEdicion && empleado.getTelefono() != null ? empleado.getTelefono() : "" %>"
                                       maxlength="25"
                                       placeholder="Número de teléfono">
                            </div>

                            <div class="d-grid gap-2 d-md-flex justify-content-md-end">
                                <a href="EmpleadoServlet" class="btn btn-secondary me-md-2">
                                    <i class="fas fa-times"></i> Cancelar
                                </a>
                                <button type="submit" class="btn btn-primary">
                                    <i class="fas fa-save"></i> 
                                    <%= esEdicion ? "Actualizar" : "Guardar" %>
                                </button>
                            </div>
                        </form>
                    </div>
                </div>
            </div>
        </div>
    </div>

    <script src="https://cdn.jsdelivr.net/npm/bootstrap@5.3.0/dist/js/bootstrap.bundle.min.js"></script>
    <script>
        // Validación del formulario
        document.getElementById('empleadoForm').addEventListener('submit', function(e) {
            const nombres = document.getElementById('nombres').value.trim();
            const apellidos = document.getElementById('apellidos').value.trim();
            const dpi = document.getElementById('dpi').value.trim();
            const puesto = document.getElementById('idPuesto').value;
            const fechaNacimiento = document.getElementById('fechaNacimiento').value;
            const fechaInicioLabores = document.getElementById('fechaInicioLabores').value;
            
            if (nombres === '' || apellidos === '' || dpi === '' || puesto === '' || 
                fechaNacimiento === '' || fechaInicioLabores === '') {
                e.preventDefault();
                alert('Por favor complete todos los campos obligatorios');
                return false;
            }
            
            // Validar que la fecha de nacimiento sea anterior a la fecha de inicio
            if (new Date(fechaNacimiento) >= new Date(fechaInicioLabores)) {
                e.preventDefault();
                alert('La fecha de nacimiento debe ser anterior a la fecha de inicio de labores');
                document.getElementById('fechaNacimiento').focus();
                return false;
            }
        });

        // Enfocar el primer campo al cargar
        document.addEventListener('DOMContentLoaded', function() {
            document.getElementById('nombres').focus();
        });
    </script>
</body>
</html>
