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
                           value="<%= esEdicion && venta.getFechaFactura() != null ? venta.getFechaFactura().toString() : "" %>" required>
                </div>
                <div class="col-md-4">
                    <label for="idCliente" class="form-label">Cliente</label>
                    <div class="input-group">
                        <input type="text" class="form-control" id="buscarCliente" placeholder="Buscar cliente...">
                        <button type="button" class="btn btn-outline-primary" onclick="buscarClientes()">
                            <i class="fas fa-search"></i>
                        </button>
                        <button type="button" class="btn btn-outline-success" onclick="abrirModalNuevoCliente()">
                            <i class="fas fa-plus"></i>
                        </button>
                    </div>
                    <select class="form-select mt-2" id="idCliente" name="idCliente">
                        <option value="">Cliente General</option>
                        <%
                            if (clientes != null) {
                                for (Cliente cliente : clientes) {
                                    boolean selected = esEdicion && venta.getIdCliente() == cliente.getIdCliente();
                        %>
                        <option value="<%= cliente.getIdCliente() %>" <%= selected ? "selected" : "" %>>
                            <%= cliente.getNombreCompleto() %>
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
                                        <div class="input-group">
                                            <select class="form-select producto-select" name="idProducto[]" required onchange="actualizarPrecio(this)">
                                                <option value="">Seleccionar producto...</option>
                                                <%
                                                    if (productos != null) {
                                                        for (Producto producto : productos) {
                                                            boolean selectedProd = detalle.getIdProducto() == producto.getIdProducto();
                                                %>
                                                <option value="<%= producto.getIdProducto() %>"
                                                        data-precio="<%= producto.getPrecioVenta() != null ? producto.getPrecioVenta().doubleValue() : 0.0 %>"
                                                        <%= selectedProd ? "selected" : "" %>>
                                                    <%= producto.getProducto() %> - Q.<%= String.format("%.2f", producto.getPrecioVenta() != null ? producto.getPrecioVenta().doubleValue() : 0.0) %>
                                                </option>
                                                <%
                                                        }
                                                    }
                                                %>
                                            </select>
                                            <button type="button" class="btn btn-outline-primary" onclick="abrirModalProductos(this)">
                                                <i class="fas fa-search"></i>
                                            </button>
                                        </div>
                                    </td>
                                    <td>
                                        <input type="number" class="form-control cantidad-input" name="cantidad[]" 
                                               value="<%= detalle.getCantidad() %>" min="1" required onchange="calcularSubtotal(this)">
                                    </td>
                                    <td>
                                        <input type="number" class="form-control precio-input" name="precio[]"
                                               value="<%= detalle.getPrecioUnitario() %>" step="0.01" min="0" required onchange="calcularSubtotal(this)">
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
                <div class="input-group">
                    <select class="form-select producto-select" name="idProducto[]" required onchange="actualizarPrecio(this)">
                        <option value="">Seleccionar producto...</option>
                        <%
                            if (productos != null) {
                                for (Producto producto : productos) {
                        %>
                        <option value="<%= producto.getIdProducto() %>" data-precio="<%= producto.getPrecioVenta() != null ? producto.getPrecioVenta().doubleValue() : 0.0 %>">
                            <%= producto.getProducto() %> - Q.<%= String.format("%.2f", producto.getPrecioVenta() != null ? producto.getPrecioVenta().doubleValue() : 0.0) %>
                        </option>
                        <%
                                }
                            }
                        %>
                    </select>
                    <button type="button" class="btn btn-outline-primary" onclick="abrirModalProductos(this)">
                        <i class="fas fa-search"></i>
                    </button>
                </div>
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

    // Búsqueda de clientes en tiempo real
    function buscarClientes() {
        const termino = document.getElementById('buscarCliente').value;
        const selectCliente = document.getElementById('idCliente');

        if (termino.length < 2) {
            return;
        }

        fetch(`BusquedaServlet?tipo=clientes&termino=${encodeURIComponent(termino)}`)
            .then(response => response.json())
            .then(data => {
                // Limpiar opciones existentes excepto "Cliente General"
                selectCliente.innerHTML = '<option value="">Cliente General</option>';

                if (Array.isArray(data)) {
                    data.forEach(cliente => {
                        const option = document.createElement('option');
                        option.value = cliente.id;
                        option.textContent = `${cliente.nombre} - ${cliente.nit}`;
                        selectCliente.appendChild(option);
                    });
                } else if (data.error) {
                    showAlert('error', 'Error', data.error);
                }
            })
            .catch(error => {
                console.error('Error en búsqueda:', error);
                showAlert('error', 'Error', 'Error al buscar clientes');
            });
    }

    // Búsqueda automática mientras escribe
    document.getElementById('buscarCliente').addEventListener('input', function() {
        const termino = this.value;
        if (termino.length >= 2) {
            setTimeout(() => {
                if (this.value === termino) { // Solo buscar si no ha cambiado
                    buscarClientes();
                }
            }, 500);
        }
    });

    // Modal para nuevo cliente
    function abrirModalNuevoCliente() {
        const modal = `
            <div class="modal fade" id="modalNuevoCliente" tabindex="-1">
                <div class="modal-dialog">
                    <div class="modal-content">
                        <div class="modal-header">
                            <h5 class="modal-title">Nuevo Cliente</h5>
                            <button type="button" class="btn-close" data-bs-dismiss="modal"></button>
                        </div>
                        <div class="modal-body">
                            <form id="formNuevoCliente">
                                <div class="mb-3">
                                    <label class="form-label">Nombres</label>
                                    <input type="text" class="form-control" id="nuevosNombres" required>
                                </div>
                                <div class="mb-3">
                                    <label class="form-label">Apellidos</label>
                                    <input type="text" class="form-control" id="nuevosApellidos" required>
                                </div>
                                <div class="mb-3">
                                    <label class="form-label">NIT</label>
                                    <input type="text" class="form-control" id="nuevoNit">
                                </div>
                                <div class="mb-3">
                                    <label class="form-label">Teléfono</label>
                                    <input type="text" class="form-control" id="nuevoTelefono">
                                </div>
                                <div class="mb-3">
                                    <label class="form-label">Email</label>
                                    <input type="email" class="form-control" id="nuevoEmail">
                                </div>
                                <div class="mb-3">
                                    <label class="form-label">Género</label>
                                    <select class="form-select" id="nuevoGenero">
                                        <option value="true">Masculino</option>
                                        <option value="false">Femenino</option>
                                    </select>
                                </div>
                            </form>
                        </div>
                        <div class="modal-footer">
                            <button type="button" class="btn btn-secondary" data-bs-dismiss="modal">Cancelar</button>
                            <button type="button" class="btn btn-primary" onclick="guardarNuevoCliente()">Guardar</button>
                        </div>
                    </div>
                </div>
            </div>
        `;

        // Agregar modal al DOM si no existe
        if (!document.getElementById('modalNuevoCliente')) {
            document.body.insertAdjacentHTML('beforeend', modal);
        }

        // Mostrar modal
        const modalElement = new bootstrap.Modal(document.getElementById('modalNuevoCliente'));
        modalElement.show();
    }

    function guardarNuevoCliente() {
        const formData = new FormData();
        formData.append('action', 'save');
        formData.append('nombres', document.getElementById('nuevosNombres').value);
        formData.append('apellidos', document.getElementById('nuevosApellidos').value);
        formData.append('nit', document.getElementById('nuevoNit').value);
        formData.append('telefono', document.getElementById('nuevoTelefono').value);
        formData.append('email', document.getElementById('nuevoEmail').value);
        formData.append('genero', document.getElementById('nuevoGenero').value);

        fetch('ClienteServlet', {
            method: 'POST',
            body: formData
        })
        .then(response => response.text())
        .then(data => {
            // Cerrar modal
            bootstrap.Modal.getInstance(document.getElementById('modalNuevoCliente')).hide();

            // Mostrar mensaje de éxito
            showAlert('success', 'Éxito', 'Cliente creado correctamente');

            // Recargar lista de clientes
            buscarClientes();
        })
        .catch(error => {
            console.error('Error:', error);
            showAlert('error', 'Error', 'Error al crear cliente');
        });
    }

    // Modal para seleccionar productos
    let selectProductoActual = null;

    function abrirModalProductos(button) {
        // Guardar referencia al select que abrió el modal
        selectProductoActual = button.parentElement.querySelector('.producto-select');

        const modal = `
            <div class="modal fade" id="modalProductos" tabindex="-1">
                <div class="modal-dialog modal-lg">
                    <div class="modal-content">
                        <div class="modal-header">
                            <h5 class="modal-title">Seleccionar Producto</h5>
                            <button type="button" class="btn-close" data-bs-dismiss="modal"></button>
                        </div>
                        <div class="modal-body">
                            <div class="mb-3">
                                <input type="text" class="form-control" id="buscarProductoModal" placeholder="Buscar producto...">
                            </div>
                            <div class="table-responsive">
                                <table class="table table-hover" id="tablaProductos">
                                    <thead>
                                        <tr>
                                            <th>Producto</th>
                                            <th>Marca</th>
                                            <th>Precio</th>
                                            <th>Existencia</th>
                                            <th>Acción</th>
                                        </tr>
                                    </thead>
                                    <tbody id="bodyProductos">
                                        <!-- Se llena dinámicamente -->
                                    </tbody>
                                </table>
                            </div>
                        </div>
                    </div>
                </div>
            </div>
        `;

        // Agregar modal al DOM si no existe
        if (!document.getElementById('modalProductos')) {
            document.body.insertAdjacentHTML('beforeend', modal);
        }

        // Cargar productos
        cargarProductosModal();

        // Mostrar modal
        const modalElement = new bootstrap.Modal(document.getElementById('modalProductos'));
        modalElement.show();
    }

    function cargarProductosModal(termino = '') {
        const tbody = document.getElementById('bodyProductos');
        tbody.innerHTML = '<tr><td colspan="5" class="text-center">Cargando...</td></tr>';

        fetch(`BusquedaServlet?tipo=productos&termino=${encodeURIComponent(termino)}`)
            .then(response => response.json())
            .then(data => {
                tbody.innerHTML = '';

                if (Array.isArray(data)) {
                    data.forEach(producto => {
                        const fila = `
                            <tr>
                                <td>${producto.nombre}</td>
                                <td>${producto.marca || 'Sin marca'}</td>
                                <td>Q. ${producto.precio.toFixed(2)}</td>
                                <td>${producto.existencia}</td>
                                <td>
                                    <button type="button" class="btn btn-sm btn-primary"
                                            onclick="seleccionarProducto(${producto.id}, '${producto.nombre}', ${producto.precio})">
                                        Seleccionar
                                    </button>
                                </td>
                            </tr>
                        `;
                        tbody.insertAdjacentHTML('beforeend', fila);
                    });
                } else if (data.error) {
                    tbody.innerHTML = `<tr><td colspan="5" class="text-center text-danger">${data.error}</td></tr>`;
                } else {
                    tbody.innerHTML = '<tr><td colspan="5" class="text-center">No se encontraron productos</td></tr>';
                }
            })
            .catch(error => {
                console.error('Error:', error);
                tbody.innerHTML = '<tr><td colspan="5" class="text-center text-danger">Error al cargar productos</td></tr>';
            });
    }

    function seleccionarProducto(id, nombre, precio) {
        if (selectProductoActual) {
            // Limpiar opciones existentes
            selectProductoActual.innerHTML = '<option value="">Seleccionar producto...</option>';

            // Agregar el producto seleccionado
            const option = document.createElement('option');
            option.value = id;
            option.textContent = `${nombre} - Q.${precio.toFixed(2)}`;
            option.setAttribute('data-precio', precio);
            option.selected = true;
            selectProductoActual.appendChild(option);

            // Actualizar precio
            actualizarPrecio(selectProductoActual);
        }

        // Cerrar modal
        bootstrap.Modal.getInstance(document.getElementById('modalProductos')).hide();
    }

    // Búsqueda en modal de productos
    document.addEventListener('DOMContentLoaded', function() {
        document.addEventListener('input', function(e) {
            if (e.target && e.target.id === 'buscarProductoModal') {
                const termino = e.target.value;
                setTimeout(() => {
                    if (e.target.value === termino) {
                        cargarProductosModal(termino);
                    }
                }, 500);
            }
        });
    });
</script>
