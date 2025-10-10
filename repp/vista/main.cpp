#include <iostream>
#include <windows.h>
#include <stdlib.h>
#include <string>
#include <ctime>
#include <iomanip>
#include <fstream>
#include <sstream>
#include "ConexionBD.h"
#include "Puesto.h"
#include "Marca.h"
#include "Proveedor.h"
#include "Producto.h"
#include "Empleado.h"
#include "Cliente.h"
#include "Compra.h"
#include "Venta.h"

using namespace std;

void limpiarPantalla() {
    system("cls");
}

void pausar() {
    cout << "\nPresione Enter para continuar...";
    cin.ignore();
    cin.get();
}

// Función segura para obtener la fecha y hora actual
string obtenerFechaHoraActual() {
    time_t now = time(0);
    struct tm timeinfo;
    char buffer[80];

    // Usar localtime_s en lugar de localtime
    localtime_s(&timeinfo, &now);

    // Formatear la fecha y hora como YYYY-MM-DD HH:MM:SS
    string fecha = to_string(1900 + timeinfo.tm_year) + "-" +
                  to_string(1 + timeinfo.tm_mon) + "-" +
                  to_string(timeinfo.tm_mday) + " " +
                  to_string(timeinfo.tm_hour) + ":" +
                  to_string(timeinfo.tm_min) + ":" +
                  to_string(timeinfo.tm_sec);

    return fecha;
}

// Funciones para Marca
void menuMarcas();
void ingresarMarca();
void mostrarMarcas();
void actualizarMarca();
void eliminarMarca();

// Funciones para Puesto
void menuPuestos();
void ingresarPuesto();
void mostrarPuestos();
void actualizarPuesto();
void eliminarPuesto();

// Funciones para Proveedor
void menuProveedores();
void ingresarProveedor();
void mostrarProveedores();
void actualizarProveedor();
void eliminarProveedor();

// Funciones para Producto
void menuProductos();
void ingresarProducto();
void mostrarProductos();
void actualizarProducto();
void eliminarProducto();

// Funciones para Empleado
void menuEmpleados();
void ingresarEmpleado();
void mostrarEmpleados();
void actualizarEmpleado();
void eliminarEmpleado();

// Funciones para Cliente
void menuClientes();
void ingresarCliente();
bool ingresarClienteConNIT(const string& nitPreestablecido);
void mostrarClientes();
void actualizarCliente();
void eliminarCliente();

// Funciones para Compra
void menuCompras();
void ingresarCompra();
void mostrarCompras();
void mostrarDetallesCompra();
void actualizarCompra();
void eliminarCompra();

// Funciones para detalles de compra
void menuDetallesCompra();
void actualizarDetalleCompra();
void eliminarDetalleCompra();

// Funciones para Venta
void menuVentas();
void ingresarVenta();
void mostrarVentas();
void mostrarDetallesVenta();
void actualizarVenta();
void eliminarVenta();

// Funciones para detalles de venta
void menuDetallesVenta();
void actualizarDetalleVenta();
void eliminarDetalleVenta();

// Funciones adicionales para Ventas
void buscarClientePorNIT();
void imprimirFactura();
string generarCorrelativoFactura();
string validarFormatoNIT(string nit);
string validarFormatoNITParaBusqueda(string nit);
void generarFacturaVentaPDF(int idVenta);

// Funciones adicionales para Compras
string generarNumeroOrdenCompra();
void generarFacturaCompra(int idcompra);
string formatearMoneda(double cantidad);
void generarFacturaPDF(int idcompra);

// Funciones de validación y manejo de errores
int validarEnteroPositivo(const string& mensaje, int minimo = 1);
double validarDecimalPositivo(const string& mensaje, double minimo = 0.01);
string validarTextoNoVacio(const string& mensaje);
string validarFecha(const string& mensaje);
void manejarErrorBD(const string& operacion, const string& consulta, MYSQL* conector);
bool confirmarOperacion(const string& mensaje);
int validarOpcionMenu(const string& mensaje, int minimo = 0, int maximo = 10);

// Implementación de funciones para Proveedor
void menuProveedores() {
    int opcion = 0;
    do {
        limpiarPantalla();
        cout << "======================================\n";
        cout << "        GESTIÓN DE PROVEEDORES       \n";
        cout << "======================================\n\n";
        cout << "1. Ingresar nuevo proveedor\n";
        cout << "2. Mostrar todos los proveedores\n";
        cout << "3. Actualizar proveedor\n";
        cout << "4. Eliminar proveedor\n";
        cout << "0. Volver al menú principal\n\n";
        opcion = validarOpcionMenu("Ingrese una opcion: ", 0, 4);

        switch (opcion) {
            case 1:
                ingresarProveedor();
                break;
            case 2:
                mostrarProveedores();
                break;
            case 3:
                actualizarProveedor();
                break;
            case 4:
                eliminarProveedor();
                break;
            case 0:
                // Volver al menú principal
                break;
            default:
                cout << "\nOpción no válida. Intente de nuevo.\n";
                pausar();
        }
    } while (opcion != 0);
}

void ingresarProveedor() {
    limpiarPantalla();
    cout << "===== INGRESAR NUEVO PROVEEDOR =====\n\n";

    string proveedor, nit, direccion, telefono;

    cout << "Ingrese Nombre de Proveedor: ";
    getline(cin, proveedor);
    cout << "Ingrese NIT: ";
    getline(cin, nit);
    cout << "Ingrese Dirección: ";
    getline(cin, direccion);
    cout << "Ingrese Teléfono: ";
    getline(cin, telefono);

    // Usamos 0 como valor temporal para idProveedor, ya que será generado por la base de datos
    Proveedor p = Proveedor(0, proveedor, nit, direccion, telefono);
    p.crear();

    pausar();
}

void mostrarProveedores() {
    limpiarPantalla();
    cout << "===== LISTADO DE PROVEEDORES =====\n\n";

    Proveedor p = Proveedor();
    p.leer();

    pausar();
}

void actualizarProveedor() {
    limpiarPantalla();
    cout << "===== ACTUALIZAR PROVEEDOR =====\n\n";

    Proveedor p = Proveedor();
    p.leer();

    int idProveedor = 0;
    string proveedor, nit, direccion, telefono;

    cout << "\nIngrese el ID del proveedor a modificar: ";
    cin >> idProveedor;
    cin.ignore();
    cout << "Ingrese Nuevo Nombre de Proveedor: ";
    getline(cin, proveedor);
    cout << "Ingrese Nuevo NIT: ";
    getline(cin, nit);
    cout << "Ingrese Nueva Dirección: ";
    getline(cin, direccion);
    cout << "Ingrese Nuevo Teléfono: ";
    getline(cin, telefono);

    p = Proveedor(idProveedor, proveedor, nit, direccion, telefono);
    p.actualizar();

    cout << "\nDatos actualizados:\n";
    p.leer();

    pausar();
}

void eliminarProveedor() {
    limpiarPantalla();
    cout << "===== ELIMINAR PROVEEDOR =====\n\n";

    Proveedor p = Proveedor();
    p.leer();

    int idProveedor = 0;
    cout << "\nIngrese el ID del proveedor a eliminar: ";
    cin >> idProveedor;

    char confirmar;
    cout << "¿Está seguro de eliminar este registro? (S/N): ";
    cin >> confirmar;

    if (confirmar == 'S' || confirmar == 's') {
        p.setIdProveedor(idProveedor);
        p.eliminar();
        cout << "\nRegistro eliminado. Lista actualizada:\n";
        p.leer();
    } else {
        cout << "\nOperación cancelada.\n";
    }

    pausar();
}



// Implementación de funciones para Producto
void menuProductos() {
    int opcion = 0;
    do {
        limpiarPantalla();
        cout << "======================================\n";
        cout << "        GESTIÓN DE PRODUCTOS         \n";
        cout << "======================================\n\n";
        cout << "1. Ingresar nuevo producto\n";
        cout << "2. Mostrar todos los productos\n";
        cout << "3. Actualizar producto\n";
        cout << "4. Eliminar producto\n";
        cout << "0. Volver al menú principal\n\n";
        opcion = validarOpcionMenu("Ingrese una opcion: ", 0, 4);

        switch (opcion) {
            case 1:
                ingresarProducto();
                break;
            case 2:
                mostrarProductos();
                break;
            case 3:
                actualizarProducto();
                break;
            case 4:
                eliminarProducto();
                break;
            case 0:
                // Volver al menú principal
                break;
            default:
                cout << "\nOpción no válida. Intente de nuevo.\n";
                pausar();
        }
    } while (opcion != 0);
}

void ingresarProducto() {
    limpiarPantalla();
    cout << "===== INGRESAR NUEVO PRODUCTO =====\n\n";

    // Mostrar marcas disponibles
    cout << "Marcas disponibles:\n";
    Marca m = Marca();
    m.leer();

    string producto, descripcion, imagen, fecha_ingreso;
    short idMarca = 0;
    double precio_costo = 0.0, precio_venta = 0.0;
    int existencia = 0;

    cout << "Ingrese Nombre de Producto: ";
    getline(cin, producto);
    cout << "Ingrese ID de Marca: ";
    cin >> idMarca;
    cin.ignore();
    cout << "Ingrese Descripción: ";
    getline(cin, descripcion);
    cout << "Ingrese Ruta de Imagen: ";
    getline(cin, imagen);
    cout << "Ingrese Precio de Costo: ";
    cin >> precio_costo;
    cout << "Ingrese Precio de Venta: ";
    cin >> precio_venta;
    cout << "Ingrese Existencia: ";
    cin >> existencia;
    cin.ignore();

    // Obtener fecha actual para fecha_ingreso
    fecha_ingreso = obtenerFechaHoraActual();

    // Usamos 0 como valor temporal para idProducto, ya que será generado por la base de datos
    Producto p = Producto(0, producto, idMarca, descripcion, imagen, precio_costo, precio_venta, existencia, fecha_ingreso);
    p.crear();

    pausar();
}

void mostrarProductos() {
    limpiarPantalla();
    cout << "===== LISTADO DE PRODUCTOS =====\n\n";

    Producto p = Producto();
    p.leer();

    pausar();
}

void actualizarProducto() {
    limpiarPantalla();
    cout << "===== ACTUALIZAR PRODUCTO =====\n\n";

    Producto p = Producto();
    p.leer();

    // Mostrar marcas disponibles
    cout << "\nMarcas disponibles:\n";
    Marca m = Marca();
    m.leer();

    int idProducto = 0;
    string producto, descripcion, imagen, fecha_ingreso;
    short idMarca = 0;
    double precio_costo = 0.0, precio_venta = 0.0;
    int existencia = 0;

    cout << "\nIngrese el ID del producto a modificar: ";
    cin >> idProducto;
    cin.ignore();
    cout << "Ingrese Nuevo Nombre de Producto: ";
    getline(cin, producto);
    cout << "Ingrese Nuevo ID de Marca: ";
    cin >> idMarca;
    cin.ignore();
    cout << "Ingrese Nueva Descripción: ";
    getline(cin, descripcion);
    cout << "Ingrese Nueva Ruta de Imagen: ";
    getline(cin, imagen);
    cout << "Ingrese Nuevo Precio de Costo: ";
    cin >> precio_costo;
    cout << "Ingrese Nuevo Precio de Venta: ";
    cin >> precio_venta;
    cout << "Ingrese Nueva Existencia: ";
    cin >> existencia;
    cin.ignore();

    // Obtener fecha actual para fecha_ingreso
    fecha_ingreso = obtenerFechaHoraActual();

    p = Producto(idProducto, producto, idMarca, descripcion, imagen, precio_costo, precio_venta, existencia, fecha_ingreso);
    p.actualizar();

    cout << "\nDatos actualizados:\n";
    p.leer();

    pausar();
}

void eliminarProducto() {
    limpiarPantalla();
    cout << "===== ELIMINAR PRODUCTO =====\n\n";

    Producto p = Producto();
    p.leer();

    int idProducto = 0;
    cout << "\nIngrese el ID del producto a eliminar: ";
    cin >> idProducto;

    char confirmar;
    cout << "¿Está seguro de eliminar este registro? (S/N): ";
    cin >> confirmar;

    if (confirmar == 'S' || confirmar == 's') {
        p.setIdProducto(idProducto);
        p.eliminar();
        cout << "\nRegistro eliminado. Lista actualizada:\n";
        p.leer();
    } else {
        cout << "\nOperación cancelada.\n";
    }

    pausar();
}



// Implementación de funciones para Empleado
void menuEmpleados() {
    int opcion = 0;
    do {
        limpiarPantalla();
        cout << "======================================\n";
        cout << "        GESTIÓN DE EMPLEADOS         \n";
        cout << "======================================\n\n";
        cout << "1. Ingresar nuevo empleado\n";
        cout << "2. Mostrar todos los empleados\n";
        cout << "3. Actualizar empleado\n";
        cout << "4. Eliminar empleado\n";
        cout << "0. Volver al menú principal\n\n";
        opcion = validarOpcionMenu("Ingrese una opcion: ", 0, 4);

        switch (opcion) {
            case 1:
                ingresarEmpleado();
                break;
            case 2:
                mostrarEmpleados();
                break;
            case 3:
                actualizarEmpleado();
                break;
            case 4:
                eliminarEmpleado();
                break;
            case 0:
                // Volver al menú principal
                break;
            default:
                cout << "\nOpción no válida. Intente de nuevo.\n";
                pausar();
        }
    } while (opcion != 0);
}

void ingresarEmpleado() {
    limpiarPantalla();
    cout << "===== INGRESAR NUEVO EMPLEADO =====\n\n";

    // Mostrar puestos disponibles
    cout << "Puestos disponibles:\n";
    Puesto p = Puesto();
    p.leer();

    string nombres, apellidos, direccion, telefono, DPI, fecha_nacimiento, fecha_inicio_labores, fecha_ingreso;
    bool genero = false;
    short idPuesto = 0;
    char gen;

    cout << "Ingrese Nombres: ";
    getline(cin, nombres);
    cout << "Ingrese Apellidos: ";
    getline(cin, apellidos);
    cout << "Ingrese Dirección: ";
    getline(cin, direccion);
    cout << "Ingrese Teléfono: ";
    getline(cin, telefono);
    cout << "Ingrese DPI: ";
    getline(cin, DPI);
    cout << "Ingrese Género (M/F): ";
    cin >> gen;
    genero = (gen == 'M' || gen == 'm') ? true : false;
    cin.ignore();
    cout << "Ingrese Fecha de Nacimiento (YYYY-MM-DD): ";
    getline(cin, fecha_nacimiento);
    cout << "Ingrese ID de Puesto: ";
    cin >> idPuesto;
    cin.ignore();
    cout << "Ingrese Fecha de Inicio de Labores (YYYY-MM-DD): ";
    getline(cin, fecha_inicio_labores);

    // Obtener fecha actual para fecha_ingreso
    fecha_ingreso = obtenerFechaHoraActual();

    // Usamos 0 como valor temporal para idEmpleado, ya que será generado por la base de datos
    Empleado e = Empleado(0, nombres, apellidos, direccion, telefono, DPI, genero, fecha_nacimiento, idPuesto, fecha_inicio_labores, fecha_ingreso);
    e.crear();

    pausar();
}

void mostrarEmpleados() {
    limpiarPantalla();
    cout << "===== LISTADO DE EMPLEADOS =====\n\n";

    Empleado e = Empleado();
    e.leer();

    pausar();
}

void actualizarEmpleado() {
    limpiarPantalla();
    cout << "===== ACTUALIZAR EMPLEADO =====\n\n";

    Empleado e = Empleado();
    e.leer();

    // Mostrar puestos disponibles
    cout << "\nPuestos disponibles:\n";
    Puesto p = Puesto();
    p.leer();

    int idEmpleado = 0;
    string nombres, apellidos, direccion, telefono, DPI, fecha_nacimiento, fecha_inicio_labores, fecha_ingreso;
    bool genero = false;
    short idPuesto = 0;
    char gen;

    cout << "\nIngrese el ID del empleado a modificar: ";
    cin >> idEmpleado;
    cin.ignore();
    cout << "Ingrese Nuevos Nombres: ";
    getline(cin, nombres);
    cout << "Ingrese Nuevos Apellidos: ";
    getline(cin, apellidos);
    cout << "Ingrese Nueva Dirección: ";
    getline(cin, direccion);
    cout << "Ingrese Nuevo Teléfono: ";
    getline(cin, telefono);
    cout << "Ingrese Nuevo DPI: ";
    getline(cin, DPI);
    cout << "Ingrese Nuevo Género (M/F): ";
    cin >> gen;
    genero = (gen == 'M' || gen == 'm') ? true : false;
    cin.ignore();
    cout << "Ingrese Nueva Fecha de Nacimiento (YYYY-MM-DD): ";
    getline(cin, fecha_nacimiento);
    cout << "Ingrese Nuevo ID de Puesto: ";
    cin >> idPuesto;
    cin.ignore();
    cout << "Ingrese Nueva Fecha de Inicio de Labores (YYYY-MM-DD): ";
    getline(cin, fecha_inicio_labores);

    // Obtener fecha actual para fecha_ingreso
    fecha_ingreso = obtenerFechaHoraActual();

    e = Empleado(idEmpleado, nombres, apellidos, direccion, telefono, DPI, genero, fecha_nacimiento, idPuesto, fecha_inicio_labores, fecha_ingreso);
    e.actualizar();

    cout << "\nDatos actualizados:\n";
    e.leer();

    pausar();
}

void eliminarEmpleado() {
    limpiarPantalla();
    cout << "===== ELIMINAR EMPLEADO =====\n\n";

    Empleado e = Empleado();
    e.leer();

    int idEmpleado = 0;
    cout << "\nIngrese el ID del empleado a eliminar: ";
    cin >> idEmpleado;

    char confirmar;
    cout << "¿Está seguro de eliminar este registro? (S/N): ";
    cin >> confirmar;

    if (confirmar == 'S' || confirmar == 's') {
        e.setIdEmpleado(idEmpleado);
        e.eliminar();
        cout << "\nRegistro eliminado. Lista actualizada:\n";
        e.leer();
    } else {
        cout << "\nOperación cancelada.\n";
    }

    pausar();
}



// Implementación de funciones para Cliente
void menuClientes() {
    int opcion = 0;
    do {
        limpiarPantalla();
        cout << "======================================\n";
        cout << "         GESTIÓN DE CLIENTES         \n";
        cout << "======================================\n\n";
        cout << "1. Ingresar nuevo cliente\n";
        cout << "2. Mostrar todos los clientes\n";
        cout << "3. Buscar cliente por NIT\n";
        cout << "4. Actualizar cliente\n";
        cout << "5. Eliminar cliente\n";
        cout << "0. Volver al menú principal\n\n";
        opcion = validarOpcionMenu("Ingrese una opcion: ", 0, 5);

        switch (opcion) {
            case 1:
                ingresarCliente();
                break;
            case 2:
                mostrarClientes();
                break;
            case 3:
                buscarClientePorNIT();
                break;
            case 4:
                actualizarCliente();
                break;
            case 5:
                eliminarCliente();
                break;
            case 0:
                // Volver al menú principal
                break;
            default:
                cout << "\nOpción no válida. Intente de nuevo.\n";
                pausar();
        }
    } while (opcion != 0);
}

void ingresarCliente() {
    limpiarPantalla();
    cout << "===== INGRESAR NUEVO CLIENTE =====\n\n";

    string nombres, apellidos, NIT, telefono, correo_electronico, fecha_ingreso;
    bool genero = false;
    char gen;

    cout << "Ingrese Nombres: ";
    getline(cin, nombres);
    cout << "Ingrese Apellidos: ";
    getline(cin, apellidos);
    cout << "Ingrese NIT: ";
    getline(cin, NIT);
    cout << "Ingrese Género (M/F): ";
    cin >> gen;
    genero = (gen == 'M' || gen == 'm') ? true : false;
    cin.ignore();
    cout << "Ingrese Teléfono: ";
    getline(cin, telefono);
    cout << "Ingrese Correo Electrónico: ";
    getline(cin, correo_electronico);

    // Obtener fecha actual para fecha_ingreso
    fecha_ingreso = obtenerFechaHoraActual();

    // Usamos 0 como valor temporal para idCliente, ya que será generado por la base de datos
    Cliente c = Cliente(0, nombres, apellidos, NIT, genero, telefono, correo_electronico, fecha_ingreso);
    c.crear();

    pausar();
}

bool ingresarClienteConNIT(const string& nitPreestablecido) {
    limpiarPantalla();
    cout << "===== CREAR NUEVO CLIENTE =====\n\n";
    cout << "NIT: " << nitPreestablecido << " \n";

    try {
        string nombres, apellidos, telefono, correo_electronico, fecha_ingreso;
        bool genero = false;
        char gen;

        nombres = validarTextoNoVacio("Ingrese Nombres: ");
        apellidos = validarTextoNoVacio("Ingrese Apellidos: ");

        cout << "Ingrese Genero (M/F): ";
        cin >> gen;
        genero = (gen == 'M' || gen == 'm') ? true : false;
        cin.ignore();

        telefono = validarTextoNoVacio("Ingrese Telefono: ");
        correo_electronico = validarTextoNoVacio("Ingrese Correo Electronico: ");

        // Obtener fecha actual para fecha_ingreso
        fecha_ingreso = obtenerFechaHoraActual();

        // Crear cliente directamente con consulta SQL para mejor control
        ConexionBD cn = ConexionBD();
        cn.abrir_conexion();
        if (!cn.getConector()) {
            cout << "Error: No se pudo conectar a la base de datos.\n";
            pausar();
            return false;
        }

        string gen_str = genero ? "1" : "0";
        string consulta = "INSERT INTO Clientes(nombres, apellidos, NIT, genero, telefono, correo_electronico, fecha_ingreso) VALUES ('" +
                         nombres + "', '" + apellidos + "', '" + nitPreestablecido + "', " + gen_str + ", '" +
                         telefono + "', '" + correo_electronico + "', '" + fecha_ingreso + "');";

        int q_estado = mysql_query(cn.getConector(), consulta.c_str());
        cn.cerrar_conexion();

        if (q_estado == 0) {
            cout << "\nCliente creado exitosamente con NIT: " << nitPreestablecido << endl;
            pausar();
            return true;
        } else {
            cout << "Error al crear el cliente.\n";
            pausar();
            return false;
        }
    } catch (const exception& e) {
        cout << "Error inesperado al crear cliente: " << e.what() << "\n";
        pausar();
        return false;
    }
}

void mostrarClientes() {
    limpiarPantalla();
    cout << "===== LISTADO DE CLIENTES =====\n\n";

    Cliente c = Cliente();
    c.leer();

    pausar();
}

void actualizarCliente() {
    limpiarPantalla();
    cout << "===== ACTUALIZAR CLIENTE =====\n\n";

    Cliente c = Cliente();
    c.leer();

    int idCliente = 0;
    string nombres, apellidos, NIT, telefono, correo_electronico, fecha_ingreso;
    bool genero = false;
    char gen;

    cout << "\nIngrese el ID del cliente a modificar: ";
    cin >> idCliente;
    cin.ignore();
    cout << "Ingrese Nuevos Nombres: ";
    getline(cin, nombres);
    cout << "Ingrese Nuevos Apellidos: ";
    getline(cin, apellidos);
    cout << "Ingrese Nuevo NIT: ";
    getline(cin, NIT);
    cout << "Ingrese Nuevo Género (M/F): ";
    cin >> gen;
    genero = (gen == 'M' || gen == 'm') ? true : false;
    cin.ignore();
    cout << "Ingrese Nuevo Teléfono: ";
    getline(cin, telefono);
    cout << "Ingrese Nuevo Correo Electrónico: ";
    getline(cin, correo_electronico);

    // Obtener fecha actual para fecha_ingreso
    fecha_ingreso = obtenerFechaHoraActual();

    c = Cliente(idCliente, nombres, apellidos, NIT, genero, telefono, correo_electronico, fecha_ingreso);
    c.actualizar();

    cout << "\nDatos actualizados:\n";
    c.leer();

    pausar();
}

void eliminarCliente() {
    limpiarPantalla();
    cout << "===== ELIMINAR CLIENTE =====\n\n";

    Cliente c = Cliente();
    c.leer();

    int idCliente = 0;
    cout << "\nIngrese el ID del cliente a eliminar: ";
    cin >> idCliente;

    char confirmar;
    cout << "¿Está seguro de eliminar este registro? (S/N): ";
    cin >> confirmar;

    if (confirmar == 'S' || confirmar == 's') {
        c.setIdCliente(idCliente);
        c.eliminar();
        cout << "\nRegistro eliminado. Lista actualizada:\n";
        c.leer();
    } else {
        cout << "\nOperación cancelada.\n";
    }

    pausar();
}

// Funciones para Compra
void menuCompras();
void ingresarCompra();
void mostrarCompras();
void mostrarDetallesCompra();
void actualizarCompra();
void eliminarCompra();

// Funciones para Compra_detalle
void menuDetallesCompra();
void ingresarDetalleCompra();
void mostrarDetallesCompraIndividual();
void actualizarDetalleCompra();
void eliminarDetalleCompra();


// Implementación de funciones para Compra
void menuCompras() {
    int opcion = 0;
    do {
        limpiarPantalla();
        cout << "======================================\n";
        cout << "         GESTIÓN DE COMPRAS          \n";
        cout << "======================================\n\n";
        cout << "1. Ingresar nueva compra\n";
        cout << "2. Mostrar todas las compras\n";
        cout << "3. Ver detalles de una compra\n";
        cout << "4. Actualizar compra\n";
        cout << "5. Eliminar compra\n";
        cout << "6. Gestionar detalles de compra\n";
        cout << "7. Generar factura de compra\n";
        cout << "0. Volver al menú principal\n\n";
        opcion = validarOpcionMenu("Ingrese una opcion: ", 0, 7);

        switch (opcion) {
            case 1:
                ingresarCompra();
                break;
            case 2:
                mostrarCompras();
                break;
            case 3:
                mostrarDetallesCompra();
                break;
            case 4:
                actualizarCompra();
                break;
            case 5:
                eliminarCompra();
                break;
            case 6:
                menuDetallesCompra();
                break;
            case 7:
                {
                    limpiarPantalla();
                    cout << "===== GENERAR FACTURA PDF DE COMPRA =====\n\n";
                    Compra c = Compra();
                    c.leer();
                    int idcompra = 0;
                    cout << "\nIngrese el ID de la compra para generar factura PDF: ";
                    cin >> idcompra;
                    generarFacturaPDF(idcompra);
                    pausar();
                }
                break;
            case 0:
                // Volver al menú principal
                break;
            default:
                cout << "\nOpción no válida. Intente de nuevo.\n";
                pausar();
        }
    } while (opcion != 0);
}

void ingresarCompra() {
    limpiarPantalla();
    cout << "===== INGRESAR NUEVA COMPRA =====\n\n";

    try {
        // Mostrar proveedores disponibles
        cout << "PROVEEDORES DISPONIBLES:\n";
        Proveedor prov = Proveedor();
        prov.leer();

        int idproveedor = validarEnteroPositivo("\nIngrese ID de Proveedor: ");
        string fecha_order, fecha_ingreso;

        // Generar número de orden de compra automáticamente
        string numeroOrden = generarNumeroOrdenCompra();
        int no_order_compra = stoi(numeroOrden);

        cout << "\nINFORMACION DE LA COMPRA:\n";
        cout << "Numero de orden de compra generado automaticamente: " << no_order_compra << endl;

        fecha_order = validarFecha("Ingrese Fecha de Orden (YYYY-MM-DD): ");

        // Obtener fecha actual para fecha_ingreso
        fecha_ingreso = obtenerFechaHoraActual();

        // Usamos 0 como valor temporal para idcompra, ya que será generado por la base de datos
        Compra c = Compra(0, no_order_compra, idproveedor, fecha_order, fecha_ingreso);

        // Agregar detalles de compra con validaciones
        char agregarDetalle = 'S';
        bool primerDetalle = true;

        while (agregarDetalle == 'S' || agregarDetalle == 's') {
            if (primerDetalle) {
                cout << "\nPRODUCTOS DISPONIBLES:\n";
                Producto prod = Producto();
                prod.leer();
                primerDetalle = false;
            }

            cout << "\n--- AGREGAR DETALLE DE COMPRA ---\n";
            DetalleCompra detalle;

            // Usamos 0 como valor temporal para idcompra_detalle, ya que será generado por la base de datos
            detalle.idcompra_detalle = 0;
            detalle.idproducto = validarEnteroPositivo("Ingrese ID de Producto: ");
            detalle.cantidad = validarEnteroPositivo("Ingrese Cantidad: ");

            // Obtener precio de costo automáticamente de la base de datos
            ConexionBD cn = ConexionBD();
            cn.abrir_conexion();
            if (!cn.getConector()) {
                cout << "Error de conexion a la base de datos.\n";
                pausar();
                return;
            }

            string consulta = "SELECT precio_costo, producto FROM Productos WHERE idProducto = " + to_string(detalle.idproducto) + ";";
            const char* sql = consulta.c_str();
            int q_estado = mysql_query(cn.getConector(), sql);

            if (q_estado) {
                manejarErrorBD("Buscar producto", consulta, cn.getConector());
                cn.cerrar_conexion();
                continue; // Continuar con el siguiente detalle
            }

            MYSQL_RES* resultado = mysql_store_result(cn.getConector());
            if (resultado) {
                MYSQL_ROW fila = mysql_fetch_row(resultado);
                if (fila) {
                    detalle.precio_unitario = atof(fila[0]);
                    double subtotal = detalle.cantidad * detalle.precio_unitario;
                    cout << "Producto: " << fila[1] << "\n";
                    cout << "Precio de costo:" << formatearMoneda(detalle.precio_unitario) << "\n";
                    cout << "Subtotal: " << formatearMoneda(subtotal) << "\n";
                } else {
                    cout << "Producto no encontrado con ID: " << detalle.idproducto << "\n";
                    mysql_free_result(resultado);
                    cn.cerrar_conexion();
                    continue; // Continuar con el siguiente detalle
                }
                mysql_free_result(resultado);
            }
            cn.cerrar_conexion();

            c.agregarDetalle(detalle);

            cout << "\n¿Desea agregar otro detalle? (S/N): ";
            cin >> agregarDetalle;
            cin.ignore();
        }

        // Crear la compra y obtener el ID generado
        cout << "\nGuardando compra...\n";
        int idCompraCreada = c.crear();

        // Generar factura PDF automáticamente si la compra fue exitosa
        if (idCompraCreada > 0) {
            cout << "\nCompra creada exitosamente!\n";
            cout << "Generando factura de compra en PDF...\n";
            generarFacturaPDF(idCompraCreada);
        } else {
            cout << "Error al crear la compra. No se generara PDF.\n";
        }

    } catch (const exception& e) {
        cout << "Error inesperado: " << e.what() << "\n";
    }

    pausar();
}

void mostrarCompras() {
    limpiarPantalla();
    cout << "===== LISTADO DE COMPRAS =====\n\n";

    Compra c = Compra();
    c.leer();

    pausar();
}

void mostrarDetallesCompra() {
    limpiarPantalla();
    cout << "===== DETALLES DE COMPRA =====\n\n";

    Compra c = Compra();
    c.leer();

    int idcompra = validarEnteroPositivo("\nIngrese el ID de la compra para ver detalles: ");

    c.leerDetalles(idcompra);

    pausar();
}

void actualizarCompra() {
    limpiarPantalla();
    cout << "===== ACTUALIZAR COMPRA =====\n\n";

    Compra c = Compra();
    c.leer();

    // Mostrar proveedores disponibles
    cout << "\nProveedores disponibles:\n";
    Proveedor prov = Proveedor();
    prov.leer();

    int idcompra = 0, no_order_compra = 0, idproveedor = 0;
    string fecha_order, fecha_ingreso;

    cout << "\nIngrese el ID de la compra a modificar: ";
    cin >> idcompra;
    cout << "Ingrese Nuevo No. de Orden de Compra: ";
    cin >> no_order_compra;
    cout << "Ingrese Nuevo ID de Proveedor: ";
    cin >> idproveedor;
    cin.ignore();
    cout << "Ingrese Nueva Fecha de Orden (YYYY-MM-DD): ";
    getline(cin, fecha_order);

    // Obtener fecha actual para fecha_ingreso
    fecha_ingreso = obtenerFechaHoraActual();

    c = Compra(idcompra, no_order_compra, idproveedor, fecha_order, fecha_ingreso);
    c.actualizar();

    cout << "\nDatos actualizados:\n";
    c.leer();

    pausar();
}

void eliminarCompra() {
    limpiarPantalla();
    cout << "===== ELIMINAR COMPRA =====\n\n";

    Compra c = Compra();
    c.leer();

    int idcompra = validarEnteroPositivo("\nIngrese el ID de la compra a eliminar: ");

    if (confirmarOperacion("¿Está seguro de eliminar este registro y todos sus detalles?")) {
        c.setIdCompra(idcompra);
        c.eliminar();
        cout << "\nRegistro eliminado. Lista actualizada:\n";
        c.leer();
    } else {
        cout << "\nOperacion cancelada.\n";
    }

    pausar();
}



// Implementación de funciones para Venta
void menuVentas() {
    int opcion = 0;
    do {
        limpiarPantalla();
        cout << "======================================\n";
        cout << "          GESTIÓN DE VENTAS          \n";
        cout << "======================================\n\n";
        cout << "1. Ingresar nueva venta\n";
        cout << "2. Mostrar todas las ventas\n";
        cout << "3. Ver detalles de una venta\n";
        cout << "4. Actualizar venta\n";
        cout << "5. Eliminar venta\n";
        cout << "6. Gestionar detalles de venta\n";
        cout << "7. Buscar cliente por NIT\n";
        cout << "9. Generar factura PDF\n";
        cout << "0. Volver al menú principal\n\n";
        opcion = validarOpcionMenu("Ingrese una opcion: ", 0, 9);

        switch (opcion) {
            case 1:
                ingresarVenta();
                break;
            case 2:
                mostrarVentas();
                break;
            case 3:
                mostrarDetallesVenta();
                break;
            case 4:
                actualizarVenta();
                break;
            case 5:
                eliminarVenta();
                break;
            case 6:
                menuDetallesVenta();
                break;
            case 7:
                buscarClientePorNIT();
                break;
            case 8:
                imprimirFactura();
                break;
            case 9:
                {
                    limpiarPantalla();
                    cout << "===== GENERAR FACTURA PDF DE VENTA =====\n\n";
                    Venta v = Venta();
                    v.leer();
                    int idVenta = validarEnteroPositivo("\nIngrese el ID de la venta para generar factura PDF: ");
                    generarFacturaVentaPDF(idVenta);
                    pausar();
                }
                break;
            case 0:
                // Volver al menú principal
                break;
            default:
                cout << "\nOpción no válida. Intente de nuevo.\n";
                pausar();
        }
    } while (opcion != 0);
}

void ingresarVenta() {
    limpiarPantalla();
    cout << "===== INGRESAR NUEVA VENTA =====\n\n";

    try {
        // Buscar cliente por NIT con validación mejorada
        string nitCliente;
        int idcliente = 0;
        bool clienteEncontrado = false;

        do {
            cout << "\n=== BUSQUEDA DE CLIENTE ===\n";
            cout << "Ingrese el NIT del cliente\n";
            cout << "(Si no tiene NIT, ingrese: C/F para consumidor final)\n";
            nitCliente = validarTextoNoVacio("NIT del cliente: ");

            // Validar y buscar cliente
            string nitValidado = validarFormatoNIT(nitCliente);
            if (nitValidado.empty()) {
                cout << "\nFormato de NIT invalido. Intente nuevamente.\n";
                cout << "Formatos validos:\n";
                cout << "- NIT: 8 a 13 digitos (ej: 12345678, 1234567890123)\n";
                cout << "- Consumidor final: C/F\n\n";
                continue;
            }

            // Caso especial para consumidor final (C/F)
            if (nitValidado == "C/F") {
                cout << "Consumidor final detectado. Creando cliente con NIT: C/F\n";
                cout << "\n=== CREAR CLIENTE CONSUMIDOR FINAL ===\n";
                cout << "NIT: C/F (Consumidor Final)\n";

                // Solicitar todos los datos como un cliente normal
                string nombres, apellidos, telefono, correo_electronico;
                bool genero = false;
                char gen;

                nombres = validarTextoNoVacio("Ingrese Nombres: ");
                apellidos = validarTextoNoVacio("Ingrese Apellidos: ");

                cout << "Ingrese Genero (M/F): ";
                cin >> gen;
                genero = (gen == 'M' || gen == 'm') ? true : false;
                cin.ignore();

                telefono = validarTextoNoVacio("Ingrese Telefono: ");
                correo_electronico = validarTextoNoVacio("Ingrese Correo Electronico: ");

                string fecha_ingreso = obtenerFechaHoraActual();

                // Usar la clase Cliente directamente como en el CRUD que funciona
                Cliente clienteNuevo = Cliente(0, nombres, apellidos, "C/F", genero, telefono, correo_electronico, fecha_ingreso);
                clienteNuevo.crear();

                cout << "\nCliente consumidor final creado exitosamente!" << endl;
                cout << "Nombre: " << nombres << " " << apellidos << endl;
                clienteEncontrado = true;

                // Buscar el cliente recién creado usando una consulta simple
                ConexionBD cnBuscar = ConexionBD();
                cnBuscar.abrir_conexion();
                if (cnBuscar.getConector()) {
                    string consultaBuscar = "SELECT idCliente FROM Clientes WHERE NIT = 'C/F' ORDER BY idCliente DESC LIMIT 1;";
                    const char* cb = consultaBuscar.c_str();
                    int q_estado_buscar = mysql_query(cnBuscar.getConector(), cb);
                    if (!q_estado_buscar) {
                        MYSQL_RES* resultado = mysql_store_result(cnBuscar.getConector());
                        if (resultado != nullptr) {
                            MYSQL_ROW fila = mysql_fetch_row(resultado);
                            if (fila != nullptr) {
                                idcliente = atoi(fila[0]);
                                cout << "ID del cliente: " << idcliente << endl;
                            }
                            // No liberar resultado manualmente, dejar que MySQL lo maneje automáticamente
                        }
                    }
                }
                // No cerrar conexión aquí, dejar que el destructor lo maneje

                continue; // Continuar con el bucle
            }

            // Para NITs normales, buscar en la base de datos
            ConexionBD cn = ConexionBD();
            cn.abrir_conexion();
            if (!cn.getConector()) {
                cout << "Error de conexion a la base de datos.\n";
                pausar();
                return;
            }

            string consulta;

            // Si es C/F, buscar por nombre y apellido
            if (nitValidado == "C/F") {
                cout << "\n=== BUSQUEDA DE CONSUMIDOR FINAL ===\n";
                cout << "Como hay varios clientes con NIT C/F, buscaremos por nombre:\n";

                string nombreBuscar = validarTextoNoVacio("Ingrese el nombre del cliente: ");
                string apellidoBuscar = validarTextoNoVacio("Ingrese el apellido del cliente: ");

                consulta = "SELECT idCliente, nombres, apellidos FROM Clientes WHERE NIT = 'C/F' AND nombres LIKE '%" + nombreBuscar + "%' AND apellidos LIKE '%" + apellidoBuscar + "%';";
                cout << "\nBuscando cliente consumidor final: " << nombreBuscar << " " << apellidoBuscar << endl;
            } else {
                // Para NITs normales
                consulta = "SELECT idCliente, nombres, apellidos FROM Clientes WHERE NIT = '" + nitValidado + "';";
            }

            const char* sql = consulta.c_str();
            int q_estado = mysql_query(cn.getConector(), sql);

            if (q_estado) {
                manejarErrorBD("Buscar cliente", consulta, cn.getConector());
                pausar();
                return;
            }

            MYSQL_RES* resultado = mysql_store_result(cn.getConector());
            if (resultado) {
                MYSQL_ROW fila = mysql_fetch_row(resultado);
                if (fila) {
                    idcliente = atoi(fila[0]);
                    cout << "Cliente encontrado: " << fila[1] << " " << fila[2] << endl;
                    clienteEncontrado = true;
                } else {
                    cout << "Cliente no encontrado con NIT: " << nitValidado << endl;
                    if (confirmarOperacion("¿Desea crear un nuevo cliente con este NIT?")) {
                        // No liberar resultado manualmente, dejar que MySQL lo maneje automáticamente
                        // No cerrar conexión aquí, dejar que el destructor lo maneje

                        // Crear cliente usando la clase Cliente directamente
                        cout << "\n=== CREAR NUEVO CLIENTE ===\n";
                        cout << "NIT: " << nitValidado << "\n";

                        string nombres, apellidos, telefono, correo_electronico;
                        bool genero = false;
                        char gen;

                        nombres = validarTextoNoVacio("Ingrese Nombres: ");
                        apellidos = validarTextoNoVacio("Ingrese Apellidos: ");

                        cout << "Ingrese Genero (M/F): ";
                        cin >> gen;
                        genero = (gen == 'M' || gen == 'm') ? true : false;
                        cin.ignore();

                        telefono = validarTextoNoVacio("Ingrese Telefono: ");
                        correo_electronico = validarTextoNoVacio("Ingrese Correo Electronico: ");

                        string fecha_ingreso = obtenerFechaHoraActual();

                        // Usar la clase Cliente directamente como en el CRUD que funciona
                        Cliente clienteNuevo = Cliente(0, nombres, apellidos, nitValidado, genero, telefono, correo_electronico, fecha_ingreso);
                        clienteNuevo.crear();

                        cout << "\nCliente creado exitosamente!" << endl;
                        cout << "Nombre: " << nombres << " " << apellidos << endl;
                        clienteEncontrado = true;

                        // Buscar el cliente recién creado usando una consulta simple
                        ConexionBD cnBuscar = ConexionBD();
                        cnBuscar.abrir_conexion();
                        if (cnBuscar.getConector()) {
                            string consultaBuscar = "SELECT idCliente FROM Clientes WHERE NIT = '" + nitValidado + "';";
                            const char* cb = consultaBuscar.c_str();
                            int q_estado_buscar = mysql_query(cnBuscar.getConector(), cb);
                            if (!q_estado_buscar) {
                                MYSQL_RES* resultado = mysql_store_result(cnBuscar.getConector());
                                if (resultado != nullptr) {
                                    MYSQL_ROW fila = mysql_fetch_row(resultado);
                                    if (fila != nullptr) {
                                        idcliente = atoi(fila[0]);
                                        cout << "ID del cliente: " << idcliente << endl;
                                    }
                                    // No liberar resultado manualmente, dejar que MySQL lo maneje automáticamente
                                }
                            }
                        }
                        // No cerrar conexión aquí, dejar que el destructor lo maneje
                    } else {
                        cout << "Intente con otro NIT o cancele la operacion.\n";
                    }
                }
            }
        } while (!clienteEncontrado);

        // Mostrar empleados disponibles
        cout << "\nEmpleados disponibles:\n";
        Empleado e = Empleado();
        e.leer();

        int idempleado = validarEnteroPositivo("\nIngrese ID de Empleado: ");
        char serie = 'A'; // Serie por defecto
        string fechafactura, fecha_ingreso;

        // Generar número de factura automáticamente
        string numeroFactura = generarCorrelativoFactura();
        int nofactura = stoi(numeroFactura);

        cout << "\nINFORMACION DE LA FACTURA:\n";
        cout << "Numero de factura generado automaticamente: " << nofactura << endl;
        cout << "Serie asignada automaticamente: " << serie << endl;

        // Obtener fecha actual para fecha de factura y fecha_ingreso
        fechafactura = obtenerFechaHoraActual().substr(0, 10); // Solo la fecha, sin la hora
        fecha_ingreso = obtenerFechaHoraActual();
        cout << "Fecha de factura: " << fechafactura << endl;

        // Usamos 0 como valor temporal para idVenta, ya que será generado por la base de datos
        Venta v = Venta(0, nofactura, serie, fechafactura, idcliente, idempleado, fecha_ingreso);

        // Agregar detalles de venta con validaciones
        char agregarDetalle = 'S';
        bool primerDetalle = true;

        while (agregarDetalle == 'S' || agregarDetalle == 's') {
            if (primerDetalle) {
                cout << "\nPRODUCTOS DISPONIBLES:\n";
                Producto prod = Producto();
                prod.leer();
                primerDetalle = false;
            }

            cout << "\n--- AGREGAR DETALLE DE VENTA ---\n";
            DetalleVenta detalle;

            // Usamos 0 como valor temporal para idventa_detalle, ya que será generado por la base de datos
            detalle.idventa_detalle = 0;
            detalle.idProducto = validarEnteroPositivo("Ingrese ID de Producto: ");

            // Validar cantidad como string pero asegurar que sea numérica
            bool cantidadValida = false;
            do {
                detalle.cantidad = validarTextoNoVacio("Ingrese Cantidad: ");

                // Verificar que la cantidad sea un número válido
                try {
                    double cantidadNum = stod(detalle.cantidad);
                    if (cantidadNum > 0) {
                        cantidadValida = true;
                    } else {
                        cout << "Error: La cantidad debe ser mayor a 0. Intente nuevamente.\n";
                    }
                } catch (const exception& e) {
                    cout << "Error: La cantidad debe ser un numero valido. Intente nuevamente.\n";
                }
            } while (!cantidadValida);

            // Obtener precio de venta automáticamente de la base de datos
            ConexionBD cn = ConexionBD();
            cn.abrir_conexion();
            if (!cn.getConector()) {
                cout << "Error de conexion a la base de datos.\n";
                pausar();
                return;
            }

            string consulta = "SELECT precio_venta, producto FROM Productos WHERE idProducto = " + to_string(detalle.idProducto) + ";";
            const char* sql = consulta.c_str();
            int q_estado = mysql_query(cn.getConector(), sql);

            if (q_estado) {
                manejarErrorBD("Buscar producto", consulta, cn.getConector());
                cout << "\n¿Desea intentar con otro producto? (S/N): ";
                cin >> agregarDetalle;
                cin.ignore();
                continue; // Continuar con el siguiente detalle
            }

            MYSQL_RES* resultado = mysql_store_result(cn.getConector());
            bool productoEncontrado = false;
            if (resultado != nullptr) {
                MYSQL_ROW fila = mysql_fetch_row(resultado);
                if (fila != nullptr) {
                    detalle.precio_unitario = atof(fila[0]);
                    double subtotal = stod(detalle.cantidad) * detalle.precio_unitario;
                    cout << "Producto: " << fila[1] << "\n";
                    cout << "Precio unitario: " << formatearMoneda(detalle.precio_unitario) << "\n";
                    cout << "Subtotal: " << formatearMoneda(subtotal) << "\n";
                    productoEncontrado = true;
                } else {
                    cout << "Producto no encontrado con ID: " << detalle.idProducto << "\n";
                }
                // No liberar resultado manualmente, dejar que MySQL lo maneje automáticamente
            }
            // No cerrar conexión manualmente, el destructor lo hará automáticamente

            if (!productoEncontrado) {
                cout << "\n¿Desea intentar con otro producto? (S/N): ";
                cin >> agregarDetalle;
                cin.ignore();
                continue; // Continuar con el siguiente detalle
            }

            // Solo agregar el detalle si el producto fue encontrado
            v.agregarDetalle(detalle);

            cout << "\n¿Desea agregar otro detalle? (S/N): ";
            cin >> agregarDetalle;
            cin.ignore();
        }

        // Verificar que se hayan agregado detalles
        if (v.getDetalles().empty()) {
            cout << "\nError: No se puede crear una venta sin productos. Debe agregar al menos un detalle.\n";
            pausar();
            return;
        }

        // Crear la venta
        cout << "\nGuardando venta...\n";

        // Llamar al método crear y obtener el ID
        int idVentaCreada = v.crear();

        // Generar PDF automáticamente si la venta fue exitosa
        if (idVentaCreada > 0) {
            cout << "\nVenta creada exitosamente!\n";
            cout << "Generando factura PDF automaticamente...\n";
            generarFacturaVentaPDF(idVentaCreada);
        } else {
            cout << "\nVenta creada exitosamente!\n";
            cout << "Para generar PDF, use la opcion 9 del menu de ventas.\n";
        }

    } catch (const exception& e) {
        cout << "Error inesperado: " << e.what() << "\n";
    }

    pausar();
}

void mostrarVentas() {
    limpiarPantalla();
    cout << "===== LISTADO DE VENTAS =====\n\n";

    Venta v = Venta();
    v.leer();

    pausar();
}

void mostrarDetallesVenta() {
    limpiarPantalla();
    cout << "===== DETALLES DE VENTA =====\n\n";

    Venta v = Venta();
    v.leer();

    int idVenta = validarEnteroPositivo("\nIngrese el ID de la venta para ver detalles: ");

    v.leerDetalles(idVenta);

    pausar();
}

void actualizarVenta() {
    limpiarPantalla();
    cout << "===== ACTUALIZAR VENTA =====\n\n";

    Venta v = Venta();
    v.leer();

    // Mostrar clientes disponibles
    cout << "\nClientes disponibles:\n";
    Cliente c = Cliente();
    c.leer();

    // Mostrar empleados disponibles
    cout << "\nEmpleados disponibles:\n";
    Empleado e = Empleado();
    e.leer();

    int idVenta = 0, nofactura = 0, idcliente = 0, idempleado = 0;
    char serie;
    string fechafactura, fecha_ingreso;

    cout << "\nIngrese el ID de la venta a modificar: ";
    cin >> idVenta;
    cout << "Ingrese Nuevo No. de Factura: ";
    cin >> nofactura;
    cout << "Ingrese Nueva Serie (A-Z): ";
    cin >> serie;
    cin.ignore();
    cout << "Ingrese Nueva Fecha de Factura (YYYY-MM-DD): ";
    getline(cin, fechafactura);
    cout << "Ingrese Nuevo ID de Cliente: ";
    cin >> idcliente;
    cout << "Ingrese Nuevo ID de Empleado: ";
    cin >> idempleado;

    // Obtener fecha actual para fecha_ingreso
    fecha_ingreso = obtenerFechaHoraActual();

    v = Venta(idVenta, nofactura, serie, fechafactura, idcliente, idempleado, fecha_ingreso);
    v.actualizar();

    cout << "\nDatos actualizados:\n";
    v.leer();

    pausar();
}

void eliminarVenta() {
    limpiarPantalla();
    cout << "===== ELIMINAR VENTA =====\n\n";

    Venta v = Venta();
    v.leer();

    int idVenta = validarEnteroPositivo("\nIngrese el ID de la venta a eliminar: ");

    if (confirmarOperacion("¿Está seguro de eliminar este registro y todos sus detalles?")) {
        v.setIdVenta(idVenta);
        v.eliminar();
        cout << "\nRegistro eliminado. Lista actualizada:\n";
        v.leer();
    } else {
        cout << "\nOperacion cancelada.\n";
    }

    pausar();
}



// Implementación de funciones para Marca
void menuMarcas() {
    int opcion = 0;
    do {
        limpiarPantalla();
        cout << "======================================\n";
        cout << "           GESTIÓN DE MARCAS         \n";
        cout << "======================================\n\n";
        cout << "1. Ingresar nueva marca\n";
        cout << "2. Mostrar todas las marcas\n";
        cout << "3. Actualizar marca\n";
        cout << "4. Eliminar marca\n";
        cout << "0. Volver al menú principal\n\n";
        opcion = validarOpcionMenu("Ingrese una opcion: ", 0, 4);

        switch (opcion) {
            case 1:
                ingresarMarca();
                break;
            case 2:
                mostrarMarcas();
                break;
            case 3:
                actualizarMarca();
                break;
            case 4:
                eliminarMarca();
                break;
            case 0:
                // Volver al menú principal
                break;
            default:
                cout << "\nOpción no válida. Intente de nuevo.\n";
                pausar();
        }
    } while (opcion != 0);
}

void ingresarMarca() {
    limpiarPantalla();
    cout << "===== INGRESAR NUEVA MARCA =====\n\n";

    string marca = validarTextoNoVacio("Ingrese Nombre de Marca: ");

    // Usamos 0 como valor temporal para idmarca, ya que será generado por la base de datos
    Marca m = Marca(0, marca);
    m.crear();

    pausar();
}

void mostrarMarcas() {
    limpiarPantalla();
    cout << "===== LISTADO DE MARCAS =====\n\n";

    Marca m = Marca();
    m.leer();

    pausar();
}

void actualizarMarca() {
    limpiarPantalla();
    cout << "===== ACTUALIZAR MARCA =====\n\n";

    Marca m = Marca();
    m.leer();

    short idmarca = validarEnteroPositivo("\nIngrese el ID de la marca a modificar: ");
    string marca = validarTextoNoVacio("Ingrese Nuevo Nombre de Marca: ");

    m = Marca(idmarca, marca);
    m.actualizar();

    cout << "\nDatos actualizados:\n";
    m.leer();

    pausar();
}

void eliminarMarca() {
    limpiarPantalla();
    cout << "===== ELIMINAR MARCA =====\n\n";

    Marca m = Marca();
    m.leer();

    short idmarca = validarEnteroPositivo("\nIngrese el ID de la marca a eliminar: ");

    if (confirmarOperacion("¿Está seguro de eliminar este registro?")) {
        m.setIdMarca(idmarca);
        m.eliminar();
        cout << "\nRegistro eliminado. Lista actualizada:\n";
        m.leer();
    } else {
        cout << "\nOperacion cancelada.\n";
    }

    pausar();
}

// Implementación de funciones para Puesto
void menuPuestos() {
    int opcion = 0;
    do {
        limpiarPantalla();
        cout << "======================================\n";
        cout << "           GESTIÓN DE PUESTOS        \n";
        cout << "======================================\n\n";
        cout << "1. Ingresar nuevo puesto\n";
        cout << "2. Mostrar todos los puestos\n";
        cout << "3. Actualizar puesto\n";
        cout << "4. Eliminar puesto\n";
        cout << "0. Volver al menú principal\n\n";
        opcion = validarOpcionMenu("Ingrese una opcion: ", 0, 4);

        switch (opcion) {
            case 1:
                ingresarPuesto();
                break;
            case 2:
                mostrarPuestos();
                break;
            case 3:
                actualizarPuesto();
                break;
            case 4:
                eliminarPuesto();
                break;
            case 0:
                // Volver al menú principal
                break;
            default:
                cout << "\nOpción no válida. Intente de nuevo.\n";
                pausar();
        }
    } while (opcion != 0);
}

void ingresarPuesto() {
    limpiarPantalla();
    cout << "===== INGRESAR NUEVO PUESTO =====\n\n";

    string puesto = validarTextoNoVacio("Ingrese Nombre de Puesto: ");

    // Usamos 0 como valor temporal para idPuesto, ya que será generado por la base de datos
    Puesto p = Puesto(0, puesto);
    p.crear();

    pausar();
}

void mostrarPuestos() {
    limpiarPantalla();
    cout << "===== LISTADO DE PUESTOS =====\n\n";

    Puesto p = Puesto();
    p.leer();

    pausar();
}

void actualizarPuesto() {
    limpiarPantalla();
    cout << "===== ACTUALIZAR PUESTO =====\n\n";

    Puesto p = Puesto();
    p.leer();

    short idPuesto = validarEnteroPositivo("\nIngrese el ID del puesto a modificar: ");
    string puesto = validarTextoNoVacio("Ingrese Nuevo Nombre de Puesto: ");

    p = Puesto(idPuesto, puesto);
    p.actualizar();

    cout << "\nDatos actualizados:\n";
    p.leer();

    pausar();
}

void eliminarPuesto() {
    limpiarPantalla();
    cout << "===== ELIMINAR PUESTO =====\n\n";

    Puesto p = Puesto();
    p.leer();

    short idPuesto = validarEnteroPositivo("\nIngrese el ID del puesto a eliminar: ");

    if (confirmarOperacion("¿Está seguro de eliminar este registro?")) {
        p.setIdPuesto(idPuesto);
        p.eliminar();
        cout << "\nRegistro eliminado. Lista actualizada:\n";
        p.leer();
    } else {
        cout << "\nOperacion cancelada.\n";
    }

    pausar();
}

// Función para el menú de detalles de compra
void menuDetallesCompra() {
    int opcion = 0;
    do {
        limpiarPantalla();
        cout << "======================================\n";
        cout << "      GESTIÓN DE DETALLES COMPRA     \n";
        cout << "======================================\n\n";
        cout << "1. Actualizar detalle de compra\n";
        cout << "2. Eliminar detalle de compra\n";
        cout << "0. Volver al menú de compras\n\n";
        cout << "Ingrese una opción: ";
        cin >> opcion;
        cin.ignore();

        switch (opcion) {
            case 1:
                actualizarDetalleCompra();
                break;
            case 2:
                eliminarDetalleCompra();
                break;
            case 0:
                // Volver al menú de compras
                break;
            default:
                cout << "\nOpción no válida. Intente de nuevo.\n";
                pausar();
        }
    } while (opcion != 0);
}

// Función para el menú de detalles de venta
void menuDetallesVenta() {
    int opcion = 0;
    do {
        limpiarPantalla();
        cout << "======================================\n";
        cout << "       GESTIÓN DE DETALLES VENTA     \n";
        cout << "======================================\n\n";
        cout << "1. Actualizar detalle de venta\n";
        cout << "2. Eliminar detalle de venta\n";
        cout << "0. Volver al menú de ventas\n\n";
        cout << "Ingrese una opción: ";
        cin >> opcion;
        cin.ignore();

        switch (opcion) {
            case 1:
                actualizarDetalleVenta();
                break;
            case 2:
                eliminarDetalleVenta();
                break;
            case 0:
                // Volver al menú de ventas
                break;
            default:
                cout << "\nOpción no válida. Intente de nuevo.\n";
                pausar();
        }
    } while (opcion != 0);
}

// Función para validar formato de NIT (versión simple para compatibilidad)
string validarFormatoNIT(string nit) {
    // Validación básica del NIT guatemalteco
    if (nit == "C/F" || nit == "c/f") {
        return "C/F"; // Consumidor final
    }

    // Eliminar espacios y guiones
    string nitLimpio = "";
    for (char c : nit) {
        if (c != ' ' && c != '-') {
            nitLimpio += c;
        }
    }

    // Verificar que tenga entre 8 y 13 dígitos
    if (nitLimpio.length() < 8 || nitLimpio.length() > 13) {
        return ""; // NIT inválido
    }

    // Verificar que solo contenga dígitos
    for (char c : nitLimpio) {
        if (c < '0' || c > '9') {
            return ""; // NIT inválido
        }
    }

    return nitLimpio;
}

// Función para validar formato de NIT para búsqueda (excluye C/F)
string validarFormatoNITParaBusqueda(string nit) {
    // No permitir C/F en búsquedas
    if (nit == "C/F" || nit == "c/f") {
        return ""; // NIT inválido para búsqueda
    }

    // Eliminar espacios y guiones
    string nitLimpio = "";
    for (char c : nit) {
        if (c != ' ' && c != '-') {
            nitLimpio += c;
        }
    }

    // Verificar que tenga entre 8 y 13 dígitos
    if (nitLimpio.length() < 8 || nitLimpio.length() > 13) {
        return ""; // NIT inválido
    }

    // Verificar que solo contenga dígitos
    for (char c : nitLimpio) {
        if (c < '0' || c > '9') {
            return ""; // NIT inválido
        }
    }

    return nitLimpio;
}

// Función para generar correlativo de factura automático
string generarCorrelativoFactura() {
    // Obtener fecha actual
    time_t now = time(0);
    struct tm timeinfo;
    localtime_s(&timeinfo, &now);

    // Generar número de factura basado en fecha y hora
    int numeroFactura = (timeinfo.tm_year + 1900) * 10000 +
                       (timeinfo.tm_mon + 1) * 100 +
                       timeinfo.tm_mday;

    return to_string(numeroFactura);
}

// Función para generar número de orden de compra automático
string generarNumeroOrdenCompra() {
    // Obtener fecha actual
    time_t now = time(0);
    struct tm timeinfo;
    localtime_s(&timeinfo, &now);

    // Generar número de orden basado en fecha y hora (diferente formato que factura)
    int numeroOrden = (timeinfo.tm_year + 1900 - 2000) * 1000000 +  // Año en 2 dígitos
                     (timeinfo.tm_mon + 1) * 10000 +                // Mes
                     timeinfo.tm_mday * 100 +                       // Día
                     timeinfo.tm_hour;                              // Hora

    return to_string(numeroOrden);
}

// Función para formatear números como moneda con comas
string formatearMoneda(double cantidad) {
    // Convertir a string con 2 decimales
    stringstream ss;
    ss << fixed << setprecision(2) << cantidad;
    string numero = ss.str();

    // Encontrar la posición del punto decimal
    size_t puntoDecimal = numero.find('.');
    string parteEntera = numero.substr(0, puntoDecimal);
    string parteDecimal = numero.substr(puntoDecimal);

    // Agregar comas a la parte entera (de derecha a izquierda)
    string resultado = "";
    int contador = 0;

    for (int i = parteEntera.length() - 1; i >= 0; i--) {
        if (contador > 0 && contador % 3 == 0) {
            resultado = "," + resultado;
        }
        resultado = parteEntera[i] + resultado;
        contador++;
    }

    return "Q " + resultado + parteDecimal;
}

// Función para generar PDF nativo usando solo C++
void generarFacturaPDF(int idcompra) {
    ConexionBD cn = ConexionBD();
    cn.abrir_conexion();

    if (!cn.getConector()) {
        cout << "Error en la conexión a la base de datos para generar factura.\n";
        return;
    }

    // Crear carpeta facturas si no existe
    system("if not exist \"facturas\" mkdir facturas");

    // Obtener información de la compra
    string consulta = "SELECT c.idcompra, c.no_order_compra, c.fecha_order, c.fecha_ingreso, "
                     "p.proveedor, p.nit as proveedor_nit, p.direccion, p.telefono "
                     "FROM Compras c "
                     "INNER JOIN Proveedores p ON c.idproveedor = p.idProveedor "
                     "WHERE c.idcompra = " + to_string(idcompra) + ";";

    const char* sql = consulta.c_str();
    int q_estado = mysql_query(cn.getConector(), sql);

    if (q_estado) {
        cout << "Error al consultar información de la compra.\n";
        cn.cerrar_conexion();
        return;
    }

    MYSQL_RES* resultado = mysql_store_result(cn.getConector());
    if (!resultado) {
        cout << "Error al obtener resultados de la compra.\n";
        cn.cerrar_conexion();
        return;
    }

    MYSQL_ROW fila = mysql_fetch_row(resultado);
    if (!fila) {
        cout << "No se encontró la compra especificada.\n";
        mysql_free_result(resultado);
        cn.cerrar_conexion();
        return;
    }

    // Generar nombre del archivo PDF
    string nombreArchivoPDF = "facturas/factura_compra_" + to_string(idcompra) + ".pdf";

    // Crear archivo PDF
    ofstream pdf(nombreArchivoPDF, ios::binary);
    if (!pdf.is_open()) {
        cout << "Error al crear el archivo PDF.\n";
        mysql_free_result(resultado);
        cn.cerrar_conexion();
        return;
    }

    // Escribir encabezado PDF
    pdf << "%PDF-1.4\n";
    pdf << "1 0 obj\n";
    pdf << "<<\n";
    pdf << "/Type /Catalog\n";
    pdf << "/Pages 2 0 R\n";
    pdf << ">>\n";
    pdf << "endobj\n\n";

    // Objeto de páginas
    pdf << "2 0 obj\n";
    pdf << "<<\n";
    pdf << "/Type /Pages\n";
    pdf << "/Kids [3 0 R]\n";
    pdf << "/Count 1\n";
    pdf << ">>\n";
    pdf << "endobj\n\n";

    // Página
    pdf << "3 0 obj\n";
    pdf << "<<\n";
    pdf << "/Type /Page\n";
    pdf << "/Parent 2 0 R\n";
    pdf << "/MediaBox [0 0 612 792]\n";  // Tamaño carta
    pdf << "/Contents 4 0 R\n";
    pdf << "/Resources <<\n";
    pdf << "/Font <<\n";
    pdf << "/F1 5 0 R\n";
    pdf << "/F2 6 0 R\n";
    pdf << ">>\n";
    pdf << ">>\n";
    pdf << ">>\n";
    pdf << "endobj\n\n";

    // Preparar contenido de la página
    stringstream contenido;

    // Título centrado
    contenido << "BT\n";
    contenido << "/F2 20 Tf\n";
    contenido << "180 750 Td\n";
    contenido << "(FACTURA DE COMPRA) Tj\n";
    contenido << "ET\n";

    // Línea horizontal debajo del título
    contenido << "50 735 m\n";
    contenido << "550 735 l\n";
    contenido << "S\n";

    // Información de la factura (sin tabla, solo texto organizado)
    contenido << "BT\n";
    contenido << "/F2 12 Tf\n";
    contenido << "50 710 Td\n";
    contenido << "(No Factura: " << fila[1] << ") Tj\n";
    contenido << "300 0 Td\n";
    contenido << "(Fecha: " << fila[2] << ") Tj\n";
    contenido << "ET\n";

    contenido << "BT\n";
    contenido << "/F1 11 Tf\n";
    contenido << "50 690 Td\n";
    contenido << "(NIT Proveedor: " << fila[5] << ") Tj\n";
    contenido << "ET\n";

    contenido << "BT\n";
    contenido << "/F1 11 Tf\n";
    contenido << "50 670 Td\n";
    contenido << "(Proveedor: " << fila[4] << ") Tj\n";
    contenido << "ET\n";

    contenido << "BT\n";
    contenido << "/F1 11 Tf\n";
    contenido << "50 650 Td\n";
    contenido << "(Direccion: " << fila[6] << ") Tj\n";
    contenido << "ET\n";

    contenido << "BT\n";
    contenido << "/F1 11 Tf\n";
    contenido << "50 630 Td\n";
    contenido << "(Fecha Ingreso: " << fila[3] << ") Tj\n";
    contenido << "ET\n";

    mysql_free_result(resultado);

    // Obtener detalles de la compra
    string consultaDetalles = "SELECT cd.cantidad, p.producto, m.marca, cd.precio_unitario, "
                             "(cd.cantidad * cd.precio_unitario) as subtotal "
                             "FROM Compras_detalle cd "
                             "INNER JOIN Productos p ON cd.idproducto = p.idProducto "
                             "INNER JOIN Marcas m ON p.idMarca = m.idmarca "
                             "WHERE cd.idcompra = " + to_string(idcompra) + ";";

    const char* sqlDetalles = consultaDetalles.c_str();
    q_estado = mysql_query(cn.getConector(), sqlDetalles);

    if (!q_estado) {
        MYSQL_RES* resultadoDetalles = mysql_store_result(cn.getConector());

        // Título de productos
        contenido << "BT\n";
        contenido << "/F2 14 Tf\n";
        contenido << "50 600 Td\n";
        contenido << "(PRODUCTOS) Tj\n";
        contenido << "ET\n";

        // Línea debajo del título de productos
        contenido << "50 590 m\n";
        contenido << "550 590 l\n";
        contenido << "S\n";

        // Definir posiciones de columnas para la tabla
        int col1 = 50;   // Cantidad
        int col2 = 100;  // Producto
        int col3 = 320;  // Marca
        int col4 = 420;  // Precio Unitario
        int col5 = 500;  // Subtotal
        int colFin = 550; // Final de tabla

        // Tabla de productos - Encabezado
        int yTabla = 570;

        // Líneas horizontales del encabezado
        contenido << col1 << " " << yTabla << " m\n";
        contenido << colFin << " " << yTabla << " l\n";
        contenido << col1 << " " << (yTabla - 25) << " m\n";
        contenido << colFin << " " << (yTabla - 25) << " l\n";
        contenido << "S\n";

        // Líneas verticales del encabezado
        contenido << col1 << " " << yTabla << " m\n";
        contenido << col1 << " " << (yTabla - 25) << " l\n";
        contenido << col2 << " " << yTabla << " m\n";
        contenido << col2 << " " << (yTabla - 25) << " l\n";
        contenido << col3 << " " << yTabla << " m\n";
        contenido << col3 << " " << (yTabla - 25) << " l\n";
        contenido << col4 << " " << yTabla << " m\n";
        contenido << col4 << " " << (yTabla - 25) << " l\n";
        contenido << col5 << " " << yTabla << " m\n";
        contenido << col5 << " " << (yTabla - 25) << " l\n";
        contenido << colFin << " " << yTabla << " m\n";
        contenido << colFin << " " << (yTabla - 25) << " l\n";
        contenido << "S\n";

        // Encabezados de la tabla
        contenido << "BT\n";
        contenido << "/F2 10 Tf\n";
        contenido << (col1 + 15) << " " << (yTabla - 15) << " Td\n";
        contenido << "(Cant.) Tj\n";
        contenido << "ET\n";

        contenido << "BT\n";
        contenido << "/F2 10 Tf\n";
        contenido << (col2 + 80) << " " << (yTabla - 15) << " Td\n";
        contenido << "(Producto) Tj\n";
        contenido << "ET\n";

        contenido << "BT\n";
        contenido << "/F2 10 Tf\n";
        contenido << (col3 + 25) << " " << (yTabla - 15) << " Td\n";
        contenido << "(Marca) Tj\n";
        contenido << "ET\n";

        contenido << "BT\n";
        contenido << "/F2 10 Tf\n";
        contenido << (col4 + 10) << " " << (yTabla - 15) << " Td\n";
        contenido << "(P. Unit.) Tj\n";
        contenido << "ET\n";

        contenido << "BT\n";
        contenido << "/F2 10 Tf\n";
        contenido << (col5 + 10) << " " << (yTabla - 15) << " Td\n";
        contenido << "(Subtotal) Tj\n";
        contenido << "ET\n";

        double total = 0.0;
        int yPos = yTabla - 25;

        if (resultadoDetalles) {
            MYSQL_ROW filaDetalle;
            while ((filaDetalle = mysql_fetch_row(resultadoDetalles))) {
                double precioUnitario = atof(filaDetalle[3]);
                double subtotal = atof(filaDetalle[4]);

                // Líneas horizontales de la fila
                contenido << col1 << " " << yPos << " m\n";
                contenido << colFin << " " << yPos << " l\n";
                contenido << col1 << " " << (yPos - 20) << " m\n";
                contenido << colFin << " " << (yPos - 20) << " l\n";
                contenido << "S\n";

                // Líneas verticales de la fila
                contenido << col1 << " " << yPos << " m\n";
                contenido << col1 << " " << (yPos - 20) << " l\n";
                contenido << col2 << " " << yPos << " m\n";
                contenido << col2 << " " << (yPos - 20) << " l\n";
                contenido << col3 << " " << yPos << " m\n";
                contenido << col3 << " " << (yPos - 20) << " l\n";
                contenido << col4 << " " << yPos << " m\n";
                contenido << col4 << " " << (yPos - 20) << " l\n";
                contenido << col5 << " " << yPos << " m\n";
                contenido << col5 << " " << (yPos - 20) << " l\n";
                contenido << colFin << " " << yPos << " m\n";
                contenido << colFin << " " << (yPos - 20) << " l\n";
                contenido << "S\n";

                // Contenido de la fila - cada texto en su posición absoluta
                contenido << "BT\n";
                contenido << "/F1 9 Tf\n";
                contenido << (col1 + 20) << " " << (yPos - 12) << " Td\n";
                contenido << "(" << filaDetalle[0] << ") Tj\n";
                contenido << "ET\n";

                contenido << "BT\n";
                contenido << "/F1 9 Tf\n";
                contenido << (col2 + 5) << " " << (yPos - 12) << " Td\n";
                contenido << "(" << filaDetalle[1] << ") Tj\n";
                contenido << "ET\n";

                contenido << "BT\n";
                contenido << "/F1 9 Tf\n";
                contenido << (col3 + 5) << " " << (yPos - 12) << " Td\n";
                contenido << "(" << filaDetalle[2] << ") Tj\n";
                contenido << "ET\n";

                contenido << "BT\n";
                contenido << "/F1 8 Tf\n";
                contenido << (col4 + 5) << " " << (yPos - 12) << " Td\n";
                contenido << "(" << formatearMoneda(precioUnitario) << ") Tj\n";
                contenido << "ET\n";

                contenido << "BT\n";
                contenido << "/F1 8 Tf\n";
                contenido << (col5 + 5) << " " << (yPos - 12) << " Td\n";
                contenido << "(" << formatearMoneda(subtotal) << ") Tj\n";
                contenido << "ET\n";

                total += subtotal;
                yPos -= 20;
            }
            mysql_free_result(resultadoDetalles);
        }

        // Fila del total
        contenido << col1 << " " << yPos << " m\n";
        contenido << colFin << " " << yPos << " l\n";
        contenido << col1 << " " << (yPos - 25) << " m\n";
        contenido << colFin << " " << (yPos - 25) << " l\n";
        contenido << col4 << " " << yPos << " m\n";
        contenido << col4 << " " << (yPos - 25) << " l\n";
        contenido << col5 << " " << yPos << " m\n";
        contenido << col5 << " " << (yPos - 25) << " l\n";
        contenido << colFin << " " << yPos << " m\n";
        contenido << colFin << " " << (yPos - 25) << " l\n";
        contenido << "S\n";

        // Texto del total
        contenido << "BT\n";
        contenido << "/F2 12 Tf\n";
        contenido << (col4 + 15) << " " << (yPos - 15) << " Td\n";
        contenido << "(TOTAL:) Tj\n";
        contenido << "ET\n";

        contenido << "BT\n";
        contenido << "/F2 12 Tf\n";
        contenido << (col5 + 5) << " " << (yPos - 15) << " Td\n";
        contenido << "(" << formatearMoneda(total) << ") Tj\n";
        contenido << "ET\n";

        // Mensaje final
        contenido << "BT\n";
        contenido << "/F1 10 Tf\n";
        contenido << "50 " << (yPos - 60) << " Td\n";
        contenido << "(Gracias por su compra.) Tj\n";
        contenido << "ET\n";
    }

    string contenidoStr = contenido.str();

    // Objeto de contenido
    pdf << "4 0 obj\n";
    pdf << "<<\n";
    pdf << "/Length " << contenidoStr.length() << "\n";
    pdf << ">>\n";
    pdf << "stream\n";
    pdf << contenidoStr;
    pdf << "endstream\n";
    pdf << "endobj\n\n";

    // Fuente normal
    pdf << "5 0 obj\n";
    pdf << "<<\n";
    pdf << "/Type /Font\n";
    pdf << "/Subtype /Type1\n";
    pdf << "/BaseFont /Helvetica\n";
    pdf << ">>\n";
    pdf << "endobj\n\n";

    // Fuente negrita
    pdf << "6 0 obj\n";
    pdf << "<<\n";
    pdf << "/Type /Font\n";
    pdf << "/Subtype /Type1\n";
    pdf << "/BaseFont /Helvetica-Bold\n";
    pdf << ">>\n";
    pdf << "endobj\n\n";

    // Tabla xref
    long xrefPos = pdf.tellp();
    pdf << "xref\n";
    pdf << "0 7\n";
    pdf << "0000000000 65535 f \n";
    pdf << "0000000009 65535 n \n";
    pdf << "0000000074 65535 n \n";
    pdf << "0000000120 65535 n \n";
    pdf << "0000000274 65535 n \n";
    pdf << "0000000000 65535 n \n";  // Se calculará después
    pdf << "0000000000 65535 n \n";  // Se calculará después

    // Trailer
    pdf << "trailer\n";
    pdf << "<<\n";
    pdf << "/Size 7\n";
    pdf << "/Root 1 0 R\n";
    pdf << ">>\n";
    pdf << "startxref\n";
    pdf << xrefPos << "\n";
    pdf << "%%EOF\n";

    pdf.close();
    cn.cerrar_conexion();

    cout << "\n=== FACTURA PDF GENERADA ===\n";
    cout << "✓ PDF generado exitosamente: " << nombreArchivoPDF << endl;

    // Intentar abrir el PDF
    string comandoAbrir = "start \"\" \"" + nombreArchivoPDF + "\"";
    if (system(comandoAbrir.c_str()) == 0) {
        cout << "✓ El PDF se ha abierto exitosamente.\n";
    } else {
        cout << "PDF guardado en: " << nombreArchivoPDF << endl;
        cout << "Puede encontrarlo en la carpeta 'facturas'.\n";
    }
}

// Función para generar factura de compra en HTML
void generarFacturaCompra(int idcompra) {
    ConexionBD cn = ConexionBD();
    cn.abrir_conexion();

    if (!cn.getConector()) {
        cout << "Error en la conexión a la base de datos para generar factura.\n";
        return;
    }

    // Crear carpeta facturas si no existe
    system("if not exist \"facturas\" mkdir facturas");

    // Obtener información de la compra
    string consulta = "SELECT c.idcompra, c.no_order_compra, c.fecha_order, c.fecha_ingreso, "
                     "p.proveedor, p.nit as proveedor_nit, p.direccion, p.telefono "
                     "FROM Compras c "
                     "INNER JOIN Proveedores p ON c.idproveedor = p.idProveedor "
                     "WHERE c.idcompra = " + to_string(idcompra) + ";";

    const char* sql = consulta.c_str();
    int q_estado = mysql_query(cn.getConector(), sql);

    if (q_estado) {
        cout << "Error al consultar información de la compra.\n";
        cn.cerrar_conexion();
        return;
    }

    MYSQL_RES* resultado = mysql_store_result(cn.getConector());
    if (!resultado) {
        cout << "Error al obtener resultados de la compra.\n";
        cn.cerrar_conexion();
        return;
    }

    MYSQL_ROW fila = mysql_fetch_row(resultado);
    if (!fila) {
        cout << "No se encontró la compra especificada.\n";
        mysql_free_result(resultado);
        cn.cerrar_conexion();
        return;
    }

    // Generar nombres de archivos
    string nombreArchivoHTML = "facturas/factura_compra_" + to_string(idcompra) + ".html";
    string nombreArchivoPDF = "facturas/factura_compra_" + to_string(idcompra) + ".pdf";

    // Crear archivo HTML
    ofstream archivo(nombreArchivoHTML);
    if (!archivo.is_open()) {
        cout << "Error al crear el archivo de factura.\n";
        mysql_free_result(resultado);
        cn.cerrar_conexion();
        return;
    }

    // Escribir HTML de la factura
    archivo << "<!DOCTYPE html>\n";
    archivo << "<html>\n<head>\n";
    archivo << "<meta charset='UTF-8'>\n";
    archivo << "<title>Factura de Compra #" << fila[1] << "</title>\n";
    archivo << "<style>\n";
    archivo << "@media print { body { margin: 0; } }\n";
    archivo << "body { font-family: Arial, sans-serif; margin: 20px; font-size: 12px; }\n";
    archivo << "table { border-collapse: collapse; width: 100%; margin-top: 10px; }\n";
    archivo << "th, td { border: 1px solid #000; padding: 8px; text-align: left; }\n";
    archivo << "th { background-color: #f0f0f0; font-weight: bold; }\n";
    archivo << ".header { text-align: center; margin-bottom: 20px; }\n";
    archivo << ".header h1 { margin: 10px 0; font-size: 24px; }\n";
    archivo << ".info-table { width: 100%; margin-bottom: 20px; }\n";
    archivo << ".total { font-weight: bold; background-color: #f0f0f0; }\n";
    archivo << ".money { text-align: right; }\n";
    archivo << ".center { text-align: center; }\n";
    archivo << "</style>\n</head>\n<body>\n";

    archivo << "<div class='header'>\n";
    archivo << "<h1>FACTURA DE COMPRA</h1>\n";
    archivo << "</div>\n";

    archivo << "<table class='info-table'>\n";
    archivo << "<tr><td><strong>No Factura</strong></td><td>" << fila[1] << "</td><td><strong>Fecha:</strong></td><td>" << fila[2] << "</td></tr>\n";
    archivo << "<tr><td><strong>NIT Proveedor</strong></td><td>" << fila[5] << "</td><td><strong>Fecha Ingreso:</strong></td><td>" << fila[3] << "</td></tr>\n";
    archivo << "<tr><td><strong>Proveedor</strong></td><td colspan='3'>" << fila[4] << "</td></tr>\n";
    archivo << "<tr><td><strong>Dirección</strong></td><td colspan='3'>" << fila[6] << "</td></tr>\n";
    archivo << "</table>\n";

    mysql_free_result(resultado);

    // Obtener detalles de la compra
    string consultaDetalles = "SELECT cd.cantidad, p.producto, m.marca, cd.precio_unitario, "
                             "(cd.cantidad * cd.precio_unitario) as subtotal "
                             "FROM Compras_detalle cd "
                             "INNER JOIN Productos p ON cd.idproducto = p.idProducto "
                             "INNER JOIN Marcas m ON p.idMarca = m.idmarca "
                             "WHERE cd.idcompra = " + to_string(idcompra) + ";";

    const char* sqlDetalles = consultaDetalles.c_str();
    q_estado = mysql_query(cn.getConector(), sqlDetalles);

    if (!q_estado) {
        MYSQL_RES* resultadoDetalles = mysql_store_result(cn.getConector());

        archivo << "<h3>PRODUCTOS</h3>\n";
        archivo << "<table>\n";
        archivo << "<tr><th class='center'>Cantidad</th><th>Producto</th><th>Marca</th><th class='money'>Precio Unitario</th><th class='money'>Subtotal</th></tr>\n";

        double total = 0.0;
        if (resultadoDetalles) {
            MYSQL_ROW filaDetalle;
            while ((filaDetalle = mysql_fetch_row(resultadoDetalles))) {
                double precioUnitario = atof(filaDetalle[3]);
                double subtotal = atof(filaDetalle[4]);

                archivo << "<tr>";
                archivo << "<td class='center'>" << filaDetalle[0] << "</td>";
                archivo << "<td>" << filaDetalle[1] << "</td>";
                archivo << "<td>" << filaDetalle[2] << "</td>";
                archivo << "<td class='money'>" << formatearMoneda(precioUnitario) << "</td>";
                archivo << "<td class='money'>" << formatearMoneda(subtotal) << "</td>";
                archivo << "</tr>\n";
                total += subtotal;
            }
            mysql_free_result(resultadoDetalles);
        }

        archivo << "<tr class='total'><td colspan='4'><strong>Total:</strong></td><td class='money'><strong>" << formatearMoneda(total) << "</strong></td></tr>\n";
        archivo << "</table>\n";

        archivo << "<p style='margin-top: 20px;'><strong>Gracias por su compra.</strong></p>\n";
    }

    archivo << "</body>\n</html>";
    archivo.close();
    cn.cerrar_conexion();

    cout << "\n=== FACTURA GENERADA ===\n";
    cout << "Archivo HTML: " << nombreArchivoHTML << endl;

    // Función para verificar si un archivo existe
    auto archivoExiste = [](const string& ruta) -> bool {
        ifstream archivo(ruta);
        return archivo.good();
    };

    // Intentar convertir a PDF usando diferentes métodos
    bool pdfGenerado = false;

    // Método 1: Intentar usar wkhtmltopdf si está disponible
    cout << "Intentando generar PDF...\n";
    string comandoPDF1 = "wkhtmltopdf --page-size A4 --margin-top 0.75in --margin-right 0.75in --margin-bottom 0.75in --margin-left 0.75in \"" + nombreArchivoHTML + "\" \"" + nombreArchivoPDF + "\" >nul 2>&1";
    system(comandoPDF1.c_str());

    // Verificar si realmente se creó el PDF
    if (archivoExiste(nombreArchivoPDF)) {
        pdfGenerado = true;
        cout << "✓ PDF generado exitosamente: " << nombreArchivoPDF << endl;
    }

    // Si se generó PDF, intentar abrirlo
    if (pdfGenerado) {
        string comandoAbrir = "start \"\" \"" + nombreArchivoPDF + "\"";
        if (system(comandoAbrir.c_str()) == 0) {
            cout << "✓ El PDF se ha abierto exitosamente.\n";
        } else {
            cout << "PDF guardado en: " << nombreArchivoPDF << endl;
            cout << "No se pudo abrir automáticamente. Puede encontrarlo en la carpeta 'facturas'.\n";
        }
    } else {
        // Si no se pudo generar PDF, abrir HTML con instrucciones claras
        cout << "⚠ No se pudo generar PDF automáticamente.\n";
        cout << "Esto puede deberse a que wkhtmltopdf no está instalado.\n\n";
        cout << "OPCIONES PARA GENERAR PDF:\n";
        cout << "1. Abriendo archivo HTML para convertir manualmente...\n";

        string comandoHTML = "start \"\" \"" + nombreArchivoHTML + "\"";
        system(comandoHTML.c_str());

        cout << "\n📋 INSTRUCCIONES PARA GENERAR PDF:\n";
        cout << "   • En el navegador que se abrió, presione Ctrl+P\n";
        cout << "   • En 'Destino', seleccione 'Guardar como PDF' o 'Microsoft Print to PDF'\n";
        cout << "   • Ajuste los márgenes si es necesario\n";
        cout << "   • Haga clic en 'Guardar'\n";
        cout << "   • Guarde el archivo como: factura_compra_" << idcompra << ".pdf\n";
        cout << "   • Guárdelo en la carpeta 'facturas'\n\n";
        cout << "2. Para conversión automática futura:\n";
        cout << "   • Instale wkhtmltopdf desde: https://wkhtmltopdf.org/downloads.html\n";
        cout << "   • Agregue wkhtmltopdf al PATH del sistema\n";
        cout << "   • Reinicie el programa\n\n";
        cout << "3. El archivo HTML siempre estará disponible en: " << nombreArchivoHTML << "\n";
    }
}

// Función para buscar cliente por NIT
void buscarClientePorNIT() {
    limpiarPantalla();
    cout << "===== BUSCAR CLIENTE POR NIT =====\n\n";

    string nitBuscar;
    cout << "Ingrese el NIT del cliente a buscar\n";
    cout << "(Si no tiene NIT, ingrese: C/F para consumidor final)\n";
    cout << "NIT del cliente: ";
    getline(cin, nitBuscar);

    // Validar formato del NIT (ahora permite C/F)
    string nitValidado = validarFormatoNIT(nitBuscar);
    if (nitValidado.empty()) {
        cout << "\nFormato de NIT invalido. Intente nuevamente.\n";
        cout << "Formatos validos:\n";
        cout << "- NIT: 8 a 13 digitos (ej: 12345678, 1234567890123)\n";
        cout << "- Consumidor final: C/F\n\n";
        pausar();
        return;
    }

    cout << "Buscando cliente con NIT: " << nitValidado << endl;

    // Buscar en la base de datos
    ConexionBD cn = ConexionBD();
    cn.abrir_conexion();
    if (cn.getConector()) {
        string consulta;

        // Si es C/F, buscar por nombre y apellido
        if (nitValidado == "C/F") {
            cout << "\n=== BUSQUEDA DE CONSUMIDOR FINAL ===\n";
            cout << "Como hay varios clientes con NIT C/F, buscaremos por nombre:\n";

            string nombreBuscar = validarTextoNoVacio("Ingrese el nombre del cliente: ");
            string apellidoBuscar = validarTextoNoVacio("Ingrese el apellido del cliente: ");

            consulta = "SELECT idCliente, nombres, apellidos, NIT, telefono, correo_electronico FROM Clientes WHERE NIT = 'C/F' AND nombres LIKE '%" + nombreBuscar + "%' AND apellidos LIKE '%" + apellidoBuscar + "%';";
            cout << "\nBuscando cliente consumidor final: " << nombreBuscar << " " << apellidoBuscar << endl;
        } else {
            // Para NITs normales
            consulta = "SELECT idCliente, nombres, apellidos, NIT, telefono, correo_electronico FROM Clientes WHERE NIT = '" + nitValidado + "';";
        }

        const char* sql = consulta.c_str();
        int q_estado = mysql_query(cn.getConector(), sql);
        if (!q_estado) {
            MYSQL_RES* resultado = mysql_store_result(cn.getConector());

            if (resultado) {
                MYSQL_ROW fila = mysql_fetch_row(resultado);

                if (fila) {
                    cout << "\n=== CLIENTE ENCONTRADO ===\n";
                    cout << "ID: " << fila[0] << endl;
                    cout << "Nombre completo: " << fila[1] << " " << fila[2] << endl;
                    cout << "NIT: " << fila[3] << endl;
                    cout << "Teléfono: " << (fila[4] ? fila[4] : "No especificado") << endl;
                    cout << "Correo: " << (fila[5] ? fila[5] : "No especificado") << endl;
                } else {
                    cout << "\nNo se encontró ningún cliente con el NIT: " << nitValidado << endl;
                    cout << "¿Desea crear un nuevo cliente? (S/N): ";
                    char respuesta;
                    cin >> respuesta;
                    cin.ignore();

                    if (respuesta == 'S' || respuesta == 's') {
                        ingresarCliente();
                    }
                }

                // No liberar resultado manualmente, dejar que MySQL lo maneje automáticamente
            } else {
                cout << "Error al obtener resultados de la consulta.\n";
            }
        } else {
            cout << "Error al ejecutar la consulta: " << mysql_error(cn.getConector()) << endl;
        }
    } else {
        cout << "Error en la conexión a la base de datos.\n";
    }
    // No cerrar conexión manualmente, el destructor lo hará automáticamente

    pausar();
}

// Función para imprimir factura
void imprimirFactura() {
    limpiarPantalla();
    cout << "===== IMPRIMIR FACTURA =====\n\n";

    // Mostrar ventas disponibles
    cout << "Ventas disponibles:\n";
    Venta v = Venta();
    v.leer();

    int idVenta = 0;
    cout << "\nIngrese el ID de la venta para imprimir factura: ";
    cin >> idVenta;

    // Obtener información de la venta y cliente
    ConexionBD cn = ConexionBD();
    cn.abrir_conexion();
    if (cn.getConector()) {
        // Consulta para obtener información completa de la venta
        string consulta = "SELECT v.nofactura, v.serie, v.fechafactura, "
                         "c.nombres, c.apellidos, c.NIT, "
                         "e.nombres as emp_nombres, e.apellidos as emp_apellidos "
                         "FROM Ventas v "
                         "INNER JOIN Clientes c ON v.idcliente = c.idCliente "
                         "INNER JOIN Empleados e ON v.idempleado = e.idEmpleado "
                         "WHERE v.idVenta = " + to_string(idVenta) + ";";

        const char* sql = consulta.c_str();
        mysql_query(cn.getConector(), sql);
        MYSQL_RES* resultado = mysql_store_result(cn.getConector());

        if (resultado) {
            MYSQL_ROW fila = mysql_fetch_row(resultado);
            if (fila) {
                // Imprimir encabezado de factura
                cout << "\n======================================\n";
                cout << "              FACTURA                 \n";
                cout << "======================================\n";
                cout << "No Factura: " << fila[0] << "        Fecha: " << fila[2] << "\n";
                cout << "Serie: " << fila[1] << "\n";
                cout << "NIT: " << fila[5] << "\n";
                cout << "Cliente: " << fila[3] << " " << fila[4] << "\n";
                cout << "Atendido por: " << fila[6] << " " << fila[7] << "\n";
                cout << "======================================\n";
                cout << "                PRODUCTOS             \n";
                cout << "======================================\n";

                // Obtener detalles de la venta con información de productos
                string consultaDetalles = "SELECT vd.cantidad, p.producto, m.marca, p.precio_venta, "
                                         "(CAST(vd.cantidad AS DECIMAL(10,2)) * vd.precio_unitario) as subtotal "
                                         "FROM Ventas_detalle vd "
                                         "INNER JOIN Productos p ON vd.idProducto = p.idProducto "
                                         "INNER JOIN Marcas m ON p.idMarca = m.idmarca "
                                         "WHERE vd.idventa = " + to_string(idVenta) + ";";

                const char* sqlDetalles = consultaDetalles.c_str();
                mysql_query(cn.getConector(), sqlDetalles);
                MYSQL_RES* resultadoDetalles = mysql_store_result(cn.getConector());

                double total = 0.0;
                if (resultadoDetalles) {
                    MYSQL_ROW filaDetalle;
                    while ((filaDetalle = mysql_fetch_row(resultadoDetalles))) {
                        cout << filaDetalle[0] << " - " << filaDetalle[1]
                             << " marca " << filaDetalle[2]
                             << "    Q " << filaDetalle[3] << "\n";
                        total += atof(filaDetalle[4]);
                    }
                    // No liberar resultado manualmente, dejar que MySQL lo maneje automáticamente
                }

                cout << "======================================\n";
                cout << "Total:                    Q " << fixed << setprecision(2) << total << "\n";
                cout << "======================================\n";
                cout << "Gracias por su compra.\n";
                cout << "======================================\n";
            } else {
                cout << "No se encontró la venta especificada.\n";
            }
            // No liberar resultado manualmente, dejar que MySQL lo maneje automáticamente
        }
    } else {
        cout << "Error en la conexión a la base de datos.\n";
    }
    // No cerrar conexión manualmente, el destructor lo hará automáticamente

    pausar();
}

// Implementaciones de funciones para detalles de compra
void actualizarDetalleCompra() {
    limpiarPantalla();
    cout << "===== ACTUALIZAR DETALLE DE COMPRA =====\n\n";

    // Mostrar todas las compras primero
    Compra c = Compra();
    c.leer();

    int idcompra = 0;
    cout << "\nIngrese el ID de la compra para ver sus detalles: ";
    cin >> idcompra;

    // Mostrar detalles de la compra seleccionada
    c.leerDetalles(idcompra);

    int idcompra_detalle = 0;
    cout << "\nIngrese el ID del detalle de compra a modificar: ";
    cin >> idcompra_detalle;

    // Mostrar productos disponibles
    cout << "\nProductos disponibles:\n";
    Producto prod = Producto();
    prod.leer();

    int idproducto = 0, cantidad = 0;
    double precio_unitario = 0.0;

    cout << "\nIngrese Nuevo ID de Producto: ";
    cin >> idproducto;
    cout << "Ingrese Nueva Cantidad: ";
    cin >> cantidad;

    // Obtener precio de costo automáticamente de la base de datos
    ConexionBD cn = ConexionBD();
    cn.abrir_conexion();
    if (cn.getConector()) {
        string consulta = "SELECT precio_costo, producto FROM Productos WHERE idProducto = " + to_string(idproducto) + ";";
        const char* sql = consulta.c_str();
        mysql_query(cn.getConector(), sql);
        MYSQL_RES* resultado = mysql_store_result(cn.getConector());

        if (resultado) {
            MYSQL_ROW fila = mysql_fetch_row(resultado);
            if (fila) {
                precio_unitario = atof(fila[0]);
                cout << "Producto: " << fila[1] << " - Precio de costo: Q" << precio_unitario << endl;
            } else {
                cout << "Producto no encontrado. Usando precio 0.00" << endl;
                precio_unitario = 0.0;
            }
            // No liberar resultado manualmente, dejar que MySQL lo maneje automáticamente
        }

        // Actualizar en la base de datos
        string id = to_string(idcompra_detalle);
        string idprod = to_string(idproducto);
        string cant = to_string(cantidad);
        string pu = to_string(precio_unitario);

        string consultaUpdate = "UPDATE Compras_detalle SET idproducto = " + idprod + ", cantidad = " + cant + ", precio_unitario = " + pu + " WHERE idcompra_detalle = " + id + ";";
        const char* c = consultaUpdate.c_str();
        int q_estado = mysql_query(cn.getConector(), c);
        if (!q_estado) {
            cout << "\nActualización de Detalle de Compra Exitosa..." << endl;
            cout << "\nDetalles actualizados:\n";
            Compra comp = Compra();
            comp.leerDetalles(idcompra);
        }
        else {
            cout << "xxx Error al actualizar detalle de compra xxx" << endl;
            cout << consultaUpdate << endl;
        }
    }
    else {
        cout << "xxx Error en la conexión xxx" << endl;
    }
    // No cerrar conexión manualmente, el destructor lo hará automáticamente

    pausar();
}

void eliminarDetalleCompra() {
    limpiarPantalla();
    cout << "===== ELIMINAR DETALLE DE COMPRA =====\n\n";

    // Mostrar todas las compras primero
    Compra c = Compra();
    c.leer();

    int idcompra = 0;
    cout << "\nIngrese el ID de la compra para ver sus detalles: ";
    cin >> idcompra;

    // Mostrar detalles de la compra seleccionada
    c.leerDetalles(idcompra);

    int idcompra_detalle = 0;
    cout << "\nIngrese el ID del detalle de compra a eliminar: ";
    cin >> idcompra_detalle;

    char confirmar;
    cout << "¿Está seguro de eliminar este detalle de compra? (S/N): ";
    cin >> confirmar;

    if (confirmar == 'S' || confirmar == 's') {
        // Eliminar de la base de datos
        int q_estado = 0;
        ConexionBD cn = ConexionBD();
        cn.abrir_conexion();
        if (cn.getConector()) {
            string id = to_string(idcompra_detalle);
            string consulta = "DELETE FROM Compras_detalle WHERE idcompra_detalle = " + id + ";";
            const char* c = consulta.c_str();
            q_estado = mysql_query(cn.getConector(), c);
            if (!q_estado) {
                cout << "\nEliminación de Detalle de Compra Exitosa..." << endl;
                cout << "\nDetalles actualizados:\n";
                Compra comp = Compra();
                comp.leerDetalles(idcompra);
            }
            else {
                cout << "xxx Error al eliminar detalle de compra xxx" << endl;
                cout << consulta << endl;
            }
        }
        else {
            cout << "xxx Error en la conexión xxx" << endl;
        }
        // No cerrar conexión manualmente, el destructor lo hará automáticamente
    } else {
        cout << "\nOperación cancelada.\n";
    }

    pausar();
}

// Implementaciones de funciones para detalles de venta
void actualizarDetalleVenta() {
    limpiarPantalla();
    cout << "===== ACTUALIZAR DETALLE DE VENTA =====\n\n";

    // Mostrar todas las ventas primero
    Venta v = Venta();
    v.leer();

    int idventa = 0;
    cout << "\nIngrese el ID de la venta para ver sus detalles: ";
    cin >> idventa;

    // Mostrar detalles de la venta seleccionada
    v.leerDetalles(idventa);

    int idventa_detalle = 0;
    cout << "\nIngrese el ID del detalle de venta a modificar: ";
    cin >> idventa_detalle;

    // Mostrar productos disponibles
    cout << "\nProductos disponibles:\n";
    Producto prod = Producto();
    prod.leer();

    int idProducto = 0;
    string cantidad;
    double precio_unitario = 0.0;

    cout << "\nIngrese Nuevo ID de Producto: ";
    cin >> idProducto;
    cin.ignore();
    cout << "Ingrese Nueva Cantidad: ";
    getline(cin, cantidad);

    // Obtener precio de venta automáticamente de la base de datos
    ConexionBD cn = ConexionBD();
    cn.abrir_conexion();
    if (cn.getConector()) {
        string consulta = "SELECT precio_venta, producto FROM Productos WHERE idProducto = " + to_string(idProducto) + ";";
        const char* sql = consulta.c_str();
        mysql_query(cn.getConector(), sql);
        MYSQL_RES* resultado = mysql_store_result(cn.getConector());

        if (resultado) {
            MYSQL_ROW fila = mysql_fetch_row(resultado);
            if (fila) {
                precio_unitario = atof(fila[0]);
                cout << "Producto: " << fila[1] << " - Precio de venta: Q" << precio_unitario << endl;
            } else {
                cout << "Producto no encontrado. Usando precio 0.00" << endl;
                precio_unitario = 0.0;
            }
            // No liberar resultado manualmente, dejar que MySQL lo maneje automáticamente
        }

        // Actualizar en la base de datos
        string id = to_string(idventa_detalle);
        string idprod = to_string(idProducto);
        string pu = to_string(precio_unitario);

        string consultaUpdate = "UPDATE Ventas_detalle SET idProducto = " + idprod + ", cantidad = '" + cantidad + "', precio_unitario = " + pu + " WHERE idventa_detalle = " + id + ";";
        const char* c = consultaUpdate.c_str();
        int q_estado = mysql_query(cn.getConector(), c);
        if (!q_estado) {
            cout << "\nActualización de Detalle de Venta Exitosa..." << endl;
            cout << "\nDetalles actualizados:\n";
            Venta vent = Venta();
            vent.leerDetalles(idventa);
        }
        else {
            cout << "xxx Error al actualizar detalle de venta xxx" << endl;
            cout << consultaUpdate << endl;
        }
    }
    else {
        cout << "xxx Error en la conexión xxx" << endl;
    }
    // No cerrar conexión manualmente, el destructor lo hará automáticamente

    pausar();
}

void eliminarDetalleVenta() {
    limpiarPantalla();
    cout << "===== ELIMINAR DETALLE DE VENTA =====\n\n";

    // Mostrar todas las ventas primero
    Venta v = Venta();
    v.leer();

    int idventa = 0;
    cout << "\nIngrese el ID de la venta para ver sus detalles: ";
    cin >> idventa;

    // Mostrar detalles de la venta seleccionada
    v.leerDetalles(idventa);

    int idventa_detalle = 0;
    cout << "\nIngrese el ID del detalle de venta a eliminar: ";
    cin >> idventa_detalle;

    char confirmar;
    cout << "¿Está seguro de eliminar este detalle de venta? (S/N): ";
    cin >> confirmar;

    if (confirmar == 'S' || confirmar == 's') {
        // Eliminar de la base de datos
        int q_estado = 0;
        ConexionBD cn = ConexionBD();
        cn.abrir_conexion();
        if (cn.getConector()) {
            string id = to_string(idventa_detalle);
            string consulta = "DELETE FROM Ventas_detalle WHERE idventa_detalle = " + id + ";";
            const char* c = consulta.c_str();
            q_estado = mysql_query(cn.getConector(), c);
            if (!q_estado) {
                cout << "\nEliminación de Detalle de Venta Exitosa..." << endl;
                cout << "\nDetalles actualizados:\n";
                Venta vent = Venta();
                vent.leerDetalles(idventa);
            }
            else {
                cout << "xxx Error al eliminar detalle de venta xxx" << endl;
                cout << consulta << endl;
            }
        }
        else {
            cout << "xxx Error en la conexión xxx" << endl;
        }
        // No cerrar conexión manualmente, el destructor lo hará automáticamente
    } else {
        cout << "\nOperación cancelada.\n";
    }

    pausar();
}

int main() {
    SetConsoleOutputCP(CP_UTF8);
    system("chcp 65001 > nul");

    int opcion = 0;

    do {
        limpiarPantalla();
        cout << "======================================\n";
        cout << "      SISTEMA DE GESTIÓN GENERAL     \n";
        cout << "======================================\n\n";
        cout << "1. Gestión de Marcas\n";
        cout << "2. Gestión de Puestos\n";
        cout << "3. Gestión de Proveedores\n";
        cout << "4. Gestión de Productos\n";
        cout << "5. Gestión de Empleados\n";
        cout << "6. Gestión de Clientes\n";
        cout << "7. Gestión de Compras\n";
        cout << "8. Gestión de Ventas\n";
        cout << "0. Salir\n\n";
        opcion = validarOpcionMenu("Ingrese una opcion: ", 0, 8);

        switch (opcion) {
            case 1:
                menuMarcas();
                break;
            case 2:
                menuPuestos();
                break;
            case 3:
                menuProveedores();
                break;
            case 4:
                menuProductos();
                break;
            case 5:
                menuEmpleados();
                break;
            case 6:
                menuClientes();
                break;
            case 7:
                menuCompras();
                break;
            case 8:
                menuVentas();
                break;
            case 0:
                cout << "\nGracias por usar el sistema. ¡Hasta pronto!\n";
                break;
            default:
                cout << "\nOpción no válida. Intente de nuevo.\n";
                pausar();
        }

    } while (opcion != 0);

    return 0;
}

// ===== IMPLEMENTACIÓN DE FUNCIONES DE VALIDACIÓN =====

// Función para validar entrada de números enteros positivos
int validarEnteroPositivo(const string& mensaje, int minimo) {
    int valor;
    bool entradaValida = false;

    do {
        cout << mensaje;
        if (cin >> valor) {
            if (valor >= minimo) {
                entradaValida = true;
            } else {
                cout << "Error: El valor debe ser mayor o igual a " << minimo << ". Intente nuevamente.\n";
            }
        } else {
            cout << "Error: Debe ingresar un numero entero valido. Intente nuevamente.\n";
            cin.clear(); // Limpiar el estado de error
            cin.ignore(10000, '\n'); // Descartar entrada inválida
        }
    } while (!entradaValida);

    cin.ignore(); // Limpiar buffer
    return valor;
}

// Función para validar entrada de números decimales positivos
double validarDecimalPositivo(const string& mensaje, double minimo) {
    double valor;
    bool entradaValida = false;

    do {
        cout << mensaje;
        if (cin >> valor) {
            if (valor >= minimo) {
                entradaValida = true;
            } else {
                cout << "Error: El valor debe ser mayor o igual a " << minimo << ". Intente nuevamente.\n";
            }
        } else {
            cout << "Error: Debe ingresar un numero decimal valido. Intente nuevamente.\n";
            cin.clear();
            cin.ignore(10000, '\n');
        }
    } while (!entradaValida);

    cin.ignore();
    return valor;
}

// Función para validar texto no vacío
string validarTextoNoVacio(const string& mensaje) {
    string texto;
    bool entradaValida = false;

    do {
        cout << mensaje;
        getline(cin, texto);

        // Eliminar espacios al inicio y final
        size_t inicio = texto.find_first_not_of(" \t");
        if (inicio != string::npos) {
            size_t fin = texto.find_last_not_of(" \t");
            texto = texto.substr(inicio, fin - inicio + 1);
        }

        if (!texto.empty()) {
            entradaValida = true;
        } else {
            cout << "Error: Este campo no puede estar vacio. Intente nuevamente.\n";
        }
    } while (!entradaValida);

    return texto;
}

// Función para validar formato de fecha
string validarFecha(const string& mensaje) {
    string fecha;
    bool entradaValida = false;

    do {
        cout << mensaje;
        getline(cin, fecha);

        // Validación básica del formato YYYY-MM-DD
        if (fecha.length() == 10 && fecha[4] == '-' && fecha[7] == '-') {
            bool formatoValido = true;

            // Verificar que los caracteres sean dígitos en las posiciones correctas
            for (int i = 0; i < 10; i++) {
                if (i != 4 && i != 7) { // Saltar las posiciones de los guiones
                    if (fecha[i] < '0' || fecha[i] > '9') {
                        formatoValido = false;
                        break;
                    }
                }
            }

            if (formatoValido) {
                // Validaciones adicionales basicas
                int anio = stoi(fecha.substr(0, 4));
                int mes = stoi(fecha.substr(5, 2));
                int dia = stoi(fecha.substr(8, 2));

                if (anio >= 1900 && anio <= 2100 && mes >= 1 && mes <= 12 && dia >= 1 && dia <= 31) {
                    entradaValida = true;
                } else {
                    cout << "Error: Fecha fuera de rango valido. Intente nuevamente.\n";
                }
            } else {
                cout << "Error: Formato de fecha invalido. Use YYYY-MM-DD. Intente nuevamente.\n";
            }
        } else {
            cout << "Error: Formato de fecha invalido. Use YYYY-MM-DD. Intente nuevamente.\n";
        }
    } while (!entradaValida);

    return fecha;
}

// Función para manejo centralizado de errores de base de datos
void manejarErrorBD(const string& operacion, const string& consulta, MYSQL* conector) {
    cout << "\nERROR EN BASE DE DATOS\n";
    cout << "Operacion: " << operacion << "\n";
    cout << "Error MySQL: " << mysql_error(conector) << "\n";
    cout << "Codigo de error: " << mysql_errno(conector) << "\n";

    // Mostrar errores comunes de manera amigable
    int codigoError = mysql_errno(conector);
    switch (codigoError) {
        case 1062:
            cout << "Causa probable: Registro duplicado. Verifique que no este ingresando datos que ya existen.\n";
            break;
        case 1452:
            cout << "Causa probable: Referencia invalida. Verifique que los IDs relacionados existan.\n";
            break;
        case 1451:
            cout << "Causa probable: No se puede eliminar porque hay registros relacionados.\n";
            break;
        case 1054:
            cout << "Causa probable: Campo inexistente en la tabla.\n";
            break;
        case 1146:
            cout << "Causa probable: Tabla inexistente.\n";
            break;
        default:
            cout << "Consulte con el administrador del sistema.\n";
            break;
    }

    cout << "\nConsulta ejecutada: " << consulta << "\n";
}

// Función para confirmar operaciones críticas
bool confirmarOperacion(const string& mensaje) {
    char respuesta;
    cout << mensaje << " (S/N): ";
    cin >> respuesta;
    cin.ignore();
    return (respuesta == 'S' || respuesta == 's');
}

// Función para validar opciones de menú y evitar bucles infinitos
int validarOpcionMenu(const string& mensaje, int minimo, int maximo) {
    int opcion;
    bool entradaValida = false;

    do {
        cout << mensaje;

        // Verificar si la entrada es un número válido
        if (cin >> opcion) {
            // Verificar si está en el rango válido
            if (opcion >= minimo && opcion <= maximo) {
                entradaValida = true;
            } else {
                cout << "Error: Opcion invalida. Debe estar entre " << minimo << " y " << maximo << ". Intente nuevamente.\n\n";
            }
        } else {
            // Si no es un número, limpiar el buffer y mostrar error
            cout << "Error: Debe ingresar un numero valido. Intente nuevamente.\n\n";
            cin.clear(); // Limpiar el estado de error
            cin.ignore(10000, '\n'); // Descartar toda la línea inválida
        }
    } while (!entradaValida);

    cin.ignore(); // Limpiar buffer para próximas entradas
    return opcion;
}

// ===== FUNCIÓN PARA GENERAR PDF DE VENTAS =====

// Función para generar PDF nativo de ventas usando solo C++
void generarFacturaVentaPDF(int idVenta) {
    ConexionBD cn = ConexionBD();
    cn.abrir_conexion();

    if (!cn.getConector()) {
        cout << "Error en la conexion a la base de datos para generar factura.\n";
        return;
    }

    try {
        // Crear carpeta facturas si no existe
        system("if not exist \"facturas\" mkdir facturas");

        // Obtener información de la venta
        string consulta = "SELECT v.idVenta, v.nofactura, v.serie, v.fechafactura, "
                         "c.nombres, c.apellidos, c.NIT, c.telefono, c.correo_electronico, "
                         "CONCAT(e.nombres, ' ', e.apellidos) as empleado "
                         "FROM Ventas v "
                         "INNER JOIN Clientes c ON v.idcliente = c.idCliente "
                         "INNER JOIN Empleados e ON v.idempleado = e.idEmpleado "
                         "WHERE v.idVenta = " + to_string(idVenta) + ";";

        const char* sql = consulta.c_str();
        int q_estado = mysql_query(cn.getConector(), sql);

        if (q_estado) {
            manejarErrorBD("Consultar informacion de venta", consulta, cn.getConector());
            cn.cerrar_conexion();
            return;
        }

        MYSQL_RES* resultado = mysql_store_result(cn.getConector());
        if (!resultado) {
            cout << "Error al obtener resultados de la venta.\n";
            cn.cerrar_conexion();
            return;
        }

        MYSQL_ROW fila = mysql_fetch_row(resultado);
        if (!fila) {
            cout << "No se encontro la venta especificada (ID: " << idVenta << ").\n";
            mysql_free_result(resultado);
            cn.cerrar_conexion();
            return;
        }

        // Extraer datos de la venta
        string numeroFactura = fila[1];
        string serie = fila[2];
        string fechaFactura = fila[3];
        string clienteNombre = string(fila[4]) + " " + string(fila[5]);
        string clienteNIT = fila[6];
        string clienteTelefono = fila[7] ? fila[7] : "No especificado";
        string clienteCorreo = fila[8] ? fila[8] : "No especificado";
        string empleado = fila[9];

        mysql_free_result(resultado);

        // Generar nombre del archivo PDF
        string nombreArchivoPDF = "facturas/factura_venta_" + to_string(idVenta) + ".pdf";

        // Crear archivo PDF
        ofstream pdf(nombreArchivoPDF, ios::binary);
        if (!pdf.is_open()) {
            cout << "Error al crear el archivo PDF.\n";
            cn.cerrar_conexion();
            return;
        }

        // Obtener fecha y hora actual
        time_t tiempoActual = time(0);
        struct tm* tiempoLocal;
        char buffer[80];

        #ifdef _WIN32
            struct tm tiempoLocalStruct;
            localtime_s(&tiempoLocalStruct, &tiempoActual);
            tiempoLocal = &tiempoLocalStruct;
        #else
            tiempoLocal = localtime(&tiempoActual);
        #endif

        strftime(buffer, sizeof(buffer), "%d/%m/%Y %H:%M:%S", tiempoLocal);
        string fechaGeneracion = buffer;

        // Escribir encabezado PDF
        pdf << "%PDF-1.4\n";
        pdf << "1 0 obj\n";
        pdf << "<<\n";
        pdf << "/Type /Catalog\n";
        pdf << "/Pages 2 0 R\n";
        pdf << ">>\n";
        pdf << "endobj\n\n";

        pdf << "2 0 obj\n";
        pdf << "<<\n";
        pdf << "/Type /Pages\n";
        pdf << "/Kids [3 0 R]\n";
        pdf << "/Count 1\n";
        pdf << ">>\n";
        pdf << "endobj\n\n";

        pdf << "3 0 obj\n";
        pdf << "<<\n";
        pdf << "/Type /Page\n";
        pdf << "/Parent 2 0 R\n";
        pdf << "/MediaBox [0 0 612 792]\n";
        pdf << "/Contents 4 0 R\n";
        pdf << "/Resources << /Font << /F1 5 0 R /F2 6 0 R >> >>\n";
        pdf << ">>\n";
        pdf << "endobj\n\n";

        // Contenido de la página
        stringstream contenido;
        contenido << "BT\n";
        contenido << "/F2 16 Tf\n";
        contenido << "50 750 Td\n";
        contenido << "(FACTURA DE VENTA) Tj\n";
        contenido << "0 -30 Td\n";
        contenido << "/F1 12 Tf\n";
        contenido << "(Factura No: " << serie << "-" << numeroFactura << ") Tj\n";
        contenido << "0 -20 Td\n";
        contenido << "(Fecha: " << fechaFactura << ") Tj\n";
        contenido << "0 -20 Td\n";
        contenido << "(Generado: " << fechaGeneracion << ") Tj\n";

        contenido << "0 -40 Td\n";
        contenido << "/F2 14 Tf\n";
        contenido << "(DATOS DEL CLIENTE) Tj\n";
        contenido << "0 -25 Td\n";
        contenido << "/F1 11 Tf\n";
        contenido << "(Cliente: " << clienteNombre << ") Tj\n";
        contenido << "0 -18 Td\n";
        contenido << "(NIT: " << clienteNIT << ") Tj\n";
        contenido << "0 -18 Td\n";
        contenido << "(Telefono: " << clienteTelefono << ") Tj\n";
        contenido << "0 -18 Td\n";
        contenido << "(Correo: " << clienteCorreo << ") Tj\n";
        contenido << "0 -18 Td\n";
        contenido << "(Atendido por: " << empleado << ") Tj\n";

        // Obtener detalles de la venta
        string consultaDetalles = "SELECT p.producto, vd.cantidad, vd.precio_unitario, "
                                 "(vd.cantidad * vd.precio_unitario) as subtotal "
                                 "FROM Ventas_detalle vd "
                                 "INNER JOIN Productos p ON vd.idProducto = p.idProducto "
                                 "WHERE vd.idventa = " + to_string(idVenta) + ";";

        q_estado = mysql_query(cn.getConector(), consultaDetalles.c_str());
        if (!q_estado) {
            MYSQL_RES* resultadoDetalles = mysql_store_result(cn.getConector());
            if (resultadoDetalles) {
                contenido << "0 -40 Td\n";
                contenido << "/F2 14 Tf\n";
                contenido << "(DETALLE DE PRODUCTOS) Tj\n";
                contenido << "0 -25 Td\n";
                contenido << "/F1 10 Tf\n";
                contenido << "(Producto                    Cant.    Precio Unit.    Subtotal) Tj\n";
                contenido << "0 -15 Td\n";
                contenido << "(================================================================) Tj\n";

                double total = 0.0;
                MYSQL_ROW filaDetalle;
                while ((filaDetalle = mysql_fetch_row(resultadoDetalles))) {
                    string producto = filaDetalle[0];
                    string cantidad = filaDetalle[1];
                    double precioUnitario = atof(filaDetalle[2]);
                    double subtotal = atof(filaDetalle[3]);
                    total += subtotal;

                    // Truncar nombre del producto si es muy largo
                    if (producto.length() > 25) {
                        producto = producto.substr(0, 22) + "...";
                    }

                    contenido << "0 -15 Td\n";
                    contenido << "(" << producto;
                    // Agregar espacios para alineación
                    for (int i = producto.length(); i < 25; i++) contenido << " ";
                    contenido << cantidad;
                    for (int i = cantidad.length(); i < 9; i++) contenido << " ";
                    contenido << formatearMoneda(precioUnitario);
                    contenido << formatearMoneda(subtotal) << ") Tj\n";
                }

                contenido << "0 -20 Td\n";
                contenido << "(================================================================) Tj\n";
                contenido << "0 -20 Td\n";
                contenido << "/F2 12 Tf\n";
                contenido << "(TOTAL:  " << formatearMoneda(total) << ") Tj\n";

                mysql_free_result(resultadoDetalles);
            }
        }

        contenido << "ET\n";

        string contenidoStr = contenido.str();
        pdf << "4 0 obj\n";
        pdf << "<<\n";
        pdf << "/Length " << contenidoStr.length() << "\n";
        pdf << ">>\n";
        pdf << "stream\n";
        pdf << contenidoStr;
        pdf << "endstream\n";
        pdf << "endobj\n\n";

        // Fuentes
        pdf << "5 0 obj\n";
        pdf << "<<\n";
        pdf << "/Type /Font\n";
        pdf << "/Subtype /Type1\n";
        pdf << "/BaseFont /Helvetica\n";
        pdf << ">>\n";
        pdf << "endobj\n\n";

        pdf << "6 0 obj\n";
        pdf << "<<\n";
        pdf << "/Type /Font\n";
        pdf << "/Subtype /Type1\n";
        pdf << "/BaseFont /Helvetica-Bold\n";
        pdf << ">>\n";
        pdf << "endobj\n\n";

        // Tabla xref
        long xrefPos = pdf.tellp();
        pdf << "xref\n";
        pdf << "0 7\n";
        pdf << "0000000000 65535 f \n";
        pdf << "0000000009 65535 n \n";
        pdf << "0000000074 65535 n \n";
        pdf << "0000000120 65535 n \n";
        pdf << "0000000274 65535 n \n";
        pdf << "0000000000 65535 n \n";
        pdf << "0000000000 65535 n \n";

        // Trailer
        pdf << "trailer\n";
        pdf << "<<\n";
        pdf << "/Size 7\n";
        pdf << "/Root 1 0 R\n";
        pdf << ">>\n";
        pdf << "startxref\n";
        pdf << xrefPos << "\n";
        pdf << "%%EOF\n";

        pdf.close();
        cn.cerrar_conexion();

        cout << "\nFACTURA PDF GENERADA\n";
        cout << "PDF generado exitosamente: " << nombreArchivoPDF << endl;

        // Intentar abrir el PDF automaticamente
        string comando = "start \"\" \"" + nombreArchivoPDF + "\"";
        int resultado_comando = system(comando.c_str());

        if (resultado_comando == 0) {
            cout << "PDF abierto automaticamente.\n";
        } else {
            cout << "PDF guardado en la carpeta 'facturas'.\n";
            cout << "Puede abrir manualmente el archivo: " << nombreArchivoPDF << "\n";
        }

    } catch (const exception& e) {
        cout << "Error inesperado al generar PDF: " << e.what() << "\n";
        cn.cerrar_conexion();
    }
}