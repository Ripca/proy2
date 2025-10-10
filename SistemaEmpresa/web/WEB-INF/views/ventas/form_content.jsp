<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ page import="java.util.List" %>
<%@ page import="com.sistemaempresa.models.*" %>

<%
    Venta venta = (Venta) request.getAttribute("venta");
    List<Cliente> clientes = (List<Cliente>) request.getAttribute("clientes");
    List<Producto> productos = (List<Producto>) request.getAttribute("productos");
    boolean esEdicion = venta != null;
    String titulo = esEdicion ? "Editar Venta" : "Nueva Venta";
    String action = esEdicion ? "update" : "save";
%>

<!-- Header de la página -->
<div class="d-flex justify-content-between align-items-center mb-4">
    <div>
        <h2 class="mb-0"><%= titulo %></h2>
        <p class="text-muted mb-0">Complete la información de la venta</p>
    </div>
    <a href="VentaServlet" class="btn btn-secondary">
        <i class="fas fa-arrow-left"></i> Volver a la Lista
    </a>
</div>

<div class="card">
    <div class="card-body">
        <form action="VentaServlet" method="post" id="formVenta">
            <input type="hidden" name="action" value="<%= action %>">
            <% if (esEdicion) { %>
                <input type="hidden" name="idVenta" value="<%= venta.getIdVenta() %>">
            <% } %>

            <!-- Información de la Venta -->
            <div class="row mb-4">
                <div class="col-md-4">
                    <label for="noFactura" class="form-label">No. Factura <span class="text-danger">*</span></label>
                    <input type="text" class="form-control" id="noFactura" name="noFactura" 
                           value="<%= esEdicion ? venta.getNoFactura() : "" %>" required>
                </div>
                <div class="col-md-4">
                    <label for="fecha" class="form-label">Fecha <span class="text-danger">*</span></label>
                    <input type="date" class="form-control" id="fecha" name="fecha" 
                           value="<%= esEdicion && venta.getFecha() != null ? venta.getFecha().toString() : "" %>" required>
                </div>
                <div class="col-md-4">
                    <label for="idCliente" class="form-label">Cliente</label>
                    <select class="form-select" id="idCliente" name="idCliente">
                        <option value="">Cliente General</option>
                        <%
                            if (clientes != null) {
                                for (Cliente cliente : clientes) {
                                    boolean selected = esEdicion && venta.getIdCliente() != null && 
                                                     venta.getIdCliente().equals(cliente.getIdCliente());
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

            <!-- Detalle de Productos -->
            <div class="card mb-4">
                <div class="card-header d-flex justify-content-between align-items-center">
                    <h5 class="mb-0">Detalle de Productos</h5>
                    <button type="button" class="btn btn-success btn-sm" onclick="agregarProducto()">
                        <i class="fas fa-plus"></i> Agregar Producto
                    </button>
                </div>
                <div class="card-body">
                    <div class="table-responsive">
                        <table class="table table-bordered" id="tablaProductos">
                            <thead class="table-dark">
                                <tr>
                                    <th>Producto</th>
                                    <th>Cantidad</th>
                                    <th>Precio</th>
                                    <th>Subtotal</th>
                                    <th>Acciones</th>
                                </tr>
                            </thead>
                            <tbody id="detalleProductos">
                                <%
                                    if (esEdicion && venta.getDetalles() != null && !venta.getDetalles().isEmpty()) {
                                        for (VentaDetalle detalle : venta.getDetalles()) {
                                %>
                                <tr>
                                    <td>
                                        <select class="form-select producto-select" name="idProducto[]" required onchange="actualizarPrecio(this)">
                                            <option value="">Seleccionar producto...</option>
                                            <%
                                                if (productos != null) {
                                                    for (Producto producto : productos) {
                                                        boolean selectedProd = detalle.getIdProducto().equals(producto.getIdProducto());
                                            %>
                                            <option value="<%= producto.getIdProducto() %>" 
                                                    data-precio="<%= producto.getPrecioVenta() %>" 
                                                    <%= selectedProd ? "selected" : "" %>>
                                                <%= producto.getProducto() %> - Q.<%= String.format("%.2f", producto.getPrecioVenta()) %>
                                            </option>
                                            <%
                                                    }
                                                }
                                            %>
                                        </select>
                                    </td>
                                    <td>
                                        <input type="number" class="form-control cantidad-input" name="cantidad[]" 
                                               value="<%= detalle.getCantidad() %>" min="1" required onchange="calcularSubtotal(this)">
                                    </td>
                                    <td>
                                        <input type="number" class="form-control precio-input" name="precio[]" 
                                               value="<%= detalle.getPrecio() %>" step="0.01" min="0" required onchange="calcularSubtotal(this)">
                                    </td>
                                    <td>
                                        <input type="number" class="form-control subtotal-input" readonly 
                                               value="<%= detalle.getSubtotal() %>">
                                    </td>
                                    <td>
                                        <button type="button" class="btn btn-danger btn-sm" onclick="eliminarFila(this)">
                                            <i class="fas fa-trash"></i>
                                        </button>
                                    </td>
                                </tr>
                                <%
                                        }
                                    }
                                %>
                            </tbody>
                        </table>
                    </div>
                </div>
            </div>

            <!-- Total -->
            <div class="row mb-4">
                <div class="col-md-8"></div>
                <div class="col-md-4">
                    <div class="card bg-light">
                        <div class="card-body">
                            <h5 class="card-title">Total de la Venta</h5>
                            <h3 class="text-success">Q. <span id="totalVenta">0.00</span></h3>
                            <input type="hidden" name="total" id="totalInput" value="0">
                        </div>
                    </div>
                </div>
            </div>

            <!-- Botones -->
            <div class="d-flex justify-content-end gap-2">
                <a href="VentaServlet" class="btn btn-secondary">
                    <i class="fas fa-times"></i> Cancelar
                </a>
                <button type="submit" class="btn btn-primary">
                    <i class="fas fa-save"></i> <%= esEdicion ? "Actualizar" : "Guardar" %> Venta
                </button>
            </div>
        </form>
    </div>
</div>

<script>
    let contadorFilas = 0;

    function agregarProducto() {
        const tbody = document.getElementById('detalleProductos');
        const nuevaFila = document.createElement('tr');

        nuevaFila.innerHTML = `
            <td>
                <select class="form-select producto-select" name="idProducto[]" required onchange="actualizarPrecio(this)">
                    <option value="">Seleccionar producto...</option>
                    <%
                        if (productos != null) {
                            for (Producto producto : productos) {
                    %>
                    <option value="<%= producto.getIdProducto() %>" data-precio="<%= producto.getPrecioVenta() %>">
                        <%= producto.getProducto() %> - Q.<%= String.format("%.2f", producto.getPrecioVenta()) %>
                    </option>
                    <%
                            }
                        }
                    %>
                </select>
            </td>
            <td>
                <input type="number" class="form-control cantidad-input" name="cantidad[]" value="1" min="1" required onchange="calcularSubtotal(this)">
            </td>
            <td>
                <input type="number" class="form-control precio-input" name="precio[]" step="0.01" min="0" required onchange="calcularSubtotal(this)">
            </td>
            <td>
                <input type="number" class="form-control subtotal-input" readonly value="0.00">
            </td>
            <td>
                <button type="button" class="btn btn-danger btn-sm" onclick="eliminarFila(this)">
                    <i class="fas fa-trash"></i>
                </button>
            </td>
        `;

        tbody.appendChild(nuevaFila);
        contadorFilas++;
    }

    function eliminarFila(boton) {
        const fila = boton.closest('tr');
        fila.remove();
        calcularTotal();
    }

    function actualizarPrecio(select) {
        const fila = select.closest('tr');
        const precioInput = fila.querySelector('.precio-input');
        const selectedOption = select.options[select.selectedIndex];

        if (selectedOption.value) {
            const precio = selectedOption.getAttribute('data-precio');
            precioInput.value = precio;
            calcularSubtotal(precioInput);
        } else {
            precioInput.value = '';
            calcularSubtotal(precioInput);
        }
    }

    function calcularSubtotal(input) {
        const fila = input.closest('tr');
        const cantidad = parseFloat(fila.querySelector('.cantidad-input').value) || 0;
        const precio = parseFloat(fila.querySelector('.precio-input').value) || 0;
        const subtotal = cantidad * precio;

        fila.querySelector('.subtotal-input').value = subtotal.toFixed(2);
        calcularTotal();
    }

    function calcularTotal() {
        let total = 0;
        const subtotales = document.querySelectorAll('.subtotal-input');

        subtotales.forEach(function(subtotal) {
            total += parseFloat(subtotal.value) || 0;
        });

        document.getElementById('totalVenta').textContent = total.toFixed(2);
        document.getElementById('totalInput').value = total.toFixed(2);
    }

    // Validación del formulario
    document.getElementById('formVenta').addEventListener('submit', function(e) {
        const filas = document.querySelectorAll('#detalleProductos tr');

        if (filas.length === 0) {
            e.preventDefault();
            showAlert('error', 'Error', 'Debe agregar al menos un producto a la venta.');
            return false;
        }

        let hayProductosValidos = false;
        filas.forEach(function(fila) {
            const producto = fila.querySelector('.producto-select').value;
            const cantidad = fila.querySelector('.cantidad-input').value;
            const precio = fila.querySelector('.precio-input').value;

            if (producto && cantidad && precio) {
                hayProductosValidos = true;
            }
        });

        if (!hayProductosValidos) {
            e.preventDefault();
            showAlert('error', 'Error', 'Debe completar la información de al menos un producto.');
            return false;
        }
    });

    // Calcular total inicial si hay productos
    document.addEventListener('DOMContentLoaded', function() {
        calcularTotal();

        // Si no hay productos, agregar uno por defecto
        const filas = document.querySelectorAll('#detalleProductos tr');
        if (filas.length === 0) {
            agregarProducto();
        }
    });
</script>
