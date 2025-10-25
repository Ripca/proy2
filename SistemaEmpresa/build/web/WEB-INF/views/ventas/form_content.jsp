<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ page import="java.util.List" %>
<%@ page import="com.sistemaempresa.models.*" %>
<%@ page import="java.util.ArrayList" %>

<%
    Venta venta = (Venta) request.getAttribute("venta");
    List<Cliente> clientes = (List<Cliente>) request.getAttribute("clientes");
    List<Producto> productos = (List<Producto>) request.getAttribute("productos");
    List<VentaDetalle> detalles = (List<VentaDetalle>) request.getAttribute("detalles");

    if (clientes == null) clientes = new java.util.ArrayList<>();
    if (productos == null) productos = new java.util.ArrayList<>();
    if (detalles == null) detalles = new java.util.ArrayList<>();

    boolean esEdicion = venta != null;
    String titulo = esEdicion ? "Editar Venta" : "Nueva Venta";
    String action = esEdicion ? "update" : "save";
    
    // Para edición, obtener datos del cliente
    String nombreCliente = "";
    String nitCliente = "";
    String telefonoCliente = "";
    int idCliente = 0;
    
    if (esEdicion) {
        nombreCliente = venta.getNombreCliente() != null ? venta.getNombreCliente() : "";
        // Buscar cliente completo para obtener NIT y teléfono
        for (Cliente cliente : clientes) {
            if (cliente.getIdCliente() == venta.getIdCliente()) {
                nitCliente = cliente.getNit() != null ? cliente.getNit() : "";
                telefonoCliente = cliente.getTelefono() != null ? cliente.getTelefono() : "";
                idCliente = cliente.getIdCliente();
                break;
            }
        }
    }
%>

<!-- HEADER -->
<div class="page-header">
    <div class="add-item d-flex">
        <div class="page-title">
            <h4><i class="fas fa-receipt me-2"></i><%= titulo %></h4>
            <h6>Gestión de ventas a clientes</h6>
        </div>
    </div>
    <div class="page-btn">
        <a href="VentaServlet" class="btn btn-added color">
            <i class="fas fa-list me-2"></i>Listado de ventas
        </a>
    </div>
</div>

<!-- CONTENIDO PRINCIPAL -->
<div class="row">
    <div class="col-md-12">
        <div class="card shadow-sm">
            <div class="card-header bg-success text-white">
                <h5 class="mb-0"><i class="fas fa-file-invoice"></i> Datos de la Venta</h5>
            </div>
            <div class="card-body">
                <form action="VentaServlet" method="post" id="formVenta">
                    <input type="hidden" name="action" value="<%= action %>">
                    <input type="hidden" name="idCliente" id="hiddenIdCliente" value="<%= esEdicion ? String.valueOf(idCliente) : "" %>">
                    <% if (esEdicion) { %>
                        <input type="hidden" name="idVenta" value="<%= venta.getIdVenta() %>">
                    <% } %>

                    <!-- DATOS PRINCIPALES -->
                    <div class="row mb-3">
                        <div class="col-md-6">
                            <label class="form-label fw-bold">Cliente <span class="text-danger">*</span></label>
                            <div class="input-group">
                                <span class="input-group-text"><i class="fas fa-user"></i></span>
                                <input type="text" id="txtNitCliente" class="form-control" placeholder="Buscar por NIT" 
                                       value="<%= esEdicion ? nitCliente : "" %>" autocomplete="off">
                                <button type="button" class="btn btn-outline-success" id="btnBuscarCliente">
                                    <i class="fas fa-search"></i> Buscar
                                </button>
                                <button type="button" class="btn btn-outline-info" id="btnListarClientes" data-bs-toggle="modal" data-bs-target="#modalClientes">
                                    <i class="fas fa-list"></i> Listar
                                </button>
                            </div>
                            <small class="text-muted">Ingrese el NIT del cliente o haga clic en Listar</small>
                        </div>
                        <div class="col-md-6">
                            <label class="form-label fw-bold">Nombre Cliente</label>
                            <div class="input-group">
                                <span class="input-group-text"><i class="fas fa-id-card"></i></span>
                                <input type="text" id="txtNombreCliente" class="form-control" 
                                       value="<%= esEdicion ? nombreCliente : "" %>" disabled>
                            </div>
                            <span id="lblIdCliente" style="display:none;"><%= esEdicion ? String.valueOf(idCliente) : "" %></span>
                        </div>
                    </div>

                    <div class="row mb-3">
                        <div class="col-md-3">
                            <label class="form-label fw-bold">No. Factura <span class="text-danger">*</span></label>
                            <div class="input-group">
                                <span class="input-group-text"><i class="fas fa-hashtag"></i></span>
                                <input type="number" id="txtNoFactura" name="noFactura" class="form-control" 
                                       value="<%= esEdicion ? String.valueOf(venta.getNoFactura()) : "" %>" required>
                            </div>
                        </div>
                        <div class="col-md-3">
                            <label class="form-label fw-bold">Serie <span class="text-danger">*</span></label>
                            <div class="input-group">
                                <span class="input-group-text"><i class="fas fa-barcode"></i></span>
                                <input type="text" id="txtSerie" name="serie" class="form-control" 
                                       value="<%= esEdicion ? venta.getSerie() : "" %>" maxlength="1" required>
                            </div>
                        </div>
                        <div class="col-md-3">
                            <label class="form-label fw-bold">Fecha Venta <span class="text-danger">*</span></label>
                            <div class="input-group">
                                <span class="input-group-text"><i class="fas fa-calendar"></i></span>
                                <input type="date" id="txtFecha" name="fecha" class="form-control"
                                       value="<%= esEdicion && venta.getFechaFactura() != null ? venta.getFechaFactura().toString() : "" %>" required>
                            </div>
                        </div>
                        <div class="col-md-3">
                            <label class="form-label fw-bold">Teléfono Cliente</label>
                            <div class="input-group">
                                <span class="input-group-text"><i class="fas fa-phone"></i></span>
                                <input type="text" id="txtTelefonoCliente" class="form-control" 
                                       value="<%= esEdicion ? telefonoCliente : "" %>" disabled>
                            </div>
                        </div>
                    </div>

                    <hr class="my-4">

                    <!-- SECCION DE PRODUCTOS -->
                    <h5 class="mb-3"><i class="fas fa-box"></i> Productos de la Venta</h5>
                    
                    <div class="row mb-3">
                        <div class="col-md-8">
                            <label class="form-label fw-bold">Buscar Producto</label>
                            <div class="input-group">
                                <span class="input-group-text"><i class="fas fa-barcode"></i></span>
                                <input type="text" id="txtCodigoProducto" class="form-control" placeholder="Ingrese código o nombre del producto" autocomplete="off">
                                <button type="button" class="btn btn-outline-success" id="btnBuscarProducto">
                                    <i class="fas fa-search"></i> Buscar
                                </button>
                                <button type="button" class="btn btn-outline-info" id="btnListarProductos" data-bs-toggle="modal" data-bs-target="#modalProductos">
                                    <i class="fas fa-list"></i> Listar
                                </button>
                            </div>
                        </div>
                    </div>

                    <!-- TABLA DE PRODUCTOS -->
                    <div class="table-responsive mb-4">
                        <table id="grvProductosCompra" class="table table-hover table-bordered">
                            <thead class="table-dark">
                                <tr>
                                    <th style="display:none;">Id Producto</th>
                                    <th>Producto</th>
                                    <th>Cantidad</th>
                                    <th>Existencias</th>
                                    <th>Precio Venta</th>
                                    <th>Subtotal</th>
                                    <th style="width: 80px;">Acción</th>
                                </tr>
                            </thead>
                            <tbody>
                                <% if (esEdicion && detalles != null) {
                                    for (VentaDetalle detalle : detalles) {
                                        System.out.println("DEBUG form_content.jsp - Detalle idVentaDetalle: " + detalle.getIdVentaDetalle() + ", idProducto: " + detalle.getIdProducto());
                                        // Buscar el producto completo para obtener existencias actuales
                                        int existenciasActuales = 0;
                                        for (Producto producto : productos) {
                                            if (producto.getIdProducto() == detalle.getIdProducto()) {
                                                existenciasActuales = producto.getExistencia();
                                                break;
                                            }
                                        }
                                %>
                                    <tr>
                                        <td style="display:none;">
                                            <input type="hidden" name="idVentaDetalle" value="<%= detalle.getIdVentaDetalle() %>">
                                            <input type="hidden" name="idProducto" value="<%= detalle.getIdProducto() %>">
                                        </td>
                                        <td><%= detalle.getNombreProducto() %></td>
                                        <td>
                                            <input type="number" class="form-control form-control-sm cantidad" name="cantidad"
                                                   value="<%= detalle.getCantidad() %>" min="1"
                                                   max="<%= existenciasActuales + Integer.parseInt(detalle.getCantidad()) %>"
                                                   data-existencia="<%= existenciasActuales %>">
                                        </td>
                                        <td><%= existenciasActuales %></td>
                                        <td>
                                            <input type="hidden" name="precioUnitario" value="<%= detalle.getPrecioUnitario() %>">
                                            Q. <%= String.format("%.2f", detalle.getPrecioUnitario()) %>
                                        </td>
                                        <td class="subtotal">Q. <%= String.format("%.2f", detalle.getSubtotal()) %></td>
                                        <td>
                                            <button type="button" class="btn btn-sm btn-danger" onclick="eliminarFilaProducto(this)">
                                                <i class="fas fa-trash"></i>
                                            </button>
                                        </td>
                                    </tr>
                                <% } 
                                } %>
                            </tbody>
                        </table>
                    </div>

                    <!-- RESUMEN DE TOTALES -->
                    <div class="row">
                        <div class="col-md-8"></div>
                        <div class="col-md-4">
                            <div class="card bg-light">
                                <div class="card-body">
                                    <div class="d-flex justify-content-between mb-2">
                                        <span>Subtotal:</span>
                                        <strong><span class="lbl-info-subtotal">Q. <%= esEdicion ? String.format("%.2f", venta.getTotal()) : "0.00" %></span></strong>
                                    </div>
                                    <div class="d-flex justify-content-between mb-3 pb-3 border-bottom">
                                        <span>Descuento:</span>
                                        <strong><span class="lbl-info-descuento">Q. 0.00</span></strong>
                                    </div>
                                    <div class="d-flex justify-content-between">
                                        <h5>Total:</h5>
                                        <h5><strong><span class="lbl-info-total">Q. <%= esEdicion ? String.format("%.2f", venta.getTotal()) : "0.00" %></span></strong></h5>
                                    </div>
                                </div>
                            </div>
                        </div>
                    </div>

                    <!-- BOTONES DE ACCION -->
                    <div class="row mt-4">
                        <div class="col-md-12">
                            <button type="submit" class="btn btn-success btn-lg me-2">
                                <i class="fas fa-save me-2"></i><%= esEdicion ? "Actualizar Venta" : "Guardar Venta" %>
                            </button>
                            <a href="VentaServlet" class="btn btn-secondary btn-lg">
                                <i class="fas fa-times me-2"></i>Cancelar
                            </a>
                        </div>
                    </div>

                </form>
            </div>
        </div>
    </div>
</div>

<!-- MODAL DE CLIENTES -->
<div class="modal fade" id="modalClientes" tabindex="-1" aria-labelledby="modalClientesLabel" aria-hidden="true">
    <div class="modal-dialog modal-lg">
        <div class="modal-content">
            <div class="modal-header bg-success text-white">
                <h5 class="modal-title" id="modalClientesLabel">Seleccionar Cliente</h5>
                <button type="button" class="btn-close btn-close-white" data-bs-dismiss="modal" aria-label="Close"></button>
            </div>
            <div class="modal-body">
                <div class="table-responsive">
                    <table class="table table-hover table-bordered" id="tablaClientesModal">
                        <thead class="table-dark">
                            <tr>
                                <th>NIT</th>
                                <th>Nombres</th>
                                <th>Apellidos</th>
                                <th>Teléfono</th>
                                <th>Acción</th>
                            </tr>
                        </thead>
                        <tbody id="bodyClientesModal">
                        </tbody>
                    </table>
                </div>
            </div>
        </div>
    </div>
</div>

<!-- MODAL DE PRODUCTOS -->
<div class="modal fade" id="modalProductos" tabindex="-1" aria-labelledby="modalProductosLabel" aria-hidden="true">
    <div class="modal-dialog modal-lg">
        <div class="modal-content">
            <div class="modal-header bg-info text-white">
                <h5 class="modal-title" id="modalProductosLabel">Seleccionar Producto</h5>
                <button type="button" class="btn-close btn-close-white" data-bs-dismiss="modal" aria-label="Close"></button>
            </div>
            <div class="modal-body">
                <div class="table-responsive">
                    <table class="table table-hover table-bordered" id="tablaProductosModal">
                        <thead class="table-dark">
                            <tr>
                                <th>Código</th>
                                <th>Producto</th>
                                <th>Existencias</th>
                                <th>Precio Venta</th>
                                <th>Acción</th>
                            </tr>
                        </thead>
                        <tbody id="bodyProductosModal">
                        </tbody>
                    </table>
                </div>
            </div>
        </div>
    </div>
</div>

<script type="text/javascript">
    function agregarProductoATabla(producto) {
        const table = document.querySelector('#grvProductosCompra tbody');
        const fila = document.createElement('tr');
        const subtotal = producto.precio_venta * 1;

        // IMPORTANTE: Primera celda con campos ocultos para el servidor
        const tdId = document.createElement('td');
        tdId.style.display = 'none';

        // Input oculto para idProducto (REQUERIDO para el servidor)
        const inputIdProducto = document.createElement('input');
        inputIdProducto.type = 'hidden';
        inputIdProducto.name = 'idProducto';
        inputIdProducto.value = producto.idProducto;
        tdId.appendChild(inputIdProducto);

        // Input oculto para precioUnitario (REQUERIDO para el servidor)
        const inputPrecioOculto = document.createElement('input');
        inputPrecioOculto.type = 'hidden';
        inputPrecioOculto.name = 'precioUnitario';
        inputPrecioOculto.value = producto.precio_venta;
        tdId.appendChild(inputPrecioOculto);

        const tdNombre = document.createElement('td');
        tdNombre.textContent = producto.producto;

        const tdCantidad = document.createElement('td');
        const inputCantidad = document.createElement('input');
        inputCantidad.type = 'number';
        inputCantidad.className = 'form-control form-control-sm cantidad';
        inputCantidad.name = 'cantidad';
        inputCantidad.value = '1';
        inputCantidad.min = '1';
        inputCantidad.max = producto.existencia || 999999;
        inputCantidad.dataset.existencia = producto.existencia || 0;
        inputCantidad.addEventListener('change', function() {
            const existencia = parseInt(this.dataset.existencia) || 0;
            const cantidad = parseInt(this.value) || 0;
            if (cantidad > existencia) {
                alert('La cantidad no puede ser mayor a las existencias disponibles (' + existencia + ')');
                this.value = existencia;
            }
            actualizarSubtotal(this);
        });
        tdCantidad.appendChild(inputCantidad);

        const tdExistencia = document.createElement('td');
        tdExistencia.textContent = producto.existencia || 0;

        const tdPrecio = document.createElement('td');
        tdPrecio.textContent = 'Q. ' + parseFloat(producto.precio_venta).toFixed(2);

        const tdSubtotal = document.createElement('td');
        tdSubtotal.className = 'subtotal';
        tdSubtotal.textContent = 'Q. ' + subtotal.toFixed(2);

        const tdAccion = document.createElement('td');
        const btnEliminar = document.createElement('button');
        btnEliminar.type = 'button';
        btnEliminar.className = 'btn btn-sm btn-danger';
        btnEliminar.innerHTML = '<i class="fas fa-trash"></i>';
        btnEliminar.addEventListener('click', function() { eliminarFilaProducto(this); });
        tdAccion.appendChild(btnEliminar);

        fila.appendChild(tdId);
        fila.appendChild(tdNombre);
        fila.appendChild(tdCantidad);
        fila.appendChild(tdExistencia);
        fila.appendChild(tdPrecio);
        fila.appendChild(tdSubtotal);
        fila.appendChild(tdAccion);

        table.appendChild(fila);
        console.log('DEBUG: Producto agregado - idProducto: ' + producto.idProducto + ', precio: ' + producto.precio_venta);
        actualizarTotales();
    }

    function eliminarFilaProducto(btn) {
        btn.closest('tr').remove();
        actualizarTotales();
    }

    function actualizarSubtotal(input) {
        const fila = input.closest('tr');
        const cantidad = parseFloat(input.value) || 0;

        // Obtener el precio del input oculto precioUnitario
        const precioInput = fila.querySelector('input[name="precioUnitario"]');
        const precioVenta = precioInput ? parseFloat(precioInput.value) : 0;

        // Actualizar el input oculto cantidad con el nuevo valor
        const cantidadInput = fila.querySelector('input[name="cantidad"]');
        if (cantidadInput) {
            cantidadInput.value = input.value;
        }

        const subtotal = cantidad * precioVenta;
        fila.querySelector('.subtotal').textContent = 'Q. ' + subtotal.toFixed(2);
        console.log('DEBUG actualizarSubtotal: cantidad=' + cantidad + ', precio=' + precioVenta + ', subtotal=' + subtotal);
        actualizarTotales();
    }

    function actualizarTotales() {
        let subtotal = 0;
        const filas = document.querySelectorAll('#grvProductosCompra tbody tr');
        filas.forEach(fila => {
            const subtotalText = fila.querySelector('.subtotal').textContent.replace('Q. ', '');
            subtotal += parseFloat(subtotalText) || 0;
        });
        document.querySelector('.lbl-info-subtotal').textContent = 'Q. ' + subtotal.toFixed(2);
        document.querySelector('.lbl-info-descuento').textContent = 'Q. 0.00';
        document.querySelector('.lbl-info-total').textContent = 'Q. ' + subtotal.toFixed(2);
    }

    function buscarCliente() {
        const nit = document.getElementById('txtNitCliente').value.trim();
        if (!nit) {
            alert('Ingrese el NIT');
            return;
        }
        $.ajax({
            url: 'ClienteServlet',
            type: 'GET',
            data: { action: 'buscarPorNit', nit: nit },
            dataType: 'json',
            success: function(result) {
                if (result && result.id_cliente) {
                    document.getElementById('lblIdCliente').textContent = result.id_cliente;
                    document.getElementById('hiddenIdCliente').value = result.id_cliente;
                    document.getElementById('txtNombreCliente').value = (result.nombres || '') + ' ' + (result.apellidos || '');
                    document.getElementById('txtTelefonoCliente').value = result.telefono || '';
                } else {
                    if (confirm('Cliente no encontrado. ¿Desea crear un nuevo cliente con este NIT?')) {
                        window.location.href = 'ClienteServlet?action=new&nit=' + encodeURIComponent(nit);
                    }
                }
            },
            error: function() {
                alert('Error al buscar');
            }
        });
    }

    function buscarProducto() {
        const termino = document.getElementById('txtCodigoProducto').value.trim();
        if (!termino) {
            alert('Ingrese código o nombre');
            return;
        }
        $.ajax({
            url: 'ProductoServlet',
            type: 'GET',
            data: { action: 'buscarAjax', termino: termino },
            dataType: 'json',
            success: function(result) {
                console.log("Producto:::");
                console.log(result);
                if (result && result.length > 0) {
                    agregarProductoATabla(result[0]);
                    document.getElementById('txtCodigoProducto').value = '';
                } else {
                    if (confirm('Producto no encontrado. ¿Desea crear un nuevo producto?')) {
                        window.location.href = 'ProductoServlet?action=new&nombre=' + encodeURIComponent(termino);
                    }
                }
            },
            error: function() {
                alert('Error al buscar');
            }
        });
    }

    function cargarClientesModal() {
        $.ajax({
            url: 'ClienteServlet',
            type: 'GET',
            data: { action: 'obtenerTodos' },
            dataType: 'json',
            success: function(result) {
                const tbody = document.getElementById('bodyClientesModal');
                tbody.innerHTML = '';
                if (result && result.length > 0) {
                    result.forEach(function(cliente) {
                        const fila = document.createElement('tr');
                        const tdNit = document.createElement('td');
                        tdNit.textContent = cliente.nit;
                        const tdNombres = document.createElement('td');
                        tdNombres.textContent = cliente.nombres;
                        const tdApellidos = document.createElement('td');
                        tdApellidos.textContent = cliente.apellidos;
                        const tdTelefono = document.createElement('td');
                        tdTelefono.textContent = cliente.telefono;
                        const tdAccion = document.createElement('td');
                        const btn = document.createElement('button');
                        btn.type = 'button';
                        btn.className = 'btn btn-sm btn-success';
                        btn.innerHTML = '<i class="fas fa-check"></i> Seleccionar';
                        btn.addEventListener('click', function() {
                            seleccionarClienteModal(cliente.id_cliente, cliente.nombres + ' ' + cliente.apellidos, cliente.nit, cliente.telefono);
                        });
                        tdAccion.appendChild(btn);
                        fila.appendChild(tdNit);
                        fila.appendChild(tdNombres);
                        fila.appendChild(tdApellidos);
                        fila.appendChild(tdTelefono);
                        fila.appendChild(tdAccion);
                        tbody.appendChild(fila);
                    });
                } else {
                    tbody.innerHTML = '<tr><td colspan="5" class="text-center">No hay clientes disponibles</td></tr>';
                }
            },
            error: function() {
                alert('Error al cargar clientes');
            }
        });
    }

    function cargarProductosModal() {
        $.ajax({
            url: 'ProductoServlet',
            type: 'GET',
            data: { action: 'obtenerTodos' },
            dataType: 'json',
            success: function(result) {
                const tbody = document.getElementById('bodyProductosModal');
                tbody.innerHTML = '';
                if (result && result.length > 0) {
                    result.forEach(function(producto) {
                        const fila = document.createElement('tr');
                        const tdCodigo = document.createElement('td');
                        tdCodigo.textContent = producto.idProducto || '';
                        const tdNombre = document.createElement('td');
                        tdNombre.textContent = producto.producto;
                        const tdExistencia = document.createElement('td');
                        tdExistencia.textContent = producto.existencia || 0;
                        const tdPrecio = document.createElement('td');
                        tdPrecio.textContent = 'Q. ' + parseFloat(producto.precio_venta).toFixed(2);
                        const tdAccion = document.createElement('td');
                        const btn = document.createElement('button');
                        btn.type = 'button';
                        btn.className = 'btn btn-sm btn-success';
                        btn.innerHTML = '<i class="fas fa-check"></i> Seleccionar';
                        btn.addEventListener('click', function() {
                            agregarProductoATabla(producto);
                            const modal = bootstrap.Modal.getInstance(document.getElementById('modalProductos'));
                            if (modal) modal.hide();
                        });
                        tdAccion.appendChild(btn);
                        fila.appendChild(tdCodigo);
                        fila.appendChild(tdNombre);
                        fila.appendChild(tdExistencia);
                        fila.appendChild(tdPrecio);
                        fila.appendChild(tdAccion);
                        tbody.appendChild(fila);
                    });
                } else {
                    tbody.innerHTML = '<tr><td colspan="5" class="text-center">No hay productos disponibles</td></tr>';
                }
            },
            error: function() {
                alert('Error al cargar productos');
            }
        });
    }

    function seleccionarClienteModal(idCliente, nombre, nit, telefono) {
        document.getElementById('lblIdCliente').textContent = idCliente;
        document.getElementById('hiddenIdCliente').value = idCliente;
        document.getElementById('txtNombreCliente').value = nombre;
        document.getElementById('txtNitCliente').value = nit;
        document.getElementById('txtTelefonoCliente').value = telefono;
        const modal = bootstrap.Modal.getInstance(document.getElementById('modalClientes'));
        if (modal) modal.hide();
    }

    document.addEventListener('DOMContentLoaded', function() {
        // Agregar event listeners a los inputs de cantidad existentes (para edición)
        document.querySelectorAll('#grvProductosCompra .cantidad').forEach(input => {
            input.addEventListener('change', function() {
                actualizarSubtotal(this);
            });
        });

        document.getElementById('btnBuscarCliente').addEventListener('click', buscarCliente);
        document.getElementById('btnBuscarProducto').addEventListener('click', buscarProducto);
        document.getElementById('btnListarClientes').addEventListener('click', cargarClientesModal);
        document.getElementById('btnListarProductos').addEventListener('click', cargarProductosModal);

        document.getElementById('txtNitCliente').addEventListener('keypress', function(e) {
            if (e.key === 'Enter') {
                e.preventDefault();
                buscarCliente();
            }
        });

        document.getElementById('txtCodigoProducto').addEventListener('keypress', function(e) {
            if (e.key === 'Enter') {
                e.preventDefault();
                buscarProducto();
            }
        });

        document.getElementById('formVenta').addEventListener('submit', function(e) {
            e.preventDefault();
            if (!document.getElementById('hiddenIdCliente').value) {
                alert('Seleccione cliente');
                return;
            }
            const filas = document.querySelectorAll('#grvProductosCompra tbody tr');
            if (filas.length === 0) {
                alert('Agregue productos');
                return;
            }

            // DEBUG: Verificar que cada fila tenga los inputs ocultos correctos
            let filasValidas = 0;
            filas.forEach((fila, index) => {
                const idProductoInput = fila.querySelector('input[name="idProducto"]');
                const cantidadInput = fila.querySelector('input[name="cantidad"]');
                const precioInput = fila.querySelector('input[name="precioUnitario"]');

                if (idProductoInput && idProductoInput.value && idProductoInput.value.trim() !== '') {
                    filasValidas++;
                    console.log('DEBUG Fila ' + index + ': idProducto=' + idProductoInput.value + ', cantidad=' + (cantidadInput ? cantidadInput.value : 'N/A') + ', precio=' + (precioInput ? precioInput.value : 'N/A'));
                } else {
                    console.warn('DEBUG: Fila ' + index + ' sin idProducto válido, será ignorada');
                }
            });

            if (filasValidas === 0) {
                alert('No hay productos válidos para guardar');
                return;
            }

            console.log('DEBUG: Total de filas válidas: ' + filasValidas);
            this.submit();
        });

        // Solo establecer fecha actual si no es edición
        <% if (!esEdicion) { %>
            const today = new Date().toISOString().split('T')[0];
            document.getElementById('txtFecha').value = today;
        <% } %>
        
        actualizarTotales();
    });
</script>