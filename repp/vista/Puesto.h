#pragma once
#include <iostream>
#include <mysql.h>
#include <string>
#include "ConexionBD.h"

using namespace std;

class Puesto {
    // Atributos
private:
    short idPuesto = 0;
    string puesto;

    // Constructor
public:
    Puesto() {}
    Puesto(short id, string p) {
        idPuesto = id;
        puesto = p;
    }

    // Métodos get y set
    void setIdPuesto(short id) { idPuesto = id; }
    void setPuesto(string p) { puesto = p; }

    short getIdPuesto() { return idPuesto; }
    string getPuesto() { return puesto; }

    // Métodos CRUD
    void crear() {
        int q_estado = 0;
        ConexionBD cn = ConexionBD();
        cn.abrir_conexion();
        if (cn.getConector()) {
            // Modificamos la consulta para usar AUTO_INCREMENT
            string consulta = "INSERT INTO Puestos(puesto) VALUES ('" + puesto + "');";
            const char* c = consulta.c_str();
            q_estado = mysql_query(cn.getConector(), c);
            if (!q_estado) {
                cout << "Ingreso de Puesto Exitoso..." << endl;
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
            string consulta = "SELECT * FROM Puestos;";
            const char* c = consulta.c_str();
            q_estado = mysql_query(cn.getConector(), c);
            if (!q_estado) {
                resultado = mysql_store_result(cn.getConector());
                cout << "------------------- PUESTOS -------------------" << endl;
                cout << "ID | PUESTO" << endl;
                cout << "----------------------------------------------" << endl;
                while (fila = mysql_fetch_row(resultado)) {
                    cout << fila[0] << " | " << fila[1] << endl;
                }
                cout << "----------------------------------------------" << endl;
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
            string id = to_string(idPuesto);
            string consulta = "UPDATE Puestos SET puesto = '" + puesto + "' WHERE idPuesto = " + id + ";";
            const char* c = consulta.c_str();
            q_estado = mysql_query(cn.getConector(), c);
            if (!q_estado) {
                cout << "Actualización de Puesto Exitosa..." << endl;
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
            string id = to_string(idPuesto);
            string consulta = "DELETE FROM Puestos WHERE idPuesto = " + id + ";";
            const char* c = consulta.c_str();
            q_estado = mysql_query(cn.getConector(), c);
            if (!q_estado) {
                cout << "Eliminación de Puesto Exitosa..." << endl;
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
