#pragma once
#include <iostream>
#include <mysql.h>
#include <string>
#include "ConexionBD.h"

using namespace std;

class Cliente {
    // Atributos
private:
    int idCliente = 0;
    string nombres;
    string apellidos;
    string NIT;
    bool genero = false;
    string telefono;
    string correo_electronico;
    string fecha_ingreso;

    // Constructor
public:
    Cliente() {}
    Cliente(int id, string nom, string ape, string nit, bool gen, string tel, string email, string fi) {
        idCliente = id;
        nombres = nom;
        apellidos = ape;
        NIT = nit;
        genero = gen;
        telefono = tel;
        correo_electronico = email;
        fecha_ingreso = fi;
    }

    // Métodos get y set
    void setIdCliente(int id) { idCliente = id; }
    void setNombres(string nom) { nombres = nom; }
    void setApellidos(string ape) { apellidos = ape; }
    void setNIT(string nit) { NIT = nit; }
    void setGenero(bool gen) { genero = gen; }
    void setTelefono(string tel) { telefono = tel; }
    void setCorreoElectronico(string email) { correo_electronico = email; }
    void setFechaIngreso(string fi) { fecha_ingreso = fi; }

    int getIdCliente() { return idCliente; }
    string getNombres() { return nombres; }
    string getApellidos() { return apellidos; }
    string getNIT() { return NIT; }
    bool getGenero() { return genero; }
    string getTelefono() { return telefono; }
    string getCorreoElectronico() { return correo_electronico; }
    string getFechaIngreso() { return fecha_ingreso; }

    // Métodos CRUD
    void crear() {
        int q_estado = 0;
        ConexionBD cn = ConexionBD();
        cn.abrir_conexion();
        if (cn.getConector()) {
            string gen = genero ? "1" : "0";

            // Modificamos la consulta para usar AUTO_INCREMENT
            string consulta = "INSERT INTO Clientes(nombres, apellidos, NIT, genero, telefono, correo_electronico, fecha_ingreso) VALUES ('" + nombres + "', '" + apellidos + "', '" + NIT + "', " + gen + ", '" + telefono + "', '" + correo_electronico + "', '" + fecha_ingreso + "');";
            const char* c = consulta.c_str();
            q_estado = mysql_query(cn.getConector(), c);
            if (!q_estado) {
                cout << "Ingreso de Cliente Exitoso..." << endl;
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
    }

    void leer() {
        int q_estado = 0;
        ConexionBD cn = ConexionBD();
        MYSQL_ROW fila;
        MYSQL_RES* resultado;
        cn.abrir_conexion();
        if (cn.getConector()) {
            string consulta = "SELECT idCliente, nombres, apellidos, NIT, IF(genero=1,'Masculino','Femenino') as genero, telefono, correo_electronico, fecha_ingreso FROM Clientes;";
            const char* c = consulta.c_str();
            q_estado = mysql_query(cn.getConector(), c);
            if (!q_estado) {
                resultado = mysql_store_result(cn.getConector());
                cout << "---------------------------------------- CLIENTES ----------------------------------------" << endl;
                cout << "ID | NOMBRES | APELLIDOS | NIT | GÉNERO | TELÉFONO | CORREO | FECHA INGRESO" << endl;
                cout << "--------------------------------------------------------------------------------------" << endl;
                while (fila = mysql_fetch_row(resultado)) {
                    cout << fila[0] << " | " << fila[1] << " | " << fila[2] << " | " << fila[3] << " | " << fila[4] << " | " << fila[5] << " | " << fila[6] << " | " << fila[7] << endl;
                }
                cout << "--------------------------------------------------------------------------------------" << endl;
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
            string id = to_string(idCliente);
            string gen = genero ? "1" : "0";

            string consulta = "UPDATE Clientes SET nombres = '" + nombres + "', apellidos = '" + apellidos + "', NIT = '" + NIT + "', genero = " + gen + ", telefono = '" + telefono + "', correo_electronico = '" + correo_electronico + "', fecha_ingreso = '" + fecha_ingreso + "' WHERE idCliente = " + id + ";";
            const char* c = consulta.c_str();
            q_estado = mysql_query(cn.getConector(), c);
            if (!q_estado) {
                cout << "Actualización de Cliente Exitosa..." << endl;
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
            string id = to_string(idCliente);
            string consulta = "DELETE FROM Clientes WHERE idCliente = " + id + ";";
            const char* c = consulta.c_str();
            q_estado = mysql_query(cn.getConector(), c);
            if (!q_estado) {
                cout << "Eliminación de Cliente Exitosa..." << endl;
            }
            else {
                cout << "xxx Error al eliminar información xxx" << endl;
                cout << "Puede que este registro esté siendo utilizado en otra tabla" << endl;
                cout << consulta << endl;
            }
        }
        else {
            cout << "xxx Error en la conexión xxx" << endl;
        }
        cn.cerrar_conexion();
    }
};
