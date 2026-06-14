/* package org.donatrack.dominio;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class ServiceUsuario {
    private List<Donante> donantes = new ArrayList<Donante>();

    public void agregarDonante(Donante donante) {
        donantes.add(donante);
    }

    public void darBajaDonante(Donante donante) {
        donantes.remove(donante);
    }

    public void importarDonantesMasivo(String rutaArchivoCsv) {
        String linea = "";
        String separador = ",";

        try (BufferedReader br = new BufferedReader(new FileReader(rutaArchivoCsv))) {
            br.readLine();

            while ((linea = br.readLine()) != null) {
                String[] datos = linea.split(separador);

                String tipoPersona = datos[0];
                // String tipoDoc = datos[1];                
                String documento = datos[2].replace(".", "").replace("-", "");
                String nombreRazonSocial = datos[3];
                String email = datos[4];
                String telefono = datos[5];

                Donante donanteExistente = null;

                for (Donante donante : donantes) {
                    for (Contacto contacto : donante.getContactos()) {
                        if (contacto instanceof Mail && ((Mail) contacto).getDireccionMail().equals(email)) {
                            donanteExistente = donante;
                            break;
                        }
                    }
                    if (donanteExistente != null) break;
                }

                if (donanteExistente != null) {
                    if (donanteExistente instanceof DonantePersona) {
                        DonantePersona humana = (DonantePersona) donanteExistente;

                        String[] partesNombre = nombreRazonSocial.split(" ", 2);
                        humana.setNombre(partesNombre[0]);
                        humana.setApellido(partesNombre[1]);
                        humana.setNroDocumento(documento);

                    } else if (donanteExistente instanceof DonanteJuridico) {
                        DonanteJuridico juridica = (DonanteJuridico) donanteExistente;
                        juridica.setRazonSocial(nombreRazonSocial);
                    }
                }

                else {
                    Donante nuevoDonante = null;

                    Mail nuevoMail = new Mail(email);
                    Telefono nuevoTel = new Telefono(telefono);

                    if (tipoPersona.equals("HUMANA")) {
                       
                        String[] partesNombre = nombreRazonSocial.split(" ", 2);
                        String nombre = partesNombre[0];
                        String apellido = partesNombre[1];
                        //documento tomado del principio del while
                        DonantePersona pHumana = new DonantePersona(nombre, apellido, documento, nuevoMail, nuevoTel);
                        nuevoDonante = pHumana;

                    } else if (tipoPersona.equals("JURIDICA")) {
                  
                        DonanteJuridico pJuridica = new DonanteJuridico(nombreRazonSocial, documento, nuevoMail,nuevoTel);                          nuevoDonante = pJuridica;
                    }

                    if (nuevoDonante != null) {
                        this.donantes.add(nuevoDonante);
                    }
                }
            }
        } catch (IOException e) {
            System.err.println("Error al acceder al archivo CSV: " + e.getMessage());
        } catch (NumberFormatException e) {
            System.err.println("Error procesando un número de documento: " + e.getMessage());
        }
    }


} */
