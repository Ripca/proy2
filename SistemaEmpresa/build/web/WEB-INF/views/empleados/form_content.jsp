<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ page import="com.sistemaempresa.models.Empleado" %>
<%@ page import="com.sistemaempresa.models.Puesto" %>
<%@ page import="java.util.List" %>

<%
    Empleado empleado = (Empleado) request.getAttribute("empleado");
    List<Puesto> puestos = (List<Puesto>) request.getAttribute("puestos");
    boolean esEdicion = empleado != null;
    String titulo = esEdicion ? "Editar Empleado" : "Nuevo Empleado";
    String action = esEdicion ? "update" : "save";
%>

<!-- Header de la página -->
<div class="d-flex justify-content-between align-items-center mb-4">
    <div>
        <h2 class="mb-0"><%= titulo %></h2>
        <p class="text-muted mb-0">Complete la información del empleado</p>
    </div>
    <a href="EmpleadoServlet" class="btn btn-secondary">
        <i class="fas fa-arrow-left"></i> Volver a la Lista
    </a>
</div>

<!-- Formulario -->
<div class="row justify-content-center">
    <div class="col-lg-8">
        <div class="card">
            <div class="card-header">
                <h5 class="mb-0">
                    <i class="fas fa-user-edit"></i> Información del Empleado
                </h5>
            </div>
            <div class="card-body">
                <form action="EmpleadoServlet" method="post" id="formEmpleado">
                    <input type="hidden" name="action" value="<%= action %>">
                    <% if (esEdicion) { %>
                        <input type="hidden" name="idEmpleado" value="<%= empleado.getIdEmpleado() %>">
                    <% } %>

                    <div class="row">
                        <div class="col-md-6">
                            <div class="mb-3">
                                <label for="nombres" class="form-label">
                                    Nombres <span class="text-danger">*</span>
                                </label>
                                <input type="text" class="form-control" id="nombres" name="nombres" 
                                       value="<%= esEdicion ? empleado.getNombres() : "" %>" 
                                       required maxlength="100">
                            </div>
                        </div>
                        <div class="col-md-6">
                            <div class="mb-3">
                                <label for="apellidos" class="form-label">
                                    Apellidos <span class="text-danger">*</span>
                                </label>
                                <input type="text" class="form-control" id="apellidos" name="apellidos" 
                                       value="<%= esEdicion ? empleado.getApellidos() : "" %>" 
                                       required maxlength="100">
                            </div>
                        </div>
                    </div>

                    <div class="row">
                        <div class="col-md-6">
                            <div class="mb-3">
                                <label for="dpi" class="form-label">DPI</label>
                                <input type="text" class="form-control" id="dpi" name="dpi"
                                       value="<%= esEdicion && empleado.getDpi() != null ? empleado.getDpi() : "" %>"
                                       maxlength="15">
                            </div>
                        </div>
                        <div class="col-md-6">
                            <div class="mb-3">
                                <label for="genero" class="form-label">
                                    Género <span class="text-danger">*</span>
                                </label>
                                <select class="form-select" id="genero" name="genero" required>
                                    <option value="">Seleccionar género...</option>
                                    <option value="true" <%= esEdicion && empleado.isGenero() ? "selected" : "" %>>Masculino</option>
                                    <option value="false" <%= esEdicion && !empleado.isGenero() ? "selected" : "" %>>Femenino</option>
                                </select>
                            </div>
                        </div>
                    </div>

                    <div class="row">
                        <div class="col-md-6">
                            <div class="mb-3">
                                <label for="telefono" class="form-label">Teléfono</label>
                                <input type="tel" class="form-control" id="telefono" name="telefono"
                                       value="<%= esEdicion && empleado.getTelefono() != null ? empleado.getTelefono() : "" %>"
                                       maxlength="25">
                            </div>
                        </div>
                        <div class="col-md-6">
                            <div class="mb-3">
                                <label for="fecha_nacimiento" class="form-label">
                                    Fecha de Nacimiento <span class="text-danger">*</span>
                                </label>
                                <input type="date" class="form-control" id="fecha_nacimiento" name="fecha_nacimiento"
                                       value="<%= esEdicion && empleado.getFechaNacimiento() != null ? empleado.getFechaNacimiento().toString() : "" %>"
                                       required>
                            </div>
                        </div>
                    </div>

                    <div class="row">
                        <div class="col-md-6">
                            <div class="mb-3">
                                <label for="idPuesto" class="form-label">
                                    Puesto <span class="text-danger">*</span>
                                </label>
                                <select class="form-select" id="idPuesto" name="idPuesto" required>
                                    <option value="">Seleccionar puesto...</option>
                                    <%
                                        if (puestos != null) {
                                            for (Puesto puesto : puestos) {
                                                boolean selected = esEdicion && empleado.getIdPuesto() == puesto.getIdPuesto();
                                    %>
                                    <option value="<%= puesto.getIdPuesto() %>" <%= selected ? "selected" : "" %>>
                                        <%= puesto.getPuesto() %>
                                    </option>
                                    <%
                                            }
                                        }
                                    %>
                                </select>
                            </div>
                        </div>
                        <div class="col-md-6">
                            <div class="mb-3">
                                <label for="fecha_inicio_labores" class="form-label">
                                    Fecha de Inicio de Labores <span class="text-danger">*</span>
                                </label>
                                <input type="date" class="form-control" id="fecha_inicio_labores" name="fecha_inicio_labores"
                                       value="<%= esEdicion && empleado.getFechaInicioLabores() != null ? empleado.getFechaInicioLabores().toString() : "" %>"
                                       required>
                            </div>
                        </div>
                    </div>

                    <div class="mb-3">
                        <label for="direccion" class="form-label">Dirección</label>
                        <textarea class="form-control" id="direccion" name="direccion" rows="3" 
                                  maxlength="200"><%= esEdicion && empleado.getDireccion() != null ? empleado.getDireccion() : "" %></textarea>
                    </div>

                    <% if (esEdicion && empleado.getFechaIngreso() != null) { %>
                    <div class="mb-3">
                        <label class="form-label">Fecha de Ingreso</label>
                        <input type="text" class="form-control" 
                               value="<%= empleado.getFechaIngreso().toString() %>" readonly>
                    </div>
                    <% } %>

                    <!-- Botones -->
                    <div class="d-flex justify-content-end gap-2 mt-4">
                        <a href="EmpleadoServlet" class="btn btn-secondary">
                            <i class="fas fa-times"></i> Cancelar
                        </a>
                        <button type="submit" class="btn btn-primary">
                            <i class="fas fa-save"></i> <%= esEdicion ? "Actualizar" : "Guardar" %>
                        </button>
                    </div>
                </form>
            </div>
        </div>
    </div>
</div>

<script>
    // Validación del formulario
    document.getElementById('formEmpleado').addEventListener('submit', function(e) {
        const nombres = document.getElementById('nombres').value.trim();
        const apellidos = document.getElementById('apellidos').value.trim();
        
        if (!nombres || !apellidos) {
            e.preventDefault();
            showAlert('error', 'Error', 'Los nombres y apellidos son obligatorios.');
            return false;
        }
    });
    
    // Auto-focus en el primer campo
    document.addEventListener('DOMContentLoaded', function() {
        document.getElementById('nombres').focus();
    });
</script>
