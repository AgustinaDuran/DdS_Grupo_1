@RestController
@RequestMapping("/api/notificaciones")
@CrossOrigin(origins = "*")
public class NotificadorController{
    private final Notificador notificador;

     //http://localhost:8080/api/notificaciones -> POST
    @PostMapping
    public ResponseEntity<String> recibirNotificacion(
        @RequestBody CrearNotificacionDTO notificacionDTO) {
        
        notificador.notificar(notificacionDTO);

        return ResponseEntity.ok("Notificacion enviada correctamente");
    }
}