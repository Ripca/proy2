#pragma once
#include <iostream>
#include <mysql.h>
#include <string>
#include <vector>
#include "ConexionBD.h"

using namespace std;

// Estructura para detalles de compra
struct DetalleCompra {
    int idcompra_detalle;
    int idproducto;
    int cantidad;
    double precio_unitario;
};

class Compra {
    // Atributos
private:
    int idcompra = 0;
    int no_order_compra = 0;
    int idproveedor = 0;
    string fecha_order;
    string fecha_ingreso;
    vector<DetalleCompra> detalles;

    // Constructor
public:
    Compra() {}
    Compra(int id, int noc, int idp, string fo, string fi) {
        idcompra = id;
        no_order_compra = noc;
        idproveedor = idp;
        fecha_order = fo;
        fecha_ingreso = fi;
    }

    // Métodos get y set
    void setIdCompra(int id) { idcompra = id; }
    void setNoOrderCompra(int noc) { no_order_compra = noc; }
    void setIdProveedor(int idp) { idproveedor = idp; }
    void setFechaOrder(string fo) { fecha_order = fo; }
    void setFechaIngreso(string fi) { fecha_ingreso = fi; }
    void agregarDetalle(DetalleCompra detalle) { detalles.push_back(detalle); }
    void limpiarDetalles() { detalles.clear(); }

    int getIdCompra() { return idcompra; }
    int getNoOrderCompra() { return no_order_compra; }
    int getIdProveedor() { return idproveedor; }
    string getFechaOrder() { return fecha_order; }
    string getFechaIngreso() { return fecha_ingreso; }
    vector<DetalleCompra> getDetalles() { return detalles; }

    // Métodos CRUD
    int crear() {
        int q_estado = 0;
        int idCompraCreada = 0;
        ConexionBD cn = ConexionBD();
        cn.abrir_conexion();
        if (cn.getConector()) {
            string noc = to_string(no_order_compra);
            string idp = to_string(idproveedor);

            // Modificamos la consulta para usar AUTO_INCREMENT
            string consulta = "INSERT INTO Compras(no_order_compra, idproveedor, fecha_order, fecha_ingreso) VALUES (" + noc + ", " + idp + ", '" + fecha_order + "', '" + fecha_ingreso + "');";
            const char* c = consulta.c_str();
            q_estado = mysql_query(cn.getConector(), c);
            if (!q_estado) {
                cout << "Ingreso de Compra Exitoso..." << endl;

                // Obtener el ID de la compra recién insertada
                q_estado = mysql_query(cn.getConector(), "SELECT LAST_INSERT_ID();");
                if (!q_estado) {
                    MYSQL_RES* resultado = mysql_store_result(cn.getConector());
                    MYSQL_ROW fila = mysql_fetch_row(resultado);
                    string id = fila[0];
                    idCompraCreada = atoi(id.c_str());

                    // Insertar detalles de compra
                    for (DetalleCompra detalle : detalles) {
                        string idprod = to_string(detalle.idproducto);
                        string cant = to_string(detalle.cantidad);
                        string pu = to_string(detalle.precio_unitario);

                        // Modificamos la consulta para usar AUTO_INCREMENT
                        string consultaDetalle = "INSERT INTO Compras_detalle(idcompra, idproducto, cantidad, precio_unitario) VALUES (" + id + ", " + idprod + ", " + cant + ", " + pu + ");";
                        const char* cd = consultaDetalle.c_str();
                        q_estado = mysql_query(cn.getConector(), cd);
                        if (q_estado) {
                            cout << "xxx Error al ingresar detalle de compra xxx" << endl;
                            cout << consultaDetalle << endl;
                        }
                    }

                    mysql_free_result(resultado);
                }
                else {
                    cout << "xxx Error al obtener ID de la compra xxx" << endl;
                }
            }
            else {
                cout << "xxx Error al ingresar información xxx" << endl;
                cout << consulta << endl;
            }
        }
        else {
            cout << "xxx Error en la conexión xxx" << endl;
        }
        cn.cerrar_conexion();
        return idCompraCreada;
    }

    void leer() {
        int q_estado = 0;
        ConexionBD cn = ConexionBD();
        MYSQL_ROW fila;
        MYSQL_RES* resultado;
        cn.abrir_conexion();
        if (cn.getConector()) {
            string consulta = "SELECT c.idcompra, c.no_order_compra, p.proveedor, c.fecha_order, c.fecha_ingreso FROM Compras AS c INNER JOIN Proveedores AS p ON c.idproveedor = p.idProveedor;";
            const char* c = consulta.c_str();
            q_estado = mysql_query(cn.getConector(), c);
            if (!q_estado) {
                resultado = mysql_store_result(cn.getConector());
                cout << "------------------------- COMPRAS -------------------------" << endl;
                cout << "ID | NO. ORDEN | PROVEEDOR | FECHA ORDEN | FECHA INGRESO" << endl;
                cout << "-----------------------------------------------------------" << endl;
                while (fila = mysql_fetch_row(resultado)) {
                    cout << fila[0] << " | " << fila[1] << " | " << fila[2] << " | " << fila[3] << " | " << fila[4] << endl;
                }
                cout << "-----------------------------------------------------------" << endl;
            }
            else {
                cout << "xxx Error al consultar información xxx" << endl;
            }
        }
        else {
            cout << "xxx Error en la conexión xxx" << endl;
        }
        cn.cerrar_conexion();
    }

    void leerDetalles(int idCompra) {
        int q_estado = 0;
        ConexionBD cn = ConexionBD();
        MYSQL_ROW fila;
        MYSQL_RES* resultado;
        cn.abrir_conexion();
        if (cn.getConector()) {
            string id = to_string(idCompra);
            string consulta = "SELECT cd.idcompra_detalle, p.producto, cd.cantidad, cd.precio_unitario, (cd.cantidad * cd.precio_unitario) as subtotal FROM Compras_detalle AS cd INNER JOIN Productos AS p ON cd.idproducto = p.idProducto WHERE cd.idcompra = " + id + ";";
            const char* c = consulta.c_str();
            q_estado = mysql_query(cn.getConector(), c);
            if (!q_estado) {
                resultado = mysql_store_result(cn.getConector());
                cout << "------------------------- DETALLES DE COMPRA -------------------------" << endl;
                cout << "ID | PRODUCTO | CANTIDAD | PRECIO UNITARIO | SUBTOTAL" << endl;
                cout << "-------------------------------------------------------------------" << endl;
                double total = 0;
                while (fila = mysql_fetch_row(resultado)) {
                    cout << fila[0] << " | " << fila[1] << " | " << fila[2] << " | " << fila[3] << " | " << fila[4] << endl;
                    total += atof(fila[4]);
                }
                cout << "-------------------------------------------------------------------" << endl;
                cout << "TOTAL: " << total << endl;
                cout << "-------------------------------------------------------------------" << endl;
            }
            else {
                cout << "xxx Error al consultar información xxx" << endl;
            }
        }
        else {
            cout << "xxx Error en la conexión xxx" << endl;
        }
        cn.cerrar_conexion();
    }

    void actualizar() {
        int q_estado = 0;
        ConexionBD cn = ConexionBD();
        cn.abrir_conexion();
        if (cn.getConector()) {
            string id = to_string(idcompra);
            string noc = to_string(no_order_compra);
            string idp = to_string(idproveedor);

            string consulta = "UPDATE Compras SET no_order_compra = " + noc + ", idproveedor = " + idp + ", fecha_order = '" + fecha_order + "', fecha_ingreso = '" + fecha_ingreso + "' WHERE idcompra = " + id + ";";
            const char* c = consulta.c_str();
            q_estado = mysql_query(cn.getConector(), c);
            if (!q_estado) {
                cout << "Actualización de Compra Exitosa..." << endl;
            }
            else {
                cout << "xxx Error al actualizar información xxx" << endl;
                cout << consulta << endl;
            }
        }
        else {
            cout << "xxx Error en la conexión xxx" << endl;
        }
        cn.cerrar_conexion();
    }

    void eliminar() {
        int q_estado = 0;
        ConexionBD cn = ConexionBD();
        cn.abrir_conexion();
        if (cn.getConector()) {
            string id = to_string(idcompra);

            // Primero eliminar los detalles
            string consultaDetalles = "DELETE FROM Compras_detalle WHERE idcompra = " + id + ";";
            const char* cd = consultaDetalles.c_str();
            q_estado = mysql_query(cn.getConector(), cd);
            if (!q_estado) {
                // Luego eliminar la compra
                string consulta = "DELETE FROM Compras WHERE idcompra = " + id + ";";
                const char* c = consulta.c_str();
                q_estado = mysql_query(cn.getConector(), c);
                if (!q_estado) {
                    cout << "Eliminación de Compra Exitosa..." << endl;
                }
                else {
                    cout << "xxx Error al eliminar compra xxx" << endl;
                    cout << consulta << endl;
                }
            }
            else {
                cout << "xxx Error al eliminar detalles de compra xxx" << endl;
                cout << consultaDetalles << endl;
            }
        }
        else {
            cout << "xxx Error en la conexión xxx" << endl;
        }
        cn.cerrar_conexion();
    }
};
