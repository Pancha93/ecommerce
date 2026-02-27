package SistemadeGestiondeOrdenes.entity.services.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import SistemadeGestiondeOrdenes.entity.entities.ConfiguracionEmail;
import SistemadeGestiondeOrdenes.entity.repositories.ConfiguracionEmailRepository;
import java.util.Optional;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.MessagingException;
import java.util.Properties;

@Service
public class ConfiguracionEmailServiceImpl {

    @Autowired
    private ConfiguracionEmailRepository repository;

    public Optional<ConfiguracionEmail> getConfiguration() {
        return repository.findAll().stream().findFirst();
    }

    public ConfiguracionEmail updateConfiguration(ConfiguracionEmail config) {
        return repository.save(config);
    }

    public void testSMTPConnection(ConfiguracionEmail config) throws MessagingException {
        Properties props = new Properties();
        props.put("mail.smtp.auth", "true");
        props.put("mail.smtp.starttls.enable", "true");
        props.put("mail.smtp.host", config.getHost());
        props.put("mail.smtp.port", config.getPort().toString());
        props.put("mail.smtp.ssl.protocols", "TLSv1.2");
        props.put("mail.smtp.ssl.trust", config.getHost());
        Session session = Session.getInstance(props, new javax.mail.Authenticator() {
            protected javax.mail.PasswordAuthentication getPasswordAuthentication() {
                return new javax.mail.PasswordAuthentication(config.getUsername(), config.getPassword());
            }
        });
        session.setDebug(true);
        Transport transport = session.getTransport("smtp");
        transport.connect(config.getHost(), config.getUsername(), config.getPassword());
        transport.close();
    }

}
