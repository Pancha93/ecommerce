package SistemadeGestiondeOrdenes.entity.services.impl;

import com.fasterxml.jackson.databind.JsonNode;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSenderImpl;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import SistemadeGestiondeOrdenes.entity.entities.ConfiguracionEmail;
import java.util.Properties;
import java.util.concurrent.CompletableFuture;

@Service
public class NotificacionEmailServiceImpl {

    @Autowired
    private ConfiguracionEmailServiceImpl emailConfigService;

    @Async
    public CompletableFuture<String> enviarNotificacion(JsonNode info) {

        // 1. Obtener la configuración almacenada
        ConfiguracionEmail config = emailConfigService
                .getConfiguration()
                .orElseThrow(() -> new RuntimeException("No se encontró configuración de email"));

        // 2. Configurar el JavaMailSender
        JavaMailSenderImpl mailSender = new JavaMailSenderImpl();
        mailSender.setHost(config.getHost());
        mailSender.setPort(config.getPort());
        mailSender.setUsername(config.getUsername());
        mailSender.setPassword(config.getPassword());
        Properties props = mailSender.getJavaMailProperties();
        props.put("mail.smtp.auth", "true");
        props.put("mail.smtp.starttls.enable", "true");
        props.put("mail.smtp.ssl.trust", "*");

        // 3. Determinar si hay mensaje y/o destinatario
        String destinatario = (info != null && info.has("To"))
                ? info.get("To").asText()
                : null;
        if (destinatario == null || destinatario.trim().isEmpty()) {
            destinatario = config.getUsername();
        }

        boolean tieneMensaje = (info != null && info.has("Message") && !info.get("Message").asText().trim().isEmpty());

        // 4. Construir el body y subject
        String subject;
        String cuerpo;

        if (!tieneMensaje) {
            subject = "Correo de prueba exitoso";
            cuerpo = "Este es un mensaje de prueba.\n" +
                     "Enviado exitosamente 200 OK";
        } else {
            subject = (info.has("Subject") && !info.get("Subject").asText().trim().isEmpty())
                    ? info.get("Subject").asText()
                    : "Notificación de Cliente";
            cuerpo = info.get("Message").asText();
        }

        // 5. Armar y enviar el correo
        SimpleMailMessage message = new SimpleMailMessage();
        message.setFrom(config.getUsername());
        message.setTo(destinatario);
        message.setSubject(subject);
        message.setText(cuerpo);

        try {
            mailSender.send(message);
            return CompletableFuture.completedFuture(
                    "Correo enviado correctamente a " + destinatario
            );
        } catch (Exception e) {
            throw new RuntimeException("No se pudo enviar el correo: " + e.getMessage(), e);
        }
    }

}
