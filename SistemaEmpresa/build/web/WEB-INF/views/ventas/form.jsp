<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ page import="java.util.List" %>
<%@ page import="com.sistemaempresa.models.*" %>
<%@ page import="java.time.format.DateTimeFormatter" %>

<%
    Venta venta = (Venta) request.getAttribute("venta");
    List<Cliente> clientes = (List<Cliente>) request.getAttribute("clientes");
    List<Empleado> empleados = (List<Empleado>) request.getAttribute("empleados");
    List<Producto> productos = (List<Producto>) request.getAttribute("productos");
    
    boolean esEdicion = venta != null;
    String titulo = esEdicion ? "Editar Venta" : "Nueva Venta";
    String action = esEdicion ? "update" : "save";
%>

<!DOCTYPE html>
<html lang="es">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title><%= titulo %> - Sistema Empresa</title>
    <link href="https://cdn.jsdelivr.net/npm/bootstrap@5.3.0/dist/css/bootstrap.min.css" rel="stylesheet">
    <link href="https://cdnjs.cloudflare.com/ajax/libs/font-awesome/6.0.0/css/all.min.css" rel="stylesheet">
</head>
<body>
    <div class="container-fluid">
        <div class="row">
            <div class="col-12">
                <div class="d-flex justify-content-between align-items-center mb-4">
                    <h2><i class="fas fa-shopping-cart"></i> <%= titulo %></h2>
                    <a href="VentaServlet" class="btn btn-secondary">
                        <i class="fas fa-arrow-left"></i> Volver
                    </a>
                </div>

                <form action="VentaServlet" method="post" id="formVenta">
                    <input type="hidden" name="action" value="<%= action %>">
                    <% if (esEdicion) { %>
                        <input type="hidden" name="idVenta" value="<%= venta.getIdVenta() %>">
                    <% } %>

                    <!-- Datos de la Venta -->
                    <div class="card mb-4">
                        <div class="card-header">
                            <h5 class="mb-0"><i class="fas fa-file-invoice"></i> Datos de la Venta</h5>
                        </div>
                        <div class="card-body">
                            <div class="row">
                                <div class="col-md-3">
                                    <div class="mb-3">
                                        <label for="noFactura" class="form-label">No. Factura *</label>
                                        <input type="number" class="form-control" id="noFactura" name="noFactura" 
                                               value="<%= esEdicion ? venta.getNoFactura() : "" %>" required>
                                    </div>
                                </div>
                                <div class="col-md-3">
                                    <div class="mb-3">
                                        <label for="serie" class="form-label">Serie *</label>
                                        <input type="text" class="form-control" id="serie" name="serie" 
                                               value="<%= esEdicion ? venta.getSerie() : "" %>" required maxlength="10">
                                    </div>
                                </div>
                                <div class="col-md-3">
                                    <div class="mb-3">
                                        <label for="fechaFactura" class="form-label">Fecha Factura *</label>
                                        <input type="date" class="form-control" id="fechaFactura" name="fechaFactura" 
                                               value="<%= esEdicion ? venta.getFechaFactura().toString() : "" %>" required>
                                    </div>
                                </div>
                                <div class="col-md-3">
                                    <div class="mb-3">
                                        <label for="idCliente" class="form-label">Cliente *</label>
                                        <select class="form-select" id="idCliente" name="idCliente" required>
                                            <option value="">Seleccionar cliente...</option>
                                            <%
                                                if (clientes != null) {
                                                    for (Cliente cliente : clientes) {
                                                        boolean selected = esEdicion && venta.getIdCliente() == cliente.getIdCliente();
                                            %>
                                            <option value="<%= cliente.getIdCliente() %>" <%= selected ? "selected" : "" %>>
                                                <%= cliente.getCliente() %>
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
                                        <label for="idEmpleado" class="form-label">Empleado *</label>
                                        <select class="form-select" id="idEmpleado" name="idEmpleado" required>
                                            <option value="">Seleccionar empleado...</option>
                                            <%
                                                if (empleados != null) {
                                                    for (Empleado empleado : empleados) {
                                                        boolean selected = esEdicion && venta.getIdEmpleado() == empleado.getIdEmpleado();
                                            %>
                                            <option value="<%= empleado.getIdEmpleado() %>" <%= selected ? "selected" : "" %>>
                                                <%= empleado.getNombres() %> <%= empleado.getApellidos() %>
                                            </option>
                                            <%
                                                    }
                                                }
                                            %>
                                        </select>
                                    </div>
                                </div>
                            </div>
                        </div>
                    </div>

                    <!-- Detalles de la Venta -->
                    <div class="card mb-4">
                        <div class="card-header d-flex justify-content-between align-items-center">
                            <h5 class="mb-0"><i class="fas fa-list"></i> Detalles de la Venta</h5>
                            <button type="button" class="btn btn-success btn-sm" onclick="agregarDetalle()">
                                <i class="fas fa-plus"></i> Agregar Producto
                            </button>
                        </div>
                        <div class="card-body">
                            <div class="table-responsive">
                                <table class="table table-bordered" id="tablaDetalles">
                                    <thead class="table-light">
                                        <tr>
                                            <th>Producto</th>
                                            <th>Cantidad</th>
                                            <th>Precio Unitario</th>
                                            <th>Subtotal</th>
                                            <th>Acciones</th>
                                        </tr>
                                    </thead>
                                    <tbody id="detallesBody">
                                        <% if (esEdicion && venta.tieneDetalles()) {
                                            for (int i = 0; i < venta.getDetalles().size(); i++) {
                                                VentaDetalle detalle = venta.getDetalles().get(i);
                                                System.out.println("DEBUG JSP - Detalle " + i + " idVentaDetalle: " + detalle.getIdVentaDetalle());
                                        %>
                                        <tr>
                                            <td>
                                                <input type="hidden" name="idVentaDetalle" value="<%= detalle.getIdVentaDetalle() %>">
                                                <select class="form-select" name="idProducto" onchange="calcularSubtotal(this)" required>
                                                    <option value="">Seleccionar...</option>
                                                    <% for (Producto producto : productos) { %>
                                                    <option value="<%= producto.getIdProducto() %>"
                                                            <%= detalle.getIdProducto() == producto.getIdProducto() ? "selected" : "" %>>
                                                        <%= producto.getProducto() %>
                                                    </option>
                                                    <% } %>
                                                </select>
                                            </td>
                                            <td>
                                                <input type="number" class="form-control" name="cantidad"
                                                       value="<%= detalle.getCantidad() %>" min="1" onchange="calcularSubtotal(this)" required>
                                            </td>
                                            <td>
                                                <input type="number" class="form-control" name="precioUnitario"
                                                       value="<%= detalle.getPrecioUnitario() %>" step="0.01" min="0" onchange="calcularSubtotal(this)" required>
                                            </td>
                                            <td>
                                                <input type="text" class="form-control subtotal" readonly
                                                       value="<%= String.format("%.2f", detalle.calcularSubtotal()) %>">
                                            </td>
                                            <td>
                                                <button type="button" class="btn btn-danger btn-sm" onclick="eliminarDetalle(this)">
                                                    <i class="fas fa-trash"></i>
                                                </button>
                                            </td>
                                        </tr>
                                        <% } } %>
                                    </tbody>
                                    <tfoot>
                                        <tr class="table-info">
                                            <th colspan="3" class="text-end">Total:</th>
                                            <th id="totalGeneral">0.00</th>
                                            <th></th>
                                        </tr>
                                    </tfoot>
                                </table>
                            </div>
                        </div>
                    </div>

                    <!-- Botones -->
                    <div class="d-flex justify-content-end gap-2">
                        <a href="VentaServlet" class="btn btn-secondary">Cancelar</a>
                        <button type="submit" class="btn btn-primary">
                            <i class="fas fa-save"></i> <%= esEdicion ? "Actualizar" : "Guardar" %>
                        </button>
                    </div>
                </form>
            </div>
        </div>
    </div>

    <script src="https://cdn.jsdelivr.net/npm/bootstrap@5.3.0/dist/js/bootstrap.bundle.min.js"></script>
    <script>
        let contadorFilas = 0;

        function agregarDetalle() {
            const tbody = document.getElementById('detallesBody');
            const fila = document.createElement('tr');
            
            fila.innerHTML = `
                <td>
                    <select class="form-select" name="idProducto" onchange="calcularSubtotal(this)" required>
                        <option value="">Seleccionar...</option>
                        <% if (productos != null) { for (Producto producto : productos) { %>
                        <option value="<%= producto.getIdProducto() %>"><%= producto.getProducto() %></option>
                        <% } } %>
                    </select>
                </td>
                <td>
                    <input type="number" class="form-control" name="cantidad" min="1" onchange="calcularSubtotal(this)" required>
                </td>
                <td>
                    <input type="number" class="form-control" name="precioUnitario" step="0.01" min="0" onchange="calcularSubtotal(this)" required>
                </td>
                <td>
                    <input type="text" class="form-control subtotal" readonly value="0.00">
                </td>
                <td>
                    <button type="button" class="btn btn-danger btn-sm" onclick="eliminarDetalle(this)">
                        <i class="fas fa-trash"></i>
                    </button>
                </td>
            `;
            
            tbody.appendChild(fila);
            contadorFilas++;
        }

        function eliminarDetalle(boton) {
            const fila = boton.closest('tr');
            fila.remove();
            calcularTotal();
        }

        function calcularSubtotal(elemento) {
            const fila = elemento.closest('tr');
            const cantidad = parseFloat(fila.querySelector('input[name="cantidad"]').value) || 0;
            const precio = parseFloat(fila.querySelector('input[name="precioUnitario"]').value) || 0;
            const subtotal = cantidad * precio;
            
            fila.querySelector('.subtotal').value = subtotal.toFixed(2);
            calcularTotal();
        }

        function calcularTotal() {
            const subtotales = document.querySelectorAll('.subtotal');
            let total = 0;
            
            subtotales.forEach(function(subtotal) {
                total += parseFloat(subtotal.value) || 0;
            });
            
            document.getElementById('totalGeneral').textContent = total.toFixed(2);
        }

        // Calcular total inicial si hay detalles
        document.addEventListener('DOMContentLoaded', function() {
            calcularTotal();
        });

        // Validar formulario antes de enviar
        document.getElementById('formVenta').addEventListener('submit', function(e) {
            const detalles = document.querySelectorAll('#detallesBody tr');
            if (detalles.length === 0) {
                e.preventDefault();
                alert('Debe agregar al menos un producto a la venta.');
                return false;
            }

            // Validar que todos los detalles tengan datos completos
            let valido = true;
            detalles.forEach(function(fila) {
                const producto = fila.querySelector('select[name="idProducto"]').value;
                const cantidad = fila.querySelector('input[name="cantidad"]').value;
                const precio = fila.querySelector('input[name="precioUnitario"]').value;

                if (!producto || !cantidad || !precio) {
                    valido = false;
                }
            });

            if (!valido) {
                e.preventDefault();
                alert('Todos los detalles deben tener producto, cantidad y precio.');
                return false;
            }
        });
    </script>
</body>
</html>
