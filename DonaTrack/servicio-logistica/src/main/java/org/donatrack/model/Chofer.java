package org.donatrack.model;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.DocumentReference;

@Document(collection = "choferes")
public class Chofer {

    @Id
    private String id;

    @DocumentReference
    private Camion camion;

    public Chofer() {}

    public Chofer(Camion camion) { this.camion = camion; }

    public String getId() { return id; }
    public void setId(String id) { this.id = id; }
    public Camion getCamion() { return camion; }
    public void setCamion(Camion camion) { this.camion = camion; }
}